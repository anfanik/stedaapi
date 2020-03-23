package me.anfanik.steda.api.menu.filling;

import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.button.MenuButton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anfanik
 * Date: 19/09/2019
 */

public interface FillingStrategy<S extends MenuSession> {

    Map<Integer, MenuButton> generate(S session);

    class ResultBuilder {

        private Map<Integer, MenuButton> buttons = new HashMap<>();

        public ResultBuilder addButton(MenuButton menuButton, int... slots) {
            for (int slot : slots) {
                buttons.put(slot, menuButton);
            }
            return this;
        }

        public Map<Integer, MenuButton> build() {
            return buttons;
        }

    }

}
