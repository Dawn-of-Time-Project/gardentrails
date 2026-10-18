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
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.dawnoftime.gardentrails.block.IBiomeColoredBlock;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
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
    public static class ForgeBlockEntitiesRegistry extends GTBlockEntitiesRegistry {
        public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GTCommon.MOD_ID);

        @Override
        public <T extends BlockEntity> Supplier<BlockEntityType<T>> register(String name, BiFunction<BlockPos, BlockState, T> factoryIn, Supplier<Block[]> validBlocksSupplier) {
            return BLOCK_ENTITY_TYPES_REGISTRY.register(name, () -> BlockEntityType.Builder.of(factoryIn::apply, validBlocksSupplier.get()).build(null));
        }
    }

    public static class ForgeBlocksRegistry extends GTBlocksRegistry {
        public static final DeferredRegister<Block> BLOCKS_REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, GTCommon.MOD_ID);
        public static final DeferredRegister<Item> BLOCK_ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, GTCommon.MOD_ID);
        @SafeVarargs
        @Override
        public final <T extends Block, Y extends Item> Supplier<T> registerWithItem(String id, Supplier<T> block, Function<T, Y> item, TagKey<Block>... tags) {
            RegistryObject<T> registryBlock = BLOCKS_REGISTRY.register(id, block);
            if(item != null) {
                BLOCK_ITEMS_REGISTRY.register(id, () -> item.apply(registryBlock.get()));
            }
            return registryBlock;
        }

        @Override
        public <T extends Block, Y extends Item> Supplier<T> registerWithFlowerPotItem(String blockID, Supplier<T> block, String itemID, Function<T, Y> item, @Nullable IBiomeColoredBlock.ColorType potColor) {
            RegistryObject<T> registryBlock = BLOCKS_REGISTRY.register(blockID, block);
            if (item != null) {
                BLOCK_ITEMS_REGISTRY.register(itemID, () -> item.apply(registryBlock.get()));
            }
            this.registerPot(blockID, registryBlock, potColor);
            return registryBlock;
        }
    }

    public static class ForgeEntitiesRegistry extends GTEntitiesRegistry {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GTCommon.MOD_ID);
        @Override
        public <T extends Entity> Supplier<EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
            return ENTITY_TYPES_REGISTRY.register(name, () -> builder.get().build(name));
        }
    }

    public static class ForgeMobEffectsRegistry extends GTMobEffectsRegistry {
        public static final DeferredRegister<MobEffect> MOB_EFFECTS_REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GTCommon.MOD_ID);

        @Override
        public <T extends MobEffect> Supplier<T> register(String name, Supplier<T> effect) {
            return MOB_EFFECTS_REGISTRY.register(name, effect);
        }
    }

    public static class ForgeFeaturesRegistry extends GTFeaturesRegistry {
        public static final DeferredRegister<Feature<?>> FEATURES_REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, GTCommon.MOD_ID);

        @Override
        public <Y extends FeatureConfiguration, T extends Feature<Y>> Supplier<T> register(String name, Supplier<T> featureSupplier) {
            return FEATURES_REGISTRY.register(name, featureSupplier);
        }
    }

    public static class ForgeItemsRegistry extends GTItemsRegistry {
        public static final DeferredRegister<Item> ITEMS_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, GTCommon.MOD_ID);

        @Override
        protected Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
            return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
        }

        @Override
        public <T extends Item> Supplier<Item> register(String name, Supplier<T> itemSupplier) {
            return ITEMS_REGISTRY.register(name, itemSupplier);
        }

    }

    public static class ForgeRecipeSerializersRegistry extends GTRecipeSerializersRegistry {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS_REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GTCommon.MOD_ID);

        @Override
        public <T extends RecipeSerializer<? extends Recipe<?>>> Supplier<T> register(String name, Supplier<T> recipeSerializer) {
            return RECIPE_SERIALIZERS_REGISTRY.register(name, recipeSerializer);
        }
    }

    public static class ForgeRecipeTypesRegistry extends GTRecipeTypesRegistry {
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES_REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GTCommon.MOD_ID);

        @Override
        public <T extends Recipe<?>> Supplier<RecipeType<T>> register(String name) {
            return RECIPE_TYPES_REGISTRY.register(name, () -> RecipeType.simple(new ResourceLocation(GTCommon.MOD_ID, name)));
        }
    }

    public static class ForgeCreativeModeTabsRegistry extends GTCreativeModeTabsRegistry {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GTCommon.MOD_ID);

        @Override
        public <T extends CreativeModeTab> Supplier<CreativeModeTab> register(String name, Supplier<ItemStack> iconSupplier, Component title) {
            return CREATIVE_MODE_TABS_REGISTRY.register(name, () -> CreativeModeTab.builder().icon(iconSupplier).title(title).build());
        }
    }

    public static class ForgeTagsRegistry extends GTTags {
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
        GTEntitiesRegistry.INSTANCE = new ForgeEntitiesRegistry();
        ForgeEntitiesRegistry.ENTITY_TYPES_REGISTRY.register(bus);

        GTMobEffectsRegistry.INSTANCE = new ForgeMobEffectsRegistry();
        ForgeMobEffectsRegistry.MOB_EFFECTS_REGISTRY.register(bus);

        GTBlocksRegistry.INSTANCE = new ForgeBlocksRegistry();
        GTBlocksRegistry.INSTANCE.registerAll();
        GTItemsRegistry.INSTANCE = new ForgeItemsRegistry();
        GTItemsRegistry.INSTANCE.registerAll();
        GTBlockEntitiesRegistry.INSTANCE = new ForgeBlockEntitiesRegistry();
        GTFeaturesRegistry.INSTANCE = new ForgeFeaturesRegistry();
        GTRecipeSerializersRegistry.INSTANCE = new ForgeRecipeSerializersRegistry();
        GTRecipeTypesRegistry.INSTANCE = new ForgeRecipeTypesRegistry();
        GTTags.INSTANCE = new ForgeTagsRegistry();
        GTCreativeModeTabsRegistry.INSTANCE = new ForgeCreativeModeTabsRegistry();

        // Register all deffered registries
        ForgeBlocksRegistry.BLOCKS_REGISTRY.register(bus);
        ForgeBlocksRegistry.BLOCK_ITEMS_REGISTRY.register(bus);
        ForgeItemsRegistry.ITEMS_REGISTRY.register(bus);
        ForgeBlockEntitiesRegistry.BLOCK_ENTITY_TYPES_REGISTRY.register(bus);
        ForgeFeaturesRegistry.FEATURES_REGISTRY.register(bus);
        ForgeRecipeSerializersRegistry.RECIPE_SERIALIZERS_REGISTRY.register(bus);
        ForgeRecipeTypesRegistry.RECIPE_TYPES_REGISTRY.register(bus);
        ForgeCreativeModeTabsRegistry.CREATIVE_MODE_TABS_REGISTRY.register(bus);

        bus.addListener((EntityAttributeCreationEvent event) -> event.put(GTEntitiesRegistry.INSTANCE.SILKMOTH_ENTITY.get(), SilkmothEntity.createAttributes().build()));
        bus.addListener((EntityAttributeCreationEvent event) -> event.put(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), QuetzalEntity.createAttributes().build()));
        bus.addListener((SpawnPlacementRegisterEvent event) -> event.register(GTEntitiesRegistry.INSTANCE.QUETZAL_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, QuetzalEntity::checkQuetzalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR));
        bus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if(event.getTab() == GTCreativeModeTabsRegistry.INSTANCE.GT_TAB.get()) {
                GTCreativeModeTabsRegistry.addOrderedItems(event::accept);
            }
        });
    }
}
