package com.actionsheet;

public class ButtonInfo {
    public ButtonInfo(String t, boolean d) {
        this.setTitle(t);
        this.setDisabled(d);
    }

    public ButtonInfo() {}

    private String title;
    private boolean disabled = false;
    private boolean isDestructive = false;
    private boolean isCancel = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDestructive() {
        return isDestructive;
    }

    public void setDestructive(boolean destructive) {
        isDestructive = destructive;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }
}
