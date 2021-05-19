package me.anfanik.steda.api.wrapped.v1_12_R1.nms;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import me.anfanik.steda.api.wrapped.nms.WrappedNbtTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

@SuppressWarnings("ConstantConditions")
public class WrappedNbtTagCompound_v1_12_R1 implements WrappedNbtTagCompound {

    public WrappedNbtTagCompound generate(NBTTagCompound tagCompound) {
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        return new WrappedNbtTagCompound_v1_12_R1(tagCompound);
    }

    @Getter
    private final NBTTagCompound handle;

    public WrappedNbtTagCompound_v1_12_R1() {
        handle = null;
    }

    public WrappedNbtTagCompound_v1_12_R1(NBTTagCompound handle) {
        this.handle = handle;
    }

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
