package minesweeper;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Name: Ian Pitman
 * Date: 3/26/17
 * Period: 4
 * 
 * Time Spent: 12 hrs (TOTAL, including HW #1, 2, and 3) over ~1 week period
 * 
 * Reflection: This lab was a very interactive project. I loved that we were
 * left to our own devices to figure out how to use HTML and CSS to make the
 * help pages and that there were many in class work days given to work on this
 * project. For this lab, I used GitHub to keep track of all my changes. It took
 * me a while to figure out how to use it but I think that I'm going to be using
 * it a lot from now on. I had several challenges that I faced when creating
 * Minesweeper; most were simply control flow errors or stuff that I don't know
 * how to do, like WebView. I solved the former by testing, reviewing, fixing,
 * and repeating that process until everything was fixed. I solved the latter
 * by googling my problems. I usually could find Stack Overflow solutions or
 * online documentation to solve my problems. After completing this lab, I'm
 * very excited to pursue future projects in similar scale to this. I am
 * definitely looking at adding the "optional" features listed on SchoolLoop as
 * well.
 */

public class MinesweeperView extends Application {
    
    private static final int[] BEGINNER_SETTINGS = { 8, 8, 5 };
    
    private MenuBar menu;
    private Menu game;
    private MenuItem beginner;
    private Menu options;
    private MenuItem setMines;
    private Menu help;
    private MenuItem about;
    private MenuItem howTo;
    private MenuItem exit;
    
    private BorderPane root;
    private HBox hud;
    private Label time;
    private Label mines;
    
    private GridPane gridContainer;
    private ImageView[][] imageGrid;
    private MinesweeperModel model;
    
    private boolean first;
    private int flagsRemaining;
    private int minesRemaining;
    private long initialTime;
    private long timeElapsed;
    private Timer timer;
    
    private boolean cheatMode = true;
    
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
        exit = new MenuItem("Exit");
        
        beginner = new MenuItem("Start Beginner Game");
        
        setMines = new MenuItem("Set Mines");
        
        about = new MenuItem("About");
        howTo = new MenuItem("How To Play");
        
        game.getItems().addAll(beginner, exit);
        options.getItems().addAll(setMines);
        help.getItems().addAll(about, howTo);
        menu.getMenus().addAll(game, options, help);
        
        time = new Label("Time Elapsed: 0");
        mines = new Label("Mines Remaining: 10");
        hud.setPadding(new Insets(10, 10, 10, 10));
        hud.setSpacing(10);
        hud.setAlignment(Pos.CENTER);
        hud.getChildren().addAll(time, mines);
        
        gridContainer = new GridPane();
        gridContainer.setAlignment(Pos.CENTER);
        model = new MinesweeperModel(BEGINNER_SETTINGS[0], BEGINNER_SETTINGS[1], BEGINNER_SETTINGS[2]);
        imageGrid = new ImageView[model.getNumRows()][model.getNumCols()];
        
        addListeners();
        updateImageView();
        updateGridContainer();
        
        root.setTop(menu);
        root.setBottom(hud);
        root.setCenter(gridContainer);
        Scene scene = new Scene(root, 300, 400);
        
        first = true;
        flagsRemaining = BEGINNER_SETTINGS[2];
        minesRemaining = BEGINNER_SETTINGS[2];
        initialTime = System.nanoTime();
        
        startTimer();
        
        primaryStage.setTitle("Minesweeper GUI");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/images/icons/logo.png"));
        primaryStage.show();
    }
    
    public void addListeners() {
        gridContainer.setOnMouseClicked(new GridContainerHandler());
        beginner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newGame(BEGINNER_SETTINGS[0], BEGINNER_SETTINGS[1], BEGINNER_SETTINGS[2]);
            }
        });
        
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
        setMines.setOnAction(new SetMinePrompter());
        
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayBrowser("/site/about.html", 250, 150, "About");
            }
        });
        
        howTo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                displayBrowser("/site/howto.html", 800, 600, "How To Play");
            }
        });
        
    }
    
    public void updateImageView() {
        for (int row = 0; row < imageGrid.length; row++) {
            for (int col = 0; col < imageGrid[0].length; col++) {
                Image temp = blank;
                // Cheater mode
                if (cheatMode && model.hasMine(row, col)) {
                    temp = bomb_death;
                }
                
                if (model.isShown(row, col)) {
                    temp = numbers[model.getNumber(row, col)];
                    if (model.hasMine(row, col)) {
                        temp = bomb_death;
                    }
                }
                
                if (model.isFlagged(row, col)) {
                    temp = flag;
                }
                imageGrid[col][row] = new ImageView(temp);
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
            int rowAt = (int)(e.getY() - 41) / 32;
            int colAt = (int)(e.getX() - 22) / 32;
            
            try {
                //System.out.println("( " + e.getX() + ", " + e.getY() + " )");
                if (e.getButton().equals(MouseButton.PRIMARY)) {
                    if (model.isFlagged(rowAt, colAt)) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Message");
                        alert.setHeaderText("This tile is flagged!");
                        alert.setContentText("Click 'OK' to continue");
                        alert.showAndWait();
                    } else {
                        if (first && model.hasMine(rowAt, colAt)) {
                            model.regen(rowAt, colAt);
                            updateImageView();
                            updateGridContainer();
                            first = false;
                            return;
                        }
                        model.reveal(rowAt, colAt);
                        if (model.hasMine(rowAt, colAt)) {
                            gameOver(rowAt, colAt);
                        }
                        //System.out.println("Left clicked (" + rowAt + ", " + colAt + ")");
                    }
                }
                if (e.getButton().equals(MouseButton.SECONDARY)) {
                    if (model.isFlagged(rowAt, colAt)) {
                        flagsRemaining++;
                        model.setFlagged(rowAt, colAt, false);
                        if (model.hasMine(rowAt, colAt)) {
                            minesRemaining++;
                        }
                    } else {
                        if (flagsRemaining > 0) {
                            flagsRemaining--;
                            model.setFlagged(rowAt, colAt, true);
                        } else {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Message");
                            alert.setHeaderText("You're using too many flags!");
                            alert.setContentText("Click 'OK' to continue");
                            alert.showAndWait();
                        }
                        if (model.hasMine(rowAt, colAt)) {
                            minesRemaining--;
                            if (minesRemaining == 0) {
                                mines.setText("Mines Remaining: 0");
                                updateImageView();
                                updateGridContainer();
                                win();
                                newGame(BEGINNER_SETTINGS[0], BEGINNER_SETTINGS[1], BEGINNER_SETTINGS[2]);
                            }
                        }
                    }
                    mines.setText("Mines Remaining: " + flagsRemaining);
                }
                updateImageView();
                updateGridContainer();
                first = false;
            } catch (ArrayIndexOutOfBoundsException err) {
                System.out.println("Click inside of grid.");
            }
            
        }
        
    }
    
    private class SetMinePrompter implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BorderPane bp = new BorderPane();
            
            VBox centerWrapper = new VBox();
            centerWrapper.setSpacing(10);
            centerWrapper.setAlignment(Pos.CENTER);
            TextField minesPrompter = new TextField();
            minesPrompter.setPrefSize(100, 20);
            minesPrompter.setPromptText("Enter number of mines desired.");
            
            centerWrapper.getChildren().addAll(new Label("Number of Mines: "), minesPrompter);
            bp.setCenter(centerWrapper);
            
            HBox bottomWrapper = new HBox();
            bottomWrapper.setSpacing(10);
            bottomWrapper.setAlignment(Pos.CENTER);
            Button button = new Button("OK.");
            
            bottomWrapper.getChildren().addAll(button);
            bp.setBottom(bottomWrapper);
            
            Scene scene = new Scene(bp, 250, 150, Color.web("#666970"));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Set Mines");
            stage.getIcons().add(new Image("/images/icons/logo.png"));
            stage.show();
            
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int numMines = Integer.parseInt(minesPrompter.getText());
                    if (numMines <= 0 || numMines > 64) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Invalid number. Using beginner settings.");
                        alert.setContentText("Click 'OK' to continue.");
                        alert.showAndWait();
                        newGame(8, 8, 5);
                    } else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Message");
                        alert.setHeaderText("New game created with " + numMines + " mines.");
                        alert.setContentText("Click 'OK' to continue.");
                        alert.showAndWait();
                        newGame(8, 8, numMines);
                    }
                    stage.close();
                }
            });
        }
    }
    
    public void newGame(int row, int col, int numMines) {
        model = new MinesweeperModel(row, col, numMines);
        addListeners();
        updateImageView();
        updateGridContainer();
        first = true;
        minesRemaining = numMines;
        flagsRemaining = numMines;
        initialTime = System.nanoTime();
        stopTimer();
        startTimer();
        mines.setText("Mines Remaining: " + flagsRemaining);
    }
    
    public void gameOver(int rowAt, int colAt) {
        stopTimer();
        model.setShown(rowAt, colAt, true);
        cheatMode = true;
        updateImageView();
        updateGridContainer();
        //cheatMode = false;
        gameOverAlert();
        newGame(8, 8, 10);
    }
    
    public void win() {
        stopTimer();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("You won!");
        alert.setHeaderText("You sweeped all the mines!");
        alert.setContentText("Click 'OK' to start new beginner game.");
        alert.showAndWait();
    }
    
    private void gameOverAlert() {
        stopTimer();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setHeaderText("You clicked on a mine.");
        alert.setContentText("Click 'OK' to start new beginner game.");
        alert.showAndWait();
    }
    
    private void displayBrowser(String page, int width, int height, String title) {
        Scene scene = new Scene(new Browser(page),width,height, Color.web("#666970"));
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.getIcons().add(new Image("/images/icons/logo.png"));
        stage.show();
    }
    
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new GameTimeTracker(), 0, 1000);
    }
    
    public void stopTimer() {
        timer.cancel();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * JavaFX Browser class adapted from:
     *   http://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm
     */
    private class Browser extends Region {
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        public Browser(String loc) {
            //apply the styles
            getStyleClass().add("browser");
            // load the web page
            URL url = this.getClass().getResource(loc);
            webEngine.load(url.toString());
            //add the web view to the scene
            getChildren().add(browser);
        }
        
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }

        @Override protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
        }

        @Override protected double computePrefWidth(double height) {
            return 750;
        }

        @Override protected double computePrefHeight(double width) {
            return 500;
        }
    }
    
    private class GameTimeTracker extends TimerTask {

        @Override
        public void run() {
            timeElapsed = System.nanoTime() - initialTime;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    time.setText("Time Elapsed: " + timeElapsed / 1000000000);
                }
            });
        }
        
    }
}