package team6.slidingtiles;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * AiPlayer unit tests
 */

public class AiPlayerTest {
    @Test
    public void test() {
        NumberBoard testBoard = new NumberBoard(true,3);
        AiPlayer testPlayer = new AiPlayer();
        testPlayer.setBoard(testBoard);
        int count = 0;
        while (!testBoard.isComplete()) {
            System.out.println("Loop: " + count++);
            System.out.println(testBoard);
            testPlayer.makeMove();
            if (count > 10000) {
                break;
            }
        }
        System.out.println("Loops: " + count);
        System.out.println(testBoard);
    }
}
