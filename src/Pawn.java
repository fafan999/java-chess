import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

	public Pawn(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "pawn";
		this.index += 5;
	}

	@Override
	public ArrayList<Point> getPossibleMoves() {
		ArrayList<Point> possibleMoves = new ArrayList<Point>();
		Piece firstOtherPiece;
		Piece secondOtherPiece;
		Point checkPoint = (Point) this.coordinate.clone();
		int direction = Alliance.WHITE == side ? 1 : -1;
		int startingRank = Alliance.WHITE == side ? 1 : 6;

		// move one square forward
		checkPoint.translate(0, direction);
		firstOtherPiece = board.getPiece(checkPoint);
		if (ChessBoard.isOnBoard(checkPoint)) {
			if (firstOtherPiece == null) {
				possibleMoves.add((Point) checkPoint.clone());
			}
		}

		// move two squares forward
		checkPoint.translate(0, direction);
		secondOtherPiece = board.getPiece(checkPoint);
		if (ChessBoard.isOnBoard(checkPoint)) {
			if (coordinate.y == startingRank && firstOtherPiece == null && secondOtherPiece == null) {
				possibleMoves.add((Point) checkPoint.clone());
				board.doublePawnPushSquare = new Point((Point) checkPoint.clone());
			}
		}

		// capture diagonally
		Point[] capturingSquares = { new Point(coordinate.x + 1, coordinate.y + direction),
				new Point(coordinate.x - 1, coordinate.y + direction) };
		for (Point p : capturingSquares) {
			Piece otherPiece = board.getPiece(p);
			if (otherPiece != null) {
				if (otherPiece.side != this.side) {
					possibleMoves.add(p);
				}
			}
			// capture enPassant
			if (board.enPassantSquare != null) {
				if (p.equals(board.enPassantSquare)) {
					possibleMoves.add(p);
				}
			}
		}
		return possibleMoves;
	}
}
