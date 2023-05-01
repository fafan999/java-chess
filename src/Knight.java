import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class Knight extends Piece {
	private static final Point[] ALL_SQUARES = { new Point(2, 1), new Point(2, -1), new Point(-2, 1), new Point(-2, -1),
			new Point(1, 2), new Point(-1, 2), new Point(1, -2), new Point(-1, -2) };

	public Knight(Point coordinate, Alliance side, LinkedList<Piece> allPieces) {
		super(coordinate, side, allPieces);
		this.name = "knight";
		this.index += 3;
	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Point checkPoint;

		for (Point p : ALL_SQUARES) {
			checkPoint = (Point) this.coordinate.clone();
			checkPoint.translate(p.x, p.y);
			if (isOnBoard(checkPoint)) {
				Piece otherPiece = Piece.getPiece(checkPoint, allPieces);
				if (otherPiece != null) {
					if (otherPiece.side != this.side) {
						// If it's not the same color
						possibleMoves.add(new Move.AttackMove(this, (Point) this.coordinate.clone(),
								(Point) checkPoint.clone(), otherPiece));
					}
				} else {
					// If there isn't any piece
					possibleMoves
							.add(new Move.QuietMove(this, (Point) this.coordinate.clone(), (Point) checkPoint.clone()));
				}
			}
		}
		return possibleMoves;
	}

}
