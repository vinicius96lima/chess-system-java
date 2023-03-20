package chess;

import boardGame.Board;
import boardGame.Piece;

public class ChessPiece extends Piece{
	
	private color colorPiece;
	
	public ChessPiece(Board board, color colorPiece) {
		super(board);
		this.colorPiece = colorPiece;
	}

	public color getColorPiece() {
		return colorPiece;
	}
	
}
