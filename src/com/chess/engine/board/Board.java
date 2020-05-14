package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

/**
 * Name: Justin Schaumberger
 * File: Board.java
 * Date: 5/9/20
 */
public class Board
{

    /**
     * The game board
     */
    private final List<Tile> gameBoard;
    /**
     * The collection of all the white pieces
     */
    private final Collection<Piece> whitePieces;
    /**
     * The collection of all the black pieces
     */
    private final Collection<Piece> blackPieces;

    /**
     * The white player
     */
    private final WhitePlayer whitePlayer;

    /**
     * The black player
     */
    private final BlackPlayer blackPlayer;
    /**
     * The current player
     */
    private final Player currentPlayer;

    /**
     * Gets the white player
     * @return the white player
     */
    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Gets the black player
     * @return the black player
     */
    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Constructor
     */
    private Board(final Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces= calculateActivePieces(this.gameBoard,Alliance.WHITE);
        this.blackPieces= calculateActivePieces(this.gameBoard,Alliance.BLACK);

        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);

        this.whitePlayer= new WhitePlayer(this,whiteStandardLegalMoves,blackStandardLegalMoves);
        this.blackPlayer= new BlackPlayer(this,blackStandardLegalMoves,whiteStandardLegalMoves);
        this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",tileText));
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }


    /**
     * Returns a list of all the legal moves for a given alliance
     * @param pieces list of all the pieces
     * @return the list of all the possible moves
     */
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();

        /**
         * Go through each piece, get the legal moves, and add it to the list
         */
        for (final Piece piece:pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));

        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a list of all the pieces of a certain alliance
     * @param gameBoard the board
     * @param alliance the alliance we want
     * @return a list of all the pieces of a given alliance
     */
    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
        final List<Piece> activePieces = new ArrayList<>();

        for(final Tile tile: gameBoard){
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance) {
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    /**
     * Crates the game board
     * @return a list of all the tiles on the board
     */
    private static List<Tile> createGameBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.createTile(i,builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    /**
     * Create a new standard chess board
     * @return the chess board
     */
    public static Board createStandardBoard() {
        final Builder builder = new Builder();
        // Black pieces
        builder.setPiece(new Rook(0,Alliance.BLACK));
        builder.setPiece(new Knight(1,Alliance.BLACK));
        builder.setPiece(new Bishop(2,Alliance.BLACK));
        builder.setPiece(new Queen(3,Alliance.BLACK));
        builder.setPiece(new King(4,Alliance.BLACK));
        builder.setPiece(new Bishop(5,Alliance.BLACK));
        builder.setPiece(new Knight(6,Alliance.BLACK));
        builder.setPiece(new Rook(7,Alliance.BLACK));

        // Black Pawns
        for (int i = 8; i < 16; i++) {
            builder.setPiece(new Pawn(i,Alliance.BLACK));
        }

        // White pieces
        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Knight(57,Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Queen(59,Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new Knight(62,Alliance.WHITE));
        builder.setPiece(new Rook(63,Alliance.WHITE));

        // White Pawns
        for (int i = 48; i <56; i++) {
            builder.setPiece(new Pawn(i,Alliance.WHITE));
        }

        // white to move
        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();

    }

    /**
     * Gets all the legal moves for both players
     * @return the legal moves for both players
     */
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer
        .getLegalMoves()));
    }

    /**
     * Returns the black pieces
     * @return all the black pieces
     */
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    /**
     * Returns all the white pieces
     * @return all the white pieces
     */
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    /**
     * Get the tile at a given coordinate
     * @param tileCoordinate the tile coordinate
     * @return the tile at the given coordinate
     */
    public Tile getTile(final int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }

    /**
     * Gets the current player
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Use to build an instance of the board
     */
    public static class Builder {
        /**
         * The board
         */
        Map<Integer, Piece> boardConfig;
        /**
         * The player to make the next move
         */
        Alliance nextMoveMaker;

        private Pawn enPassantPawn;

        /**
         * Default constructor
         */
        public Builder() {
            this.boardConfig = new HashMap<>();

        }

        /**
         * Sets a piece on the board
         * @param piece the piece to be put on the board
         * @return the builder object
         */
        public Builder setPiece(final Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        /**
         * Change whose move it is
         * @param nextMoveMaker the color that makes the next move
         * @return
         */
        public Builder setMoveMaker(final Alliance nextMoveMaker) {
            this.nextMoveMaker=nextMoveMaker;
            return this;
        }

        /**
         * Builds a new board
         * @return the board object
         */
        public Board build() {
            return new Board(this);
        }


        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
