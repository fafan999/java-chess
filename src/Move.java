import java.awt.Point;
import java.util.LinkedList;

public abstract class Move {

	final Piece movedPiece;
	final Point startCoordinate;
	final Point destinationCoordiante;
	final LinkedList<Piece> allPieces;
	boolean isValid;

	private Move(final Piece movedPiece, final Point startCoordinate, final Point destinationCoordinate) {
		this.movedPiece = movedPiece;
		this.startCoordinate = startCoordinate;
		this.destinationCoordiante = destinationCoordinate;
		this.allPieces = movedPiece.allPieces;
	}

	/**
	 * Execute this move on the board
	 */
	public abstract void makeMove();

	public abstract void resetMove();

//	public abstract boolean leavesKingInCheck();

	public static final class QuietMove extends Move {

		public QuietMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate) {
			super(movedPiece, startCoordinate, destinationCoordinate);
		}

		@Override
		public void makeMove() {
			this.movedPiece.coordinate.setLocation(this.destinationCoordiante);
			this.movedPiece.setRealPosition();
		}

		@Override
		public void resetMove() {
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
		}

	}

	/**
	 * Handling the moves resulting in capture
	 * 
	 * @author superpc
	 */
	public static final class AttackMove extends Move {

		private final Piece attackedPiece;

		public AttackMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate, Piece attackedPiece) {
			super(movedPiece, startCoordinate, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public void makeMove() {
			this.attackedPiece.take();
			this.movedPiece.coordinate.setLocation(this.destinationCoordiante);
			this.movedPiece.setRealPosition();
		}

		@Override
		public void resetMove() {
			this.movedPiece.allPieces.add(attackedPiece);
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
		}

//		@Override
//		public boolean leavesKingInCheck() {
//			boolean isValid;
//			this.makeMove();
//			if (Piece.isInCheck(movedPiece.side, movedPiece.allPieces)) {
//				isValid = false;
//			} else {
//				isValid = true;
//			}
//			this.resetMove();
//			return isValid;
//		}

	}

	/**
	 * Handling the double pawn push moves
	 * 
	 * @author superpc
	 *
	 */
	public static final class DoublePawnPush extends Move {

		public DoublePawnPush(Piece movedPiece, Point startCoordinate, Point destinationCoordinate) {
			super(movedPiece, startCoordinate, destinationCoordinate);
		}

		@Override
		public void makeMove() {
			this.movedPiece.coordinate.setLocation(this.destinationCoordiante);
			this.movedPiece.setRealPosition();
		}

		@Override
		public void resetMove() {
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
		}

//		@Override
//		public boolean leavesKingInCheck() {
//			boolean isValid;
//			this.makeMove();
//			if (Piece.isInCheck(movedPiece.side, movedPiece.allPieces)) {
//				isValid = false;
//			} else {
//				isValid = true;
//			}
//			this.resetMove();
//			return isValid;
//		}

	}

}
