package local;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 17.04.2017.
 */
public class Mouse extends Shape {

    public Mouse(int x, int y) {
        filename = "mouse_section.png";

        points = new ArrayList<Point>();

        points.add(new Point(x, y));
    }


}
