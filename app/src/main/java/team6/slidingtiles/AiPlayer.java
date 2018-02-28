package team6.slidingtiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * AI player for number board game
 * - create player with number board or set one, then call makeMove to make the player choose and
 *   complete a move
 */

public class AiPlayer {

    private NumberBoard board;
    private State prevMove = null;

    /**
     * Default constructor, must setBoard() before use
     */
    public AiPlayer() {
    }

    /**
     * Preferred constructor
     * @param board the NumberBoard for the AI player to use
     */
    public AiPlayer(NumberBoard board) {
        this.board = board;
    }

    /**
     * Set the NumberBoard for the AI player to use
     * @param board the NumberBoard for the AI player to use
     */
    public void setBoard(NumberBoard board) {
        this.board = board;
    }

    /**
     * Set the depth (in moves) for the AI to search
     * @param depth number of consecutive moves to search for "ideal" next move
    public void setDepth(int depth) {
        if (depth < 1) {
            throw new RuntimeException("Search depth must be greater than 0");
        }
        this.depth = depth;
    }
     */

    /**
     * Call to make the AI player find and make a move
     */
    public void makeMove() {
        if ((this.board.getBlankX() == Board.TILE_COUNT - 1 && this.board.getBlankY() == Board.TILE_COUNT - 1) ||
            (this.board.getBlankX() == 0 && this.board.getBlankY() == 0)) {
            System.out.println("asdfasdfasdfasdfasdf");
            return;
        }
        State.Location tileToMove = getMove();
        board.swapTiles(tileToMove.getXIndex(), tileToMove.getYIndex());
    }

    /**
     * Returns the best available move (tile Location) for the AI player to choose
     * @return Location of the tile to swap with the blank
     */
    @SuppressWarnings("unchecked")
    private State.Location getMove() {
        int[] best = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] bestLocation = {-1, -1, -1, -1};
        State current = new State(this.board.getBoardAsBytes());
//        List<State>[] moveList = new ArrayList[4];
        ArrayList<State>[] moveList = (ArrayList<State>[])new ArrayList[4];
//        List<List<State>> moveList = new ArrayList<List<State>>(4);
        moveList[0] = new ArrayList<State>();
        moveList[1] = new ArrayList<State>();
        moveList[2] = new ArrayList<State>();
        moveList[3] = new ArrayList<State>();
        State up = new State(current.getTiles());
        if (up.swap(State.Direction.UP)) {
//            List<State> temp = new ArrayList<>();
//            temp.add(up);
//            moveList.add(temp);
            moveList[0].add(up);
        }
        State rt = new State(current.getTiles());
        if (rt.swap(State.Direction.RIGHT)) {
            moveList[1].add(rt);
        }
        State dn = new State(current.getTiles());
        if (dn.swap(State.Direction.DOWN)) {
            moveList[2].add(dn);
        }
        State lt = new State(current.getTiles());
        if (lt.swap(State.Direction.LEFT)) {
            moveList[3].add(lt);
        }

        for (int i = 0; i < 4; i++) {
            if (moveList[i].size() > 0) {
                moveList[i].addAll(moveList[i].get(0).getPossibleActions(i)); // remove backwards /////////////
                ArrayList<State> temp = new ArrayList<>();
                for (int j = 1; j < moveList[i].size(); j++) {
                    temp.addAll(moveList[i].get(j).getPossibleActions(99));  // mess ///
                }
                moveList[i].addAll(temp);

                for (int x = 0; x < moveList[i].size(); x++) {
                    int dist = moveList[i].get(x).distance();
                    if (dist < best[i]) {
                        best[i] = dist;
                        bestLocation[i] = x;
                    }
                }
            }
        }

        int theBest = 0;
        System.out.println(best[0]);
        for (int i = 1; i < 4; i++){
            System.out.println(best[i]);
            if (best[i] <= best[theBest] && bestLocation[i] <= bestLocation[theBest]) {
                theBest = i;
            }
        }

        prevMove = moveList[theBest].get(bestLocation[theBest]); ////////////// wrong ///////////////////////

        return moveList[theBest].get(0).getBlankLocation();
    }

}
