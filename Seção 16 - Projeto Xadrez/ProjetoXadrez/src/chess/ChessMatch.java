package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enums.Color;
import chess.exceptions.ChessException;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulterable;
	private ChessPiece promoted;

	private List<ChessPiece> piecesOnTheBoard = new ArrayList<ChessPiece>();
	private List<ChessPiece> capturedPieces = new ArrayList<ChessPiece>();

	public ChessMatch() {
		turn = 1;
		currentPlayer = Color.WHITE;
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toMatrixPosition();
		Position target = targetPosition.toMatrixPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		ChessPiece capturedPiece = makeMove(source, target);

		ChessPiece movedPiece = ((ChessPiece) board.getPiece(target));

		// Verificando se o jogador atual se colocou em Xeque com a pr�pria jogada.
		if (testCheck(currentPlayer)) {
			UndoMove(source, target, capturedPiece);
			throw new ChessException("Voc� n�o pode se colocar em Xeque!");
		}

		// Promotion
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)
					|| (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece) board.getPiece(target);
				promoted = replacePromotedPiece("Q");
			}
		}

		// Verificando se o jogador atual colocou seu advers�rio em Xeque com a jogada.
		if (testCheck(opponentColor(currentPlayer))) {
			check = true;
		} else {
			check = false;
		}
		if (testCheckMate(opponentColor(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}

		// #Enpassant
		if (movedPiece instanceof Pawn
				&& (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulterable = movedPiece;
		} else {
			enPassantVulterable = null;
		}

		return capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("N�o h� pe�a para ser promovida!");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			return promoted;
		}
		Position pos = promoted.getChessPosition().toMatrixPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B"))
			return new Bishop(board, color);
		else if (type.equals("Q"))
			return new Queen(board, color);
		else if (type.equals("N"))
			return new Knight(board, color);
		return new Rook(board, color);
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toMatrixPosition();
		validateSourcePosition(position);
		return board.getPiece(position).possibleMoves();
	}

	private ChessPiece makeMove(Position source, Position target) {
		ChessPiece piece = (ChessPiece) board.removePiece(source);
		Piece capturedPiece = board.removePiece(target);

		piece.increaseMoveCount();

		board.placePiece(piece, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}

		if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = ((ChessPiece) board.removePiece(sourceRook));
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}

		if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = ((ChessPiece) board.removePiece(sourceRook));
			board.placePiece(rook, targetRook);
			rook.increaseMoveCount();
		}

		if (piece instanceof Pawn && source.getColumn() != target.getColumn() && capturedPiece == null) {
			Position pawnPosition;
			if (piece.getColor() == Color.WHITE) {
				pawnPosition = new Position(target.getRow() + 1, target.getColumn());
			} else {
				pawnPosition = new Position(target.getRow() - 1, target.getColumn());
			}
			capturedPiece = board.removePiece(pawnPosition);
			capturedPieces.add((ChessPiece) capturedPiece);
			piecesOnTheBoard.remove(capturedPiece);
		}

		return (ChessPiece) capturedPiece;
	}

	private void UndoMove(Position source, Position target, ChessPiece capturedPiece) {
		ChessPiece piece = (ChessPiece) board.removePiece(target);
		piece.decreaseMoveCount();
		board.placePiece(piece, source);
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			piecesOnTheBoard.add(capturedPiece);
			capturedPieces.remove((ChessPiece) capturedPiece);
		}

		if (piece instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() + 3);
			Position targetRook = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = ((ChessPiece) board.removePiece(targetRook));
			board.placePiece(rook, sourceRook);
			rook.decreaseMoveCount();
		}

		if (piece instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceRook = new Position(source.getRow(), source.getColumn() - 4);
			Position targetRook = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = ((ChessPiece) board.removePiece(targetRook));
			board.placePiece(rook, sourceRook);
			rook.increaseMoveCount();
		}

		if (piece instanceof Pawn && source.getColumn() != target.getColumn() && capturedPiece == enPassantVulterable) {
			ChessPiece pawn = (ChessPiece) board.removePiece(target);
			Position pawnPosition;
			if (piece.getColor() == Color.WHITE) {
				pawnPosition = new Position(3, target.getColumn());
			} else {
				pawnPosition = new Position(4, target.getColumn());
			}
			board.placePiece(pawn, pawnPosition);
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("N�o existe pe�a na posi��o de origem!");
		}
		if (!board.getPiece(position).isThereAnyPossibleMove()) {
			throw new ChessException("N�o existem movimentos poss�veis para esta pe�a!");
		}
		if (currentPlayer != ((ChessPiece) board.getPiece(position)).getColor()) {
			throw new ChessException("Voc� n�o pode mover pe�as advers�rias!");
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
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {

		placePiece(new ChessPosition('d', 1), new Queen(board, Color.WHITE));
		placePiece(new ChessPosition('a', 1), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('e', 1), new King(board, Color.WHITE, this));
		placePiece(new ChessPosition('h', 1), new Rook(board, Color.WHITE));
		placePiece(new ChessPosition('c', 1), new Bishop(board, Color.WHITE));
		placePiece(new ChessPosition('f', 1), new Bishop(board, Color.WHITE));
		placePiece(new ChessPosition('b', 1), new Knight(board, Color.WHITE));
		placePiece(new ChessPosition('g', 1), new Knight(board, Color.WHITE));
		placePiece(new ChessPosition('a', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('b', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('c', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('d', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('e', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('f', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('g', 2), new Pawn(board, Color.WHITE, this));
		placePiece(new ChessPosition('h', 2), new Pawn(board, Color.WHITE, this));

		placePiece(new ChessPosition('d', 8), new Queen(board, Color.BLACK));
		placePiece(new ChessPosition('b', 8), new Knight(board, Color.BLACK));
		placePiece(new ChessPosition('g', 8), new Knight(board, Color.BLACK));
		placePiece(new ChessPosition('a', 8), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('e', 8), new King(board, Color.BLACK, this));
		placePiece(new ChessPosition('h', 8), new Rook(board, Color.BLACK));
		placePiece(new ChessPosition('c', 8), new Bishop(board, Color.BLACK));
		placePiece(new ChessPosition('f', 8), new Bishop(board, Color.BLACK));
		placePiece(new ChessPosition('a', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('b', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('c', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('d', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('e', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('f', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('g', 7), new Pawn(board, Color.BLACK, this));
		placePiece(new ChessPosition('h', 7), new Pawn(board, Color.BLACK, this));
	}

	private boolean testCheck(Color color) {
		Position kingPosition = getKing(color).getChessPosition().toMatrixPosition();
		List<ChessPiece> opponentPieces = piecesOnTheBoard.stream()
				.filter(piece -> piece.getColor().equals(opponentColor(color))).collect(Collectors.toList());
		for (ChessPiece chessPiece : opponentPieces) {
			boolean[][] possibleMoves = chessPiece.possibleMoves();
			if (possibleMoves[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {

		if (!testCheck(color)) {
			return false;
		}

		List<ChessPiece> myPieces = piecesOnTheBoard.stream().filter(piece -> piece.getColor().equals(color))
				.collect(Collectors.toList());

		for (ChessPiece chessPiece : myPieces) {
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (chessPiece.possibleMoves()[i][j]) {
						Position source = chessPiece.getChessPosition().toMatrixPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						UndoMove(source, target, (ChessPiece) capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public Board getBoard() {
		return board;
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	private Color opponentColor(Color color) {
		return (color == Color.WHITE ? Color.BLACK : Color.WHITE);
	}

	private ChessPiece getKing(Color color) {
		List<ChessPiece> list = piecesOnTheBoard.stream().filter(piece -> ((ChessPiece) piece).getColor() == color)
				.collect(Collectors.toList());
		for (ChessPiece chessPiece : list) {
			if (chessPiece instanceof King) {
				return chessPiece;
			}
		}
		throw new IllegalStateException("N�o h� rei no tabuleiro!");
	}

	private void nextTurn() {
		turn++;
		if (currentPlayer == Color.WHITE) {
			currentPlayer = Color.BLACK;
		} else {
			currentPlayer = Color.WHITE;
		}
	}

	public boolean isCheck() {
		return check;
	}

	public boolean isCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantVulterable() {
		return enPassantVulterable;
	}

	public ChessPiece getPromoted() {
		return promoted;
	}

}
