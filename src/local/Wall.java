package local;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by user on 17.04.2017.
 */
public class Wall extends Shape {

    public Wall() {
        filename = "border_section.png";

        points = new ArrayList<Point>();

        makeHorizontalLine(0, Constants.FIELD_WIDTH, 0);
        makeHorizontalLine(0, Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT);
        makeVerticalLine(0, 0, Constants.FIELD_HEIGHT);
        makeVerticalLine(Constants.FIELD_WIDTH, 0, Constants.FIELD_HEIGHT);

        makeVerticalLine(Constants.FIELD_WIDTH / 2, Constants.FIELD_HEIGHT / 2 - 1, Constants.FIELD_HEIGHT / 2 + 1);

    }

    private void makeHorizontalLine(int column1, int column2, int row) {
        for (int i = column1; i <= column2; i++) {
            points.add(new Point(i, row));
        }
    }

    private void makeVerticalLine(int column, int row1, int row2) {
        for (int i = row1; i <= row2; i++) {
            points.add(new Point(column, i));
        }
    }

}
