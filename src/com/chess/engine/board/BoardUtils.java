package com.chess.engine.board;

/**
 * Name: Justin Schaumberger
 * File: BoardUtils.java
 * Date: 5/9/20
 */
public class BoardUtils {

    /**
     * Arrays filled with true if the index is in the column and false otherwise
     */
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    /**
     * Arrays filled with true if the index is in the row and false otherwise
     */
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);


    /**
     * the number of tiles on a board
     */
    public static final int NUM_TILES = 64;
    /**
     * The number of tiles per row
     */
    public static final int NUM_TILES_PER_ROW=8;

    public BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    /**
     * Create a boolean list of 64 entries where the coordinates are true if they are in the input column
     * @param columnNumber the given column number
     * @return a boolean list filled with trues for coordinates in the given column
     */
    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[columnNumber]=true;
            columnNumber+=NUM_TILES_PER_ROW;
        }while(columnNumber<NUM_TILES);

        return column;
    }
    /**
     * Create a boolean list of 64 entries where the coordinates are true if they are in the input row
     * @param rowNumber the given row number
     * @return a boolean list filled with trues for coordinates in the given column
     */
    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber]=true;
            rowNumber++;
        }while(rowNumber%NUM_TILES_PER_ROW !=0);

        return row;
    }

    /**
     * Check whether a tile coordinate is valid
     * @param coordinate the tile coordinate
     * @return true if the tile coordinate is valid
     */
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate>=0 && coordinate < NUM_TILES;
    }
}
