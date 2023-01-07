package onScreen;

import control.GamePanel;
import general.Vector;
import item.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Tricorder extends OnScreen{
    final GamePanel gamePanel;
    final BufferedImage[] img_tricorder;
    boolean img_Counter;
    BufferedImage img_healthPoint;
    BufferedImage img_selectBox;
    final Vector position;
    final Vector healthBarPosition;
    final Vector[] itemPositions;

    public Tricorder(GamePanel gamePanel, Vector position) {
        this.gamePanel = gamePanel;
        this.img_tricorder = new BufferedImage[2];
        this.img_Counter = true;
        this.position = position;
        healthBarPosition = new Vector(position.x() + 22 * gamePanel.scale, position.y()+ 27 * gamePanel.scale);
        itemPositions = new Vector[5];
        itemPositions[0] = new Vector(position.x() + 5 * gamePanel.scale, position.y() + 53 * gamePanel.scale);
        itemPositions[1] = new Vector(position.x() + 11  * gamePanel.scale, position.y() + 59 * gamePanel.scale);
        itemPositions[2] = new Vector(position.x() + 20 * gamePanel.scale, position.y() + 56 * gamePanel.scale);
        itemPositions[3] = new Vector(position.x() + 29 * gamePanel.scale, position.y() + 59 * gamePanel.scale);
        itemPositions[4] = new Vector(position.x() + 35 * gamePanel.scale, position.y() + 53 * gamePanel.scale);
        getImages();
    }

    public void getImages() {
        try {
            img_tricorder[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/Tricorder.png")));
            img_tricorder[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/Tricorder_2.png")));
            img_healthPoint = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/Healthpoint.png")));
            img_selectBox = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/SelectBox.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void draw(Graphics2D g2) {
        // update needed picture (quick heart rate when player is in range of monster)
        boolean playerHasQuickHeartRate = gamePanel.getPlayer().isBeingAttacked() || gamePanel.getPlayer().getHealth() <= 2;
        if(gamePanel.currentFrame() == 30 || playerHasQuickHeartRate && gamePanel.currentFrame() % 15 == 0)
            img_Counter = !img_Counter;
        if(gamePanel.getPlayer().getHealth() <= 0)
            img_Counter = false;
        else if (gamePanel.getPlayer().getHealth() <= 2)
            try {
                img_tricorder[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/Tricorder_Critical.png")));
            } catch (IOException ignored) {
                System.err.println("Error: Corrupted or missing image file");
            }
        // draw tricorder
        BufferedImage current;
        if(img_Counter)
            current = img_tricorder[0];
        else
            current = img_tricorder[1];
        g2.drawImage(current,
                position.x(),
                position.y(),
                3 * gamePanel.tileSize,
                (int) (4.5* gamePanel.tileSize),
                null);
        // draw health
        for (int i = 0; i < gamePanel.getPlayer().getHealth(); i++) {
            g2.drawImage(img_healthPoint,
                    (int) (healthBarPosition.x() + 2 * i * gamePanel.scale),
                    healthBarPosition.y(),
                    (int) (2 * gamePanel.scale),
                    (int) (2 * gamePanel.scale),
                    null);
        }
        // draw select-box for selected item TODO fix
        g2.drawImage(img_selectBox,
                itemPositions[gamePanel.getPlayer().getItemSlot()].x(),
                itemPositions[gamePanel.getPlayer().getItemSlot()].y(),
                (int) (8 * gamePanel.scale),
                (int) (8 * gamePanel.scale),
                null);
        // draw player items
        for (int i = 0; i < 5; i++) {
            try {
                Item currentItem = gamePanel.getPlayer().getItems().get(i);
                if (currentItem != null) {
                    g2.drawImage(currentItem.getGraphic(),
                            itemPositions[i].x(),
                            itemPositions[i].y(),
                            (int) (8 * gamePanel.scale),
                            (int) (8 * gamePanel.scale),
                            null);
                    if (i == gamePanel.getPlayer().getItemSlot())
                        currentItem.draw(g2);
                }
            } catch (Exception ignored) {}
        }
        // draw ship environment states
        int oxygenLevel = gamePanel.getShipStatus().getOxygenLevel();
        int temperature = gamePanel.getShipStatus().getTemperatureLevel();
        g2.setFont(new Font("MONOSPACED", Font.BOLD, 16));
        g2.setColor(new Color(0xFFFFFF));
        g2.drawString("O2:",
                (int) (position.x() + 1.0 * gamePanel.tileSize),
                (int) (position.y() + 2.15 * gamePanel.tileSize));
        g2.drawString("T.:",
                (int) (position.x() + 1.0 * gamePanel.tileSize),
                (int) (position.y() + 2.35 * gamePanel.tileSize));
        if (oxygenLevel < 15)
            g2.setColor(new Color(0xAD1C1C));
        else
            g2.setColor(new Color(0xFFFFFF));
        g2.drawString(getRightedString(oxygenLevel) + " %",
                (int) (position.x() + 1.5 * gamePanel.tileSize),
                (int) (position.y() + 2.15 * gamePanel.tileSize));
        if (temperature < 10)
            g2.setColor(new Color(0xAD1C1C));
        else
            g2.setColor(new Color(0xFFFFFF));
        g2.drawString(getRightedString(temperature) + "°C",
                (int) (position.x() + 1.5 * gamePanel.tileSize),
                (int) (position.y() + 2.35 * gamePanel.tileSize));
    }

    public String getRightedString(int value) {
        StringBuilder result = new StringBuilder("" + value);
        int offset = value <= 0 ? 1 : 1 - (int) Math.log10(value);
        for (int i = offset; i > 0; i--)
            result.insert(0, " ");
        return result.toString();
    }
}
