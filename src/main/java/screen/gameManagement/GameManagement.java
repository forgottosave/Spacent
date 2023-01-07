package screen.gameManagement;

import control.GamePanel;
import control.KeyHandler;
import general.Vector;
import onScreen.PopupMessage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GameManagement {
    // auto-save filename
    public static final String AUTO_SAVE_FILE = "gamestate_autosave.spnt";
    // general properties
    final GamePanel gamePanel;
    final KeyHandler keyHandler;
    int frameCount = 0;
    int actionCount = 100;
    // background state
    final int buttonCount = 7;
    int currentButton;
    // images
    BufferedImage background;
    BufferedImage headline;
    BufferedImage cursor;
    final Vector[] buttonPositions;
    final String[] filenames;

    public GameManagement(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.buttonPositions = new Vector[buttonCount];
        buttonPositions[0] = new Vector(gamePanel.screenSize.width * .191,gamePanel.screenSize.height * .365);
        buttonPositions[1] = new Vector(gamePanel.screenSize.width * .492,gamePanel.screenSize.height * .365);
        buttonPositions[2] = new Vector(gamePanel.screenSize.width * .782,gamePanel.screenSize.height * .365);
        buttonPositions[3] = new Vector(gamePanel.screenSize.width * .191,gamePanel.screenSize.height * .705);
        buttonPositions[4] = new Vector(gamePanel.screenSize.width * .492,gamePanel.screenSize.height * .705);
        buttonPositions[5] = new Vector(gamePanel.screenSize.width * .782,gamePanel.screenSize.height * .705);
        buttonPositions[6] = new Vector(gamePanel.screenSize.width * .055,gamePanel.screenSize.height * .897);
        this.filenames = new String[buttonCount - 1];
        filenames[0] = "gamestate_0.spnt";
        filenames[1] = "gamestate_1.spnt";
        filenames[2] = "gamestate_2.spnt";
        filenames[3] = "gamestate_3.spnt";
        filenames[4] = "gamestate_4.spnt";
        filenames[5] = AUTO_SAVE_FILE;
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/screens/Background_GameManagement.png")));
            headline = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Headline.png")));
            cursor = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Cursor.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void update() {
        frameCount = (int) ((frameCount + 1) % gamePanel.FPS);
        actionCount++;
        // update key action
        if (actionCount > gamePanel.FPS / 3 && (keyHandler.left || keyHandler.right || keyHandler.action
                || keyHandler.up || keyHandler.down || keyHandler.action2 || keyHandler.action3 || keyHandler.escaping)) {
            if (keyHandler.up || keyHandler.left)
                moveCursor(-1);
            else if (keyHandler.down || keyHandler.right)
                moveCursor(1);
            else if (keyHandler.action && currentButton != buttonCount - 1) {
                gamePanel.setPopup(new PopupMessage(gamePanel,"Loading game " + (currentButton + 1) + "...", Color.WHITE, 1.1));
                gamePanel.repaint();
                gamePanel.loadGame(filenames[currentButton]);
            }
            else if (keyHandler.action2 && currentButton != buttonCount - 1) {
                if (currentButton == buttonCount - 2) {
                    gamePanel.setPopup(new PopupMessage(gamePanel,"Can't write to autosave", new Color(0x9D2F2F), 1.8));
                }
                else {
                    gamePanel.setPopup(new PopupMessage(gamePanel, "Saving game " + (currentButton + 1) + "...", Color.WHITE, 1.8));
                    gamePanel.repaint();
                    gamePanel.saveGame(filenames[currentButton]);
                }
            }
            else if (keyHandler.action3) {
                gamePanel.setPopup(new PopupMessage(gamePanel,"Starting new game...", Color.WHITE, 1.8));
                gamePanel.repaint();
                gamePanel.newGame();
            }
            else {
                gamePanel.setCurrentScreen(GamePanel.SCREEN_MENU);
            }
            actionCount = 0;
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(background,
                0,
                0,
                gamePanel.screenSize.width,
                gamePanel.screenSize.height,
                null);
        // Draw Foreground
        g2.drawImage(cursor,
                buttonPositions[currentButton].x(),
                buttonPositions[currentButton].y(),
                (int) (gamePanel.tileSize * 0.5),
                (int) (gamePanel.tileSize * 0.5),
                null);
        g2.drawImage(headline,
                (int) (gamePanel.screenSize.width * 0.26),
                (int) (- gamePanel.screenSize.width * 0.15),
                (int) (gamePanel.screenSize.width * 0.5),
                (int) (gamePanel.screenSize.width * 0.5),
                null);
        // Draw Info Message
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.PLAIN, 20));
        String underline = "Navigate: 'a' & 'd'   Save Game: 'shift'   Load Game: 'space'   New Game: 'ctrl'   Menu: 'esc'";
        g2.drawString(underline,
                (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.0025 * underline.length()),
                gamePanel.screenSize.height - 20);
    }

    public void moveCursor(int value) {
        currentButton = (currentButton + value) % (buttonCount);
        if (currentButton < 0)
            currentButton = buttonCount - 1;
    }
}
