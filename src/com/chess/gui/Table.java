package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.board.Board.*;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/**
 * Name: Justin Schaumberger
 * File: Table.java
 * Date: 5/13/20
 */
public class Table {

    /**
     * The dimensions for the JFrame
     */
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    /**
     * The dimensions for the board
     */
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    /**
     * The dimensions for a tile
     */
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
    /**
     * The chess board being played on
     */
    private Board chessBoard;
    /**
     * The path to the directory with pieces images
     */
    private static String  defaultPieceImagesPath = "Chess/art/simple/";

    /**
     * The frame for the game
     */
    private final JFrame gameFrame;
    /**
     * The board panel for the game that holds all the tiles
     */
    private final BoardPanel boardPanel;
    /**
     * The color for the light tiles
     */
    private final Color lightTileColor = Color.decode("#FFFACD");
    /**
     * Color for the dark tiles
     */
    private final Color darkTileColor = Color.decode("#593E1A");

    /**
     * The starting location of the piece the human wants to move a piece from
     */
    private Tile sourceTile;
    /**
     * The destination location of the piece the human wants to move a piece from
     */
    private Tile destinationTile;
    /**
     * the piece the user wants to move
     */
    private Piece humanMovePiece;


    /**
     * Constructor
     */
    public Table() {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.chessBoard = createStandardBoard();

        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.setVisible(true);

    }

    /**
     * Creates the menu bar
     * @return the JMenuBar
     */
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    /**
     * Creates the file menu
     * @return a JMenu containing the options for the file menu
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file!");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    /**
     * Visual component that represents the board
     */
    private class BoardPanel extends JPanel {
        /**
         * The board that contains all the tiles
         */
        final List<TilePanel> boardTiles;

        /**
         * Constructor
         */
         BoardPanel() {
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();

            // Create all the tiles
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this,i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        /**
         * Draws the board
         * @param board the chess board
         */
        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardTiles) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    /**
     * Visual component that represents a Tile
     */
    private class TilePanel extends JPanel {

        /**
         * The location of the tile on the board
         */
        private final int tileID;

        /**
         * Constructor
         * @param boardPanel
         * @param tileID the tile ID
         */
        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {


                    // If it is a right click (cancel any clicks)
                    if (isRightMouseButton(e)) {
                        sourceTile=null;
                        destinationTile=null;
                        humanMovePiece=null;


                    }
                    // If it is a left click
                    else if (isLeftMouseButton(e)) {
                        // First click
                        if (sourceTile == null) {
                            System.out.println("FIRST CLICK");
                            // Get the tile they click on
                            sourceTile = chessBoard.getTile(tileID);

                            // Get the piece they click on
                            humanMovePiece = sourceTile.getPiece();
                            System.out.println("The piece is "+humanMovePiece);
                            // if there is no piece on the tile, then undo the selection of the tile
                            if (humanMovePiece == null) {
                                sourceTile=null;
                            }

                        } else {
                            // second click
                            System.out.println("Second click");
                            destinationTile = chessBoard.getTile(tileID);
                            System.out.println(destinationTile);
                            final Move move = Move.MoveFactory.createMove(chessBoard,sourceTile.getTileCoordinate(),destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            // If the move is legal and can be made
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getBoard();
                                //TODO add the move that was made to the move log
                            }
                            // Reset the status of the clicks
                            sourceTile = null;
                            destinationTile = null;
                            humanMovePiece = null;

                        }


                        // Update the GUI
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }

                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });


            validate();

        }

        /**
         * Add the image to the tile of pieces
         * @param board the game board
         */
        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            // Check if the tile has a piece
            if (board.getTile(this.tileID).isTileOccupied()) {
                try {
                    // Get the image for the piece
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                                                                                board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1) +
                                                                                board.getTile(this.tileID).getPiece().toString() +
                                                                                ".gif"));
                    // Add the image to the Tile
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        /**
         * Assigns a color to the TilePanel (black or white)
         */
        private void assignTileColor() {
            if (    BoardUtils.EIGHTH_RANK[this.tileID]||
                    BoardUtils.SIXTH_RANK[this.tileID] ||
                    BoardUtils.FOURTH_RANK[this.tileID] ||
                    BoardUtils.SECOND_RANK[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
            } else if(  BoardUtils.SEVENTH_RANK[this.tileID] ||
                        BoardUtils.FIFTH_RANK[this.tileID] ||
                        BoardUtils.THIRD_RANK[this.tileID] ||
                        BoardUtils.FIRST_RANK[this.tileID]) {
                    setBackground(this.tileID % 2  != 0 ? lightTileColor : darkTileColor);
                }
        }


        /**
         * Draws a tile
         * @param board the board
         */
        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }

    }






}
