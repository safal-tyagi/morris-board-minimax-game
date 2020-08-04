
/**
 * Description: Position on the board. Holds the current state viz. Black, White or Blank
 * Author: Safal Tyagi (SKT180001)
 */

public enum Position {
    Black('B'), White('W'), Blank('x');

    public final char state;

    Position(char state) {
        this.state = state;
    }
}