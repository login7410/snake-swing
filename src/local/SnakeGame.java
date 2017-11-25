package local;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by user on 17.04.2017.
 */
public class SnakeGame {
    public static SnakeGame game = new SnakeGame();
//    private JFrame frame;
    private JPanel canvas;
    private KeyboardObserver keyboardObserver;
    private boolean paused;

    private Mouse mouse;
    private Snake snake;
    private Wall walls;

    public Wall getWalls() {
        return walls;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void createMouse() {
        boolean created = false;

        while (!created) {
            int x = (int) (Math.random() * Constants.FIELD_WIDTH);
            int y = (int) (Math.random() * Constants.FIELD_HEIGHT);

            Point point = new Point(x, y);

            if (!walls.getPoints().contains(point) && !snake.getPoints().contains(point)) {
                mouse = new Mouse(x, y);
                created = true;
            }
        }
    }

    public void eatMouse() {
        createMouse();
    }

    public static void main(String[] args) {
        game.makeGUI();
        game.init();
    }

    private void gamePrepare() {
        walls = new Wall();
        snake = new Snake();
        createMouse();
        paused = false;

    }

    private void init() {
        //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();

        gamePrepare();

        new GameThread().start();
    }

    private void makeGUI() {
        JFrame frame = new JFrame("Snake");
//        frame.setTitle("Transparent JFrame Demo");

//        frame.setUndecorated(true);
//        frame.setSize((Constants.FIELD_WIDTH + 1) * Constants.ASPECT_RATIO, (Constants.FIELD_HEIGHT + 1) * Constants.ASPECT_RATIO);

        frame.setSize((Constants.FIELD_WIDTH + 1) * Constants.ASPECT_RATIO + 4, (Constants.FIELD_HEIGHT + 1) * Constants.ASPECT_RATIO + 28);
        frame.setResizable(false);                            // No window resize
        frame.setLocationRelativeTo(null);                    // Располагать в центре экрана
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();                       // Корневая панель
//        main.setBackground(Color.blue);                   // Синий цвет - будущая решётка
        frame.getContentPane().add(BorderLayout.CENTER, canvas);                       // Добавляем в окно


        canvas.setFocusable(true);
        canvas.requestFocusInWindow();

//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setLayout(new GridBagLayout());

//        frame.setOpacity(0.0f);
        frame.setVisible(true);
    }

    class GameThread extends Thread {
        @Override
        public void run() {

            while (true) {
                if (keyboardObserver.hasKeyEvents()) {
                    KeyEvent event = keyboardObserver.getEventFromTop();
                    //Если равно символу пробела - рестарт.
                    if (event.getKeyChar() == KeyEvent.VK_SPACE) {
                        gamePrepare();
                    }
                    if (event.getKeyChar() == KeyEvent.VK_ESCAPE) {
                        System.exit(0);
                    }
                }

                while (snake.isAlive()) {
                    //"наблюдатель" содержит события о нажатии клавиш?
                    if (keyboardObserver.hasKeyEvents()) {
                        KeyEvent event = keyboardObserver.getEventFromTop();
                        //Если равно символу 'q' - выйти из игры.
                        if (event.getKeyChar() == 'q' || event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            System.exit(0);
                        }

                        if (event.getKeyChar() == KeyEvent.VK_SPACE) {
                            paused = !paused;
                        }

                        //Если "стрелка влево" - сдвинуть фигурку влево
                        if (event.getKeyCode() == KeyEvent.VK_LEFT)
                            if (snake.getDirection() == SnakeDirection.LEFT) {
                                snake.setBoosted(true);
                            } else {
                                snake.setBoosted(false);
                                snake.setDirection(SnakeDirection.LEFT);
                            }
                            //Если "стрелка вправо" - сдвинуть фигурку вправо
                        else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                            if (snake.getDirection() == SnakeDirection.RIGHT) {
                                snake.setBoosted(true);
                            } else {
                                snake.setBoosted(false);
                                snake.setDirection(SnakeDirection.RIGHT);
                            }
                            //Если "стрелка вверх" - сдвинуть фигурку вверх
                        else if (event.getKeyCode() == KeyEvent.VK_UP)
                            if (snake.getDirection() == SnakeDirection.UP) {
                                snake.setBoosted(true);
                            } else {
                                snake.setBoosted(false);
                                snake.setDirection(SnakeDirection.UP);
                            }
                            //Если "стрелка вниз" - сдвинуть фигурку вниз
                        else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                            if (snake.getDirection() == SnakeDirection.DOWN) {
                                snake.setBoosted(true);
                            } else {
                                snake.setBoosted(false);
                                snake.setDirection(SnakeDirection.DOWN);
                            }
                    }

                    if (!paused) {
                        snake.move();
                    }
                    canvas.repaint();
                    sleep();
                }
            }
        }

        public void sleep() {

            try {
                int level = snake.getPoints().size();
                int delay = level < 11 ? (Constants.INITIAL_DELAY - Constants.DELAY_STEP * level) : 100;
                if (snake.isBoosted()) {
                    delay = delay / 2;
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Панель для рисования
    class Canvas extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            canvas.setBackground(new Color(170, 239, 110));
            g.drawImage(new ImageIcon(this.getClass().getResource(
                    "images/bg.jpg")).getImage(), 0, 0, null);


            walls.draw(g);      // Отрисовка стен
            snake.draw(g);      // Отрисовка змеи
            mouse.draw(g);      // Отрисовка мыши

            // Если конец игры - вывести "Game over"
            if (!snake.isAlive()) {
                g.setColor(Color.white);
                g.setFont(new Font("Tahoma", Font.BOLD, 40));
                g.drawString("G A M E   O V E R", canvas.getWidth() / 2 - 170, canvas.getHeight() / 2 + 20);
            }

            // Пауза
            if (paused) {
                g.setColor(Color.white);
                g.setFont(new Font("Tahoma", Font.BOLD, 40));
                g.drawString("P A U S E D", canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 + 20);
            }
        }
    }

    // Мониторинг клавиатуры
    class KeyboardObserver extends Thread {
        private Queue<KeyEvent> keyEvents = new ArrayBlockingQueue<KeyEvent>(100);

        @Override
        public void run() {

            canvas.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent e) {
                    //do nothing
                }

                public void keyReleased(KeyEvent e) {
                    //do nothing
                }

                public void keyPressed(KeyEvent e) {
                    keyEvents.add(e);
                }
            });
        }

        public boolean hasKeyEvents() {
            return !keyEvents.isEmpty();
        }

        public KeyEvent getEventFromTop() {
            return keyEvents.poll();
        }
    }

}
