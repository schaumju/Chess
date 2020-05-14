package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/**
 * Name: Justin Schaumberger
 * File: MoveTransition.java
 * Date: 5/11/20
 */
public class MoveTransition {

    /**
     * The board
     */
    public final Board transitionBoard;
    /**
     * The move
     */
    private final Move move;
    /**
     * Controls the validity of the move (Whether it is ok or not)
     */
    private final MoveStatus moveStatus;

    /**
     * Constructor
     * @param transitionBoard the board
     * @param move the move
     * @param moveStatus controls the validity of the move
     */
    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    /**
     * Gets the move status
     * @return the move status
     */
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    /**
     * Gets the board
     * @return the board
     */
    public Board getBoard() {
        return this.transitionBoard;
    }
}
