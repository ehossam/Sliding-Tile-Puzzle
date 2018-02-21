package team6.slidingtiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class State implements Serializable {

    public final static State GOAL_FIRST;
    public final static State GOAL_LAST;

    private byte[] tiles;

    public State() {
    }

    public State(byte[] inTiles) {
        tiles = new byte[tiles.length];
        System.arraycopy(inTiles, 0, this.tiles, 0, inTiles.length);
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
            State other = (State)obj;
            return Arrays.equals(this.tiles, other.tiles);
        }

        return false;
    }

    /*
    @Override
    public int hashCode() {
        if (hashCode == -1){
            int result = 17;
            for (int i = 0; i < allCells.length; i++) {
                result = 31 * result + allCells[i];
            }
            hashCode = result;
        }

        return hashCode;
    }
    */

    public byte getPossibleActions() {
        byte actions = 0;

        //CellLocation emptyCell = getEmptyCellLocation();

        if (emptyCell.getRowIndex() > 0) {
            CellLocation upCell = new CellLocation(emptyCell.getRowIndex() - 1,
                    emptyCell.getColumnIndex());
            actions.add(new Action(upCell, Move.DOWN));
        }

        if (emptyCell.getRowIndex() < dimension - 1) {
            CellLocation upCell = new CellLocation(emptyCell.getRowIndex() + 1,
                    emptyCell.getColumnIndex());
            actions.add(new Action(upCell, Move.UP));
        }

        if (emptyCell.getColumnIndex() > 0) {
            CellLocation upCell = new CellLocation(emptyCell.getRowIndex(),
                    emptyCell.getColumnIndex() - 1);
            actions.add(new Action(upCell, Move.RIGHT));
        }

        if (emptyCell.getColumnIndex() < dimension - 1) {
            CellLocation upCell = new CellLocation(emptyCell.getRowIndex(),
                    emptyCell.getColumnIndex() + 1);
            actions.add(new Action(upCell, Move.LEFT));
        }

        return actions;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.tiles);
    }


    */





}
