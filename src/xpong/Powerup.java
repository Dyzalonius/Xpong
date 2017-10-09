package xpong;

import processing.core.PApplet;

/**
 * Powerups that balls can collide with.
 *
 * @author Ties van Kipshagen
 */
public class Powerup extends GameObject {

    PApplet applet;
    float posX, posY;
    int powerupType, powerupSize, frames;
    boolean powerupStatus;
    CollisionBoxRect collisionBox;

    /**
     * Constructor.
     *
     * @param applet
     */
    public Powerup(PApplet applet) {
        this.applet = applet;
        this.powerupSize = applet.width / 16;
        this.powerupStatus = false;
        this.powerupType = genRandomPowerupType();

        genRandomPos();

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
     * Getter powerupSize.
     *
     * @return
     */
    public float getPowerupSize() {
        return powerupSize;
    }

    /**
     * Getter powerupStatus.
     *
     * @return
     */
    public boolean getPowerupStatus() {
        return powerupStatus;
    }

    @Override
    public CollisionBoxRect getCollisionBox() {
        return collisionBox;
    }

    /**
     * Generates a random posX around the middle line.
     */
    private void genRandomPos() {
        float newPosX = (float) (((applet.width / 5) * 2) + (Math.random() * (applet.width / 5)));
        float newPosY;
        boolean suitableY;

        do {
            newPosY = (float) (((applet.height / 8) * 2) + (Math.random() * (applet.height / 8) * 5));
            suitableY = true;

            for (Powerup powerup : Applet.powerupArray) {
                if (Math.abs(newPosY - powerup.getPosY()) < powerupSize) {
                    suitableY = false;
                    break;
                }
            }
        } while (!suitableY);

        posX = newPosX;
        posY = newPosY;
    }

    /**
     * Generates a random powerupType.
     *
     * @return
     */
    private int genRandomPowerupType() {
        int type = (int) (Math.random() * 3);

        if (type == 2) {
            type = 1;
        }
        return type;
    }

    /**
     * Draws the powerup.
     */
    public void drawPowerup() {
        if (!powerupStatus) {
            drawPowerupSpawning();
        } else {
            String text = "";

            switch (this.powerupType) {
                case 0:
                    text += "S";
                    applet.fill(255, 0, 0);
                    break;
                case 1:
                    text += "B";
                    applet.fill(0, 255, 0);
                    break;
                default:
                    applet.fill(0, 0, 255);
                    break;
            }
            applet.noStroke();
            applet.ellipse(posX, posY, powerupSize, powerupSize);

            int textSize = (this.powerupSize / 10) * 9;
            applet.textSize(textSize);
            applet.fill(0);
            applet.text(text, (float) (posX - (this.powerupSize / 3.9)), (float) (posY + (this.powerupSize / 3.1)));
        }
    }

    /**
     * Specifically draws the powerup while its still spawning.
     */
    private void drawPowerupSpawning() {
        if (frames < (applet.frameRate)) {
            switch (this.powerupType) {
                case 0:
                    applet.fill(255, 0, 0);
                    break;
                case 1:
                    applet.fill(0, 255, 0);
                    break;
                default:
                    applet.fill(0, 0, 255);
                    break;
            }
            float ellipseSize = powerupSize * (frames / (applet.frameRate));
            applet.noStroke();
            applet.ellipse(posX, posY, ellipseSize, ellipseSize);
            frames++;
        } else {
            powerupStatus = true;
            this.collisionBox = new CollisionBoxRect(this.posX, this.posY, 0, this.powerupSize, this.powerupSize);
            Applet.gameObjectArray.add(this);
        }
    }

    /**
     * Activates the powerup based on its type.
     *
     * @param ball
     */
    public void activatePowerup(Ball ball) {
        switch (powerupType) {
            case 0:
                ball.splitBall(ball);
                break;
            case 1:
                ball.bounceBall(ball);
                break;
            default:
                break;
        }
        Applet.powerupArray.remove(this);
    }
}
