import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {
	private static final Point[] ALL_SQUARES = { new Point(2, 1), new Point(2, -1), new Point(-2, 1), new Point(-2, -1),
			new Point(1, 2), new Point(-1, 2), new Point(1, -2), new Point(-1, -2) };

	public Knight(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
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
			if (ChessBoard.isOnBoard(checkPoint)) {
				Piece otherPiece = board.getPiece(checkPoint);
				if (otherPiece != null) {
					if (otherPiece.side != this.side) {
						// If it's not the same color
						possibleMoves
								.add(new Move.AttackMove(this, this.coordinate, checkPoint, this.board, otherPiece));
					}
				} else {
					// If there isn't any piece
					possibleMoves.add(new Move.QuietMove(this, this.coordinate, checkPoint, board));
				}
			}
		}
		return possibleMoves;
	}

}
