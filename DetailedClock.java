
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.text.Text;

public class DetailedClock extends Application {

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Create a clock and a label
        ClockPane clock = new ClockPane();
        String timeString = clock.getHour() + ":" + clock.getMinute()
                + ":" + clock.getSecond();
        Label lblCurrentTime = new Label(timeString);

        // Place clock and label in border pane
        BorderPane pane = new BorderPane();
        pane.setCenter(clock);
        pane.setBottom(lblCurrentTime);
        BorderPane.setAlignment(lblCurrentTime, Pos.TOP_CENTER);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 250, 250);
        primaryStage.setTitle("DisplayClock"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class ClockPane extends Pane {

    private int hour;
    private int minute;
    private int second;

    // Clock pane's width and height
    private double w = 250, h = 250;

    /**
     * Construct a default clock with the current time
     */
    public ClockPane() {
        setCurrentTime();
    }

    /**
     * Construct a clock with specified hour, minute, and second
     */
    public ClockPane(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        paintClock();
    }

    /**
     * Return hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Set a new hour
     */
    public void setHour(int hour) {
        this.hour = hour;
        paintClock();
    }

    /**
     * Return minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Set a new minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
        paintClock();
    }

    /**
     * Return second
     */
    public int getSecond() {
        return second;
    }

    /**
     * Set a new second
     */
    public void setSecond(int second) {
        this.second = second;
        paintClock();
    }

    /**
     * Return clock pane's width
     */
    public double getW() {
        return w;
    }

    /**
     * Set clock pane's width
     */
    public void setW(double w) {
        this.w = w;
        paintClock();
    }

    /**
     * Return clock pane's height
     */
    public double getH() {
        return h;
    }

    /**
     * Set clock pane's height
     */
    public void setH(double h) {
        this.h = h;
        paintClock();
    }

    /* Set the current time for the clock */
    public void setCurrentTime() {
        // Construct a calendar for the current date and time
        Calendar calendar = new GregorianCalendar();

        // Set current hour, minute and second
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);

        paintClock(); // Repaint the clock
    }

    /**
     * Paint the clock
     */
    protected void paintClock() {
        // Initialize clock parameters
        double clockRadius = Math.min(w, h) * 0.8 * 0.5;
        double centerX = w / 2;
        double centerY = h / 2;

        Circle circle = new Circle(centerX, centerY, clockRadius);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);

        // Calculate the angle between each hour mark
        double angleStep = 360.0 / 12.0;

        // Start angle for positioning the numbers
        double startAngle = 90; // Start at the top (12 o'clock)
        double myradius = clockRadius - 15;
        for (int hour = 12; hour >= 1; hour--) {
            // Calculate the angle in radians
            double radianAngle = Math.toRadians(startAngle);

            // Calculate (x, y) coordinates for each hour label
            double x = centerX + myradius * Math.cos(radianAngle);
            double y = centerY - myradius * Math.sin(radianAngle);

            // Create a Text label and set its position
            Text text = new Text(String.valueOf(hour));
            text.setX(x - 5); // Offset for better centering
            text.setY(y + 5); // Offset for better centering

            getChildren().add(text);

            // Increment the angle for the next hour mark
            startAngle += angleStep;
        }

        // Draw lines around the clock
        drawLinesAroundClock(centerX, centerY, clockRadius, 6, 5, 8);

        // Draw second hand
        double sLength = clockRadius * 0.8;
        double secondX = centerX + sLength
                * Math.sin(second * (2 * Math.PI / 60));
        double secondY = centerY - sLength
                * Math.cos(second * (2 * Math.PI / 60));
        Line sLine = new Line(centerX, centerY, secondX, secondY);

        sLine.setStroke(Color.RED);

        // Draw minute hand
        double mLength = clockRadius * 0.65;
        double xMinute = centerX + mLength
                * Math.sin(minute * (2 * Math.PI / 60));
        double minuteY = centerY - mLength
                * Math.cos(minute * (2 * Math.PI / 60));
        Line mLine = new Line(centerX, centerY, xMinute, minuteY);

        mLine.setStroke(Color.BLUE);

        // Draw hour hand
        double hLength = clockRadius * 0.5;
        double hourX = centerX + hLength
                * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
        double hourY = centerY - hLength
                * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
        Line hLine = new Line(centerX, centerY, hourX, hourY);

        hLine.setStroke(Color.GREEN);

        getChildren().addAll(circle, sLine, mLine, hLine);
    }

    private void drawLinesAroundClock(double centerX, double centerY, double clockRadius, double angleStep, double defaultLineLength, double specialLineLength) {
        List<Line> lines = new ArrayList<>();
        for (double angle = 0; angle < 360; angle += angleStep) {
            double startX = centerX + clockRadius * Math.cos(Math.toRadians(angle));
            double startY = centerY - clockRadius * Math.sin(Math.toRadians(angle));

            double endX, endY;

            if (angle % 90 == 0) {
                // At 0, 90, 180, and 270 degrees, use the special line length
                endX = startX - specialLineLength * Math.cos(Math.toRadians(angle));
                endY = startY + specialLineLength * Math.sin(Math.toRadians(angle));
            } else {
                // For other angles, use the default line length
                endX = startX - defaultLineLength * Math.cos(Math.toRadians(angle));
                endY = startY + defaultLineLength * Math.sin(Math.toRadians(angle));
            }

            Line line = new Line(startX, startY, endX, endY);
            lines.add(line);
        }

        getChildren().addAll(lines);
    }

}
