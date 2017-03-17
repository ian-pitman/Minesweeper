package minesweeper;

public class MinesweeperModel implements MSModel {

    private Tile[][] grid;
    
    public MinesweeperModel(int rows, int cols, int numMines) {
        grid = new Tile[rows][cols];
        generateGrid(numMines);
    }
    
    private void generateGrid(int num) {
        for (int i = 0; i < num; i++) {
            
        }
        for (int row = 0; row < this.getNumRows(); row++) {
            for (int col = 0; col < this.getNumCols(); col++ ) {
                
            }
        }
    }

    @Override
    public void setValueAt(int row, int col, Tile tile) {
        grid[row][col] = tile;
    }

    @Override
    public Tile getTileAt(int row, int col) {
        return grid[row][col];
    }

    @Override
    public int getNumRows() {
        return grid.length;
    }

    @Override
    public int getNumCols() {
        return grid[0].length;
    }

    @Override
    public void setShown(int row, int col, boolean isShown) {
        
    }

    @Override
    public void setFlagged(int row, int col, boolean isFlagged) {
        
    }

    @Override
    public void setMine(int row, int col, boolean hasMine) {
        
    }

    @Override
    public boolean isShown(int row, int col) {
        return grid[row][col].isShown();
    }

    @Override
    public boolean isFlagged(int row, int col) {
        return grid[row][col].isFlagged();
    }

    @Override
    public boolean hasMine(int row, int col) {
        return grid[row][col].hasMine();
    }

    @Override
    public int countSurroundingMines(int row, int col) {
        return 0;
    }
    
}