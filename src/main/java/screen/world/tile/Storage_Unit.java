package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import entity.Player;
import general.Vector;

import java.awt.*;

public class Storage_Unit extends Tile {
    public static final String TILE_NAME = "Storage Unit";

    public Storage_Unit(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, true);
        neededEnterAbilities.add(Ability.MORPH);
    }

    @Override
    public void loadImagePaths() {
        pths_tile[0] = "/Tiles/Interactables/Chest.png";
        pths_tile[1] = "/Tiles/Interactables/Chest.png";
        pths_tile[2] = "/Tiles/Interactables/Chest.png";
        pths_tile[3] = "/Tiles/Interactables/Chest.png";
        pths_tile[4] = "/Tiles/Interactables/Chest.png";
        pths_tile[5] = "/Tiles/Interactables/Chest.png";
        pths_tile[6] = "/Tiles/Interactables/Chest.png";
        pths_tile[7] = "/Tiles/Interactables/Chest.png";
        pths_tile[8] = "/Tiles/Interactables/Chest.png";
    }

    @Override
    public void interact(Player player) {
        // TODO
        System.out.println("Interacting");
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if(isInteractedWith) {
            gamePanel.log("INTERACTING");
        }
    }
}