package entity;

import control.GamePanel;
import control.KeyHandler;
import screen.end.PlayerDeadEnd;
import general.Vector;
import general.Direction;
import item.Faser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{
    final KeyHandler keyHandler;
    int itemSlot;

    /**
     * Controllable Player character
     * @param gamePanel Reference to the Gamepanel
     * @param keyHandler Listener for keyboard input
     * @param position Current Entity position
     */
    public Player(GamePanel gamePanel, KeyHandler keyHandler, Vector position) {
        super(gamePanel, 10, 5, 16, 1, 70, position);
        this.keyHandler = keyHandler;
        this.abilities.add(Ability.PLAYER);
        this.abilities.add(Ability.HEALTHY);
        items.add(0, null);
        items.add(1, new Faser(gamePanel));
    }

    @Override
    public void getImage() {
        try {
            img_front[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Front.png")));
            img_front[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Front-Step.png")));
            img_front[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Front-Step2.png")));
            img_back[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Back.png")));
            img_back[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Back-Step.png")));
            img_back[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Back-Step2.png")));
            img_left[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Left.png")));
            img_left[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Left-Step.png")));
            img_left[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Left-Step2.png")));
            img_right[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Right.png")));
            img_right[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Right-Step.png")));
            img_right[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/player/Player_Right-Step2.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    @Override
    public void die() {
        System.out.println("PLAYER DEAD");
        gamePanel.setEndScreen(new PlayerDeadEnd(gamePanel));
        gamePanel.setCurrentScreen(GamePanel.SCREEN_END);
    }

    public void update2() {
        // move character
        if(keyHandler.right && keyHandler.up)
            moveEntity(Direction.UPRI);
        else if(keyHandler.right && keyHandler.down)
            moveEntity(Direction.DORI);
        else if(keyHandler.left && keyHandler.up)
            moveEntity(Direction.UPLE);
        else if(keyHandler.left && keyHandler.down)
            moveEntity(Direction.DOLE);
        else if(keyHandler.up)
            moveEntity(Direction.UP);
        else if(keyHandler.down)
            moveEntity(Direction.DOWN);
        else if(keyHandler.left)
            moveEntity(Direction.LEFT);
        else if(keyHandler.right)
            moveEntity(Direction.RIGHT);
        // character actions
        if(keyHandler.action2) {
            try {
                items.get(itemSlot).use(this);
            } catch (Exception ignored) {} // no item in slot
        }
        if(keyHandler.action) {
            gamePanel.getWorld().interact();
        }
        // update item slot
        itemSlot = keyHandler.itemSlot;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getDirectionalImage();
        g2.drawImage(image,
                gamePanel.screenSize.width / 2 - gamePanel.tileSize / 2,
                gamePanel.screenSize.height / 2,
                gamePanel.tileSize,
                gamePanel.tileSize,
                null);
    }

    public int getItemSlot() {
        return itemSlot;
    }
}
