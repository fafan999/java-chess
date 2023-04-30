import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece {

	public Rook(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "rook";
		this.index += 4; // index of the Rook images
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

}
