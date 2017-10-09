package xpong;

import java.util.ArrayList;
import processing.core.PApplet;

/**
 * Effect that creates a trail for a ball
 *
 * @author Ties van Kipshagen
 */
public class EffectTrail extends Effect {

    ArrayList<float[]> trailArray;
    Ball target;

    /**
     * Constructor.
     *
     * @param posX
     * @param posY
     * @param applet
     * @param ball
     */
    public EffectTrail(float posX, float posY, PApplet applet, Ball ball) {
        super(posX, posY, applet);
        this.target = ball;
        this.trailArray = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            float[] placeholderPos = {-1000, -1000};
            trailArray.add(placeholderPos);
        }
    }

    /**
     * Updates effect every frame, both drawing and cleanup.
     */
    @Override
    public void updateEffect() {
        // remove effect if ball is not being updated anymore, frames > 100 to make sure its not removed on spawn
        if (!(Applet.ballArray.contains(target)) && frames > 100) {
            if (trailArray.isEmpty()) {
                Applet.effectArray.remove(this);
            }
        } else {
            // add current position to the trailArray
            float[] currentPos = {target.getPosX(), target.getPosY()};
            trailArray.add(currentPos);
        }

        // remove the oldest position because obsolete (cleanup)
        if (!trailArray.isEmpty()) {
            trailArray.remove(0);
        }

        frames++;
    }
    
    @Override
    public void drawEffect() {
        // draw the trail with positions from the trailArray
        for (int i = 0; i < trailArray.size(); i++) {
            applet.noStroke();
            applet.fill(255, 50 - (100 - i) / 2);
            applet.ellipse(trailArray.get(i)[0], trailArray.get(i)[1], (float) (target.getBallSize()), (float) (target.getBallSize()));
        }
    }
}
