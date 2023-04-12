import java.awt.Point;
import java.util.ArrayList;

public class King extends Piece{

	public King(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "king";
		this.index +=0;
	}
	
	@Override
	public ArrayList<Point> getPossibleMoves() {
		ArrayList<Point> possibleMoves = new ArrayList<Point>();
		Point checkPoint;
		Point[] allSquares = {new Point(1,0), new Point(1,-1), new Point(1,1), new Point(-1,0),
							  new Point(0,1), new Point(-1,1), new Point(-1,-1), new Point(0,-1)};
		
		for (Point p: allSquares) {
			checkPoint = (Point) this.coordinate.clone();
			checkPoint.translate(p.x, p.y);
			if(ChessBoard.isOnBoard(checkPoint)) {
				Piece otherPiece = board.getPiece(checkPoint);
				if(otherPiece != null) {
					if(otherPiece.side != this.side) {
						possibleMoves.add(checkPoint); //If it's not the same color
					}
				} else {
					possibleMoves.add(checkPoint); //If there isn't any piece
				}
			}
		}
		return possibleMoves;
	}
}
