package paint;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Inspiration extends Application {

    private Button draw;
    private Button clear;
    private TextField objCount;
    private TextField maxSize;
    ObservableList<String> shapeTypes
            = FXCollections.observableArrayList("Rectangle", "Circle");
    final ComboBox combobox = new ComboBox(shapeTypes);
    private BorderPane bdrPane = new BorderPane();
    private HBox drawingBarPane;
    private int siz = 100;
    private int obj = 100;
    Pane artworkPane = new Pane();

    public Pane AddHBox() {

        drawingBarPane = new HBox();

        Label lbl1 = new Label("Object Count");
        Label lbl2 = new Label("Size");

        objCount = new TextField();
        maxSize = new TextField();
        draw = new Button("Draw");
        clear = new Button("Clear");
        combobox.setValue("Circle");
        drawingBarPane.getChildren().addAll(combobox, lbl1, objCount, lbl2, maxSize, draw, clear);

        /**
         * Draws Shape
         *
         * @param drawShape draws selected shape
         * @param artworkPane allows shape to be drawn
         * @return shapes
         * @see shape
         */

        draw.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                artworkPane.getChildren().clear();
                drawShape();
            }
        });

        /**
         * Clears Screen
         */
        clear.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                artworkPane.getChildren().clear();
            }
        });

        /**
         * Returns number of objects
         *
         * @param obj object selected
         * @param artworkPane allows shape to be drawn
         * @return shapes
         * @see shape
         */

        objCount.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                artworkPane.getChildren().clear();
                obj = Integer.parseInt(objCount.getText());
                drawShape();
            }
        });
        maxSize.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                artworkPane.getChildren().clear();
                siz = Integer.parseInt(maxSize.getText());
                drawShape();
            }
        });
        return drawingBarPane;
    }

    @Override
    public void start(Stage primaryStage) {
        DrawCircle();
        combobox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                artworkPane.getChildren().clear();
                drawShape();
            }
        });

        bdrPane.setBottom(AddHBox());
        bdrPane.setCenter(artworkPane);

        Scene scene = new Scene(bdrPane, 725, 400);
        primaryStage.setTitle("Inspiration");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Returns circle
     *
     * @param obj count number of circles
     * @param artworkPane allows shape to be drawn
     * @return shapes
     * @see shape
     */

    public void DrawCircle() {
        for (int i = 0; i < obj; i++) {
            Circle cir = new Circle(getCenterX(), getCenterY(), getRadius());
            cir.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            artworkPane.getChildren().add(cir);
            cir.setOpacity(0.5);
        }

    }

    /**
     * Returns a shape object that can then be drawn to the screen.
     *
     * @param rect shape object
     * @param circ second shape object
     * @return the shape specified
     * @see shape with random colors and sizes
     */

    public void drawShape() {
        if (combobox.getValue() == "Rectangle") {
            for (int i = 0; i < obj; i++) {

                double height = (int) (Math.random() * siz);
                double width = (int) (Math.random() * siz);
                double xcord = (int) (Math.random() * 500 + 50);
                double ycord = (int) (Math.random() * 180 + 50);
                Rectangle rec = new Rectangle(xcord, ycord, width, height);
                rec.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                rec.setOpacity(0.5);
                artworkPane.getChildren().add(rec);
            }

        }
        if (combobox.getValue() == "Circle") {
            for (int i = 0; i < obj; i++) {
                Circle cir = new Circle(getCenterX(), getCenterY(), getRadius());
                cir.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                cir.setOpacity(0.5);
                artworkPane.getChildren().add(cir);

            }
        }
    }

    public double getRadius() {
        return (int) (Math.random() * siz);
    }

    public double getCenterY() {
        return (int) (Math.random() * 180 + 50);
    }

    public double getCenterX() {
        return (int) (Math.random() * 500 + 50);
    }

    public static void main(String[] args) {
        launch(args);

    }
}