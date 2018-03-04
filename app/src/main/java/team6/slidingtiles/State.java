package team6.slidingtiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a board State to help the AI player look ahead
 * This class contains some modified code based on the work of Julien Dramaix
 * (https://github.com/jDramaix/)
 */

public class State {
    private byte[] tiles;

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

    static class Location {

        private int x;
        private int y;

        /**
         * Location constructor
         * @param x x-coordinate of Location
         * @param y y-coordinate of Location
         */
        Location(int x, int y) {
            if (y < 0 || x < 0 || y >= Board.TILE_SIDE || x >= Board.TILE_SIDE) {
                throw new RuntimeException("Tile location is not on the board");
            }
            this.x = x;
            this.y = y;
        }

        /**
         * Get x-coordinate of Location
         * @return x-coordinate of Location
         */
        int getX() {
            return this.x;
        }

        /**
         * Get y-coordinate of Location
         * @return y-coordinate of Location
         */
        int getY() {
            return this.y;
        }

        /**
         * Checks equality of two Locations
         * @param obj the Location to compare to this one
         * @return true if the Locations are equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Location) {
                Location other = (Location) obj;
                return  other.x == this.x && other.y == this.y;
            }
            return false;
        }
    }

    /**
     * State constructor
     * @param inTiles the byte array of tiles to copy
     */
    State(byte[] inTiles) {
        tiles = new byte[inTiles.length];
        System.arraycopy(inTiles, 0, this.tiles, 0, inTiles.length);
    }

    /**
     * Get the byte array of tiles
     * @return the byte array of tiles
     */
    byte[] getTiles() {
        byte[] tilesOut = new byte[tiles.length];
        System.arraycopy(this.tiles, 0, tilesOut, 0, this.tiles.length);
        return tilesOut;
    }

    /**
     * Checks equality of two States
     * @param obj the State to compare to this one
     * @return true if the states are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            State other = (State) obj;
            return Arrays.equals(this.tiles, other.tiles);
        }

        return false;
    }

    /**
     * Get a list of potential states within one move of the current state
     * @param prevDir The direction to exclude from the list of states (previous state)
     * @return the list of states that can be reached from this state in one move
     */
    List<State> getFollowingStates(int prevDir) {
        List<State> states = new ArrayList<>();
        int blankX = getBlankLocation().getX();
        int blankY = getBlankLocation().getY();

        if (blankY > 0 && prevDir != Direction.UP.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.UP);
            states.add(next);
        }
        if (blankY < Board.TILE_SIDE - 1 && prevDir != Direction.DOWN.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.DOWN);
            states.add(next);
        }
        if (blankX > 0 && prevDir != Direction.LEFT.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.LEFT);
            states.add(next);
        }
        if (blankX < Board.TILE_SIDE - 1 && prevDir != Direction.RIGHT.getValue()) {
            State next = new State(this.tiles);
            next.swap(Direction.RIGHT);
            states.add(next);
        }

        return states;
    }

    /**
     * Get the location of the blank tile
     * @return the Location of the blank tile
     */
    Location getBlankLocation() {
        for (int i = 0; i < Board.TILE_COUNT; i++) {
            if (tiles[i] == 0) { // (byte)0 is the blank tile in this representation
                return new Location(i % Board.TILE_SIDE, i / Board.TILE_SIDE);
            }
        }
        throw new RuntimeException("No blank tile found");
    }

    /**
     * Returns the Manhattan + linear conflict distance from the current state to the winning state
     *
     * @param isBlankLast if the blank tile is expected to be first (or last)
     * @return total Manhattan + linear conflict distance for this board/state
     */
    int distance(boolean isBlankLast) {
        int counter = 0;

        for (int i = 0; i < Board.TILE_COUNT; i++) {
            int value = tiles[i];
            if (value == 0) {
                continue;
            }

            int row = i / Board.TILE_SIDE;
            int column = i % Board.TILE_SIDE;
            if (isBlankLast) {
                value--;
            }
            int expectedRow = value / Board.TILE_SIDE;
            int expectedColumn = value % Board.TILE_SIDE;

            int difference = Math.abs(row - expectedRow) + Math.abs(column - expectedColumn);
            counter += difference;
        }

        counter += linearVerticalConflict();
        counter += linearHorizontalConflict();

        return counter;
    }

    /**
     * Get the linear vertical conflict moves for this state
     * @return the total linear vertical conflict moves for this state
     */
    private int linearVerticalConflict() {
        int dimension = Board.TILE_SIDE;
        int linearConflict = 0;

        for (int row = 0; row < dimension; row++) {
            int max = -1;
            for (int column = 0; column < dimension; column++) {
                int tileValue = this.tiles[row * Board.TILE_SIDE + column];
                if (tileValue != 0 && (tileValue - 1) / dimension == row) {
                    if (tileValue > max) {
                        max = tileValue;
                    } else {
                        linearConflict += 2;
                    }
                }
            }
        }
        return linearConflict;
    }

    /**
     * Get the linear horizontal conflict moves for this state
     * @return the total linear horizontal conflict moves for this state
     */
    private int linearHorizontalConflict() {
        int dimension = Board.TILE_SIDE;
        int linearConflict = 0;

        for (int column = 0; column < dimension; column++) {
            int max = -1;
            for (int row = 0; row < dimension; row++) {
                int tileValue = this.tiles[row * Board.TILE_SIDE + column];
                if (tileValue != 0 && tileValue % dimension == column + 1) {
                    if (tileValue > max) {
                        max = tileValue;
                    } else {
                        linearConflict += 2;
                    }
                }
            }
        }
        return linearConflict;
    }

    /**
     * Swaps the blank tile with the tile in the provided direction
     * @param dir the direction to swap the blank tile to
     * @return true if the swap was successful, otherwise false
     */
    boolean swap(Direction dir) {
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

    /**
     * Swaps the tiles at the provided indices
     * @param first the index of the first tile
     * @param second the index of the second tile
     * @return true if the swap was successful, otherwise false
     */
    private boolean swapSpots(int first, int second) {
        if (first < 0 || first >= Board.TILE_COUNT || second < 0 || second >= Board.TILE_COUNT) {
            return false;
        }
        byte temp = this.tiles[first];
        this.tiles[first] = this.tiles[second];
        this.tiles[second] = temp;
        return true;
    }

    /**
     * Get the index of the blank tile
     * @return the index of the blank tile
     */
    private int getBlankIndex() {
        for (int i = 0; i < Board.TILE_COUNT; i++) {
            if (tiles[i] == 0) {
                return i;
            }
        }
        throw new RuntimeException("No blank tile found");
    }

    /**
     * Get the String rep. of the state
     * @return the String representation of the state
     */
    @Override
    public String toString() {
        return Arrays.toString(this.tiles);
    }

}
