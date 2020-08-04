import java.util.List;

/**
 * Description: Plays in the midgame/endgame phase of the MiniMax game with static estimation function
 * Author: Safal Tyagi (SKT180001)
 */

public class MiniMaxGameImproved {

	public static Output MiniMax(Board board, int depth, boolean isWhite) {
		Output out = new Output();
		if (depth == 0) {
			out.estimate = MorrisBoardGame.estimateMidEndGameImproved(board);
			out.positionsEvaluated++;
			return out;
		}

		Output in = new Output();
		List<Board> nextMoves = (isWhite)
				? MorrisBoardGame.generateMovesMidEndgame(board, (board.getNumOfPieces(Position.White) == 3))
				: MorrisBoardGame.generateMovesMidEndgameBlack(board);
		out.estimate = (isWhite) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (Board b : nextMoves) {
			if (isWhite) {
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