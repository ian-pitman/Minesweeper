package minesweeper;

public interface MSModel {
    public void setValueAt(int row, int col, Tile tile);
    public Tile getTileAt(int row, int col);
    public int getNumRows();
    public int getNumCols();
    public void setShown(int row, int col, boolean isShown);
    public void setFlagged(int row, int col, boolean isFlagged);
    public void setMine(int row, int col, boolean hasMine);
    public boolean isShown(int row, int col);
    public boolean isFlagged(int row, int col);
    public boolean hasMine(int row, int col);
    public int countSurroundingMines(int row, int col);
}