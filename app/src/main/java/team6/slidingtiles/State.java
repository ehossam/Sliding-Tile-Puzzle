package team6.slidingtiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * State to track AI
 * Based on the work of Julien Dramaix (https://github.com/jDramaix/SlidingPuzzle)
 */

public class State {

    public enum Direction {
        UP(0), RIGHT(1), LEFT(2), DOWN(3);
        int value;

        Direction(int v) {
            value = v;
        }
        int getValue() {
            return this.value;
        }
    }

    public static class Location {

        private int x;
        private int y;

        public Location() {
        }

        public Location(int xIndex, int yIndex) {
            if (yIndex < 0 || xIndex < 0 || yIndex >= Board.TILE_SIDE || xIndex >= Board.TILE_SIDE) {
                throw new RuntimeException("Tile location is not on the board");
            }
            this.x = xIndex;
            this.y = yIndex;
        }

        public int getYIndex() {
            return this.y;
        }

        public int getXIndex() {
            return this.x;
        }
    }

    public final static State GOAL_FIRST;
    public final static State GOAL_LAST;

    private int hashCode = -1;
    private byte[] tiles;

    public State() {
    }

    public State(byte[] inTiles) {
        tiles = new byte[inTiles.length];
        System.arraycopy(inTiles, 0, this.tiles, 0, inTiles.length);
    }

    public byte[] getTiles() {
        byte[] tilesOut = new byte[tiles.length];
        System.arraycopy(this.tiles, 0, tilesOut, 0, this.tiles.length);
        return tilesOut;
    }

    private static State getGoal(boolean isBlankFirst) {
        byte[] expBytes = new byte[Board.TILE_COUNT];
        if (isBlankFirst) {
            for (int i = 0; i < Board.TILE_COUNT; i++) {
                expBytes[i] = (byte) i;
            }
        } else {
            for (int i = 0; i < Board.TILE_COUNT - 1; i++) {
                expBytes[i] = (byte) (i + 1);
            }
            expBytes[Board.TILE_COUNT - 1] = 0;
        }

        return new State(expBytes);
    }

    static {
        GOAL_FIRST = getGoal(true);
        GOAL_LAST = getGoal(false);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            State other = (State) obj;
            return Arrays.equals(this.tiles, other.tiles);
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (hashCode == -1) {
            int result = 17;
            for (int i = 0; i < Board.TILE_COUNT; i++) {
                result = 31 * result + this.tiles[i];
            }
            hashCode = result;
        }
        return hashCode;
    }

    /*
    public List<Location> getPossibleActionsLocations() {
        List<Location> tilesToSwap = new ArrayList<>();
        int blankX = getBlankLocation().getXIndex();
        int blankY = getBlankLocation().getYIndex();

        if (blankY > 0) {
            tilesToSwap.add(new Location(blankX, blankY - 1));
        }

        if (blankY < Board.TILE_SIDE - 1) {
            tilesToSwap.add(new Location(blankX, blankY + 1));
        }

        if (blankX > 0) {
            tilesToSwap.add(new Location(blankX - 1, blankY));
        }

        if (blankX < Board.TILE_SIDE - 1) {
            tilesToSwap.add(new Location(blankX + 1, blankY));
        }

        return tilesToSwap;
    }
    */

    public List<State> getPossibleActions(int dir) {
        List<State> actions = new ArrayList<>();
        int blankX = getBlankLocation().getXIndex();
        int blankY = getBlankLocation().getYIndex();

        if (blankY > 0 && dir != Direction.UP.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.UP);
            actions.add(next);
        }

        if (blankY < Board.TILE_SIDE - 1 && dir != Direction.DOWN.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.DOWN);
            actions.add(next);
        }

        if (blankX > 0 && dir != Direction.LEFT.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.LEFT);
            actions.add(next);
        }

        if (blankX < Board.TILE_SIDE - 1 && dir != Direction.RIGHT.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.RIGHT);
            actions.add(next);
        }

        return actions;
    }

    public Location getBlankLocation() {
        for (int i = 0; i < Board.TILE_COUNT; i++) {
            if (tiles[i] == 0) {
                return new Location(i % Board.TILE_SIDE, i / Board.TILE_SIDE);
            }
        }
        throw new RuntimeException("No Empty tile found");
    }

    /**
     * Returns the Manhattan distance from the current state to the winning state
     *
     * @return total Manhattan distance for this board/state
     */
    public int distance() {
        int counter = 0;

        for (int i = 0; i < Board.TILE_COUNT; i++) {
            int value = tiles[i];
            if (value == 0) {
                continue;
            }

            int row = i / Board.TILE_SIDE;
            int column = i % Board.TILE_SIDE;
            int expectedRow = (value - 1) / Board.TILE_SIDE;
            int expectedColumn = (value - 1) % Board.TILE_SIDE;

            int difference = Math.abs(row - expectedRow) + Math.abs(column - expectedColumn);
            counter += difference;
        }

        counter += linearVerticalConflict();
        counter += linearHorizontalConflict();

        return counter;
    }

    private int linearVerticalConflict() {
        int dimension = Board.TILE_SIDE;
        int linearConflict = 0;

        for (int row = 0; row < dimension; row++){
            byte max = -1;
            for (int column = 0;  column < dimension; column++){
                byte cellValue = this.tiles[row * Board.TILE_SIDE + column];
                //is tile in its goal row ?
                if (cellValue != 0 && (cellValue - 1) / dimension == row){
                    if (cellValue > max){
                        max = cellValue;
                    }else {
                        //linear conflict, one tile must move up or down to allow the other to pass by and then back up
                        //add two moves to the manhattan distance
                        linearConflict += 2;
                    }
                }
            }
        }
        return linearConflict;
    }

    private int linearHorizontalConflict() {
        int dimension = Board.TILE_SIDE;
        int linearConflict = 0;

        for (int column = 0; column < dimension; column++) {
            byte max = -1;
            for (int row = 0;  row < dimension; row++){
                byte cellValue = this.tiles[row * Board.TILE_SIDE + column];
                //is tile in its goal row ?
                if (cellValue != 0 && cellValue % dimension == column + 1){
                    if (cellValue > max){
                        max = cellValue;
                    }else {
                        //linear conflict, one tile must move left or right to allow the other to pass by and then back up
                        //add two moves to the manhattan distance
                        linearConflict += 2;
                    }
                }
            }
        }
        return linearConflict;
    }

    public boolean swap(Direction dir) {
        int blank = this.getBlankIndex();
        switch (dir) {
            case UP:
                return swapSpots(blank, blank - Board.TILE_SIDE);
            case RIGHT:
                return blank % Board.TILE_SIDE < Board.TILE_SIDE - 1 && swapSpots(blank + 1, blank);
            case DOWN:
                return swapSpots(blank, blank + Board.TILE_SIDE);
            case LEFT:
                return blank % Board.TILE_SIDE > 0 && swapSpots(blank - 1, blank);
            default:
                return false;
        }
    }

    private boolean swapSpots(int first, int second) {
        if (first < 0 || first >= Board.TILE_COUNT || second < 0 || second >= Board.TILE_COUNT) {
            return false;
        }
        byte temp = this.tiles[first];
        this.tiles[first] = this.tiles[second];
        this.tiles[second] = temp;
        return true;
    }

    private int getBlankIndex() {
        for (int i = 0; i < Board.TILE_COUNT; i++) {
            if (tiles[i] == 0) {
                return i;
            }
        }
        throw new RuntimeException("No Empty tile found");
    }

    @Override
    public String toString() {
        return Arrays.toString(this.tiles);
    }

}
