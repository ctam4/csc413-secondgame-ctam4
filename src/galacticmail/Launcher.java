package galacticmail;

import java.util.HashMap;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import javax.imageio.ImageIO;

public class Launcher {
    private JContainer splash, gameworld;
    private int level;

    private HashMap<String, BufferedImage> resources;
    private HashMap<String, String> strings;

    private HashMap<String, Integer> scoreboard;

    private final int unitSize = 40;
    private int scale;

    public Launcher() {
        // initialize resource & strings
        resources = new HashMap<>();
        strings = new HashMap<>();
        // import resources using path
        putResource("icon", "/resources/galactic.ico");
        // import strings
        putString("name", "Galactic Mail");
        // get current screen size
        DisplayMode device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        System.out.println(this.getClass().getSimpleName() + " - Launcher() - Get screen size: " + device.getWidth() + "x" + device.getHeight());
        this.scale = (int) Math.floor(device.getWidth() / 1280.0);
        System.out.println(this.getClass().getSimpleName() + " - Launcher() - Set scale: " + getScale());
        // initialize level
        this.level = 1;
        // initialize scoreboard
        this.scoreboard = new HashMap<>();
        // initialize JContainer object
        this.splash = new Splash(this);
        // ready to splash
        this.splash.show();
    }

    public static void main(String[] args) {
        new Launcher();
    }

    public Splash getSplash() {
        return (Splash) this.splash;
    }

    public Gameworld getGameworld() {
        return (Gameworld) this.gameworld;
    }

    protected BufferedImage getResource(String key) {
        return this.resources.get(key);
    }

    protected void putResource(String key, String value) {
        try {
            this.resources.put(key, ImageIO.read(this.getClass().getResourceAsStream(value)));
        } catch (Exception e) {
            System.out.println(this.getClass().getSimpleName() + " - putResource() - " + e);
        }
    }

    protected String getString(String key) {
        return this.strings.get(key);
    }

    protected void putString(String key, String value) {
        this.strings.put(key, value);
    }

    public HashMap<String, Integer> getScoreboard() {
        return this.scoreboard;
    }

    public void putScoreboard(String name, int pay) {
        this.scoreboard.put(name, pay);
    }

    public int getUnitSize() {
        return this.unitSize;
    }

    public int getScale() {
        return this.scale;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void start() {
        this.splash.hide();
        // initialize JContainer object
        this.gameworld = new Gameworld(this);
        // ready to game
        this.gameworld.show();
    }

    public void restart() {
        this.gameworld.close();
        // initialize JContainer object
        this.gameworld = new Gameworld(this);
        // ready to game
        this.gameworld.show();
    }

    public void reset() {
        this.gameworld.close();
        this.splash.show();
    }

    public void quit() {
        this.splash.close();
        System.exit(0);
    }
}
