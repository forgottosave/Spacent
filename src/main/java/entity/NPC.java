package entity;

import control.GamePanel;
import general.Vector;
import general.Direction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class NPC extends Entity{

    int currentAction = 0;
    int actionTime = 0;
    boolean displayMessage = false;
    final String infoText;

    /**
     * Non-playable, peaceful character wandering the world.
     * Moves randomly and talks with an on-screen message if the Player is near.
     * @param gamePanel Reference to the Gamepanel
     * @param position Current Entity position
     * @param infoText Text to 'speak' when Player is near
     */
    public NPC(GamePanel gamePanel, Vector position, String infoText) {
        super(gamePanel,6, 3, 3, 1, 120, position);
        this.infoText = infoText;
        abilities.add(Ability.HEALTHY);
    }

    @Override
    public void getImage() {
        try {
            img_front[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Front.png")));
            img_front[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Front-Step.png")));
            img_front[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Front-Step2.png")));
            img_back[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Back.png")));
            img_back[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Back-Step.png")));
            img_back[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Back-Step2.png")));
            img_left[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Left.png")));
            img_left[1] = img_left[0];
            img_left[2] = img_left[0];
            img_right[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/NPC/NPC_Right.png")));
            img_right[1] = img_right[0];
            img_right[2] = img_right[0];
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    @Override
    public void die() {
        System.out.println("Oh no I'm dead");
    }

    @Override
    public void update2() {
        if (actionTime-- == 0) {
            displayMessage = !gamePanel
                    .getWorld()
                    .getAllEntitiesInSight(this)
                    .stream()
                    .filter(e -> e.getAbilities().contains(Ability.PLAYER))
                    .toList()
                    .isEmpty();
            actionTime = (int) (Math.random() * 90) + 120;
            currentAction = (int) (Math.random() * 10);
        }
        switch (currentAction) {
            case 0 -> moveEntity(Direction.UP);
            case 1 -> moveEntity(Direction.DOWN);
            case 2 -> moveEntity(Direction.LEFT);
            case 3 -> moveEntity(Direction.RIGHT);
            case 4 -> moveEntity(Direction.UPRI);
            case 5 -> moveEntity(Direction.DORI);
            case 6 -> moveEntity(Direction.UPLE);
            case 7 -> moveEntity(Direction.DOLE);
        }
    }

    /**
     * draw entity_2 in new frame
     * @param g2 2D-Graphic to draw in
     */
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (displayMessage)
            g2.drawString(infoText,
                    (int) (gamePanel.getPlayerDependantCoordinate(position).x() - gamePanel.tileSize * 0.04 * infoText.length()),
                    gamePanel.getPlayerDependantCoordinate(position).y() - gamePanel.tileSize);
    }

    @Override
    public String toString() {
        return super.toString().substring(0,super.toString().length() - 1) + "-info=" + infoText + "}";
    }
}
