import java.awt.*;
import javax.swing.*;
 
class TestFrame extends JFrame {  
	private static final long serialVersionUID = 1L;

	public TestFrame() {
		new WbAddDlg(this);
	}
	
}

public class Test {

	public static void main(String[] args) {
		new TestFrame();
	}

}
