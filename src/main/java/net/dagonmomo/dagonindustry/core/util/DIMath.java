package net.dagonmomo.dagonindustry.core.util;

public class DIMath
{
    public static boolean isBetween(Number value, Number min, Number max) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
