import java.awt.Point;

public class Move {

	public final Piece movedPiece;
	public final Point startCoordinate;
	public final Point destinationCoordiante;
	public final ChessBoard board;
	
	public Move(final Piece movedPiece, 
				final Point startCoordinate, 
				final Point destinationCoordinate, 
				final ChessBoard board) {
		this.movedPiece = movedPiece;
		this.startCoordinate = startCoordinate;
		this.destinationCoordiante = destinationCoordinate;
		this.board = board;		
	}
	
	
	
}
