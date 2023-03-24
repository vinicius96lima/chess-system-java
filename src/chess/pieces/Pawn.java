// *Lembrete:
// O peão pode se mover somente uma vez para frente, com excessçao se esse for seu 
// primeiro movimento que ai ele pode se mover duas casas.

package chess.pieces;

import boardGame.Board;
import boardGame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.color;

public class Pawn extends ChessPiece{
	
	private ChessMatch chessMatchPawn;
	
	public Pawn(Board board, color colorPawn, ChessMatch chessMatch) {
		super(board, colorPawn);
		this.chessMatchPawn = chessMatch;
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
			
			//SpecialMove en passant white.
			
			if(position.getRow() == 3) {
				Position left = new Position( position.getRow(), position.getColumn() - 1 );
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatchPawn.getEnPassantVulnerable()) {
					mat[left.getRow() - 1][left.getColumn()] = true;
				}
				Position right = new Position( position.getRow(), position.getColumn() + 1 );
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatchPawn.getEnPassantVulnerable()) {
					mat[right.getRow() - 1][right.getColumn()] = true;
				}
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
		
			
			//SpecialMove en passant black.
			
			if(position.getRow() == 4) {
				Position left = new Position( position.getRow(), position.getColumn() - 1 );
				if(getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatchPawn.getEnPassantVulnerable()) {
					mat[left.getRow() + 1][left.getColumn()] = true;
				}
				Position right = new Position( position.getRow(), position.getColumn() + 1 );
				if(getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatchPawn.getEnPassantVulnerable()) {
					mat[right.getRow() + 1][right.getColumn()] = true;
				}
			}
				
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}
}
