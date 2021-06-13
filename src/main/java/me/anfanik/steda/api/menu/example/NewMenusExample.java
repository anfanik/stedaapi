package me.anfanik.steda.api.menu.example;

import lombok.val;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.menu.content.exact.PatternContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NewMenusExample {

    static {
        MenuItem.createDynamic(session -> new ItemStack(Material.AIR));

        PatternContentProvider.builder()
                .matrix("--i---w--",
                        "-+++++++-",
                        "--c---r--")
                .staticItem('i', ItemBuilder.fromMaterial(Material.PAPER)
                        .setName("Информация")
                        .setLore("Пошёл нахуй",
                                "Хуй соси"))
                .item('w', MenuItem.createDynamic(session -> {
                            val player = session.getPlayer();
                            if (player.hasPermission("хуй")) {
                                return ItemBuilder.fromMaterial(Material.DIAMOND)
                                        .setName("Ты мужик")
                                        .build();
                            } else {
                                return ItemBuilder.fromMaterial(Material.BARRIER)
                                        .setName("Ты девка тупая")
                                        .build();
                            }
                        }))
                .item('c', session -> {
                    val player = session.getPlayer();
                    if (player.hasPermission("хуй")) {
                        return MenuItem.createStatic(ItemBuilder.fromMaterial(Material.BARRIER)
                                        .setName("Закрыть")
                                        .setLore("Только для мужиков"),
                                MenuClickHandler.player(HumanEntity::closeInventory));
                    } else {
                        return null;
                    }
                })
                .item('r', MenuItem.createStatic(ItemBuilder.fromMaterial(Material.BARRIER)
                        .setName("Обновить"),
                        (session, click) -> session.update()))
                .items('+', new ArrayList<>() /* Тут чиста лист кнопок, которые будут ставиться */,
                        MenuItem.createStatic(ItemBuilder.fromMaterial(Material.BARRIER)
                            .setName("Сори больше нет кнопок")
                            .setLore("Чё я тебе, их рожу что ли?")))
                .build();
    }

}
