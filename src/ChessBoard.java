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
	private static Image[] pieceImages = new Image[12];
	private Piece selectedPiece = null;
	public LinkedList<Piece> allPieces = new LinkedList<>(); // list for tracking the pieces
	public final int SIZE_OF_SQUARE; // determine how big the board is
	private Alliance sideToMove = Alliance.WHITE; // white starts
	public Point doublePawnPushSquare = null; // if it's a double pawn push
	public Point enPassantSquare = null; // en passant square

	public ChessBoard(int SIZE_OF_SQUARE) {
		super();
		this.SIZE_OF_SQUARE = SIZE_OF_SQUARE;
		positionFromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR "); // initial position
		// get the images of the pieces ---------------------------------
		try {
//			BufferedImage all = ImageIO.read(new File("chess.png")); 			
			BufferedImage all = ImageIO.read(new File("chess_set_SZB.png"));
			int index = 0;
			for (int y = 0; y < 400; y += 200) {
				for (int x = 0; x < 1200; x += 200) {
					pieceImages[index] = all.getSubimage(x, y, 200, 200).getScaledInstance(SIZE_OF_SQUARE,
							SIZE_OF_SQUARE, BufferedImage.SCALE_SMOOTH);
					index++;
				}
			}
		} catch (Exception e) {
			System.err.println("Nem sikerült a bábuk képének beolvasása");
		} // -------------------------------------------------------------

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				selectedPiece = getPiece(new Point(e.getX() / SIZE_OF_SQUARE, 7 - e.getY() / SIZE_OF_SQUARE));
				if (selectedPiece != null) {
					allPieces.remove(selectedPiece);
					allPieces.addLast(selectedPiece); // put it at the last so it is at the top
					if (selectedPiece.side != sideToMove) {
						selectedPiece = null; // check which side turn it is
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
		for (Move m : legalMoves()) {
			Point p = m.getDestinationCoordiante();
			if (getPiece(p) == null) {
				g.fillOval(p.x * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 3, (7 - p.y) * SIZE_OF_SQUARE + SIZE_OF_SQUARE / 3,
						SIZE_OF_SQUARE / 3, SIZE_OF_SQUARE / 3);
			} else {
				// if there is a piece draw triangles at each corner (a little magic)
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
		for (Piece p : allPieces) {
			g.drawImage(pieceImages[p.index], p.xRealPosition, p.yRealPosition, this);
		}

	}

	public ArrayList<Move> legalMoves() {
		ArrayList<Move> legalMoves = new ArrayList<Move>();
		if (selectedPiece != null) {
			legalMoves = selectedPiece.getPossibleMoves();
		} else {
			legalMoves.clear();
		}
		return legalMoves;
	}

	public ArrayList<Move> getPieceMoves(Piece selectedPiece) {
		return selectedPiece.getPossibleMoves();
	}

	public void validateMove(Piece selectedPiece, Point newCoordinate) {
		// check which side turn it is
//		if (selectedPiece.isWhite != sideToMove) {
//			return;
//		}		
		ArrayList<Move> pieceMoves = selectedPiece.getPossibleMoves();
		Move newMove = null;
		// get the new move corresponding to the new coordinate
		for (Move p : pieceMoves) {
			if (p.destinationCoordiante.equals(newCoordinate))
				newMove = p;
		}

//		if (pieceMoves.contains(newCoordinate)) {
//			selectedPiece.move(newCoordinate);
		if (newMove != null) {
			newMove.makeMove();
			// check if there can be en passant move in the next move
			if (this.doublePawnPushSquare != null) {
				if (this.doublePawnPushSquare.equals(newCoordinate)) {
					int direction = selectedPiece.side == Alliance.WHITE ? 1 : -1;
					this.enPassantSquare = new Point(newCoordinate.x, newCoordinate.y - direction);
				} else {
					this.enPassantSquare = null;
					this.doublePawnPushSquare = null;
				}
			}

			this.sideToMove = this.sideToMove == Alliance.WHITE ? Alliance.BLACK : Alliance.WHITE; // the next turn

		} else

		{
			setRealPosition(selectedPiece);
			System.out.println("The " + selectedPiece + " can't move to " + newCoordinate + ".");
		}

	}

	public boolean containsCoordinate(final ArrayList<Move> pieceMoves, final Point coordinate) {
		return pieceMoves.stream().filter(o -> o.getDestinationCoordiante().equals(coordinate)).findFirst().isPresent();
	}

	/**
	 * Get the piece by coordinate
	 * 
	 * @param coordinate
	 * @return
	 */
	public Piece getPiece(Point coordinate) {
		for (Piece p : allPieces) {
			if (p.coordinate.equals(coordinate)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Set the piece position by coordinate
	 * 
	 * @param p
	 */
	public void setRealPosition(Piece p) {
		p.xRealPosition = p.coordinate.x * SIZE_OF_SQUARE;
		p.yRealPosition = (7 - p.coordinate.y) * SIZE_OF_SQUARE; // we count rank from bottom of the table
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
	 * @param coordinate
	 * @param pieceType  The type of the piece
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
			new Pawn(coordinate, side, this);
			break;
		case "r":
			new Rook(coordinate, side, this);
			break;
		case "n":
			new Knight(coordinate, side, this);
			break;
		case "b":
			new Bishop(coordinate, side, this);
			break;
		case "q":
			new Queen(coordinate, side, this);
			break;
		case "k":
			new King(coordinate, side, this);
			break;
		}
	}

	public static final boolean isOnBoard(Point coordinate) {
		if (0 <= coordinate.x && coordinate.x <= 7 && 0 <= coordinate.y && coordinate.y <= 7) {
			return true;
		} else {
			return false;
		}
	}

}
