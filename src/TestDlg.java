import javax.swing.*;

public class TestDlg extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public TestDlg(JFrame frame) {
		super(frame, "", true);
		setTitle("테스트 모달");
		setSize(400, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(false);
		setModal(true);
	}
}
