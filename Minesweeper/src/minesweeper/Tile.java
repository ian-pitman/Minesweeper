package minesweeper;

public class Tile {

    private boolean shown;
    private int number;
    private boolean isFlagged;
    private boolean hasMine;
    
    public Tile(boolean shown, int number, boolean isFlagged, boolean isMine) {
        this.shown = shown;
        this.number = number;
        this.isFlagged = isFlagged;
        this.hasMine = isMine;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }
}
