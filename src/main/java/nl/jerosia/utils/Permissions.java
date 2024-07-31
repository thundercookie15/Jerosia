package nl.jerosia.utils;

public enum Permissions {
    WANDS("wands"),
    WANDS_EMPIRE_WAND("wands.empirewand"),;

    private final String permission;

    Permissions(String permission) {
        this.permission = "jerosia.%s".formatted(permission);
    }

    public String getPermission() {
        return permission;
    }
}
