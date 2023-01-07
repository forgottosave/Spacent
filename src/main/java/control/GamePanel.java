package control;

import screen.gameManagement.GameManagement;
import screen.map.TransporterPanel;
import onScreen.OnScreen;
import onScreen.PopupMessage;
import onScreen.Tricorder;
import screen.end.EndScreen;
import entity.*;
import general.Vector;
import general.Direction;
import screen.menu.MainMenu;
import screen.world.World;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * This class represents the game panel for this game.
 * It stores general information like display size, scale, all the worlds, ...
 * @since 26.03.22
 * @author Timon
 */
public class GamePanel extends JPanel implements Runnable {
    // Screen properties
    public final int originalTileSize = 16;
    public double scale = 9; //screenWidth / tilesOnScreen.width;
    public int tileSize = (int) (originalTileSize * scale);
    public final int tileCountHorizontal = 19;
    public final int tileCountVertical = 11;
    final public Dimension screenSize = new Dimension(tileCountHorizontal*tileSize,tileCountVertical*tileSize);
    // Screen attributes
    final KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    int currentScreen; // 0=Menu, 1=TransporterPanel, 2=World, 3=EndScene, 4=GameManagement
    // Screens
    MainMenu mainMenu;
    GameManagement gameManagement;
    TransporterPanel transporterPanel;
    public static final int SCREEN_MENU = 0;
    public static final int SCREEN_GAME_MANAGEMENT = 1;
    public static final int SCREEN_TRANSPORTER = 2;
    public static final int SCREEN_END = 3;
    public static final int SCREEN_WORLD = 4;
    // World
    int currentWorld = 0;
    World[] world;
    Player player;
    ShipStatus shipStatus;
    // On-Screens
    ArrayList<OnScreen> onScreens;
    Shader shader;
    Tricorder tricorder;
    EndScreen endScreen;
    PopupMessage popup;
    // Game properties
    public final double FPS = 60;
    int frameCount = 0;

    public GamePanel() {
        // JPanel settings
        this.setPreferredSize(screenSize);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        newGame();
    }

    /**
     * Starts a new game
     */
    public void newGame() {
        // general settings
        currentScreen = SCREEN_MENU;
        currentWorld = 0;
        // Screens
        mainMenu = new MainMenu(this, keyHandler);
        gameManagement = new GameManagement(this, keyHandler);
        transporterPanel = new TransporterPanel(this,keyHandler);
        // Player generation
        player = new Player(this, keyHandler, new Vector(14*tileSize, 18*tileSize));
        // World generation
        shipStatus = new ShipStatus(this);
        world = new World[7];
        world[0] = new World(this,"/Worlds/World1"); // storage room
        world[1] = new World(this,"/Worlds/World2"); // engine room
        world[2] = new World(this,"/Worlds/World3"); // hangar
        world[3] = new World(this,"/Worlds/World4"); // bar
        world[4] = new World(this,"/Worlds/World5"); // crew cabins
        world[5] = new World(this,"/Worlds/World6"); // front deck
        world[6] = new World(this,"/Worlds/World7"); // bridge
        // on-screen stuff
        onScreens = new ArrayList<>();
        shader = new Shader(this);
        tricorder = new Tricorder(this, new Vector(10,(tileCountVertical - 5) * tileSize));
        onScreens.add(tricorder);
        popup = new PopupMessage(this, "",Color.WHITE,0);
        endScreen = null;
    }

    //// Graphic related methods
    /**
     * Calculated drawing offset for centered drawing
     * @param value raw value
     * @return offset value
     */
    public int getDrawingOffset(int value) {
        return value - (tileSize / 2);
    }

    //// Player related methods
    /**
     * Get screen related coordinates
     * @param x raw x value
     * @param y raw y value
     * @return Vector of relative coordinate
     */
    public Vector getPlayerDependantCoordinate(int x, int y) {
        return new Vector(
                x - (player.getPosition().x() - screenSize.width / 2),
                y - (player.getPosition().y() - screenSize.height / 2) + (tileSize - 5));
    }
    /**
     * Get screen related coordinates
     * @param c Vector
     * @return Vector of relative coordinate
     */
    public Vector getPlayerDependantCoordinate(Vector c) {
        return getPlayerDependantCoordinate(c.x(), c.y());
    }

    //// World logic related methods
    /**
     * Checks whether an entity is allowed to move into a given direction.
     * @param e Entity
     * @param d Direction
     * @return true -> is allowed; false -> isn't allowed
     */
    public boolean allowedToEnterArea(Entity e, Direction d) {
        return world[currentWorld].checkEntryPermission(e, d);
    }
    /**
     * @return Current frame in current second. Range between 0 and (FPS - 1).
     */
    public int currentFrame() {
        return frameCount;
    }

