package org.dawnoftime.gardentrails.registry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.dawnoftime.gardentrails.GTCommon;
import org.dawnoftime.gardentrails.GTConfig;
import org.dawnoftime.gardentrails.platform.Services;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.List;

public class GTDynamicRecipes {
    private static final GTConfig config = Services.PLATFORM.getConfig();

    public static final String DoT_ID = "dawnoftimebuilder";
    public static JsonObject LIGHT_GRAY_FUTON=null;
    public static JsonObject SMALL_TATAMI_MAT =null;
    public static JsonObject SPRUCE_LEGLESS_CHAIR=null;
    public static JsonObject WHITE_LITTLE_FLAG=null;
    public static JsonObject WHITE_CUSHION=null;


    public static final String AotA_ID = "armoroftheages";
    public static JsonObject RAIJIN_ARMOR_CHEST = null;
    public static JsonObject RAIJIN_ARMOR_LEGS = null;
    public static JsonObject PHARAOH_ARMOR_CHEST = null;
    public static JsonObject PHARAOH_ARMOR_LEGS = null;
    public static JsonObject JAPANESE_LIGHT_ARMOR_HEAD = null;
    public static JsonObject JAPANESE_LIGHT_ARMOR_CHEST = null;
    public static JsonObject JAPANESE_LIGHT_ARMOR_LEGS = null;
    public static JsonObject QUETZALCOATL_ARMOR_HEAD = null;
    public static JsonObject QUETZALCOATL_ARMOR_CHEST = null;
    public static JsonObject QUETZALCOATL_ARMOR_FEET = null;

