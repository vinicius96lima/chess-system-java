package boardGame;

public class Board {
	
	private int rowsBoard;
	private int columnBoard;
	private Piece[][] pieces;
	
	public Board(int rowsBoard, int columnBoard) {
		this.rowsBoard = rowsBoard;
		this.columnBoard = columnBoard;
		pieces = new Piece[rowsBoard][columnBoard];
	}


	public int getRowsBoard() {
		return rowsBoard;
	}

	public void setRowsBoard(int rowsBoard) {
		this.rowsBoard = rowsBoard;
	}

	public int getColumnBoard() {
		return columnBoard;
	}

	public void setColumnBoard(int columnBoard) {
		this.columnBoard = columnBoard;
	}
	
	public Piece piece( int row, int column) {
		return pieces [row][column];
	}
	
	public Piece piece ( Position position) {
		return pieces [position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}

}
