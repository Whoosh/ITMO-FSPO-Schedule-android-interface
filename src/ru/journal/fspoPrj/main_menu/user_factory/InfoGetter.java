package ru.journal.fspoPrj.main_menu.user_factory;

public interface InfoGetter {
    Class<?> getToolClass(int index);
    String getToolName(int index);
    int getToolsCount();
}
