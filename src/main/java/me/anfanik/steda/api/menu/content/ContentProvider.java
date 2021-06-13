package me.anfanik.steda.api.menu.content;

import me.anfanik.steda.api.menu.MenuSession;

public interface ContentProvider<S extends MenuSession> {

    void provide(S session, ContentPreparingContext context);

}
