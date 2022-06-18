package net.dagonmomo.dagonindustry.core.util;

public class DIMath
{
    /**
     * Returns true if the given value is between the given min and max values (inclusive)
     */
    public static boolean isBetween(Number value, Number min, Number max) {
        return value.doubleValue() >= min.doubleValue() && value.doubleValue() <= max.doubleValue();
    }

    /**
     * Limits the value to the given min and max values (inclusive)
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
