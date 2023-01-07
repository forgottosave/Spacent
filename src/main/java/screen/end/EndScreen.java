package screen.end;

import onScreen.PopupMessage;
import control.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class EndScreen {
    static final int END_SCREEN_TIME = 4;
    static final int CREDITS_TIME = 18;

    final GamePanel gamePanel;
    BufferedImage background;
    int timer;

    List<String> credits;

    public EndScreen(GamePanel gamePanel, String message, boolean goodEnding) {
        this.gamePanel = gamePanel;
        this.timer = (END_SCREEN_TIME + CREDITS_TIME) * 60;
        gamePanel.setPopup(new PopupMessage(gamePanel,message,goodEnding ? Color.WHITE : new Color(0x9D2F2F), END_SCREEN_TIME + 2));
        loadImage();
        loadCredits();
    }

    abstract void loadImage();

    private void loadCredits() {
        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/Endings/credits.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            credits = new ArrayList<>();
            String line;
            int lineCounter = 0;
            while ((line = br.readLine()) != null) {
                credits.add(lineCounter,line);
                lineCounter++;
            }
            br.readLine();
        } catch (Exception e) {
            System.err.println("Failed to load end credits");
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("MONOSPACED", Font.PLAIN, 40));
        g2.drawImage(background,0,0,gamePanel.screenSize.width, gamePanel.screenSize.height, null);
        timer--;
        if (timer <= 0) {
            gamePanel.setEndScreen(null);
            gamePanel.setCurrentScreen(GamePanel.SCREEN_MENU);
        }
        else if (timer < (CREDITS_TIME + 4) * 60) {
            for (int i = 0; i < credits.size(); i++) {
                g2.drawString(credits.get(i),
                        (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.005 * credits.get(i).length()),
                        (int) (gamePanel.screenSize.height - (CREDITS_TIME * 60 - timer) * gamePanel.scale / 1.2) + i * gamePanel.tileSize);
            }
        }
    }
}
