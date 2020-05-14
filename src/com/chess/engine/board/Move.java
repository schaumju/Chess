package com.chess.engine.board;

import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.Board.*;

/**
 * Name: Justin Schaumberger
 * File: Move.java
 * Date: 5/9/20
 */
public abstract class Move {
    /**
     * the board
     */
    final Board board;
    /**
     * the piece being moved
     */
    final Piece movedPiece;
    /**
     * the location the piece is being moved to
     */
    final int destinationCoordinate;
    /**
     * Representation of an invalid move
     */
    public static final Move NULL_MOVE = new NullMove();

    /**
     * Constructor
     *
     * @param board                 the board
     * @param piece                 the piece being moved
     * @param destinationCoordinate the location the piece is being moved to
     */
    private Move(final Board board, final Piece piece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = piece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode() {
        System.out.println();
        final int prime = 31;
        int result = 1;

        result = prime * result + this.movedPiece.getPiecePosition();
        result = prime *result + this.destinationCoordinate;
        result = prime *result +this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return this.getCurrentCoordinate()==otherMove.getCurrentCoordinate() &&
                this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                this.getAttackedPiece().equals(otherMove.getAttackedPiece());

    }

    /**
     * Gets the destination coordinate
     *
     * @return the destination coordinate
     */
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /**
     * Gets the attacking piece
     * @return the attacking piece
     */
    public Piece getAttackedPiece() {
        return null;
    }

    /**
     * Determines if the move is an attacking move
     * @return true if it is an attacking move
     */
    public boolean isAttack() {
        return false;
    }

    /**
     * Determines if the move is a castle move
     * @return true if it is a castle move
     */
    public boolean isCastlingMove() {
        return false;
    }

    /**
     * Gets the current position of the piece before it is moved
     * @return the coordinate of the piece before it is moved
     */
    private int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    /**
     * Getter for the moved piece
     *
     * @return the moved piece
     */
    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public Board execute() {
        // Use a builder to create a new board
        final Builder builder = new Builder();

        // set the current player's pieces
        for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        // Set the opponent's pieces
        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        // Set the piece to the new location
        builder.setPiece(this.movedPiece.movePiece(this));
        // Set the move maker to the opponent
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public static final class MajorMove extends Move {
        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        public MajorMove(final Board board, final Piece piece, final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }


    }

    /**
     * Attacking move (capture a piece)
     */
    public static class AttackMove extends Move {
        /**
         * The piece being attacked
         */
        final Piece attackedPiece;

        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        public AttackMove(final Board board, final Piece piece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackMove)) {
                return false;
            }
            final Move otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());

        }
    }

    /**
     * Representation of a pawn move
     */
    public static final class PawnMove extends Move {


        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        private PawnMove(final Board board, final Piece piece, final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }
    }

    /**
     * Representation of a Pawn attack move
     */
    public static class PawnAttackMove extends AttackMove {


        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        private PawnAttackMove(final Board board, final Piece piece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    /**
     * Representation of En Passant
     */
    public static final class PawnEnPassantAtttackMove extends PawnAttackMove {
        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         * @param attackedPiece
         */
        private PawnEnPassantAtttackMove(Board board, Piece piece, int destinationCoordinate, Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }
    }

    /**
     * Representation of a Pawn Jump move (pawn moves 2 spaces)
     */
    public static final class PawnJump extends Move {

        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        private PawnJump(final Board board, final Piece piece, final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // Move the pawn
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    static abstract class CastleMove extends Move {

        /**
         * The rook involved in the castle
         */
        protected final Rook castleRook;
        /**
         * The start location of the rook
         */
        protected final int castleRookStart;
        /**
         * The end location of the rook
         */
        protected final int castleRookDestination;

        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         * @param castleRook            the rook involved in the castle
         * @param castleRookStart       the start location of the rook
         * @param castleRookDestination the end location of the rook
         */
        private CastleMove(final Board board, final Piece piece, final int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }

            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            // TODO look into the first move on normal piece
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();

        }
    }

    /**
     * Castle involving the king side rook
     */
    public static final class KingSideCastleMove extends CastleMove {
        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        public KingSideCastleMove(final Board board, final Piece piece, final int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public String toString() {
            return "O-O";
        }


    }

    /**
     * Castle involving the queen side rook
     */
    public static final class QueenSideCastleMove extends CastleMove {
        /**
         * Constructor
         *
         * @param board                 the board
         * @param piece                 the piece being moved
         * @param destinationCoordinate the location the piece is being moved to
         */
        public QueenSideCastleMove(final Board board, final Piece piece, final int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "O-O-O";
        }
    }



    public static final class NullMove extends Move {
        /**
         * Constructor
         */
        private NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move");
        }
    }

    /**
     * Used to creates moves
     */
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable");
        }

        /**
         * Creates a move
         * @param board the board
         * @param currentCoordinate current coordinate of the piece being moved
         * @param destinationCoordinate destination coordinate of the piece being moved
         * @return
         */
        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }




}
