import java.awt.Point;
import java.util.ArrayList;

public abstract class Piece {
	public Point coordinate; // position on the chessboard
	public int xRealPosition; // position when it is dragged
	public int yRealPosition;
	int index = 0; // index of the white king image (first row)
	public final Alliance side; // the side the piece belongs to
	public String name; // type of the piece

	public Piece(Point coordinate, Alliance side) {
		this.coordinate = coordinate; // x -> file, y+1 -> rank
		this.side = side;
		if (side.isBlack()) {
			// index of the black king image (second row)
			this.index += 6;
		}
		this.setRealPosition();
		ChessBoard.allPieces.add(this); // add the piece to the rest of the pieces
	}

	/**
	 * Calculate the piece pseudo legal moves without validating them *
	 * 
	 * @return
	 */
	public abstract ArrayList<Move> getPossibleMoves();

	/**
	 * Remove the piece from the board
	 */
	public void take() {
		ChessBoard.allPieces.remove(this);
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
	 * @return piece at the given coordinate
	 */
	public static final Piece getPiece(Point coordinate) {
		for (Piece p : ChessBoard.allPieces) {
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
	public static Piece getKing(Alliance side) {
		for (Piece p : ChessBoard.allPieces) {
			if (p.side.equals(side) && p instanceof King) {
				return p;
			}
		}
		System.err.println("There is no " + side.toString().toLowerCase() + " king on the board: Not a valid game");
		return null;
	}

	/**
	 * Get the pieces of the given side from the list of pieces
	 * 
	 * @param side
	 * @return pieces of the given side
	 */
	public static ArrayList<Piece> getSidePieces(Alliance side) {
		ArrayList<Piece> sidePieces = new ArrayList<Piece>();
		for (Piece p : ChessBoard.allPieces) {
			if (p.side.equals(side)) {
				sidePieces.add(p);
			}
		}
		return sidePieces;
	}

	/**
	 * Check if the king of the given side is in check in the current position
	 * 
	 * @param side
	 * @return true if the king is in check, false otherwise
	 */
	public static boolean isKingInCheck(Alliance side) {
		Piece king = Piece.getKing(side);
		if (isCoordinateInCheck(king.side, king.coordinate)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the given coordinate can be reached by a piece of the opponent
	 * 
	 * @param side
	 * @param coordinate
	 * @return true if the coordinate is in check, false otherwise
	 */
	public static boolean isCoordinateInCheck(Alliance side, Point coordinate) {
		ArrayList<Piece> oppositePieces = Piece.getSidePieces(side.getOppositeSide());
		for (Piece p : oppositePieces) {
			if (Piece.containsCoordinate(p.getPossibleMoves(), coordinate)) {
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
