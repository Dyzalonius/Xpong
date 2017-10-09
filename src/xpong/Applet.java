package xpong;

import java.util.ArrayList;
import processing.core.PApplet;

/**
 * Applet for Xpong. This is basically the main class. It extends PApplet so we can use draw() etc.
 *
 * @author Ties van Kipshagen
 */
public class Applet extends PApplet {

    public static ArrayList<Ball> ballArray;
    public static ArrayList<Box> boxArray;
    public static ArrayList<Effect> effectArray;
    public static ArrayList<Paddle> paddleArray;
    public static ArrayList<Powerup> powerupArray;
    public static ArrayList<GameObject> gameObjectArray;

    /**
     * Runs on startup.
     */
    @Override
    public void settings() {
        size(800, 500); // 1600 x 900
    }

    /**
     * Runs once after startup.
     */
    @Override
    public void setup() {
        frameRate(180);
        background(0);
        drawMiddleLine();

        ballArray = new ArrayList<>();
        boxArray = new ArrayList<>();
        effectArray = new ArrayList<>();
        paddleArray = new ArrayList<>();
        powerupArray = new ArrayList<>();
        gameObjectArray = new ArrayList<>();
        
        boxArray.add((new Box(0, -10, width, 10, this)));
        boxArray.add((new Box(0, height, width, 10, this)));
        //boxArray.add((new Box(-10, 0, 10, height, this)));
        //boxArray.add((new Box(width, 0, 10, height, this)));
        
        paddleArray.add((new Paddle(width / 80, 'W', 'S', this))); //TODO: allow for keyinput UP and DOWN
        paddleArray.add((new Paddle(width - 2*(width / 80), 'O', 'L', this)));
    }

    /**
     * Runs every frame after startup.
     */
    @Override
    public void draw() {
        if (focused) {
            background(0);
            drawMiddleLine();
            drawScores();

            handleBalls();
            handleEffects();
            handlePowerups();
            handlePaddles();
            handleCollision();
        } else {
            background(0);
            fill(255);
            int textSize = width / 4;
            textSize(textSize);
            text("PAUSED", width / 40, height / 2 + (textSize / 4));
        }
    }

    /**
     * Check if any assigned keys are pressed.
     */
    @Override
    public void keyPressed() {
        for (int i = 0; i < paddleArray.size(); i++) {
            if (keyCode == paddleArray.get(i).getCharUp()) {
                paddleArray.get(i).setKeyUp(true);
            }
            if (keyCode == paddleArray.get(i).getCharDown()) {
                paddleArray.get(i).setKeyDown(true);
            }
        }
        if (keyCode == 'R') {
            resetGame();
        }
    }

    /**
     * Check if any assigned keys are released.
     */
    @Override
    public void keyReleased() {
        for (int i = 0; i < paddleArray.size(); i++) {
            if (keyCode == paddleArray.get(i).getCharUp()) {
                paddleArray.get(i).setKeyUp(false);
            }
            if (keyCode == paddleArray.get(i).getCharDown()) {
                paddleArray.get(i).setKeyDown(false);
            }
        }
    }

    /**
     * Draws the middle line of the field.
     */
    private void drawMiddleLine() {
        strokeWeight(4);
        stroke(255);
        strokeCap(PROJECT);

        for (int i = 0; i < 25; i += 2) {
            line(width / 2, height / 25 * i, width / 2, height / 25 * (i + 1));
        }
    }

    /**
     * Draws the scores on either side of the middle line.
     */
    private void drawScores() {
        int score1 = paddleArray.get(0).getScore();

        // check how many digits score1 has
        int score1offset = (int) Math.log10(score1) + 1;

        int score2 = paddleArray.get(1).getScore();
        int textSize = height / 10;

        fill(255);
        textSize(textSize);

        text(score1, (width / 2) - 20 - (score1offset * textSize / 3 * 2), textSize + 10);
        text(score2, (width / 2) + 20, textSize + 10);
    }

    /**
     * Handle all balls every frame.
     */
    private void handleBalls() {
        // if no balls remaining, spawn new ball
        if (ballArray.isEmpty()) {
            Ball ball = new Ball(this);
            ballArray.add(ball);

            // spawn trail for ball
            Effect trail = new EffectTrail(ball.getPosX(), ball.getPosY(), this, ball);
            Applet.effectArray.add(trail);
        }

        // handle all balls
        for (int i = 0; i < ballArray.size(); i++) {
            ballArray.get(i).handleBall();
        }
    }

    /**
     * Handle all effects every frame.
     */
    private void handleEffects() {
        // handle all explosions
        for (int i = 0; i < effectArray.size(); i++) {
            effectArray.get(i).handleEffect();
        }
    }

    /**
     * Handle all paddles every frame.
     */
    private void handlePaddles() {
        // handle all paddles
        for (int i = 0; i < paddleArray.size(); i++) {
            paddleArray.get(i).handlePaddle();
        }
    }

    /**
     * Handle all powerups every frame.
     */
    private void handlePowerups() {
        // randomly (one in 1000 frames) spawn new powerups
        if (powerupArray.size() < 3 && Math.random() > 0.999) {
            Powerup powerup = new Powerup(this);
            powerupArray.add(powerup);
        }

        // handle all powerups
        for (int i = 0; i < powerupArray.size(); i++) {
            powerupArray.get(i).drawPowerup();
        }
    }

    private void handleCollision() {
        for (int i = 0; i < gameObjectArray.size(); i++) {
            for (int j = 0; j < gameObjectArray.size(); j++) {
                if (!gameObjectArray.get(i).equals(gameObjectArray.get(j))) {
                    int collisionCheck = gameObjectArray.get(i).getCollisionBox().checkCollision(gameObjectArray.get(j));
                    if (collisionCheck != -1) {
                        gameObjectArray.get(i).getCollisionBox().handleCollision(collisionCheck, gameObjectArray.get(j));
                    }
                }
            }
        }
    }

    /**
     * Reset all variable to starting values.
     */
    private void resetGame() {
        // remove all balls and powerups from gameObjects
        for (int i = gameObjectArray.size()-1; i >= 0; i--) {
            if ((gameObjectArray.get(i) instanceof Ball) || (gameObjectArray.get(i) instanceof Powerup)) {
                gameObjectArray.remove(i);
            }
        }
        
        ballArray = new ArrayList<>();
        powerupArray = new ArrayList<>();
        
        // spawn ball
        Ball ball = new Ball(this);
        ballArray.add(ball);
        
        // spawn trail for ball
        Effect trail = new EffectTrail(ball.getPosX(), ball.getPosY(), this, ball);
        Applet.effectArray.add(trail);

        for (int i = 0; i < paddleArray.size(); i++) {
            paddleArray.get(i).resetPaddle();
        }
    }
}
