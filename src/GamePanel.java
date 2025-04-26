import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;  // size of each comoponent of the snake 
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT) / (UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 1000;  // delay between each movement of the snake

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;   // initial length of the snake starts with 6 parts which are 25X25 units
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R'; // R = right, L = left, U = up, D = down
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }//GamePanel

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }//startGame

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }//paintComponent

    public void draw(Graphics g) {
        if (running) {
            // draw the apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draw the snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) 
                {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }//if 
                else 
                {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }//else
            }//for i

            // draw the score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }//if
        else 
        {
            gameOver(g);
        }//else
    }//draw

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }//newApple

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }//for i

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                System.out.println(y[0]);
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                System.out.println(y[0]);
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                System.out.println(x[0]);
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                System.out.println(x[0]);
                break;
            case 'E':
                x[0] = x[0] + UNIT_SIZE;
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'C':
                x[0] = x[0] + UNIT_SIZE;
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'Q':
                x[0] = x[0] - UNIT_SIZE;
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'Z':
                x[0] = x[0] - UNIT_SIZE;
                y[0] = y[0] + UNIT_SIZE;
                break;
        }//switch
    }//move

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }//if
    }//checkApple

    public void checkCollisions() {
        // check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }//if
        }//for i

        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }//if
        // check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }//if
        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }//if
        // check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }//if

        if (!running) {
            timer.stop();
        }//if
    }//checkCollisions

    public void gameOver(Graphics g) {
        // Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }//gameOver

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }//if
        repaint();
    }//actionPerformed

    public class MyKeyAdapter extends KeyAdapter {
        boolean leftPressed = false;
        boolean rightPressed = false;
        boolean upPressed = false;
        boolean downPressed = false;
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (!rightPressed) {
                        leftPressed = true;
                    }//if
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!leftPressed) {
                        rightPressed = true;
                    }//if
                    break;
                case KeyEvent.VK_UP:
                    if (!downPressed) {
                        upPressed = true;
                    }//if
                    break;
                case KeyEvent.VK_DOWN:
                    if (!upPressed) {
                        downPressed = true;
                    }//if
                    break;
            }//switch
            if(rightPressed && upPressed){
                direction = 'E';
            }//if
            else if(rightPressed && downPressed){
                direction = 'C';
            }//else if
            else if(leftPressed && upPressed){
                direction = 'Q';
            }//else if
            else if(leftPressed && downPressed){
                direction = 'Z';
            }//else if
            else if(rightPressed){
                direction = 'R';
            }//else if
            else if(leftPressed){
                direction = 'L';
            }//else if
            else if(upPressed){
                direction = 'U';
            }//else if
            else if(downPressed){
                direction = 'D';
            }
        }//keyPressed

        @Override
        public void keyReleased(KeyEvent e){
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                    leftPressed = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    rightPressed = false;
                    break;
                case KeyEvent.VK_UP:
                    upPressed = false;
                    break;
                case KeyEvent.VK_DOWN:
                    downPressed = false;
                    break;
            }//switch
        }//keyReleased
    }//MyKeyAdapter
}//GamePannel
