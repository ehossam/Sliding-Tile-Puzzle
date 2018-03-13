package team6.slidingtiles;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;

/**
 * State unit tests
 */

public class StateTest {
    @Test
    public void stateReturnsCorrectBytes() {
        byte[] testBytes = new byte[Board.TILE_COUNT];
        for (byte i = 0; i < Board.TILE_COUNT; i++) {
            testBytes[(int)i] = i;
        }
        State testState = new State(testBytes);
        byte[] returnedBytes = testState.getTiles();

        assert(Arrays.equals(testBytes, returnedBytes));
    }

    @Test
    public void equalStatesAreEqual() {
        byte[] testBytes1 = new byte[Board.TILE_COUNT];
        byte[] testBytes2 = new byte[Board.TILE_COUNT];
        for (byte i = 0; i < Board.TILE_COUNT; i++) {
            testBytes1[(int)i] = i;
            testBytes2[(int)i] = i;
        }
        State testState1 = new State(testBytes1);
        State testState2 = new State(testBytes2);

        assert(testState1.equals(testState2));
    }

    @Test
    public void unequalStatesAreNotEqual() {
        byte[] testBytes1 = new byte[Board.TILE_COUNT];
        byte[] testBytes2 = new byte[Board.TILE_COUNT];
        for (byte i = 0; i < Board.TILE_COUNT; i++) {
            testBytes1[(int)i] = i;
            testBytes2[(int)i] = i;
        }
        testBytes2[0] = (byte)2;
        State testState1 = new State(testBytes1);
        State testState2 = new State(testBytes2);

        assert(!testState1.equals(testState2));
    }

    @Test
    public void getBlankLocationIsCorrect() {
        byte[] testBytes = new byte[Board.TILE_COUNT];
        for (byte i = 0; i < Board.TILE_COUNT; i++) {
            testBytes[(int)i] = i;
        }
        testBytes[0] = (byte)8;
        testBytes[8] = (byte)0;
        State testState = new State(testBytes);
        State.Location expected = new State.Location(1, 3);

        assert(testState.getBlankLocation().equals(expected));
    }

    @Test
    public void distanceIsCorrect() {
        byte[] testBytes = new byte[Board.TILE_COUNT];
        for (byte i = 0; i < Board.TILE_COUNT; i++) {
            testBytes[(int) i] = i;
        }
        testBytes[0] = (byte)8;
        testBytes[8] = (byte)0;
        State testState = new State(testBytes);

        assertEquals(testState.distance(false), 4);
    }

}
