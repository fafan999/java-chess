import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

	public Pawn(Point coordinate, Alliance side) {
		super(coordinate, side);
		this.name = "pawn";
		this.index += 5;
	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Piece firstOtherPiece;
		Piece secondOtherPiece;
		Point checkPoint = (Point) this.coordinate.clone();
		int direction = this.side.getDirection();
		int startingRank = this.side.getStartingRank();

		// move one square forward
		checkPoint.translate(0, direction);
		firstOtherPiece = Piece.getPiece(checkPoint);
		if (isOnBoard(checkPoint)) {
			if (firstOtherPiece == null) {
				possibleMoves
						.add(new Move.QuietMove(this, (Point) this.coordinate.clone(), (Point) checkPoint.clone()));
			}
		}

		// move two squares forward (double pawn push)
		checkPoint.translate(0, direction);
		secondOtherPiece = Piece.getPiece(checkPoint);
		if (coordinate.y == startingRank && firstOtherPiece == null && secondOtherPiece == null) {
			possibleMoves
					.add(new Move.DoublePawnPush(this, (Point) this.coordinate.clone(), (Point) checkPoint.clone()));
		}

		// capture diagonally
		Point[] capturingSquares = { new Point(coordinate.x + 1, coordinate.y + direction),
				new Point(coordinate.x - 1, coordinate.y + direction) };
		for (Point p : capturingSquares) {
			Piece otherPiece = Piece.getPiece(p);
			if (otherPiece != null) {
				if (otherPiece.side != this.side) {
					possibleMoves.add(new Move.AttackMove(this, (Point) this.coordinate.clone(), p, otherPiece));
				}
			}
			// capture enPassant
			if (p.equals(ChessBoard.enPassantSquare)) {
				otherPiece = Piece.getPiece(new Point(p.x, p.y - direction));
				possibleMoves.add(new Move.AttackMove(this, (Point) this.coordinate.clone(), p, otherPiece));
			}
		}
		return possibleMoves;
	}
}
