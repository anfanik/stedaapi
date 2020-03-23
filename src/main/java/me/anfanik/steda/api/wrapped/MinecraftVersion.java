package me.anfanik.steda.api.wrapped;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

/**
 * @author Anfanik
 * Date: 21/09/2019
 */

public enum MinecraftVersion {
    /*TODO Add versions support
    v1_8_R1, v1_8_R2, v1_8_R3,
    v1_12_R1
    v1_9_R1, v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_13_R1, v1_13_R2;*/

    v1_12_R1,
    v1_8_R3;

    private static final List<MinecraftVersion> PARTIALLY_SUPPORTED = Arrays.asList(v1_12_R1);

    private static final MinecraftVersion CURRENT_VERSION;

    static {
        MinecraftVersion version;
        String versionName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            version = valueOf(versionName);
        } catch (IllegalArgumentException ignored) {
            throw new IllegalStateException("Unsupported version: " + versionName);
        }
        CURRENT_VERSION = version;
        System.out.println("Current version: " + version);
        if (PARTIALLY_SUPPORTED.contains(version)) {
            System.out.println("Version " + version + " not fully supported!");
        }
    }

    public static MinecraftVersion getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public boolean isCurrent() {
        return this == CURRENT_VERSION;
    }

}

