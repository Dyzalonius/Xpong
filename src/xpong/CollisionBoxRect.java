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
        if (gameObject.getCollisionBox().getCollisionType() == 2) {
            // update lastCollision
            gameObject.getCollisionBox().setLastCollision(this);
        
            System.out.println(gameObject + " COLLIDES WITH " + this + "\t Side " + collisionSide);
            // check if the object is a paddle
            if (collisionType == 1) {
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
            } else {
                switch (collisionSide) {
                    case 0:
                        gameObject.invertDeltaX();
                        break;
                    case 1:
                        gameObject.invertDeltaY();
                        break;
                    case 2:
                        gameObject.invertDeltaX();
                        break;
                    case 3:
                        gameObject.invertDeltaY();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public int checkCollision(GameObject gameObject) {
        CollisionBoxRect collisionBoxRect = (CollisionBoxRect) gameObject.getCollisionBox(); // cant do this if theres ellipses too

        // if collision boxes collide, then return collisionSide
        if (!(posY + height/2 < collisionBoxRect.getPosY() - collisionBoxRect.getHeight()/2
                || posY - height/2 > collisionBoxRect.getPosY() + collisionBoxRect.getHeight()/2
                || posX + width/2 < collisionBoxRect.getPosX() - collisionBoxRect.getWidth()/2
                || posX - width/2 > collisionBoxRect.getPosX() + collisionBoxRect.getWidth()/2)) {
            System.out.println("Collision!");
            // make sure gameObject hasnt already been collided with
            if (this != gameObject.getCollisionBox().getLastCollision()) {
                return checkCollisionSide(gameObject);
            }
        }
        return -1;
    }

    public int checkCollisionSide(GameObject gameObject) { //TODO: fix, broken as titties. If you hit something in a shallow angle, it collides wrong
        // check if top/bot
        if (Math.abs(gameObject.getDeltaY()) > Math.abs(gameObject.getDeltaX())) {
            // check if bot
            if (gameObject.getDeltaY() >= 0) {
                return 1; // bot
            }
            return 3; // top
        } else {
            // check if right
            if (gameObject.getDeltaX() >= 0) {
                return 0; // right
            }
            return 2; // left
        }
    }
}
