import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.io.*;

public class Dialogs extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JPanel pnlNorth = new JPanel();
	private JLabel lblTitle = new JLabel("", SwingConstants.CENTER);
	
	public Dialogs(Frame frame, String name, int width, int height) {
		super(frame, "", true);
		setTitle(name);
		setSize(width, height);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		//대화상자를 화면 정중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - width / 2;
		int y = res.height / 2 - height / 2;
		setLocation(x, y);
		
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		
		c.add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 5) );
		pnlNorth.setBackground( Frame.getColor() );
		pnlNorth.setBorder( new MatteBorder( 0, 0, 2, 0, Frame.getColorBorder() ) );
		pnlNorth.setPreferredSize( new Dimension(0, 50) );
		pnlNorth.add(lblTitle);
		
		lblTitle.setFont( new Font(Frame.getFontName(), Font.BOLD, 26) );
	} //생성자
	
	
	//getter
	public JPanel getPanel() {
		return pnlNorth;
	}
	
	public JLabel getLabel() {
		return lblTitle;
	}
}//Dialogs 클래스


class MessageBox extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JLabel lblText = new JLabel();
	private JButton btnOk = new JButton("확인");
	private JButton btnCancel = new JButton("취소");
	
	//JFrame
	public MessageBox(JFrame frame) {
		this(frame, "", null, null);
	}
	
	public MessageBox(JFrame frame, String msg) {
		this(frame, msg, null, null);
	}
	
	public MessageBox(JFrame frame, String msg, String btnType) {
		this(frame, msg, btnType, null);
	}
	
	public MessageBox(JFrame frame, String msg, String btnType, String iconType) {
		super(frame, frame.getName(), true);
	}
	
	
	//JDialog
	public MessageBox(JDialog dialog) {
		this(dialog, "", null, null);
	}
	
	public MessageBox(JDialog dialog, String msg) {
		this(dialog, msg, null, null);
	}
	
	public MessageBox(JDialog dialog, String msg, String btnType) {
		this(dialog, msg, btnType, null);
	}
	
	public MessageBox(JDialog dialog, String msg, String btnType, String iconType) {
		super(dialog, dialog.getName(), true);
	}
} //MessageBox 클래스


//문제집 - [정렬]
class WbSortDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Frame frame;
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private JButton[] aryBtn = new JButton[6];
	
	public WbSortDlg(Frame frame) {
		super(frame, "문제집 정렬", 400, 600);
		this.frame = frame;
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
	} //생성자

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		
		for (int i=0; i<aryBtn.length; i++) {
			if (btn == aryBtn[i]) {
				frame.sort(i);
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
				break;
			}
		}
	} //actionPerformed()
} //WbSortDlg 클래스


