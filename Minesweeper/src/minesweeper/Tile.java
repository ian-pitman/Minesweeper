package minesweeper;

public class Tile {

    private boolean shown;
    private int number;
    private boolean isFlagged;
    private boolean hasMine;
    
    public Tile(boolean shown, int number, boolean isFlagged, boolean hasMine) {
        this.shown = shown;
        this.number = number;
        this.isFlagged = isFlagged;
        this.hasMine = hasMine;
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
    
    public String booleanCode(boolean foo) {
        return foo ? "t" : "f";
    }
    
    @Override
    public String toString() {
        return booleanCode(this.isShown()) + " " + 
                this.getNumber() + " " + 
                booleanCode(this.isFlagged()) + " " + 
                booleanCode(this.hasMine());
    }
}
