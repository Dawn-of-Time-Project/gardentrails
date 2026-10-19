package org.dawnoftime.gardentrails;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.dawnoftime.gardentrails.block.templates.LegacyPergolaBlock;
import org.dawnoftime.gardentrails.item.GTCauldronInteractions;
import org.dawnoftime.gardentrails.item.GTDispenserBehaviors;
import org.dawnoftime.gardentrails.loot.LootModifiers;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.worldgen.GTConfigBiomeModifier;
import org.dawnoftime.gardentrails.worldgen.GTConfigSpawnBiomeModifier;

@Mod(GTCommon.MOD_ID)
public class GTNeoForge {

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
        DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, GTCommon.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<GTConfigBiomeModifier>> CONFIG_BIOME_MODIFIER =
        BIOME_MODIFIER_SERIALIZERS.register("config_driven", () -> GTConfigBiomeModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<GTConfigSpawnBiomeModifier>> CONFIG_SPAWN_BIOME_MODIFIER =
        BIOME_MODIFIER_SERIALIZERS.register("config_spawn", () -> GTConfigSpawnBiomeModifier.CODEC);

    public GTNeoForge(IEventBus eventBus, ModContainer container) {
        GTConfigLoader.load(FMLPaths.CONFIGDIR.get());
        GTCommon.init();

        RegistryImpls.init(eventBus);
        LootModifiers.register(eventBus);
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);

        eventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            GTCauldronInteractions.init();
            GTDispenserBehaviors.init();
            GTBlocksRegistry.FLAMMABLE_BLOCKS.forEach((block, fire) -> ((FireBlock) Blocks.FIRE).setFlammable(block.get(), fire.spread(), fire.destruction()));
        }));
        // Legacy pergolas of old worlds are converted when their chunk loads
        NeoForge.EVENT_BUS.addListener((ChunkEvent.Load event) -> {
            if (!event.getLevel().isClientSide()) {
                LegacyPergolaBlock.convertChunk(event.getChunk());
            }
        });
    }
}
