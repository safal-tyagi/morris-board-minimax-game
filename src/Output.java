import java.util.List;

/**
 * Description: Display output in required format
 * Author: Safal Tyagi (SKT180001)
 */

public class Output {
	public int estimate;
	public int positionsEvaluated;
	public Board board;

	public String toString() {
		String output = "Output Board Position:\t" + printList(board.positions) + "\n" 
						+ "Positions Evaluated:\t" + positionsEvaluated + "\n" 
						+ "MINIMAX Estimate:\t" + estimate;
		System.out.println(output);
		return output;
	}

	public String printList(List<Position> bList) {
		String sOut = new String();
		for (Position p : bList) {
			sOut = sOut.concat(Character.toString(p.state));
		}
		return sOut;
	}
}
