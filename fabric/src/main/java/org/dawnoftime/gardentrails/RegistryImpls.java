package org.dawnoftime.gardentrails;

import org.dawnoftime.gardentrails.util.QuetzalShoulderSync;

import org.dawnoftime.gardentrails.util.RegistrationContext;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.dawnoftime.gardentrails.block.templates.LegacyPergolaBlock;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import org.dawnoftime.gardentrails.entity.QuetzalEntity;
import org.dawnoftime.gardentrails.entity.SilkmothEntity;
import org.dawnoftime.gardentrails.item.IconItem;
import org.dawnoftime.gardentrails.registry.*;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryImpls {
    public static class FabricBlockEntitiesRegistry extends GTBlockEntitiesRegistry {
        @Override
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier) {
            BlockEntityType<T> blockEntity = (BlockEntityType<T>) Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), FabricBlockEntityTypeBuilder.create((FabricBlockEntityTypeBuilder.Factory<BlockEntity>) factoryIn::apply, validBlocksSupplier.get()).build());
            return () -> blockEntity;
        }
    }

    public static class FabricBlocksRegistry extends GTBlocksRegistry {
        @SafeVarargs
        @Override
        public final <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags) {
            T registryBlock = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, id), RegistrationContext.block(id, block));
            if(item != null) {
                Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, id), RegistrationContext.blockItem(id, () -> item.apply(registryBlock)));
            }
            return () -> registryBlock;
        }

        @Override
        public <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String blockID, Supplier<T> block, String itemID, Function<T, Y> item, @Nullable IBiomeColoredBlock.ColorType potColor) {
            T toReturn = Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, blockID), RegistrationContext.block(blockID, block));
            if (item != null) {
                Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, itemID), RegistrationContext.blockItem(itemID, () -> item.apply(toReturn)));
            }
            this.registerPot(blockID, () -> toReturn, potColor);
            return () -> toReturn;
        }
    }

    public static class FabricEntitiesRegistry extends GTEntitiesRegistry {
        @Override
        public <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
            var entity = Registry.register(BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), builder.get().build(net.minecraft.resources.ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name))));
            return () -> entity;
        }
    }

    public static class FabricMobEffectsRegistry extends GTMobEffectsRegistry {
        @Override
        public Supplier<Holder<MobEffect>> register(String name, Supplier<MobEffect> effect) {
            Holder<MobEffect> registered = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), effect.get());
            return () -> registered;
        }
    }

    public static class FabricFeaturesRegistry extends GTFeaturesRegistry {
        @Override
        public <Y extends FeatureConfiguration, T extends Feature<Y>> Supplier<T> register(String name, Supplier<T> featureSupplier) {
            var feature = Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), featureSupplier.get());
            return () -> feature;
        }
    }

    public static class FabricItemsRegistry extends GTItemsRegistry {
        @Override
        protected Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, Item.Properties properties) {
            return new SpawnEggItem(properties.spawnEgg(entityType.get()));
        }

        @Override
        public <T extends Item> Supplier<Item> register(String name, Supplier<T> itemSupplier) {
            T item = Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), RegistrationContext.item(name, itemSupplier));
            return () -> item;
        }

    }

    public static class FabricRecipeSerializersRegistry extends GTRecipeSerializersRegistry {
        @Override
        public <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer) {
            var recipe = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), recipeSerializer.get());
            return () -> recipe;
        }
    }

    public static class FabricRecipeTypesRegistry extends GTRecipeTypesRegistry {
        @Override
        public <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
            RecipeType<T> type = RecipeType.register(name);
            return () -> type;
        }
    }

    public static class FabricCreativeModeTabsRegistry extends GTCreativeModeTabsRegistry {
        @Override
        public <T extends CreativeModeTab> Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, Component title) {
            var group = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, name), FabricItemGroup.builder().icon(iconSupplier).title(title).displayItems((itemDisplayParameters, output) -> {
                GTCreativeModeTabsRegistry.addOrderedItems(output::accept);
            }).build());
            return () -> group;
        }
    }

    public static class FabricTagsRegistry extends GTTags {
        @Override
        public TagKey<Block> registerBlock(Identifier id) {
            return TagKey.create(Registries.BLOCK, id);
        }

        @Override
        public TagKey<Item> registerItem(Identifier id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static class FabricQuetzalShoulderSync extends QuetzalShoulderSync {
        private static final net.fabricmc.fabric.api.attachment.v1.AttachmentType<Integer> SHOULDER = net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry.create(
                Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "quetzal_shoulder"),
                builder -> builder.initializer(() -> 0).syncWith(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate.all()));

        @Override
        public int getMask(net.minecraft.world.entity.player.Player player) {
            return player.getAttachedOrElse(SHOULDER, 0);
        }

        @Override
        public void setMask(net.minecraft.world.entity.player.Player player, int mask) {
            player.setAttached(SHOULDER, mask);
        }
    }

    public static void init() {
        QuetzalShoulderSync.INSTANCE = new FabricQuetzalShoulderSync();
        GTEntitiesRegistry.INSTANCE = new FabricEntitiesRegistry();
        GTMobEffectsRegistry.INSTANCE = new FabricMobEffectsRegistry();
        GTBlocksRegistry.INSTANCE = new FabricBlocksRegistry();
        GTBlocksRegistry.INSTANCE.registerAll();
        GTItemsRegistry.INSTANCE = new FabricItemsRegistry();
        GTItemsRegistry.INSTANCE.registerAll();
        GTBlockEntitiesRegistry.INSTANCE = new FabricBlockEntitiesRegistry();
        GTFeaturesRegistry.INSTANCE = new FabricFeaturesRegistry();
        GTRecipeSerializersRegistry.INSTANCE = new FabricRecipeSerializersRegistry();
        GTRecipeTypesRegistry.INSTANCE = new FabricRecipeTypesRegistry();
        GTTags.INSTANCE = new FabricTagsRegistry();
        GTCreativeModeTabsRegistry.INSTANCE = new FabricCreativeModeTabsRegistry();

        FabricDefaultAttributeRegistry.register(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), SilkmothEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), QuetzalEntity.createAttributes());

        GTBlocksRegistry.FLAMMABLE_BLOCKS.forEach((block, fire) -> FlammableBlockRegistry.getDefaultInstance().add(block.get(), fire.spread(), fire.destruction()));
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk) -> LegacyPergolaBlock.convertChunk(chunk));
    }
}
