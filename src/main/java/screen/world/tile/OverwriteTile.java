package screen.world.tile;

import control.GamePanel;
import general.Vector;

public class OverwriteTile extends Tile{

    /**
     * Tile that represents a part of a bigger tile.
     * It contains no image and has the same access conditions as the parent tile.
     * Do NOT use as regular tile!
     * @param parent Parent tile to inherit settings from
     * @param position Position in world
     */
    public OverwriteTile(GamePanel gamePanel, Tile parent, Vector position) {
        super(gamePanel, parent.name, position, parent.isTransparent);
        neededEnterAbilities = parent.neededEnterAbilities;
    }

    @Override
    public void loadImagePaths() {
        // No image, as tile is part of a bigger tile
        /* Images for debugging
        pths_tile[0] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[1] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[2] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[3] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[4] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[5] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[6] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[7] = "/OnScreens/Tricorder/SelectBox.png";
        pths_tile[8] = "/OnScreens/Tricorder/SelectBox.png";
         */
    }
}
