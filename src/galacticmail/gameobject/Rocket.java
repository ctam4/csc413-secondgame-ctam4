package galacticmail.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.Math;

import galacticmail.Launcher;

public class Rocket extends GameMovableObject {
    private boolean isFlying;

    private final int R = 10;
    private final int ROTATESPEED = 10;

    private int health;
    private int pay;
    private boolean left, right, ignite;

    public Rocket(Launcher app, double scale, BufferedImage image, int x, int y, int vx, int vy, int angle, int maxX, int maxY, boolean isFlying, int health, int pay) {
        super(app, scale, image, x, y, vx, vy, angle, maxX, maxY);
        this.isFlying = this.ignite = isFlying;
        this.health = health;
        this.pay = pay;
        this.left = this.right = false;
    }

    public boolean onCollision(GameObject gameObject) {
        switch (gameObject.getClass().getSimpleName()) {
            case "Asteroid":
                // flying
                if (this.isFlying) {
                    return !takeDamage(((Asteroid) gameObject).getDamage());
                }
                // landed
                else {
                    return false;
                }
            case "Moon":
                // flying
                if (this.isFlying) {
                    if (this.ignite) {
                        toggleIgnite(false);
                        land(gameObject);
                    }
                }
                // landed
                else if (!this.ignite){
                    takePenalty(((Moon) gameObject).getPenalty());
                    moveAlong(gameObject);
                }
                return false;
        }
        return false;
    }

    public boolean getIsFlying() {
        return this.isFlying;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPay() {
        return this.pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public void update() {
        if (this.left) {
            turnLeft();
            toggleLeft(false);
        }
        if (this.right) {
            turnRight();
            toggleRight(false);
        }
        if (this.ignite) {
            if (!this.isFlying) {
                this.app.getGameworld().addFlyingRocket();
            } else {
                launch();
            }
        }
    }

    private void turnLeft() {
        this.angle -= this.ROTATESPEED;
        if (this.angle < 0) {
            this.angle += 360;
        } else if (this.angle >= 360) {
            this.angle -= 360;
        }
    }

    private void turnRight() {
        this.angle += this.ROTATESPEED;
        if (this.angle < 0) {
            this.angle += 360;
        } else if (this.angle >= 360) {
            this.angle -= 360;
        }
    }

    private void launch() {
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

    private void land(GameObject gameObject) {
        setPay(getPay() + ((Moon) gameObject).getPay());
        this.app.getGameworld().addLandedRocket(gameObject);
    }

    private void moveAlong(GameObject gameObject) {
        GameMovableObject gameMovableObject = (GameMovableObject) gameObject;
        this.x = gameMovableObject.getX();
        this.y = gameMovableObject.getY();
        this.vx = gameMovableObject.getVx();
        this.vy = gameMovableObject.getVy();
    }

    public void toggleLeft(boolean value) {
        System.out.println(this.getClass().getSimpleName() + " - toggleLeft()");
        this.left = value;
    }

    public void toggleRight(boolean value) {
        System.out.println(this.getClass().getSimpleName() + " - toggleRight()");
        this.right = value;
    }

    public void toggleIgnite(boolean value) {
        System.out.println(this.getClass().getSimpleName() + " - toggleIgnite()");
        this.ignite = value;
    }

    public boolean takeDamage(int damage) {
        System.out.println(this.getClass().getSimpleName() + " - takeDamage()");
        if (this.health - damage > 0) {
            this.health -= damage;
            return true;
        }
        this.health = 0;
        return false;
    }

    public void takePenalty(int penalty) {
        System.out.println(this.getClass().getSimpleName() + " - takePenalty()");
        if (this.pay - penalty > 0) {
            this.pay -= penalty;
        }
        else {
            this.pay = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        // draw health bar
        Graphics2D g2d = (Graphics2D) g;
        // left
        Rectangle rect_1 = new Rectangle(this.x, (int) Math.round(this.y - 10 * this.app.getScale() * this.scale), (int) Math.round(this.width * ((this.health / 100.0 > 0 && this.health % 100.0 == 0) ? 1.0 : (this.health % 100.0 / 100))), (int) Math.round(this.height * 0.1));
        if (rect_1.getWidth() > 0) {
            g2d.setColor(Color.GREEN);
            g2d.fillRect((int) Math.round(rect_1.getX()), (int) Math.round(rect_1.getY()), (int) Math.round(rect_1.getWidth()), (int) Math.round(rect_1.getHeight()));
        }
        // right
        Rectangle rect_2 = new Rectangle((int) Math.round(this.x + rect_1.getWidth()), (int) Math.round(rect_1.getY()), (int) Math.round(this.width - rect_1.getWidth()), (int) Math.round(rect_1.getHeight()));
        if (rect_2.getWidth() > 0) {
            g2d.setColor(Color.RED);
            g2d.fillRect((int) Math.round(rect_2.getX()), (int) Math.round(rect_2.getY()), (int) Math.round(rect_2.getWidth()), (int) Math.round(rect_2.getHeight()));
        }
    }
}