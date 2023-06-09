import java.awt.Point;
import java.util.ArrayList;

public class Queen extends Piece {

	public Queen(Point coordinate, Alliance side) {
		super(coordinate, side);
		this.name = "queen";
		this.index += 1;
	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		// BISHOP
		// Check diagonal moves to the top left
		checkLine(-1, 1, possibleMoves);
		// Check diagonal moves to the top right
		checkLine(1, 1, possibleMoves);
		// Check diagonal moves to the bottom left
		checkLine(-1, -1, possibleMoves);
		// Check diagonal moves to the bottom right
		checkLine(1, -1, possibleMoves);

		// ROOK
		// Check moves towards top
		checkLine(0, 1, possibleMoves);
		// Check moves towards bottom
		checkLine(0, -1, possibleMoves);
		// Check moves towards left
		checkLine(-1, 0, possibleMoves);
		// Check moves towards right
		checkLine(1, 0, possibleMoves);

		return possibleMoves;
	}

	/**
	 * Check moves in a line
	 * 
	 * @param translateX    x step of the line
	 * @param translateY    y step of the line
	 * @param possibleMoves The container of the possible moves
	 */
	private void checkLine(int translateX, int translateY, ArrayList<Move> possibleMoves) {
		Point checkPoint;
		// Check moves in a line
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(translateX, translateY);
		while (isOnBoard(checkPoint)) {
			Piece otherPiece = Piece.getPiece(checkPoint);
			if (otherPiece != null) {
				if (otherPiece.side == side) {
					break; // if same color, then return
				} else {
					// if different color, then add and return
					possibleMoves.add(new Move.AttackMove(this, (Point) this.coordinate.clone(),
							(Point) checkPoint.clone(), otherPiece));
					break;
				}
			}
			possibleMoves.add(new Move.QuietMove(this, (Point) this.coordinate.clone(), (Point) checkPoint.clone()));
			checkPoint.translate(translateX, translateY);
		}

	}

}
