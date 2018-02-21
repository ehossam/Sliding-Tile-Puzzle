package team6.slidingtiles;

import android.text.BoringLayout;

import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * NumberBoard unit tests
 */
public class NumberBoardTest {
    @Test
    public void boardConstructedContainsAllNumbers() {
        NumberBoard test = new NumberBoard(true, 10);
        String[][] board = test.getBoard();
        Set<String> actual = new HashSet<>();
        Set<String> expect = new HashSet<>();
        expect.add(Board.BLANK);
        for (int i = 1; i < 25; i++) {
            expect.add(Integer.toString(i));
        }
        for (int i = 0; i < 5; i++) {
            actual.addAll(Arrays.asList(board[i]).subList(0, 5));
        }
        assertEquals(5, board.length);
        assertEquals(5, board[0].length);
        assertTrue(actual.containsAll(expect));
    }

    @Test
    public void swapWithAdjacentWorks() {
        NumberBoard test = new NumberBoard(true, 1);
        boolean success = test.swapTiles(Board.TILE_SIDE - 1, Board.TILE_SIDE - 1);
        assertTrue(success);
    }

    @Test
    public void swapWithNonAdjacentFails() {
        NumberBoard test = new NumberBoard(true, 1);
        boolean success = test.swapTiles(0, 0);
        assertTrue(!success);
    }

    @Test
    public void completeBoardIsComplete() {
        NumberBoard test = new NumberBoard(true, 1);
        test.swapTiles(Board.TILE_SIDE - 1, Board.TILE_SIDE - 1);
        boolean success = test.isComplete();
        assertTrue(success);
    }

    @Test
    public void incompleteBoardIsNotComplete() {
        NumberBoard test = new NumberBoard(true, 1);
        boolean success = test.isComplete();
        assertTrue(!success);
    }

    @Test
    public void boardCopyIsDeepCopy() {
        NumberBoard test = new NumberBoard(true, 1);
        String[][] copy = test.getBoard();
        // make copy invalid
        copy[Board.TILE_SIDE - 1][Board.TILE_SIDE - 1] = "FAIL";

        // complete game and check for success
        boolean successSwap = test.swapTiles(Board.TILE_SIDE - 1, Board.TILE_SIDE - 1);
        boolean successComplete = test.isComplete();
        assertTrue(successSwap);
        assertTrue(successComplete);
    }

    @Test
    public void byteArrayCorrect() {
        NumberBoard test = new NumberBoard(false, 1);
        test.swapTiles(0, 0);
        byte[] testBytes = test.getBoardAsBytes();
        byte[] expBytes = new byte[Board.TILE_COUNT];
        for (int i = 0; i < Board.TILE_COUNT; i++) {
            expBytes[i] = (byte)i;
        }
        assert(Arrays.equals(testBytes, expBytes));
    }

}
