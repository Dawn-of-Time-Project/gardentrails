package org.dawnoftime.gardentrails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class GTConfigLoader {

    public static GTConfig INSTANCE = new GTConfig();

    public static void load(Path configDir) {
        Path file = configDir.resolve("gardentrails-config.txt");
        if (!Files.exists(file)) {
            writeDefaults(file);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(file);
            for (String raw : lines) {
                String line = raw.strip();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq < 0) continue;
                String key = line.substring(0, eq).strip();
                String rawValue = line.substring(eq + 1).strip();
                boolean boolValue = Boolean.parseBoolean(rawValue);
                switch (key) {
                    case "generateChestLoot"      -> INSTANCE.generateChestLoot     = boolValue;
                    case "generateSilk"           -> INSTANCE.generateSilk          = boolValue;
                    case "generateGrapes"         -> INSTANCE.generateGrapes        = boolValue;
                    case "generateMaize"          -> INSTANCE.generateMaize         = boolValue;
                    case "generateRice"           -> INSTANCE.generateRice          = boolValue;
                    case "generateMulberry"       -> INSTANCE.generateMulberry      = boolValue;
                    case "generateMold"           -> INSTANCE.generateMold          = boolValue;
                    case "generateArrows"         -> INSTANCE.generateArrows        = boolValue;
                    case "generateFeathers"       -> INSTANCE.generateFeathers      = boolValue;
                    case "dawnOfTimeUseSilk"      -> INSTANCE.dawnOfTimeUseSilk     = boolValue;
                    case "armorOfTheAgesUseSilk"  -> INSTANCE.armorOfTheAgesUseSilk = boolValue;
                    case "armorOfTheAgesUseQuetzalFeathers" -> INSTANCE.armorOfTheAgesUseQuetzalFeathers = boolValue;
                    default -> parsePlantKey(key, rawValue, boolValue);
                }
            }
        } catch (IOException e) {
            System.err.println("[GardenTrails] Failed to read config, using defaults: " + e.getMessage());
        }
    }

    // Keys "<plant>_enabled" and "<plant>_extra_biomes", unknown keys are ignored
    private static void parsePlantKey(String key, String rawValue, boolean boolValue) {
        for (String plant : GTConfig.PLANTS) {
            if (key.equals(plant + "_enabled")) {
                INSTANCE.setPlantEnabled(plant, boolValue);
                return;
            }
            if (key.equals(plant + "_extra_biomes")) {
                INSTANCE.setExtraBiomes(plant, parseList(rawValue));
                return;
            }
        }
    }

    private static List<String> parseList(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(","))
            .map(String::strip)
            .filter(s -> !s.isEmpty())
            .toList();
    }

    private static void writeDefaults(Path file) {
        String content = """
# Garden Trails configuration
# Restart required after changes

# Master toggle for chest loot injection
generateChestLoot=true

# Individual items (only if generateChestLoot=true)
generateSilk=true
generateGrapes=true
generateMaize=true
generateRice=true
generateMulberry=true

# Mold in the simple dungeons and the stronghold chests, colored arrows and feathers in the jungle temple chests
generateMold=true
generateArrows=true
generateFeathers=true

# Requires Dawn of Time - replaces white wool with silk in DoT recipes
dawnOfTimeUseSilk=true

# Requires Armor of the Ages - replaces string with silk in AotA recipes
armorOfTheAgesUseSilk=true

# Requires Armor of the Ages - Quetzalcoatl armor recipes use Garden Trails green and red feathers
armorOfTheAgesUseQuetzalFeathers=true

# Plant placement
# The biomes of each plant come from the datapack tag data/gardentrails/tags/worldgen/biome/has_<plant>.json
# (the same tag also drives the quetzal spawn). This file only overrides it:
#   <plant>_enabled=false          the plant does not spawn anymore
#   <plant>_extra_biomes=a:b,c:d   comma-separated biome IDs added on top of the tag
# Biomes can only be added here. To remove biomes, override the tag with a datapack.
# IDs for biomes from mods that are not installed are ignored automatically.

boxwood_bush_enabled=true
boxwood_bush_extra_biomes=
camellia_enabled=true
camellia_extra_biomes=
commelina_enabled=true
commelina_extra_biomes=
cypress_enabled=true
cypress_extra_biomes=
geranium_pink_enabled=true
geranium_pink_extra_biomes=
geranium_orange_enabled=true
geranium_orange_extra_biomes=
geranium_purple_enabled=true
geranium_purple_extra_biomes=
geranium_purpleish_enabled=true
geranium_purpleish_extra_biomes=
geranium_red_enabled=true
geranium_red_extra_biomes=
geranium_white_enabled=true
geranium_white_extra_biomes=
ivy_enabled=true
ivy_extra_biomes=
mulberry_enabled=true
mulberry_extra_biomes=
red_maple_enabled=true
red_maple_extra_biomes=
orange_maple_enabled=true
orange_maple_extra_biomes=
green_maple_enabled=true
green_maple_extra_biomes=
rice_enabled=true
rice_extra_biomes=
wild_grape_enabled=true
wild_grape_extra_biomes=
wild_maize_enabled=true
wild_maize_extra_biomes=
quetzal_enabled=true
quetzal_extra_biomes=
""";
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, content);
        } catch (IOException e) {
            System.err.println("[GardenTrails] Failed to write default config: " + e.getMessage());
        }
    }
}
