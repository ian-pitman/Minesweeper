package minesweeper;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

public class MinesweeperView extends Application {
    
    private MenuBar menu;
    private Menu game;
    private MenuItem beginner;
    private MenuItem intermediate;
    private MenuItem expert;
    private MenuItem custom;
    private Menu options;
    private MenuItem setMines;
    private MenuItem gridSize;
    private Menu help;
    private MenuItem about;
    private MenuItem howTo;
    private Menu exit;
    
    private BorderPane root;
    private HBox hud;
    private Label time;
    private Label mines;
    
    private GridPane gridContainer;
    private ImageView[][] imageGrid;
    private MinesweeperModel model;
    
    private final Image blank = new Image("images\\blank.gif");
    private final Image flag = new Image("images\\bomb_flagged.gif");
    private final Image bomb_death = new Image("images\\bomb_death.gif");
    private final Image[] numbers = loadNumberImages();
    
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        menu = new MenuBar();
        hud = new HBox();
        
        game = new Menu("Game");
        options = new Menu("Options");
        help = new Menu("Help");
        exit = new Menu("Exit");
        
        beginner = new MenuItem("Start Beginner Game");
        intermediate = new MenuItem("Start Intermediate Game");
        expert = new MenuItem("Start Expert Game");
        custom = new MenuItem("Start Custom Game");
        
        setMines = new MenuItem("Set Mines");
        gridSize = new MenuItem("Set Grid Size");
        
        about = new MenuItem("About");
        howTo = new MenuItem("How To Play");
        
        game.getItems().addAll(beginner, intermediate, expert, custom);
        options.getItems().addAll(setMines, gridSize);
        help.getItems().addAll(about, howTo);
        menu.getMenus().addAll(game, options, help, exit);
        
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
        
        addListeners();
        updateImageView();
        updateGridContainer();
        
        model.setShown(5, 5, true);
        
        root.setTop(menu);
        root.setBottom(hud);
        root.setCenter(gridContainer);
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Minesweeper GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void addListeners() {
        gridContainer.setOnMouseClicked(new GridContainerHandler());
    }
    
    public void updateImageView() {
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
        if (gridContainer.getChildren().size() > 0) {
            gridContainer.getChildren().remove(0, gridContainer.getChildren().size());
        }
        for (int row = 0; row < imageGrid.length; row++) {
            for (int col = 0; col < imageGrid[0].length; col++) {
                gridContainer.add(imageGrid[row][col], row, col);
            }
        }
    }
    
    public Image[] loadNumberImages() {
        Image[] result = new Image[9];
        for (int i = 0; i < 9; i++) {
            result[i] = new Image("images\\num_" + i + ".gif");
        }
        return result;
    }
    
    private class GridContainerHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent e) {
            int rowAt = (int)(e.getX() - 272) / 32;
            int colAt = (int)(e.getY() - 137) / 32;

            System.out.println("( " + e.getX() + ", " + e.getY() + " )");
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                model.reveal(rowAt, colAt);
                if (model.hasMine(rowAt, colAt)) {
                    System.out.println("Game Over.");
                    model.setShown(rowAt, colAt, true);
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Game Over!");
                    alert.setHeaderText("You clicked on a mine.");
                    alert.setContentText("Click 'OK' to continue.");
                    alert.showAndWait();
                    model = new MinesweeperModel(8, 8, 10);
                    addListeners();
                    updateImageView();
                    updateGridContainer();
                }
                System.out.println("Left clicked (" + rowAt + ", " + colAt + ")");
            }
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                model.setFlagged(rowAt, colAt, !model.isFlagged(rowAt, colAt));
                System.out.println("Right clicked (" + rowAt + ", " + colAt + ")");
            }
            updateImageView();
            updateGridContainer();
        }
        
    }
    
    
    public static void main(String[] args) {
        launch(args);
    } 
    
}
