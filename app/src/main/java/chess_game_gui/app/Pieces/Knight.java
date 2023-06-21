package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.Board;
import chess_game_gui.app.MainComponents.Cell;
import chess_game_gui.app.MainComponents.Piece;

public class Knight extends Piece
{
    public Knight(int color)
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

        // Check if moved 2 rows and 1 col or 1 row and 2 cols
        if (!((Math.abs(startRow - endRow) == 2 && Math.abs(startCol - endCol) == 1) ||
                (Math.abs(startRow - endRow) == 1 && Math.abs(startCol - endCol) == 2)))
            return false;

        // End piece is same color
        if (end.getPiece() != null)
        {
            if (start.getPiece().getColor() == end.getPiece().getColor())
                return false;
        }

        return true;
    }

    @Override
    public boolean hasMoves(Board board, Cell start) 
    {
        int row = start.getRow();
        int col = start.getCol();

        // All possible knight positions that can attack the king
        int knightCells[][] = {{row - 1, col - 2}, {row - 1, col + 2},
                                {row + 1, col - 2}, {row + 1, col + 2},
                                {row - 2, col - 1}, {row - 2, col + 1},
                                {row + 2, col - 1}, {row + 2, col + 1}};

        for (int i = 0; i < 8; i++)
        {
            int currDestRow = knightCells[i][0];
            int currDestCol = knightCells[i][1];
            
            // If not in board bounds
            if (!(0 <= currDestRow &&currDestRow < Board.ROWS) || !(0 <= currDestCol && currDestCol < Board.COLS))
                continue;
            
            Cell destCell = board.getCell(currDestRow, currDestCol);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }

        return false;
    }    
}
