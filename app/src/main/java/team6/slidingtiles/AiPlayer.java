package team6.slidingtiles;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * AI player for number board game
 * create player with number board or set one, then call makeMove to make the player choose and
 * complete a move
 * <p>
 * How to use:
 * 1. Use NumberBoard copy constructor to dupe human player's board.
 * 2. Create AiPlayer with that NumberBoard (AI references _that_ NumberBoard, doesn't copy it)
 * 3. EITHER theAiPlayer.getNextMove() to get the State.Location of the tile to swap,
 * then call theNumberBoard.swapTiles(move.getX(), move.getY()) yourself
 * (theAiPlayer.getNextMove() returns null when theNumberBoard.isComplete())
 * OR theAiPlayer.makeMove() and the AiPlayer updates the NumberBoard itself
 * 4. Repeat step 3 at internals (0.5 - 1 sec?)
 * See AiPlayerTest.java for examples
 */

public class AiPlayer {
    private int maxDepth;
    private int maxPrevStates;
    private int moveCount;
    private boolean isBlankLast;
    private NumberBoard board;
    private Queue<State> prevStates;

    /**
     * Default constructor, must setBoard() before use
     */
    AiPlayer() {
        this(null, 8);
    }

    /**
     * Preferred constructor
     *
     * @param board    the NumberBoard for the AI player to use
     * @param maxDepth the maximum number of moves for the AI to look ahead
     */
    AiPlayer(NumberBoard board, int maxDepth) {
        this.setBoard(board);
        this.maxPrevStates = 10;
        this.maxDepth = maxDepth;
        this.prevStates = new LinkedList<>();
        this.isBlankLast = true;
    }

    /**
     * Set the NumberBoard for the AI player to use
     *
     * @param board the NumberBoard for the AI player to use
     */
    public void setBoard(NumberBoard board) {
        this.board = board;
        this.prevStates = new LinkedList<>();
        this.moveCount = 0;
    }

    /**
     * Call to make the AI player find and make a move
     */
    void makeMove() {
        if (this.board == null) {
            throw new IllegalArgumentException("setBoard() has not been called");
        }
        if (this.board.isComplete()) {
            return;
        }
        State.Location tileToMove = getMove();
        this.moveCount++;
        // this should ensure that any repeating cycles the AI gets caught in will _eventually_ break
        if (this.moveCount % 20 == 0) {
            this.maxPrevStates++;
        }
        board.swapTiles(tileToMove.getX(), tileToMove.getY());
    }

    /**
     * Call to get the AI player move
     *
     * @return Location of the tile to swap with the blank
     */
    State.Location getNextMove() {
        if (this.board == null) {
            throw new IllegalArgumentException("setBoard() has not been called");
        }
        if (this.board.isComplete()) {
            return null;
        }
        State.Location tileToMove = getMove();
        this.moveCount++;
        // this should ensure that any repeating cycles the AI gets caught in will _eventually_ break
        if (this.moveCount % 20 == 0) {
            this.maxPrevStates++;
        }
        return tileToMove;
    }

    /**
     * Returns the best available move (tile Location) for the AI player to choose
     * The AI player looks maxDepth moves ahead to decide the best option.
     *
     * @return Location of the tile to swap with the blank
     */
    @SuppressWarnings("unchecked")
    private State.Location getMove() {
        int[] best = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] bestLocation = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        State current = new State(this.board.getBoardAsBytes());

        // Don't return to recently visited states
        while (prevStates.size() >= this.maxPrevStates) {
            prevStates.remove();
        }
        prevStates.add(current);

        ArrayList<State>[] moveList = (ArrayList<State>[]) new ArrayList[4];
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

        // Check up to maxDepth moves ahead, then return best option
        for (int i = 0; i < 4; i++) {
            if (moveList[i].size() > 0) {
                // Don't undo the previous move
                if (prevStates.size() > 0 && prevStates.contains(moveList[i].get(0))) {
                    moveList[i].clear();
                    continue;
                }

                int min = 0;
                int max;
                for (int z = 0; z < this.maxDepth - 1; z++) {
                    max = moveList[i].size();
                    ArrayList<State> temp = new ArrayList<>();
                    for (int j = min; j < max; j++) {
                        temp.addAll(moveList[i].get(j).getFollowingStates(-1));
                    }
                    min = max;
                    temp.removeAll(prevStates);
                    moveList[i].addAll(temp);
                }

                // find best distance possible from this starting direction
                for (int x = 0; x < moveList[i].size(); x++) {
                    int dist = moveList[i].get(x).distance(this.isBlankLast);
                    if (dist < best[i]) {
                        best[i] = dist;
                        bestLocation[i] = x;
                    }
                }
            }
        }

        // Find the best direction to go
        int theBest = 0;
        for (int i = 1; i < 4; i++) {
            if (best[i] < best[theBest]) {
                theBest = i;
            } else if (best[i] == best[theBest] && bestLocation[i] < bestLocation[theBest]) {
                theBest = i;
            }
        }

        if (moveList[theBest].size() > 0) {
            return moveList[theBest].get(0).getBlankLocation();
        } else { // AI was dumb, so it must go to a state it already visited
            if (!up.equals(current)) { // just go up, if possible
                return up.getBlankLocation();
            } else { // otherwise, go down
                return dn.getBlankLocation();
            }
        }
    }

}
