package Chess_v1.Game;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
public class Window extends JFrame {
    private ChessBoard chessboard = new ChessBoard();
    private JButton reset;
    private JLabel turn;
    private JLabel error;
    private JLabel promotionboard;
    private JLabel suggestionboard;
    private int width = 1400;
    private int height = 800;
    Window() {
        Color background = new Color(135,135,75);
        Color button = new Color(0,162,232);
        setSize(width,height);
        setLayout(null);
        chessboard.setBounds((width-600)/2, (height-600)/2,600,600);
        add(chessboard);

        reset = new JButton();
        reset.setBounds(10,300,200,70);
        reset.setActionCommand("reset");
        reset.setIcon((new ImageIcon(getClass().getResource("../Icons/reset.png"))));
        reset.setBackground(background);
        reset.setBorderPainted(false);
        reset.setFocusPainted(false);
        add(reset);

        JPanel messageboard = new JPanel();
        messageboard.setLayout(new GridLayout(2,1));
        messageboard.setBounds((width-600)/2, 0,600,(height-600)/2);
        messageboard.setBorder(new LineBorder(new Color(48, 42, 15),5));
        messageboard.setBackground(new Color(66, 57, 17));
        messageboard.setOpaque(true);
        turn = new JLabel("",SwingConstants.CENTER);
        error = new JLabel("",SwingConstants.CENTER);
        turn.setForeground(Color.WHITE);
        error.setForeground(Color.WHITE);
        messageboard.add(turn);
        messageboard.add(error);
        add(messageboard);

        promotionboard = new JLabel();
        promotionboard.setLayout(new GridLayout(4,1));
        promotionboard.setBounds(((width-600)/2)-100,200,100,400);
        promotionboard.setBorder(new LineBorder(Color.black,5));
        promotionboard.setVisible(false);
        JButton queen = new JButton(); JButton rook = new JButton(); JButton bishop = new JButton(); JButton knight = new JButton();
        queen.setActionCommand("Queen"); queen.setEnabled(false); queen.setBackground(button); queen.setBorderPainted(false); queen.setFocusPainted(false);
        rook.setActionCommand("Rook"); rook.setEnabled(false); rook.setBackground(button); rook.setBorderPainted(false); rook.setFocusPainted(false);
        bishop.setActionCommand("Bishop"); bishop.setEnabled(false); bishop.setBackground(button); bishop.setBorderPainted(false); bishop.setFocusPainted(false);
        knight.setActionCommand("Knight"); knight.setEnabled(false); knight.setBackground(button); knight.setBorderPainted(false); knight.setFocusPainted(false);
        promotionboard.add(queen); promotionboard.add(rook); promotionboard.add(bishop); promotionboard.add(knight);
        add(promotionboard);

        suggestionboard = new JLabel();
        suggestionboard.setLayout(new GridLayout(2,1));
        suggestionboard.setBounds(1100,200,250,200);
        suggestionboard.setVisible(false);
        suggestionboard.setBackground(button);
        suggestionboard.setOpaque(true);
        JLabel text = new JLabel("Do you accept the suggested move?",SwingConstants.CENTER);
        text.setBackground(button);
        JLabel options = new JLabel();
        options.setLayout(new GridLayout(1,2));
        JButton no = new JButton("Decline");
        no.setActionCommand("Decline"); no.setEnabled(false); no.setBackground(button); no.setBorderPainted(false); no.setFocusPainted(false);
        JButton yes = new JButton("Accept");
        yes.setActionCommand("Accept"); yes.setEnabled(false); yes.setBackground(button); yes.setBorderPainted(false); yes.setFocusPainted(false);
        options.add(no); options.add(yes); suggestionboard.add(text); suggestionboard.add(options);
        add(suggestionboard);
    
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        setTitle("SamChess");
        setIconImage((new ImageIcon(getClass().getResource("../Icons/icon.png"))).getImage());
        getContentPane().setBackground(background);
        setVisible(true);
    }
    public JButton[] getButtons() {
        JButton[] buttons = new JButton[7+8*8];
        buttons[0] = reset;
        int i = 1;
        for (Component label : suggestionboard.getComponents()) {
            if (((JLabel)label).getComponents().length != 0) {
                for (Component component : ((JLabel)label).getComponents()) {
                    buttons[i] = (JButton)component;
                    i++;
                }
            }
        }
        for (Component button :promotionboard.getComponents()) {
            buttons[i] = (JButton)button;
            i++;
        }
        for (JButton[] row : chessboard.getTiles()) {
            for (JButton button : row) {
                buttons[i] = button;
                i++;
            }
        }
        return buttons;
    }
    public void writeBoard(ImageIcon[][] images) {
        JButton[][] buttons = chessboard.getTiles();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col].setIcon(images[row][col]);
            }
        }
    }
    public void setPossibleMoves(String[] possible_moves) {
        for (String move: possible_moves) {
            JButton tile = chessboard.getTile(move);
            tile.setBackground(Color.RED);
        }
    }
    public void setPickedPos(String pos) {
        chessboard.getTile(pos).setBackground(new Color(50,140,20));
    }
    public void resetBackground() {
        chessboard.resetBackground();
    }
    public void setTurn(String team) {
        turn.setText(team+"'s turn");
    }
    public void setError(String err) {
        error.setText(err);
    }
    public void showPromotion(String team) {
        promotionboard.setVisible(true);
        for (Component promotion : promotionboard.getComponents()) {
            JButton button = (JButton)promotion;
            button.setEnabled(true);
            button.setIcon(new ImageIcon(getClass().getResource("../Icons/"+button.getActionCommand()+"_"+team+".png")));
        }
    }
    public void hidePromotion() {
        promotionboard.setVisible(false);
        for (Component promotion : promotionboard.getComponents()) {
            
            ((JButton)promotion).setEnabled(false);
        }
    }
    public void showSuggestion() {
        suggestionboard.setVisible(true);
        for (Component label : suggestionboard.getComponents()) {
            if (((JLabel)label).getComponents().length != 0) {
                for (Component component : ((JLabel)label).getComponents()) {
                        component.setEnabled(true);
                   }
            }
        }
    }
    public void hideSuggestion() {
        suggestionboard.setVisible(false);
        for (Component label : suggestionboard.getComponents()) {
            if (((JLabel)label).getComponents().length != 0) {
                for (Component component : ((JLabel)label).getComponents()) {
                        component.setEnabled(false);
                   }
            }
        }
    }
}   