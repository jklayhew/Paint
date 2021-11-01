package paint;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Stack;

/**
 * @author Justin Layhew
 */

public class Paint extends Application {

    public void start(Stage primaryStage) {

        /*------- Menus, MenuItems and MenuBar ------*/
        Menu File = new Menu("File");
        Menu art = new Menu("Art Generator");
        Menu options = new Menu("Options");
        Menu help = new Menu("Help");
        Menu exit = new Menu("Exit");

        /**
         * Creates Menus for menuBar also includes sub menus for opening images
         * and saving
         */
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(File, art, options, help, exit);

        Image openIcon = new Image("https://cdn-icons-png.flaticon.com/512/25/25402.png");
        ImageView openView = new ImageView(openIcon);
        openView.setFitWidth(15);
        openView.setFitHeight(15);
        MenuItem open = new MenuItem("Open");
        open.setGraphic(openView);

        /**
         * Creates graphic for open menuItem
         */
        Image saveIcon = new Image("https://cdn1.iconfinder.com/data/icons/ios-11-glyphs/30/save-512.png");
        ImageView saveView = new ImageView(saveIcon);
        saveView.setFitWidth(15);
        saveView.setFitHeight(15);
        MenuItem save = new MenuItem("Save");
        save.setGraphic(saveView);

        /**
         * Creates graphic for save menuItem
         */
        File.getItems().addAll(open, save);

        MenuItem releaseNotes = new MenuItem("Release Notes");
        options.getItems().add(releaseNotes);

        MenuItem guide = new MenuItem("Guide");
        help.getItems().add(guide);

        MenuItem exi = new MenuItem("Close Program (Shift + X)");
        exit.getItems().add(exi);

        MenuItem artGen = new MenuItem("Generate");
        art.getItems().add(artGen);

        Stack<Shape> undoHistory = new Stack();
        Stack<Shape> redoHistory = new Stack();

        /* ---------- Buttons ---------- */
        ToggleButton drawbtn = new ToggleButton("Draw");
        ToggleButton erasebtn = new ToggleButton("Eraser");
        ToggleButton linebtn = new ToggleButton("Line");
        ToggleButton rectbtn = new ToggleButton("Rectange");
        ToggleButton circlebtn = new ToggleButton("Circle");
        ToggleButton elpslebtn = new ToggleButton("Ellipse");
        ToggleButton textbtn = new ToggleButton("Text");
        ToggleButton colorbtn = new ToggleButton("Pixel Color");
        ToggleButton roundRectbtn = new ToggleButton("Round Rect");

        /**
         * Returns an icon for buttons.
         *
         * @param url an absolute URL giving the base location of the image
         * @param name the location of the image, relative to the url argument
         * @return the image at the specified URL
         * @see Image
         */
        Image circPic = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Circle_-_black_simple.svg/1200px-Circle_-_black_simple.svg.png");
        ImageView circView = new ImageView(circPic);
        circView.setFitHeight(20);
        circView.setFitWidth(20);
        circlebtn.setGraphic(circView);

        Image roundRectPic = new Image("https://img2.greatnotions.com/StockDesign/XLarge/Grand_Slam_Designs/log130.jpg");
        ImageView roundRectView = new ImageView(roundRectPic);
        roundRectView.setFitHeight(20);
        roundRectView.setFitWidth(20);
        roundRectbtn.setGraphic(roundRectView);

        Image rectPic = new Image("https://useruploads.socratic.org/INNT6RHNTaYMwuI1yWA4_rectangle.png");
        ImageView rectView = new ImageView(rectPic);
        rectView.setFitHeight(20);
        rectView.setFitWidth(20);
        rectbtn.setGraphic(rectView);

        Image linePic = new Image("https://w7.pngwing.com/pngs/352/859/png-transparent-white-line-black-m-font-line-white-text-rectangle.png");
        ImageView lineView = new ImageView(linePic);
        lineView.setFitHeight(20);
        lineView.setFitWidth(20);
        linebtn.setGraphic(lineView);

        Image erasePic = new Image("https://www.davison.com/wp/wp-content/uploads/2012/04/eraser.jpg");
        ImageView eraseView = new ImageView(erasePic);
        eraseView.setFitHeight(20);
        eraseView.setFitWidth(20);
        erasebtn.setGraphic(eraseView);

        Image drawPic = new Image("https://i.pinimg.com/originals/6e/35/ef/6e35ef7687065eb1e4c037781f3c4cdc.png");
        ImageView drawView = new ImageView(drawPic);
        drawView.setFitHeight(20);
        drawView.setFitWidth(20);
        drawbtn.setGraphic(drawView);

        Image ovalPic = new Image("https://icons.iconarchive.com/icons/icons8/ios7/512/Editing-Ellipse-icon.png");
        ImageView ovalView = new ImageView(ovalPic);
        ovalView.setFitHeight(20);
        ovalView.setFitWidth(20);
        elpslebtn.setGraphic(ovalView);

        Image dropPic = new Image("https://w7.pngwing.com/pngs/625/752/png-transparent-computer-icons-dropper-angle-people-eye.png");
        ImageView dropView = new ImageView(dropPic);
        dropView.setFitHeight(20);
        dropView.setFitWidth(20);
        colorbtn.setGraphic(dropView);

        Image texPic = new Image("https://pngimg.com/uploads/letter_a/letter_a_PNG6.png");
        ImageView tView = new ImageView(texPic);
        tView.setFitHeight(20);
        tView.setFitWidth(20);
        textbtn.setGraphic(tView);

        /**
         * Creates shape buttons
         */
        ToggleButton[] toolsArr = {drawbtn, erasebtn, linebtn, rectbtn, circlebtn, elpslebtn, textbtn, colorbtn, roundRectbtn};

        ToggleGroup tools = new ToggleGroup();

        for (ToggleButton tool : toolsArr) {
            tool.setMinWidth(90);
            tool.setToggleGroup(tools);
            tool.setCursor(Cursor.HAND);
        }

        ColorPicker cpLine = new ColorPicker(Color.BLACK);
        ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);

