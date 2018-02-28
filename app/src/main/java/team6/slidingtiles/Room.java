package team6.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheesea on 2/27/18.
 */

public class Room {
    Boolean isOpen;
    int p1Score;
    int p2Score;
    List<String> initBoardState;
    List<String> usedEquations;
    long time;
    String key;

    public Room(){
        isOpen  = true;
        p1Score = 0;
        p2Score = 0;
        time    = 0;
        usedEquations = new ArrayList<>();
        initBoardState = new ArrayList<>();
    }

    public Room(List<String> initBoardState){
        isOpen  = true;
        p1Score = 0;
        p2Score = 0;
        time    = 0;
        usedEquations = new ArrayList<>();

        this.initBoardState = initBoardState;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setP1Score(int p1Score) {
        this.p1Score = p1Score;
    }

    public void setP2Score(int p2Score) {
        this.p2Score = p2Score;
    }

    public void setUsedEquations(List<String> usedEquations) {
        this.usedEquations = usedEquations;
    }

    public void setInitBoardState(List<String> initBoardState) {
        this.initBoardState = initBoardState;
    }

    public String getKey() {
        return key;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public List<String> getInitBoardState() {
        return initBoardState;
    }

    public List<String> getUsedEquations() {
        return usedEquations;
    }

    public long getTime() {
        return time;
    }
}


