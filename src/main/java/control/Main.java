package control;

import screen.gameManagement.GameManagement;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Starter class for the game 'Spacent'
 * @since 26.03.22
 * @author Timon
 */
public class Main {

    private static final Locale locale = new Locale("en");
    private static final ResourceBundle textbundle = ResourceBundle.getBundle("texts",locale);

    public static void main(String[] args) {
        // create loading screen
        JFrame loading = new JFrame();
        LoadingScreen loadingScreen = new LoadingScreen();
        loading.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        loading.setResizable(false);
        loading.setTitle(textbundle.getString("title"));
        loading.add(loadingScreen);
        loading.setUndecorated(true);
        loading.pack();
        loading.setLocationRelativeTo(null);

        loading.setVisible(true);
        loadingScreen.startLoadingThread();
        // create window
        JFrame window = new JFrame();
        GamePanel gamePanel = new GamePanel();
        // set window behavior
        window.addWindowListener(new WindowEventHandler(gamePanel));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle(textbundle.getString("title"));
        // add game panel to window
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        // close loading screen
        loadingScreen.closeLoadingScreen();
        loading.setVisible(false);
        // start game
        window.setVisible(true);
        gamePanel.startGameThread();
    }

    static class WindowEventHandler extends WindowAdapter {
        final GamePanel gamePanel;

        public WindowEventHandler(GamePanel gamePanel) {
            this.gamePanel = gamePanel;
        }

        public void windowClosing(WindowEvent evt) {
            gamePanel.saveGame(GameManagement.AUTO_SAVE_FILE);
        }
    }
}
