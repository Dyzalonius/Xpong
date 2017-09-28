package xpong;

import processing.core.PApplet;

/**
 * Effect that creates an explosion on a position.
 *
 * @author Ties van Kipshagen
 */
public class EffectExplosion extends Effect {

    int framesMax;

    /**
     * Constructor.
     *
     * @param posX
     * @param posY
     * @param applet
     */
    public EffectExplosion(float posX, float posY, PApplet applet) {
        super(posX, posY, applet);
        this.framesMax = (int) applet.frameRate / 8;
    }

    /**
     * Handles effect every frame, both drawing and cleanup.
     */
    @Override
    public void handleEffect() {
        applet.fill(255, 0);
        applet.strokeWeight(5);
        applet.stroke(255);

        applet.ellipse(posX, posY, frames * 5, frames * 5);

        if (this.frames >= this.framesMax) {
            Applet.effectArray.remove(this);
        }
        frames++;
    }
}
