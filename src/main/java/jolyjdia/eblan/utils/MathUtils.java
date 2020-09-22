package jolyjdia.eblan.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {
    public static final double DEGREES_TO_RADIANS = 0.017453292519943295;
    public static final double RADIANS_TO_DEGREES = 57.29577951308232;
    //ЧЕКАЙ ThreadLocalRandom
    @Deprecated public static final Random RANDOM = new Random();
    public static final double CIRCE = 6.283185307179586;

    private MathUtils() {}

    public static double offset(@NotNull Location loc, @NotNull Location loc1) {
        return offset(loc.toVector(), loc1.toVector());
    }

    public static double offset(@NotNull Vector v, Vector v1) {
        return v.subtract(v1).length();
    }
    public static double toRadians(double angdeg) {
        return angdeg * DEGREES_TO_RADIANS;
    }

    public static double toDegrees(double angrad) {
        return angrad * RADIANS_TO_DEGREES;
    }
    public static int randomInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
    public static int randomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public static double randomDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
    public static double randomDouble(double bound) {
        return ThreadLocalRandom.current().nextDouble(bound);
    }
    public static double randomDouble(double origin, double bound) {
        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public static float randomFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }
    @SuppressWarnings("preview")
    public static float randomFloat(float bound) {
        if (bound <= 0.0F) {
            throw new IllegalArgumentException("граница должна быть положительной");
        }
        float result = ThreadLocalRandom.current().nextFloat() * bound;
        return (result < bound) ? result : // correct for rounding
                Float.intBitsToFloat(Float.floatToIntBits(bound) - 1);
    }
    @SuppressWarnings("preview")
    public static float randomFloat(float origin, float bound) {
        if (origin >= bound) {
            throw new IllegalArgumentException("граница должна быть больше начала");
        }
        float r = ThreadLocalRandom.current().nextFloat();
        if (origin < bound) {
            r = r * (bound - origin) + origin;
            if (r >= bound) {
                r = Float.intBitsToFloat(Float.floatToIntBits(bound) - 1);
            }
        }
        return r;
    }

    public static boolean randomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
