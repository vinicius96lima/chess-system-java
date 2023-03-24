package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;

public abstract class ChessPiece extends Piece{
	
	private color colorPiece;
	private int moveCount;
	
	public ChessPiece(Board board, color colorPiece) {
		super(board);
		this.colorPiece = colorPiece;
	}

	public color getColorPiece() {
		return colorPiece;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColorPiece() != colorPiece;
	}
	
	
	
}
