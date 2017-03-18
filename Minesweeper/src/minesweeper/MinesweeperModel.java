package minesweeper;

import java.util.Random;

public class MinesweeperModel implements MSModel {

    private Tile[][] grid;
    
    public MinesweeperModel(int rows, int cols, int numMines) {
        grid = new Tile[rows][cols];
        generateGrid(numMines);
    }
    
    private void generateGrid(int numMines) {
        for (int row = 0; row < this.getNumRows(); row++) {
            for (int col = 0; col < this.getNumCols(); col++) {
                grid[row][col] = new Tile(false, 0, false, false);
            }
        }
        for (int i = 0; i < numMines; i++) {
            Random r = new Random();
            int a = r.nextInt(this.getNumRows());
            int b = r.nextInt(this.getNumCols());
            
            while (grid[a][b].hasMine() == true) {
                a = r.nextInt(this.getNumRows());
                b = r.nextInt(this.getNumCols());
            }
            
            grid[a][b] = new Tile(false, 0, false, true);
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
        Tile old = grid[row][col];
        grid[row][col] = new Tile(isShown, old.getNumber(), old.isFlagged(), old.hasMine());
    }
    
    @Override
    public void setNumber(int row, int col, int newNumber) {
        Tile old = grid[row][col];
        grid[row][col] = new Tile(old.isShown(), newNumber, old.isFlagged(), old.hasMine());
    }

    @Override
    public void setFlagged(int row, int col, boolean isFlagged) {
        Tile old = grid[row][col];
        grid[row][col] = new Tile(old.isShown(), old.getNumber(), isFlagged, old.hasMine());
    }

    @Override
    public void setMine(int row, int col, boolean hasMine) {
        Tile old = grid[row][col];
        grid[row][col] = new Tile(old.isShown(), old.getNumber(), old.isFlagged(), hasMine);
    }

    @Override
    public boolean isShown(int row, int col) {
        return grid[row][col].isShown();
    }
    
    @Override
    public int getNumber(int row, int col) {
        return grid[row][col].getNumber();
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
    
    @Override
    public String toString() {
        String output = "";
        for (int r = 0; r < this.getNumRows(); r++) {
            for (int c = 0; c < this.getNumCols(); c++) {
                output += "Tile (" + r + ", " + c + "): " + grid[r][c].toString();
            }
            output += "\n";
        }
        return output;
    }
    
}