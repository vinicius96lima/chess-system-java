package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.color;

public class King extends ChessPiece {
	
	
	private ChessMatch chessMatch;
	
	public King(Board board, color colorPiece, ChessMatch chessMatch) {
		super(board, colorPiece);
		this.chessMatch = chessMatch;

	}

	@Override
	public String toString() {
		return "K";
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece p =(ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColorPiece() == getColorPiece() && p.getMoveCount() == 0;
	}
	
	
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColorPiece() !=getColorPiece();
		
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [][] mat = new boolean[getBoard().getRowsBoard()][getBoard().getColumnBoard()];
		
		Position p = new Position(0,0);
		
		//Above
		p.setValues(position.getRow() -1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Bellow
		p.setValues(position.getRow() +1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Left
		p.setValues(position.getRow(), position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Right
		p.setValues(position.getRow(), position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//DiagonalLeft
		p.setValues(position.getRow() -1, position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//DiagonalRight
		p.setValues(position.getRow() -1, position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//DiagonalBellowLeft
		p.setValues(position.getRow() +1, position.getColumn()+1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//DiagonalBellowRight
		p.setValues(position.getRow() +1, position.getColumn()-1);
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// SpecialMoveCastling
		
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			// SpecialMove Castlgin kingside Rook
			Position posRook1 = new Position (position.getRow(), position.getColumn() + 3);
			if ( testRookCastling(posRook1)) {
				Position p1 = new Position (position.getRow(), position.getColumn() + 1); // Verificando se a primeira casa está vazia para realizar o movimento.
				Position p2 = new Position (position.getRow(), position.getColumn() + 2); // Verificando se a primeira casa está vazia para realizar o movimento.
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat [position.getRow()][position.getColumn() + 2] = true;
				}
			}			
		}
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			// SpecialMove Castlgin kingside Rook
			Position posRook2 = new Position (position.getRow(), position.getColumn() - 4);
			if ( testRookCastling(posRook2)) {
				Position p1 = new Position (position.getRow(), position.getColumn() - 1); // Verificando se a primeira casa está vazia para realizar o movimento.
				Position p2 = new Position (position.getRow(), position.getColumn() - 2); // Verificando se a primeira casa está vazia para realizar o movimento.
				Position p3 = new Position (position.getRow(), position.getColumn() - 3); // Verificando se a primeira casa está vazia para realizar o movimento.
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat [position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}

		
		return mat;
	}

}

