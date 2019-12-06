package galacticmail.gameobject;

import java.awt.image.BufferedImage;
import java.lang.Math;

import galacticmail.Launcher;

public class Moon extends GameMovableObject {
    private final int R = 1;

    private int pay;
    private int penalty;

    public Moon(Launcher app, double scale, BufferedImage image, int x, int y, int vx, int vy, int angle, int maxX, int maxY, int pay, int penalty) {
        super(app, scale, image, x, y, vx, vy, angle, maxX, maxY);
        this.pay = pay;
        this.penalty = penalty;
    }

    public boolean onCollision(GameObject gameObject) {
        switch (gameObject.getClass().getSimpleName()) {
            case "Asteroid":
                return false;
            case "Moon":
                return true;
            case "Rocket":
                if (((Rocket) gameObject).getIsLanded()) {
                    // landed (idle)
                    if (!((Rocket) gameObject).getIsFlying()) {
                        return false;
                    }
                    // landed (ignited)
                    else {
                        return true;
                    }
                }
                // flying
                else if (((Rocket) gameObject).getIsFlying()) {
                    return false;
                }
        }
        return false;
    }

    public void update() {
        // go straight
        this.vx = (int) Math.round(this.R * this.app.getScale() * this.scale * Math.cos(Math.toRadians(this.angle)));
        this.vy = (int) Math.round(this.R * this.app.getScale() * this.scale * Math.sin(Math.toRadians(this.angle)));
        this.x += this.vx;
        this.y += this.vy;
        // if the new position is out of current panel
        if (!validPosition(this.x, this.y)) {
            if (this.x < 0) {
                this.x += this.maxX;
            }
            else if (this.x > this.maxX) {
                this.x -= this.maxX;
            }
            if (this.y < 0) {
                this.y += this.maxY;
            }
            else if (this.y > this.maxY) {
                this.y -= this.maxY;
            }
        }
    }

    public int getPay() {
        return this.pay;
    }

    public int getPenalty() {
        return this.penalty;
    }
}