//문제집 - [문제집 추가]
class WbAddDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlNew = new JPanel();
	private JPanel pnlLoad = new JPanel();
	private JPanel pnlNewCenter = new JPanel();
	private JLabel lblNew = new JLabel("새로 만들기");
	private JLabel lblNewTitle = new JLabel("제목");
	private JHintTextField tfNewTitle = new JHintTextField("  제목을 입력하세요");
	private JLabel lblNewColor = new JLabel("색상");
	private JColorComboBox cbColor = new JColorComboBox();
	private JButton btnNewOk = new JButton("문제집 만들기");
	private JLabel lblLoad = new JLabel("불러오기");
	private JButton btnLoad = new JButton("파일 가져오기");
	
	private Frame frame;
	
	public WbAddDlg(Frame frame) {
		super(frame, "문제집 추가", 300, 400);
		this.frame = frame;
		lblTitle.setText("문제집 추가");
		
		Container c = getContentPane();
		c.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new BoxLayout(pnlCenter, BoxLayout.Y_AXIS) );
		pnlCenter.add(pnlNew);
		
		//North
		pnlNew.setPreferredSize( new Dimension(0, 255) );
		pnlNew.setLayout( new BorderLayout() );
		pnlNew.setBorder( BorderFactory.createEmptyBorder(10, 10, 0, 10) );
		
		lblNew.setFont( new Font(Frame.getFontName(), Font.BOLD, 15) );
		lblNew.setForeground( Frame.getColorFont() );
		pnlNew.add(lblNew, BorderLayout.NORTH);
		
		pnlNew.add(pnlNewCenter, BorderLayout.CENTER);
		pnlNewCenter.setLayout(null);
		
		lblNewTitle.setFont( new Font(Frame.getFontName(), Font.PLAIN, 13) );
		lblNewTitle.setSize(100, 20);
		lblNewTitle.setLocation(0, 5);
		pnlNewCenter.add(lblNewTitle);
		
		tfNewTitle.setSize(264, 30);
		tfNewTitle.setLocation(0, 30);
		pnlNewCenter.add(tfNewTitle);
		
		lblNewColor.setFont( new Font(Frame.getFontName(), Font.PLAIN, 13) );
		lblNewColor.setSize(100, 20);
		lblNewColor.setLocation(0, 70);
		pnlNewCenter.add(lblNewColor);
		
		cbColor.setSize(264, 30);
		cbColor.setLocation(0, 95);
		pnlNewCenter.add(cbColor);
		
		btnNewOk.setSize(180, 35);
		btnNewOk.setLocation(42, 140);
		btnNewOk.setBackground( Frame.getColor() );
		btnNewOk.setBorder( new MatteBorder( 2, 2, 2, 2, Frame.getColorBorder() ) );
		pnlNewCenter.add(btnNewOk);
		btnNewOk.addActionListener(this);
		
		//Center Border
		pnlNewCenter.setBorder( new MatteBorder( 0, 0, 2, 0, Frame.getColorBorder() ) );
		
		//South
		pnlCenter.add(pnlLoad);
		pnlLoad.setLayout( new BorderLayout() );
		pnlLoad.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		pnlLoad.setPreferredSize( new Dimension(0, 95) );
		
		lblLoad.setFont( new Font(Frame.getFontName(), Font.BOLD, 15) );
		lblLoad.setForeground( Frame.getColorFont() );
		pnlLoad.add(lblLoad, BorderLayout.NORTH);
		
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout(null);
		pnlLoad.add(pnlTemp, BorderLayout.CENTER);
		
		btnLoad.setSize(180, 35);
		btnLoad.setLocation(42, 5);
		btnLoad.setBackground( Frame.getColor() );
		btnLoad.setBorder( new MatteBorder( 2, 2, 2, 2, Frame.getColorBorder() ) );
		pnlTemp.add(btnLoad);
		btnLoad.addActionListener(this);
		
		setVisible(true);
	} //생성자
	
	
	class JColorComboBox extends JComboBox<Color> {
		private static final long serialVersionUID = 1L;
		private Color[] aryColor = {
			new Color(244, 153, 192),
			new Color(247, 148, 30),
			new Color(255, 247, 154),
			new Color(171, 211, 116),
			new Color(109, 207, 246),
		};

		@SuppressWarnings("unchecked")
		public JColorComboBox() {
			super();
			DefaultComboBoxModel<Color> model = new DefaultComboBoxModel<Color>();
			
			for (Color color : aryColor) {
				model.addElement(color);
			}
			setModel(model);
			setRenderer( new ColorRenderer() );
			this.setOpaque(true);
			this.setSelectedIndex(0);
		} //생성자
		
		@Override
		public void setSelectedItem(Object object) {
			super.setSelectedItem(object);
			setBackground( (Color)object );
		}
		
		@SuppressWarnings("rawtypes")
		class ColorRenderer extends JLabel implements ListCellRenderer {
			private static final long serialVersionUID = 1L;
			public ColorRenderer() {
				this.setOpaque(true);
				this.setPreferredSize( new Dimension(0, 30) );
			}
			
			@Override
			public Component getListCellRendererComponent(JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				setBackground( (Color)value );
				setText(" ");
				return this;
			}
		} //ColorRenderer 클래스
		
		public Color getColor() {
			return aryColor[ getSelectedIndex() ];
		}
	} //JColorComboBox 클래스
	
	
	class JHintTextField extends JTextField implements FocusListener {
		private static final long serialVersionUID = 1L;
		private String hint;
		private boolean flagHint = true;
		private boolean flagWarn = false;
		
		public JHintTextField(String hint) {
			super(hint);
		    this.hint = hint;
		    super.setBorder( BorderFactory.createLineBorder( Frame.getColorBorder() ) );
		    super.setForeground(Color.BLACK);
		    super.addFocusListener(this);
		} //생성자

		@Override
		public void focusGained(FocusEvent e) {
			if( this.getText().isEmpty() ) {
				super.setText("");
				if (!flagWarn) {
					super.setBorder( BorderFactory.createLineBorder( Frame.getColorBorder() ) );
					super.setForeground(Color.BLACK);
				}
				flagHint = false;
		    }
		} //focusGained()

		@Override
		public void focusLost(FocusEvent e) {
			if( this.getText().isEmpty() ) {
			    super.setText(hint);
			    if (!flagWarn) {
			    	super.setBorder( BorderFactory.createLineBorder( Frame.getColorBorder() ) );
				    super.setForeground(Color.GRAY);
			    }
			    flagHint = true;
			    return;
		    }
			else if(flagWarn) {
				flagWarn = false;
				super.setBorder( BorderFactory.createLineBorder( Frame.getColorBorder() ) );
			    super.setForeground(Color.BLACK);
			}
		} //focusLost()
		
		@Override
		public String getText() {
			if (flagHint) {
				return "";
			}
			else {
				return super.getText();
			}
		}
		
		//공백 경고
		public void warn() {
			flagWarn = true;
			super.setForeground(Color.RED);
			super.setBorder( BorderFactory.createLineBorder(Color.RED) );
		}
	} //JHintTextField 클래스
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewOk) { //저장
			//문제집 제목이 공백일 경우
			if ( tfNewTitle.getText().isEmpty() ) {
				tfNewTitle.warn();
				return;
			}
			
			String fileName = tfNewTitle.getText();
			Color fileColor = cbColor.getColor();
			
			//파일 새로 생성
			String separator = File.separator;
	    	String folderPath = Frame.getFolderPath() + separator + "Workbooks";
	    	String filePath = folderPath + separator + fileName + ".workbook";
	    	
    		Workbook wb = new Workbook(fileName, fileColor);
    		FileIO ob = new FileIO();
        	ob.saveFile(filePath, wb);
	    	
	    	this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
		}
		else if (e.getSource() == btnLoad) { //불러오기
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileHidingEnabled(true);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("문제집 파일 (*.workbook)",
					"workbook");
			jfc.setFileFilter(filter);
			
			int result = jfc.showOpenDialog(null); //파일 선택기 열기
			
			if (result == JFileChooser.APPROVE_OPTION) { //파일이 정상적으로 선택됨
				String filePath = jfc.getSelectedFile().toString();
				File file = new File(filePath);
				String fileName = file.getName().split("\\.")[0];
				
				//TODO 파일 가져오기 & 문제집 메뉴 메인 리스트 초기화
				//파일 불러오기
				FileIO ob = new FileIO();
				Workbook wb = ob.loadFile(filePath);
				
				//불러온 파일 Workbooks 폴더에 복사
				String separator = File.separator;
		    	String folderPath = Frame.getFolderPath() + separator + "Workbooks";
		    	filePath = folderPath + separator + fileName + ".workbook";
				ob.saveFile(filePath, wb);
				
				frame.sort();
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
			}
			else { //파일 선택 에러
				System.out.println("파일 선택 안됨");
			}
		}
	} //actionPerformed()
} //WbAddDlg 클래스


class QOptionDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Frame frame;
	private JLabel lblTitle = getLabel();

	public QOptionDlg(Frame frame) {
		super(frame, "문제 풀이 옵션", 300, 400);
		this.frame = frame;
		lblTitle.setText("문제집 추가");
		
		Container c = getContentPane();
		c.setLayout( new GridLayout(3, 1, 0, 0) );
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
