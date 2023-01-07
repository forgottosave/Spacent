package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean up,down,left,right,action,action2,action3,escaping;
    public int itemSlot = 0;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> action = true;
            case KeyEvent.VK_SHIFT -> action2 = true;
            case KeyEvent.VK_CONTROL -> action3 = true;
            case KeyEvent.VK_ESCAPE -> escaping = true;
            case KeyEvent.VK_1 -> itemSlot = 0;
            case KeyEvent.VK_2 -> itemSlot = 1;
            case KeyEvent.VK_3 -> itemSlot = 2;
            case KeyEvent.VK_4 -> itemSlot = 3;
            case KeyEvent.VK_5 -> itemSlot = 4;
            default -> System.out.println("Key not implemented yet");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_D -> right = false;
            case KeyEvent.VK_SPACE -> action = false;
            case KeyEvent.VK_SHIFT -> action2 = false;
            case KeyEvent.VK_CONTROL -> action3 = false;
            case KeyEvent.VK_ESCAPE -> escaping = false;
            case KeyEvent.VK_1 -> itemSlot = 0;
            case KeyEvent.VK_2 -> itemSlot = 1;
            case KeyEvent.VK_3 -> itemSlot = 2;
            case KeyEvent.VK_4 -> itemSlot = 3;
            case KeyEvent.VK_5 -> itemSlot = 4;
            default -> System.out.println("Key not implemented yet");
        }
    }
}
