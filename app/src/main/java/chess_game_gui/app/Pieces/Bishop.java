package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.Board;
import chess_game_gui.app.MainComponents.Cell;
import chess_game_gui.app.MainComponents.Piece;

public class Bishop extends Piece
{
    public Bishop(int color)
    {
        super(color);
    }

    @Override
    public boolean validateMove(Board board, Cell start, Cell end) 
    {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Check if moved diagonally
        if (Math.abs(startRow - endRow) != Math.abs(startCol - endCol))
            return false;
        
        // Check if same color piece at end
        if (end.getPiece() != null) 
        {
            if (start.getPiece().getColor() == end.getPiece().getColor())
                return false;
        }

        // Check if there's a piece in the way
        int rowOffset = (endRow > startRow) ? 1 : -1;
        int colOffset = (endCol > startCol) ? 1 : -1;
        int currRow = startRow + rowOffset;
        int currCol = startCol + colOffset;

        while (currRow != endRow && currCol != endCol) 
        {
            if (board.getCell(currRow, currCol).getPiece() != null)
                return false;

            currRow += rowOffset;
            currCol += colOffset;
        }

        // If all assertions passed, return true
        return true;
    }

    @Override
    public boolean hasMoves(Board board, Cell start)
    {
        int row = start.getRow();
        int col = start.getCol();

        // Check left up
        int currDestRow = row - 1;
        int currDestCol = col - 1;

        while (currDestRow != -1 && currDestCol != -1) 
        {
            Cell destCell = board.getCell(currDestRow, currDestCol);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;

            currDestRow--;
            currDestCol--;
        }

        // Check right up
        currDestRow = row - 1;
        currDestCol = col + 1;

        while (currDestRow != -1 && currDestCol != Board.COLS) 
        {
            Cell destCell = board.getCell(currDestRow, currDestCol);

            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;

            currDestRow--;
            currDestCol++;
        }

        // Check left down
        currDestRow = row + 1;
        currDestCol = col - 1;

        while (currDestRow != Board.ROWS && currDestCol != -1) 
        {
            Cell destCell = board.getCell(currDestRow, currDestCol);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;

            currDestRow++;
            currDestCol--;
        }

        // Check right down
        currDestRow = row + 1;
        currDestCol = col + 1;

        while (currDestRow != Board.ROWS && currDestCol != Board.COLS)
        {
            Cell destCell = board.getCell(currDestRow, currDestCol);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;

            currDestRow++;
            currDestCol++;
        }

        return false;
    }
}
