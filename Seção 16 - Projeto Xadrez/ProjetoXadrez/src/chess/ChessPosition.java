package chess;

import boardgame.Position;
import chess.exceptions.ChessException;

// Classe para realizar a "tradu��o" entre
// as posi��es de xadrez e posi��es de matriz.
public class ChessPosition {

	private int row;
	private char column;

	public ChessPosition() {
	}

	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row > 8 || row < 1) {
			throw new ChessException(
					"Erro criando o tabuleiro de Xadrez! Linha e/ou coluna inv�lidos. Tente os valores de a1 at� h8!");
		}
		this.column = column;
		this.row = row;
	}

	protected Position toMatrixPosition() {
		return new Position(8 - row, column - 'a');
	}

	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char) ('a' - position.getColumn()), (8 - position.getRow()));
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	@Override
	public String toString() {
		return "" + column + row;
	}
}
