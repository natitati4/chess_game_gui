package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.Board;
import chess_game_gui.app.MainComponents.Cell;
import chess_game_gui.app.MainComponents.Piece;

public class Queen extends Piece
{
    public Queen(int color)
    {
        super(color);
    }

    @Override
    public boolean validateMove(Board board, Cell start, Cell end) 
    {
        Rook rook = new Rook(start.getPiece().getColor());
        Bishop bishop = new Bishop(start.getPiece().getColor());

        // Check if the move is valid for either Rook or Bishop
        return rook.validateMove(board, start, end) || bishop.validateMove(board, start, end);
    }

    @Override
    public boolean hasMoves(Board board, Cell start) 
    {
        Piece queen = start.getPiece();

        // Check rook moves for the queen
        Rook rook = new Rook(start.getPiece().getColor());
        start.setPiece(rook);
        
        // If has any rook moves
        if (rook.hasMoves(board, start))
        {
            // Restore queen and return true as moves are available
            start.setPiece(queen);
            return true;
        }

        // Check bishop moves for the queen
        Bishop bishop = new Bishop(start.getPiece().getColor());
        start.setPiece(bishop);

        // If has any rook moves
        if (bishop.hasMoves(board, start))
        {
            // Restore queen and return true as moves are available
            start.setPiece(queen);
            return true;
        }

        start.setPiece(queen); // Restore queen
        return false;
    }    
}
