package minesweeper;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Name: Ian Pitman
 * Date: 3/20/17
 * Period: 4
 * 
 * Time Spent: 2 hours.
 * 
 * Reflection: This lab is relatively hard but I feel like I'm learning a lot
 * from it. Some features are lacking and I still need to fix how the reveal
 * algorithm works with the GUI. Since I have already spent a lot of time on
 * this, I will resubmit this.
 */

public class Minesweeper extends Application {
    
    private MenuBar menu;
    private Menu file;
    private MenuItem beginner;
    private MenuItem intermediate;
    private MenuItem expert;
    private MenuItem custom;
    private Menu options;
    private MenuItem setMines;
    private MenuItem gridSize;
    private Menu about;
    private Menu howTo;
    private Menu exit;
    
    private BorderPane root;
    private HBox hud;
    private Label time;
    private Label mines;
    
    private GridPane gridContainer;
    private ImageView[][] imageGrid;
    private MinesweeperModel model;
    
    private Image blank = new Image("images\\blank.gif");
    private Image flag = new Image("images\\bomb_flagged.gif");
    private Image bomb_death = new Image("images\\bomb_death.gif");
    private Image[] numbers = loadNumberImages();
    
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        menu = new MenuBar();
        hud = new HBox();
        
        file = new Menu("File");
        options = new Menu("Options");
        about = new Menu("About");
        howTo = new Menu("How To Play");
        exit = new Menu("Exit");
        
        beginner = new MenuItem("Start Beginner Game");
        intermediate = new MenuItem("Start Intermediate Game");
        expert = new MenuItem("Start Expert Game");
        custom = new MenuItem("Start Custom Game");
        
        setMines = new MenuItem("Set Mines");
        gridSize = new MenuItem("Set Grid Size");
        
        file.getItems().addAll(beginner, intermediate, expert, custom);
        options.getItems().addAll(setMines, gridSize);
        menu.getMenus().addAll(file, options, about, howTo, exit);
        
        time = new Label("Time Elapsed: ");
        mines = new Label("Mines Remaining: ");
        hud.setPadding(new Insets(10, 10, 10, 10));
        hud.setSpacing(10);
        hud.setAlignment(Pos.CENTER);
        hud.getChildren().addAll(time, mines);
        
        gridContainer = new GridPane();
        gridContainer.setAlignment(Pos.CENTER);
        model = new MinesweeperModel(8, 8, 10);
        imageGrid = new ImageView[model.getNumRows()][model.getNumCols()];
        
        updateGridContainer();
        addListeners();
        
        root.setTop(menu);
        root.setBottom(hud);
        root.setCenter(gridContainer);
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Minesweeper GUI (Template)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void addListeners() {
        gridContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                int rowAt = (int)(e.getX() - 272) / 32;
                int colAt = (int)(e.getY() - 137) / 32;
                System.out.println("( " + e.getX() + ", " + e.getY() + " )");
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    model.reveal(rowAt, colAt);
                    System.out.println("Left clicked (" + rowAt + ", " + colAt + ")");
                }
                if (e.getButton().equals(MouseButton.SECONDARY)) {
                    model.setFlagged(rowAt, colAt, !model.isFlagged(rowAt, colAt));
                    System.out.println("Right clicked (" + rowAt + ", " + colAt + ")");
                }
                updateGridContainer();
            }
        });
    }
    
    public void updateView() {
        for (int row = 0; row < imageGrid.length; row++) {
            for (int col = 0; col < imageGrid[0].length; col++) {
                Image temp = blank;
                if (model.isFlagged(row, col)) {
                    temp = flag;
                }
                if (model.isShown(row, col)) {
                    temp = numbers[model.getNumber(row, col)];
                    if (model.hasMine(row, col)) {
                        temp = bomb_death;
                    }
                }
                imageGrid[row][col] = new ImageView(temp);
            }
        }
    }
    
    public void updateGridContainer() {
        gridContainer = new GridPane();
        gridContainer.setAlignment(Pos.CENTER);
        
        updateView();
        
        for (int row = 0; row < imageGrid.length; row++) {
            for (int col = 0; col < imageGrid[0].length; col++) {
                gridContainer.add(imageGrid[row][col], row, col);
            }
        }
        
        addListeners();
        
        root.setCenter(gridContainer);
    }
    
    public Image[] loadNumberImages() {
        Image[] result = new Image[9];
        for (int i = 0; i < 9; i++) {
            result[i] = new Image("images\\num_" + i + ".gif");
        }
        return result;
    }
    
    public static void main(String[] args) {
        launch(args);
    } 
    
}
