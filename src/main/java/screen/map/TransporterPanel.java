package screen.map;

import control.GamePanel;
import control.KeyHandler;
import general.Vector;
import onScreen.PopupMessage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class TransporterPanel {
    // general properties
    final GamePanel gamePanel;
    final KeyHandler keyHandler;
    int frameCount = 0;
    int actionCount = 100;
    final int mapCount = 7;
    // background state
    int currentButton;
    boolean showManual;
    // images
    BufferedImage background;
    BufferedImage backgroundRepaired;
    BufferedImage manual;
    BufferedImage manualTricorderInfo;
    BufferedImage manualTricorderInfo2;
    BufferedImage map;
    BufferedImage cursor;
    BufferedImage cursor_err;
    final Vector[] buttonPositions;


    public TransporterPanel(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.currentButton = 0;
        this.showManual = false;
        this.buttonPositions = new Vector[mapCount + 1];
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Transporter-Panel.png")));
            backgroundRepaired = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Transporter-Panel-Repaired.png")));
            manual = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Manual.png")));
            manualTricorderInfo = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Manual-Tricorder-Info.png")));
            manualTricorderInfo2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Manual-Tricorder-Info2.png")));
            map = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Transporter/Spaceship_Map.png")));
            cursor = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Cursor.png")));
            cursor_err = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Cursor_Error.png")));
            buttonPositions[0] = new Vector(gamePanel.screenSize.width * .139,gamePanel.screenSize.height * .78);
            buttonPositions[1] = new Vector(gamePanel.screenSize.width * .326,gamePanel.screenSize.height * .642);
            buttonPositions[2] = new Vector(gamePanel.screenSize.width * .426,gamePanel.screenSize.height * .621);
            buttonPositions[3] = new Vector(gamePanel.screenSize.width * .341,gamePanel.screenSize.height * .571);
            buttonPositions[4] = new Vector(gamePanel.screenSize.width * .482,gamePanel.screenSize.height * .511);
            buttonPositions[5] = new Vector(gamePanel.screenSize.width * .471,gamePanel.screenSize.height * .452);
            buttonPositions[6] = new Vector(gamePanel.screenSize.width * .72,gamePanel.screenSize.height * .441);
            buttonPositions[7] = new Vector(gamePanel.screenSize.width * .62,gamePanel.screenSize.height * .399);
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void update() {
        frameCount = (int) ((frameCount + 1) % gamePanel.FPS);
        actionCount++;
        // update key action
        if (actionCount > gamePanel.FPS / 3 && (keyHandler.left || keyHandler.right || keyHandler.action || keyHandler.escaping)) {
            if (keyHandler.left) {
                if (!showManual)
                    moveCursor(-1);
            }
            else if (keyHandler.right) {
                if (!showManual)
                    moveCursor(1);
            }
            else if (keyHandler.action) {
                if (currentButton == 0 || showManual) {
                    showManual = !showManual;
                }
                else if (gamePanel.getShipStatus().getWorldsUnlocked() >= currentButton -1){
                    gamePanel.setCurrentWorld(currentButton - 1);
                    gamePanel.getPlayer().setPosition(gamePanel.getWorld().getTransporterPos());
                    gamePanel.setCurrentScreen(GamePanel.SCREEN_WORLD);
                }
                else {
                    gamePanel.setPopup(new PopupMessage(gamePanel,
                            "Destination not safe yet", new Color(0x9D2F2F), 1.2));
                    gamePanel.log("Entering world" + (currentButton - 1) + " failed: not unlocked yet");
                }
            }
            else if (showManual) {
                showManual = false;
            }
            else {
                gamePanel.setCurrentScreen(GamePanel.SCREEN_MENU);
            }
            actionCount = 0;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.PLAIN, 20));
        // get general info
        int shields = gamePanel.getShipStatus().getShields();
        int lifeSupp = gamePanel.getShipStatus().getLifeSupportSystems();
        // draw background
        if (lifeSupp < 35) {
            g2.drawImage(background,
                    0,
                    0,
                    gamePanel.screenSize.width,
                    gamePanel.screenSize.height,
                    null);
        }
        else {
            g2.drawImage(backgroundRepaired,
                    0,
                    0,
                    gamePanel.screenSize.width,
                    gamePanel.screenSize.height,
                    null);
        }
        if (showManual) {
            g2.drawImage(manual,
                    gamePanel.screenSize.width / 4,
                    gamePanel.screenSize.height / 8,
                    (int) (3 * gamePanel.screenSize.width / 5.),
                    (int) (manual.getHeight() * ((3 * gamePanel.screenSize.width / 5.) / manual.getWidth())),
                    null);
            g2.drawImage(manualTricorderInfo,
                    (int) (gamePanel.screenSize.width * 0.11),
                    gamePanel.screenSize.height / 5,
                    (int) (3 * gamePanel.screenSize.width / 25.),
                    (int) (manual.getHeight() * ((3 * gamePanel.screenSize.width / 5.) / manual.getWidth())),
                    null);
            g2.drawImage(manualTricorderInfo2,
                    (int) (gamePanel.screenSize.width * 0.79),
                    gamePanel.screenSize.height / 5,
                    (int) (3 * gamePanel.screenSize.width / 25.),
                    (int) (manual.getHeight() * ((3 * gamePanel.screenSize.width / 5.) / manual.getWidth())),
                    null);
            // Draw Foreground
            g2.drawImage(cursor,
                    buttonPositions[currentButton].x(),
                    buttonPositions[currentButton].y(),
                    (int) (gamePanel.screenSize.width * 0.021),
                    (int) (gamePanel.screenSize.width * 0.021),
                    null);
            // Draw Info Message
            String underline2 = "Back: 'space' & 'escape'";
            g2.drawString(underline2,
                    (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.0025 * underline2.length()),
                    gamePanel.screenSize.height - 20);
        }
        else {
            g2.drawImage(map,
                    gamePanel.screenSize.width / 5,
                    gamePanel.screenSize.height / 8,
                    (int) (3 * gamePanel.screenSize.width / 5.),
                    (int) (map.getHeight() * ((3 * gamePanel.screenSize.width / 5.) / map.getWidth())),
                    null);
            // Draw Foreground
            g2.drawImage(currentButton - 1 > gamePanel.getShipStatus().getWorldsUnlocked() ? cursor_err : cursor,
                    buttonPositions[currentButton].x(),
                    buttonPositions[currentButton].y(),
                    (int) (gamePanel.screenSize.width * 0.021),
                    (int) (gamePanel.screenSize.width * 0.021),
                    null);
            // Draw Info Message
            String underline = "Navigate: 'a' & 'd'  Select: 'space'  Menu: 'esc'";
            g2.drawString(underline,
                    (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.0025 * underline.length()),
                    gamePanel.screenSize.height - 20);
        }
        // Draw Ship Info
        g2.setFont(new Font("MONOSPACED", Font.BOLD, 45));
        if (shields <= 40)
            g2.setColor(new Color(0xD31E1E));
        else
            g2.setColor(new Color(0x2C9310));
        g2.drawString(getRightedString(shields) + "%",
                (int) (gamePanel.screenSize.width * .89),
                (int) (gamePanel.screenSize.height * .858));
        if (lifeSupp <= 40)
            g2.setColor(new Color(0xD31E1E));
        else
            g2.setColor(new Color(0x2C9310));
        g2.drawString(getRightedString(lifeSupp) + "%",
                (int) (gamePanel.screenSize.width * .89),
                (int) (gamePanel.screenSize.height * .903));
    }

    public String getRightedString(int value) {
        StringBuilder result = new StringBuilder("" + value);
        int offset = value <= 0 ? 2 : 2 - (int) Math.log10(value);
        for (int i = offset; i > 0; i--)
            result.insert(0, " ");
        return result.toString();
    }

    public void moveCursor(int value) {
        currentButton = (currentButton + value) % (mapCount + 1);
        if (currentButton < 0)
            currentButton = mapCount;
    }
}
