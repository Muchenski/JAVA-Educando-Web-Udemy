package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enums.Color;
import chess.exceptions.ChessException;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toMatrixPosition();
		Position target = targetPosition.toMatrixPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		ChessPiece capturedPiece = makeMove(source, target);
		return capturedPiece;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toMatrixPosition();
		validateSourcePosition(position);
		return board.getPiece(position).possibleMoves();
	}

	private ChessPiece makeMove(Position source, Position target) {
		Piece piece = board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(piece, target);
		return (ChessPiece) capturedPiece;
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("N�o existe pe�a na posi��o de origem!");
		}
		if (!board.getPiece(position).isThereAnyPossibleMove()) {
			throw new ChessException("N�o existem movimentos poss�veis para esta pe�a!");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.getPiece(source).possibleMove(target)) {
			throw new ChessException("A pe�a escolhida n�o pode se mover para a posi��o de destino!");
		}
	}

	// Nosso jogo n�o tem que conhecer as pe�as gen�ricas,
	// mas sim deve conhecer as pe�as do pr�prio jogo.
	public ChessPiece[][] getPieces() {
		ChessPiece[][] chessPieces = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				chessPieces[i][j] = (ChessPiece) board.getPiece(i, j);
			}
		}
		return chessPieces;
	}

	private void placePiece(ChessPosition chessPosition, ChessPiece piece) {
		board.placePiece(piece, chessPosition.toMatrixPosition());
	}

	private void initialSetup() {

		placePiece(new ChessPosition('c', 1), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('c', 2), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('d', 2), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('e', 2), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('e', 1), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('d', 1), new King(board, Color.WHITE));

		placePiece(new ChessPosition('c', 7), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('c', 8), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('d', 7), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('e', 7), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('e', 8), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('d', 8), new King(board, Color.BLACK));
	}

	public Board getBoard() {
		return board;
	}

}