    public static void init(){
        //Dawn of Times recipes
        if(Services.PLATFORM.isModLoaded(DoT_ID) && config.dawnOfTimeUseSilk){

            LIGHT_GRAY_FUTON = createShapedRecipe(RecipeCategory.BUILDING_BLOCKS,
                    Identifier.fromNamespaceAndPath(DoT_ID, "light_gray_futon"),
                    List.of(
                            addItem('T', Identifier.fromNamespaceAndPath(DoT_ID, "thatch_bamboo")),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("SSS",
                            "TTT"));
            addRecipe(Identifier.fromNamespaceAndPath(DoT_ID, "light_gray_futon"), LIGHT_GRAY_FUTON);

            SMALL_TATAMI_MAT = createShapedRecipe(RecipeCategory.BUILDING_BLOCKS,
                    Identifier.fromNamespaceAndPath(DoT_ID, "small_tatami_mat"),
                    List.of(
                            addItem('T', Identifier.fromNamespaceAndPath(DoT_ID, "thatch_bamboo_slab")),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("STS")
            );
            addRecipe(Identifier.fromNamespaceAndPath(DoT_ID, "small_tatami_mat"), SMALL_TATAMI_MAT);

            SPRUCE_LEGLESS_CHAIR = createShapedRecipe(RecipeCategory.BUILDING_BLOCKS,
                    Identifier.fromNamespaceAndPath(DoT_ID, "spruce_legless_chair"),
                    List.of(
                            addItem('T', Items.SPRUCE_SLAB),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("T  ",
                            "TSS",
                            "TTT"));
            addRecipe(Identifier.fromNamespaceAndPath(DoT_ID, "spruce_legless_chair"), SPRUCE_LEGLESS_CHAIR);

            WHITE_LITTLE_FLAG = createShapedRecipe(RecipeCategory.BUILDING_BLOCKS,
                    Identifier.fromNamespaceAndPath(DoT_ID, "white_little_flag"),
                    List.of(
                            addItem('T', Items.STICK),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("TT",
                            "SS"));
            addRecipe(Identifier.fromNamespaceAndPath(DoT_ID, "white_little_flag"), WHITE_LITTLE_FLAG);

            WHITE_CUSHION = createShapedRecipe(RecipeCategory.BUILDING_BLOCKS,
                    Identifier.fromNamespaceAndPath(DoT_ID, "white_cushion"),
                    List.of(
                            addItem('F', Items.FEATHER),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("S  ",
                            "F  ",
                            "S  "));
            addRecipe(Identifier.fromNamespaceAndPath(DoT_ID, "white_cushion"), WHITE_CUSHION);
        }

        //Armor of the Ages recipes
        if(Services.PLATFORM.isModLoaded(AotA_ID) && config.armorOfTheAgesUseSilk){
            RAIJIN_ARMOR_CHEST = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "raijin_armor_chest"),
                    List.of(
                            addItem('R', Items.REDSTONE_BLOCK),
                            addItem('D', Items.DIAMOND_CHESTPLATE),
                            addItem('G', Items.GOLD_BLOCK),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("RDR",
                            "SGS",
                            "S S"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "raijin_armor_chest"), RAIJIN_ARMOR_CHEST);

            RAIJIN_ARMOR_LEGS = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "raijin_armor_legs"),
                    List.of(
                            addItem('R', Items.RED_DYE),
                            addItem('D', Items.DIAMOND_LEGGINGS),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("RDR",
                            "SSS",
                            "S S"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "raijin_armor_legs"), RAIJIN_ARMOR_LEGS);

            PHARAOH_ARMOR_CHEST = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "pharaoh_armor_chest"),
                    List.of(
                            addItem('G', Items.GOLD_BLOCK),
                            addItem('C', Items.GOLDEN_CHESTPLATE),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("G G",
                            "SCS",
                            "S S"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "pharaoh_armor_chest"), PHARAOH_ARMOR_CHEST);

            PHARAOH_ARMOR_LEGS = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "pharaoh_armor_legs"),
                    List.of(
                            addItem('G', Items.GOLD_BLOCK),
                            addItem('L', Items.GOLDEN_LEGGINGS),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("SLS",
                            "SGS"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "pharaoh_armor_legs"), PHARAOH_ARMOR_LEGS);

            JAPANESE_LIGHT_ARMOR_HEAD = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_head"),
                    List.of(
                            addItem('H', Items.LEATHER_HELMET),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("SHS",
                            "  S"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_head"), JAPANESE_LIGHT_ARMOR_HEAD);

            JAPANESE_LIGHT_ARMOR_CHEST = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_chest"),
                    List.of(
                            addItem('C', Items.LEATHER_CHESTPLATE),
                            addItem('L', Items.LEATHER),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("S S",
                            "SCS",
                            "LLL"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_chest"), JAPANESE_LIGHT_ARMOR_CHEST);

            JAPANESE_LIGHT_ARMOR_LEGS = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_legs"),
                    List.of(
                            addItem('P', Items.LEATHER_LEGGINGS),
                            addItem('L', Items.LEATHER),
                            addItem('S', Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "silk"))
                    ),
                    List.of("SPS",
                            "L L"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "japanese_light_armor_legs"), JAPANESE_LIGHT_ARMOR_LEGS);
        }

        //Armor of the Ages Quetzalcoatl recipes (Garden Trails feathers)
        if(Services.PLATFORM.isModLoaded(AotA_ID) && config.armorOfTheAgesUseQuetzalFeathers){
            final Identifier greenFeather = Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "green_feather");
            final Identifier redFeather = Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, "red_feather");

            QUETZALCOATL_ARMOR_HEAD = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_head"),
                    List.of(
                            addItem('G', greenFeather),
                            addItem('R', redFeather),
                            addItem('o', Items.NETHERITE_HELMET),
                            addItem('i', Items.GOLD_INGOT),
                            addItem('v', Items.SKELETON_SKULL)
                    ),
                    List.of("GGG",
                            "RoR",
                            "ivi"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_head"), QUETZALCOATL_ARMOR_HEAD);

            QUETZALCOATL_ARMOR_CHEST = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_chest"),
                    List.of(
                            addItem('G', greenFeather),
                            addItem('R', redFeather),
                            addItem('B', Items.BONE_BLOCK),
                            addItem('x', Items.GOLD_INGOT),
                            addItem('i', Items.NETHERITE_CHESTPLATE),
                            addItem('O', Items.EGG)
                    ),
                    List.of("GBR",
                            "xix",
                            "ROG"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_chest"), QUETZALCOATL_ARMOR_CHEST);

            QUETZALCOATL_ARMOR_FEET = createShapedRecipe(RecipeCategory.COMBAT,
                    Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_feet"),
                    List.of(
                            addItem('R', redFeather),
                            addItem('B', Items.BONE_BLOCK),
                            addItem('x', Items.NETHERITE_BOOTS)
                    ),
                    List.of("BxB",
                            "R R"));
            addRecipe(Identifier.fromNamespaceAndPath(AotA_ID, "quetzalcoatl_armor_feet"), QUETZALCOATL_ARMOR_FEET);
        }
    }

    /**
    Add recipe to the list with a specific id
     */
    public static void addRecipe(final Identifier recipeId, final JsonObject json){
        GTCommon.dynamicRecipes.add(new Tuple<>(recipeId, json));
    }

    /**
     Add recipe to the list with the mod id
     */
    public static void addRecipe(final String recipeId, final JsonObject json){
        GTCommon.dynamicRecipes.add(new Tuple<>(Identifier.fromNamespaceAndPath(GTCommon.MOD_ID, recipeId), json));
    }

    public static @NotNull JsonObject createShapedRecipe(@NotNull final RecipeCategory category, final Item result,
                                                         @NotNull final List<Triplet<String, Character, Identifier>> keys,
                                                         @NotNull final List<String> pattern){
        return createShapedRecipe(category, BuiltInRegistries.ITEM.getKey(result), keys, pattern);
    }

    /**
     * Creates the shaped recipe directly using the same logic as vanilla json builders, for other kinds of recipes, you would need to recreate their respective json builder
     */
    public static @NotNull JsonObject createShapedRecipe(@NotNull final RecipeCategory category, final Identifier result,
                                                         @NotNull final List<Triplet<String, Character, Identifier>> keys,
                                                         @NotNull final List<String> pattern){
        final JsonObject mainJson = new JsonObject();

        //Type
        mainJson.addProperty("type", "minecraft:crafting_shaped");

        //Category
        mainJson.addProperty("category", category.getFolderName());

        //We create a new Json Element, and add our crafting pattern to it.
        final JsonArray jsonArray = new JsonArray();
        for(String s : pattern) {
            jsonArray.add(s);
        }
        //Then we add the pattern to our json object.
        mainJson.add("pattern", jsonArray);

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all the defined keys.
        final JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        for (final Triplet<String, Character, Identifier> key : keys) {
            // Since 1.21.2 an ingredient is a plain string: "namespace:item" or "#namespace:tag"
            keyList.addProperty(String.valueOf(key.getB()), (key.getA().equals("tag") ? "#" : "") + key.getC());
        }

        mainJson.add("key", keyList);

        //Result
        final JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("id", result.toString());
        jsonobject.addProperty("count", 1);
        mainJson.add("result", jsonobject);

        return mainJson;
    }

    /**
     * To add an item directly to the recipe
     */
    public static Triplet<String, Character, Identifier> addItem(final Character c, final Item item){
        return new Triplet<>("item", c, BuiltInRegistries.ITEM.getKey(item));
    }

    /**
     * To add a item tag to the recipe
     */
    @SuppressWarnings("all")
    public static Triplet<String, Character, Identifier> addItem(Character c, TagKey<Item> tag){
        return new Triplet<>("tag", c, tag.location());
    }

    /**
     * To add an item or tag using their id to the recipe
     */
    public static Triplet<String, Character, Identifier> addItem(final Character c, final Identifier tag, final boolean isTag){

        return new Triplet<>(isTag? "tag": "item", c, tag);
    }

    /**
     * To add an item using their id to the recipe
     */
    public static Triplet<String, Character, Identifier> addItem(final Character c, final Identifier item){
        return addItem(c, item, false);
    }
}
