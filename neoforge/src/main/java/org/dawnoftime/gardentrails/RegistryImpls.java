package org.dawnoftime.gardentrails;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
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
    public static class NeoForgeBlockEntitiesRegistry extends GTBlockEntitiesRegistry {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES_REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GTCommon.MOD_ID);

        @Override
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier) {
            return BLOCK_ENTITY_TYPES_REGISTRY.register(name, () -> BlockEntityType.Builder.of(factoryIn::apply, validBlocksSupplier.get()).build(null));
        }
    }

    public static class NeoForgeBlocksRegistry extends GTBlocksRegistry {
        public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(Registries.BLOCK, GTCommon.MOD_ID);
        public static final DeferredRegister<Item> BLOCK_ITEMS_REGISTRY = DeferredRegister.create(Registries.ITEM, GTCommon.MOD_ID);
        @SafeVarargs
        @Override
        public final <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags) {
            DeferredHolder<Block, T> registryBlock = BLOCKS_REGISTRY.register(id, block);
            if(item != null) {
                BLOCK_ITEMS_REGISTRY.register(id, () -> item.apply(registryBlock.get()));
            }
            return registryBlock;
        }

        @Override
        public <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String blockID, Supplier<T> block, String itemID, Function<T, Y> item, @Nullable IBiomeColoredBlock.ColorType potColor) {
            DeferredHolder<Block, T> registryBlock = BLOCKS_REGISTRY.register(blockID, block);
            if (item != null) {
                BLOCK_ITEMS_REGISTRY.register(itemID, () -> item.apply(registryBlock.get()));
            }
            this.registerPot(blockID, registryBlock, potColor);
            return registryBlock;
        }
    }

    public static class NeoForgeEntitiesRegistry extends GTEntitiesRegistry {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES_REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, GTCommon.MOD_ID);
        @Override
        public <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
            return ENTITY_TYPES_REGISTRY.register(name, () -> builder.get().build(name));
        }
    }

    public static class NeoForgeMobEffectsRegistry extends GTMobEffectsRegistry {
        public static final DeferredRegister<MobEffect> MOB_EFFECTS_REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, GTCommon.MOD_ID);

        @Override
        public Supplier<Holder<MobEffect>> register(String name, Supplier<MobEffect> effect) {
            DeferredHolder<MobEffect, MobEffect> holder = MOB_EFFECTS_REGISTRY.register(name, effect);
            return () -> holder;
        }
    }

    public static class NeoForgeFeaturesRegistry extends GTFeaturesRegistry {
        public static final DeferredRegister<Feature<?>> FEATURES_REGISTRY = DeferredRegister.create(Registries.FEATURE, GTCommon.MOD_ID);

        @Override
        public <Y extends FeatureConfiguration, T extends Feature<Y>> Supplier<T> register(String name, Supplier<T> featureSupplier) {
            return FEATURES_REGISTRY.register(name, featureSupplier);
        }
    }

    public static class NeoForgeItemsRegistry extends GTItemsRegistry {
        public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(Registries.ITEM, GTCommon.MOD_ID);

        @Override
        protected Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
            return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
        }

        @Override
        public <T extends Item> Supplier<Item> register(String name, Supplier<T> itemSupplier) {
            return ITEMS_REGISTRY.register(name, itemSupplier);
        }

    }

    public static class NeoForgeRecipeSerializersRegistry extends GTRecipeSerializersRegistry {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS_REGISTRY = DeferredRegister.create(Registries.RECIPE_SERIALIZER, GTCommon.MOD_ID);

        @Override
        public <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer) {
            return RECIPE_SERIALIZERS_REGISTRY.register(name, recipeSerializer);
        }
    }

    public static class NeoForgeRecipeTypesRegistry extends GTRecipeTypesRegistry {
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES_REGISTRY = DeferredRegister.create(Registries.RECIPE_TYPE, GTCommon.MOD_ID);

        @Override
        public <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
            return RECIPE_TYPES_REGISTRY.register(name, () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(GTCommon.MOD_ID, name)));
        }
    }

    public static class NeoForgeCreativeModeTabsRegistry extends GTCreativeModeTabsRegistry {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GTCommon.MOD_ID);

        @Override
        public <T extends CreativeModeTab> Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, Component title) {
            return CREATIVE_MODE_TABS_REGISTRY.register(name, () -> CreativeModeTab.builder().icon(iconSupplier).title(title).build());
        }
    }

    public static class NeoForgeTagsRegistry extends GTTags {
        @Override
        public TagKey<Block> registerBlock(ResourceLocation id) {
            return TagKey.create(Registries.BLOCK, id);
        }

        @Override
        public TagKey<Item> registerItem(ResourceLocation id) {
            return TagKey.create(Registries.ITEM, id);
        }
    }

    public static void init(IEventBus bus) {
        GTEntitiesRegistry.INSTANCE = new NeoForgeEntitiesRegistry();
        NeoForgeEntitiesRegistry.ENTITY_TYPES_REGISTRY.register(bus);

        GTMobEffectsRegistry.INSTANCE = new NeoForgeMobEffectsRegistry();
        NeoForgeMobEffectsRegistry.MOB_EFFECTS_REGISTRY.register(bus);

        GTBlocksRegistry.INSTANCE = new NeoForgeBlocksRegistry();
        GTBlocksRegistry.INSTANCE.registerAll();
        GTItemsRegistry.INSTANCE = new NeoForgeItemsRegistry();
        GTItemsRegistry.INSTANCE.registerAll();
        GTBlockEntitiesRegistry.INSTANCE = new NeoForgeBlockEntitiesRegistry();
        GTFeaturesRegistry.INSTANCE = new NeoForgeFeaturesRegistry();
        GTRecipeSerializersRegistry.INSTANCE = new NeoForgeRecipeSerializersRegistry();
        GTRecipeTypesRegistry.INSTANCE = new NeoForgeRecipeTypesRegistry();
        GTTags.INSTANCE = new NeoForgeTagsRegistry();
        GTCreativeModeTabsRegistry.INSTANCE = new NeoForgeCreativeModeTabsRegistry();

        // Register all deffered registries
        NeoForgeBlocksRegistry.BLOCKS_REGISTRY.register(bus);
        NeoForgeBlocksRegistry.BLOCK_ITEMS_REGISTRY.register(bus);
        NeoForgeItemsRegistry.ITEMS_REGISTRY.register(bus);
        NeoForgeBlockEntitiesRegistry.BLOCK_ENTITY_TYPES_REGISTRY.register(bus);
        NeoForgeFeaturesRegistry.FEATURES_REGISTRY.register(bus);
        NeoForgeRecipeSerializersRegistry.RECIPE_SERIALIZERS_REGISTRY.register(bus);
        NeoForgeRecipeTypesRegistry.RECIPE_TYPES_REGISTRY.register(bus);
        NeoForgeCreativeModeTabsRegistry.CREATIVE_MODE_TABS_REGISTRY.register(bus);

        // The vanilla brushable block entity only accepts vanilla blocks, so the moldy log must be added to it
        bus.addListener((net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent event) -> event.modify(BlockEntityType.BRUSHABLE_BLOCK, GTBlocksRegistry.INSTANCE.ROTTEN_OAK_LOG.get()));
        bus.addListener((EntityAttributeCreationEvent event) -> event.put(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), SilkmothEntity.createAttributes().build()));
        bus.addListener((EntityAttributeCreationEvent event) -> event.put(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), QuetzalEntity.createAttributes().build()));
        bus.addListener((RegisterSpawnPlacementsEvent event) -> event.register(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, QuetzalEntity::checkQuetzalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR));
        bus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if(event.getTab() == GTCreativeModeTabsRegistry.INSTANCE.GT_TAB.get()) {
                GTCreativeModeTabsRegistry.addOrderedItems(event::accept);
            }
        });
    }
}
