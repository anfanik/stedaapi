package me.anfanik.steda.api.utility.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class LocationSerializer {

    public static String serialize(Location location) {
        if (location == null) {
            return null;
        }
        float yaw = location.getYaw(), pitch = location.getPitch();
        String serializedLocation = location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
        if (yaw + pitch != 0F) {
            serializedLocation += " " + yaw + " " + pitch;
        }
        return serializedLocation;
    }

    public static Location deserialize(String serializedString) {
        return deserialize(serializedString, null);
    }

    public static Location deserialize(String serializedString, World world) {
        if (serializedString == null) {
            return null;
        }
        String[] split = serializedString.split(" ");
        if (world == null) {
            world = Bukkit.getWorld(split[0]);
            if (world == null) {
                return null;
            }
        }
        Location location = new Location(
                world,
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3])
        );
        if (split.length == 6) {
            location.setYaw(Float.parseFloat(split[4]));
            location.setPitch(Float.parseFloat(split[5]));
        }
        return location;
    }

}
