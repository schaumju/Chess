package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

/**
 * Name: Justin Schaumberger
 * File: JChess.java
 * Date: 5/10/20
 */
public class JChess {
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
        Table table = new Table();
    }
}
