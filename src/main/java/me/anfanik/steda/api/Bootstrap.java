package me.anfanik.steda.api;

import me.anfanik.steda.api.wrapped.MinecraftVersion;
import org.bukkit.plugin.java.JavaPlugin;

public class Bootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Initializing StedaApi..");
        StedaApi.initialize(this);
    }

}
