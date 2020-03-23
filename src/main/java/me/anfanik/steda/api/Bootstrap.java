package me.anfanik.steda.api;

import me.anfanik.steda.api.wrapped.MinecraftVersion;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Anfanik
 * Date: 17/09/2019
 */

public class Bootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing StedaApi..");
        StedaApi.initialize(this);
        System.out.println("Initializing Yoichi..");
        System.out.println("Current version: " + MinecraftVersion.getCurrentVersion());
    }

}
