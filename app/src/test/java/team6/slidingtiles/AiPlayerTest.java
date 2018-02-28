package team6.slidingtiles;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * AiPlayer unit tests
 */

public class AiPlayerTest {
    @Test
    public void test() {
        NumberBoard testBoard = new NumberBoard(true, 25);
        AiPlayer testPlayer = new AiPlayer();
        testPlayer.setBoard(testBoard);
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
        testPlayer.makeMove();
        System.out.println(testBoard);
    }
}
