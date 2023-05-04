import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame; //for the main window
import javax.swing.JOptionPane; // popup window


public class SuperChess {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("SzuperSakk"); // creating the main window
		mainFrame.setDefaultCloseOperation(3); // EXIT_ON_CLOSE
		mainFrame.setBackground(Color.WHITE);

		JButton blackResign = new JButton("Feladom!");
		blackResign.setBackground(Color.BLACK);
		blackResign.setForeground(Color.WHITE);
		blackResign.setFont(new Font("Arial", Font.PLAIN, 20));
		blackResign.setFocusPainted(false); // prettier

		String drawLabel = "Egyezzünk ki\ndöntetlenben!";
		JButton draw = new JButton("<html>" + drawLabel.replaceAll("\\n", "<br>") + "</html>");
		draw.setBackground(new Color(190, 205, 158));
		draw.setForeground(Color.BLACK);
		draw.setFont(new Font("Arial", Font.PLAIN, 20));
		draw.setFocusPainted(false);

		JButton whiteResign = new JButton("Feladom!");
		whiteResign.setBackground(Color.WHITE);
		whiteResign.setForeground(Color.BLACK);
		whiteResign.setFont(new Font("Arial", Font.PLAIN, 20));
		whiteResign.setFocusPainted(false);
		
		ChessBoard chessBoard = new ChessBoard(mainFrame);
		chessBoard.setSize(ChessBoard.SIZE_OF_SQUARE * 8, ChessBoard.SIZE_OF_SQUARE * 8);

		// for positioning the buttons and chessboard
		mainFrame.setLayout(new GridBagLayout());
		GridBagConstraints position = new GridBagConstraints();
		position.fill = GridBagConstraints.HORIZONTAL; // buttons have equal length
		position.weighty = 0.33;
		position.ipady = 40;
		position.gridx = 0;
		position.gridy = 0;
		position.insets = new Insets(0, 5, 0, 0);
		mainFrame.add(blackResign, position);
		position.weighty = 0.33;
		position.ipady = 20;
		position.gridx = 0;
		position.gridy = 1;
		position.insets = new Insets(100, 5, 100, 0);
		mainFrame.add(draw, position);
		position.weighty = 0.33;
		position.ipady = 40;
		position.gridx = 0;
		position.gridy = 2;
		position.insets = new Insets(0, 5, 0, 0);
		mainFrame.add(whiteResign, position);
		position.fill = GridBagConstraints.VERTICAL;
		position.ipady = ChessBoard.SIZE_OF_SQUARE * 8;
		position.ipadx = ChessBoard.SIZE_OF_SQUARE * 8;
		position.weighty = 1.0;
		position.gridheight = 3;
		position.gridx = 1;
		position.gridy = 0;
		position.insets = new Insets(0, 5, 0, 0);
		mainFrame.add(chessBoard, position);

		mainFrame.pack(); // Resize the window, so all the components can be seen
		mainFrame.setResizable(false); // Do not let the user to resize the window
		mainFrame.setLocationRelativeTo(null); // center the window
		mainFrame.setVisible(true);
		
		draw.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(mainFrame, "Döntetlen a játékosok közös megegyezése alapján.");
				chessBoard.restartTheGame();				
			}
		});
	}
}
