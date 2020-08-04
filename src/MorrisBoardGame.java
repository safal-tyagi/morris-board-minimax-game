import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: Defines a Morris-Variant of the Minimax game.
 * Author: Safal Tyagi (SKT180001)
 */

public class MorrisBoardGame {

	// *************************************************************
	// White Moves
	// *************************************************************
	public static List<Board> generateMovesOpening(Board board) {
		ArrayList<Board> list = new ArrayList<Board>();
		for (int i = 0; i < board.positions.size(); i++) {
			if (board.positions.get(i) == Position.Blank) {
				Board newBoard = new Board(board.positions);
				newBoard.positions.set(i, Position.White);
				if (closeMill(i, newBoard)) {
					list = generateRemove(newBoard, list);
				} else {
					list.add(newBoard);
				}
			}
		}
		return list;
	}

	public static List<Board> generateMovesMidEndgame(Board board, Boolean isHoppingAllowed) {
		if (isHoppingAllowed) {
			return generateHopping(board);
		} else {
			return generateMove(board);
		}
	}

	// *************************************************************
	// Other moves
	// *************************************************************
	public static List<Board> generateHopping(Board board) {
		ArrayList<Board> list = new ArrayList<Board>();
		for (int i = 0; i < board.positions.size(); i++) {
			if (board.positions.get(i) == Position.White) {
				for (int j = 0; j < board.positions.size(); j++) {
					if (board.positions.get(j) == Position.Blank) {
						Board newBoard = new Board(board.positions);
						newBoard.positions.set(i, Position.Blank);
						newBoard.positions.set(j, Position.White);
						if (closeMill(j, newBoard)) {
							generateRemove(newBoard, list);
						} else {
							list.add(newBoard);
						}
					}
				}
			}
		}
		return list;
	}

	public static List<Board> generateMove(Board board) {
		ArrayList<Board> list = new ArrayList<Board>();
		for (int i = 0; i < board.positions.size(); i++) {
			if (board.positions.get(i) == Position.White) {
				List<Integer> n = getNeighbors(i);
				for (int j : n) {
					if (board.positions.get(j) == Position.Blank) {
						Board newBoard = new Board(board.positions);
						newBoard.positions.set(i, Position.Blank);
						newBoard.positions.set(j, Position.White);
						if (closeMill(j, newBoard)) {
							list = generateRemove(newBoard, list);
						} else {
							list.add(newBoard);
						}
					}
				}
			}
		}
		return list;
	}

	public static ArrayList<Board> generateRemove(Board board, ArrayList<Board> list) {
		for (int i = 0; i < board.positions.size(); i++) {
			if (board.positions.get(i) == Position.Black) {
				if (!closeMill(i, board)) {
					Board newBoard = new Board(board.positions);
					newBoard.positions.set(i, Position.Blank);
					list.add(newBoard);
				}
			}
		}
		return list;
	}

	// *************************************************************
	// Black Moves
	// *************************************************************
	public static List<Board> generateMovesOpeningBlack(Board board) {
		Board tempFlipBoard = board.performBoardFlip(board);
		List<Board> moveList = generateMovesOpening(tempFlipBoard);
		for (int i = 0; i < moveList.size(); i++) {
			Board newBoard = moveList.get(i);
			moveList.set(i, newBoard.performBoardFlip(newBoard));
		}
		return moveList;
	}

	public static List<Board> generateMovesMidEndgameBlack(Board board) {
		Board tempFlipBoard = board.performBoardFlip(board);
		int whiteCount = tempFlipBoard.getNumOfPieces(Position.White);
		List<Board> moveList = generateMovesMidEndgame(tempFlipBoard, (whiteCount == 3));
		ArrayList<Board> out = new ArrayList<Board>();
		for (int i = 0; i < moveList.size(); i++) {
			Board newBoard = moveList.get(i);
			out.add(newBoard.performBoardFlip(newBoard));
		}
		return out;
	}

	// *************************************************************
	// Static Estimation
	// *************************************************************
	public static int estimateOpening(Board board) {
		return (board.getNumOfPieces(Position.White) - board.getNumOfPieces(Position.Black));
	}

