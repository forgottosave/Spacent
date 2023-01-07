package item;

import control.GamePanel;
import entity.Entity;
import general.Vector;
import onScreen.PopupMessage;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Item {
    final GamePanel gamePanel;
    final int coolDown;
    int timer;
    // general info
    final String name;
    int durability;
    final String mgs_durabilityDown;
    BufferedImage img_itembar;
    final Vector position;
    final BufferedImage[] img_item;
    BufferedImage img_broken;

    /**
     * Holdable and usable item
     * @param gamePanel Reference to the Gamepanel
     * @param name Identifying name
     * @param durability Amount of usages before breaking
     * @param coolDownSeconds Second until reuse
     * @param mgs_durabilityDown Popup message to display when item is broken
     */
    public Item(GamePanel gamePanel, String name, int durability, double coolDownSeconds, String mgs_durabilityDown) {
        this.position = new Vector(gamePanel.screenSize.width - 3 * gamePanel.tileSize - 10,(gamePanel.tileCountVertical - 5) * gamePanel.tileSize);
        this.gamePanel = gamePanel;
        this.coolDown = (int)(coolDownSeconds * 60.);
        timer = 10000;
        this.name = name;
        this.durability = durability;
        this.mgs_durabilityDown = mgs_durabilityDown;
        img_item = new BufferedImage[durability];
        getImage();
    }

    /**
     * Set all images for each durability
     */
    public abstract void getImage();

    public abstract void use2(Entity user);
    public void use(Entity user) {
        if (timer > coolDown) {
            if (durability <= 0) {
                gamePanel.setPopup(new PopupMessage(gamePanel,mgs_durabilityDown, new Color(0x9D2F2F), 1));
            }
            else {
                timer = 0;
                durability--;
                use2(user);
            }
        }
        else if (gamePanel.currentFrame() == 0 && durability > 0) {
            gamePanel.setPopup(new PopupMessage(gamePanel,"Reload time " + (coolDown - timer) / 60 + "sec", Color.WHITE, 1));
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = durability <= 0 ? img_broken : img_item[durability - 1];
        g2.drawImage(img,
                position.x(),
                position.y(),
                3 * gamePanel.tileSize,
                (int) (4.5* gamePanel.tileSize),
                null);
    }

    public void update() {
        timer++;
    }

    public BufferedImage getGraphic() {
        return img_itembar;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name~'" + name + '\'' +
                "*durability~" + durability +
                '}';
    }
}
