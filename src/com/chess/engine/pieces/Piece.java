package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Name: Justin Schaumberger
 * File: Piece.java
 * Date: 5/8/20
 */



public abstract class Piece {

    /**
     * Position of the piece on the board
     */
    protected final int piecePosition;
    /**
     * The Alliance of the piece
     */
    protected final Alliance pieceAlliance;
    /**
     * Determines if the piece has moved yet
     */
    protected final boolean isFirstMove;

    /**
     * The type of the piece
     */
    protected final PieceType pieceType;
    /**
     * The hash code of the piece
     */
    private final int cachedHashCode;

    /**
     * Constructor
     * @param piecePosition position of the piece on the board
     * @param pieceAlliance the Alliance of the piece
     */
    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.pieceType=pieceType;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    /**
     * Computes the hash code
     * @return the hash code
     */
    protected int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;

    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Check whether a tile coordinate is valid
     * @param coordinate the tile coordinate
     * @return true if the tile coordinate is valid
     */
    private boolean isValidTileCoordinate(int coordinate) {
        return coordinate>=0 && coordinate < 64;
    }

    public int getPiecePosition() {
        return piecePosition;
    }

    public Alliance getPieceAlliance() {
        return pieceAlliance;
    }


    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition()
                && pieceType == otherPiece.getPieceType()
                && pieceAlliance == otherPiece.getPieceAlliance()
                && isFirstMove == otherPiece.isFirstMove;
    }
    @Override
    public int hashCode() {
        return this.cachedHashCode;

    }


    /**
     * Gets all the possible moves for a piece
     * @param board the board that the piece is on
     * @return the list of all possible moves
     */
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    /**
     * Moves a piece
     * @param move the move
     * @return a new piece that is the same kind but updated piece position
     */
    public abstract Piece movePiece(Move move);

    /**
     * Gets the piece type
     * @return the piece type of the piece
     */
    public PieceType getPieceType() {
        return pieceType;
    }




    public abstract boolean isColumnExclusion(final int currentPosition, final int candidateOffset);

    public enum PieceType {

        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        BISHOP("B"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        QUEEN("Q"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K"){
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        PieceType(final String pieceName){
            this.pieceName=pieceName;
        }


        @Override
        public String toString() {
            return this.pieceName;
        }

        /**
         * Determines if the piece is a king
         * @return true if the piece is a king and false otherwise
         */
        public abstract boolean isKing();
        /**
         * Determines if the piece is a rook
         * @return true if the piece is a rook and false otherwise
         */
        public abstract boolean isRook();
    }


}
