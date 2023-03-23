package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	private int turn;
	private color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
		turn = 1;
		currentPlayer = color.WHITE;
		check = false;
	}
	
	public int getTurn() {
		return turn;
		
	}
	public color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
	
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	//Método para verificar se a peça foi capturada, e se está em check.
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
				throw new ChessException("You can not put yourself in check.");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))){
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	//Método para validar as posições das peças.
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColorPiece()) {
			throw new ChessException("The chosen piece is not yours");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece.");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	
	//Método para mover as peças
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
			
		}
		return capturedPiece;
	}
	
	//Método para retroceder as peças.
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.dereaseMoveCount();
		board.placePiece(p, source);		
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	//Metodo para troca de turno
	
	private void nextTurn() {
		turn ++;
		currentPlayer = (currentPlayer == color.WHITE) ? color.BLACK : color.WHITE;
	}
	
	private color opponent(color colorOponent) {
		return (colorOponent == color.WHITE)? color.BLACK : color.WHITE;
	}
	
	private ChessPiece king(color colorKing) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColorPiece() == colorKing).collect(Collectors.toList());
		for(Piece p : list) {
			if(p instanceof King) {
				return(ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no "+ colorKing + "king on the borad");
	}
	
	//Método para testar a cor.
	
	private boolean testCheck(color colorCheck) {
		Position kingPosition = king(colorCheck).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColorPiece() == opponent(colorCheck)).collect(Collectors.toList());
		for(Piece p: opponentPieces) {
			boolean [][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;		
	}
	
	private boolean testCheckMate(color colorTestCheck) {
		if(!testCheck(colorTestCheck)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColorPiece() == colorTestCheck).collect(Collectors.toList());
		for(Piece p : list) {
			boolean [][] mat = p.possibleMoves();
			for(int i=0; i<board.getRowsBoard(); i++) {
				for(int j=0; j<board.getColumnBoard(); j++) {
					if(mat[i][j]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i,j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(colorTestCheck);
						undoMove(source, target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void placeNewPiece(char coloumn, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(coloumn, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	
	
	private void initialSetup() {
	
		placeNewPiece('a', 1, new Rook(board, color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, color.WHITE));
		
		placeNewPiece('b', 1, new Pawn(board, color.WHITE));
		placeNewPiece('b', 2, new Knight(board, color.WHITE));
		
		placeNewPiece('c', 2, new Knight(board, color.WHITE));		
        placeNewPiece('c', 1, new Bishop(board, color.WHITE));
        
        placeNewPiece('d', 2, new Queen(board, color.WHITE));		
        placeNewPiece('d', 1, new Queen(board, color.WHITE));
        
        
        placeNewPiece('e', 1, new King(board, color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, color.WHITE));
        
        placeNewPiece('h', 1, new Rook(board, color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, color.WHITE));

        
        placeNewPiece('a', 8, new Rook(board, color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, color.BLACK));
        
        placeNewPiece('c', 8, new Bishop(board, color.BLACK));
        
        placeNewPiece('b', 7, new Pawn(board, color.BLACK));
        placeNewPiece('b', 8, new Knight(board, color.BLACK));
        
        placeNewPiece('d', 8, new Queen(board, color.BLACK));
        
        placeNewPiece('e', 8, new King(board, color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, color.BLACK));
        placeNewPiece('h', 8, new Rook(board, color.BLACK));
       
       
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}