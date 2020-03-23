package me.anfanik.steda.api.wrapped.craft;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.nms.WrappedNmsItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author Anfanik
 * Date: 21/09/2019
 */

public interface WrappedCraftItemStack {

    static WrappedCraftItemStack get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedCraftItemStack get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedCraftItemStack_v1_8_R3();
            case v1_12_R1: return new WrappedCraftItemStack_v1_12_R1();
            default: return null;
        }
    }

    default WrappedCraftItemStack fromBukkitItemStack(ItemStack itemStack) {
        return get().generate(itemStack);
    }

    WrappedCraftItemStack generate(ItemStack itemStack);

    WrappedNmsItemStack asNmsCopy();

    ItemStack asBukkitCopy(WrappedNmsItemStack nmsItemStack);

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedCraftItemStack_v1_8_R3 implements WrappedCraftItemStack {

        public WrappedCraftItemStack generate(ItemStack itemStack) {
            return new WrappedCraftItemStack_v1_8_R3(
                    org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asCraftCopy(itemStack)
            );
        }

        public ItemStack asBukkitCopy(WrappedNmsItemStack nmsItemStack) {
            return org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asBukkitCopy(
                    ((WrappedNmsItemStack.WrappedNmsItemStack_v1_8_R3) nmsItemStack).getHandle()
            );
        }

        private WrappedCraftItemStack_v1_8_R3() {
            handle = null;
        }

        @Getter
        private final org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack handle;

        @Override
        public WrappedNmsItemStack asNmsCopy() {
            return ((WrappedNmsItemStack.WrappedNmsItemStack_v1_8_R3) WrappedNmsItemStack.get(MinecraftVersion.v1_8_R3)).generate(handle);
        }

    }

    class WrappedCraftItemStack_v1_12_R1 implements WrappedCraftItemStack {

        public WrappedCraftItemStack generate(ItemStack itemStack) {
            return new WrappedCraftItemStack_v1_12_R1(
                    org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asCraftCopy(itemStack)
            );
        }

        public ItemStack asBukkitCopy(WrappedNmsItemStack nmsItemStack) {
            return org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asBukkitCopy(
                    ((WrappedNmsItemStack.WrappedNmsItemStack_v1_12_R1) nmsItemStack).getHandle()
            );
        }

        private WrappedCraftItemStack_v1_12_R1() {
        }

        private WrappedCraftItemStack_v1_12_R1(org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack handle) {
            this.handle = handle;
        }

        @Getter
        private org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack handle;

        @Override
        public WrappedNmsItemStack asNmsCopy() {
            return ((WrappedNmsItemStack.WrappedNmsItemStack_v1_12_R1) WrappedNmsItemStack.get(MinecraftVersion.v1_12_R1)).generate(handle);
        }

    }

}
