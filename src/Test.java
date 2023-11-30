import javax.swing.*;
 
class TestFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public TestFrame() {
		setTitle("테스트");
		setSize(300 ,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLocation(500, 300);
		
		int answer = MessageBox.show(this, "테스트 메시지\n2번째 줄 텍스트\n3번째 줄",
				MessageBox.btnYES_NO, MessageBox.iconINFORMATION);
		if (answer == MessageBox.idOK) {
			System.out.println("확인");
		}
		else if(answer == MessageBox.idCANCEL) {
			System.out.println("취소");
		}
	}
}

public class Test {

	public static void main(String[] args) {
		new TestFrame();
	}

}
