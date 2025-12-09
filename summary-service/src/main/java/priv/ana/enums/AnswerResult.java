package priv.ana.enums;

public enum AnswerResult {
    WRONG("wrong"),
    RIGHT("correct");

    private String name;

    AnswerResult(String name) {
        this.name = name;
    }

    public static AnswerResult getAnswerResult(boolean isRight) {
        return isRight ? RIGHT : WRONG;
    }
}
