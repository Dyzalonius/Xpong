package xpong;

import processing.core.PApplet;

/**
 * Object that requires to be drawn differently every frame, and is purely visual.
 *
 * @author Ties van Kipshagen
 */
public abstract class Effect {

    int frames;
    float posX, posY;
    PApplet applet;

    /**
     * Constructor.
     *
     * @param posX
     * @param posY
     * @param applet
     */
    public Effect(float posX, float posY, PApplet applet) {
        this.posX = posX;
        this.posY = posY;
        this.applet = applet;
        this.frames = 0;
    }

    /**
     * Abstract method to handle the effect, specific for every effect.
     */
    public abstract void handleEffect();
}
