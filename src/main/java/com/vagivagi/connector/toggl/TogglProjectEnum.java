package com.vagivagi.connector.toggl;

public enum TogglProjectEnum {

    // FIXME プロジェクト追加後
    HOUSEWORK(161127189, "HouseWork"),
    GOING_OUT(161202384, "GoingOut"),
    SLEEP(161127207, "Sleep"),
    GAME(161126779, "Game"),
    EXERCISE(161126796, "Exercise");

    TogglProjectEnum(int pid, String name) {
        this.pid = pid;
        this.name = name;
    }

    private final int pid;
    private final String name;

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    static TogglProjectEnum toToggleProjectEnum(String name) {
        for (TogglProjectEnum value : TogglProjectEnum.values()) {
            if (value.getName().equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new TogglWrapperNotExistProjectException(name + "is not found.");
    }
}
