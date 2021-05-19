package me.anfanik.steda.api.wrapped.craft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.nms.WrappedNmsItemStack;
import me.anfanik.steda.api.wrapped.v1_12_R1.craft.WrappedCraftItemStack_v1_12_R1;
import me.anfanik.steda.api.wrapped.v1_13_R1.craft.WrappedCraftItemStack_v1_13_R1;
import me.anfanik.steda.api.wrapped.v1_13_R2.craft.WrappedCraftItemStack_v1_13_R2;
import me.anfanik.steda.api.wrapped.v1_16_R1.craft.WrappedCraftItemStack_v1_16_R1;
import me.anfanik.steda.api.wrapped.v1_16_R2.craft.WrappedCraftItemStack_v1_16_R2;
import me.anfanik.steda.api.wrapped.v1_16_R3.craft.WrappedCraftItemStack_v1_16_R3;
import me.anfanik.steda.api.wrapped.v1_8_R3.craft.WrappedCraftItemStack_v1_8_R3;
import org.bukkit.inventory.ItemStack;

public interface WrappedCraftItemStack {

    static WrappedCraftItemStack get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedCraftItemStack get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedCraftItemStack_v1_8_R3();
            case v1_12_R1: return new WrappedCraftItemStack_v1_12_R1();
            case v1_13_R1: return new WrappedCraftItemStack_v1_13_R1();
            case v1_13_R2: return new WrappedCraftItemStack_v1_13_R2();
            case v1_16_R1: return new WrappedCraftItemStack_v1_16_R1();
            case v1_16_R2: return new WrappedCraftItemStack_v1_16_R2();
            case v1_16_R3: return new WrappedCraftItemStack_v1_16_R3();
            default: return null;
        }
    }

    default WrappedCraftItemStack fromBukkitItemStack(ItemStack itemStack) {
        return get().generate(itemStack);
    }

    WrappedCraftItemStack generate(ItemStack itemStack);

    WrappedNmsItemStack asNmsCopy();

    ItemStack asBukkitCopy(WrappedNmsItemStack nmsItemStack);

}