	public static int estimateMidEndGame(Board board) {
		int blackCount = board.getNumOfPieces(Position.Black);
		int whiteCount = board.getNumOfPieces(Position.White);
		List<Board> l = generateMovesMidEndgame(board, (whiteCount == 3));
		int numBMoves = l.size();
		if (blackCount <= 2) {
			return 10000;
		} else if (whiteCount <= 2) {
			return -10000;
		} else if (blackCount == 0) {
			return 10000;
		} else {
			return 1000 * (whiteCount - blackCount) - numBMoves;
		}
	}

	public static int estimateOpeningImproved(Board board) {
		return (board.getNumOfPieces(Position.White) + numOfPossibleMills(Position.White, board)
				- board.getNumOfPieces(Position.Black));
	}

	public static int estimateMidEndGameImproved(Board board) {
		int blackCount = board.getNumOfPieces(Position.Black);
		int whiteCount = board.getNumOfPieces(Position.White);
		int numOfPossMill = numOfPossibleMills(Position.White, board);
		List<Board> list = generateMovesMidEndgame(board, (whiteCount == 3));
		int numBMoves = list.size();
		if (blackCount <= 2) {
			return 10000;
		} else if (whiteCount <= 2) {
			return -10000;
		} else if (blackCount == 0) {
			return 10000;
		} else {
			return 1000 * (whiteCount - blackCount + numOfPossMill) - numBMoves;
		}
	}

	// *************************************************************
	// Mills Management
	// *************************************************************
	public static boolean closeMill(int location, Board board) {
		Position position = board.positions.get(location);
		if (position == Position.Blank)
			return false;
		else
			return checkMills(board, location, position);
	}

	public static boolean checkMills(Board board, int location, Position position) {
		switch (location) {
			case 0:
				return (checkMill(board, position, 1, 2) || checkMill(board, position, 3, 6))
						|| checkMill(board, position, 8, 20);
			case 1:
				return (checkMill(board, position, 0, 2));
			case 2:
				return (checkMill(board, position, 0, 1) || checkMill(board, position, 5, 7)
						|| checkMill(board, position, 13, 22));
			case 3:
				return (checkMill(board, position, 0, 6) || checkMill(board, position, 4, 5)
						|| checkMill(board, position, 9, 17));
			case 4:
				return (checkMill(board, position, 3, 5));
			case 5:
				return (checkMill(board, position, 3, 4) || checkMill(board, position, 2, 7)
						|| checkMill(board, position, 12, 19));
			case 6:
				return (checkMill(board, position, 0, 3) || checkMill(board, position, 10, 14));
			case 7:
				return (checkMill(board, position, 2, 5) || checkMill(board, position, 11, 16));
			case 8:
				return (checkMill(board, position, 0, 20) || checkMill(board, position, 9, 10));
			case 9:
				return (checkMill(board, position, 8, 10) || checkMill(board, position, 3, 17));
			case 10:
				return (checkMill(board, position, 8, 9) || checkMill(board, position, 6, 14));
			case 11:
				return (checkMill(board, position, 7, 16) || checkMill(board, position, 12, 13));
			case 12:
				return (checkMill(board, position, 11, 13) || checkMill(board, position, 5, 19));
			case 13:
				return (checkMill(board, position, 11, 12) || checkMill(board, position, 2, 22));
			case 14:
				return (checkMill(board, position, 17, 20) || checkMill(board, position, 15, 16)
						|| checkMill(board, position, 6, 10));
			case 15:
				return (checkMill(board, position, 14, 16) || checkMill(board, position, 18, 21));
			case 16:
				return (checkMill(board, position, 14, 15) || checkMill(board, position, 19, 22)
						|| checkMill(board, position, 7, 11));
			case 17:
				return (checkMill(board, position, 3, 9) || checkMill(board, position, 14, 20)
						|| checkMill(board, position, 18, 19));
			case 18:
				return (checkMill(board, position, 17, 19) || checkMill(board, position, 15, 21));
			case 19:
				return (checkMill(board, position, 17, 18) || checkMill(board, position, 16, 22)
						|| checkMill(board, position, 5, 12));
			case 20:
				return (checkMill(board, position, 0, 8) || checkMill(board, position, 14, 17)
						|| checkMill(board, position, 21, 22));
			case 21:
				return (checkMill(board, position, 20, 22) || checkMill(board, position, 15, 18));
			case 22:
				return (checkMill(board, position, 2, 13) || checkMill(board, position, 16, 19)
						|| checkMill(board, position, 20, 21));
			default:
				return false;
		}
	}

