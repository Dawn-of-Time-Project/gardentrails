package org.dawnoftime.gardentrails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GTConfig {
    // Non-worldgen toggles
    public boolean generateChestLoot     = true;
    public boolean generateSilk          = true;
    public boolean generateGrapes        = true;
    public boolean generateMaize         = true;
    public boolean generateRice          = true;
    public boolean generateMulberry      = true;
    public boolean generateMold          = true;
    public boolean generateArrows        = true;
    public boolean generateFeathers      = true;
    public boolean dawnOfTimeUseSilk     = true;
    public boolean armorOfTheAgesUseSilk = true;
    public boolean armorOfTheAgesUseQuetzalFeathers = true;

    // Every plant (and the quetzal spawn) placed by biome. The biome list itself is a datapack tag
    // "gardentrails:has_<plant>", the config only disables a plant or adds extra biomes to it.
    public static final List<String> PLANTS = List.of(
        "boxwood_bush", "camellia", "commelina", "cypress",
        "geranium_pink", "geranium_orange", "geranium_purple", "geranium_purpleish", "geranium_red", "geranium_white",
        "ivy", "mulberry", "red_maple", "orange_maple", "green_maple",
        "rice", "wild_grape", "wild_maize", "quetzal"
    );

    private final Map<String, Boolean> plantEnabled = new HashMap<>();
    private final Map<String, List<String>> plantExtraBiomes = new HashMap<>();

    public boolean isPlantEnabled(String plant) {
        return plantEnabled.getOrDefault(plant, true);
    }

    public void setPlantEnabled(String plant, boolean enabled) {
        plantEnabled.put(plant, enabled);
    }

    public List<String> getExtraBiomes(String plant) {
        return plantExtraBiomes.getOrDefault(plant, List.of());
    }

    public void setExtraBiomes(String plant, List<String> biomes) {
        plantExtraBiomes.put(plant, biomes);
    }
}
