package chess;

import java.awt.Color;
import java.security.InvalidParameterException;
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
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	
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
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
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
		
		ChessPiece movePiece = (ChessPiece)board.piece(target);
		
		//Special Move promotion.
		
		promoted = null;
		if(movePiece instanceof Pawn) {
			if((movePiece.getColorPiece() == color.WHITE && target.getRow() == 0) || (movePiece.getColorPiece() == color.BLACK && target.getRow() == 7)){
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if(testCheckMate(opponent(currentPlayer))){
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		// SpecialMove en passant
		
		if ( movePiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)){
			enPassantVulnerable = movePiece;			
		}		
		else {
			enPassantVulnerable = null;
			
		}
		return (ChessPiece)capturedPiece;
	}
	
	 	public ChessPiece replacePromotedPiece(String type) {
	 		if(promoted == null) {
	 			throw new IllegalStateException("There is no piece to be protected.");
	 			}
	 		if(type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
	 			throw new InvalidParameterException("Invalid type for promotion.");
	 		}
	 		Position pos = promoted.getChessPosition().toPosition();
	 		Piece p = board.removePiece(pos);
	 		piecesOnTheBoard.remove(p);
	 		
	 		ChessPiece newPiece = newPiece(type, promoted.getColorPiece());
	 		board.placePiece(newPiece, pos);
	 		piecesOnTheBoard.add(newPiece);
	 		
	 		return newPiece;
	 	}
	 	
	 	private ChessPiece newPiece(String type, color color) {
	 		if(type.equals("B")) return new Bishop(board, color);
	 		if(type.equals("N")) return new Knight(board, color);
	 		if(type.equals("Q")) return new Queen(board, color);
	 		return new Rook(board, color);	 		
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
		
		//Special move castling kingside Rook.
		
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position (source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position (source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceRook);
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}
		
		//Special move castling queengside Rook.
		
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position (source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position (source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceRook);
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}
		
		//SpecialMove en passant.
		
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if(p.getColorPiece() == color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				}
				else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	//Método para retroceder as peças.
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);		
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		//Special move castling kingside Rook
		
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position (source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position (source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetRook);
			board.placePiece(rook, sourceRook);
			rook.decreaseMoveCount();
		}
		
		//Special move castling queengside Rook
		
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position (source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position (source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetRook);
			board.placePiece(rook, sourceRook);
			rook.decreaseMoveCount();
		}
		
		//SpecialMove en passant.
		
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if(p.getColorPiece() == color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				}
				else {
					pawnPosition = new Position(4, target.getColumn());
				}
				board.placePiece(pawn, pawnPosition);
			}
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
		placeNewPiece('a', 2, new Pawn(board, color.WHITE, this));
		
		
		placeNewPiece('b', 1, new Pawn(board, color.WHITE, this));
		placeNewPiece('b', 2, new Knight(board, color.WHITE));
		
		placeNewPiece('c', 2, new Knight(board, color.WHITE));		
        placeNewPiece('c', 1, new Bishop(board, color.WHITE));
        
        placeNewPiece('d', 1, new Queen(board, color.WHITE));		
        placeNewPiece('d', 2, new Pawn(board, color.WHITE, this));
        
        
        placeNewPiece('e', 1, new King(board, color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, color.WHITE, this));
        
        placeNewPiece('f', 1, new Bishop(board, color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, color.WHITE, this));
        
        placeNewPiece('h', 1, new Rook(board, color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, color.WHITE, this));

        
        placeNewPiece('a', 8, new Rook(board, color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, color.BLACK, this));
        
        placeNewPiece('c', 8, new Bishop(board, color.BLACK));
        
        placeNewPiece('b', 7, new Pawn(board, color.BLACK, this));
        placeNewPiece('b', 8, new Knight(board, color.BLACK));
        
        placeNewPiece('d', 8, new Queen(board, color.BLACK));
        
        placeNewPiece('e', 8, new King(board, color.BLACK, this));
        
        placeNewPiece('f', 8, new Bishop(board, color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, color.BLACK, this));
        
        
        placeNewPiece('h', 8, new Rook(board, color.BLACK));
       
       
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}