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

import static com.chess.engine.board.Move.*;

/**
 * Name: Justin Schaumberger
 * File: King.java
 * Date: 5/9/20
 */
public class King extends Piece {
    /**
     * Possible places the piece can move
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Constructor
     *
     * @param piecePosition position of the piece on the board
     * @param pieceAlliance the Alliance of the piece
     */
    public King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KING);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();


        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            /**
             * Check if it is a valid space
             */
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                if (isColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }

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
                        legalMoves.add(new Move.AttackMove(board,this,candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }


        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public King movePiece(Move move) {
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }


    @Override
    public boolean isColumnExclusion(int currentPosition, int candidateOffset) {
        return isFirstColumnExclusion(currentPosition, candidateOffset) || isEighthColumnExclusion(currentPosition, candidateOffset);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -9) || (candidateOffset == -7) || (candidateOffset == -1));

    }
    /**
     * Capture edge cases for when the piece is in the eighth column
     * @param currentPosition the current position of the night
     * @param candidateOffset the candidate offset move for the knight
     * @return true if the location of the piece and the offset would violate the rule
     */
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 9) || (candidateOffset == 7) || (candidateOffset==1) );

    }
    @Override
    public String toString(){
        return PieceType.KING.toString();
    }
}
