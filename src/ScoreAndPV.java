import java.util.ArrayList;

public class ScoreAndPV {

    private int score;
    private ArrayList<Node> pv;

    public ScoreAndPV(int score, ArrayList<Node> pv){
        this.score = score;
        this.pv = pv;
    }

    public ScoreAndPV(int score){
        this.score = score;
        this.pv = new ArrayList<>();
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Node> getPv() {
        return pv;
    }

    public static ScoreAndPV getPVandNegatedScore(ScoreAndPV s){
        return new ScoreAndPV(-s.getScore(), s.pv);
    }
}
