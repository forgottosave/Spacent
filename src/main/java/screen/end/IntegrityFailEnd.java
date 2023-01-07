package screen.end;

import control.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class IntegrityFailEnd extends EndScreen{

    public IntegrityFailEnd(GamePanel gamePanel) {
        super(gamePanel, "Ship Integrity Failed", false);
    }

    @Override
    public void loadImage() {
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Endings/Spaceship_Integrity_Failed.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }
}
