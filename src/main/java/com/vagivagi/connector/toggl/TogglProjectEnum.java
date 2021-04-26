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
    ENGLISH_GENERAL(161126635, "English"),
    ENGLISH_HATSUON(165302293, "EnglishHatsuon"),
    ENGLISH_ONDOKU(165302294, "EnglishOndoku"),
    ENGLISH_HOBBY_COMIC(165302316, "EnglishHobbyComic"),
    ENGLISH_HOBBY_MOVIE(168702280, "EnglishHobbyMovie"),
    ENGLISH_HOBBY_GAME(168707208, "EnglishHobbyGame"),
    ENGLISH_SYSTEM(168707327, "EnglishSystem"),
    ENGLISH_SEIDOKU(165302299, "EnglishSeidoku"),
    ENGLISH_COMPOSITION(165302297, "EnglishComposition"),
    ENGLISH_PRE_TADOKU(165302304, "EnglishPreTadoku"),
    ENGLISH_BUNPOU(169345880, "EnglishBunpou"),
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
