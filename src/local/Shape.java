package local;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by user on 17.04.2017.
 */
public abstract class Shape {
    protected String filename;
    protected List<Point> points;

    public List<Point> getPoints() {
        return points;
    }

//    public String getFilename() {
//        return filename;
//    }

    public void draw(Graphics g) {
        for (Point point : points) {
            g.drawImage(new ImageIcon(this.getClass().getResource(
                    "images/" + filename)).getImage(), point.getX() * Constants.ASPECT_RATIO, point.getY() * Constants.ASPECT_RATIO, null);
        }
    }
}
