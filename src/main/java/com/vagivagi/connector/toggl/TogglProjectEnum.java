package com.vagivagi.connector.toggl;

public enum TogglProjectEnum {

    // FIXME プロジェクト追加後
    HOUSEWORK(161127189, "HouseWork"),
    GOING_OUT(161202384, "GoingOut"),
    SLEEP(161127207, "Sleep"),
    GAME(161126779, "Game"),
    EXERCISE(161126796, "Exercise"),
    COMMUTING(164896217, "Commuting"),
    MOVIE(161127080, "Movie"),
    WORK(161126645, "Work"),
    ENGLISH_HATSUON(165302293, "EnglishHatsuon"),
    ENGLISH_ONDOKU(165302294, "EnglishOndoku"),
    ENGLISH_HOBBY(165302316, "EnglishHobby"),
    ENGLISH_SEIDOKU(165302299, "EnglishSeidoku"),
    ENGLISH_COMPOSITION(165302297, "EnglishComposition"),
    ENGLISH_PRE_TADOKU(165302304, "EnglishPreTadoku"),
    ENGLISH_SPEAKING(165302313, "EnglishSpeaking"),
    ENGLISH_TOEIC(165302314, "EnglishSpeaking"),
    ENGLISH_WORD(165302310, "EnglishWord");

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
        throw new TogglWrapperNotExistProjectException(name + " is not found.");
    }

    static String getEnglishProjectIds() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TogglProjectEnum value : TogglProjectEnum.values()) {
            if (value.getName().startsWith("English")) {
                stringBuilder.append(String.valueOf(value.pid));
                stringBuilder.append(",");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
