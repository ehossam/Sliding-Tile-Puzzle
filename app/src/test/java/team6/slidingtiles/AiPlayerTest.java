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
        AiPlayer testPlayer = new AiPlayer(testBoard, 8);

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
            testBoard.swapTiles(move.getX(), move.getY());
        }

        assert(testBoard.isComplete());
    }

    @Test
    public void aiPlayerCompletesRandomBoardInReasonableNumberOfMoves() {
        NumberBoard testBoard = new NumberBoard(true, 5); // = shuffled 25 times
        AiPlayer testPlayer = new AiPlayer(testBoard, 10);
        int count = 0;
        while (!testBoard.isComplete() && count++ < 100) {
            testPlayer.makeMove();
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
        while (!testBoard.isComplete() && count++ < 10000) { // make sure it can't loop forever, just in case
            System.out.print("Move " + count);
            System.out.println(testBoard);
            testPlayer.makeMove();
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
            while (!testBoard.isComplete() && count++ < 10000) { // make sure it can't loop forever, just in case
                testPlayer.makeMove();
            }
            loops[i] = count;
            System.out.println(i);
        }
        System.out.println(Arrays.toString(loops));
    }
    */

}
