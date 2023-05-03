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

	/**
	 * Reset this move on the board
	 */
	public abstract void resetMove();

	/**
	 * Handling basic moves
	 */
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

	/**
	 * Handling castles
	 */
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
			// TODO remove this, as it is not needed, and not correctly implemented
			this.movedPiece.coordinate.setLocation(this.startCoordinate);
			this.movedPiece.setRealPosition();
			this.rook.coordinate.setLocation(this.rookStartCoordinate);
			this.rook.setRealPosition();
		}
	}

	public static final class PromotionMove extends Move {
		private Piece promotionPiece;

		public PromotionMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate) {
			super(movedPiece, startCoordinate, destinationCoordinate);

		}

		@Override
		public void makeMove() {
			ChessBoard.allPieces.remove(this.movedPiece);
			promotionPiece = this.createNewPiece();
		}

		@Override
		public void resetMove() {
			ChessBoard.allPieces.remove(this.promotionPiece);
			ChessBoard.allPieces.add(this.movedPiece);
		}

		private final Piece createNewPiece() {
			return new Queen(this.destinationCoordiante, this.movedPiece.side);
		}
	}

	public static final class AttackPromotionMove extends Move {
		private final Piece attackedPiece;
		private Piece promotionPiece;

		public AttackPromotionMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate,
				Piece attackedPiece) {
			super(movedPiece, startCoordinate, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}

		@Override
		public void makeMove() {
			this.attackedPiece.take();
			ChessBoard.allPieces.remove(this.movedPiece);
			promotionPiece = this.createNewPiece();

		}

		@Override
		public void resetMove() {
			ChessBoard.allPieces.remove(this.promotionPiece);
			ChessBoard.allPieces.add(this.attackedPiece);
			ChessBoard.allPieces.add(this.movedPiece);
		}

		private final Piece createNewPiece() {
			return new Queen(this.destinationCoordiante, this.movedPiece.side);
		}

	}
}
