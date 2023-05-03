import java.awt.Point;

public abstract class Move {

	final Piece movedPiece;
	final Point startCoordinate;
	final Point destinationCoordiante;
	boolean isValid;

	private Move(final Piece movedPiece, final Point startCoordinate, final Point destinationCoordinate) {
		this.movedPiece = movedPiece;
		this.startCoordinate = startCoordinate;
		this.destinationCoordiante = destinationCoordinate;
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
			ChessBoard.allPieces.add(attackedPiece);
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
		}
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
	}

	public static final class CastleMove extends Move {

		private final Piece rook;
		private final Point rookStartCoordinate;
		private final Point rookDestinationCoordinate;
		private final int rookDirection;

		public CastleMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate, Piece rook) {
			super(movedPiece, startCoordinate, destinationCoordinate);
			if (this.startCoordinate.x < rook.coordinate.x) {
				// right rook (short castle - king side castle)
				this.rookDirection = -1;
			} else {
				// left rook (long castle - queen side castle)
				this.rookDirection = +1;
			}
			this.rook = rook;
			this.rookStartCoordinate = rook.coordinate;
			this.rookDestinationCoordinate = new Point(this.destinationCoordiante.x + this.rookDirection,
					this.destinationCoordiante.y);
		}

		@Override
		public void makeMove() {
			this.movedPiece.coordinate.setLocation(this.destinationCoordiante);
			this.movedPiece.setRealPosition();
			this.rook.coordinate.setLocation(this.rookDestinationCoordinate);
			this.rook.setRealPosition();
		}

		@Override
		public void resetMove() {
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
			this.rook.coordinate.setLocation(this.rookStartCoordinate);
			this.rook.setRealPosition();
		}
	}
}
