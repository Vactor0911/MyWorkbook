import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.io.*;

class MessageBox extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	//버튼 타입 id
	static final int btnOK = 1;
	static final int btnOK_CANCEL = 2;
	static final int btnYES_NO = 3;
	//버튼 클릭 id
	static final int idOK = 1;
	static final int idCANCEL = 2;
	static final int idYES = 3;
	static final int idNO = 4;
	//아이콘 id
	static final int iconINFORMATION = 1;
	static final int iconQUESTION = 2;
	static final int iconEXCLAMATION = 3;
	static final int iconERROR = 4;
	
	private final ImageIcon iconInformation = new ImageIcon("images/Workbook.png");
	private final ImageIcon iconQuestion = new ImageIcon("images/iconQuestion.png");
	private final ImageIcon iconExclamation = new ImageIcon("images/iconExclamation.png");
	private final ImageIcon iconError = new ImageIcon("images/iconError.png");
	
	private final Font font = new Font(Frame.getFontName(), Font.PLAIN, 15);
	
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlCenterMain = new JPanel();
	private JButton btnOk = new JButton("확인");
	private JButton btnCancel = new JButton("취소");
	private JButton btnYes = new JButton("예");
	private JButton btnNo = new JButton("아니오");
	
	private int answer = 0;
	private String msg;
	private int btnType;
	private int iconType;
	
	//JFrame
	public MessageBox(JFrame frame, String msg, int btnType, int iconType) {
		super(frame, frame.getTitle(), true);
		this.msg = msg;
		this.btnType = btnType;
		this.iconType = iconType;
		setLocation( frame.getLocation() );
		draw();
	}
	
	//JDialog
	public MessageBox(JDialog dialog, String msg, int btnType, int iconType) {
		super(dialog, dialog.getTitle(), true);
		this.msg = msg;
		this.btnType = btnType;
		this.iconType = iconType;
		setLocation( dialog.getLocation() );
		draw();
	}
	

	/**
	 * 메시지 박스 대화상자를 그린다.
	 */
	public void draw() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		JPanel pnlBase = new JPanel();
		pnlBase.setLayout( new BorderLayout() );
		pnlBase.setBackground(null);
		add(pnlBase);
		
		//버튼 객체 설정
		JButton[] aryBtn = { btnOk, btnCancel, btnYes, btnNo };
		for (int i=0; i<aryBtn.length; i++) {
			Dimension size = new Dimension(80, 30);
			aryBtn[i].setPreferredSize(size);
			aryBtn[i].setMinimumSize(size);
			aryBtn[i].setFont(font);
			aryBtn[i].addActionListener(this);
		}

		//버튼
		pnlBase.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setLayout( new FlowLayout(FlowLayout.CENTER, 5, 10) );
		pnlSouth.setBackground( new Color(220, 220, 220) );
		switch (btnType) {
			case btnOK:
				pnlSouth.add(btnOk);
				break;
			case btnOK_CANCEL:
				pnlSouth.add(btnOk);
				pnlSouth.add(btnCancel);
				break;
			case btnYES_NO:
				pnlSouth.add(btnYes);
				pnlSouth.add(btnNo);
				break;
			default:
				return;
		}
		
		//메시지
		pnlCenterMain.setLayout( new BorderLayout(5, 0) );
		pnlCenterMain.setBorder( BorderFactory.createEmptyBorder(10, 20, 10, 20) );
		pnlBase.add(pnlCenterMain, BorderLayout.CENTER);
		
		pnlCenter.setLayout( new BoxLayout(pnlCenter, BoxLayout.Y_AXIS) );
		pnlCenterMain.add(pnlCenter, BorderLayout.CENTER);
		
		String[] aryMsg = msg.split("\n");
		for (int i=0; i<aryMsg.length; i++) {
			JLabel lbl = new JLabel(aryMsg[i], SwingConstants.LEFT);
			lbl.setFont(font);
			pnlCenter.add(lbl);
		}
		
		pack();
		
		//아이콘
		Image icon = null;
		switch (iconType) {
			case iconINFORMATION:
				icon = iconInformation.getImage();
				break;
			case iconQUESTION:
				icon = iconQuestion.getImage();
				break;
			case iconEXCLAMATION:
				icon = iconExclamation.getImage();
				break;
			case iconERROR:
				icon = iconError.getImage();
				break;
			default:
				return;
		}
		
		int size = pnlCenterMain.getHeight();
		icon = icon.getScaledInstance(size, size, Image.SCALE_SMOOTH);
		JLabel lblIcon = new JLabel( new ImageIcon(icon) );
		lblIcon.setPreferredSize( new Dimension(size, size) );
		pnlCenterMain.add(lblIcon, BorderLayout.WEST);
		
		pack();
		setVisible(true);
	} //draw()
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param frame - 대화상자를 귀속시킬 JFrame 프레임
	 * @param msg - 표시할 메시지
	 * @param btnType - 표시할 버튼 레이아웃
	 * @param iconType - 표시할 아이콘
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JFrame frame, String msg, int btnType, int iconType) {
		MessageBox ob = new MessageBox(frame, msg, btnType, iconType);
		return ob.answer;
	}
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param frame - 대화상자를 귀속시킬 JFrame 프레임
	 * @param msg - 표시할 메시지
	 * @param btnType - 표시할 버튼 레이아웃
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JFrame frame, String msg, int btnType) {
		return show(frame, msg, btnType, iconINFORMATION);
	}
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param frame - 대화상자를 귀속시킬 JFrame 프레임
	 * @param msg - 표시할 메시지
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JFrame frame, String msg) {
		return show(frame, msg, btnOK, iconINFORMATION);
	}
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param dialog - 대화상자를 귀속시킬 JDialog 프레임
	 * @param msg - 표시할 메시지
	 * @param btnType - 표시할 버튼 레이아웃
	 * @param iconType - 표시할 아이콘
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JDialog dialog, String msg, int btnType, int iconType) {
		MessageBox ob = new MessageBox(dialog, msg, btnType, iconType);
		return ob.answer;
	}
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param dialog - 대화상자를 귀속시킬 JDialog 프레임
	 * @param msg - 표시할 메시지
	 * @param btnType - 표시할 버튼 레이아웃
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JDialog dialog, String msg, int btnType) {
		return show(dialog, msg, btnType, iconINFORMATION);
	}
	
	/**
	 * 메시지 박스 대화상자를 생성한다.
	 * @param dialog - 대화상자를 귀속시킬 JDialog 프레임
	 * @param msg - 표시할 메시지
	 * @return 클릭한 버튼의 id
	 */
	public static int show(JDialog dialog, String msg) {
		return show(dialog, msg, btnOK, iconINFORMATION);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource().equals(btnOk) ) {
			answer = idOK;
		}
		else if ( e.getSource().equals(btnCancel) ) {
			answer = idCANCEL;
		}
		else if ( e.getSource().equals(btnYes) ) {
			answer = idYES;
		}
		else if ( e.getSource().equals(btnNo) ) {
			answer = idNO;
		}
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
	}
} //MessageBox 클래스


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
		
		//버튼 객체 폰트 설정
		Font font = new Font(Frame.getFontName(), Font.PLAIN, 15);
		btnNewOk.setFont(font);
		btnLoad.setFont(font);
		
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
        	MessageBox.show(this, "문제집을 성공적으로 생성했습니다!");
        	
        	frame.reloadWb();
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
				MessageBox.show(this, "문제집을 성공적으로 불러왔습니다!");
				
				frame.reloadWb();
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
			}
			else { //파일 선택 에러
				System.out.println("파일 선택 안됨");
			}
		}
	} //actionPerformed()
} //WbAddDlg 클래스


class WbOptionDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final ImageIcon imageSolve = new ImageIcon("images/Workbook.png");
	private final ImageIcon imageEdit = new ImageIcon("images");
	private final ImageIcon imageExport = new ImageIcon("images");
	private final ImageIcon imageInfo = new ImageIcon("images");
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private Workbook wb;
	private ImageButton btnSolve = new ImageButton(imageSolve, "문제 풀이");
	private ImageButton btnEdit = new ImageButton(imageEdit, "수정");
	private ImageButton btnExport = new ImageButton(imageExport, "내보내기");
	private ImageButton btnInfo = new ImageButton(imageInfo, "정보");

	public WbOptionDlg(Frame frame, Workbook wb) {
		super(frame, wb.getName(), 300, 300);
		setMinimumSize( new Dimension(300, 300) );
		this.wb = wb;
		lblTitle.setText( wb.getName(15) );
		
		add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new GridLayout(4, 1, 0, 0) );
		
		ImageButton[] aryBtn = { btnSolve, btnEdit, btnExport, btnInfo };
		for (ImageButton k : aryBtn) {
			pnlCenter.add(k);
			k.addActionListener(this);
		}
		
		pack();
		setVisible(true);
	} //생성자
	
	class ImageButton extends JButton implements MouseListener {
		private static final long serialVersionUID = 1L;
		private JPanel pnlBase = new JPanel();
		private JLabel lblIcon;
		private JLabel lblText = new JLabel("", SwingConstants.CENTER);
		private String text = "";
		
		public ImageButton (ImageIcon icon, String text) {
			setBackground(null);
			setBorder(null);
			setContentAreaFilled(false);
			setPreferredSize( new Dimension(0, 60) );
			setVisible(true);
			addMouseListener(this);
			this.text = text;
			
			JPanel pnlBase = new JPanel();
			add(pnlBase, BorderLayout.CENTER);
			pnlBase.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
			pnlBase.setLayout( new BorderLayout() );
			
			Image iconBtn = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			
			lblIcon = new JLabel( new ImageIcon(iconBtn) );
			pnlBase.add(lblIcon, BorderLayout.WEST);
			
			lblText.setText(text);
			lblText.setFont( new Font(Frame.getFontName(), Font.BOLD, 20) );
			pnlBase.add(lblText, BorderLayout.CENTER);
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.HAND_CURSOR) );
			JButton btn = (JButton)e.getComponent();
			btn.setBorder( new MatteBorder( 1, 1, 1, 1, Frame.getColorBorder() ) );
		}

		@Override
		public void mouseExited(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
			JButton btn = (JButton)e.getComponent();
			btn.setBorder(null);
		}
		
		@Override
		public String getText() {
			return text;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO 버튼 클릭 이벤트 기능 구현
		if (e.getSource() == btnSolve) { //문제 풀이
		}
		else if (e.getSource() == btnEdit) { //수정
		}
		else if (e.getSource() == btnExport) { //내보내기
		}
		else if (e.getSource() == btnInfo) { //정보
		}
		
		System.out.println( ( (ImageButton)e.getSource() ).getText() );
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
	}
} //QOptionDlg 클래스
