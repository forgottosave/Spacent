package screen.end;

        import control.GamePanel;

        import javax.imageio.ImageIO;
        import java.awt.*;
        import java.awt.image.BufferedImage;
        import java.io.IOException;
        import java.util.Objects;

public class PlayerDeadEnd extends EndScreen{
    final static int ANIMATION_TIME = 7;

    BufferedImage animation;
    int animationTimer;

    public PlayerDeadEnd(GamePanel gamePanel) {
        super(gamePanel, "Player Dead", false);
        animationTimer = ANIMATION_TIME * 60;
    }

    @Override
    public void loadImage() {
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Endings/Ending_Player_Dead.png")));
            animation = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Endings/Ending_Player_Dead_Animation.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (animationTimer <= 0) {
            super.draw(g2);
        }
        else {
            animationTimer--;
            gamePanel.getShader().draw(g2);
            g2.drawImage(animation,
                    (int) (-2 * (ANIMATION_TIME * 60 - animationTimer) * gamePanel.scale + gamePanel.screenSize.width),
                    0,
                    (int) (animation.getWidth() * gamePanel.scale / 2),
                    gamePanel.screenSize.height,
                    null);
        }
    }
}