package team6.slidingtiles;


public class UserScore {
    public String name;
    public int score;

    public UserScore() {

    }

    public UserScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
