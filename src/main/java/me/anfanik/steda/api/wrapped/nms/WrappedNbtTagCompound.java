package me.anfanik.steda.api.wrapped.nms;

import lombok.*;
import me.anfanik.steda.api.wrapped.MinecraftVersion;

/**
 * @author Anfanik
 * Date: 21/09/2019
 */

public interface WrappedNbtTagCompound {

    static WrappedNbtTagCompound get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedNbtTagCompound get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedNbtTagCompound_v1_8_R3();
            case v1_12_R1: return new WrappedNbtTagCompound_v1_12_R1();
            default: return null;
        }
    }

    //void set(String key, WrappedNbtBase nbtBase);
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

    @SuppressWarnings("ConstantConditions")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedNbtTagCompound_v1_8_R3 implements WrappedNbtTagCompound {

        public WrappedNbtTagCompound generate(net.minecraft.server.v1_8_R3.NBTTagCompound tagCompound) {
            if (tagCompound == null) {
                tagCompound = new net.minecraft.server.v1_8_R3.NBTTagCompound();
            }
            return new WrappedNbtTagCompound_v1_8_R3(tagCompound);
        }

        private WrappedNbtTagCompound_v1_8_R3() {
            handle = null;
        }

        @Getter
        private final net.minecraft.server.v1_8_R3.NBTTagCompound handle;

        @Override
        @SneakyThrows
        public String get(String key) {
            val base = handle.get(key);
            if (base == null) {
                return null;
            }
            val field = base.getClass().getDeclaredField("data");
            field.setAccessible(true);
            return field.get(base).toString();
        }

        @Override
        public void remove(String key) {
            handle.remove(key);
        }

        @Override
        public void setString(String key, String value) {
            handle.setString(key, value);
        }

        @Override
        public void setBoolean(String key, boolean value) {
            handle.setBoolean(key, value);
        }

        @Override
        public void setByte(String key, byte value) {
            handle.setByte(key, value);
        }

        @Override
        public void setByteArray(String key, byte[] value) {
            handle.setByteArray(key, value);
        }

        @Override
        public void setDouble(String key, double value) {
            handle.setDouble(key, value);
        }

        @Override
        public void setFloat(String key, float value) {
            handle.setFloat(key, value);
        }

        @Override
        public void setInt(String key, int value) {
            handle.setInt(key, value);
        }

        @Override
        public void setIntArray(String key, int[] value) {
            handle.setIntArray(key, value);
        }

        @Override
        public void setLong(String key, long value) {
            handle.setLong(key, value);
        }

        @Override
        public void setShort(String key, short value) {
            handle.setShort(key, value);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedNbtTagCompound_v1_12_R1 implements WrappedNbtTagCompound {

        public WrappedNbtTagCompound generate(net.minecraft.server.v1_12_R1.NBTTagCompound tagCompound) {
            if (tagCompound == null) {
                tagCompound = new net.minecraft.server.v1_12_R1.NBTTagCompound();
            }
            return new WrappedNbtTagCompound_v1_12_R1(tagCompound);
        }

        private WrappedNbtTagCompound_v1_12_R1() {
            handle = null;
        }

        @Getter
        private final net.minecraft.server.v1_12_R1.NBTTagCompound handle;

        @Override
        @SneakyThrows
        public String get(String key) {
            val base = handle.get(key);
            if (base == null) {
                return null;
            }
            val field = base.getClass().getDeclaredField("data");
            field.setAccessible(true);
            return field.get(base).toString();
        }

        @Override
        public void remove(String key) {
            handle.remove(key);
        }

        @Override
        public void setString(String key, String value) {
            handle.setString(key, value);
        }

        @Override
        public void setBoolean(String key, boolean value) {
            handle.setBoolean(key, value);
        }

        @Override
        public void setByte(String key, byte value) {
            handle.setByte(key, value);
        }

        @Override
        public void setByteArray(String key, byte[] value) {
            handle.setByteArray(key, value);
        }

        @Override
        public void setDouble(String key, double value) {
            handle.setDouble(key, value);
        }

        @Override
        public void setFloat(String key, float value) {
            handle.setFloat(key, value);
        }

        @Override
        public void setInt(String key, int value) {
            handle.setInt(key, value);
        }

        @Override
        public void setIntArray(String key, int[] value) {
            handle.setIntArray(key, value);
        }

        @Override
        public void setLong(String key, long value) {
            handle.setLong(key, value);
        }

        @Override
        public void setShort(String key, short value) {
            handle.setShort(key, value);
        }

    }

}
