// *Lembrete:
// O peão pode se mover somente uma vez para frente, com excessçao se esse for seu 
// primeiro movimento que ai ele pode se mover duas casas.

package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessPiece;
import chess.color;

public class Pawn extends ChessPiece{
	
	public Pawn(Board board, color colorPawn) {
		super(board, colorPawn);
	}

	@Override
	public boolean[][] possibleMoves() {
		
		boolean [][] mat = new boolean[getBoard().getRowsBoard()][getBoard().getColumnBoard()];
		Position p = new Position(0,0);
		
		if(getColorPiece() == color.WHITE) {
			p.setValues(position.getRow()-1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()-2, position.getColumn());
			Position p2 = new Position(position.getRow()-1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//DiagonalLeft
			p.setValues(position.getRow() -1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//DiagonalRight
			p.setValues(position.getRow() -1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
	}
		else {
			p.setValues(position.getRow()+1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow()+2, position.getColumn());
			Position p2 = new Position(position.getRow()+1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//DiagonalLeft
			p.setValues(position.getRow() +1, position.getColumn()-1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//DiagonalRight
			p.setValues(position.getRow() +1, position.getColumn()+1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
				
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "p";
	}
}
