package com.chess.engine.board;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
/**
 * Name: Justin Schaumberger
 * File: Tile.java
 * Date: 5/8/20
 */




/**
 * Abstraction for a tile on the board
 */
public abstract class Tile {

    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer,EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }

    /**
     * The location of the tile on the board
     */
    protected final int tileCoordinate;

    /**
     * Constructor
     * @param tileCoordinate the location of the tile on the board
     */
    Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    /**
     * Gets the tile coordinate
     * @return the tile coordinate
     */
    public int getTileCoordinate() {
        return this.tileCoordinate;
    }

    /**
     * Determines if the tile is occupied
     * @return true if the tile is occupied by a piece and false otherwise
     */
    public abstract boolean isTileOccupied();

    /**
     * Returns the Piece on the tile
     * @return the Piece on the tile
     */
    public abstract Piece getPiece();

    /**
     * Abstraction for a tile with no piece
     */
    public static final class EmptyTile extends Tile {

        /**
         * Constructor
         * @param tileCoordinate the location of the tile on the board
         */
        EmptyTile(int tileCoordinate) {
            super(tileCoordinate);
        }
        @Override
        /**
         * Determines if the tile is occupied
         * @return true if the tile is occupied by a piece and false otherwise
         */
        public boolean isTileOccupied() {
            return false;
        }

        @Override
        /**
         * Returns the Piece on the tile
         * @return the Piece on the tile
         */
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    /**
     * Abstraction for a tile with a piece
     */
    public static final class OccupiedTile extends Tile {

        /**
         * The Piece that is on the tile
         */
        private final Piece pieceOnTile;

        /**
         * Constructor
         * @param tileCoordinate the location of the tile on the board
         * @param pieceOnTile the Piece that is on the tile
         */
        OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        @Override
        /**
         * Determines if the tile is occupied
         * @return true if the tile is occupied by a piece and false otherwise
         */
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        /**
         * Returns the Piece on the tile
         * @return the Piece on the tile
         */
        public Piece getPiece() {
            return pieceOnTile;
        }

        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
        }
    }

}
