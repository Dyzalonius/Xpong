package xpong;

/**
 *
 * @author Ties van Kipshagen
 */
public abstract class CollisionBox {

    float posX, posY;
    int collisionType;
    CollisionBox lastCollision;

    public CollisionBox(float posX, float posY, int collisionType) {
        this.posX = posX;
        this.posY = posY;
        this.collisionType = collisionType; // 0=static, 1=paddle, 2=ball
        this.lastCollision = null;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }
    
    public int getCollisionType() {
        return collisionType;
    }
    
    public CollisionBox getLastCollision() {
        return lastCollision;
    }
    
    public void setPosX(float deltaX) {
        posX = deltaX;
    }
    
    public void setPosY(float deltaY) {
        posY = deltaY;
    }
    
    public void setLastCollision(CollisionBox collisionBox) {
        lastCollision = collisionBox;
    }

    public abstract void handleCollision(int collisionSide, GameObject gameObject);
    
    public abstract int checkCollision(GameObject gameObject);
    
    //public abstract int checkCollision(CollisionBoxCircle collisionBoxCircle);
}
