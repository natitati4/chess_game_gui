package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.*;

public class Pawn extends Piece
{
    public Pawn(int color)
    {
        super(color);
    }

    @Override
    public boolean validateMove(Board board, Cell start, Cell end) 
    {   
        // Pawn is white
        if (start.getPiece().getColor() == WHITE)
        {            
            return validateWhitePawnMove(board, start, end);
        }
        
        // Pawn is white
        else
        {
            return validateBlackPawnMove(board, start, end);
        }
    }

    private boolean validateWhitePawnMove(Board board, Cell start, Cell end)
    {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Validate that moved 1 or 2 rows and between -1 and 1 cols
        if (!((endRow - startRow == 2 || endRow - startRow == 1) && (startCol - 1 <= endCol && endCol <= startCol + 1)))
            return false;

        // If moved 2
        if (endRow - startRow == 2)
        {
            // Check if col is the same
            if (endCol != startCol || startRow != 1)
                return false;
        }

        // If moved 1
        if (endRow - startRow == 1)
        {
            // If col is the same
            if (endCol == startCol)
            {
                // If theres a piece blocking
                if (board.getCell(endRow, endCol).getPiece() != null)
                    return false;
            }

            // Else if col is to left or right
            else
            {   
                Move lastMove = board.getLastMove();
                boolean isEnPassantable = false;
                
                // Check if en passant
                if (lastMove != null)
                {
                    isEnPassantable = lastMove.getEnd().getPiece() instanceof Pawn && // Check if it's a pawn
                                    lastMove.getStart().getRow() - lastMove.getEnd().getRow() == 2 && // Check last if moved 2 row
                                    lastMove.getEnd().getCol() == lastMove.getStart().getCol() && // Check last if same col
                                    lastMove.getEnd().getRow() == startRow && // Check if same row as last
                                    lastMove.getEnd().getCol() == endCol; // Check if ended on same col as last
                    
                    if (isEnPassantable)
                    {
                        lastMove.setEnPassent(true);
                        board.setLastMove(lastMove);
                        return true;
                    }
                        
                }

                // If no piece (if en passent it would already return true)
                if (board.getCell(endRow, endCol).getPiece() == null)
                    return false;
                
                // If piece of same color
                else if (board.getCell(endRow, endCol).getPiece().getColor() == Piece.WHITE)
                    return false;
            }
        }

        // If all assertions passed, return true
        return true;
    }


    private boolean validateBlackPawnMove(Board board, Cell start, Cell end)
    {
        int startRow = start.getRow();
        int startCol = start.getCol();
        int endRow = end.getRow();
        int endCol = end.getCol();

        // Validate that moved 1 or 2 rows and between -1 and 1 cols
        if (!((startRow - endRow == 2 || startRow - endRow == 1) && (startCol - 1 <= endCol && endCol <= startCol + 1)))
            return false;
        
        // If moved 2
        if (startRow - endRow == 2)
        {
            // Check if col is the same and starting row
            if (endCol != startCol || startRow != Board.ROWS - 2)
                return false;
        }

        // If moved 1
        if (startRow - endRow == 1)
        {
            // If col is the same
            if (endCol == startCol)
            {
                // If theres a piece blocking
                if (board.getCell(endRow, endCol).getPiece() != null)
                    return false;
            }

            // Else if col is to left or right
            else
            {   
                Move lastMove = board.getLastMove();
                boolean isEnPassantable = false;
                
                // Check if en passant
                if (lastMove != null)
                {
                    isEnPassantable = lastMove.getEnd().getPiece() instanceof Pawn && // Check if it's a pawn
                                    lastMove.getEnd().getRow() - lastMove.getStart().getRow() == 2 && // Check last if moved 2 row
                                    lastMove.getEnd().getCol() == lastMove.getStart().getCol() && // Check last if same col
                                    lastMove.getEnd().getRow() == startRow && // Check if same row as last
                                    endCol == lastMove.getEnd().getCol(); // Check if ended on same col as last
                    
                    if (isEnPassantable)
                    {
                        lastMove.setEnPassent(true);
                        board.setLastMove(lastMove);
                        return true;
                    }
                        
                }
                
                // If no piece
                if (board.getCell(endRow, endCol).getPiece() == null)
                    return false;
                
                // If piece of same color
                else if (board.getCell(endRow, endCol).getPiece().getColor() == Piece.BLACK)
                    return false;
            }
        }

        // If all assertions passed, return true
        return true;
    }

    @Override
    public boolean hasMoves(Board board, Cell start) 
    {
        // TODO: add check for an en passant possibility of blocking the check
        int row = start.getRow();
        int col = start.getCol();

        int destRow;

        if (start.getPiece().getColor() == Piece.BLACK)
            destRow = row - 1;
        else
            destRow = row + 1;

        for (int i = -1; i <= 1; i++)
        {
            int currDestCol = col + i;

            // If not in board bounds
            if (!(0 <= currDestCol && currDestCol < Board.COLS))
                continue;
            
            Cell destCell = board.getCell(destRow, currDestCol);
            
            // If move can be made
            if (board.movePiece(start, destCell, false))
                return true;
        }
        
        return false;
    }
}
