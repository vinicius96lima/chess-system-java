package chess.pieces;

import boardGame.Board;
import chess.ChessPiece;
import chess.color;

public class Rook extends ChessPiece{

	public Rook(Board board, color colorPiece) {
		super(board, colorPiece);
		
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean[getBoard().getRowsBoard()][getBoard().getColumnBoard()];
		return mat;
	}
	

}
