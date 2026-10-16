package org.dawnoftime.gardentrails;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import org.dawnoftime.gardentrails.registry.GTCriteriaRegistry;
import org.dawnoftime.gardentrails.registry.GTDynamicRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GTCommon {
	public static List<Tuple<Identifier, JsonObject>> dynamicRecipes = new ArrayList<>();
	public static final String MOD_ID = "gardentrails";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier CREATIVE_ICONS = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/creative_icons.png");

	public static void init() {
		GTCriteriaRegistry.init();
		GTDynamicRecipes.init();
	}
}
