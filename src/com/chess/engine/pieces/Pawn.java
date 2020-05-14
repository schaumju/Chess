package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Name: Justin Schaumberger
 * File: Pawn.java
 * Date: 5/9/20
 */
public class Pawn extends Piece {

    /**
     * Possible places the piece can move
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};
    /**
     * Constructor
     *
     * @param piecePosition position of the piece on the board
     * @param pieceAlliance the Alliance of the piece
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,PieceType.PAWN);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            /**
             * Get the destination factoring in the color
             */
            int candidateDestinationDestination = this.piecePosition + (currentCandidateOffset)*this.pieceAlliance.getDirection();

            /**
             * If it is not valid then skip it
             */
            if (BoardUtils.isValidTileCoordinate(candidateDestinationDestination)) {
                continue;
            }
            /**
             * If the tile 1 forward is not occupied then it is a valid move
             */
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationDestination).isTileOccupied()) {
                // TODO more work to do here (deal with promotions)!!!!!
                legalMoves.add(new MajorMove(board, this, candidateDestinationDestination));
            }
            /**
             * For moving a pawn 2 spaces (if it is the first move)
             */
            else if (currentCandidateOffset == 16 && this.isFirstMove() && (BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || ((BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
                final int behindCandidateDestinationCoordinate = this.piecePosition+ this.pieceAlliance.getDirection()*8;
                /**
                 * Check to make sure the two spaces in front of the pawn are unoccupied
                 */
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationDestination).isTileOccupied()) {
                    /**
                     * We can make a move now
                     */
                    // TODO more work to do here!!!!!
                    legalMoves.add(new MajorMove(board, this, candidateDestinationDestination));
                }
            }
            /**
             * Attacking moves
             * Rule out illegal moves (ie: far right column trying to go diagonally right)
             */
            else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {

                /**
                 * There must be a pieced to capture to move diagonally
                 */
                if (board.getTile(candidateDestinationDestination).isTileOccupied()) {
                    final Piece piecedOnCandidate = board.getTile(candidateDestinationDestination).getPiece();
                    /**
                     * The piece must be of the opposite Alliance
                     */
                    if (this.pieceAlliance != piecedOnCandidate.getPieceAlliance()) {
                        // TODO more work to do here!!!!!
                        legalMoves.add(new MajorMove(board, this, candidateDestinationDestination));
                    }
                }


            }
            else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {

                /**
                 * There must be a pieced to capture to move diagonally
                 */
                if (board.getTile(candidateDestinationDestination).isTileOccupied()) {
                    final Piece piecedOnCandidate = board.getTile(candidateDestinationDestination).getPiece();
                    /**
                     * The piece must be of the opposite Alliance
                     */
                    if (this.pieceAlliance != piecedOnCandidate.getPieceAlliance()) {
                        // TODO more work to do here!!!!!
                        legalMoves.add(new MajorMove(board, this, candidateDestinationDestination));
                    }
                }
            }


        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public boolean isColumnExclusion(int currentPosition, int candidateOffset) {
        return false;
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
