package me.anfanik.steda.api.wrapped;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public enum MinecraftVersion {

    v1_8_R3,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3;

    private static final List<MinecraftVersion> PARTIALLY_SUPPORTED = Arrays.asList();

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
            System.out.println("Version " + version + " is not fully supported!");
        }
    }

    public static MinecraftVersion getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public boolean isCurrent() {
        return this == CURRENT_VERSION;
    }

}

