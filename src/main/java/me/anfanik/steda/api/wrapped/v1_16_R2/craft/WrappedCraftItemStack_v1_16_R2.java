package me.anfanik.steda.api.wrapped.v1_16_R2.craft;

import lombok.Getter;
import me.anfanik.steda.api.wrapped.MinecraftVersion;
import me.anfanik.steda.api.wrapped.craft.WrappedCraftItemStack;
import me.anfanik.steda.api.wrapped.nms.WrappedNmsItemStack;
import me.anfanik.steda.api.wrapped.v1_16_R2.nms.WrappedNmsItemStack_v1_16_R2;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("ConstantConditions")
public class WrappedCraftItemStack_v1_16_R2 implements WrappedCraftItemStack {

    public WrappedCraftItemStack generate(ItemStack itemStack) {
        return new WrappedCraftItemStack_v1_16_R2(
                CraftItemStack.asCraftCopy(itemStack)
        );
    }

    public ItemStack asBukkitCopy(WrappedNmsItemStack nmsItemStack) {
        return CraftItemStack.asBukkitCopy(
                ((WrappedNmsItemStack_v1_16_R2) nmsItemStack).getHandle()
        );
    }

    @Getter
    private final CraftItemStack handle;

    public WrappedCraftItemStack_v1_16_R2() {
        handle = null;
    }

    public WrappedCraftItemStack_v1_16_R2(CraftItemStack handle) {
        this.handle = handle;
    }

    @Override
    public WrappedNmsItemStack asNmsCopy() {
        return ((WrappedNmsItemStack_v1_16_R2) WrappedNmsItemStack.get(MinecraftVersion.v1_16_R2)).generate(handle);
    }

}
