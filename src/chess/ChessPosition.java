package chess;

import boardGame.Position;

public class ChessPosition {
	
	private char columnChessPosition;
	private int rowChessPosition;
	
	public ChessPosition(char columnChessPosition, int rowChessPosition) {
		if(columnChessPosition < 'a' || columnChessPosition > 'h' || rowChessPosition < 1 || rowChessPosition > 8) {
			throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to a8.");
		}
		this.columnChessPosition = columnChessPosition;
		this.rowChessPosition = rowChessPosition;
	}

	
	
	public char getColumnChessPosition() {
		return columnChessPosition;
	}

	public int getRowChessPosition() {
		return rowChessPosition;
	}


	protected Position toPosition() {
		return new Position(8 - rowChessPosition, columnChessPosition - 'a');
	}
		
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + columnChessPosition + rowChessPosition;
	}
	
	
}
