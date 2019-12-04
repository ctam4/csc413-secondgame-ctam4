package tankgame.gameobject;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.lang.Math;

import tankgame.Launcher;

public abstract class GameObject {
    protected double scale;
    protected Launcher app;
    protected BufferedImage image;
    protected int width, height, x, y;

    protected GameObject(Launcher app, double scale, BufferedImage image, int x, int y) {
        this.app = app;
        this.scale = scale;
        this.image = image;
        this.width = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * this.scale);
        this.height = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * this.scale);
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public abstract boolean onCollision(GameObject gameObject);

    public void draw(Graphics g) {
        g.drawImage(this.image, this.x, this.y, this.width, this.height, null);
    }
}
