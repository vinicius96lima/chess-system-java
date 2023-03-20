package chess;

import boardGame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRowsBoard()][board.getColumnBoard()];
		for(int r=0; r<board.getRowsBoard(); r++) {
			for( int c=0; c<board.getColumnBoard(); c++) {
				mat[r][c] = (ChessPiece)board.piece(r, c);
			}
		}
		return mat;
	}	
	
	private void placeNewPiece(char coloumn, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(coloumn, row).toPosition());
	}
	
	
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, color.BLACK));
		placeNewPiece('e', 8, new King(board, color.WHITE));
		placeNewPiece('c', 5, new Rook(board, color.BLACK));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}