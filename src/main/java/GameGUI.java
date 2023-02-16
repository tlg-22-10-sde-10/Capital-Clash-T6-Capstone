import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
    private JPanel mainPanel;
    private JButton startBtn;
    private JTextField nameInput;

    public GameGUI() {
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(startBtn, nameInput.getText());
            }
        });
    }

    public static void main(String[] args) {
        GameGUI game = new GameGUI();
        game.setContentPane(game.mainPanel);
        game.setTitle("Capital Clash");
        game.setBounds(600, 200, 600, 600);
        game.setVisible(true);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
