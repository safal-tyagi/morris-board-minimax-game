import java.util.List;

/**
 * Description: Plays in the midgame/endgame phase of the MiniMax game with Black moved first, board is flipped in this case
 * Author: Safal Tyagi (SKT180001)
 */

public class MiniMaxGameBlack {

	public static Output MiniMax(Board board, int depth, boolean isBlack) {
		Output out = new Output();
		if (depth == 0) {
			out.estimate = MorrisBoardGame.estimateMidEndGame(board);
			out.positionsEvaluated++;
			return out;
		}

		Output in = new Output();
		List<Board> nextMoves = (isBlack) ? MorrisBoardGame.generateMovesMidEndgameBlack(board)
				: MorrisBoardGame.generateMovesMidEndgame(board, (board.getNumOfPieces(Position.White) == 3));
		out.estimate = (isBlack) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (Board b : nextMoves) {
			if (isBlack) {
				in = MiniMax(b, depth - 1, false);
				out.positionsEvaluated += in.positionsEvaluated;
				if (in.estimate > out.estimate) {
					out.estimate = in.estimate;
					out.board = b;
				}
			} else {
				in = MiniMax(b, depth - 1, true);
				out.positionsEvaluated += in.positionsEvaluated;
				out.positionsEvaluated++;
				if (in.estimate < out.estimate) {
					out.estimate = in.estimate;
					out.board = b;
				}
			}
		}
		return out;
	}

	public static void main(String[] args) {
		if (args.length == 3) {
			String inputBoardPositions = args[0]; 
			String outputBoardPositions = args[1]; 
			int depth = Integer.parseInt(args[2]);
			Board initialBoard = new Board(MorrisBoardGame.getBoardConfigFromFile(inputBoardPositions));
			Output algOut = MiniMax(initialBoard, depth, true);
			MorrisBoardGame.writeOutput(algOut, outputBoardPositions);
		} else {
			System.out.println("Incorrect arguments. Please input: <input board file> <output board file> <search depth>");
		}
	}

}