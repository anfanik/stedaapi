package me.anfanik.steda.api.cooldown;

public class CooldownService {

    private static CooldownService instance;

    public static CooldownService get() {
        return instance;
    }

    {
        instance = this;
    }



}
