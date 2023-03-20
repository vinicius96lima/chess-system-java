package chess.pieces;

import boardGame.Board;
import chess.ChessPiece;
import chess.color;

public class King extends ChessPiece {

	public King(Board board, color colorPiece) {
		super(board, colorPiece);

	}

	@Override
	public String toString() {
		return "K";
	}

}

