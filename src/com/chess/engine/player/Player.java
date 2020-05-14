package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Name: Justin Schaumberger
 * File: Player.java
 * Date: 5/10/20
 */
public abstract class Player {

    /**
     * The board
     */
    protected final Board board;
    /**
     * The player's king
     */
    protected final King playerKing;
    /**
     * List of legal moves
     */
    protected final Collection<Move> legalMoves;
    /**
     * Determines if the player is in check
     */
    private final boolean isInCheck;

    /**
     * Constructor
     * @param board the board
     * @param opponentMoves the opponents's legal moves
     * @param legalMoves the list of legal moves
     */
    protected Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    /**
     * Checks if the player is in check
     * @param piecePosition the position of the king
     * @param moves the opponents moves
     * @return
     */
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();

        for (final Move move : moves) {
            if (piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }

    /**
     * Get the king for the player on the board
     * @return the king
     */
    protected King establishKing(){
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King)piece;
            }
        }
        // If we can't establish a King there is an issue
        throw new RuntimeException("Should not reach here! Not a valid board");
    }

    /**
     * Determines if the move is legal
     * @param move the move being proposed
     * @return true if the move is legal and false otherwise
     */
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    /**
     * Determines if the player is in check
     * @return true if the player is in check
     */
    public boolean isInCheck() {
        return this.isInCheck;
    }

    /**
     * Determines if the player is in check mate
     * @return true if the player is in check mate
     */
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    /**
     * Determines if the player can make any legal moves (does not leave the player in check)
     * @return true if the player can make a move
     */
    protected boolean hasEscapeMoves() {
        // Try to make every move and if you can then it is not check mate
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the player is in stale mate
     * @return true if the player is in stale mate
     */
    public boolean isInStaleMate() {
        // If the player is not in Check and can't make a move then it's stale mate
        return !isInCheck && !hasEscapeMoves();
    }

    /**
     * Determines if the player has castled
     * @return true if the player has castled
     */
    public boolean isCastled() {
        return false;
    }

    /**
     * Performs the actual move
     * @param move the move being made
     * @return the MoveTransition object with the move
     */
    public MoveTransition makeMove(final Move move) {
        // If the move is illegal
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();

        // get all moves against the current player's king
        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.getCurrentPlayer().getLegalMoves());

        // If there are King attacks, the move is illegal because it leaves the player in check
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        // Otherwise the move is legal and we can make it
        return new MoveTransition(transitionBoard,move, MoveStatus.DONE);
    }

    /**
     * Returns the player's king
     * @return the player's king
     */
    private Piece getPlayerKing() {
        return this.playerKing;
    }

    /**
     * Returns the legal moves
     * @return the legal moves
     */
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    /**
     * Returns all the active pieces
     * @return a collection of all the pieces
     */
    public abstract Collection<Piece> getActivePieces();

    /**
     * Gets the Alliance of the player
     * @return the Alliance of the player
     */
    public abstract Alliance getAlliance();

    /**
     * Gets the opponent
     * @return the opponent
     */
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegal, Collection<Move> opponentLegals);
}
