package me.anfanik.steda.api.menu.filling;

import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.button.MenuButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PatternFillingStrategy<S extends MenuSession> implements FillingStrategy<S> {

    private final String[] matrix;

    private final Map<Character, MenuButton> buttons = new HashMap<>();

    public PatternFillingStrategy<S> withButton(char itemChar, MenuButton button) {
        buttons.put(itemChar, button);
        return this;
    }

    @Override
    public Map<Integer, MenuButton> generate(S session) {
        ResultBuilder builder = new ResultBuilder();
        int slot = 0;
        for (String line : matrix) {
            for (char item : line.toCharArray()) {
                MenuButton button = buttons.get(item);
                if (button != null) {
                    builder.addButton(button, slot);
                }
                slot++;
            }
        }
        return builder.build();
    }

}
