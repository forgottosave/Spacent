package entity;

import control.GamePanel;
import general.Vector;
import general.Direction;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Infected extends Entity{

    int currentAction = 0;
    int actionTime = 0;

    public Infected(GamePanel gamePanel, Vector position) {
        super(gamePanel,4, 4, 8, 1, 60, position);
        abilities.add(Ability.INFECTED);
    }

    @Override
    public void getImage() {
        try {
            img_front[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/Infected/Infected_Front.png")));
            img_front[1] = img_front[0];
            img_front[2] = img_front[0];
            img_back[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/Infected/Infected_Back.png")));
            img_back[1] = img_back[0];
            img_back[2] = img_back[0];
            img_left[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/Infected/Infected_Left.png")));
            img_left[1] = img_left[0];
            img_left[2] = img_left[0];
            img_right[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/entity/Infected/Infected_Right.png")));
            img_right[1] = img_right[0];
            img_right[2] = img_right[0];
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    @Override
    public void die() {
        System.out.println("Infected Dead");
    }

    @Override
    public void update2() {
        // change action timer
        if (actionTime-- == 0) {
            actionTime = (int) (Math.random() * 120) + 60;
            currentAction = (int) (Math.random() * 10);
        }
        // check for near entities in sight
        List<Entity> nearEntities = gamePanel
                .getWorld()
                .getAllEntitiesInSight(this)
                .stream()
                .filter(entity -> entity.getAbilities().contains(Ability.HEALTHY))
                .toList();
        Entity closestEntity = gamePanel.getWorld().checkForNearestEntityFromList(this, nearEntities);
        if (closestEntity != null && gamePanel.getWorld().checkLineOfSight(position, closestEntity.position, 0)) {
            closestEntity.setBeingAttacked(true);
            if(Vector.length(Vector.vectorBetweenEntities(this,closestEntity)) < attackRange) {
                attack();
            }
            Direction nearestEntityDir = Direction.getDirectionFromVector(Vector.vectorBetweenEntities(this,closestEntity));
            moveEntity(nearestEntityDir);
        }
        // if no entity in sight: walk into random direction
        else {
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
    }
}
