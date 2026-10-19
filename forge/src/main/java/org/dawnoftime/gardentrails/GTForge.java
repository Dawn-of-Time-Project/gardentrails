package org.dawnoftime.gardentrails;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.dawnoftime.gardentrails.block.templates.LegacyPergolaBlock;
import org.dawnoftime.gardentrails.item.GTCauldronInteractions;
import org.dawnoftime.gardentrails.item.GTDispenserBehaviors;
import org.dawnoftime.gardentrails.loot.LootModifiers;
import org.dawnoftime.gardentrails.registry.GTBlocksRegistry;
import org.dawnoftime.gardentrails.worldgen.GTConfigBiomeModifier;
import org.dawnoftime.gardentrails.worldgen.GTConfigSpawnBiomeModifier;

@Mod(GTCommon.MOD_ID)
public class GTForge {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, GTCommon.MOD_ID);

    public static final RegistryObject<Codec<GTConfigBiomeModifier>> CONFIG_BIOME_MODIFIER =
        BIOME_MODIFIER_SERIALIZERS.register("config_driven", () -> GTConfigBiomeModifier.CODEC);

    public static final RegistryObject<Codec<GTConfigSpawnBiomeModifier>> CONFIG_SPAWN_BIOME_MODIFIER =
        BIOME_MODIFIER_SERIALIZERS.register("config_spawn", () -> GTConfigSpawnBiomeModifier.CODEC);

    public GTForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GTConfigLoader.load(FMLPaths.CONFIGDIR.get());
        GTCommon.init();

        RegistryImpls.init(modEventBus);
        LootModifiers.register(modEventBus);
        BIOME_MODIFIER_SERIALIZERS.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(GTForgeClient.class);
        }

        modEventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(() -> {
            GTCauldronInteractions.init();
            GTDispenserBehaviors.init();
            GTBlocksRegistry.FLAMMABLE_BLOCKS.forEach((block, fire) -> ((FireBlock) Blocks.FIRE).setFlammable(block.get(), fire.spread(), fire.destruction()));
        }));
        // Legacy pergolas of old worlds are converted when their chunk loads
        MinecraftForge.EVENT_BUS.addListener((ChunkEvent.Load event) -> {
            if (!event.getLevel().isClientSide()) {
                LegacyPergolaBlock.convertChunk(event.getChunk());
            }
        });
    }
}
