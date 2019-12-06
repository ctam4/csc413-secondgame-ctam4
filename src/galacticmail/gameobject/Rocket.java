package galacticmail.gameobject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.Math;

import galacticmail.Launcher;

public class Rocket extends GameMovableObject {
    private boolean isLanded, isFlying;

    private final int R = 15;
    private final int ROTATESPEED = 10;

    private int health;
    private int pay;
    private boolean left, right, ignite;

    public Rocket(Launcher app, double scale, BufferedImage image, int x, int y, int vx, int vy, int angle, int maxX, int maxY, boolean isLanded, boolean isFlying, int health, int pay) {
        super(app, scale, image, x, y, vx, vy, angle, maxX, maxY);
        this.isLanded = isLanded;
        this.isFlying = this.ignite = isFlying;
        this.health = health;
        this.pay = pay;
        this.left = this.right = false;
    }

    public boolean onCollision(GameObject gameObject) {
        switch (gameObject.getClass().getSimpleName()) {
            case "Asteroid":
                // landed (idle & ignited)
                if (isLanded) {
                    return false;
                }
                // flying
                else if (this.isFlying) {
                    return !takeDamage(((Asteroid) gameObject).getDamage());
                }
            case "Moon":
                // landed (idle)
                if (this.isLanded && !this.isFlying) {
                    takePenalty(((Moon) gameObject).getPenalty());
                    moveAlong(gameObject);
                }
                // flying
                else if (!this.isLanded && this.isFlying) {
                    toggleIgnite(false);
                    land(gameObject);
                }
                return false;
        }
        return false;
    }

    public boolean getIsLanded() {
        return this.isLanded;
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
            if (this.isLanded) {
                // landed (idle)
                if (!this.isFlying) {
                    this.app.getGameworld().addFlyingRocket();
                }
                // landed (ignited)
                else {
                    launch();
                }
            }
            // flying
            else {
                fly();
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

    private void fly() {
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

    private void launch() {
        this.isLanded = false;
        this.vx = 0;
        this.vy = 0;
        do {
            this.vx += (int) Math.round((this.width + 1) * Math.cos(Math.toRadians(this.angle)));
            this.vy += (int) Math.round((this.height + 1) * Math.sin(Math.toRadians(this.angle)));
        } while ((this.vx != 0 && Math.abs(this.vx) < this.width + 1) || (this.vy != 0 && Math.abs(this.vy) < this.height + 1));
        if (this.vx == 0) {
            if (this.angle == 0 || this.angle == 270) {
                this.vy -= this.height + 1;
            } else {
                this.vy += this.height + 1;
            }
        } else if (this.vy == 0) {
            if (this.angle == 90 || this.angle == 180) {
                this.vx -= this.width + 1;
            } else {
                this.vx += this.width + 1;
            }
        }
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
