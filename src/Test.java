import java.awt.*;
import javax.swing.*;
 
class TestFrame extends JFrame {  
	private static final long serialVersionUID = 1L;

	public TestFrame() {
		setTitle("LayeredPane 테스트");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Container c = getContentPane();
		
//		JPanel pnl = new JPanel();
//		pnl.setLayout( new GridLayout(3, 1) );
//		for (int i=0; i<255; i++) {
//			JLabel lbl = new JLabel();
//			lbl.setOpaque(true);
//			int color = i + 1;
//			lbl.setBackground( new Color(color, color, color) );
//			pnl.add(lbl);
//		}
//		pnl.setPreferredSize( new Dimension(400, 1000) );
//		
//		JScrollPane sp = new JScrollPane(pnl);
//		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		c.add(sp, BorderLayout.CENTER);
		
//		c.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 10) );
//		String[] str = { "USA", "UK", "Germany", "Canada" };
//		JComboBox<String> combobox = new JComboBox<String>(str);
//		c.add(combobox);
//		combobox.setToolTipText("Test");
//		combobox.setBackground(null);
		
		c.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 10) );
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add("New");
		fileMenu.add("Open");
		fileMenu.add("Close");
	    fileMenu.addSeparator();
	    
	    c.add(fileMenu);
		
		setSize(400, 400);
	}
}  

class TestDlg extends JDialog {
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

public class Test {

	public static void main(String[] args) {
		new TestFrame();
	}

}
