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

    public void onCollision(int collisionSide, GameObject gameObject) { //NOTE: the collisionSide seems to be the side of the gameObject, not this

        // check if its a ball thats bouncing against the object
        switch (gameObject.getCollisionBox().getCollisionType()) {
            case 2:
                // update lastCollision
                gameObject.getCollisionBox().setLastCollision(this.getCollisionBox());

                System.out.println(collisionSide);
                
                // handle collision based on collisionType
                switch (this.getCollisionBox().getCollisionType()) {
                    // the object is a paddle
                    case 1:
                        double oldSpeed = Math.sqrt(gameObject.getDeltaX() * gameObject.getDeltaX() + gameObject.getDeltaY() * gameObject.getDeltaY());
                        float newDeltaY = 0;
                        float newDeltaX = 0;

                        // check what paddle we are dealing with. TODO: replace with a proper way of checking this.
                        if (gameObject.getDeltaX() < 0) {
                            //check where on the paddle it hits, and change direction accordingly
                            newDeltaY = (float) ((gameObject.getCollisionBox().getPosY() - (Applet.paddleArray.get(0).getPosY() + Applet.paddleArray.get(0).getPaddleHalfHeight())) * (oldSpeed / 100));
                            newDeltaX = (float) Math.sqrt(oldSpeed * oldSpeed - newDeltaY * newDeltaY);
                            gameObject.setDeltaX(Math.abs(newDeltaX));
                        } else {
                            //check where on the paddle it hits, and change direction accordingly
                            newDeltaY = (float) ((gameObject.getCollisionBox().getPosY() - (Applet.paddleArray.get(1).getPosY() + Applet.paddleArray.get(1).getPaddleHalfHeight())) * (oldSpeed / 100));
                            newDeltaX = (float) Math.sqrt(oldSpeed * oldSpeed - newDeltaY * newDeltaY);
                            gameObject.setDeltaX(-Math.abs(newDeltaX));
                        }
                        gameObject.setDeltaY(newDeltaY);

                        // make the ball go faster
                        gameObject.multiplyDeltaX(1.05);
                        gameObject.multiplyDeltaY(1.05);
                        break;
                    default:
                        switch (collisionSide) {
                            case 0:
                                gameObject.invertDeltaY();
                                break;
                            case 1:
                                gameObject.invertDeltaX();
                                break;
                            case 2:
                                gameObject.invertDeltaY();
                                break;
                            case 3:
                                gameObject.invertDeltaX();
                                break;
                            default:
                                break;
                        }
                        break;
                }
                break;
            default:
                break;
        }
    }
}
