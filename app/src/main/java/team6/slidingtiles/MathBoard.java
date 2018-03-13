package team6.slidingtiles;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * This class represents the math-based tile board.
 */

public class MathBoard extends Board {

    private static List<String> LEGAL_TILES;
    private LinkedHashSet<String> foundEquations;

    static {
        List<String> temp = new ArrayList<>();
        temp.add(Board.BLANK);
        temp.add("=");
        temp.add("+");
        temp.add("-");
        temp.add("*");
        temp.add("/");
        for (int i = 0; i < 10; i++) {
            temp.add(Integer.toString(i));
        }

        LEGAL_TILES = temp;
    }

    /**
     * MathBoard default constructor
     */
    MathBoard() {
        this(false);
    }

    /**
     * MathBoard constructor
     *
     * @param inBoard initial state to set this MathBoard to (List of tiles as Strings)
     */
    MathBoard(List<String> inBoard) {
        if (inBoard.size() != Board.TILE_COUNT) {
            throw new RuntimeException("Wrong number of tiles provided. " +
                    "Received " + inBoard.size() + ", expected " + Board.TILE_COUNT);
        }
        if (Collections.frequency(inBoard, Board.BLANK) != 1) {
            throw new RuntimeException("There should be only one blank tile.");
        }

        this.board = new String[Board.TILE_SIDE][Board.TILE_SIDE];

        for (int i = 0; i < Board.TILE_COUNT; i++) {
            String current = inBoard.get(i);
            if (!MathBoard.LEGAL_TILES.contains(current)) {
                throw new RuntimeException("Invalid tile glyph provided.");
            }

            int currentX = i % Board.TILE_SIDE;
            int currentY = i / Board.TILE_SIDE;
            if (current.equals(Board.BLANK)) {
                this.blankX = currentX;
                this.blankY = currentY;
            }

            this.board[currentY][currentX] = current;
        }

        this.foundEquations = new LinkedHashSet<>();
    }

    /**
     * MathBoard constructor
     *
     * @param isTest If true, will not shuffle the board, making testing easier
     */
    MathBoard(boolean isTest) {

        this.foundEquations = new LinkedHashSet<>();
        this.blankX = 3;
        this.blankY = 3;
        this.board = new String[][]{{"6", "5", "4", "-", "9"},
                                    {"7", "8", "*", "9", "/"},
                                    {"1", "+", "2", "=", "3"},
                                    {"4", "2", "=", Board.BLANK, "="},
                                    {"1", "=", "8", "0", "3"}};
        if (!isTest) {
            shuffle(512);
        }
    }

    /**
     * Get points scored for supplied equation (if legal and not previously found)
     * @param startX      starting tile's x-coordinate
     * @param startY      starting tile's y-coordinate
     * @param isVertical  if equation is vertical (true) or horizontal (false)
     * @return number of new points scored for current configuration, 0 if equation is illegal
     *         or already found
     */
    int getScore(int startX, int startY, boolean isVertical) {
        // make sure starting tiles are on the board, and in first column or row

        /*
        if (startX < 0 || startX >= Board.TILE_SIDE ||
                startY < 0 || startY >= Board.TILE_SIDE ||
                (startX != 0 && startY != 0)) {
            Log.d("startX",Integer.toString(startX));
            throw new IllegalArgumentException("Tile location is off the board");
        }
        */

        StringBuilder equation = new StringBuilder();

        if (isVertical) {
            for (int i = 0; i < Board.TILE_SIDE; i++) {
                equation.append(this.board[i][startX]);
            }
        } else {
            for (int i = 0; i < Board.TILE_SIDE; i++) {
                equation.append(this.board[startY][i]);
            }
        }

        if (equation.charAt(3) == '=') {
            // normalize equation format to #r = #1 [op] #2
            equation = equation.reverse();
            // just reversing changes the semantics of the equation,
            // so the operands are reversed back to the correct order
            char temp = equation.charAt(4);
            equation.setCharAt(4, equation.charAt(2));
            equation.setCharAt(2, temp);
        }

        int operand1 = Character.getNumericValue(equation.charAt(2));
        int operand2 = Character.getNumericValue(equation.charAt(4));
        int result = Character.getNumericValue(equation.charAt(0));
        // make sure characters are actually digits, and equal sign is found
        if (result < 0 || result > 9 ||
            operand1 < 0 || operand1 > 9 ||
            operand2 < 0 || operand2 > 9 ||
            equation.charAt(1) != '=') {  // we could do result < 1, but let's be thorough
            return  0;
        }

       //String equationNormed = equation.toString();
        String equationNormed = equation.toString();


        if (!this.foundEquations.contains(equationNormed)) {  // check that equation has not previously been found
            switch (equation.charAt(3)) {  // operator
                case '+':
                    if (result == operand1 + operand2) {
                        this.foundEquations.add(equationNormed);
                        return result;
                    }
                    break;
                case '-':
                    if (result == operand1 - operand2) {
                        this.foundEquations.add(equationNormed);
                        return result;
                    }
                    break;
                case '*':
                    if (result == operand1 * operand2) {
                        this.foundEquations.add(equationNormed);
                        return result;
                    }
                    break;
                case '/':
                    if (operand2 != 0 && result == operand1 / operand2) {
                        this.foundEquations.add(equationNormed);
                        return result;
                    }
                    break;
            }
        }

        return 0;
    }

    /**
     * Get the set of found equations
     * @return a hash set of equations that have been found
     */

    public LinkedHashSet<String> foundEquations() {
        // create deep copy
        LinkedHashSet<String> copy = new LinkedHashSet<>();
        copy.addAll(this.foundEquations);
        return copy;
    }

    public static final Parcelable.Creator<MathBoard> CREATOR
            = new Parcelable.Creator<MathBoard>() {
        public MathBoard createFromParcel(Parcel in) {
            return new MathBoard(in);
        }

        public MathBoard[] newArray(int size) {
            return new MathBoard[size];
        }
    };

    private MathBoard(Parcel in) {
        Bundle bundle = in.readBundle(getClass().getClassLoader());
        board = (String[][]) bundle.getSerializable(ARG_BOARD);
        blankX = bundle.getInt(ARG_BLANKX);
        blankY = bundle.getInt(ARG_BLANKY);
    }

    /**
     * Creates a string representation of the MathBoard
     * @return a string version of the board
     */
    public String toString() {
        StringBuilder boardString = new StringBuilder("\n+-----------+");
        for (int i = 0; i < Board.TILE_SIDE; i++) {
            boardString.append("\n| ");
            for (int j = 0; j < Board.TILE_SIDE; j++) {
                boardString.append(String.format("%s ", board[i][j]));
            }
            boardString.append("|");
        }
        boardString.append("\n+-----------+");
        return boardString.append("\n").toString();
    }

}
