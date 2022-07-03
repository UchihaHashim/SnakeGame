package com.hashim;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;

    final int[] x = new int[GAME_UNITS + UNIT_SIZE];
    final int[] y = new int[GAME_UNITS + UNIT_SIZE];
    int bodyParts = 5;
    int applesEaten = 0;
    int highScore;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setBorder(new LineBorder(Color.yellow,2));
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();

    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, 0, i * UNIT_SIZE);
            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++)
                if (i == 0) {
                    g.setColor(Color.RED);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //rainbow Snake
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.green);
                    g.fillOval(x[i] + 2, y[i] + 2, UNIT_SIZE - 5, UNIT_SIZE - 5);
                }
            {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
            }

        } else {
            gameOver(g);
        }

    }

    public void newApple() {

        for (int i = 0; i < bodyParts; i++) {
            while ((appleX == x[i]) && (appleY == y[i])) {
                appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
                appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
            }
        }
    }


    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollision() {
        //CHECKS IF HEAD COLLIDES WITH BODY
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }
        //CHECKS IF HEAD HITS LEFT BORDER
        if (x[0] < 0) {
            running = false;
        }
        //CHECKS IF HEAD HITS RIGHT BORDER
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = false;
        }
        //CHECKS IF HEAD HITS TOP BORDER
        if (y[0] < 0) {
            running = false;
        }
        //CHECKS IF HEAD HITS BOTTOM BORDER
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }
        if (!running)
            timer.stop();
    }

    public void gameOver(Graphics g) {
        //Game Over
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);


        //Score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.white);
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Space to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press Space to restart")) / 2, (SCREEN_HEIGHT / 2) + 40);

        //HighScore
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString("Highscore: " + highScore, (SCREEN_WIDTH - metrics4.stringWidth("Score: " + applesEaten)) / 2 - 30, SCREEN_HEIGHT/2 - 150);
    }

    public void highScore(){
        if (highScore < applesEaten) {
            highScore = applesEaten;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
            highScore();
        }
        repaint();
    }

    public void restartGame() {
        new GamePanel();
        running = true;
        direction = 'R';
        x[0] = 0;
        y[0] = 0;
        applesEaten = 0;
        bodyParts = 3;
        newApple();
        timer = new Timer(DELAY, this);
        timer.start();
        move();
        repaint();
    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case (KeyEvent.VK_LEFT), (KeyEvent.VK_A) -> {
                    if (running) {
                        if (direction != 'R') direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if (running) {
                        if (direction != 'L') direction = 'R';
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (running) {
                        if (direction != 'D') direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (running) {
                        if (direction != 'U') direction = 'D';
                    }
                }
                case KeyEvent.VK_SPACE -> {
                    if (!running) {
                        restartGame();
                    }
                }

            }
        }
    }
}
