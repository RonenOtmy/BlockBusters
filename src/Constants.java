import java.awt.Color;

public class Constants {
	final static Color COLOURBACK = Color.GREEN;
	final static Color COLOURCELL = Color.ORANGE;
	final static Color COLOURGRID = Color.BLACK;
	final static Color COLOURONE = Color.WHITE;
	final static Color COLOURONETXT = Color.BLUE;
	final static Color COLOURTWO = Color.BLUE;// new Color(0,0,0,200);
	final static Color COLOURTWOTXT = new Color(255, 100, 255);
	final static int EMPTY = 0;
	final static int BSIZE = 5; // board size.
	final static int HEXSIZE = 140; // hex size in pixels
	final static int BORDERS = 15;
	final static int SCRSIZE = HEXSIZE * (BSIZE + 1) + BORDERS * 3; // screen size (vertical dimension).

}
