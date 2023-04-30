import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece {

	public Rook(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "rook";
		this.index += 4; // index of the Rook images
	}

	@Override
	public ArrayList<Move> getPossibleMoves() {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		Point checkPoint;
		// Check moves towards top
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(0, 1);
		while (checkPoint.x == this.coordinate.x && checkPoint.y <= 7) {
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
			checkPoint.translate(0, 1);
		}

		// Check moves towards bottom
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(0, -1);
		while (checkPoint.x == this.coordinate.x && checkPoint.y >= 0) {
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
			checkPoint.translate(0, -1);
		}

		// Check moves towards left
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(-1, 0);
		while (checkPoint.x >= 0 && checkPoint.y == this.coordinate.y) {
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
			checkPoint.translate(-1, 0);
		}

		// Check moves towards right
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(1, 0);
		while (checkPoint.x <= 7 && checkPoint.y == this.coordinate.y) {
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
			checkPoint.translate(1, 0);
		}

		return possibleMoves;
	}

}
