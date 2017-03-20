package minesweeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
        
        root.setTop(menu);
        root.setBottom(hud);
        Scene scene = new Scene(root, 800, 600);
        
        primaryStage.setTitle("Minesweeper GUI (Template)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
