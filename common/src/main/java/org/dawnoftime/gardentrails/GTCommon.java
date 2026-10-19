package org.dawnoftime.gardentrails;

import org.dawnoftime.gardentrails.registry.GTDynamicRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GTCommon {
	public static List<GTDynamicRecipes.Entry> dynamicRecipes = new ArrayList<>();
	public static final String MOD_ID = "gardentrails";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

	public static void init() {
		GTDynamicRecipes.init();
	}
}