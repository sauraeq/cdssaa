package com.equalinfotech.coffee.menu;

public class ListItemRow {
    public int icon;
    public int name;
    public ListItemRow(int icon, int name) {
        this.icon = icon;
        this.name = name;

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
