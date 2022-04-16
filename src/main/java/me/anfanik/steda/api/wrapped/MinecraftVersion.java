package me.anfanik.steda.api.wrapped;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public enum MinecraftVersion {

    UNSUPPORTED,
    v1_8_R3,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3;

    private static final Logger logger = Logger.getLogger("Minecraft Version Detector");

    private static final List<MinecraftVersion> PARTIALLY_SUPPORTED = Arrays.asList();
    private static final MinecraftVersion CURRENT_VERSION;

    static {
        String versionName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        MinecraftVersion version;

        try {
            version = valueOf(versionName);
            logger.info(String.format("Minecraft version: %s", version));
        } catch (IllegalArgumentException ignored) {
            version = UNSUPPORTED;
            logger.severe(String.format("Unsupported Minecraft version: %s", versionName));
        }

        if (PARTIALLY_SUPPORTED.contains(version)) {
            logger.warning(String.format("Version %s is not fully supported!", version));
        }

        CURRENT_VERSION = version;
    }

    public static MinecraftVersion getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public boolean isCurrent() {
        return this == CURRENT_VERSION;
    }

}

