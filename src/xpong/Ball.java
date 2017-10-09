package xpong;

import processing.core.PApplet;

/**
 * A ball. Yeah, that's all.
 *
 * @author Ties van Kipshagen
 */
public class Ball extends GameObject {

    PApplet applet;
    float posX, posY;
    int ballRadius, ballSize;
    CollisionBoxRect collisionBox;

    /**
     * Empty constructor.
     *
     * @param applet
     */
    public Ball(PApplet applet) {
        this.applet = applet;
        this.posX = (applet.width / 2);
        this.posY = (applet.height / 2);
        this.deltaX = applet.width / 400;
        this.deltaY = 0;
        this.ballRadius = applet.width / 160;
        this.ballSize = 2 * ballRadius;
        this.collisionBox = new CollisionBoxRect(this.posX, this.posY, 2, this.ballSize, this.ballSize);
        Applet.gameObjectArray.add(this);
    }

    /**
     * Filled constructor.
     *
     * @param posX
     * @param posY
     * @param deltaX
     * @param deltaY
     * @param applet
     */
    public Ball(float posX, float posY, float deltaX, float deltaY, PApplet applet) {
        this.applet = applet;
        this.posX = posX;
        this.posY = posY;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.ballRadius = applet.width / 160;
        this.ballSize = 2 * ballRadius;
        this.collisionBox = new CollisionBoxRect(this.posX, this.posY, 2, this.ballSize, this.ballSize);
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
     * Getter ballSize.
     *
     * @return
     */
    public int getBallSize() {
        return ballSize;
    }

    /**
     * Getter ballRadius.
     *
     * @return
     */
    public int getBallRadius() {
        return ballRadius;
    }
    
    @Override
    public CollisionBoxRect getCollisionBox() {
        return collisionBox;
    }

    /**
     * Update the ball.
     */
    public void updateBall() {
        posX += deltaX;
        posY += deltaY;
        
        collisionBox.setPosX(posX);
        collisionBox.setPosY(posY);
    }
    
    public void drawBall() {
        applet.noStroke();
        applet.fill(255);
        applet.ellipse(posX, posY, ballSize, ballSize);
    }

    /**
     * Handles collision with other objects
     */
    private void handleCollision() { //TODO: remove and fully integrate in collision
        // collision top
        if ((posY - ballRadius) < 0) {
            deltaY = Math.abs(deltaY);
        }

        // collision bottom
        if ((posY + ballRadius) > applet.height) {
            deltaY = -Math.abs(deltaY);
        }

        // collision left
        if ((posX - ballRadius) < 0) {
            // player 2 scores
            Applet.paddleArray.get(1).addScore();
            if (Applet.ballArray.size() > 1) {
                Applet.ballArray.remove(this);
            } else {
                resetBall(2);
            }
        }

        // collision right
        if ((posX + ballRadius) > applet.width) {
            // player 1 scores
            Applet.paddleArray.get(0).addScore();
            if (Applet.ballArray.size() > 1) {
                Applet.ballArray.remove(this); 
            } else {
                resetBall(1);
            }
        }

        // collision left paddle
        if (((posX - ballRadius) < Applet.paddleArray.get(0).getPosX() + Applet.paddleArray.get(0).getPaddleWidth()) && ((posX - ballRadius) > Applet.paddleArray.get(0).getPosX()) && paddleCheck(Applet.paddleArray.get(0))) {
            if (deltaX < 0) {
                //check where on the paddle it hits, and change direction accordingly
                double oldSpeed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                float newDeltaY = (posY - (Applet.paddleArray.get(0).getPosY() + Applet.paddleArray.get(0).getPaddleHalfHeight())) * ((float) oldSpeed / 100);
                float newDeltaX = (float) Math.sqrt(oldSpeed * oldSpeed - newDeltaY * newDeltaY);
                deltaX = Math.abs(newDeltaX);
                deltaY = newDeltaY;
                // make the ball go faster
                deltaX *= 1.05;
                deltaY *= 1.05;
            }
        }

        // collision right paddle
        if (((posX + ballRadius) > Applet.paddleArray.get(1).getPosX()) && ((posX + ballRadius) < Applet.paddleArray.get(1).getPosX() + Applet.paddleArray.get(1).getPaddleWidth()) && paddleCheck(Applet.paddleArray.get(1))) {
            if (deltaX > 0) {
                //check where on the paddle it hits, and change direction accordingly
                double oldSpeed = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                float newDeltaY = (posY - (Applet.paddleArray.get(1).getPosY() + Applet.paddleArray.get(1).getPaddleHalfHeight())) * ((float) oldSpeed / 100);
                float newDeltaX = (float) Math.sqrt(oldSpeed * oldSpeed - newDeltaY * newDeltaY);
                deltaX = -Math.abs(newDeltaX);
                deltaY = newDeltaY;
                // make the ball go faster
                deltaX *= 1.05;
                deltaY *= 1.05;
            }
        }

        // collision powerups
        for (int i = 0; i < Applet.powerupArray.size(); i++) {
            if (Applet.powerupArray.get(i).getPowerupStatus() && Math.abs(Math.abs(posX) - Math.abs(Applet.powerupArray.get(i).getPosX())) < Applet.powerupArray.get(i).getPowerupSize() / 2 && Math.abs(Math.abs(posY) - Math.abs(Applet.powerupArray.get(i).getPosY())) < Applet.powerupArray.get(i).getPowerupSize() / 2) {
                Applet.powerupArray.get(i).activatePowerup(this);
            }
        }
    }

    /**
     * Check if paddle is positioned right to hit the ball.
     *
     * @param paddle
     * @return
     */
    boolean paddleCheck(Paddle paddle) {
        return (paddle.getPosY() < this.posY) && ((paddle.getPosY() + paddle.getPaddleHeight()) > this.posY);
    }

    /**
     * Powerup splitBall. Spawns a new ball that goes in the opposite direction.
     *
     * @param ball
     */
    public void splitBall(Ball ball) {
        // spawn a explosion
        Effect explosion = new EffectExplosion(ball.getPosX(), ball.getPosY(), applet);
        Applet.effectArray.add(explosion);
        
        // spawn the new ball
        Ball newBall = new Ball(ball.getPosX(), ball.getPosY(), -(ball.getDeltaX()), -(ball.getDeltaY()), applet);
        Applet.ballArray.add(newBall);

        // spawn trail for the new ball
        Effect trail = new EffectTrail(ball.getPosX(), ball.getPosY(), applet, newBall);
        Applet.effectArray.add(trail);
    }

    /**
     * Powerup bounceBall. Inverts the balls velocities.
     *
     * @param ball
     */
    public void bounceBall(Ball ball) {
        // spawn a explosion.
        Effect explosion = new EffectExplosion(ball.getPosX(), ball.getPosY(), applet);
        Applet.effectArray.add(explosion);
        
        // invert velocities.
        ball.invertDeltaX();
        ball.invertDeltaY();
    }

    /**
     * Reset the balls variables to start values.
     *
     * @param servingPlayer
     */
    void resetBall(int servingPlayer) {
        posX = (applet.width / 2);
        posY = (applet.height / 2);
        deltaX = applet.width / 400;
        deltaY = 0f;

        if (servingPlayer == 1) {
            deltaX = -Math.abs(deltaX);
        }
    }
}
