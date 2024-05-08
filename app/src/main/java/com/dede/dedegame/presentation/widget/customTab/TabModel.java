package com.dede.dedegame.presentation.widget.customTab;

public class TabModel {
    String name;
    int resSelect;
    int resUnselect;
    boolean select;

    public TabModel(String name, int resSelect, int resUnselect, boolean select) {
        this.name = name;
        this.resSelect = resSelect;
        this.resUnselect = resUnselect;
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResSelect() {
        return resSelect;
    }

    public void setResSelect(int resSelect) {
        this.resSelect = resSelect;
    }

    public int getResUnselect() {
        return resUnselect;
    }

    public void setResUnselect(int resUnselect) {
        this.resUnselect = resUnselect;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
