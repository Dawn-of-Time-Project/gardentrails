package org.dawnoftime.gardentrails.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import net.minecraft.world.level.block.Block;
import org.dawnoftime.gardentrails.block.IBlockTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Single place defining how every Garden Trails tooltip looks.
 * Two formats only:
 * - lore: GRAY text, no italic
 * - label: GOLD title (the translation itself ends with a colon), followed by lore lines
 * Any block, item or entity mentioned inside a text is passed as a {@link #name(String)} argument (AQUA).
 */
public final class TooltipHelper {
    private static final ChatFormatting TEXT_COLOR = ChatFormatting.GRAY;
    private static final ChatFormatting LABEL_COLOR = ChatFormatting.GOLD;
    private static final ChatFormatting NAME_COLOR = ChatFormatting.AQUA;

    private TooltipHelper() {
    }

    // Forwards the lines of a block implementing IBlockTooltip to the item tooltip
    public static void appendBlockTooltip(Block block, Consumer<Component> adder) {
        if (block instanceof IBlockTooltip tooltipBlock) {
            List<Component> lines = new ArrayList<>();
            tooltipBlock.appendTooltip(lines);
            lines.forEach(adder);
        }
    }

    // Adds one GRAY line. Args can be plain values or components built with name() / names()
    public static void lore(List<Component> tooltips, String key, Object... args) {
        tooltips.add(Component.translatable(key, args).withStyle(TEXT_COLOR));
    }

    // Adds one GOLD label line, to be followed by lore() lines
    public static void label(List<Component> tooltips, String labelKey) {
        tooltips.add(Component.translatable(labelKey).withStyle(LABEL_COLOR));
    }

    // Adds a process label followed by one lore line whose %s arguments are the given translation keys, colored as names
    public static void process(List<Component> tooltips, String labelKey, String descKey, String... argKeys) {
        label(tooltips, labelKey);
        Object[] args = new Object[argKeys.length];
        for (int i = 0; i < argKeys.length; i++) {
            args[i] = name(argKeys[i]);
        }
        lore(tooltips, descKey, args);
    }

    // Translated name of a block, item or entity, colored for use as a %s argument
    public static MutableComponent name(String translationKey) {
        return Component.translatable(translationKey).withStyle(NAME_COLOR);
    }

    // Several names separated by a GRAY comma, for a single %s argument
    public static MutableComponent names(String... translationKeys) {
        MutableComponent result = Component.empty();
        for (int i = 0; i < translationKeys.length; i++) {
            if (i > 0) {
                result.append(Component.literal(", ").withStyle(TEXT_COLOR));
            }
            result.append(name(translationKeys[i]));
        }
        return result;
    }
}
