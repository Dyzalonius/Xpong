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
    public int checkCollision(GameObject gameObject) {
        CollisionBoxRect collisionBoxRect = (CollisionBoxRect) gameObject.getCollisionBox(); // cant do this if theres ellipses too

        // if collision boxes collide, then return collisionSide
        if (!(posY + height / 2 < collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2
                || posY - height / 2 > collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2
                || posX + width / 2 < collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2
                || posX - width / 2 > collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2)) {
            // make sure gameObject hasnt already been collided with
            if (gameObject.getCollisionBox().getLastCollision() != this) {
                return checkCollisionSide(gameObject);
            } else {
                // for debugging
                System.out.println("ALREADY COLLIDED");
            }
        }
        return -1;
    }

    public int checkCollisionSide(GameObject gameObject) { //TODO: fix. pls.
        CollisionBoxRect collisionBoxRect = (CollisionBoxRect) gameObject.getCollisionBox(); // cant do this if theres ellipses too

        int res = -1;
        float overlap = 0;
        float newOverlap;

        // collision top
        if (posY - height / 2 < collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2) {
            overlap = (collisionBoxRect.getPosY() + collisionBoxRect.getHeight() / 2) - (posY - height / 2);

            // for debugging
            if (gameObject instanceof Ball) {
                System.out.println("\ntop:\t" + overlap);
            }

            res = 0;
        }
        // collision right
        if (posX + width / 2 > collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2) {
            newOverlap = (posX + width / 2) - (collisionBoxRect.getPosX() - collisionBoxRect.getWidth() / 2);

            // for debugging
            if (gameObject instanceof Ball) {
                System.out.println("right:\t" + newOverlap);
            }

            if (newOverlap < overlap) {
                res = 1;
                overlap = newOverlap;
            }
        }
        // collision bot
        if (posY + height / 2 > collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2) {
            newOverlap = (posY + height / 2) - (collisionBoxRect.getPosY() - collisionBoxRect.getHeight() / 2);

            // for debugging
            if (gameObject instanceof Ball) {
                System.out.println("bot:\t" + newOverlap);
            }
            if (newOverlap < overlap) {
                res = 2;
                overlap = newOverlap;
            }
        }
        // collision left
        if (posX - width / 2 < collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2) {
            newOverlap = (collisionBoxRect.getPosX() + collisionBoxRect.getWidth() / 2) - (posX - width / 2);

            // for debugging
            if (gameObject instanceof Ball) {
                System.out.println("left:\t" + newOverlap);
            }

            if (newOverlap < overlap) {
                res = 3;
            }
        }

        return res;
    }
}
