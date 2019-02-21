import javafx.scene.control.*;

public class ButtonExt {
	
	// ButtonExt object has a coordinate in the grid (I,J), a button with default text "  ", booleans for whether it 
	//	has been clicked or flagged, and a value equal to how many bombs surrounds it
	public int I;
	public int J;
	public Button button = new Button("");
	public boolean clicked = false;
	public boolean flagged = false;
	public int value;
	public int id;
	
	public ButtonExt() {
	}
	
	public ButtonExt(int I, int J) {
		this.I = I;
		this.J = J;
	}
	
}
