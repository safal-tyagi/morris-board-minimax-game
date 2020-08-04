import java.util.ArrayList;
import java.util.List;

/**
 * Description: Board and Operation
 * Author: Safal Tyagi (SKT180001)
 */

public class Board {

	private int size = 23;
	public List<Position> positions;

	public Board() {
		this.positions = new ArrayList<Position>();
		for (int i = 0; i < size; i++) {
			this.positions.add(Position.Blank);
		}
	}

	public Board(List<Position> boardPosList) {
		this.positions = new ArrayList<Position>();
		this.positions.addAll(boardPosList);
	}

	public int getNumOfPieces(Position piece) {
		int count = 0;
		for (Position pos : this.positions) {
			if (pos == piece)
				count++;
		}
		return count;
	}
	
	public Board performBoardFlip(Board oldBoard) {
		Board newBoard = new Board();
		for (int i = 0; i < oldBoard.positions.size(); i++) {
			if (oldBoard.positions.get(i) == Position.Black)
				newBoard.positions.set(i, Position.White);
			else if (oldBoard.positions.get(i) == Position.White)
				newBoard.positions.set(i, Position.Black);
			else
				newBoard.positions.set(i, Position.Blank);
		}
		return newBoard;
	}
	
}
