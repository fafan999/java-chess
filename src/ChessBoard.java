import java.awt.Color;
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
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	private static final Image[] PIECE_IMAGES = new Image[12];
	public static final int SIZE_OF_SQUARE = 64; // determine how big the board is
	public static LinkedList<Piece> allPieces = new LinkedList<>(); // list for tracking the pieces
	public static Point enPassantSquare = null; // en passant square
	public static boolean whiteCanShortCastle = true;
	public static boolean whiteCanLongCastle = true;
	public static boolean blackCanShortCastle = true;
	public static boolean blackCanLongCastle = true;
	private Piece selectedPiece = null;
	private ArrayList<Move> selectedPieceLegalMoves = new ArrayList<Move>(); // legal moves list of the selectedPiece
	private Alliance sideToMove = Alliance.WHITE; // white starts

	public ChessBoard() {
		super();
		positionFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR "); // initial position
		// get the images of the pieces ---------------------------------
		try {
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
		} // -------------------------------------------------------------

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
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
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (selectedPiece != null) {
//					selectedPiece.move(new Point(e.getX()/SIZE_OF_SQUARE, 7-e.getY()/SIZE_OF_SQUARE));
					validateMove(selectedPiece, new Point(e.getX() / SIZE_OF_SQUARE, 7 - e.getY() / SIZE_OF_SQUARE));
//					System.out.println(selectedPiece.coordinate.toString());
					System.out.println(selectedPiece);
//					System.out.println("Is in check:" + isInCheck(Alliance.BLACK));
					Piece.isKingInCheck(Alliance.BLACK);
					selectedPiece = null;
					repaint();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectedPiece != null) {
					selectedPiece.xRealPosition = e.getX() - (SIZE_OF_SQUARE / 2);
					selectedPiece.yRealPosition = e.getY() - (SIZE_OF_SQUARE / 2);
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
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
	 * Get the selected piece possible moves and remove every move that leaves the
	 * loyal king in check. Also add the castle moves if they are allowed.
	 * 
	 * @param selectedPiece
	 */
	public void setLegalMoves(Piece selectedPiece) {
		ArrayList<Move> illegalMoves = new ArrayList<Move>();
		if (selectedPiece != null) {
			// check if the possible move is legal
			this.selectedPieceLegalMoves = selectedPiece.getPossibleMoves();
			for (Move m : this.selectedPieceLegalMoves) {
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
				selectedPieceLegalMoves.remove(m);
			}
			if (selectedPiece instanceof King) {
				// add castle moves (they are already checked)
				ArrayList<Move> castleMoves = ((King) selectedPiece).getCastleMoves();
				if (castleMoves != null) {
					selectedPieceLegalMoves.addAll(castleMoves);
				}
			}
		} else {
			this.selectedPieceLegalMoves.clear();
		}
		System.out.println("set legal moves");
	}

//	public ArrayList<Move> getPieceMoves(Piece selectedPiece) {
//		return selectedPiece.getPossibleMoves();
//	}

	public void validateMove(Piece selectedPiece, Point newCoordinate) {
		// check which side turn it is
//		if (selectedPiece.isWhite != sideToMove) {
//			return;
//		}		
//		ArrayList<Move> pieceMoves = selectedPiece.getPossibleMoves();
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
			// check if castle is not available anymore
			if (whiteCanShortCastle || whiteCanLongCastle || blackCanShortCastle || blackCanLongCastle) {
				checkCastle(newMove);
			}

			newMove.makeMove();
			this.sideToMove = this.sideToMove.getOppositeSide(); // the next turn
		} else {
			selectedPiece.setRealPosition();
			System.out.println("The " + selectedPiece + " can't move to " + newCoordinate + ".");
		}
	}

	private void checkCastle(Move validatedMove) {
		Piece movedPiece = validatedMove.movedPiece;
		if (movedPiece.side.isWhite()) {
			// right Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(7, 0)))) {
				whiteCanShortCastle = false;
				System.out.println("whiteCanShortCastle " + whiteCanShortCastle);
			}
			// left Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(0, 0)))) {
				whiteCanLongCastle = false;
				System.out.println("whiteCanLongCastle " + whiteCanLongCastle);
			}
		}

		if (movedPiece.side.isBlack()) {
			// right Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(7, 7)))) {
				blackCanShortCastle = false;
				System.out.println("blackCanShortCastle " + blackCanShortCastle);
			}
			// left Rook or the king moved
			if (movedPiece instanceof King
					|| (movedPiece instanceof Rook && movedPiece.coordinate.equals(new Point(0, 7)))) {
				blackCanLongCastle = false;
				System.out.println("blackCanLongCastle " + blackCanLongCastle);
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
		String state = fen.substring(fen.indexOf(" ") + 1); // state of the game TODO (e.g. white to move)
		String[] ranks = squares.split("/"); // every rank is divided with "/"

		int rank = 7; // reading starts from the 8th rank
		for (String r : ranks) {
			int file = 0; // reading starts from the first file
			for (int i = 0; i < r.length(); i++) {
				char c = r.charAt(i);
				if (Character.isDigit(c)) {
					file += Character.digit(c, 10);
				} else {
					createNewPiece(new Point(file, rank), c);
					file++;
				}
			}
			rank--; // step to the next rank
		}
	}

	/**
	 * Crate a new piece at the given coordinate
	 * 
	 * @param coordinate
	 * @param pieceType  The type of the piece
	 */
	public static void createNewPiece(Point coordinate, char pieceType) {
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