        TextArea text = new TextArea();
        text.setPrefRowCount(1);

        Slider slider = new Slider(1, 100, 3);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Label line_color = new Label("Line Color");
        Label fill_color = new Label("Fill Color");
        Label line_width = new Label("3.0");

        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button clear = new Button("Clear");
        Button zoom = new Button("Zoom in");
        Button zoomout = new Button("Zoom out");

        /**
         * Returns an icon for buttons.
         *
         * @param url an absolute URL giving the base location of the image
         * @param name the location of the image, relative to the url argument
         * @return the image at the specified URL
         * @see Image
         */
        Image zoomIcon = new Image("https://www.pngfind.com/pngs/m/52-523304_plus-sign-icon-png-plus-icon-png-transparent.png");
        ImageView zoomIconView = new ImageView(zoomIcon);
        zoomIconView.setFitHeight(15);
        zoomIconView.setFitWidth(15);
        zoom.setGraphic(zoomIconView);

        Image zoomOut = new Image("https://static.thenounproject.com/png/517806-200.png");
        ImageView zoomOutView = new ImageView(zoomOut);
        zoomOutView.setFitHeight(15);
        zoomOutView.setFitWidth(15);
        zoomout.setGraphic(zoomOutView);

        Image undoPic = new Image("https://toppng.com/uploads/preview/arrowicon-undo-icon-11553390553jsqr7xrsj1.png");
        ImageView undoView = new ImageView(undoPic);
        undoView.setFitHeight(20);
        undoView.setFitWidth(20);
        undo.setGraphic(undoView);

        Image redoPic = new Image("https://icons.veryicon.com/png/o/miscellaneous/editor-2/redo-33.png");
        ImageView redoView = new ImageView(redoPic);
        redoView.setFitHeight(20);
        redoView.setFitWidth(20);
        redo.setGraphic(redoView);

        Image clearPic = new Image("https://www.vhv.rs/dpng/d/456-4562054_vector-clean-home-spray-bottle-icon-png-transparent.png");
        ImageView clearView = new ImageView(clearPic);
        clearView.setFitHeight(20);
        clearView.setFitWidth(20);
        clear.setGraphic(clearView);

        /*------- Buttons ------*/
        Line line = new Line();
        Rectangle rect = new Rectangle();
        Circle circ = new Circle();
        Ellipse elps = new Ellipse();
        Rectangle roundRect = new Rectangle();

        Button[] basicArr = {undo, redo, clear, zoom, zoomout};

