package me.anfanik.steda.api.utility;

import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Anfanik
 * Date: 21/12/2019
 */

public class MathUtility {

    public static float randomInRange(float min, float max) {
        return min + ThreadLocalRandom.current().nextFloat() * (max - min);
    }

    public static int randomInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    public static double randomInRange(double min, double max) {
        return Math.random() < 0.5 ? ((1 - Math.random()) * (max - min) + min) : (Math.random() * (max - min) + min);
    }

    public static Vector rotateAroundAxisX(Vector vector, double angle) {
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = vector.getY() * cos - vector.getZ() * sin;
        z = vector.getY() * sin + vector.getZ() * cos;
        return vector.setY(y).setZ(z);
    }

    public static Vector rotateAroundAxisY(Vector vector, double angle) {
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = vector.getX() * cos + vector.getZ() * sin;
        z = vector.getX() * -sin + vector.getZ() * cos;
        return vector.setX(x).setZ(z);
    }

    public static Vector rotateAroundAxisZ(Vector vector, double angle) {
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = vector.getX() * cos - vector.getY() * sin;
        y = vector.getX() * sin + vector.getY() * cos;
        return vector.setX(x).setY(y);
    }

    public static Vector getRandomCircleVector() {
        double rnd, x, z, y;
        rnd = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);
        y = Math.sin(rnd);

        return new Vector(x, y, z);
    }

    public static Vector getRandomVector() {
        double x, y, z;
        x = ThreadLocalRandom.current().nextDouble() * 2 - 1;
        y = ThreadLocalRandom.current().nextDouble() * 2 - 1;
        z = ThreadLocalRandom.current().nextDouble() * 2 - 1;

        return new Vector(x, y, z).normalize();
    }

}
