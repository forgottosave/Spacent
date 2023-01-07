package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import entity.Player;
import general.Vector;

public class Transporter extends Tile {
    public static final String TILE_NAME = "Transporter";

    public Transporter(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, false);
        neededEnterAbilities.add(Ability.MORPH);
    }

    @Override
    public void loadImagePaths() {
        pths_tile[0] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[1] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[2] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[3] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[4] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[5] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[6] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[7] = "/Tiles/Interactables/Turbolift.png";
        pths_tile[8] = "/Tiles/Interactables/Turbolift.png";
    }

    @Override
    public void interact(Player player) {
        player.getGamePanel().setCurrentScreen(GamePanel.SCREEN_TRANSPORTER);
    }
}