package chess_game_gui.app.MainComponents;

public class Move
{
    private Cell start;
    private Cell end;
    private boolean castle;
    private boolean enPassent;

    public Move()
    {
    }

    public Move(Cell start, Cell end)
    {
        this.start = start;
        this.end = end;
    }

    /**
     * @return Cell return the start
     */
    public Cell getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Cell start) {
        this.start = start;
    }

    /**
     * @return Cell return the end
     */
    public Cell getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Cell end) {
        this.end = end;
    }

    /**
     * @return boolean return the castle
     */
    public boolean isCastle() {
        return castle;
    }

    /**
     * @param castle the castle to set
     */
    public void setCastle(boolean castle) {
        this.castle = castle;
    }


    /**
     * @return boolean return the enPassent
     */
    public boolean isEnPassent() {
        return enPassent;
    }

    /**
     * @param enPassent the enPassent to set
     */
    public void setEnPassent(boolean enPassent) {
        this.enPassent = enPassent;
    }

}