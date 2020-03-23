package me.anfanik.steda.api.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Data;

/**
 * @author Anfanik
 * Date: 29/11/2019
 */

@Data
public class Skin {

    private final String texture;
    private final String signature;

    public void applyToGameProfile(GameProfile profile) {
        profile.getProperties().put("textures", new Property("textures", getTexture(), getSignature()));
    }

}
