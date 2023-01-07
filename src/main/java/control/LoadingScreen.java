package control;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class LoadingScreen extends JPanel implements Runnable {

    boolean showScreen;
    int frameCount = 0;

    Thread loadingThread;
    final Dimension size = new Dimension(1200,400);
    BufferedImage img_loadingScreen;
    BufferedImage img_headline;

    public LoadingScreen() {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        showScreen = true;
        try {
            img_loadingScreen = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Loading_Screen/LoadingScreen.png")));
            img_headline = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Headline.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void startLoadingThread() {
        loadingThread = new Thread(this);
        loadingThread.start();
    }

    public void closeLoadingScreen() {
        showScreen = false;
    }

    @Override
    public void run() {
        // Loading Loop
        while (showScreen) {
            frameCount++;
            repaint();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // set presets
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.BOLD, 50));
        // draw image
        g2.drawImage(img_loadingScreen,
                0,
                0,
                size.width,
                size.height,
                null);
        g2.drawImage(img_headline,
                120,
                (int)(size.height * -0.6),
                img_headline.getWidth() * 10,
                img_headline.getHeight() * 10,
                null);
        g2.drawString(getLoadingString(), size.width / 2 - 90, size.height / 2 + size.height / 3);
    }

    public String getLoadingString() {
        if (frameCount >= 4)
            frameCount = 0;
        return " .".repeat(Math.max(0, frameCount));
    }
}
