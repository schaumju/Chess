package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

/**
 * Name: Justin Schaumberger
 * File: Alliance.java
 * Date: 5/9/20
 */
public enum Alliance {
    WHITE{
        @Override
        public int getDirection(){
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };

    /**
     * Gets the direction of the piece based on the alliance
     * @return -1 for white and 1 for black
     */
    public abstract int getDirection();

    /**
     * Determines if the alliance is white
     * @return true if the alliance is white
     */
    public abstract boolean isWhite();
    /**
     * Determines if the alliance is black
     * @return true if the alliance is black
     */
    public abstract boolean isBlack();
    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);
}
