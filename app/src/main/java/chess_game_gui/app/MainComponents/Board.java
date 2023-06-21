package chess_game_gui.app.MainComponents;

import chess_game_gui.app.Pieces.*;

public class Board 
{
    public static final int ACTIVE = 0;
    public static final int CHECKMATE = 1;
    public static final int STALEMATE = 2;

    public static final int ROWS = 8;
    public static final int COLS = 8;

    private Cell board[][];
    private Move lastMove = new Move();

    public Board()
    {
        this.board = new Cell[ROWS][COLS];
        
        // Initialize white piece
        board[0][0] = new Cell(0, 0, new Rook(Piece.WHITE));
        board[0][1] = new Cell(0, 1, new Knight(Piece.WHITE));
        board[0][2] = new Cell(0, 2, new Bishop(Piece.WHITE));
        board[0][3] = new Cell(0, 3, new King(Piece.WHITE));
        board[0][4] = new Cell(0, 4, new Queen(Piece.WHITE));
        board[0][5] = new Cell(0, 5, new Bishop(Piece.WHITE));
        board[0][6] = new Cell(0, 6, new Knight(Piece.WHITE));
        board[0][7] = new Cell(0, 7, new Rook(Piece.WHITE));
        
        // Initialize black piece
        board[7][0] = new Cell(7, 0, new Rook(Piece.BLACK));
        board[7][1] = new Cell(7, 1, new Knight(Piece.BLACK));
        board[7][2] = new Cell(7, 2, new Bishop(Piece.BLACK));
        board[7][3] = new Cell(7, 3, new King(Piece.BLACK));
        board[7][4] = new Cell(7, 4, new Queen(Piece.BLACK));
        board[7][5] = new Cell(7, 5, new Bishop(Piece.BLACK));
        board[7][6] = new Cell(7, 6, new Knight(Piece.BLACK));
        board[7][7] = new Cell(7, 7, new Rook(Piece.BLACK));

        // Initialize all pawns
        for (int i = 0; i < COLS; i++)
        {
            board[1][i] = new Cell(1, i, new Pawn(Piece.WHITE));
            board[6][i] = new Cell(6, i, new Pawn(Piece.BLACK));
        }

        // Initialize all other cells to null
        for (int i = 2; i < 6; i++) 
        {
            for (int j = 0; j < COLS; j++) 
            {
                board[i][j] = new Cell(i, j, null);
            }
        }
    }

    public Cell getCell(int row, int col) 
    {
        return board[row][col];
    }

    public void setCell(int row, int col, Piece piece) 
    {
        board[row][col] = new Cell(row, col, piece);
    }

    public boolean terminalBoard()
    {
        return false;
    }

