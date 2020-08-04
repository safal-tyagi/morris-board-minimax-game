import java.util.List;

/**
 * Description: Plays a move in the opening phase of the MiniMax game with Alpha-Beta Pruning
 * Author: Safal Tyagi (SKT180001)
 */

public class ABOpening {

	public static Output ABMiniMax(Board board, int depth, boolean isWhite, int alpha, int beta) {
		Output out = new Output();
		if (depth == 0) {
			out.estimate = MorrisBoardGame.estimateOpening(board);
			out.positionsEvaluated++;
			return out;
		}
		Output in = new Output();
		List<Board> nextMoves = (isWhite) ? MorrisBoardGame.generateMovesOpening(board)
				: MorrisBoardGame.generateMovesOpeningBlack(board);
		for (Board b : nextMoves) {
			if (isWhite) {
				in = ABMiniMax(b, depth - 1, false, alpha, beta);
				out.positionsEvaluated += in.positionsEvaluated;
				if (in.estimate > alpha) {
					alpha = in.estimate;
					out.board = b;
				}
			} else {
				in = ABMiniMax(b, depth - 1, true, alpha, beta);
				out.positionsEvaluated += in.positionsEvaluated;
				out.positionsEvaluated++;
				if (in.estimate < beta) {
					beta = in.estimate;
					out.board = b;
				}
			}
			if (alpha >= beta) {
				break;
			}
		}
		out.estimate = (isWhite) ? alpha : beta;
		return out;
	}

	public static void main(String[] args) {
		if (args.length == 3) {
			String inputBoardPositions = args[0]; 
			String outputBoardPositions = args[1];
			int depth = Integer.parseInt(args[2]);
			Board initialBoard = new Board(MorrisBoardGame.getBoardConfigFromFile(inputBoardPositions));
			Output algOut = ABMiniMax(initialBoard, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
			MorrisBoardGame.writeOutput(algOut, outputBoardPositions);
		} else {
			System.out.println("Incorrect arguments. Please input: <input board file> <output board file> <search depth>");
		}
	}
}