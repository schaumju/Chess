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
 * File: Queen.java
 * Date: 5/9/20
 */
public class Queen extends Piece {

    /**
     * The possible moves the Queen can make
     */
    private final static int[] CANDIDATE_MOVE__VECTOR_COORDINATES = {-9, -8, -7 - 1, 1, 7, 8, 9};


    /**
     * Constructor
     *
     * @param piecePosition position of the piece on the board
     * @param pieceAlliance the Alliance of the piece
     */
    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<Move>();

        for (final int candidateCoordinateOffset:CANDIDATE_MOVE__VECTOR_COORDINATES){

            int candidateDestinationCoordinate = this.piecePosition;
            /**
             * Need to check all the possible slides
             */
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                /**
                 * Check whether it is an exception to the rule
                 */
                if (isColumnExclusion(piecePosition,candidateCoordinateOffset)) {
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;

                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                    /**
                     * If the tile is not occupied then it is legal
                     */
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }
                    /**
                     * If the tile is occupied, you need to check the piece on the tile
                     */
                    else {
                        /**
                         * Get the piece and alliance of the piece on the tile
                         */
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                        /**
                         * If they are different alliances then it is a legal move (capture)
                         */
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        // If the space is occupied, you can go no further
                        break;
                    }

                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public boolean isColumnExclusion(int currentPosition, int candidateOffset) {
        return isFirstColumnExclusion(currentPosition,candidateOffset) || isEighthColumnExclusion(currentPosition,candidateOffset);
    }

    @Override
    public Queen movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    /**
     * Catch edge case of first column
     * @param currentPosition the current position of the piece
     * @param candidateOffset the offset being checked
     * @return true if it is an edge case
     */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == 7 || candidateOffset == -9);
    }
    /**
     * Catch edge case of eighth column
     * @param currentPosition the current position of the piece
     * @param candidateOffset the offset being checked
     * @return true if it is an edge case
     */
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
    }
    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
