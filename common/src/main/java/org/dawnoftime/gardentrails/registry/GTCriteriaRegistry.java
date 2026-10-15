package org.dawnoftime.gardentrails.registry;

import org.dawnoftime.gardentrails.advancement.KillSilkmothTrigger;

public class GTCriteriaRegistry {

    // The loader registers this trigger in the TRIGGER_TYPE registry (see KillSilkmothTrigger.ID)
    public static final KillSilkmothTrigger KILL_SILKMOTH = new KillSilkmothTrigger();

    public static void init() {}
}
