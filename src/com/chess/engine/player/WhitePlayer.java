package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

/**
 * Name: Justin Schaumberger
 * File: WhitePlayer.java
 * Date: 5/10/20
 */
public class WhitePlayer extends Player {

    /**
     * Constructor
     * @param board the board
     * @param whiteStandardLegalMoves the legal moves for white
     * @param blackStandardLegalMoves the legal moves for black
     */
    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegal, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();


        // Make sure it is the king's first move and the player is not in check currently
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // King side Castle

            // Check there are no pieces between the king and rook
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() &&
                    rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58,opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59,opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {

                    // Make sure the king doesn't move through check
                    if (Player.calculateAttacksOnTile(61,opponentLegals).isEmpty() && Player.calculateAttacksOnTile(62,opponentLegals).isEmpty() && Player.calculateAttacksOnTile(63,opponentLegals).isEmpty()) {
                        kingCastles.add(new KingSideCastleMove(this.board,
                                                                    this.playerKing,
                                                                    60,
                                                                    (Rook)rookTile.getPiece(),
                                                                     rookTile.getTileCoordinate(),
                                                                    61));
                    }
                }
            }
            // Queen side castle

            // Check there are no pieces between the king and rook
            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // Make sure the king doesn't move through check and that the piece is a rook
                    if (Player.calculateAttacksOnTile(61,opponentLegals).isEmpty() && Player.calculateAttacksOnTile(62,opponentLegals).isEmpty() && rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new QueenSideCastleMove(this.board,
                                                                     this.playerKing,
                                                                     58,
                                                                     (Rook)rookTile.getPiece(),
                                                                      rookTile.getTileCoordinate(),
                                                                     59));
                    }

                }

            }
        }


        return ImmutableList.copyOf(kingCastles);
    }
}
