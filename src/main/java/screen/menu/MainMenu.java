package screen.menu;

import control.GamePanel;
import control.KeyHandler;
import general.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenu {
    // general properties
    final GamePanel gamePanel;
    final KeyHandler keyHandler;
    int frameCount = 0;
    int actionCount = 100;
    // particles
    final List<Particle> particles;
    final List<Particle> topParticles;
    // images
    BufferedImage background;
    BufferedImage spaceship;
    BufferedImage headline;
    final BufferedImage[] buttons;
    final Vector[] buttonPositions;


    public MainMenu(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.particles = new ArrayList<>();
        this.topParticles = new ArrayList<>();
        this.buttons = new BufferedImage[1];
        this.buttonPositions = new Vector[1];
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Background.png")));
            spaceship = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Spaceship_Infected.png")));
            headline = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Headline.png")));
            buttons[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/OnScreens/Tricorder/Tricorder_2.png")));
            buttonPositions[0] = new Vector(10,10);
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void update() {
        frameCount = (int) ((frameCount + 1) % gamePanel.FPS);
        actionCount++;
        // update key action
        if (actionCount > gamePanel.FPS / 3 && (keyHandler.action || keyHandler.action2)) {
            if (keyHandler.action)
                gamePanel.setCurrentScreen(GamePanel.SCREEN_TRANSPORTER);
            else
                gamePanel.setCurrentScreen(GamePanel.SCREEN_GAME_MANAGEMENT);
            actionCount = 0;
        }
        // manage particles
        if (frameCount % 20 == 0) {
            particles.add(new Particle(new Vector(gamePanel.screenSize.getWidth(), Math.random() * gamePanel.screenSize.getHeight()), (int) (gamePanel.scale)));
        }
        if (frameCount == 0) {
            topParticles.add(new Particle(new Vector(gamePanel.screenSize.getWidth(), Math.random() * gamePanel.screenSize.getHeight()), (int) (gamePanel.scale)));
        }
        particles.removeIf(p -> p.getPosition().x() < -60 * gamePanel.scale);
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.PLAIN, 20));
        // draw background
        g2.drawImage(background,
                0,
                0,
                gamePanel.screenSize.width,
                gamePanel.screenSize.height,
                null);
        try {
            particles.forEach(p -> p.draw(g2));
        } catch (Exception ignored) {
            System.err.println("Some unimportant error.");
        }
        g2.drawImage(spaceship,
                gamePanel.screenSize.width / 5,
                gamePanel.screenSize.height / 8,
                (int) (3 * gamePanel.screenSize.width / 5.),
                (int) (spaceship.getHeight() * ((3 * gamePanel.screenSize.width / 5.) / spaceship.getWidth())),
                null);
        try {
            topParticles.forEach(p -> p.draw(g2));
        } catch (Exception ignored) {
            System.err.println("Some unimportant error.");
        }
        // Draw Foreground
        g2.drawImage(headline,
                (int) (gamePanel.screenSize.width * 0.26),
                (int) (- gamePanel.screenSize.width * 0.1),
                (int) (gamePanel.screenSize.width * 0.5),
                (int) (gamePanel.screenSize.width * 0.5),
                null);
        // Draw Info Message
        String underline = "Start Game: 'space'   Game Management: 'shift'";
        g2.drawString(underline,
                (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.0025 * underline.length()),
                gamePanel.screenSize.height - 20);
    }
}
