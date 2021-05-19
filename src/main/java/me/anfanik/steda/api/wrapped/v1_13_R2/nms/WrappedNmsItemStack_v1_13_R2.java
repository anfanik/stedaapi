package me.anfanik.steda.api.wrapped.v1_13_R2.nms;

import lombok.Getter;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.nms.WrappedNbtTagCompound;
import me.anfanik.steda.api.wrapped.nms.WrappedNmsItemStack;
import net.minecraft.server.v1_13_R2.ItemStack;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;

@SuppressWarnings("ConstantConditions")
public class WrappedNmsItemStack_v1_13_R2 implements WrappedNmsItemStack {

    public WrappedNmsItemStack generate(CraftItemStack craftItemStack) {
        return generate(CraftItemStack.asNMSCopy(craftItemStack));
    }

    public WrappedNmsItemStack generate(ItemStack nmsItemStack) {
        return new WrappedNmsItemStack_v1_13_R2(nmsItemStack);
    }

    @Getter
    private final ItemStack handle;

    public WrappedNmsItemStack_v1_13_R2() {
        handle = null;
    }

    public WrappedNmsItemStack_v1_13_R2(ItemStack handle) {
        this.handle = handle;
    }

    @Override
    public WrappedNbtTagCompound getTag() {
        return ((WrappedNbtTagCompound_v1_13_R2) WrappedNbtTagCompound.get(MinecraftVersion.v1_13_R2)).generate(handle.getTag());
    }

    @Override
    public void setTag(WrappedNbtTagCompound tagCompound) {
        handle.setTag(((WrappedNbtTagCompound_v1_13_R2) tagCompound).getHandle());
    }

}
