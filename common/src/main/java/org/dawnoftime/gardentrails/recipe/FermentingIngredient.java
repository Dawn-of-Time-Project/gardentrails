package org.dawnoftime.gardentrails.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * One ingredient of a fermenting recipe: an item or an item tag, with an optional partial NBT match
 * and an optional remainder given back to the player when the ingredient is poured in the vat.
 */
public class FermentingIngredient {
    @Nullable
    private final Item item;
    @Nullable
    private final TagKey<Item> tag;
    @Nullable
    private final String nbtText;
    @Nullable
    private final CompoundTag nbt;
    private final ItemStack remainder;

    private FermentingIngredient(@Nullable Item item, @Nullable TagKey<Item> tag, @Nullable String nbtText, ItemStack remainder) {
        this.item = item;
        this.tag = tag;
        this.nbtText = nbtText;
        this.remainder = remainder;
        try {
            this.nbt = nbtText == null ? null : TagParser.parseTag(nbtText);
        } catch (Exception e) {
            throw new JsonSyntaxException("Invalid nbt for fermenting ingredient: " + nbtText, e);
        }
    }

    public boolean test(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (this.item != null && !stack.is(this.item)) {
            return false;
        }
        if (this.tag != null && !stack.is(this.tag)) {
            return false;
        }
        return this.nbt == null || NbtUtils.compareNbt(this.nbt, stack.getTag(), true);
    }

    /** Stacks shown in recipe viewers: the item with its nbt, or every item of the tag. */
    public List<ItemStack> getDisplayStacks() {
        List<Item> items = this.item != null
                ? List.of(this.item)
                : BuiltInRegistries.ITEM.getTag(this.tag).map(set -> set.stream().map(holder -> holder.value()).toList()).orElse(List.of());
        return items.stream().map(i -> {
            ItemStack stack = new ItemStack(i);
            if (this.nbt != null) {
                stack.setTag(this.nbt.copy());
            }
            return stack;
        }).toList();
    }

    public ItemStack getRemainder() {
        return this.remainder.copy();
    }

    public static FermentingIngredient fromJson(JsonObject json) {
        Item item = null;
        TagKey<Item> tag = null;
        if (json.has("item")) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "item"));
            item = BuiltInRegistries.ITEM.getOptional(id).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + id + "'"));
        } else if (json.has("tag")) {
            tag = TagKey.create(Registries.ITEM, new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        } else {
            throw new JsonSyntaxException("A fermenting ingredient needs an 'item' or a 'tag'.");
        }
        String nbtText = json.has("nbt") ? GsonHelper.getAsString(json, "nbt") : null;
        ItemStack remainder = ItemStack.EMPTY;
        if (json.has("remainder")) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "remainder"));
            remainder = new ItemStack(BuiltInRegistries.ITEM.getOptional(id).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + id + "'")));
        }
        return new FermentingIngredient(item, tag, nbtText, remainder);
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.item != null);
        if (this.item != null) {
            buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(this.item));
        } else {
            buffer.writeResourceLocation(this.tag.location());
        }
        buffer.writeBoolean(this.nbtText != null);
        if (this.nbtText != null) {
            buffer.writeUtf(this.nbtText);
        }
        buffer.writeBoolean(!this.remainder.isEmpty());
        if (!this.remainder.isEmpty()) {
            buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(this.remainder.getItem()));
        }
    }

    public static FermentingIngredient fromNetwork(FriendlyByteBuf buffer) {
        boolean isItem = buffer.readBoolean();
        ResourceLocation id = buffer.readResourceLocation();
        Item item = isItem ? BuiltInRegistries.ITEM.get(id) : null;
        TagKey<Item> tag = isItem ? null : TagKey.create(Registries.ITEM, id);
        String nbtText = buffer.readBoolean() ? buffer.readUtf() : null;
        ItemStack remainder = buffer.readBoolean() ? new ItemStack(BuiltInRegistries.ITEM.get(buffer.readResourceLocation())) : ItemStack.EMPTY;
        return new FermentingIngredient(item, tag, nbtText, remainder);
    }
}
