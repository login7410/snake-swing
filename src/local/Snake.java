package local;

import local.Shape;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by user on 17.04.2017.
 */
public class Snake extends Shape {
    private SnakeDirection direction;
    private boolean isAlive;
    private boolean isBoosted;

    public SnakeDirection getDirection() {
        return direction;
    }

    public void setDirection(SnakeDirection direction) {
        this.direction = direction;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isBoosted() {
        return isBoosted;
    }

    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }

    public Snake() {
        this(Constants.FIELD_WIDTH / 2 - 3, Constants.FIELD_HEIGHT / 2 - 3);
    }

    public Snake(int x, int y) {
        isAlive = true;

        filename = "snake_section.png";

        direction = SnakeDirection.DOWN;

        points = new ArrayList<Point>();
        points.add(new Point(x, y));

    }

    public void move() {

        if (!isAlive) return;

        if (direction == SnakeDirection.UP)
            move(0, -1);
        else if (direction == SnakeDirection.RIGHT)
            move(1, 0);
        else if (direction == SnakeDirection.DOWN)
            move(0, 1);
        else if (direction == SnakeDirection.LEFT)
            move(-1, 0);

    }

    private void move(int dx, int dy) {
        Point head = points.get(0);
        head = new Point(head.getX() + dx, head.getY() + dy);

        //Проверяем - не вылезла ли голова за границу комнаты
        checkBorders(head);
        if (!isAlive) return;

        //Проверяем - не пересекает ли змея саму себя
        checkBody(head);
        if (!isAlive) return;

        //Проверяем - не съела ли змея мышь.
        Mouse mouse = SnakeGame.game.getMouse();
        if (head.equals(mouse.getPoints().get(0))) {
            // съела
            points.add(0, head);
            SnakeGame.game.eatMouse();
        } else {
            points.add(0, head);
            points.remove(points.size() - 1);

        }
    }

    private void checkBorders(Point point) {
        if (SnakeGame.game.getWalls().getPoints().contains(point)) {
            isAlive = false;
        }
//        if ((head.getX() < 0 || head.getX() >= Room.game.getWidth()) || head.getY() < 0 || head.getY() >= Room.game.getHeight()) {
//            isAlive = false;
//        }
    }

    private void checkBody(Point point) {
        if (points.contains(point)) {
            isAlive = false;
        }
    }

}
