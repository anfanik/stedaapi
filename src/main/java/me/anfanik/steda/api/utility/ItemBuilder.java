package me.anfanik.steda.api.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.anfanik.steda.api.wrapped.craft.WrappedCraftItemStack;
import me.anfanik.steda.api.wrapped.nms.WrappedNbtTagCompound;
import me.anfanik.steda.api.wrapped.nms.WrappedNmsItemStack;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Anfanik
 * Date: 20/09/2019
 */

public abstract class ItemBuilder<B extends ItemBuilder<?>> {

    public static MaterialItemBuilder fromMaterial(Material material) {
        return new MaterialItemBuilder(material);
    }

    public static MaterialItemBuilder fromMaterial(Material material, int data) {
        return new MaterialItemBuilder(material).setDurability(data);
    }

    public static ItemStackItemBuilder fromItem(ItemStack itemStack) {
        return new ItemStackItemBuilder(itemStack);
    }

    private List<Function<ItemStack, ItemStack>> itemChangers = new ArrayList<>();
    private List<Consumer<ItemStack>> itemModifications = new ArrayList<>();
    private List<Consumer<ItemMeta>> metaModifications = new ArrayList<>();

    public B setName(String name) {
        metaModifications.add(meta -> meta.setDisplayName(TextUtility.colorize("&f" + name)));
        return getThis();
    }

    public B setLore(Collection<String> lore) {
        metaModifications.add(meta -> meta.setLore(new ArrayList<>(TextUtility.colorize(lore))));
        return getThis();
    }

    public B setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public B appendLore(String... lines) {
        metaModifications.add(meta -> {
            List<String> lore = getLore(meta);
            lore.addAll(Arrays.asList(lines));
            meta.setLore(TextUtility.colorize(lore));
        });
        return getThis();
    }

    public B formatLore(Function<List<String>, List<String>> formatter) {
        metaModifications.add(meta -> {
            List<String> lore = getLore(meta);
            meta.setLore(TextUtility.colorize(formatter.apply(lore)));
        });
        return getThis();
    }

    public B formatLoreLines(Function<String, String> formatter) {
        metaModifications.add(meta -> {
            List<String> lore = new ArrayList<>();
            getLore(meta).forEach(line -> lore.add(formatter.apply(line)));
            meta.setLore(TextUtility.colorize(lore));
        });
        return getThis();
    }

    private List<String> getLore(ItemMeta meta) {
        List<String> lore = meta.getLore();
        return lore == null ? new ArrayList<>() : lore;
    }

    public B setAmount(int amount) {
        itemModifications.add(item -> item.setAmount(amount));
        return getThis();
    }

    public B setDurability(short durability) {
        itemModifications.add(item -> item.setDurability(durability));
        return getThis();
    }

    public B setDurability(int durability) {
        return setDurability((short) durability);
    }

    public B setUnbreakable(boolean unbreakable) {
        metaModifications.add(meta -> meta.spigot().setUnbreakable(unbreakable));
        return getThis();
    }

    public B addItemFlag(ItemFlag itemFlag) {
        metaModifications.add(meta -> meta.addItemFlags(itemFlag));
        return getThis();
    }

    public B addEnchantment(Enchantment enchantment, int level) {
        metaModifications.add(meta -> meta.addEnchant(enchantment, level, true));
        return getThis();
    }

    public NbtBuilder nbtBuilder() {
        return new NbtBuilder();
    }

    public SkullBuilder skullBuilder() {
        return new SkullBuilder();
    }

    public LeatherArmorBuilder leatherArmorBuilder() {
        return new LeatherArmorBuilder();
    }

    public PotionBuilder potionBuilder() {
        return new PotionBuilder();
    }

    @SuppressWarnings("unchecked")
    B getThis() {
        return (B) this;
    }

    protected abstract ItemStack getItemStack();

