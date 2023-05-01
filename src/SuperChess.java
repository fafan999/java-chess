import javax.swing.JFrame; //for the main window

public class SuperChess {
//	public static Piece selectedPiece = null;

	public static void main(String[] args) {

		JFrame mainFrame = new JFrame(); // creating the main window

//		JPanel chessBoardContainer = new JPanel();
//		chessBoardContainer.setBounds(100, 100, 512, 512);

		ChessBoard chessBoard = new ChessBoard();

		chessBoard.setBounds(20, 20, ChessBoard.SIZE_OF_SQUARE * 8, ChessBoard.SIZE_OF_SQUARE * 8);

		mainFrame.setSize(600, 600); // resize the window (first)
		mainFrame.setLocationRelativeTo(null); // center the window (second)
//		mainFrame.setUndecorated(true);
		mainFrame.add(chessBoard);
		mainFrame.setDefaultCloseOperation(3); // EXIT_ON_CLOSE
		mainFrame.setLayout(null);
		mainFrame.setVisible(true);
	}
}