        for (Button btn : basicArr) {
            btn.setMinWidth(90);
            btn.setCursor(Cursor.HAND);
            btn.setTextFill(Color.BLACK);
        }

        /*------- Side Panel ------*/
        VBox btns = new VBox(10);
        btns.getChildren().addAll(drawbtn, erasebtn, linebtn, rectbtn, roundRectbtn, circlebtn, elpslebtn,
                textbtn, text, line_color, colorbtn, cpLine, fill_color, cpFill, line_width, slider, undo, redo, clear, zoom, zoomout);
        btns.setPadding(new Insets(10));
        btns.setStyle("-fx-background-color: #999");
        btns.setPrefWidth(100);

        /*------- Creating Canvas ------*/
        Canvas canvas = new Canvas(1080, 790);

        GraphicsContext gc;
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(1);

        /**
         * Returns illustration
         *
         * @param buttons draws selected button
         * @param canvas allows shape to be drawn
         * @return action
         * @see shape
         */

        /*------- Actions for Shapes and Tools ------*/
        canvas.setOnMousePressed(e -> {
            if (drawbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.beginPath();
                gc.lineTo(e.getX(), e.getY());
            } else if (erasebtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (linebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                line.setStartX(e.getX());
                line.setStartY(e.getY());
            } else if (rectbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.beginPath();
                rect.setX(e.getX());
                rect.setY(e.getY());
            } else if (circlebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                circ.setCenterX(e.getX());
                circ.setCenterY(e.getY());
            } else if (roundRectbtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                roundRect.setX(e.getX());
                roundRect.setY(e.getY());
            } else if (elpslebtn.isSelected()) {
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                elps.setCenterX(e.getX());
                elps.setCenterY(e.getY());
            } else if (textbtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                gc.setStroke(cpLine.getValue());
                gc.setFill(cpFill.getValue());
                gc.fillText(text.getText(), e.getX(), e.getY());
                gc.strokeText(text.getText(), e.getX(), e.getY());
            } else if (colorbtn.isSelected()) {
                int x = (int) e.getX();
                int y = (int) e.getY();

                /*------- Pixel Color ------*/
                SnapshotParameters params = new SnapshotParameters();
                WritableImage wi = canvas.snapshot(params, null);
                ImageView iv = new ImageView(wi);

                PixelReader pixelReader = iv.getImage().getPixelReader();
                wi.getPixelReader().getArgb(x, y);

                Color color = pixelReader.getColor(x, y);

                String cords = "Point: (" + x + ", " + y + ")";
                System.out.println(cords);
                System.out.print("R = " + color.getRed() * 100);
                System.out.print(" G = " + color.getGreen() * 100);
                System.out.println(" B = " + color.getBlue() * 100);

                double R = color.getRed();
                double G = color.getGreen();
                double B = color.getBlue();

                cpLine.setValue(Color.color(R, G, B));

            }
        });

        canvas.setOnMouseDragged(e -> {
            if (drawbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
            } else if (erasebtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            }
        });

        canvas.setOnMouseReleased(e -> {

            if (drawbtn.isSelected()) {
                gc.lineTo(e.getX(), e.getY());
                gc.stroke();
                gc.closePath();
            } else if (erasebtn.isSelected()) {
                double lineWidth = gc.getLineWidth();
                gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
            } else if (linebtn.isSelected()) {
                line.setEndX(e.getX());
                line.setEndY(e.getY());
                gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());

                undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
            } else if (rectbtn.isSelected()) {
                rect.setWidth(Math.abs((e.getX() - rect.getX())));
                rect.setHeight(Math.abs((e.getY() - rect.getY())));
                if (rect.getX() > e.getX()) {
                    rect.setX(e.getX());
                }
                if (rect.getY() > e.getY()) {
                    rect.setY(e.getY());
                }

                gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                undoHistory.push(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));

            } else if (roundRectbtn.isSelected()) {
                roundRect.setWidth(Math.abs((e.getX() - roundRect.getX())));
                roundRect.setHeight(Math.abs((e.getY() - roundRect.getY())));
                if (roundRect.getX() > e.getX()) {
                    roundRect.setX(e.getX());
                }
                if (roundRect.getY() > e.getY()) {
                    roundRect.setY(e.getY());
                }

                gc.fillRoundRect(roundRect.getX(), roundRect.getY(), roundRect.getWidth(), roundRect.getHeight(), 50, 50);
                gc.strokeRoundRect(roundRect.getX(), roundRect.getY(), roundRect.getWidth(), roundRect.getHeight(), 50, 50);

                undoHistory.push(new Rectangle(roundRect.getX(), roundRect.getY(), roundRect.getWidth(), roundRect.getHeight()));

            } else if (circlebtn.isSelected()) {
                circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

                if (circ.getCenterX() > e.getX()) {
                    circ.setCenterX(e.getX());
                }
                if (circ.getCenterY() > e.getY()) {
                    circ.setCenterY(e.getY());
                }

                gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
                gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

                undoHistory.push(new Circle(circ.getCenterX(), circ.getCenterY(), circ.getRadius()));
            } else if (elpslebtn.isSelected()) {
                elps.setRadiusX(Math.abs(e.getX() - elps.getCenterX()));
                elps.setRadiusY(Math.abs(e.getY() - elps.getCenterY()));

                if (elps.getCenterX() > e.getX()) {
                    elps.setCenterX(e.getX());
                }
                if (elps.getCenterY() > e.getY()) {
                    elps.setCenterY(e.getY());
                }

                gc.strokeOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());
                gc.fillOval(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY());

                undoHistory.push(new Ellipse(elps.getCenterX(), elps.getCenterY(), elps.getRadiusX(), elps.getRadiusY()));
            }
            redoHistory.clear();
            Shape lastUndo = undoHistory.lastElement();
            lastUndo.setFill(gc.getFill());
            lastUndo.setStroke(gc.getStroke());
            lastUndo.setStrokeWidth(gc.getLineWidth());
        });

        /*------- Color Picker ------*/
        cpLine.setOnAction(e -> {
            gc.setStroke(cpLine.getValue());
        });
        cpFill.setOnAction(e -> {
            gc.setFill(cpFill.getValue());
        });

        /*------- Slider ------*/
        slider.valueProperty().addListener(e -> {
            double width = slider.getValue();
            if (textbtn.isSelected()) {
                gc.setLineWidth(1);
                gc.setFont(Font.font(slider.getValue()));
                line_width.setText(String.format("%.1f", width));
                return;
            }
            line_width.setText(String.format("%.1f", width));
            gc.setLineWidth(width);
        });

        /*------- Undo ------*/
        undo.setOnAction(e -> {
            if (!undoHistory.empty()) {
                gc.clearRect(0, 0, 1080, 790);
                Shape removedShape = undoHistory.lastElement();
                if (removedShape.getClass() == Line.class) {
                    Line tempLine = (Line) removedShape;
                    tempLine.setFill(gc.getFill());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));

                } else if (removedShape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) removedShape;
                    tempRect.setFill(gc.getFill());
                    tempRect.setStroke(gc.getStroke());
                    tempRect.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                } else if (removedShape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) removedShape;
                    tempCirc.setStrokeWidth(gc.getLineWidth());
                    tempCirc.setFill(gc.getFill());
                    tempCirc.setStroke(gc.getStroke());
                    redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                } else if (removedShape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) removedShape;
                    tempElps.setFill(gc.getFill());
                    tempElps.setStroke(gc.getStroke());
                    tempElps.setStrokeWidth(gc.getLineWidth());
                    redoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastRedo = redoHistory.lastElement();
                lastRedo.setFill(removedShape.getFill());
                lastRedo.setStroke(removedShape.getStroke());
                lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                undoHistory.pop();

                for (int i = 0; i < undoHistory.size(); i++) {
                    Shape shape = undoHistory.elementAt(i);
                    if (shape.getClass() == Line.class) {
                        Line temp = (Line) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                    } else if (shape.getClass() == Rectangle.class) {
                        Rectangle temp = (Rectangle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                        gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                    } else if (shape.getClass() == Circle.class) {
                        Circle temp = (Circle) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                    } else if (shape.getClass() == Ellipse.class) {
                        Ellipse temp = (Ellipse) shape;
                        gc.setLineWidth(temp.getStrokeWidth());
                        gc.setStroke(temp.getStroke());
                        gc.setFill(temp.getFill());
                        gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                        gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                    }
                }
            } else {
                System.out.println("there is no action to undo");
            }
        });

        /*------- Redo ------*/
        redo.setOnAction(e -> {
            if (!redoHistory.empty()) {
                Shape shape = redoHistory.lastElement();
                gc.setLineWidth(shape.getStrokeWidth());
                gc.setStroke(shape.getStroke());
                gc.setFill(shape.getFill());

                redoHistory.pop();
                if (shape.getClass() == Line.class) {
                    Line tempLine = (Line) shape;
                    gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                    undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                } else if (shape.getClass() == Rectangle.class) {
                    Rectangle tempRect = (Rectangle) shape;
                    gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                    gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

                    undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                } else if (shape.getClass() == Circle.class) {
                    Circle tempCirc = (Circle) shape;
                    gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
                    gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());

                    undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
                } else if (shape.getClass() == Ellipse.class) {
                    Ellipse tempElps = (Ellipse) shape;
                    gc.fillOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());
                    gc.strokeOval(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY());

                    undoHistory.push(new Ellipse(tempElps.getCenterX(), tempElps.getCenterY(), tempElps.getRadiusX(), tempElps.getRadiusY()));
                }
                Shape lastUndo = undoHistory.lastElement();
                lastUndo.setFill(gc.getFill());
                lastUndo.setStroke(gc.getStroke());
                lastUndo.setStrokeWidth(gc.getLineWidth());
            } else {
                System.out.println("there is no action to redo");
            }
        });

        /*------- Clear Screen  ------*/
        clear.setOnAction(e -> {
            gc.clearRect(0, 0, 1080, 790);
        });

        /*------- Open ------*/
        open.setOnAction((e) -> {
            FileChooser openFile = new FileChooser();
            FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("All Images", "*.*");
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            openFile.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterALL);

            openFile.setTitle("Open File");

            File file = openFile.showOpenDialog(primaryStage);
            if (file != null) {
                try {
                    InputStream io = new FileInputStream(file);
                    Image img = new Image(io);

                    gc.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());

                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });


        /*------- Release Notes ------*/
        releaseNotes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Label secondLabel = new Label("New Features:\n"
                        + "Version 1 updates: \n"
                        + "Added menu bar with the options, file, release notes and exit\n"
                        + "Added functions to each button for the menu bar categories\n"
                        + "Added back button to patch notes\n"
                        + "\n"
                        + "Version 2 updates:\n"
                        + "Added paint application that allows user to draw\n"
                        + "Added width adjustmetn for drawing application\n"
                        + "Added ability to insert image into canvas\n"
                        + "Added scroll bars\n"
                        + "Added save options for viewing images and canvas drawing\n"
                        + "\n"
                        + "Version 3 updates:\n"
                        + "Added more funtionality to paint ie. Shapes and lines\n"
                        + "Added keyboard shortcut to save canvas\n"
                        + "Added tabs\n"
                        + "\n"
                        + "Links:\n"
                        + "Git hub: https://github.com/jklayhew/Pain.t");

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 350, 400);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Release Notes");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                secondScene.getWindow().centerOnScreen();
                newWindow.show();
            }
        });

        /*------- Help Action ------*/
        help.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Label secondLabel = new Label("Q: How do I open an image?\n"
                        + "A: File -> Open -> Select image\n"
                        + "\n"
                        + "Q: How do I save?\n"
                        + "A: File -> Save or Shift + S\n"
                        + "\n"
                        + "Q: How can I make the brush bigger?\n"
                        + "A: Slide the slider on the tool bar to size\n"
                        + "\n"
                        + "Shortcuts:\n"
                        + "Ctrl + S = Save\n"
                        + "Shift + X = Close App\n"
                        + "Shift + C = Clear Screen\n"
                        + "\n"
                        + "Q: How do I use Pixel Color?"
                        + "\n"
                        + "A: Select Pixel Color, then click a spot on the image"
                        + "\n"
                        + "and pixel color will provide the color.");

                StackPane secondaryLayout = new StackPane();
                secondaryLayout.getChildren().add(secondLabel);

                Scene secondScene = new Scene(secondaryLayout, 280, 330);

                // New window (Stage)
                Stage newWindow = new Stage();
                newWindow.setTitle("Release Notes");
                newWindow.setScene(secondScene);

                // Set position of second window, related to primary window.
                newWindow.setX(primaryStage.getX() + 200);
                newWindow.setY(primaryStage.getY() + 100);

                secondScene.getWindow().centerOnScreen();
                newWindow.show();
            }
        });

        /*------- Save ------*/
        save.setOnAction((e) -> {
            FileChooser savefile = new FileChooser();
            FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("All Images", "*.*");
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            savefile.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterALL);

            savefile.setTitle("Save File");

            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    WritableImage writableImage = new WritableImage(1080, 790);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    System.out.println("Error!");
                }
            }
        });

        /*------- Exit ------*/
        exi.setOnAction(e -> Platform.exit());

        /* ----------Scroll Bar---------- */
        ScrollPane scroll = new ScrollPane();
        scroll.setPrefSize(1080, 790);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //Setting content to the scroll pane
        scroll.setContent(canvas);

        /* ----------- Smart Save ----------- */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                // consume event
                event.consume();
                // show close dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Close Confirmation");
                alert.setHeaderText("Do you want to quit without saving?");
                alert.initOwner(primaryStage);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                }
            }
        });

        /* ----------STAGE & SCENE---------- */
        BorderPane pane = new BorderPane();
        pane.setLeft(btns);
        pane.setCenter(canvas);
        pane.setCenter(scroll);
        pane.setTop(menuBar);

        /**
         * Returns an Image object that can then be drawn to the screen.
         *
         * @param url an absolute URL giving the base location of the image
         * @param name the location of the image, relative to the url argument
         * @return the image at the specified URL
         * @see Image
         */

        Image starter_image = new Image("https://cdn02.plentymarkets.com/2brofzsczyt8/item/images/154540/full/Fototapete-Tapete-Grafik---Papier-Vlies-Des-1-1545.jpg");

        ImageView myImage = new ImageView();
        myImage.setImage(starter_image);

        RotateTransition rotate = new RotateTransition();
        rotate.setNode(myImage);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.play();

        Scene scene = new Scene(pane, 1200, 800);
        primaryStage.setTitle("Justin's Paint Version 3.0");
        primaryStage.setScene(scene);
        primaryStage.show();

        /* -----------Keyboard Shortcut for Save and Exit and clear screen----------- */
        KeyCombination combo = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        KeyCombination x = new KeyCodeCombination(KeyCode.X, KeyCodeCombination.SHIFT_DOWN);
        KeyCombination c = new KeyCodeCombination(KeyCode.C, KeyCodeCombination.SHIFT_DOWN);

        scene.setOnKeyPressed(event -> {

            if (combo.match(event)) {

                FileChooser savefile = new FileChooser();
                FileChooser.ExtensionFilter extFilterALL = new FileChooser.ExtensionFilter("All Images", "*.*");
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                savefile.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterALL);

                savefile.setTitle("Save File");

                File file = savefile.showSaveDialog(primaryStage);
                if (file != null) {
                    try {
                        WritableImage writableImage = new WritableImage(1080, 790);
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        System.out.println("Error!");
                    }
                }
            } else if (x.match(event)) {
                primaryStage.close();
            } else if (c.match(event)) {
                gc.clearRect(0, 0, 1080, 790);
            }
        });

        /* ----------- Zoom in and out ----------- */
        zoom.setOnAction((e) -> {
            Scale contentScale = new Scale(1, 1, 1);

            contentScale.setX(contentScale.getX() + contentScale.getX() * .1);
            contentScale.setY(contentScale.getY() + contentScale.getY() * .1);
            contentScale.setZ(contentScale.getZ() + contentScale.getZ() * .1);

            canvas.getTransforms().add(contentScale);
        });

        zoomout.setOnAction((e) -> {
            Scale contentScale = new Scale(1, 1, 1);

            contentScale.setX(contentScale.getX() + contentScale.getX() * -.1);
            contentScale.setY(contentScale.getY() + contentScale.getY() * -.1);
            contentScale.setZ(contentScale.getZ() + contentScale.getZ() * -.1);

            canvas.getTransforms().add(contentScale);
        });

        art.setOnAction((e) -> {

            Inspiration draw = new Inspiration();
            draw.start(primaryStage);

        });
    }

    public static void main(String[] args) {

        launch(args);

    }
}