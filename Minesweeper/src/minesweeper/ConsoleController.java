package minesweeper;

import java.util.Scanner;

public class ConsoleController {

    public static final String MINESWEEPER_TEXT =
            "        _                                                   \n" +
            "  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __ \n" +
            " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n" +
            "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |   \n" +
            "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|   \n" +
            "                                           |_|";
    MinesweeperModel model;
    
    public ConsoleController(int rows, int cols, int numMines) {
        model = new MinesweeperModel(rows, cols, numMines);
    }
    
    public MinesweeperModel getModel() {
        return model;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("How many rows?: ");
        int numRows = sc.nextInt();
        System.out.print("How many cols?: ");
        int numCols = sc.nextInt();
        System.out.print("How many mines?: ");
        int numMines = sc.nextInt();
        
        ConsoleController ctrl = new ConsoleController(numRows, numCols, numMines); 
        System.out.println(MINESWEEPER_TEXT);
        
        int minesLeft = ctrl.getModel().numMines();
        boolean first = true;
        
        OUTER:
        do {
            if (minesLeft == 0) {
                System.out.println("You won!");
                break;
            }
            ctrl.getModel().printUpperBoard();
            System.out.println();
            ctrl.getModel().printLowerBoard();
            System.out.println("There are " + minesLeft + " mines left.");
            System.out.println("Would you like to reveal or flag a cell?");
            System.out.print("Enter 'r' or 'f' > ");
            String input = sc.next();
            System.out.print("Enter row: ");
            int row = sc.nextInt();
            System.out.print("Enter col: ");
            int col = sc.nextInt();
            if (row < 0 || col < 0 || row >= ctrl.getModel().getNumRows() || col >= ctrl.getModel().getNumCols()) {
                System.out.println("Index out of bounds!");
                continue;
            }
            switch (input) {
                case "f":
                    if (ctrl.getModel().hasMine(row, col) && !ctrl.getModel().isFlagged(row, col)) {
                        minesLeft--;
                    }
                    if (ctrl.getModel().hasMine(row, col) && ctrl.getModel().isFlagged(row, col)) {
                        minesLeft++;
                    }
                    ctrl.getModel().setFlagged(row, col, !ctrl.getModel().isFlagged(row, col));
                    break;
                case "r":
                    if (first) {
                        ctrl.getModel().regen(row, col);
                    }
                    if (ctrl.getModel().hasMine(row, col)) {
                        ctrl.getModel().setShown(row, col, true);
                        System.out.println("Game Over!");
                        break OUTER;
                    }
                    ctrl.getModel().reveal(row, col);
                    break;
                default:
                    System.out.println("Incorrect input!");
                    break;
            }
            first = false;
        } while (ctrl.getModel().numMines() != 0);
    }

}
