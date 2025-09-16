package Game;
import java.awt.*;
import javax.swing.*;
public class ChessBoard extends JPanel {
    private JButton[][] tiles = new JButton[8][8];
    private JPanel center = new JPanel();
    private JPanel letterPanel = new JPanel();
    private JPanel numberPanel = new JPanel();
    ChessBoard() {
        Color black = new Color(135,105,75);
        Color white = new Color(220,210,195);
        setBackground(Color.DARK_GRAY);
        setLayout(null);
        setPreferredSize(new Dimension(600,600));
        setSize(600,600);
        center.setLayout(new GridLayout(8,8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton tile = new JButton();
                tile.setFocusPainted(false);
                tile.setBorderPainted(false);
                tile.setBackground((i + j) % 2 == 0 ? white : black);
                center.add(tile);
                tiles[i][j] = tile;
            }
        }
        setMessages();
        center.setBounds(20,20,560,560);
        add(center);

        letterPanel.setBackground(Color.DARK_GRAY);
        letterPanel.setLayout(new GridLayout(1,8));
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (String letter:letters) {
            JLabel label = new JLabel(letter);
            label.setForeground(white);
            letterPanel.add(label);
        }
        letterPanel.setBounds(50,580,560,20);
        add(letterPanel);

        numberPanel.setBackground(Color.DARK_GRAY);
        numberPanel.setLayout(new GridLayout(8,1));
        String[] numbers = {"8","7","6","5","4","3","2","1",};
        for (String number:numbers) {
            JLabel label = new JLabel(number);
            label.setForeground(white);
            numberPanel.add(label);
        }
        numberPanel.setBounds(10,20,20,560);
        add(numberPanel);
    }
    private void setMessages() {
        for (String col: new String[]{"A", "B", "C", "D","E", "F","G", "H"}) {
            for (String row:new String[]{"1","2","3","4","5","6","7","8"}) {
                getTile(col+row).setActionCommand(col+row);
            }
        }
    }
    public void resetBackground() {
        Color black = new Color(135,105,75);
        Color white = new Color(220,210,195);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton tile = tiles[i][j];
                tile.setBackground((i + j) % 2 == 0 ? white : black);
            }
        }
    }
    public JButton getTile(String position) {
        String[] coordinates = position.split("");
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int col = 0;
        for (int i = 0; i < letters.length; i++) {
            if (letters[i].equals(coordinates[0])) {
                col = i;
            }
        }
        return tiles[8-Integer.parseInt(coordinates[1])][col];
    }
    public JButton[][] getTiles() {
        return tiles;
    }
}