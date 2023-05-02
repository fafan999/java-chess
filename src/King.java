import java.awt.Point;
import java.util.ArrayList;

public class King extends Piece {
	private static final Point[] ALL_SQUARES = { new Point(1, 0), new Point(1, -1), new Point(1, 1), new Point(-1, 0),
			new Point(0, 1), new Point(-1, 1), new Point(-1, -1), new Point(0, -1) };

	public King(Point coordinate, Alliance side) {
		super(coordinate, side);
		this.name = "king";
		this.index += 0;
	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Point checkPoint;

		for (Point p : ALL_SQUARES) {
			checkPoint = (Point) this.coordinate.clone();
			checkPoint.translate(p.x, p.y);
			if (isOnBoard(checkPoint)) {
				Piece otherPiece = Piece.getPiece(checkPoint);
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

	public Move getCastleMoves() {
		// castle short
		if (this.side.getShortCastle()) {
			boolean canCastle = true;
			int x = this.coordinate.x;
			int y = this.coordinate.y;
			Point[] shortCastleSquares = { new Point(x + 1, y), new Point(x + 2, y) };
			for (Point p : shortCastleSquares) {
				// if there is a piece between the king and the rook, castle is forbidden
				// if any of the coordinates is in check, castle is forbidden
				if (Piece.getPiece(p) != null || Piece.isCoordinateInCheck(this.side, p)
						|| Piece.isKingInCheck(this.side)) {
					canCastle = false;
					break;
				}
			}
			if (canCastle) {
				Piece rook = Piece.getPiece(new Point(this.coordinate.x + 3, this.coordinate.y));
				return new Move.ShortCastleMove(this, (Point) this.coordinate.clone(),
						new Point(this.coordinate.x + 2, this.coordinate.y), rook);
			}
			// TODO: add long castle (The castle move can be modified)
		}
		return null;
	}
}
