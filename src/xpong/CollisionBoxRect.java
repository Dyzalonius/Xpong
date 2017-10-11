package xpong;

/**
 *
 * @author Ties van Kipshagen
 */
public class CollisionBoxRect extends CollisionBox {

    float width, height;

    public CollisionBoxRect(float posX, float posY, int collisionType, float width, float height) {
        super(posX, posY, collisionType);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void handleCollision(int collisionSide, GameObject gameObject) { //NOTE: the collisionSide seems to be the side of the gameObject, not this

        // check if its a ball thats bouncing against the object
        switch (gameObject.getCollisionBox().getCollisionType()) {
            case 2:
                // update lastCollision
                gameObject.getCollisionBox().setLastCollision(this);

                System.out.println(collisionSide);
                // handle collision based on collisionType
                switch (collisionType) {
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

    @Override
    public int checkCollision(GameObject gameObject) {
        CollisionBoxRect collisionBoxRect = (CollisionBoxRect) gameObject.getCollisionBox(); // cant do this if theres ellipses too

        // if collision boxes collide, then return collisionSide
        if (!(posY + height / 2 < collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2
                || posY - height / 2 > collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2
                || posX + width / 2 < collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2
                || posX - width / 2 > collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2)) {
            // make sure gameObject hasnt already been collided with
            if (this != gameObject.getCollisionBox().getLastCollision()) {
                return checkCollisionSide(gameObject);
            }
        }
        return -1;
    }

    public int checkCollisionSide(GameObject gameObject) { //TODO: fix. pls.
        CollisionBoxRect collisionBoxRect = (CollisionBoxRect) gameObject.getCollisionBox(); // cant do this if theres ellipses too
        
        int res = 0;
        float overlap = 0;
        float newOverlap;
        
        // collision bot
        if (posY + height / 2 > collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2) {
            res = 2;
            overlap = posY + height / 2 - collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2;
        }
        // collision top
        if (posY - height / 2 < collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2) {
            newOverlap = collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2 - posY - height / 2;
            
            if (newOverlap < overlap) {
                res = 0;
                overlap = newOverlap;
            }
        }
        // collision right
        if (posX + width / 2 > collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2) {
            newOverlap = posX + width / 2 - collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2;
            
            if (newOverlap < overlap) {
                res = 1;
                overlap = newOverlap;
            }
        }
        // collision left
        if (posX - width / 2 < collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2) {
            newOverlap = collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2 - posX - width / 2;
            
            if (newOverlap < overlap) {
                res = 3;
            }
        }
        
        return res;
    }
}
