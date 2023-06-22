package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.Board;
import chess_game_gui.app.MainComponents.Cell;
import chess_game_gui.app.MainComponents.Piece;

public class Rook extends Piece
{
    private boolean hasMoved = false; // To check for castle

    public Rook(int color)
    {
        super(color);
    }

    public boolean hasMoved()
    {
        return hasMoved;
    }

    public void setMoved(boolean hasMoved)
    {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean validateMove(Board board, Cell start, Cell end) 
    {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Checked if moved horizontally or vartically
        if (!((startRow == endRow && startCol != endCol) ||
                (startRow != endRow && startCol == endCol)))
            return false;

        // Check if same color piece at end
        if (end.getPiece() != null)
        {
            if (start.getPiece().getColor() == end.getPiece().getColor())
                return false;
        }

        // Check if there's a piece in the way
        // Moved horizontally
        if (startRow == endRow)
        {
            for (int i = Math.min(startCol, endCol) + 1; i < Math.max(startCol, endCol); i++)
            {
                if (board.getCell(startRow, i).getPiece() != null)
                    return false;
            }
        }

        // Moved vertically
        else
        {
            for (int i = Math.min(startRow, endRow) + 1; i < Math.max(startRow, endRow); i++)
            {
                if (board.getCell(i, startCol).getPiece() != null)
                    return false;
            }
        }

        if (!hasMoved)
            hasMoved = true;

        // If all assertions passed, return true
        return true;
    }

    @Override
    public boolean hasMoves(Board board, Cell start) 
    {
        int row = start.getRow();
        int col = start.getCol();

        // Horizontally left
        for (int i = col - 1; i >= 0; i--)
        {
            Cell destCell = board.getCell(row, i);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }

        // Horizontally right
        for (int i = col + 1; i < Board.COLS; i++)
        {
            Cell destCell = board.getCell(row, i);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }

        // vertically down
        for (int i = row - 1; i >= 0; i--)
        {
            Cell destCell = board.getCell(i, col);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }

        // vertically up
        for (int i = row + 1; i < Board.ROWS; i++)
        {
            Cell destCell = board.getCell(i, col);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }

        // If didn't return true, not rook moves
        return false;
    }
}
