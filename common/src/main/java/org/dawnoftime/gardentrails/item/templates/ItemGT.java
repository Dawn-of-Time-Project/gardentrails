package org.dawnoftime.gardentrails.item.templates;

import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import java.util.ArrayList;

import org.dawnoftime.gardentrails.util.RegistrationContext;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.dawnoftime.gardentrails.util.TooltipHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGT extends Item {
    // Translation keys for the process labels (shared by all items of a chain)
    private static final String SILK_PROCESS_HEADER_KEY = "lore.gardentrails.silk_process";
    private static final String SAKE_PROCESS_HEADER_KEY = "lore.gardentrails.sake_process";

    private static final String[] NO_ARGS = new String[0];

    private final String[] loreKeys;
    // Label key of the process pattern ("Silk Process:", "Sake Process:")
    @Nullable private final String processHeaderKey;
    // Description key for the process pattern (supports %s substitution for the highlighted item names)
    @Nullable private final String processDescKey;
    // Translation keys of the item names to highlight, empty means no label, no highlight (e.g. Silk)
    private final String[] processArgKeys;

    public ItemGT() {
        this(RegistrationContext.item(), new String[0], null, null, NO_ARGS);
    }

    public ItemGT(Properties properties) {
        this(properties, new String[0], null, null, NO_ARGS);
    }

    public ItemGT(String loreKey) {
        this(RegistrationContext.item(), loreKey, null, null);
    }

    public ItemGT(Properties properties, @Nullable String loreKey) {
        this(properties, loreKey, null, null);
    }

    // Silk process pattern: label + lore with one highlighted item name (%s arg)
    // Pass null for processArgKey to show only the lore without a label (e.g. Silk, end of chain)
    public ItemGT(Properties properties, String processDescKey, @Nullable String processArgKey) {
        this(properties, new String[0], SILK_PROCESS_HEADER_KEY, processDescKey, processArgKey == null ? NO_ARGS : new String[]{processArgKey});
    }

    // Several lore lines, one per key
    public ItemGT(Properties properties, String[] loreKeys) {
        this(properties, loreKeys, null, null, NO_ARGS);
    }

    // Sake process pattern: label + lore with highlighted item names (one %s arg per key)
    public static ItemGT sakeProcess(Properties properties, String processDescKey, String... processArgKeys) {
        return new ItemGT(properties, new String[0], SAKE_PROCESS_HEADER_KEY, processDescKey, processArgKeys);
    }

    private ItemGT(Properties properties, @Nullable String loreKey, @Nullable String processDescKey, @Nullable String processArgKey) {
        this(properties, loreKey == null ? new String[0] : new String[]{loreKey}, processDescKey == null ? null : SILK_PROCESS_HEADER_KEY, processDescKey, processArgKey == null ? NO_ARGS : new String[]{processArgKey});
    }

    private ItemGT(Properties properties, String[] loreKeys, @Nullable String processHeaderKey, @Nullable String processDescKey, String[] processArgKeys) {
        super(properties);
        this.loreKeys = loreKeys;
        this.processHeaderKey = processHeaderKey;
        this.processDescKey = processDescKey;
        this.processArgKeys = processArgKeys;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> adder, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, adder, flag);
        List<Component> tooltips = new ArrayList<>();
        for (String loreKey : this.loreKeys) {
            TooltipHelper.lore(tooltips, loreKey);
        }
        if (this.processDescKey != null) {
            if (this.processArgKeys.length > 0 && this.processHeaderKey != null) {
                TooltipHelper.process(tooltips, this.processHeaderKey, this.processDescKey, this.processArgKeys);
            } else {
                TooltipHelper.lore(tooltips, this.processDescKey);
            }
        }
        tooltips.forEach(adder);
    }
}
