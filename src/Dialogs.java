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
		setVisible(false);
		setModal(true);
		
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
	
	public WbSortDlg(JFrame frame) {
		super( frame, "문제집 정렬", new Dimension(400, 600) );
		lblTitle.setText("정렬");
		
		Container c = getContentPane();
		c.add(pnlCenter, BorderLayout.CENTER);
		
		pnlCenter.setLayout( new GridLayout(5, 1, 0 ,0) );
		for(int i=0; i<5; i++) {
			JButton btnTemp = new JButton( Integer.toString(i) );
			btnTemp.setLayout( new BorderLayout() );
			JPanel pnlTemp = new JPanel();
			JPanel pnlTemp2 = new JPanel();
			btnTemp.add(pnlTemp, BorderLayout.NORTH);
			btnTemp.add(pnlTemp2, BorderLayout.SOUTH);
			pnlTemp.setBackground(Color.red);
			pnlTemp2.setBackground(Color.green);
			pnlCenter.add(btnTemp);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
