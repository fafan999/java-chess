import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame; //for the main window

public class SuperChess {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("SzuperSakk"); // creating the main window
		GridBagConstraints position = new GridBagConstraints(); // for positioning the buttons and chessboard
		mainFrame.setSize(800, 800); // resize the window
										// (first)
		mainFrame.setLocationRelativeTo(null); // center the window (second)
		mainFrame.setDefaultCloseOperation(3); // EXIT_ON_CLOSE

//		JPanel chessBoardContainer = new JPanel();
//		chessBoardContainer.setBounds(100, 100, 512, 512);
		Button blackResign = new Button("Feladom!");
		Button draw = new Button("Egyezzünk ki döntetlenben!");
		Button whiteResign = new Button("Feladom!");

		ChessBoard chessBoard = new ChessBoard();
		chessBoard.setSize(ChessBoard.SIZE_OF_SQUARE * 8, ChessBoard.SIZE_OF_SQUARE * 8);

		mainFrame.setLayout(new GridBagLayout());
		position.fill = GridBagConstraints.HORIZONTAL; // buttons have equal length
//		position.weighty = 0;
		position.ipady = 0;
		position.gridx = 0;
		position.gridy = 0;
		position.insets = new Insets(0, 0, 0, 0);
		mainFrame.add(blackResign, position);
//		position.weighty = 0;
		position.ipady = 0;
		position.gridx = 0;
		position.gridy = 1;
		position.insets = new Insets(ChessBoard.SIZE_OF_SQUARE * 4 - draw.getHeight() - blackResign.getHeight(), 0,
				ChessBoard.SIZE_OF_SQUARE * 4 - draw.getHeight() - whiteResign.getHeight(), 0);
		mainFrame.add(draw, position);
		position.weighty = 0;
		position.ipady = 0;
		position.gridx = 0;
		position.gridy = 2;
		position.insets = new Insets(0, 0, 0, 0);
		mainFrame.add(whiteResign, position);
		position.fill = GridBagConstraints.VERTICAL;
		position.ipady = ChessBoard.SIZE_OF_SQUARE * 8;
		position.ipadx = ChessBoard.SIZE_OF_SQUARE * 8;
//		position.weighty = 0;
		position.gridheight = 3;
		position.gridx = 1;
		position.gridy = 0;
		position.insets = new Insets(0, 0, 0, 0);
		mainFrame.add(chessBoard, position);

		mainFrame.setVisible(true);
	}
}
