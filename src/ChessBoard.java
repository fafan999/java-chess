import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	private static final Image[] PIECE_IMAGES = new Image[12];
	public static final int SIZE_OF_SQUARE = 64; // determine how big the board is
	public static LinkedList<Piece> allPieces = new LinkedList<>(); // list for tracking the pieces
	public static Point enPassantSquare = null; // en passant square
	public static boolean whiteCanShortCastle;
	public static boolean whiteCanLongCastle;
	public static boolean blackCanShortCastle;
	public static boolean blackCanLongCastle;
	private Piece selectedPiece = null;
	private ArrayList<Move> selectedPieceLegalMoves = new ArrayList<Move>(); // legal moves list of the selectedPiece
	private Alliance sideToMove; // whose side to move
	private JFrame topFrame;

	public ChessBoard(JFrame mainFrame) {
		super();
		this.topFrame = mainFrame;
		positionFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq"); // initial position
//		positionFromFen("4k2r/6r1/8/8/8/8/3R4/R3K3 w Qk");
		try {
			// get the images of the pieces
			BufferedImage allPictures = ImageIO.read(new File("chess_set_SZB.png"));
			int index = 0;
			for (int y = 0; y < 400; y += 200) {
				for (int x = 0; x < 1200; x += 200) {
					PIECE_IMAGES[index] = allPictures.getSubimage(x, y, 200, 200).getScaledInstance(SIZE_OF_SQUARE,
							SIZE_OF_SQUARE, BufferedImage.SCALE_SMOOTH);
					index++;
				}
			}
		} catch (Exception e) {
			System.err.println("Failed to load the pictures of the pieces");
		}

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedPiece = Piece.getPiece(new Point(e.getX() / SIZE_OF_SQUARE, 7 - e.getY() / SIZE_OF_SQUARE));
				if (selectedPiece != null) {
					if (!(selectedPiece.side.equals(sideToMove))) {
						selectedPiece = null; // check which side turn it is
					} else {
						setLegalMoves(selectedPiece);
						allPieces.remove(selectedPiece);
						allPieces.addLast(selectedPiece); // put it at the last so it is at the top
					}
				}
				topFrame.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedPiece != null) {
					validateMove(selectedPiece, new Point(e.getX() / SIZE_OF_SQUARE, 7 - e.getY() / SIZE_OF_SQUARE));
					System.out.println(selectedPiece);
					Piece.isKingInCheck(Alliance.BLACK);
					selectedPiece = null;

					topFrame.repaint();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectedPiece != null) {
					selectedPiece.xRealPosition = e.getX() - (SIZE_OF_SQUARE / 2);
					selectedPiece.yRealPosition = e.getY() - (SIZE_OF_SQUARE / 2);
					// repaint the top frame so the pieces won't leave a track in the background
					topFrame.repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
		});
	}

	/**
	 * Draw the board and everything on it.
	 */
	@Override
	public void paint(Graphics g) {
		boolean white = true; // the first row starts with white
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (white) {
					g.setColor(new Color(238, 238, 210));
				} else {
					g.setColor(new Color(142, 172, 106));
				}
				g.fillRect(file * SIZE_OF_SQUARE, rank * SIZE_OF_SQUARE, SIZE_OF_SQUARE, SIZE_OF_SQUARE);
				white = !white; // every second is white
			}
			white = !white; // new row starts with different color
		}
		g.setColor(new Color(0, 160, 0, 150));
		for (Move m : this.selectedPieceLegalMoves) {
			Point p = m.destinationCoordiante;
			if (Piece.getPiece(p) == null) {
				g.fillOval(p.x * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 3, (7 - p.y) * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 3,
						SIZE_OF_SQUARE / 3, SIZE_OF_SQUARE / 3);
			} else {
				// if there is a piece draw triangles at each corner
				// top left
				int[] a1 = { p.x * SIZE_OF_SQUARE, p.x * SIZE_OF_SQUARE, p.x * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 4 };
				int[] b1 = { (7 - p.y) * SIZE_OF_SQUARE, (7 - p.y) * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 4,
						(7 - p.y) * SIZE_OF_SQUARE };
				g.fillPolygon(a1, b1, 3);
				// top right
				int[] a2 = { (p.x + 1) * SIZE_OF_SQUARE - SIZE_OF_SQUARE / 4, (p.x + 1) * SIZE_OF_SQUARE,
						(p.x + 1) * SIZE_OF_SQUARE };
				int[] b2 = { (7 - p.y) * SIZE_OF_SQUARE, (7 - p.y) * SIZE_OF_SQUARE,
						(7 - p.y) * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 4 };
				g.fillPolygon(a2, b2, 3);
				// bottom right
				int[] a3 = { (p.x + 1) * SIZE_OF_SQUARE - SIZE_OF_SQUARE / 4, (p.x + 1) * SIZE_OF_SQUARE,
						(p.x + 1) * SIZE_OF_SQUARE };
				int[] b3 = { (7 - p.y + 1) * SIZE_OF_SQUARE, (7 - p.y + 1) * SIZE_OF_SQUARE,
						(7 - p.y + 1) * SIZE_OF_SQUARE - SIZE_OF_SQUARE / 4 };
				g.fillPolygon(a3, b3, 3);
				// bottom left
				int[] a4 = { p.x * SIZE_OF_SQUARE, p.x * SIZE_OF_SQUARE, p.x * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 4 };
				int[] b4 = { (7 - p.y + 1) * SIZE_OF_SQUARE, (7 - p.y + 1) * SIZE_OF_SQUARE - SIZE_OF_SQUARE / 4,
						(7 - p.y + 1) * SIZE_OF_SQUARE };
				g.fillPolygon(a4, b4, 3);
			}
		}
		for (Piece p : ChessBoard.allPieces) {
			g.drawImage(PIECE_IMAGES[p.index], p.xRealPosition, p.yRealPosition, this);
		}
	}

	/**
	 * Restart the game with the initial position
	 */
	public void restartTheGame() {
		ChessBoard.allPieces.clear();
		ChessBoard.enPassantSquare = null;
		this.positionFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq"); // initial position
		this.repaint();
	}

	/**
	 * Make the move if it is a valid move, check if sides can castle, check if the
	 * game ended in checkmate or stalemate
	 * 
	 * @param selectedPiece the piece of the tried move
	 * @param newCoordinate the coordinate of the tried move
	 */
	private void validateMove(Piece selectedPiece, Point newCoordinate) {
		Move newMove = null;
		// get the new move corresponding to the new coordinate
		for (Move p : this.selectedPieceLegalMoves) {
			if (p.destinationCoordiante.equals(newCoordinate))
				newMove = p;
		}
		if (newMove != null) {
			// check if there can be en passant move in the next move
			if (newMove instanceof Move.DoublePawnPush) {
				int direction = selectedPiece.side.getDirection();
				ChessBoard.enPassantSquare = new Point(newMove.destinationCoordiante.x,
						newMove.destinationCoordiante.y - direction);
			} else {
				ChessBoard.enPassantSquare = null;
			}
			// check if castle is available
			if (whiteCanShortCastle || whiteCanLongCastle || blackCanShortCastle || blackCanLongCastle) {
				checkCastle(newMove);
			}
			// check if it is a PromotionMove
			if (newMove instanceof Move.PromotionMove || newMove instanceof Move.AttackPromotionMove) {
				this.choosePromotion(newMove);
			}

			newMove.makeMove();
			this.sideToMove = this.sideToMove.getOppositeSide(); // the next turn

			// check if it is a checkmate or a stalemate
			boolean endGame = true;
			for (Piece p : Piece.getSidePieces(sideToMove)) {
				if (this.canMove(p)) {
					endGame = false;
					break;
				}
			}
			if (endGame) {
				String text;
				String title;
				Color textColor;
				Color buttonColor;
				if (Piece.isKingInCheck(this.sideToMove)) {
					// Game ends with checkmate
					if (this.sideToMove.isBlack()) {
						text = "A fehér oldal győzőtt, mert mattot adott az ellenfélnek.";
						title = "Fehér győzelem!";
						textColor = Color.BLACK;
						buttonColor = Color.WHITE;
					} else {
						text = "A fekete oldal győzőtt, mert mattot adott az ellenfélnek.";
						title = "Fekete győzelem!";
						textColor = Color.WHITE;
						buttonColor = Color.BLACK;
					}
				} else {
					// Game ends with a draw
					text = "Döntetlen, mert a " + (this.sideToMove.equals(Alliance.WHITE) ? "fehér" : "fekete")
							+ " játékos nem tud lépni, de nincs sakkban a királya.";
					title = "Patt!";
					textColor = Color.BLACK;
					buttonColor = new Color(190, 205, 158);
				}
				SuperChess.popupDialog(topFrame, text, title, textColor, buttonColor, null);
				this.restartTheGame();
			}

		} else {
			selectedPiece.setRealPosition();
			System.out.println("The " + selectedPiece + " can't move to " + newCoordinate + ".");
		}
		this.selectedPieceLegalMoves.clear();
	}

	/**
	 * Set the selected piece possible moves to the board and remove every move that
	 * leaves the loyal king in check. Also add the castle moves if they are
	 * allowed.
	 * 
	 * @param selectedPiece
	 */
	private void setLegalMoves(Piece selectedPiece) {
		if (selectedPiece != null) {
			this.selectedPieceLegalMoves = this.getLegalMoves(selectedPiece);
		} else {
			this.selectedPieceLegalMoves.clear();
		}
	}

	/**
	 * Get the selected piece possible moves and remove every move that leaves the
	 * loyal king in check. Also add the castle moves if they are allowed.
	 * 
	 * @param selectedPiece
	 */
	private ArrayList<Move> getLegalMoves(Piece selectedPiece) {
		ArrayList<Move> illegalMoves = new ArrayList<Move>();
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		// check if the possible move is legal
		legalMoves = selectedPiece.getPossibleMoves();
		for (Move m : legalMoves) {
			// make the move
			m.makeMove();
			// check if our king is in check
			if (Piece.isKingInCheck(m.movedPiece.side)) {
				// add to the illegal moves
				illegalMoves.add(m);
			}
			// reset the move
			m.resetMove();
		}
		for (Move m : illegalMoves) {
			// remove the illegal moves
			legalMoves.remove(m);
		}
		if (selectedPiece instanceof King) {
			// add castle moves (they are already checked)
			ArrayList<Move> castleMoves = ((King) selectedPiece).getCastleMoves();
			if (castleMoves != null) {
				legalMoves.addAll(castleMoves);
			}
		}
		return legalMoves;
	}

	/**
	 * Check if the given piece can move
	 * 
	 * @param selectedPiece
	 * @return true if the piece can move, false otherwise
	 */
	private boolean canMove(Piece selectedPiece) {
		ArrayList<Move> legalMoves = this.getLegalMoves(selectedPiece);
		if (legalMoves.size() < 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Check if either side can castle based on the castle booleans
	 * 
	 * @param validatedMove
	 */
	private void checkCastle(Move newMove) {
		Piece movedPiece = newMove.movedPiece;
		if (movedPiece.side.isWhite()) {
			// right Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(7, 0)))) {
				whiteCanShortCastle = false;
			}
			// left Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(0, 0)))) {
				whiteCanLongCastle = false;
			}
		}

		if (movedPiece.side.isBlack()) {
			// right Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(7, 7)))) {
				blackCanShortCastle = false;
			}
			// left Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(0, 7)))) {
				blackCanLongCastle = false;
			}
		}
	}

	/**
	 * Convert a Forsyth-Edwards Notation (FEN) string into a position
	 * 
	 * @param fen The FEN string
	 */
	private void positionFromFen(String fen) {
		String squares = fen.substring(0, fen.indexOf(" ")); // squares of the Pieces
		String state = fen.substring(fen.indexOf(" ") + 1); // state of the game TODO (en passant square, )
		String[] ranks = squares.split("/"); // every rank is divided with "/"

		int rank = 7; // reading starts from the 8th rank
		for (String r : ranks) {
			int file = 0; // reading starts from the first file
			for (int i = 0; i < r.length(); i++) {
				char c = r.charAt(i);
				if (Character.isDigit(c)) {
					file += Character.digit(c, 10);
				} else {
					this.createNewPiece(new Point(file, rank), c);
					file++;
				}
			}
			rank--; // step to the next rank
		}

		this.sideToMove = state.toLowerCase().contains("w") ? Alliance.WHITE : Alliance.BLACK;
		whiteCanShortCastle = state.contains("K");
		whiteCanLongCastle = state.contains("Q");
		blackCanShortCastle = state.contains("k");
		blackCanLongCastle = state.contains("q");
	}

	/**
	 * Make a popup window to let the player choose the promoted piece
	 * 
	 * @param promotionMove
	 */
	private void choosePromotion(Move promotionMove) {
		JComboBox<String> list = new JComboBox<String>(new String[] { "Vezér", "Bástya", "Huszár", "Futó" });
		list.setFont(new Font("Arial", Font.PLAIN, 20));

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(list, BorderLayout.SOUTH);

		Color textColor;
		Color buttonColor;
		String title;
		if (promotionMove.movedPiece.side.isWhite()) {
			textColor = Color.BLACK;
			buttonColor = Color.WHITE;
			title = "Fehér gyalog előléptetése";
		} else {
			textColor = Color.WHITE;
			buttonColor = Color.BLACK;
			title = "Fekete gyalog előléptetése";
		}

		SuperChess.popupDialog(topFrame, "Mivé váljon eme győzedelmes egység?", title, textColor, buttonColor, panel);

		String choice = (String) list.getSelectedItem();
		Piece promotionPiece;

		if (choice.equals("Bástya")) {
			promotionPiece = new Rook(promotionMove.destinationCoordiante, promotionMove.movedPiece.side);
		} else if (choice.equals("Huszár")) {
			promotionPiece = new Knight(promotionMove.destinationCoordiante, promotionMove.movedPiece.side);
		} else if (choice.equals("Futó")) {
			promotionPiece = new Bishop(promotionMove.destinationCoordiante, promotionMove.movedPiece.side);
		} else {
			promotionPiece = new Queen(promotionMove.destinationCoordiante, promotionMove.movedPiece.side);
		}

		if (promotionMove instanceof Move.PromotionMove) {
			((Move.PromotionMove) promotionMove).setPromotionPiece(promotionPiece);
		}

		if (promotionMove instanceof Move.AttackPromotionMove) {
			((Move.AttackPromotionMove) promotionMove).setPromotionPiece(promotionPiece);
		}
	}

	/**
	 * Crate a new piece at the given coordinate
	 * 
	 * @param coordinate coordinate of the new piece
	 * @param pieceType  type of the piece
	 */
	private void createNewPiece(Point coordinate, char pieceType) {
		final Alliance side;
		if (Character.isUpperCase(pieceType)) {
			side = Alliance.WHITE;
		} else {
			side = Alliance.BLACK;
		}

		switch (String.valueOf(pieceType).toLowerCase()) {
		case "p":
			new Pawn(coordinate, side);
			break;
		case "r":
			new Rook(coordinate, side);
			break;
		case "n":
			new Knight(coordinate, side);
			break;
		case "b":
			new Bishop(coordinate, side);
			break;
		case "q":
			new Queen(coordinate, side);
			break;
		case "k":
			new King(coordinate, side);
			break;
		}
	}
}
