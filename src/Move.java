import java.awt.Point;

public abstract class Move {

	protected final Piece movedPiece;
	protected final Point startCoordinate;
	protected final Point destinationCoordiante;
	protected final ChessBoard board;

	private Move(final Piece movedPiece, final Point startCoordinate, final Point destinationCoordinate,
			final ChessBoard board) {
		this.movedPiece = movedPiece;
		this.startCoordinate = startCoordinate;
		this.destinationCoordiante = destinationCoordinate;
		this.board = board;
	}

	public abstract void makeMove();

	public class QuietMove extends Move {

		public QuietMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate, ChessBoard board) {
			super(movedPiece, startCoordinate, destinationCoordinate, board);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void makeMove() {
			this.movedPiece.coordinate.setLocation(destinationCoordiante);
		}

	}

	public class AttackMove extends Move {

		private final Piece attackedPiece;

		public AttackMove(Piece movedPiece, Point startCoordinate, Point destinationCoordinate, ChessBoard board,
				Piece attackedPiece) {
			super(movedPiece, startCoordinate, destinationCoordinate, board);
			this.attackedPiece = attackedPiece;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void makeMove() {
			this.board.allPieces.remove(this.attackedPiece);
			this.movedPiece.coordinate.setLocation(destinationCoordiante);
		}

	}

}
