package minesweeper;

import java.util.Random;
import java.util.StringJoiner;

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
        for (int row = 0; row < this.getNumRows(); row++) {
            for (int col = 0; col < this.getNumCols(); col++) {
                grid[row][col].setNumber(countSurroundingMines(row, col));
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
        int result = 0;
        for (int r = ((row > 0) ? row - 1 : row); r <= ((row < this.getNumRows() - 1) ? row + 1 : row); r++) {
            for (int c = ((col > 0) ? col - 1 : col); c <= ((col < this.getNumCols() - 1) ? col + 1 : col); c++) {
                if (r != row || c != col) {
                    if (grid[r][c].hasMine()) {
                        result++;
                    }
                }
            }
        }
        return result;
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
    
    @Override
    public void reveal(int row, int col) {
        if (row >= 0 && col >= 0 && row < this.getNumRows() && col < this.getNumCols()) {
            if (isShown(row, col)) {

            } else if (this.getNumber(row, col) == 0) {
                setShown(row, col, true);
            } else {
                reveal(row, col - 1);
                reveal(row + 1, col);
                reveal(row, col + 1);
                reveal(row - 1, col);
            }
        }
    }
    
    public void regen(int row, int col) {
        Random r = new Random();
        int a = r.nextInt(this.getNumRows());
        int b = r.nextInt(this.getNumCols());
        
        while (hasMine(row, col)) {
            setMine(row, col, false);
            if (!hasMine(a, b)) {
                setMine(a, b, true);
            } else {
                a = r.nextInt(this.getNumRows());
                b = r.nextInt(this.getNumCols());
            }
        }
        
    }
    
    public void printUpperBoard() {
        System.out.print("    ");
        for (int col = 0; col < this.getNumRows(); col++) {
            System.out.print(col + "   ");
        }
        System.out.println();
        String split;
        StringJoiner splitJoiner = new StringJoiner("+", "|", "|");
        for (int col = 0; col < this.getNumCols(); col++) {
            splitJoiner.add(String.format("%3s", "").replace(" ", "-"));
        }
        split = "  " + splitJoiner.toString();
        for (int row = 0; row < this.getNumRows(); row++) {
            StringJoiner sj = new StringJoiner(" | ", "| ", " |");
            for (int col = 0; col < this.getNumCols(); col++) {
                if (grid[row][col].isShown()) {
                    if (grid[row][col].isFlagged()) {
                        sj.add(String.format("%1s", "!"));
                    } else if (grid[row][col].hasMine()) {
                        sj.add(String.format("%1s", "*"));
                    } else if (grid[row][col].getNumber() > 0) {
                        sj.add(String.format("%01d", grid[row][col].getNumber()));
                    } else {
                        sj.add(String.format("%1s", " "));
                    }
                } else {
                    if (grid[row][col].isFlagged()) {
                        sj.add(String.format("%1s", "!"));
                    } else {
                        sj.add(String.format("%1s", "X"));
                    }
                }
            }
            System.out.println(split);
            System.out.println(row + " " + sj.toString());
        }
        System.out.println(split);
    }
    
    public void printLowerBoard() {
        System.out.print("    ");
        for (int col = 0; col < this.getNumRows(); col++) {
            System.out.print(col + "   ");
        }
        System.out.println();
        String split;
        StringJoiner splitJoiner = new StringJoiner("+", "|", "|");
        for (int col = 0; col < this.getNumCols(); col++) {
            splitJoiner.add(String.format("%3s", "").replace(" ", "-"));
        }
        split = "  " + splitJoiner.toString();
        for (int row = 0; row < this.getNumRows(); row++) {
            StringJoiner sj = new StringJoiner(" | ", "| ", " |");
            for (int col = 0; col < this.getNumCols(); col++) {
                if (grid[row][col].isFlagged()) {
                    sj.add(String.format("%1s", "!"));
                } else if (grid[row][col].hasMine()) {
                    sj.add(String.format("%1s", "*"));
                } else if (grid[row][col].getNumber() > 0) {
                    sj.add(String.format("%01d", grid[row][col].getNumber()));
                } else {
                    sj.add(String.format("%1s", " "));
                }
            }
            System.out.println(split);
            System.out.println(row + " " + sj.toString());
        }
        System.out.println(split);
    }

    @Override
    public int numMines() {
        int result = 0;
        for (int r = 0; r < this.getNumRows(); r++) {
            for (int c = 0; c < this.getNumCols(); c++) {
                if (grid[r][c].hasMine()) {
                    result++;
                }
            }
        }
        return result;
    }
    
}