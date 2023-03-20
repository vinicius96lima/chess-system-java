package boardGame;

public class Board {
	
	private int rowsBoard;
	private int columnBoard;
	private Piece[][] pieces;
	
	public Board(int rowsBoard, int columnBoard) {
		if (rowsBoard < 1 || columnBoard <1) {
			throw new boardException("Error creating board: There must be at least 1 row and 1 column.");
		}
		this.rowsBoard = rowsBoard;
		this.columnBoard = columnBoard;
		pieces = new Piece[rowsBoard][columnBoard];
	}


	public int getRowsBoard() {
		return rowsBoard;
	}

	public int getColumnBoard() {
		return columnBoard;
	}

	public Piece piece( int row, int column) {
		if(!positionExists(row, column)) {
			throw new boardException("Position not on the board.");
		}
		return pieces [row][column];
	}
	
	public Piece piece ( Position position) {
		if(!positionExists(position)) {
			throw new boardException("Position not on the board.");
		}
		return pieces [position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if(thereIsAPiece(position)) {
			throw new boardException("There is already a piece on position. " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		if(!positionExists(position)) {
			throw new boardException("Position not on board");
		}
		if (piece(position) == null){
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	
	
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rowsBoard && column >=0 && column < columnBoard;
	}
	
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!positionExists(position)) {
			throw new boardException("Position not on the board.");
		}
		return piece(position) != null;
		
	}

}
