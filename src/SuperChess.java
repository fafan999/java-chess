import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame; //for the main window
import javax.swing.JLabel;
import javax.swing.JOptionPane; // popup window
import javax.swing.JPanel;

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
				popupDialog(mainFrame, "Döntetlen a játékosok közös megegyezése alapján.", "Döntetlen!", Color.BLACK,
						new Color(190, 205, 158), null);
				chessBoard.restartTheGame();
			}
		});

		blackResign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupDialog(mainFrame, "A fehér oldal győzőtt, mert az ellenfél feladta a játékot.", "Fehér győzelem!",
						Color.BLACK, Color.WHITE, null);
				chessBoard.restartTheGame();
			}
		});

		whiteResign.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupDialog(mainFrame, "A fekete oldal győzőtt, mert az ellenfél feladta a játékot.",
						"Fekete győzelem!", Color.WHITE, Color.BLACK, null);
				chessBoard.restartTheGame();
			}
		});
	}

	/**
	 * Make a popup window to the user to interact with it
	 * 
	 * @param mainframe   parent window
	 * @param text        information about the window
	 * @param title       title of the popup window
	 * @param textColor   button text color
	 * @param buttonColor button background color
	 * @param panel       panel of the option dialog
	 */
	static void popupDialog(JFrame mainframe, String text, String title, Color textColor, Color buttonColor,
			JPanel panel) {
		JButton okButton = new JButton("ok");
		okButton.setBackground(buttonColor);
		okButton.setForeground(textColor);
		okButton.setFont(new Font("Arial", Font.PLAIN, 20));
		okButton.setFocusPainted(false); // prettier
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = getOptionPane((JComponent) e.getSource());
				pane.setValue(okButton);
			}
		});

		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 20));
		if (panel == null) {
			panel = new JPanel(new BorderLayout());
		}
		panel.add(label, BorderLayout.NORTH);

		JOptionPane.showOptionDialog(mainframe, panel, title, JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new Object[] { okButton }, okButton);
	}

	/**
	 * Get the JOptionPane of the button
	 * 
	 * @param parent JOptionPane
	 * @return The JOptionPane of the button
	 */
	private static JOptionPane getOptionPane(JComponent parent) {
		JOptionPane pane = null;
		if (!(parent instanceof JOptionPane)) {
			pane = getOptionPane((JComponent) parent.getParent());
		} else {
			pane = (JOptionPane) parent;
		}
		return pane;
	}
}