    //// Game state methods
    /**
     * Saves game state to a file
     */
    public void saveGame(String filename) {
        log("Saving game...");
        String path = System.getProperty("user.home") + "/.Spacent/" + filename;
        try {
            // check for existing file create if needed
            File file = new File(path);
            if (file.getParentFile().mkdirs())
                log("Folder .Spacent/ created in home directory");
            if (file.createNewFile())
                log("Creating new game state...");
            else
                log("Overwriting old game state...");
            // write to file...
            FileWriter writer = new FileWriter(path);
            writer.write("SHP::" + shipStatus + "\n");
            writer.write("PLR::" + player.toString() + "\n");
            for (World w : world)
                writer.write("WLD::" + w + "\n");
            // finish
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to save game to " + path);
        }
    }
    /**
     * Loads game state from a file
     */
    public void loadGame(String filename) {
        log("Loading game...");
        String path = System.getProperty("user.home") + "/.Spacent/" + filename;
        try {
            InputStream is = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            if ((line = br.readLine()).startsWith("SHP::"))
                this.shipStatus = ShipStatus.toShipStatus(this, line.substring(5));
            else
                throw new RuntimeException("Wrong world format");
            if ((line = br.readLine()).startsWith("PLR::"))
                this.player = (Player) Entity.toEntity(this, line.substring(5));
            else
                throw new RuntimeException("Wrong world format");
            for (int i = 0; i < world.length; i++) {
                if ((line = br.readLine()).startsWith("WLD::"))
                    world[i] = World.toWorld(this, line.substring(5));
                else
                    throw new RuntimeException("Wrong world format");
            }
            // fix player in worlds
            for (World w : world) {
                w.getEntities().removeIf(e -> e.getAbilities().contains(Ability.PLAYER));
                w.spawnEntity(player);
            }
        } catch (Exception e) {
            System.err.println("Failed to load game from " + path);
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    /**
     * Starts game thread and initializes run()
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double interval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        // Game Loop
        while (gameThread != null) {
            // memorize starting time of frame
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            // update frame
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                frameCount++;
            }
            // show fps
            if (timer >= 1000000000) {
                printInfo();
                frameCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * Prints debugging info:
     * - FPS
     * - Player status
     * - Ship status
     */
    public void printInfo() {
        log("FPS="+ frameCount
                + "  Play-Time=" + shipStatus.getTimePlayedString()
                + "  Player=" + player
                + "  Ship=" + shipStatus
                + "  CurrentWorld=" + world[currentWorld]
        );
    }

    /**
     * Adds entry to log
     * @param logEntry entry to add to log
     */
    public void log(String logEntry) {
        System.out.println(java.time.Clock.systemUTC().instant().toString().substring(0,19) + "  " +
                logEntry);
    }

    /**
     * Updates all components in the game
     */
    public void update() {
        switch (currentScreen) {
            case SCREEN_MENU -> mainMenu.update();
            case SCREEN_GAME_MANAGEMENT -> gameManagement.update();
            case SCREEN_TRANSPORTER -> transporterPanel.update();
            case SCREEN_WORLD -> {world[currentWorld].update();
                shipStatus.update();}
            case SCREEN_END -> {}
            default -> currentScreen = 0;
        }
    }

    /**
     * Repaints the new frame
     * @param g Graphic to draw on
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // set presets
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.PLAIN, 20));
        // draw world
        world[currentWorld].draw(g2);
        shader.draw(g2);
        onScreens.forEach(o -> o.draw(g2));
        // Screens
        switch (currentScreen) {
            case SCREEN_MENU -> mainMenu.draw(g2);
            case SCREEN_GAME_MANAGEMENT -> gameManagement.draw(g2);
            case SCREEN_TRANSPORTER -> transporterPanel.draw(g2);
            case SCREEN_END -> {
                if (endScreen != null)
                    endScreen.draw(g2);
                else
                    currentScreen = 0;
            }
        }
        // Popup
        popup.draw(g2);
        // reset & update new frame
        world[currentWorld].getEntities().forEach(entity -> entity.setBeingAttacked(false));
        g2.dispose();
    }

    //// Fake-Setter
    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
        try {Thread.sleep(500);} catch (Exception ignored) {}
        log("Switch to screen "+currentScreen);
    }
    public void setCurrentWorld(int currentWorld) {
        this.currentWorld = currentWorld;
        log("Switch to world "+currentWorld);
    }
    //// Setter
    public void setPopup(PopupMessage popup) {
        this.popup = popup;
    }
    public void setEndScreen(EndScreen endScreen) {
        this.endScreen = endScreen;
    }
    //// Getter
    public World getWorld() {
        return world[currentWorld];
    }
    public World[] getAllWorlds() {
        return world;
    }
    public ShipStatus getShipStatus() {
        return shipStatus;
    }
    public Player getPlayer() {
        return player;
    }
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    public Shader getShader() {
        return shader;
    }
}
