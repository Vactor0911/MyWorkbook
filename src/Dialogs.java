import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;

import java.awt.event.*;

public class Dialogs extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JPanel pnlNorth = new JPanel();
	private JLabel lblTitle = new JLabel("", SwingConstants.CENTER);
	
	public Dialogs(JFrame frame, String name, Dimension dim) {
		super(frame, "", true);
		setTitle(name);
		setSize(dim.width, dim.height);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		//대화상자를 화면 정중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - dim.width / 2;
		int y = res.height / 2 - dim.height / 2;
		setLocation(x, y);
		
		Container c = getContentPane();
		
		c.add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 5) );
		pnlNorth.setBackground( Frame.getColor() );
		pnlNorth.setBorder( new MatteBorder( 0,0,2,0,Frame.getColorBorder() ) );
		pnlNorth.setPreferredSize( new Dimension(0, 50) );
		pnlNorth.add(lblTitle);
		
		lblTitle.setFont( new Font(Frame.getFontName(), Font.BOLD, 26) );
	}
	
	public JPanel getPanel() {
		return pnlNorth;
	}
	
	public JLabel getLabel() {
		return lblTitle;
	}
}

//문제집 - [정렬]
class WbSortDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private JButton[] aryBtn = new JButton[6];
	
	public WbSortDlg(JFrame frame) {
		super( frame, "문제집 정렬", new Dimension(400, 600) );
		lblTitle.setText("정렬");
		
		Container c = getContentPane();
		c.add(pnlCenter, BorderLayout.CENTER);
		
		pnlCenter.setLayout( new GridLayout(6, 1, 0 ,0) );
		String[] arySearchList = { "이름순 (오름차순)", "이름순 (내림차순)", "문제 많은순", "문제 적은순",
				"정답률 높은순", "정답률 낮은순" };
		for(int i=0; i<arySearchList.length; i++) {
			aryBtn[i] = new JButton( arySearchList[i] );
			aryBtn[i].addActionListener(this);
			pnlCenter.add( aryBtn[i] );
		}
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		
		for (int i=0; i<aryBtn.length; i++) {
			if (btn == aryBtn[i]) {
				Frame frame = new Frame();
				frame.sort(i);
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
				break;
			}
		}
	}
}

//문제집 - [문제집 추가]
class WbAddDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
		
	public WbAddDlg(JFrame frame) {
		super( frame, "문제집 추가", new Dimension(400, 600) );
		lblTitle.setText("문제집 추가");
		
		Container c = getContentPane();
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
