package team6.slidingtiles;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

/**
 * AiPlayer unit tests
 */

public class AiPlayerTest {
    @Test
    public void aiPlayerCompletes3CorrectMoves() {
        NumberBoard testBoard = new NumberBoard(true, 1);
        testBoard.swapTiles(3, 3);
        testBoard.swapTiles(2, 3);
        // board is now 3 moves from complete
        AiPlayer testPlayer = new AiPlayer();
        testPlayer.setBoard(testBoard);

        for (int i = 0; i < 3; i++) {
            testPlayer.makeMove();
        }

        assert(testBoard.isComplete());
    }

    @Test
    public void aiPlayerReturns3CorrectMoves() {
        NumberBoard testBoard = new NumberBoard(true, 1);
        testBoard.swapTiles(3, 3);
        testBoard.swapTiles(2, 3);
        // board is now 3 moves from complete
        AiPlayer testPlayer = new AiPlayer();
        testPlayer.setBoard(testBoard);

        for (int i = 0; i < 3; i++) {
            State.Location move = testPlayer.getNextMove();
            testBoard.swapTiles(move.getXIndex(), move.getYIndex());
        }

        assert(testBoard.isComplete());
    }

    // uncomment this test to watch the AI play
    /*
    @Test
    public void visualTestWatchItPlay() {
        NumberBoard testBoard = new NumberBoard(true, 100); // = shuffled 10,000 times
        AiPlayer testPlayer = new AiPlayer(testBoard, 10);
        int count = 0;
        while (!testBoard.isComplete()) {
            System.out.print("Move " + count++);
            System.out.println(testBoard);
            testPlayer.makeMove();
            if (count > 10000) {
                break;
            }
        }
        System.out.println(testBoard);
        System.out.println("Moves: " + count);
    }
    */

    /*
    // Not a unit test
    @Test
    public void testQuality() {
        int[] loops = new int[50];
        AiPlayer testPlayer = new AiPlayer();
        for (int i = 0; i < loops.length; i++) {
            int count = 0;
            NumberBoard testBoard = new NumberBoard(true, 100); // = shuffled 10,000 times
            testPlayer.setBoard(testBoard);
            while (!testBoard.isComplete()) {
                testPlayer.makeMove();
                count++;
                if (count > 10000) {
                    break;
                }
            }
            loops[i] = count;
            System.out.println(i);
        }
        System.out.println(Arrays.toString(loops));
    }
    */

}
