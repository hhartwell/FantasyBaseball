package harvey.com.fantasybaseball;

/**
 * Debugging class
 * Created by Harvey on 12/3/2017.
 */

public class PlayerObject {
    Integer user_id;
    String playerName;
    int pid;
    int ab;
    int r;
    int h;
    int hr;
    int rbi;
    double ba;
    double obp;
    int pitcher;
    double w;
    double era;
    double bb;
    double whip;


    public PlayerObject(Integer user_id, String playerName, int pid, int ab, int r, int h, int hr, int rbi, double ba, double obp, int pitcher, double w, double era, double bb, double whip) {
        this.user_id = user_id;
        this.playerName = playerName;
        this.pid = pid;
        this.ab = ab;
        this.r = r;
        this.h = h;
        this.hr = hr;
        this.rbi = rbi;
        this.ba = ba;
        this.obp = obp;
        this.pitcher = pitcher;
        this.w = w;
        this.era = era;
        this.bb = bb;
        this.whip = whip;
    }
    public PlayerObject(Integer user_id, String name){
        if (user_id != null ){
            this.user_id = user_id;
        } else {
            user_id = -1;
        }
        this.playerName = name;
    }
    public PlayerObject(String playerName, int pid){
        this.playerName = playerName;
        this.pid = pid;
    }

    @Override
    public String toString() {
        return playerName;
    }
}
