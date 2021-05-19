package me.anfanik.steda.api.wrapped.nms;

import lombok.*;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.v1_12_R1.nms.WrappedNbtTagCompound_v1_12_R1;
import me.anfanik.steda.api.wrapped.v1_13_R1.nms.WrappedNbtTagCompound_v1_13_R1;
import me.anfanik.steda.api.wrapped.v1_13_R2.nms.WrappedNbtTagCompound_v1_13_R2;
import me.anfanik.steda.api.wrapped.v1_16_R1.nms.WrappedNbtTagCompound_v1_16_R1;
import me.anfanik.steda.api.wrapped.v1_16_R2.nms.WrappedNbtTagCompound_v1_16_R2;
import me.anfanik.steda.api.wrapped.v1_16_R3.nms.WrappedNbtTagCompound_v1_16_R3;
import me.anfanik.steda.api.wrapped.v1_8_R3.nms.WrappedNbtTagCompound_v1_8_R3;

public interface WrappedNbtTagCompound {

    static WrappedNbtTagCompound get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedNbtTagCompound get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedNbtTagCompound_v1_8_R3();
            case v1_12_R1: return new WrappedNbtTagCompound_v1_12_R1();
            case v1_13_R1: return new WrappedNbtTagCompound_v1_13_R1();
            case v1_13_R2: return new WrappedNbtTagCompound_v1_13_R2();
            case v1_16_R1: return new WrappedNbtTagCompound_v1_16_R1();
            case v1_16_R2: return new WrappedNbtTagCompound_v1_16_R2();
            case v1_16_R3: return new WrappedNbtTagCompound_v1_16_R3();
            default: return null;
        }
    }

    String get(String key);

    void remove(String key);

    void setString(String key, String value);

    void setBoolean(String key, boolean value);

    void setByte(String key, byte value);

    void setByteArray(String key, byte[] value);

    void setDouble(String key, double value);

    void setFloat(String key, float value);

    void setInt(String key, int value);

    void setIntArray(String key, int[] value);

    void setLong(String key, long value);

    void setShort(String key, short value);

}
