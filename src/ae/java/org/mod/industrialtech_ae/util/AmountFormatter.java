package org.mod.industrialtech_ae.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class AmountFormatter {

    public static MutableComponent formatFluid(long mB, boolean precision, ChatFormatting color) {
        if (mB < 1000L) {
            return Component.literal(mB + " mB").withStyle(color);
        } else {
            double buckets = (double)mB / (double)1000.0F;
            if (buckets < (double)1000.0F) {
                String fmt = precision ? "%.2f B" : "%.0f B";
                return Component.literal(String.format(fmt, buckets)).withStyle(color);
            } else {
                return formatWithSuffix(buckets, precision, color);
            }
        }
    }

    public static MutableComponent formatItem(long count, boolean precision, ChatFormatting color) {
        return count < 1000L ? Component.literal(String.valueOf(count)).withStyle(color) : formatWithSuffix((double)count, precision, color);
    }

    private static MutableComponent formatWithSuffix(double value, boolean precision, ChatFormatting color) {
        String suffix;
        double displayValue;
        if (value < (double)1000000.0F) {
            suffix = "k";
            displayValue = value / (double)1000.0F;
        } else if (value < (double)1.0E9F) {
            suffix = "M";
            displayValue = value / (double)1000000.0F;
        } else if (value < 1.0E12) {
            suffix = "G";
            displayValue = value / (double)1.0E9F;
        } else if (value < 1.0E15) {
            suffix = "T";
            displayValue = value / 1.0E12;
        } else if (value < 1.0E18) {
            suffix = "P";
            displayValue = value / 1.0E15;
        } else {
            suffix = "E";
            displayValue = value / 1.0E18;
        }

        String format = precision ? "%.3f%s" : "%.1f%s";
        return Component.literal(String.format(format, displayValue, suffix)).withStyle(color);
    }

    public static MutableComponent formatWithSpaces(long value, ChatFormatting color) {
        return Component.literal(formatSpaces(value)).withStyle(color);
    }

    public static String formatSpaces(long value) {
        return String.format("%,d", value).replace(',', ' ');
    }
}
