package me.anfanik.steda.api.cooldown;

/**
 * @author Anfanik
 * Date: 06/10/2019
 */

public class CooldownService {

    private static CooldownService instance;

    public static CooldownService get() {
        return instance;
    }

    {
        instance = this;
    }



}
