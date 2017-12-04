package harvey.com.fantasybaseball;

import java.util.ArrayList;

/**
 * Debugging class
 * Created by Harvey on 12/3/2017.
 */

public class PlayerObject {
    Integer user_id;
    String playerName;
    Integer pid;
    Integer ab;
    Integer r;
    Integer h;
    Integer hr;
    Integer rbi;
    Double ba;
    Double obp;
    Integer pitcher;
    Double w;
    Double era;
    Double bb;
    Double whip;


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

    public Integer getUser_id() {
        return user_id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPid() {
        return pid;
    }

    public int getAb() {
        return ab;
    }

    public int getR() {
        return r;
    }

    public int getH() {
        return h;
    }

    public int getHr() {
        return hr;
    }

    public int getRbi() {
        return rbi;
    }

    public double getBa() {
        return ba;
    }

    public double getObp() {
        return obp;
    }

    public int getPitcher() {
        return pitcher;
    }

    public double getW() {
        return w;
    }

    public double getEra() {
        return era;
    }

    public double getBb() {
        return bb;
    }

    public double getWhip() {
        return whip;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public ArrayList<String> getPlayerAsArray(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(user_id.toString());
        list.add(playerName);
        list.add(pid.toString());
        list.add(ab.toString());
        list.add(r.toString());
        list.add(h.toString());
        list.add(hr.toString());
        list.add(rbi.toString());
        list.add(ba.toString());
        list.add(obp.toString());
        list.add(pitcher.toString());
        list.add(w.toString());
        list.add(era.toString());
        list.add(bb.toString());
        list.add(whip.toString());
        return list;
    }
}
