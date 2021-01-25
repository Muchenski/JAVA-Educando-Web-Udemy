package boardgame;

public class Piece {

	// Posi��o da pe�a no tabuleiro.
	protected Position position;
	// A pe�a deve conhecer o tabuleiro que est�.
	private Board board;

	public Piece() {

	}

	public Piece(Board board) {
		// Quando a pe�a for criada, ela ainda n�o deve
		// ser colocada no tabuleiro.
		// Ent�o sua posi��o inicial � nula.
		this.position = null;
		this.board = board;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	protected Board getBoard() {
		return board;
	}

}
