package team6.slidingtiles;

/**
 * Abstract super class for MathBoard and NumberBoard
 */

abstract class Board {
    final static String BLANK = " ";
    final static int TILE_SIDE = 5;
    final static int TILE_COUNT = (int)(Math.pow(TILE_SIDE, 2));

    String[][] board;
    int blankX;
    int blankY;

    Board() {}

    /**
     * Shuffles the Board
     * @param iterations Number of times to shuffle the blank and an adjacent tile
     */
    protected void shuffle(int iterations) {
        int direction;
        int tileX;
        int tileY;

        // randomly swap blank with adjacent tile, repeat if swap failed
        for (int i = 0; i < iterations; i++) {
            direction = (int)(Math.random() * 4);
            // 0 = up, 1 = right, 2 = down, 3 = left
            switch (direction) {
                case 0:
                    tileY = this.blankY - 1;
                    tileX = this.blankX;
                    break;
                case 1:
                    tileY = this.blankY;
                    tileX = this.blankX + 1;
                    break;
                case 2:
                    tileY = this.blankY + 1;
                    tileX = this.blankX;
                    break;
                default: // case 3
                    tileY = this.blankY;
                    tileX = this.blankX - 1;
                    break;
            }

            // Swap. If it didn't work, decrement to do it again.
            if (!swapTiles(tileX, tileY)) {
                --i;
            }
        }
    }

    /**
     * Swaps the provided tile with the blank tile on the board (if possible)
     * @param tileX tile's x-coordinate
     * @param tileY tile's y-coordinate
     * @return true if the move was legal and completed, false if the move was illegal and not completed
     */
    boolean swapTiles(int tileX, int tileY) {
       // ensure the tile is on the board
        if (tileX >= 0 && tileX < Board.TILE_SIDE && tileY >= 0 && tileY < Board.TILE_SIDE) {
            int xDiff = Math.abs(tileX - this.blankX);
            int yDiff = Math.abs(tileY - this.blankY);

            // ensure Manhattan distance between tiles is exactly one, then swap
            if (xDiff + yDiff == 1) {
                String temp = this.board[tileY][tileX];
                this.board[tileY][tileX] = this.board[this.blankY][this.blankX];
                this.board[this.blankY][this.blankX] = temp;
                this.blankX = tileX;
                this.blankY = tileY;
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the current board configuration
     * @return the board
     */
    String[][] getBoard() {
        return this.board;
    }

}