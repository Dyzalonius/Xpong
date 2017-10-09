package xpong;

/**
 *
 * @author Ties van Kipshagen
 */
public abstract class GameObject {
    
    float deltaX, deltaY;
    
    public GameObject() {
        this.deltaX = 0;
        this.deltaY = 0;
    }
    
    public abstract CollisionBox getCollisionBox();
    
    public float getDeltaX() {
        return deltaX;
    }
    
    public float getDeltaY() {
        return deltaY;
    }

    public void setDeltaX(float newDeltaX) {
        deltaX = newDeltaX;
    }
    
    public void setDeltaY(float newDeltaY) {
        deltaY = newDeltaY;
    }
    
    public void multiplyDeltaX(double multiplier) {
        deltaX *= multiplier;
    }
    
    public void multiplyDeltaY(double multiplier) {
        deltaY *= multiplier;
    }
    
    /**
     * Inverts deltaX.
     */
    public void invertDeltaX() {
        deltaX = -deltaX;
    }

    /**
     * Inverts deltaY.
     */
    public void invertDeltaY() {
        deltaY = -deltaY;
    }
}
