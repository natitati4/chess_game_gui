package chess_game_gui.app.Pieces;

import chess_game_gui.app.MainComponents.Board;
import chess_game_gui.app.MainComponents.Cell;
import chess_game_gui.app.MainComponents.Piece;

public class King extends Piece
{
    private static int bottomColor; // Used to determine which side to check for the castle

    private boolean hasMoved = false;
    private boolean hasJustShortCasteled = false;
    private boolean hasJustLongCasteled = false;

    public King(int color)
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

        int kingColor = start.getPiece().getColor();

        // - Short castle
        if (startCol - endCol == 2 * bottomColor)
        {
            // If king moved, can't castle
            if (hasMoved)
                return false;
            
            // Can't castle if there's a piece in the way
            if (board.getCell(startRow, startCol - bottomColor).getPiece() != null ||
                end.getPiece() != null)
                return false;

            // Can't castle if in check, passing through or landing on attacked cell
            if (board.isCellAttackedByColor(start, kingColor ^ Piece.BLACK) ||
                board.isCellAttackedByColor(board.getCell(startRow, startCol - bottomColor), kingColor ^ Piece.BLACK) ||
                board.isCellAttackedByColor(end, kingColor ^ Piece.BLACK))
                return false;
            
            // Check if theres a rook in place
            Piece castlingRook = board.getCell(startRow, startCol - 3 * bottomColor).getPiece();
            if (!(castlingRook instanceof Rook))
                return false;
            
            // Check if the rook is of correct color
            if (castlingRook.getColor() != kingColor)
                return false;
            
            // Check if rook has moved
            if (((Rook)(castlingRook)).hasMoved())
                return false;
            
            hasJustShortCasteled = true;
            return true;
        }

        // - Long castle
        if (endCol - startCol == 2 * bottomColor)
        {
            // If king moved, can't castle
            if (hasMoved)
                return false;
            
            // Can't castle if there's a piece in the way
            if (board.getCell(startRow, startCol + bottomColor).getPiece() != null ||
                end.getPiece() != null)
                return false;

            // Can't castle if in check, passing through or landing on attacked cell
            if (board.isCellAttackedByColor(start, kingColor ^ Piece.BLACK) ||
                board.isCellAttackedByColor(board.getCell(startRow, startCol + bottomColor), kingColor ^ Piece.BLACK) ||
                board.isCellAttackedByColor(end, kingColor ^ Piece.BLACK))
                return false;
                
            // Check if theres a rook in place
            Piece castlingRook = board.getCell(startRow, startCol + 4 * bottomColor).getPiece();
            if (!(castlingRook instanceof Rook))
                return false;
            
            // Check if the rook is of correct color
            if (castlingRook.getColor() != kingColor)
                return false;
            
            // Check if rook has moved
            if (((Rook)(castlingRook)).hasMoved())
                return false;

            hasJustLongCasteled = true;
            return true;
        }
        
        // Check regular king move
        if (!((Math.abs(startRow - endRow) <= 1) && Math.abs(startCol - endCol) <= 1))
            return false;
            
        // End piece is same color
        if (end.getPiece() != null)
        {
            if (start.getPiece().getColor() == end.getPiece().getColor())
                return false;
        }
        
        if (!this.hasMoved)
            this.hasMoved = true;

        return true;
    }    

    @Override
    public boolean hasMoves(Board board, Cell start) 
    {
        int row = start.getRow();
        int col = start.getCol();

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                int currDestRow = row + i;
                int currDestCol = col + j;
                
                // If self square or out of board bounds
                if ((i == 0 && j == 0) || !(0 <= currDestRow && currDestRow < Board.ROWS) || !(0 <= currDestCol && currDestCol < Board.COLS))
                    continue;

                Cell destCell = board.getCell(currDestRow, currDestCol);
                
                // If move can be made
                if (board.movePiece(start, destCell, false))
                    return true;
            }
        }

        return false;
    }

    /**
     * @return boolean return the hasMoved
     */
    public boolean hasMoved() {
        return hasMoved;
    }

    /**
     * @param hasMoved the hasMoved to set
     */
    public void setMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * @return boolean return the hasJustShortCasteled
     */
    public boolean hasJustShortCasteled() {
        return hasJustShortCasteled;
    }

    /**
     * @param hasJustShortCasteled the hasJustShortCasteled to set
     */
    public void setHasJustShortCasteled(boolean hasJustShortCasteled) {
        this.hasJustShortCasteled = hasJustShortCasteled;
    }

    /**
     * @return boolean return the hasJustLongCasteled
     */
    public boolean hasJustLongCasteled() {
        return hasJustLongCasteled;
    }

    /**
     * @param hasJustLongCasteled the hasJustLongCasteled to set
     */
    public void setHasJustLongCasteled(boolean hasJustLongCasteled) {
        this.hasJustLongCasteled = hasJustLongCasteled;
    }

    public static int getColorOnBottom()
    {
        return bottomColor;
    }

    public static void setColorOnBottom(int color)
    {
        bottomColor = color;
    }
}
