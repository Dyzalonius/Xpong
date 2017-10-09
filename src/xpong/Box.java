package xpong;

import processing.core.PApplet;

/**
 *
 * @author Ties van Kipshagen
 */
public class Box extends GameObject {

    PApplet applet;
    float posX, posY, width, height;
    CollisionBoxRect collisionBox;

    public Box(float posX, float posY, float width, float height, PApplet applet) {
        this.applet = applet;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.collisionBox = new CollisionBoxRect(this.posX, this.posY, 0, this.width, this.height);
        Applet.gameObjectArray.add(this);
    }
    
    @Override
    public CollisionBoxRect getCollisionBox() {
        return collisionBox;
    }
}