    /**
     * @return Cell return the board
     */
    public Cell[][] getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Cell board[][]) {
        this.board = board;
    }

    /**
     * @return Move return the lastMove
     */
    public Move getLastMove() {
        return lastMove;
    }

    /**
     * @param lastMove the lastMove to set
     */
    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public boolean movePiece(Cell src, Cell dest, boolean isRealMove)
    {
        Piece srcPiece = src.getPiece();
        Piece destPiece = dest.getPiece();
        
        if (!srcPiece.validateMove(this, src, dest))
            return false;

        dest.setPiece(srcPiece); // Cell piece landed on
        src.setPiece(null); // Cell piece moved from

        // If the one that moved is in check after moving, it's an illegal move
        if (this.isInCheck(srcPiece.getColor()))
        {
            // Revert back to before moving
            src.setPiece(srcPiece);
            dest.setPiece(destPiece);
            return false;
        }

        // If not real move, i.e it was "emulated" to check for the game state, return to original position
        if (!isRealMove)
        {
            src.setPiece(srcPiece);
            dest.setPiece(destPiece);

            if (srcPiece instanceof King)
            {
                King king = (King)srcPiece;
                king.setMoved(false); // Casuse not really moved
            }

            else if (srcPiece instanceof Rook)
            {
                Rook rook = (Rook)srcPiece;
                rook.setMoved(false); // Casuse not really moved
            }
        }

        // If was real, set it to the last move
        else
        {
            lastMove.setStart(src);
            lastMove.setEnd(dest);
        }

        return true;
    }

    public boolean isInCheck(int color)
    {
        Cell kingCell = this.getKingCell(color);
        return isCellAttackedByColor(kingCell, color ^ Piece.BLACK); // To change to opposite color
    }
    
    // Checks whether a square is attacked by a certain color
    public boolean isCellAttackedByColor(Cell cell, int color)
    {
        int row = cell.getRow();
        int col = cell.getCol();

        return checkRooksAndQueensAttacks(row, col, color) ||
                checkKnightsAttacks(row, col, color) ||
                checkBishopsAndQueensAttacks(row, col, color) ||
                checkPawnsAttacks(row, col, color);
    }

    private Cell getKingCell(int color)
    {
        for (int i = 0; i < ROWS; i++)
        {
            for (int j = 0; j < COLS; j++)
            {
                Piece currPiece = this.board[i][j].getPiece();

                if (currPiece == null)
                    continue;

                if (currPiece instanceof King && currPiece.getColor() == color)
                    return this.board[i][j];
            }
        }
        
        // Never reached, since will always find king
        return null;
    }

    // If rook or queen of the given color attacks the given square
    private boolean checkRooksAndQueensAttacks(int row, int col, int color)
    {
        // Horizontally left
        for (int i = col - 1; i >= 0; i--)
        {
            Piece currPiece = this.board[row][i].getPiece();
            if (currPiece == null)
                continue;

            // If found that it is not a rook or a queen, or it is friendly piece (it's blocking the attack)
            else if (!(currPiece instanceof Rook || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;
         
            // Else, found an enemy rook
            else
                return true;
        }

        // Horizontally right
        for (int i = col + 1; i < COLS; i++)
        {
            Piece currPiece = this.board[row][i].getPiece();
            if (currPiece == null)
                continue;

            // If found that it is not a rook or a queen, or it is friendly piece (it's blocking the check)
            else if (!(currPiece instanceof Rook || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;
         
            // Else, found an enemy rook
            else
                return true;
        }

        // vertically down
        for (int i = row - 1; i >= 0; i--)
        {
            Piece currPiece = this.board[i][col].getPiece();
            if (currPiece == null)
                continue;

            // If found that it is not a rook or a queen, or it is friendly piece (it's blocking the check)
            else if (!(currPiece instanceof Rook || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;
         
            // Else, found an enemy rook
            else
                return true;
        }

        // vertically up
        for (int i = row + 1; i < ROWS; i++)
        {
            Piece currPiece = this.board[i][col].getPiece();
            if (currPiece == null)
                continue;

            // If found that it is not a rook or a queen, or it is friendly piece (it's blocking the check)
            else if (!(currPiece instanceof Rook || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;
         
            // Else, found an enemy rook
            else
                return true;
        }

        // If didn't return true, not rooks checking
        return false;
    }

    // If knight of the given color attacks the given square
    private boolean checkKnightsAttacks(int row, int col, int color)
    {
        // All possible knight positions that can attack the square
        int knightCells[][] = {{row - 1, col - 2}, {row - 1, col + 2},
                                {row + 1, col - 2}, {row + 1, col + 2},
                                {row - 2, col - 1}, {row - 2, col + 1},
                                {row + 2, col - 1}, {row + 2, col + 1}};

        for (int i = 0; i < 8; i++)
        {
            // If not in bounds
            if (!(0 <= knightCells[i][0] && knightCells[i][0] < ROWS) || !(0 <= knightCells[i][1] && knightCells[i][1] < COLS))
                continue;

            Piece currPiece = this.board[knightCells[i][0]][knightCells[i][1]].getPiece();
            if (currPiece == null)
                continue;

            if (currPiece instanceof Knight && currPiece.getColor() == color)
                return true;
        }

        return false;
    }

    // If bishop or queen of the given color attacks the given square
    private boolean checkBishopsAndQueensAttacks(int row, int col, int color)
    {
        // Check left up
        int currRow = row - 1;
        int currCol = col - 1;
        while (currRow != -1 && currCol != -1) 
        {
            Piece currPiece = this.board[currRow][currCol].getPiece();

            if (currPiece == null)
            {
                currRow--;
                currCol--;
                continue;
            }
            
            // If found not bishop or friendly piece, it's blocking the check
            else if (!(currPiece instanceof Bishop || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;

            else
                return true;
        }

        // Check right up
        currRow = row - 1;
        currCol = col + 1;
        while (currRow != -1 && currCol != COLS) 
        {
            Piece currPiece = this.board[currRow][currCol].getPiece();

            if (currPiece == null)
            {
                currRow--;
                currCol++;
                continue;
            }
            
            // If found not bishop or friendly piece, it's blocking the check
            else if (!(currPiece instanceof Bishop || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;

            else
                return true;
        }

        // Check left down
        currRow = row + 1;
        currCol = col - 1;
        while (currRow != ROWS && currCol != -1) 
        {
            Piece currPiece = this.board[currRow][currCol].getPiece();

            if (currPiece == null)
            {
                currRow++;
                currCol--;
                continue;
            }
            
            // If found not bishop or friendly piece, it's blocking the check
            else if (!(currPiece instanceof Bishop || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;

            else
                return true;
        }

        // Check right down
        currRow = row + 1;
        currCol = col + 1;
        while (currRow != ROWS && currCol != COLS)
        {
            Piece currPiece = this.board[currRow][currCol].getPiece();

            if (currPiece == null)
            {
                currRow++;
                currCol++;
                continue;
            }
            
            // If found not bishop or friendly piece, it's blocking the check
            else if (!(currPiece instanceof Bishop || currPiece instanceof Queen) || currPiece.getColor() != color)
                break;

            else
                return true;
        }

        return false;
    }

    // If pawn of the given color attacks the given square
    private boolean checkPawnsAttacks(int row, int col, int color)
    {
        Piece pieceToLeftDiagonal = null;
        Piece pieceToRightDiagonal = null;

        if (color == Piece.BLACK)
        {
            // Check if in bounds
            if (0 <= col - 1)
                pieceToLeftDiagonal = this.getCell(row + 1, col - 1).getPiece();
            
            // Check if in bounds
            if (col + 1 < Board.COLS)
                pieceToRightDiagonal = this.getCell(row + 1, col + 1).getPiece();
        }

        else       
        {
            // Check if in bounds
            if (0 <= col - 1)
                pieceToLeftDiagonal = this.getCell(row - 1, col - 1).getPiece();

            // Check if in bounds
            if (col + 1 < Board.COLS)
                pieceToRightDiagonal = this.getCell(row - 1, col + 1).getPiece();
        }

        if (pieceToLeftDiagonal != null)
        {
            if (pieceToLeftDiagonal instanceof Pawn && pieceToLeftDiagonal.getColor() == color)
                return true;
        }

        if (pieceToRightDiagonal != null)
        {
            if (pieceToRightDiagonal instanceof Pawn && pieceToRightDiagonal.getColor() == color)
                return true;
        }

        return false;
    }

    public int getGameState(int color)
    {
        // If has at least 1 possible move, the game is active
        if (hasMoves(color))
            return ACTIVE;
        
        // No possible moves, so either checkmate or stalemate
        // Check if the king is in check
        if (this.isInCheck(color)) 
        {
            // King is in check, it is checkmate
            return CHECKMATE;
        } 
        
        else 
        {
            // King is not in check, it is stalemate
            return STALEMATE;
        }
    }

    private boolean hasMoves(int color)
    {
        // Iterate over all cells on the board
        for (int row = 0; row < ROWS; row++) 
        {
            for (int col = 0; col < COLS; col++) 
            {
                Cell cell = this.board[row][col];
                Piece piece = cell.getPiece();

                // Check if the cell contains a piece of the specified color
                if (piece != null && piece.getColor() == color) 
                {
                    // Get the possible moves for the current piece
                    if(piece.hasMoves(this, cell))
                        return true;
                }
            }
        }
    
        return false;
    }
}
