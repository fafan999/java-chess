import java.awt.Point;
import java.util.ArrayList;

public abstract class Piece {
	public Point coordinate;		//position on the chessboard
	public int xRealPosition;		//position when it is dragged
	public int yRealPosition;
	public int index = 0;			//index of the white king image (first row)
	public final Alliance side;		//the side the piece belongs to
	public String name;				//type of the piece
	public final ChessBoard board;	//chessboard which contains the piece
		
	public Piece(Point coordinate, Alliance side, ChessBoard board) {
		this.coordinate = coordinate;
		board.setRealPosition(this);
		this.side = side;
		if (Alliance.BLACK == side) {this.index +=6;}	//index of the black king image (second row)
		this.board = board;
		board.allPieces.add(this); 			//add the piece position to the positions list
	}

	public abstract ArrayList<Point> getPossibleMoves();

	public void move(Point newCoordinate) {		
		// take the other piece if it is on this coordinate
		Piece otherPiece = board.getPiece(newCoordinate);
		if(otherPiece!=null) {
			otherPiece.take();		
		}
		
		// remove the piece if en passant move
		if (this.name.equals("pawn")) {
			int direction = (this.side==Alliance.WHITE)? 1:-1;	
			if (newCoordinate.equals(board.enPassantSquare)) {
				board.allPieces.remove(board.getPiece(new Point(board.enPassantSquare.x,board.enPassantSquare.y-direction)));
			}
		}
		
		// moving the piece to the new position
		this.coordinate.setLocation(newCoordinate);
		board.setRealPosition(this);
	}
	
	
	public void take() {
		board.allPieces.remove(this);
	}

	@Override
	public String toString() {
		return side.toString().toLowerCase() + " " + name + " at " + coordinateToString();
	}
	
	public String coordinateToString() {
		String file = "abcdefgh";
		file = String.valueOf(file.charAt(this.coordinate.x));
		return file+(coordinate.y+1);
	}
	
}
