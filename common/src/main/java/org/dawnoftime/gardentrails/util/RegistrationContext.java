package org.dawnoftime.gardentrails.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.dawnoftime.gardentrails.GTCommon;

import java.util.function.Supplier;

/**
 * Since 1.21.2 the properties of a block or an item must know their registry key before the object is built.
 * The loader registries run every block or item supplier through one of the methods below, and the
 * properties factories (copy, item) read the key of the object being registered.
 */
public final class RegistrationContext {
    private enum Kind {BLOCK, ITEM, BLOCK_ITEM}

    private record Context(Kind kind, Identifier id) {
    }

    private static final ThreadLocal<Context> CURRENT = new ThreadLocal<>();

    private RegistrationContext() {
    }

    /** Runs a supplier that builds the block registered under this path. */
    public static <T> T block(String path, Supplier<T> supplier) {
        return run(new Context(Kind.BLOCK, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, path)), supplier);
    }

    /** Runs a supplier that builds an item with no block, registered under this path. */
    public static <T> T item(String path, Supplier<T> supplier) {
        return run(new Context(Kind.ITEM, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, path)), supplier);
    }

    /** Runs a supplier that builds the item of a block: its name comes from the block translation key. */
    public static <T> T blockItem(String path, Supplier<T> supplier) {
        return run(new Context(Kind.BLOCK_ITEM, Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, path)), supplier);
    }

    /** Same as {@link BlockBehaviour.Properties#ofFullCopy(BlockBehaviour)} with the key of the block being registered. */
    public static BlockBehaviour.Properties copy(BlockBehaviour source) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(source);
        Context context = CURRENT.get();
        if (context != null && context.kind() == Kind.BLOCK) {
            properties.setId(ResourceKey.create(Registries.BLOCK, context.id()));
        }
        return properties;
    }

    /** New item properties with the key of the item being registered. */
    public static Item.Properties item() {
        Item.Properties properties = new Item.Properties();
        Context context = CURRENT.get();
        if (context != null && context.kind() != Kind.BLOCK) {
            properties.setId(ResourceKey.create(Registries.ITEM, context.id()));
            if (context.kind() == Kind.BLOCK_ITEM) {
                properties.useBlockDescriptionPrefix();
            }
        }
        return properties;
    }

    private static <T> T run(Context context, Supplier<T> supplier) {
        Context previous = CURRENT.get();
        CURRENT.set(context);
        try {
            return supplier.get();
        } finally {
            if (previous == null) {
                CURRENT.remove();
            } else {
                CURRENT.set(previous);
            }
        }
    }
}
