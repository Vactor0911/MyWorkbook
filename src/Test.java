import javax.swing.*;
 
class TestFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public TestFrame() {
		setTitle("테스트");
		setSize(300 ,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocation(500, 300);
	}
}

public class Test {

	public static void main(String[] args) {
		new TestFrame();
	}

}
