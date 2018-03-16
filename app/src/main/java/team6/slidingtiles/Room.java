package team6.slidingtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheesea on 2/27/18.
 */

public class Room {
    private Boolean isOpen;
    private int p1Score;
    private int p2Score;
    private List<String> initBoardState;
    private String lastUsed;
    private boolean roundStarted;
    private String key;

    public Room(){

    }

    public Room(List<String> initBoardState){
        isOpen  = true;
        p1Score = 0;
        p2Score = 0;
        roundStarted    = false;
  //      key = null;
        lastUsed = "No equations";

        this.initBoardState = initBoardState;
    }

    /*
    public void setKey(String key) {
        this.key = key;
    }
    */

    public void setP1Score(int p1Score) {
        this.p1Score = p1Score;
    }

    public void setP2Score(int p2Score) {
        this.p2Score = p2Score;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setInitBoardState(List<String> initBoardState) {
        this.initBoardState = initBoardState;
    }

    public void setRoundStarted(boolean roundStarted) {
        this.roundStarted = roundStarted;
    }

    public void setIsOpen(Boolean open) {
        isOpen = open;
    }

    public String getKey() {
        return key;
    }

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public List<String> getInitBoardState() {
        return initBoardState;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }



    public boolean getRoundStarted() {
        return roundStarted;
    }

    public void setKey(String key){
        this.key = key;
    }
}


