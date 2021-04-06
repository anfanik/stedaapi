package me.anfanik.steda.api.wrapped.nms;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.wrapped.MinecraftVersion;

/**
 * @author Anfanik
 * Date: 21/09/2019
 */

public interface WrappedNmsItemStack {

    static WrappedNmsItemStack get() {
        return get(MinecraftVersion.getCurrentVersion());
    }

    static WrappedNmsItemStack get(MinecraftVersion version) {
        switch (version) {
            case v1_8_R3: return new WrappedNmsItemStack_v1_8_R3();
            case v1_12_R1: return new WrappedNmsItemStack_v1_12_R1();
            case v1_16_R3: return new WrappedNmsItemStack_v1_16_R3();
            default: return null;
        }
    }

    WrappedNbtTagCompound getTag();

    void setTag(WrappedNbtTagCompound nbtTagCompound);

    @SuppressWarnings("ConstantConditions")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedNmsItemStack_v1_8_R3 implements WrappedNmsItemStack {

        public WrappedNmsItemStack generate(org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack craftItemStack) {
            return generate(org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack.asNMSCopy(craftItemStack));
        }

        public WrappedNmsItemStack generate(net.minecraft.server.v1_8_R3.ItemStack nmsItemStack) {
            return new WrappedNmsItemStack_v1_8_R3(nmsItemStack);
        }

        private WrappedNmsItemStack_v1_8_R3() {
            handle = null;
        }

        @Getter
        private final net.minecraft.server.v1_8_R3.ItemStack handle;

        @Override
        public WrappedNbtTagCompound getTag() {
            return ((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_8_R3) WrappedNbtTagCompound.get(MinecraftVersion.v1_8_R3)).generate(handle.getTag());
        }

        @Override
        public void setTag(WrappedNbtTagCompound tagCompound) {
            handle.setTag(((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_8_R3) tagCompound).getHandle());
        }

    }

    @SuppressWarnings("ConstantConditions")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedNmsItemStack_v1_12_R1 implements WrappedNmsItemStack {

        public WrappedNmsItemStack generate(org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack craftItemStack) {
            return generate(org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(craftItemStack));
        }

        public WrappedNmsItemStack generate(net.minecraft.server.v1_12_R1.ItemStack nmsItemStack) {
            return new WrappedNmsItemStack_v1_12_R1(nmsItemStack);
        }

        private WrappedNmsItemStack_v1_12_R1() {
            handle = null;
        }

        @Getter
        private final net.minecraft.server.v1_12_R1.ItemStack handle;

        @Override
        public WrappedNbtTagCompound getTag() {
            return ((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_12_R1) WrappedNbtTagCompound.get(MinecraftVersion.v1_12_R1)).generate(handle.getTag());
        }

        @Override
        public void setTag(WrappedNbtTagCompound tagCompound) {
            handle.setTag(((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_12_R1) tagCompound).getHandle());
        }

    }

    @SuppressWarnings("ConstantConditions")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    class WrappedNmsItemStack_v1_16_R3 implements WrappedNmsItemStack {

        public WrappedNmsItemStack generate(org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack craftItemStack) {
            return generate(org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack.asNMSCopy(craftItemStack));
        }

        public WrappedNmsItemStack generate(net.minecraft.server.v1_16_R3.ItemStack nmsItemStack) {
            return new WrappedNmsItemStack_v1_16_R3(nmsItemStack);
        }

        private WrappedNmsItemStack_v1_16_R3() {
            handle = null;
        }

        @Getter
        private final net.minecraft.server.v1_16_R3.ItemStack handle;

        @Override
        public WrappedNbtTagCompound getTag() {
            return ((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_16_R3) WrappedNbtTagCompound.get(MinecraftVersion.v1_16_R3)).generate(handle.getTag());
        }

        @Override
        public void setTag(WrappedNbtTagCompound tagCompound) {
            handle.setTag(((WrappedNbtTagCompound.WrappedNbtTagCompound_v1_16_R3) tagCompound).getHandle());
        }

    }

}
