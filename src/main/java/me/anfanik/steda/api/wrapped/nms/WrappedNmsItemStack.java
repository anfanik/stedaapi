package me.anfanik.steda.api.wrapped.nms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.v1_12_R1.nms.WrappedNmsItemStack_v1_12_R1;
import me.anfanik.steda.api.wrapped.v1_13_R1.nms.WrappedNmsItemStack_v1_13_R1;
import me.anfanik.steda.api.wrapped.v1_13_R2.nms.WrappedNmsItemStack_v1_13_R2;
import me.anfanik.steda.api.wrapped.v1_16_R1.nms.WrappedNmsItemStack_v1_16_R1;
import me.anfanik.steda.api.wrapped.v1_16_R2.nms.WrappedNmsItemStack_v1_16_R2;
import me.anfanik.steda.api.wrapped.v1_16_R3.nms.WrappedNmsItemStack_v1_16_R3;
import me.anfanik.steda.api.wrapped.v1_8_R3.nms.WrappedNmsItemStack_v1_8_R3;

public interface WrappedNmsItemStack {

    static WrappedNmsItemStack get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedNmsItemStack get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedNmsItemStack_v1_8_R3();
            case v1_12_R1: return new WrappedNmsItemStack_v1_12_R1();
            case v1_13_R1: return new WrappedNmsItemStack_v1_13_R1();
            case v1_13_R2: return new WrappedNmsItemStack_v1_13_R2();
            case v1_16_R1: return new WrappedNmsItemStack_v1_16_R1();
            case v1_16_R2: return new WrappedNmsItemStack_v1_16_R2();
            case v1_16_R3: return new WrappedNmsItemStack_v1_16_R3();
            default: return null;
        }
    }

    WrappedNbtTagCompound getTag();

    void setTag(WrappedNbtTagCompound nbtTagCompound);

}
