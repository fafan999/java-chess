import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Piece {
	public Point coordinate; // position on the chessboard
	public int xRealPosition; // position when it is dragged
	public int yRealPosition;
	protected int index = 0; // index of the white king image (first row)
	public final Alliance side; // the side the piece belongs to
	public String name; // type of the piece
	public LinkedList<Piece> allPieces = new LinkedList<Piece>(); // tracking all the pieces

	public Piece(Point coordinate, Alliance side, LinkedList<Piece> allPieces) {
		this.coordinate = coordinate;
//		board.setRealPosition(this);
		this.side = side;
		if (Alliance.BLACK == side) {
			// index of the black king image (second row)
			this.index += 6;
		}

		this.allPieces = allPieces;
		this.setRealPosition();
		this.allPieces.add(this); // add the piece to the rest of the pieces
	}

	public abstract ArrayList<Move> getPossibleMoves();

	public void take() {
		this.allPieces.remove(this);
	}

	@Override
	public String toString() {
		return side.toString().toLowerCase() + " " + name + " at " + coordinateToString();
	}

	public String coordinateToString() {
		String file = "abcdefgh";
		file = String.valueOf(file.charAt(this.coordinate.x));
		return file + (coordinate.y + 1);
	}

	/**
	 * Set the piece position by coordinate
	 */
	public void setRealPosition() {
		this.xRealPosition = this.coordinate.x * ChessBoard.SIZE_OF_SQUARE;
		// we count rank from bottom of the table
		this.yRealPosition = (7 - this.coordinate.y) * ChessBoard.SIZE_OF_SQUARE;
	}

//	public static boolean leavesKingInCheck(Alliance side, LinkedList<Piece> allPieces) {
//		LinkedList<Piece> nextAllPieces = (LinkedList<Piece>) allPieces.clone();
//		
//		
//
//		return false;
//	}

	/**
	 * Check if the given coordinate is on the board
	 * 
	 * @param coordinate
	 * @return true if the coordinate is on the board, false otherwise
	 */
	public static final boolean isOnBoard(Point coordinate) {
		if (0 <= coordinate.x && coordinate.x <= 7 && 0 <= coordinate.y && coordinate.y <= 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the piece by coordinate from the list of pieces
	 * 
	 * @param coordinate
	 * @param allPieces
	 * @return piece at the given coordinate
	 */
	public static Piece getPiece(Point coordinate, LinkedList<Piece> allPieces) {
		for (Piece p : allPieces) {
			if (p.coordinate.equals(coordinate)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Get the king of the given side from the list of pieces
	 * 
	 * @param side
	 * @param allPieces list of the pieces
	 * @return king of the given side
	 */
	public static Piece getKing(Alliance side, LinkedList<Piece> allPieces) {
		for (Piece p : allPieces) {
			if (p.side.equals(side) && p instanceof King) {
				return p;
			}
		}
		System.err.println("There is no " + side.toString().toLowerCase() + " king on the board: Not a valid game");
		return null;
	}

	/**
	 * Get the pieces of the opponent of the given side from the list of pieces
	 * 
	 * @param side
	 * @param allPieces list of pieces of both players
	 * @return pieces of the opponent
	 */
	public static ArrayList<Piece> getOppositePieces(Alliance side, LinkedList<Piece> allPieces) {
		ArrayList<Piece> oppositePieces = new ArrayList<Piece>();
		for (Piece p : allPieces) {
			if (!p.side.equals(side)) {
				oppositePieces.add(p);
			}
		}
		return oppositePieces;
	}

	/**
	 * Check if the king of the given side is in check in the current position
	 * 
	 * @param side
	 * @param allPieces
	 * @return true if the king is in check, false otherwise
	 */
	public static boolean isInCheck(Alliance side, LinkedList<Piece> allPieces) {
		ArrayList<Piece> oppositePieces = Piece.getOppositePieces(side, allPieces);
		Piece king = Piece.getKing(side, allPieces);
		for (Piece p : oppositePieces) {
			if (Piece.containsCoordinate(p.getPossibleMoves(), king.coordinate)) {
				System.out.println(
						"The" + side.toString().toLowerCase() + " is in check at" + king.coordinate.toString());
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the given coordinate is in list of moves destination
	 * 
	 * @param pieceMoves
	 * @param coordinate
	 * @return true if the list of moves contains the given coordinate, false
	 *         otherwise
	 */
	public static boolean containsCoordinate(final ArrayList<Move> pieceMoves, final Point coordinate) {
		return pieceMoves.stream().filter(o -> o.destinationCoordiante.equals(coordinate)).findFirst().isPresent();
	}

}
