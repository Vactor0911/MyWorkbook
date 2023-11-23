import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Dialogs extends JDialog {
	private static final long serialVersionUID = 1L;
	
	public Dialogs(JFrame frame, String name, Dimension dim) {
		super(frame, "", true);
		setTitle(name);
		setSize(dim.width, dim.height);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(false);
		setModal(true);
		
		//대화상자를 화면 정중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - dim.width / 2;
		int y = res.height / 2 - dim.height / 2;
		setLocation(x, y);
	}
}

//문제집 - [검색]
class WbSearchDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public WbSearchDlg(JFrame frame) {
		super( frame, "문제집 검색", new Dimension(400, 600) );
		
		Container c = getContentPane();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}

//문제집 - [정렬]
class WbSortDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public WbSortDlg(JFrame frame) {
		super( frame, "문제집 정렬", new Dimension(400, 600) );
		
		Container c = getContentPane();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}

//문제집 - [문제집 추가]
class WbAddDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
		
	public WbAddDlg(JFrame frame) {
		super( frame, "문제집 추가", new Dimension(400, 600) );
		
		Container c = getContentPane();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
