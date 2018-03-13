package team6.slidingtiles;

import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * MathBoard unit tests
 */
public class MathBoardTest {
    @Test
    public void boardConstructedContainsAllNumbersAndSymbols() {
        MathBoard test = new MathBoard();
        String[][] board = test.getBoard();
        Set<String> actual = new HashSet<>();
        Set<String> expect = new HashSet<>();

        for (int i = 0; i <= 9; i++) {
            expect.add(Integer.toString(i));
        }
        expect.add("=");
        expect.add("+");
        expect.add("-");
        expect.add("*");
        expect.add("/");
        expect.add(Board.BLANK);

        for (int i = 0; i < 5; i++) {
            actual.addAll(Arrays.asList(board[i]).subList(0, 5));
        }
        assertEquals(5, board.length);
        assertEquals(5, board[0].length);
        assertTrue(actual.containsAll(expect));
    }

    @Test
    public void equationsReturnCorrectValue() {
        MathBoard test = new MathBoard(true);
        assertEquals(8, test.getScore(2, 0, true));
        assertEquals(0, test.getScore(2, 0, true));
        assertEquals(3, test.getScore(0,2, false));
        assertEquals(0, test.getScore(0,2, false));
        assertEquals(3, test.getScore(4, 0, true));
        assertEquals(0, test.getScore(4, 0, true));
        assertEquals(0, test.getScore(1, 0, true));
        assertEquals(0, test.getScore(1, 0, false));
    }

    @Test
    public void swapWithAdjacentWorks() {
        MathBoard test = new MathBoard(true);
        boolean success = test.swapTiles(4, 3);
        assertTrue(success);
    }

    @Test
    public void swapWithNonAdjacentFails() {
        MathBoard test = new MathBoard(true);
        boolean success = test.swapTiles(2,2);
        assertTrue(!success);
    }

    @Test
    public void boardToStringIsCorrect() {
        String expected_board = "\n" +
                "+-----------+\n" +
                "| 6 5 4 - 9 |\n" +
                "| 7 8 * 9 / |\n" +
                "| 1 + 2 = 3 |\n" +
                "| 4 2 =   = |\n" +
                "| 1 = 8 0 3 |\n" +
                "+-----------+\n";
        MathBoard test = new MathBoard(true);
        String board = test.toString();
        assertEquals(board, expected_board);
    }

    @Test
    public void boardConstructedFromStringListIsCorrect() {
        String expected_board = "\n" +
                "+-----------+\n" +
                "|   0 1 2 3 |\n" +
                "| 4 5 6 7 8 |\n" +
                "| 9 + - * / |\n" +
                "| = = = = = |\n" +
                "| = = = = = |\n" +
                "+-----------+\n";
        List<String> boardIn = Arrays.asList(" ", "0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "+", "-", "*", "/", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=");
        MathBoard test = new MathBoard(boardIn);
        String board = test.toString();
        assertEquals(board, expected_board);
    }

    @Test(expected = RuntimeException.class)
    public void boardConstructedFromTwoBlanksStringListThrowsError() {
       List<String> boardIn = Arrays.asList(" ", "0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "+", "-", "*", "/", "=", "=", "=", "=", "=", "=", "=", "=", "=", " ");
        MathBoard test = new MathBoard(boardIn);
    }

    @Test(expected = RuntimeException.class)
    public void boardConstructedFromNoBlanksStringListThrowsError() {
        List<String> boardIn = Arrays.asList("0", "0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "+", "-", "*", "/", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=");
        MathBoard test = new MathBoard(boardIn);
    }

    @Test(expected = RuntimeException.class)
    public void boardConstructedFromShortStringListThrowsError() {
        List<String> boardIn = Arrays.asList("0", "0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "+", "-", "*", "/", "=", "=", "=", "=", "=", "=", "=", "=", "=");
        MathBoard test = new MathBoard(boardIn);
    }

    @Test
    public void boardConstructedFromStringListHasBlankXYCorrect() {
        List<String> boardIn = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9", "+", " ", "-", "*", "/", "=", "=", "=", "=", "=", "=", "=", "=", "=", "=");
        MathBoard test = new MathBoard(boardIn);
        assertEquals(test.getBlankX(), 1);
        assertEquals(test.getBlankY(), 2);
    }
}