    public ItemStack build() {
        ItemStack temporaryItem = getItemStack();
        for (Function<ItemStack, ItemStack> changer : itemChangers) {
            temporaryItem = changer.apply(temporaryItem);
        }

        ItemStack item = temporaryItem;
        itemModifications.forEach(modification -> modification.accept(item));

        ItemMeta meta = item.getItemMeta();
        metaModifications.forEach(modification -> modification.accept(meta));
        item.setItemMeta(meta);
        return item;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MaterialItemBuilder extends ItemBuilder<MaterialItemBuilder> {

        private Material material;

        public MaterialItemBuilder setMaterial(Material material) {
            this.material = material;
            return getThis();
        }

        @Override
        protected ItemStack getItemStack() {
            return new ItemStack(material);
        }

    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ItemStackItemBuilder extends ItemBuilder<ItemStackItemBuilder> {

        @Getter(AccessLevel.PROTECTED)
        private ItemStack itemStack;

        public ItemStackItemBuilder setItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return getThis();
        }

    }

    public class NbtBuilder {

        private Map<String, String> nbtTags = new HashMap<>();

        public NbtBuilder setTag(String key, String value) {
            nbtTags.put(key, value);
            return this;
        }

        public B apply() {
            itemChangers.add(item -> {
                WrappedCraftItemStack craftItemStack = WrappedCraftItemStack.get().fromBukkitItemStack(item);
                WrappedNmsItemStack nmsItemStack = craftItemStack.asNmsCopy();
                WrappedNbtTagCompound tagCompound = nmsItemStack.getTag();
                nbtTags.forEach(tagCompound::setString);
                nmsItemStack.setTag(tagCompound);
                item = WrappedCraftItemStack.get().asBukkitCopy(nmsItemStack);
                return item;
            });
            return getThis();
        }

    }

    public class SkullBuilder {

        private boolean cristalix = false;

        private String owner;
        private String texture;
        private Skin skin;

        private SkullType type;

        public SkullBuilder cristalix() {
            cristalix = true;
            return this;
        }

        public SkullBuilder setType(SkullType type) {
            this.type = type;
            return this;
        }

        public SkullBuilder setOwner(Player owner) {
            this.owner = owner.getName();
            return this;
        }

        public SkullBuilder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        @Deprecated
        public SkullBuilder setTexture(String texture) {
            this.texture = texture;
            return this;
        }

        public SkullBuilder setSkin(Skin skin) {
            this.skin = skin;
            return this;
        }

        public SkullBuilder setSkin(String texture, String signature) {
            return setSkin(new Skin(texture, signature));
        }

        public B apply() {
            if (type != null) {
                setDurability(type.ordinal());
            }

            if (owner != null) {
                metaModifications.add(meta -> ((SkullMeta) meta).setOwner(owner));
            }

            if (texture != null) {
                metaModifications.add(meta -> {
                    SkullMeta skullMeta = (SkullMeta) meta;
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    if (cristalix) {
                        profile.getProperties().put("skinURL", new Property("skinURL", texture, ""));
                    } else {
                        profile.getProperties().put("textures", new Property("textures", texture));
                    }
                    try {
                        Field profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(skullMeta, profile);
                        profileField.setAccessible(false);
                    } catch (IllegalAccessException | NoSuchFieldException exception) {
                        exception.printStackTrace();
                    }
                });
            }

            if (skin != null) {
                metaModifications.add(meta -> {
                    SkullMeta skullMeta = (SkullMeta) meta;
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    profile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
                    try {
                        Field profileField = skullMeta.getClass().getDeclaredField("profile");
                        profileField.setAccessible(true);
                        profileField.set(skullMeta, profile);
                        profileField.setAccessible(false);
                    } catch (IllegalAccessException | NoSuchFieldException exception) {
                        exception.printStackTrace();
                    }
                });
            }

            return getThis();
        }

    }

    public class LeatherArmorBuilder {

        private Color color;

        public LeatherArmorBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public B apply() {
            if (color != null) {
                metaModifications.add(meta -> {
                    LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                    armorMeta.setColor(color);
                });
            }
            return getThis();
        }

    }

    public class PotionBuilder {

        private PotionEffectType mainEffectType;
        private List<PotionEffect> effects = new ArrayList<>();
        private Color color;

        public PotionBuilder mainEffectType(PotionEffectType type) {
            mainEffectType = type;
            return this;
        }

        public PotionBuilder customEffect(PotionEffect effect) {
            effects.add(effect);
            return this;
        }

        public PotionBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public B apply() {
            if (mainEffectType != null) {
                metaModifications.add(meta -> ((PotionMeta) meta).setMainEffect(mainEffectType));
            }
            if (!effects.isEmpty()) {
                metaModifications.add(meta -> effects.forEach(effect -> ((PotionMeta) meta).addCustomEffect(effect, true)));
            }
            if (color != null) {
                metaModifications.add(meta -> ((PotionMeta) meta).setColor(color));
            }
            return getThis();
        }

    }

}