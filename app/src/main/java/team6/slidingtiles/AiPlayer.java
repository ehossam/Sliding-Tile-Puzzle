package team6.slidingtiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * AI player for number board game
 * - create player with number board or set one, then call makeMove to make the player choose and
 *   complete a move
 */

public class AiPlayer {

    private NumberBoard board;
    private State prevState = null;
    private Queue<State> prevStates = null;

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
        this.setBoard(board);
    }

    /**
     * Set the NumberBoard for the AI player to use
     * @param board the NumberBoard for the AI player to use
     */
    public void setBoard(NumberBoard board) {
        this.board = board;
        this.prevStates = new LinkedList<>();
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
        /*
        if ((this.board.getBlankX() == Board.TILE_COUNT - 1 && this.board.getBlankY() == Board.TILE_COUNT - 1) ||
            (this.board.getBlankX() == 0 && this.board.getBlankY() == 0)) {
            System.out.println("asdfasdfasdfasdfasdf");
            return;
        }
        */
        if (this.board.isComplete()) {
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
        int[] bestLocation = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        State current = new State(this.board.getBoardAsBytes());
        System.out.println(current.distance());

        // Don't return to recently visited states
        while (prevStates.size() > 9) {
            prevStates.remove();
        }
        prevStates.add(current);

        ArrayList<State>[] moveList = (ArrayList<State>[])new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            moveList[i] = new ArrayList<>();
        }
        State up = new State(current.getTiles());
        if (up.swap(State.Direction.UP)) {
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

        // Check up to 3 moves ahead, then choose best option
        for (int i = 0; i < 4; i++) {
            if (moveList[i].size() > 0) {
                // Don't undo the previous move
                if (prevStates.size() > 0 && prevStates.contains(moveList[i].get(0))) {
                    moveList[i].clear();
                    continue;
                }
                ArrayList<State> tempStates = new ArrayList<>(moveList[i].get(0).getPossibleActions(i));
                tempStates.removeAll(prevStates);
                for (int m = 0; m < 4; m++) {
                    tempStates.removeAll(moveList[i]);
                }
                moveList[i].addAll(tempStates);
                ArrayList<State> temp = new ArrayList<>();
                for (int j = 1; j < moveList[i].size(); j++) {
                    temp.addAll(moveList[i].get(j).getPossibleActions(-1));
                }
                temp.removeAll(prevStates);
                for (int m = 0; m < 4; m++) {
                    temp.removeAll(moveList[i]);
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

        // Find the best direction to go
        int theBest = 0;
        for (int i = 1; i < 4; i++){
            if (best[i] < best[theBest]) {
                theBest = i;
            } else if (best[i] == best[theBest] && bestLocation[i] < bestLocation[theBest]) {
                theBest = i;
            }
        }


        if (moveList[theBest].size() > 0) {
            return moveList[theBest].get(0).getBlankLocation();
        } else { // AI was dumb, needs to go to a state it already visited
            if (!up.equals(current)) {
                return up.getBlankLocation();
            } else {
                return dn.getBlankLocation();
            }
        }
    }

}