	private static boolean checkMill(Board board, Position position, int x, int y) {
		return (board.positions.get(x) == position && board.positions.get(y) == position);
	}

	public static int numOfPossibleMills(Position position, Board board) {
		int count = 0;
		for (int i = 0; i < board.positions.size(); i++) {
			Position bPos = board.positions.get(i);
			if (bPos == Position.Blank) {
				if (checkMills(board, i, bPos)) {
					count++;
				}
			}
		}
		return count;
	}

	// *************************************************************
	// Services
	// *************************************************************
	public static List<Integer> getNeighbors(int i) {
		switch (i) {
			case 0:
				return Arrays.asList(1, 3, 8);
			case 1:
				return Arrays.asList(0, 2, 4);
			case 2:
				return Arrays.asList(1, 5, 13);
			case 3:
				return Arrays.asList(0, 4, 6, 9);
			case 4:
				return Arrays.asList(1, 3, 5);
			case 5:
				return Arrays.asList(2, 4, 7, 12);
			case 6:
				return Arrays.asList(3, 7, 10);
			case 7:
				return Arrays.asList(5, 6, 11);
			case 8:
				return Arrays.asList(0, 9, 20);
			case 9:
				return Arrays.asList(3, 8, 10, 17);
			case 10:
				return Arrays.asList(6, 9, 14);
			case 11:
				return Arrays.asList(7, 12, 16);
			case 12:
				return Arrays.asList(5, 11, 13, 19);
			case 13:
				return Arrays.asList(2, 12, 22);
			case 14:
				return Arrays.asList(10, 15, 17);
			case 15:
				return Arrays.asList(14, 16, 18);
			case 16:
				return Arrays.asList(11, 15, 19);
			case 17:
				return Arrays.asList(9, 14, 18, 20);
			case 18:
				return Arrays.asList(15, 17, 19, 21);
			case 19:
				return Arrays.asList(12, 16, 18, 22);
			case 20:
				return Arrays.asList(8, 17, 21);
			case 21:
				return Arrays.asList(18, 20, 22);
			case 22:
				return Arrays.asList(13, 19, 21);
			default:
				return (new ArrayList<Integer>());
		}
	}

	public static ArrayList<Position> getBoardConfigFromFile(String fileName) {
		String line = null;
		try {
			FileReader filereader = new FileReader(fileName);
			BufferedReader buffreader = new BufferedReader(filereader);
			line = buffreader.readLine();
			System.out.println("Input Board Position:\t" + line);
			ArrayList<Position> outList = new ArrayList<Position>();
			for (char c : line.toCharArray()) {
				if (Character.toLowerCase(c) == Position.Blank.state)
					outList.add(Position.Blank);
				else if (Character.toUpperCase(c) == Position.Black.state)
					outList.add(Position.Black);
				else if (Character.toUpperCase(c) == Position.White.state)
					outList.add(Position.White);
				else {
					System.out.println("Incorrect Input Configuration " + c);
					buffreader.close();
					return null;
				}
			}
			buffreader.close();
			return outList;
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (Exception ex) {
			System.out.println("Error reading file '" + fileName + "' : " + ex.toString());
		}
		return null;
	}

	public static void writeOutput(Output output, String fName) {
		try {
			FileWriter fileWriter = new FileWriter(fName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(output.toString());
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fName + "'");
		} catch (Exception ex) {
			System.out.println("Error writing to file '" + fName + "' : " + ex.toString());
		}
	}

}
