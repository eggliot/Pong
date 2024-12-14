import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PongPanel extends JPanel implements KeyListener {

    public static final int WIDTH = 800, HEIGHT = 600;
    // locations
    private int paddle1Y = HEIGHT / 2, paddle2Y = HEIGHT / 2;
    private int ballX = WIDTH / 2, ballY = HEIGHT / 2;
    // sizes
    private int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 100;
    private int BALL_RADIUS = 20;
    // poses
    private int PADDLE_SPEED = 5;
    private int ballSpeedX = 3, ballSpeedY = 3;

    //scores
    private int p1Score, p2Score;


    // Keys
    private boolean w = false, s = false, up = false, down = false;

    public static boolean randomAllowed = false;
    private boolean randomEnabled = false;

    private Random random = new Random();

    private Color backgroundColor = Color.BLACK;
    private Color objectColor = Color.WHITE;

    private Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.CYAN, Color.LIGHT_GRAY, Color.DARK_GRAY};

    
    private boolean running = true;
    
    
    public PongPanel() {
        setBackground(backgroundColor);
        setFocusable(true);
        addKeyListener(this); // i am the listener
    }


    public void startGame() {
        System.out.println("Game Started");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateGame();
                repaint();
                if (randomAllowed) {
                    updateRandom();
                }
                if (p1Score == 5 || p2Score == 5 || !running) {
                    System.out.println("Game Over");
                    System.out.println("P1: " + p1Score + " P2: " + p2Score);
                    System.exit(0);
                }
            }
        };
        // schedule the updates and drawing for near 60 fps - 1000/60 = ~16 millis
        timer.scheduleAtFixedRate(task, 0, 16);
    }

    private void updateRandom() {
        if (!randomEnabled) {
            if (random.nextInt(0, 10000) > 10) {
                randomEnabled = true;
                switch (random.nextInt(0, 4)) {
                    case 0 -> BALL_RADIUS += random.nextInt(5, 26); // inc ball radius by 10 to 25
                    case 1 -> PADDLE_SPEED += random.nextInt(-2, 3); // inc paddle speed by -2 to 2
                    case 2 -> objectColor = colors[random.nextInt(0, colors.length)]; // random color
                    case 3 -> PADDLE_HEIGHT += random.nextInt(-20, 21); // inc paddle height by -20 to 20
                }
            }
        } else {
            if (random.nextInt(0, 10000) > 90) {
                randomEnabled = false;
                BALL_RADIUS = 20;
                PADDLE_SPEED = 5;
                objectColor = Color.WHITE;
            }
        }
    

}

private void updateGame() {
    // move the paddles - they are opposite cause positive is down
    if (w && paddle1Y > 0) paddle1Y -= PADDLE_SPEED;
    if (s && paddle1Y < HEIGHT - PADDLE_HEIGHT) paddle1Y += PADDLE_SPEED;
    if (up && paddle2Y > 0) paddle2Y -= PADDLE_SPEED;
    if (down && paddle2Y < HEIGHT - PADDLE_HEIGHT) paddle2Y += PADDLE_SPEED;

    // ball movement
    ballX += ballSpeedX;
    ballY += ballSpeedY;

    // did ball hit walls
    if (ballY <= BALL_RADIUS || ballY >= HEIGHT) ballSpeedY *= -1;

    // ball collide with paddles
    // are the ball coords bounded by the paddle coords? ball y more than paddle top but less than the bottom (inverted coordinates remember)
    if (ballX <= PADDLE_WIDTH && ballY >= paddle1Y && ballY <= paddle1Y + PADDLE_HEIGHT) {
        ballSpeedX *= -1;
    }
    if (ballX >= WIDTH - PADDLE_WIDTH - BALL_RADIUS && ballY >= paddle2Y && ballY <= paddle2Y + PADDLE_HEIGHT) {
        ballSpeedX *= -1;
    }

    // scoring p1 is right and p2 is left
    if (ballX < PADDLE_WIDTH) {// if it passes p1 paddle ie x=0 on the left
        p2Score++;
        ballSpeedX *= -1;
        ballSpeedY *= -1;
        resetBall();
        System.out.println("P1: " + p1Score);
    }
    if (ballX > WIDTH - PADDLE_WIDTH) { // if it goes past the left paddle
        p1Score++;
        ballSpeedX *= -1;
        ballSpeedY *= -1;
        resetBall();
        System.out.println("P2 " + p2Score);
    }
}

private void resetBall() {
    ballX = WIDTH / 2;
    ballY = HEIGHT / 2;

}

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(objectColor);

    // draw paddles
    g.fillRect(0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
    g.fillRect(getWidth() - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

    // ball
    g.fillOval(ballX, ballY, BALL_RADIUS, BALL_RADIUS);

}

// tjese are for swing to listen to keys and whatnot it does its stuff for me thanks

@Override
public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();

    // ste key states to true for the corresponding keys
    if (keyCode == KeyEvent.VK_W) w = true;
    if (keyCode == KeyEvent.VK_S) s = true;
    if (keyCode == KeyEvent.VK_UP) up = true;
    if (keyCode == KeyEvent.VK_DOWN) down = true;
    if (keyCode == KeyEvent.VK_ESCAPE) running = false;
}

@Override
public void keyReleased(KeyEvent e) { // do the op for released
    int keyCode = e.getKeyCode();

    // false key states if released
    if (keyCode == KeyEvent.VK_W) w = false;
    if (keyCode == KeyEvent.VK_S) s = false;
    if (keyCode == KeyEvent.VK_UP) up = false;
    if (keyCode == KeyEvent.VK_DOWN) down = false;
}

@Override
public void keyTyped(KeyEvent e) {
// just have to override yk
}

}
