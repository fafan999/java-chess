import java.awt.Point;
import java.util.ArrayList;

public class Bishop extends Piece {

	public Bishop(Point coordinate, Alliance side, ChessBoard board) {
		super(coordinate, side, board);
		this.name = "bishop";
		this.index += 2;
	}

	@Override
	public ArrayList<Point> getPossibleMoves() {
		ArrayList<Point> possibleMoves = new ArrayList<Point>();
		Point checkPoint;
		// Check diagonal moves to the top left
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(-1, 1);
		while(checkPoint.x>=0 && checkPoint.y<=7) {
			Piece otherPiece = board.getPiece(checkPoint);
			if (otherPiece!=null) {
				if (otherPiece.side == side) {
					break; //if same color, then return					
				} else {
					possibleMoves.add((Point) checkPoint.clone()); //if different color, then add and return
					break;
				}
			}
			possibleMoves.add((Point) checkPoint.clone());
			checkPoint.translate(-1, 1);
		}
		
		// Check diagonal moves to the top right
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(1, 1);
		while(checkPoint.x<=7 && checkPoint.y<=7) {
			Piece otherPiece = board.getPiece(checkPoint);
			if (otherPiece!=null) {
				if (otherPiece.side == side) {
					break; //if same color, then return
				} else {
					possibleMoves.add((Point) checkPoint.clone()); //if different color, then add and return
					break;
				}
			}
			possibleMoves.add((Point) checkPoint.clone());
			checkPoint.translate(1, 1);
		}
		
		// Check diagonal moves to the bottom left
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(-1, -1);
		while(checkPoint.x>=0 && checkPoint.y>=0) {
			Piece otherPiece = board.getPiece(checkPoint);
			if (otherPiece!=null) {
				if (otherPiece.side == side) {
					break; //if same color, then return
				} else {
					possibleMoves.add((Point) checkPoint.clone()); //if different color, then add and return
					break;
				}
			}
			possibleMoves.add((Point) checkPoint.clone());
			checkPoint.translate(-1, -1);
		}
		
		// Check diagonal moves to the bottom right
		checkPoint = (Point) this.coordinate.clone();
		checkPoint.translate(1, -1);
		while(checkPoint.x<=7 && checkPoint.y>=0) {
			Piece otherPiece = board.getPiece(checkPoint);
			if (otherPiece!=null) {
				if (otherPiece.side == side) {
					break; //if same color, then return
				} else {
					possibleMoves.add((Point) checkPoint.clone()); //if different color, then add and return
					break;
				}
			}
			possibleMoves.add((Point) checkPoint.clone());
			checkPoint.translate(1, -1);
		}		
		
		return possibleMoves;
	}
	
	
	

}
