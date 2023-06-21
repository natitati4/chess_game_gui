package chess_game_gui.app.GameGUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;

import chess_game_gui.app.MainComponents.*;
import chess_game_gui.app.Pieces.*;

class ChessBoardPanel extends JPanel 
{
    public static final String WHITE_STR = "WHITE";
    public static final String BLACK_STR = "BLACK";

    private JPanel[][] GUIBoard = new JPanel[Board.ROWS][Board.COLS];
    private Board gameBoard = new Board();

    private JPanel boardPanel = new JPanel(new GridLayout(Board.ROWS, Board.COLS));
    
    // white pieces panels
    private static final PieceLabel whitePawn = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_pawn.png")), Piece.WHITE);
    private static final PieceLabel whiteRook = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_rook.png")), Piece.WHITE);
    private static final PieceLabel whiteKnight = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_knight.png")), Piece.WHITE);
    private static final PieceLabel whiteBishop = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_bishop.png")), Piece.WHITE);
    private static final PieceLabel whiteQueen = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_queen.png")), Piece.WHITE);
    private static final PieceLabel whiteKing = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("white_king.png")), Piece.WHITE);

    // black pieces panels'
    private static final PieceLabel blackPawn = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_pawn.png")), Piece.BLACK);
    private static final PieceLabel blackRook = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_rook.png")), Piece.BLACK);
    private static final PieceLabel blackKnight = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_knight.png")), Piece.BLACK);
    private static final PieceLabel blackBishop = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_bishop.png")), Piece.BLACK);
    private static final PieceLabel blackQueen = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_queen.png")), Piece.BLACK);
    private static final PieceLabel blackKing = new PieceLabel(new ImageIcon(ClassLoader.getSystemClassLoader().getResource("black_king.png")), Piece.BLACK);

    private JLabel statusLabel = new JLabel(WHITE_STR);
    private boolean isWhiteTurn = true;
    private ChessCellPanel firstSelectedCell = null;

    public ChessBoardPanel() 
    {
        // Add listeners to all cells
        setLayout(new GridLayout(Board.ROWS, Board.COLS));
        MyMouse myMouse = new MyMouse();
        for (int row = 0; row < Board.ROWS; row++) 
        {
            for (int col = 0; col < Board.COLS; col++) 
            {
                ChessCellPanel cellPanel = new ChessCellPanel(row, col);
                cellPanel.addMouseListener(myMouse);
                boardPanel.add(cellPanel);
                GUIBoard[row][col] = cellPanel;
            }
        }
        
        // Add all pieces components
        for (int i = 0; i < Board.ROWS; i++)
        {
            for (int j = 0; j < Board.COLS; j++)
            {
                if (gameBoard.getCell(i, j).getPiece() != null)
                    addPiece(i, j, pieceToLabel(gameBoard.getCell(i, j).getPiece()));
            }
        }

        JPanel statusPanel = new JPanel();
        statusPanel.add(new JLabel("Turn: "));
        statusPanel.add(statusLabel);

        setLayout(new BorderLayout());
        add(boardPanel);
        add(statusPanel, BorderLayout.PAGE_END);
    }

    private class MyMouse extends MouseAdapter 
    {
        @Override
        public void mousePressed(MouseEvent e) 
        {
            ChessCellPanel cellPanel = (ChessCellPanel) e.getSource();
            if (cellPanel.getComponents().length > 0 && firstSelectedCell == null) 
            {
                PieceLabel piece = (PieceLabel) cellPanel.getComponent(0);
                if (piece.getColor() == (isWhiteTurn ? Piece.WHITE : Piece.BLACK)) 
                {
                    firstSelectedCell = cellPanel;
                    cellPanel.setBackground(Color.YELLOW);
                }

            } 

            else if (firstSelectedCell != null) 
            {
                if (cellPanel == firstSelectedCell) 
                {
                    firstSelectedCell.setBackground(cellPanel.getCellColor());
                    firstSelectedCell = null;
                } 
                
                else 
                {
                    Cell srcCell = gameBoard.getCell(firstSelectedCell.getRow(), firstSelectedCell.getCol());
                    Cell destCell = gameBoard.getCell(cellPanel.getRow(), cellPanel.getCol());
                    
                    // If couldn't make move, return false;
                    if (!gameBoard.movePiece(srcCell, destCell, true))
                        return;
                    
                    // Handle special moves
                    handleCastle(destCell);
                    handleEnPassant(destCell);

                    cellPanel.removeAll();
                    cellPanel.add(firstSelectedCell.getComponent(0));
                    firstSelectedCell.removeAll();
                    firstSelectedCell.setBackground(firstSelectedCell.getCellColor());
                    firstSelectedCell = null;
                    
                    // Handle promotion after painting
                    handlePromotion(cellPanel, isWhiteTurn ? Piece.WHITE : Piece.BLACK);

                    isWhiteTurn = !isWhiteTurn;
                    statusLabel.setText(isWhiteTurn ? WHITE_STR : BLACK_STR);
                }

                boardPanel.revalidate();
                boardPanel.repaint();

                // Get the game state (pass the other color, cause in case it's checkmate, after a move, we need to check the other color
                int gameState = gameBoard.getGameState(isWhiteTurn ? Piece.WHITE : Piece.BLACK);
                
                // Check the game state
                if (gameState == Board.CHECKMATE) 
                {
                    String colorWon = isWhiteTurn ? BLACK_STR : WHITE_STR;

                    // Display a message dialog
                    JOptionPane.showMessageDialog(null, "Checkmate! " + colorWon + " wins.", "Game over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // Terminate the application
                }

                else if (gameState == Board.STALEMATE)
                {
                    // Display a message dialog
                    JOptionPane.showMessageDialog(null, "Stalemate! It's a draw.", "Game over", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); // Terminate the application
                }
            }
        }
    }

    private void addPiece(int i, int j, PieceLabel piece) 
    {
        ChessCellPanel cellPanel = (ChessCellPanel) GUIBoard[i][j];
        PieceLabel newPiece = new PieceLabel((ImageIcon)piece.getIcon(), piece.getColor());
        cellPanel.add(newPiece);
    }

    private PieceLabel pieceToLabel(Piece piece)
    { 
        switch (piece.getClass().getSimpleName())
        {
            case "Pawn":
            {
                return piece.getColor() == Piece.WHITE ? whitePawn : blackPawn;
            }
            
            case "Rook":
            {
                return piece.getColor() == Piece.WHITE ? whiteRook : blackRook;
            }

            case "Knight":
            {
                return piece.getColor() == Piece.WHITE ? whiteKnight : blackKnight;
            }

            case "Bishop":
            {
                return piece.getColor() == Piece.WHITE ? whiteBishop : blackBishop;
            }

            case "Queen":
            {
                return piece.getColor() == Piece.WHITE ? whiteQueen : blackQueen;
            }

            case "King":
            {
                return piece.getColor() == Piece.WHITE ? whiteKing : blackKing;
            }

            default:
                return null;
        }
    }

    private void handleCastle(Cell destCell)
    {
        // Handle castle
        Piece pieceJustMoved = destCell.getPiece();
        if (pieceJustMoved instanceof King)
        {
            King kingJustCasteled = (King)pieceJustMoved;
            int kingDestRow = destCell.getRow();
            int kingDestCol = destCell.getCol();

            // Short castle
            if (kingJustCasteled.hasJustShortCasteled()) 
            {
                Rook castlingRook = (Rook)gameBoard.getCell(kingDestRow, kingDestCol - 1).getPiece();
            
                // Remove rook from it's starting cell and put it in the correct square
                gameBoard.setCell(kingDestRow, kingDestCol - 1, null);
                GUIBoard[kingDestRow][kingDestCol - 1].removeAll();

                gameBoard.setCell(kingDestRow, kingDestCol + 1, castlingRook);
                addPiece(kingDestRow, kingDestCol + 1, pieceToLabel(castlingRook));

                kingJustCasteled.setHasJustShortCasteled(false);
            }

            // Long castle
            else if (kingJustCasteled.hasJustLongCasteled()) 
            {
                Rook castlingRook = (Rook)gameBoard.getCell(kingDestRow, kingDestCol + 2).getPiece();
            
                // Remove rook from it's starting cell and put it in the correct square
                gameBoard.setCell(kingDestRow, kingDestCol + 2, null);
                GUIBoard[kingDestRow][kingDestCol + 2].removeAll();

                gameBoard.setCell(kingDestRow, kingDestCol - 1, castlingRook);
                addPiece(kingDestRow, kingDestCol - 1, pieceToLabel(castlingRook));

                kingJustCasteled.setHasJustLongCasteled(false);
            }
        }
    }

    private void handleEnPassant(Cell destCell)
    {
        Move lastMove = gameBoard.getLastMove();
        if (lastMove.isEnPassent())
        {
            if (destCell.getPiece().getColor() == Piece.WHITE)
            {
                gameBoard.setCell(destCell.getRow() - 1, destCell.getCol(), null);
                GUIBoard[destCell.getRow() - 1][destCell.getCol()].removeAll();
            }
            else
            {
                gameBoard.setCell(destCell.getRow() + 1, destCell.getCol(), null);
                GUIBoard[destCell.getRow() + 1][destCell.getCol()].removeAll();
            }

            lastMove.setEnPassent(false);
        }
    }

    private void handlePromotion(ChessCellPanel cellPanel, int color)
    {
        // Handle promotion
        int lastRowForColor = isWhiteTurn ? Board.COLS - 1 : 0; // For white it's the 8th rank, for black it's the 1st rank

        for (int i = 0; i < Board.COLS; i++)
        {
            Cell lastRowCell = gameBoard.getCell(lastRowForColor, i);

            if (lastRowCell.getPiece() instanceof Pawn)
            {
                Piece promotedPiece = promotePawn(cellPanel, isWhiteTurn ? Piece.WHITE : Piece.BLACK);
                lastRowCell.setPiece(promotedPiece);
                break; // Break cause found promotion
            }
        }
    }

    private Piece promotePawn(ChessCellPanel cellPanel, int color)
    {
        // Create the dialog
        JDialog promotionDialog = new JDialog();
        promotionDialog.setTitle("Pawn Promotion");
        promotionDialog.setModal(true);
        promotionDialog.setSize(ChessCellPanel.CELL_SIZE * 2, ChessCellPanel.CELL_SIZE * 2);
        promotionDialog.setLayout(new GridLayout(2, 2));
        promotionDialog.setLocationRelativeTo(boardPanel); // Replace 'mainWindow' with your main window reference

        PieceLabel queenLabel = color == Piece.WHITE ? whiteQueen : blackQueen;
        PieceLabel rookLabel = color == Piece.WHITE ? whiteRook : blackRook;
        PieceLabel bishopLabel = color == Piece.WHITE ? whiteBishop : blackBishop;
        PieceLabel knightLabel = color == Piece.WHITE ? whiteKnight : blackKnight;
        
        AtomicInteger selectedPiece = new AtomicInteger(0);

        // Add action listeners to the labels
        queenLabel.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Handle the selection of white queen
                selectedPiece.set(1);
                promotionDialog.dispose(); // Close the dialog
            }
        });

        rookLabel.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Handle the selection of white rook
                selectedPiece.set(2);
                promotionDialog.dispose(); // Close the dialog
            }
        });

        bishopLabel.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Handle the selection of black queen
                selectedPiece.set(3);
                promotionDialog.dispose(); // Close the dialog
            }
        });

        knightLabel.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                // Handle the selection of black rook
                selectedPiece.set(4);
                promotionDialog.dispose(); // Close the dialog
            }
        });

        // Add the labels to the dialog
        promotionDialog.add(queenLabel);
        promotionDialog.add(rookLabel);
        promotionDialog.add(bishopLabel);
        promotionDialog.add(knightLabel);

        // Show the dialog and wait for the player's choice
        promotionDialog.setVisible(true);
        
        // Determine the selected piece based on the player's choice
        switch (selectedPiece.get())
        {
            case 1:
            {
                cellPanel.removeAll();
                addPiece(cellPanel.getRow(), cellPanel.getCol(), queenLabel);
                return new Queen(color);
            }

            case 2:
            {
                cellPanel.removeAll();
                addPiece(cellPanel.getRow(), cellPanel.getCol(), rookLabel);
                return new Rook(color);
            }

            case 3:
            {
                cellPanel.removeAll();
                addPiece(cellPanel.getRow(), cellPanel.getCol(), bishopLabel);
                return new Bishop(color);
            }

            case 4:
            {
                cellPanel.removeAll();
                addPiece(cellPanel.getRow(), cellPanel.getCol(), knightLabel);
                return new Knight(color);
            }

            default:
                return promotePawn(cellPanel, color);
        }
    }
}
