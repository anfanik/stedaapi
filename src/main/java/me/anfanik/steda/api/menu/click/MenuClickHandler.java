package me.anfanik.steda.api.menu.click;

import me.anfanik.steda.api.menu.MenuSession;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface MenuClickHandler<S extends MenuSession> {

    MenuClickHandler<?> EMPTY = (session, click) -> {};

    @SuppressWarnings("unchecked")
    static <S extends MenuSession> MenuClickHandler<S> empty() {
        return (MenuClickHandler<S>) EMPTY;
    }

    static <S extends MenuSession> MenuClickHandler<S> session(Consumer<S> consumer) {
        return (session, click) -> consumer.accept(session);
    }

    static <S extends MenuSession> MenuClickHandler<S> player(Consumer<Player> consumer) {
        return (session, click) -> consumer.accept(session.getPlayer());
    }

    void handle(S session, MenuClick click);

}
