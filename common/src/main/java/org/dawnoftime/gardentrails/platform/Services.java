package org.dawnoftime.gardentrails.platform;

import org.dawnoftime.gardentrails.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/** Finds the loader specific implementations through META-INF/services files (ForgePlatformHelper, FabricPlatformHelper). */
public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}
