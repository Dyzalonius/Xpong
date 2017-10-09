package xpong;

import processing.core.PApplet;

/**
 * One paddle is one player.
 *
 * @author Ties van Kipshagen
 */
public class Paddle extends GameObject {

    PApplet applet;
    float posX, posY;
    int paddleWidth, paddleHalfHeight, paddleHeight, score;
    char charUp, charDown;
    boolean keyUp, keyDown;
    CollisionBoxRect collisionBox;

    /**
     * Constructor.
     *
     * @param posX
     * @param charUp
     * @param charDown
     * @param applet
     */
    public Paddle(float posX, char charUp, char charDown, PApplet applet) {
        this.applet = applet;
        this.charUp = charUp;
        this.charDown = charDown;
        this.deltaY = (float) (applet.width / 400 * 1.5);
        this.paddleWidth = applet.width / 80;
        this.paddleHalfHeight = applet.height / 10;
        this.paddleHeight = 2 * paddleHalfHeight;
        this.posX = posX;
        this.posY = applet.height / 2 - paddleHalfHeight;
        this.keyUp = false;
        this.keyDown = false;
        this.score = 0;
        this.collisionBox = new CollisionBoxRect(this.posX + (this.paddleWidth / 2), this.posY + this.paddleHalfHeight, 1, this.paddleWidth, this.paddleHeight);
        Applet.gameObjectArray.add(this);
    }

    /**
     * Getter posX.
     *
     * @return
     */
    public float getPosX() {
        return posX;
    }

    /**
     * Getter posY.
     *
     * @return
     */
    public float getPosY() {
        return posY;
    }

    /**
     * Getter charUp.
     *
     * @return
     */
    public char getCharUp() {
        return charUp;
    }

    /**
     * Getter charDown.
     *
     * @return
     */
    public char getCharDown() {
        return charDown;
    }

    /**
     * Getter paddleWidth.
     *
     * @return
     */
    public float getPaddleWidth() {
        return paddleWidth;
    }

    /**
     * Getter paddleHalfHeight.
     *
     * @return
     */
    public float getPaddleHalfHeight() {
        return paddleHalfHeight;
    }

    /**
     * Getter paddleHeight.
     *
     * @return
     */
    public float getPaddleHeight() {
        return paddleHeight;
    }

    /**
     * Getter score.
     *
     * @return
     */
    public int getScore() {
        return score;
    }
    
    @Override
    public CollisionBoxRect getCollisionBox() {
        return collisionBox;
    }

    /**
     * Setter keyUp.
     *
     * @param newKeyUp
     */
    public void setKeyUp(boolean newKeyUp) {
        keyUp = newKeyUp;
    }

    /**
     * Setter keyDown.
     *
     * @param newKeyDown
     */
    public void setKeyDown(boolean newKeyDown) {
        keyDown = newKeyDown;
    }

    /**
     * Adder score.
     */
    public void addScore() {
        score++;
    }

    /**
     * Handles the paddle.
     */
    public void handlePaddle() {
        // Move paddle up
        if (keyUp && posY >= 0) { //TODO: move this piece of collision
            posY -= deltaY;
        }

        // Move paddle down
        if (keyDown && (posY + paddleHeight) <= applet.height) { //TODO: move this piece of collision
            posY += deltaY;
        }
        
        collisionBox.setPosX(posX+paddleWidth/2);
        collisionBox.setPosY(posY+paddleHalfHeight);

        applet.fill(255, 255);
        applet.strokeWeight(0);
        applet.stroke(255);
        applet.rect(posX, posY, paddleWidth, paddleHeight);
    }

    /**
     * Resets the paddles variables to start values.
     */
    public void resetPaddle() {
        posY = applet.height / 2 - paddleHalfHeight;
        score = 0;
    }
}
