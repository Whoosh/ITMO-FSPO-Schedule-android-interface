package ru.journal.fspoPrj.main_menu.user_factory.mights;

import ru.journal.fspoPrj.main_menu.user_factory.InfoGetter;

public class Parent implements InfoGetter{
    @Override
    public Class<?> getToolClass(int index) {
        return null;
    }

    @Override
    public String getToolName(int index) {
        return null;
    }

    @Override
    public int getToolsCount() {
        return 0;
    }
}