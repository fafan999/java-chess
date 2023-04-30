import java.awt.Point;
import java.util.ArrayList;

public class Bishop extends Piece {

	public Bishop(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "bishop";
		this.index += 2;
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
			Piece otherPiece = board.getPiece(checkPoint);
			if (otherPiece != null) {
				if (otherPiece.side == side) {
					break; // if same color, then return
				} else {
					// if different color, then add and return
					possibleMoves.add(
							new Move.AttackMove(this, this.coordinate, (Point) checkPoint.clone(), board, otherPiece));
					break;
				}
			}
			possibleMoves.add(new Move.QuietMove(this, this.coordinate, (Point) checkPoint.clone(), this.board));
			checkPoint.translate(translateX, translateY);
		}

	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();

		// Check diagonal moves to the top left
		checkLine(-1, 1, possibleMoves);
		// Check diagonal moves to the top right
		checkLine(1, 1, possibleMoves);
		// Check diagonal moves to the bottom left
		checkLine(-1, -1, possibleMoves);
		// Check diagonal moves to the bottom right
		checkLine(1, -1, possibleMoves);

		return possibleMoves;
	}

}
