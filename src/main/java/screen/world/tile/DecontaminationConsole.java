package screen.world.tile;

import control.GamePanel;
import entity.Player;
import general.Vector;

public class DecontaminationConsole extends Tile{
    public static final String TILE_NAME = "Decontamination Console";
    int state;

    public DecontaminationConsole(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, false);
        state = 0;
    }

    @Override
    public void loadImagePaths() {
        // TODO
        pths_tile[0] = "/Tiles/Floor.png";
    }

    @Override
    public void interact(Player player) {
        // TODO
    }
}
