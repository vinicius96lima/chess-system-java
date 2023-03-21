package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
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
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece)capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece.");
		}
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	
	private void placeNewPiece(char coloumn, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(coloumn, row).toPosition());
	}
	
	
	
	private void initialSetup() {
	
		placeNewPiece('e', 8, new King(board, color.BLACK));
		placeNewPiece('e', 1, new King(board, color.WHITE));
	    placeNewPiece('d', 8, new King(board, color.BLACK));
        placeNewPiece('d', 1, new King(board, color.WHITE));
        
        placeNewPiece('c', 7, new Rook(board, color.BLACK));
        placeNewPiece('c', 8, new Rook(board, color.BLACK));
        placeNewPiece('d', 7, new Rook(board, color.BLACK));
        placeNewPiece('e', 7, new Rook(board, color.BLACK));
        
        
        placeNewPiece('b', 6, new Rook(board, color.WHITE));    
        placeNewPiece('c', 1, new Rook(board, color.WHITE));
        placeNewPiece('c', 2, new Rook(board, color.WHITE));
        placeNewPiece('d', 2, new Rook(board, color.WHITE));
        placeNewPiece('e', 2, new Rook(board, color.WHITE));
        
 
        
    	

        
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}