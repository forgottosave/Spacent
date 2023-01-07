package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import general.Vector;

public class Void extends Tile {
    public static final String TILE_NAME = "Void";

    public Void(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, true);
        neededEnterAbilities.add(Ability.FLY);
    }

    @Override
    public void loadImagePaths() {
        // Void should show no image
    }
}