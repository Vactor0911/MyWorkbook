import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
	
	private final ImageIcon iconInformation = new ImageIcon( getClass().getResource("images/Information.png") );
	private final ImageIcon iconQuestion = new ImageIcon( getClass().getResource("images/Question.png") );
	private final ImageIcon iconExclamation = new ImageIcon( getClass().getResource("images/Exclamation.png") );
	private final ImageIcon iconError = new ImageIcon( getClass().getResource("images/Error.png") );
	
	private final Font font = new Font(Frame.getFontName(), Font.PLAIN, 15);
	
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlCenterMain = new JPanel();
	private JButton btnOk = new JButton("확인");
	private JButton btnCancel = new JButton("취소");
	private JButton btnYes = new JButton("예");
	private JButton btnNo = new JButton("아니오");
	
	private JFrame frame;
	private JDialog dialog;
	private int answer = 0;
	private String msg;
	private int btnType;
	private int iconType;
	
	//JFrame
	public MessageBox(JFrame frame, String msg, int btnType, int iconType) {
		super(frame, frame.getTitle(), true);
		this.frame = frame;
		this.msg = msg;
		this.btnType = btnType;
		this.iconType = iconType;
		setLocation( frame.getLocation() );
		draw();
	}
	
	//JDialog
	public MessageBox(JDialog dialog, String msg, int btnType, int iconType) {
		super(dialog, dialog.getTitle(), true);
		this.dialog = dialog;
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
		
		String[] aryMsg = msg.split("\n");
		pnlCenter.setLayout( new GridLayout(aryMsg.length, 1, 0, 0) );
		pnlCenterMain.add(pnlCenter, BorderLayout.CENTER);
		
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
		
		//대화상자를 화면 정중앙에 위치
		int x = 0;
		int y = 0;
		Window wnd;
		if (frame != null) { //부모가 JFrame
			wnd = frame;
		}
		else { //부모가 JDialog
			wnd = dialog;
		}
		x = wnd.getX() + (int)(wnd.getWidth() * 0.5) - (int)(getWidth() * 0.5);
		y = wnd.getY() + (int)(wnd.getHeight() * 0.5) - (int)(getHeight() * 0.5);
		setLocation(x, y);
		
		
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
		
		moveToMid(); //대화상자를 화면 정중앙에 위치
		
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		
		c.add(pnlNorth, BorderLayout.NORTH);
		pnlNorth.setLayout( new GridLayout() );
		pnlNorth.setBackground( Frame.getColor() );
		pnlNorth.setBorder( new MatteBorder( 0, 0, 2, 0, Frame.getColorBorder() ) );
		pnlNorth.setPreferredSize( new Dimension(0, 50) );
		
		lblTitle.setFont( new Font(Frame.getFontName(), Font.BOLD, 26) );
		pnlNorth.add(lblTitle);
	} //생성자
	
	
	//getter
	public JPanel getPanel() {
		return pnlNorth;
	}
	
	public JLabel getLabel() {
		return lblTitle;
	}
	
	//대화상자를 화면 정중앙에 위치
	public void moveToMid() {
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - getWidth() / 2;
		int y = res.height / 2 - getHeight() / 2;
		setLocation(x, y);
	}
}//Dialogs 클래스


//문제집 - [정렬]
class WbSortDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final ImageIcon imgSortNameAsc = new ImageIcon( getClass().getResource("images/SortNameAscend.png") );
	private final ImageIcon imgSortNameDesc = new ImageIcon( getClass().getResource("images/SortNameDescend.png") );
	private final ImageIcon imgSortSizeAsc = new ImageIcon( getClass().getResource("images/SortSizeAscend.png") );
	private final ImageIcon imgSortSizeDesc = new ImageIcon( getClass().getResource("images/SortSizeDescend.png") );
	
	private JButton btnSortNameAsc = new JButton("이름순 (오른차순)");
	private JButton btnSortNameDesc = new JButton("이름순 (내림차순)");
	private JButton btnSortSizeAsc = new JButton("문제 적은순");
	private JButton btnSortSizeDesc = new JButton("문제 많은순");
		
	private Frame frame;
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private JButton[] aryBtn = { btnSortNameAsc, btnSortNameDesc, btnSortSizeDesc,
			btnSortSizeAsc };
	private MyMouseAdapter adapter = new MyMouseAdapter();
	
	public WbSortDlg(Frame frame) {
		super(frame, "문제집 정렬", 300, 400);
		this.frame = frame;
		lblTitle.setText("정렬");
		
		Container c = getContentPane();
		c.add(pnlCenter, BorderLayout.CENTER);
		
		pnlCenter.setLayout( new GridLayout(4, 1, 0 ,0) );
		ImageIcon[] aryImage = { imgSortNameAsc, imgSortNameDesc, imgSortSizeDesc,
				imgSortSizeAsc };
		Font font = new Font(Frame.getFontName(), Font.PLAIN, 20);
		for(int i=0; i<aryBtn.length; i++) {
			Image image = aryImage[i].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
			JButton btn = aryBtn[i];
			btn.setIcon( new ImageIcon(image) );
			btn.setPreferredSize( new Dimension(300, 60) );
			btn.setMinimumSize( new Dimension(300, 60) );
			btn.setFont(font);
			btn.setBackground(null);
			btn.setBorder(null);
			btn.setContentAreaFilled(false);
			btn.addActionListener(this);
			btn.addMouseListener(adapter);
			pnlCenter.add(btn);
		}
		
		setVisible(true);
		moveToMid();
	} //생성자
	
	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.HAND_CURSOR) );
			JButton btn = (JButton)e.getComponent();
			btn.setBorder( new MatteBorder(1, 1, 1, 1, Frame.getColorBorder() ) );
		}

		@Override
		public void mouseExited(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
			JButton btn = (JButton)e.getComponent();
			btn.setBorder(null);
		}
	}
	
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
	private ColorComboBox cbColor = new ColorComboBox();
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
			
			String fileName = LocalDate.now( ) + "_" + LocalTime.now().toString().split("\\.")[0];
			fileName = fileName.replace("-", "_");
			fileName = fileName.replace(":", "_");
			Color fileColor = cbColor.getColor();
			
			//파일 새로 생성
			String separator = File.separator;
	    	String folderPath = Frame.getFolderPath() + separator + "Workbooks";
	    	String filePath = folderPath + separator + fileName + ".workbook";
	    	
    		Workbook wb = new Workbook( tfNewTitle.getText().trim() , fileColor);
    		FileIO.saveFile(filePath, wb);
        	MessageBox.show(this, "문제집을 성공적으로 생성했습니다!");
        	
        	frame.reloadWb();
	    	this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
		}
		else if (e.getSource() == btnLoad) { //불러오기
			UIManager.put("FileChooser.cancelButtonText","취소");
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileHidingEnabled(true);
			jfc.setApproveButtonText("열기");
			jfc.setCurrentDirectory( FileSystemView.getFileSystemView().getHomeDirectory() );
			FileNameExtensionFilter filter = new FileNameExtensionFilter("문제집 파일 (*.workbook)",
					"workbook");
			jfc.setFileFilter(filter);
			jfc.setDialogTitle("문제집 불러오기");
			
			int result = jfc.showOpenDialog(null); //파일 선택기 열기
			
			if (result == JFileChooser.APPROVE_OPTION) { //파일이 정상적으로 선택됨
				String filePath = jfc.getSelectedFile().toString();
				File file = new File(filePath);
				String fileName = file.getName().split("\\.")[0];
				
				//파일 불러오기
				Workbook wb = FileIO.loadFile(filePath);
				
				//불러온 파일 Workbooks 폴더에 복사
				String separator = File.separator;
		    	String folderPath = Frame.getFolderPath() + separator + "Workbooks";
		    	filePath = folderPath + separator + fileName + ".workbook";
		    	FileIO.saveFile(filePath, wb);
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
	private final ImageIcon imageSolve = new ImageIcon( getClass().getResource("images/WbSolving.png") );
	private final ImageIcon imageEdit = new ImageIcon( getClass().getResource("images/WbEdit.png") );
	private final ImageIcon imageExport = new ImageIcon( getClass().getResource("images/WbExport.png") );
	private final ImageIcon imageDelete = new ImageIcon( getClass().getResource("images/Delete.png") );
	private final ImageIcon imageInfo = new ImageIcon( getClass().getResource("images/Information.png") );
	
	private Frame frame;
	private String filePath;
	private Workbook wb;
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private ImageButton btnSolve = new ImageButton(imageSolve, "문제 풀이");
	private ImageButton btnEdit = new ImageButton(imageEdit, "문제 수정");
	private ImageButton btnExport = new ImageButton(imageExport, "내보내기");
	private ImageButton btnDelete = new ImageButton(imageDelete, "삭제");
	private ImageButton btnInfo = new ImageButton(imageInfo, "정보");

	public WbOptionDlg(Frame frame, String filePath) {
		super(frame, "", 300, 300);
		setMinimumSize( new Dimension(300, 300) );
		this.frame = frame;
		this.filePath = filePath;
		this.wb = FileIO.loadFile(filePath);
		setTitle( wb.getName() );
		lblTitle.setText( wb.getName() );
		
		add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new GridLayout(5, 1, 0, 0) );
		
		ImageButton[] aryBtn = { btnSolve, btnEdit, btnExport, btnDelete, btnInfo };
		for (ImageButton k : aryBtn) {
			pnlCenter.add(k);
			k.addActionListener(this);
		}
		
		pack();
		setVisible(true);
	} //생성자
	
	
	class ImageButton extends JButton {
		private static final long serialVersionUID = 1L;
		private JLabel lblIcon;
		private JLabel lblText = new JLabel("", SwingConstants.CENTER);
		private String text = "";
		
		public ImageButton (ImageIcon icon, String text) {
			setBackground(null);
			setBorder(null);
			setContentAreaFilled(false);
			setPreferredSize( new Dimension(0, 60) );
			setVisible(true);
			addMouseListener( new MyMouseAdapter() );
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

		private class MyMouseAdapter extends MouseAdapter {
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
		} //MyMouseAdapter 클래스
		
		
		@Override
		public String getText() {
			return text;
		}
	} //ImageButton 클래스
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSolve) { //문제 풀이
			if (wb.getQuestion().size() > 0) {
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
				new QOptionDlg(frame, filePath);
				return;
			}
			else {
				MessageBox.show(frame, "등록된 문제가 없습니다!", MessageBox.btnOK,
						MessageBox.iconEXCLAMATION);
				return;
			}
		}
		else if (e.getSource() == btnEdit) { //문제 수정
			frame.setMenu("Question", filePath);
		}
		else if (e.getSource() == btnExport) { //내보내기
			UIManager.put("FileChooser.cancelButtonText","취소");
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileHidingEnabled(true);
			jfc.setCurrentDirectory( FileSystemView.getFileSystemView().getHomeDirectory() );
			jfc.setApproveButtonText("저장");
			jfc.setDialogTitle("문제집 내보내기");
			
			int result = jfc.showOpenDialog(null); //파일 선택기 열기
			
			if (result == JFileChooser.APPROVE_OPTION) { //파일이 정상적으로 선택됨
				String folderPath = jfc.getSelectedFile().toString();
				String fileName = LocalDate.now( ) + "_" + LocalTime.now().toString().split("\\.")[0];
				fileName = fileName.replace("-", "_");
				fileName = fileName.replace(":", "_");
				String filePath = folderPath + File.separator + fileName + ".workbook";
				FileIO.saveFile(filePath, wb);
				
				this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
				MessageBox.show(frame, "문제집이 성공적으로 내보내졌습니다!");
				return;
			}
		}
		else if (e.getSource() == btnDelete) { //삭제
			if ( MessageBox.show(this, "문제집을 삭제하시겠습니까?", MessageBox.btnYES_NO,
					MessageBox.iconQUESTION) == MessageBox.idYES ) {
				File file = new File(filePath);
				file.delete();
				frame.reloadWb();
				MessageBox.show(this, "문제집이 성공적으로 삭제되었습니다.");
			}
			else {
				return;
			}
		}
		else if (e.getSource() == btnInfo) { //정보
			this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
			new WbInfoDlg(frame, filePath);
			return;
		}
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
	}
} //WbOptionDlg 클래스


class WbInfoDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Font boldFont = new Font(Frame.getFontName(), Font.BOLD, 20);
	
	private Frame frame;
	private String filePath;
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private Workbook wb;
	
	private JLabel lblName = new JLabel("이름");
	private HintTextField tfName = new HintTextField("제목을 입력하세요");
	
	private JLabel lblColor = new JLabel("색상");
	private JTextField tfColor = new JTextField("");
	private ColorComboBox cbColor = new ColorComboBox();
	
	private JLabel lblDate = new JLabel("생성 날짜");
	private JTextField tfDate = new JTextField("");
	
	private JButton btnEdit = new JButton("수정");
	private JButton btnOk = new JButton("확인");
	private JButton btnCancel = new JButton("취소");

	public WbInfoDlg(Frame frame, String filePath) {
		super(frame, "", 400, 300);
		this.frame = frame;
		this.filePath = filePath;		
		this.wb = FileIO.loadFile(filePath);
		lblTitle.setText( wb.getName() );
		addWindowListener( new MyWindowAdapter() );
		
		pnlCenter.setLayout( new BoxLayout(pnlCenter, BoxLayout.Y_AXIS) );
		pnlCenter.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		add(pnlCenter, BorderLayout.CENTER);
		
		addGroupLabel(pnlCenter, lblName);
		addGap(5);
		
		addTextField(pnlCenter, tfName);
		tfName.setText( wb.getName() );
		tfName.setEditable(false);
		addGap(15);
		
		addGroupLabel(pnlCenter, lblColor);
		addGap(5);
		
		pnlCenter.add(tfColor);
		tfColor.setEditable(false);
		tfColor.setPreferredSize( new Dimension(300, 40) );
		tfColor.setMinimumSize( new Dimension(300, 40) );
		tfColor.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
		tfColor.setBackground( wb.getColor() );
		
		pnlCenter.add(cbColor);
		cbColor.setVisible(false);
		addGap(15);
		
		addGroupLabel(pnlCenter, lblDate);
		addTextField(pnlCenter, tfDate);
		tfDate.setEditable(false);
		String date = new File(filePath).getName();
		date = date.split("\\.")[0];
		String[] aryDate = date.split("_");
		date = aryDate[0] + "-" + aryDate[1] + "-" + aryDate[2] + "   " + aryDate[3]
				+ ":" + aryDate[4] + ":" + aryDate[5];
		tfDate.setText(date);
		addGap(15);
		
		JPanel pnlBtn = new JPanel();
		pnlBtn.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 0) );
		pnlCenter.add(pnlBtn);
		
		JButton[] aryBtn = { btnEdit, btnOk, btnCancel };
		for (JButton btn : aryBtn) {
			btn.setPreferredSize( new Dimension(100, 50) );
			btn.setFont(boldFont);
			btn.setBackground( Frame.getColor() );
			btn.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			btn.addActionListener(this);
			pnlBtn.add(btn);
		}
		btnOk.setVisible(false);
		btnCancel.setVisible(false);
		
		pack();
		moveToMid();
		setVisible(true);
	} //생성자
	
	class MyWindowAdapter extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			new WbOptionDlg(frame, filePath);
		}
	}
	
	//그룹 라벨 추가
	private void addGroupLabel(JPanel pnl, JLabel lbl) {
		lbl.setFont(boldFont);
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new GridLayout() );
		pnlTemp.add(lbl);
		pnl.add(pnlTemp);
	}
	
	//텍스트 필드 추가
	private void addTextField(JPanel pnl, JTextField tf) {
		tf.setPreferredSize( new Dimension(300, 40) );
		tf.setMinimumSize( new Dimension(300, 40) );
		tf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
		tf.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
		pnl.add(tf);
	}
	
	//공백 추가
	private void addGap(int gap) {
		pnlCenter.add( Box.createRigidArea( new Dimension(0, gap) ) );
	}
	
	private void setEdit(boolean edit) {
		btnEdit.setVisible(edit);
		btnOk.setVisible(!edit);
		btnCancel.setVisible(!edit);
		
		tfName.setEditable(!edit);
		tfColor.setVisible(edit);
		cbColor.setVisible(!edit);
		validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnEdit) { //수정
			setEdit(false);
			cbColor.setSelectedIndex( cbColor.getColorIndex( wb.getColor() ) );
		}
		else if (e.getSource() == btnOk) { //확인
			if ( MessageBox.show(frame, "문제집 정보를 수정하시겠습니까?", MessageBox.btnYES_NO,
					MessageBox.iconQUESTION) == MessageBox.idYES ) {
				setEdit(true);
				wb.setName( tfName.getText().trim() );
				wb.setColor( cbColor.getColor() );
				FileIO.saveFile(filePath, wb);
				lblTitle.setText( wb.getName() );
				frame.reloadWb();
				tfColor.setBackground( cbColor.getColor() );
			}
		}
		else { //취소
			setEdit(true);
		}
	}
} //WbInfoDlg 클래스


class QuestionDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	static final int idADD = 1;
	static final int idEDIT = 2;
	private final Font boldFont = new Font(Frame.getFontName(), Font.BOLD, 20);
	private final ImageIcon imagePicture = new ImageIcon( getClass().getResource("images/Picture.png") );
	private final Image resizedImage = imagePicture.getImage().getScaledInstance(30, 30,
			Image.SCALE_SMOOTH);
	
	private Frame frame;
	private String filePath;
	private Workbook wb;
	private int index;
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	
	private JLabel lblCategory = new JLabel("문제 유형", SwingConstants.LEFT);
	private String[] aryCategory = { "선택형", "서술형" };
	private MyComboBox cbCategory = new MyComboBox(aryCategory);
	
	private JLabel lblQuestion = new JLabel("문제");
	private HintTextField tfTitle = new HintTextField("문제를 입력하세요");
	private JButton btnPicture = new JButton("이미지 선택");
	private JLabel lblPicture = new JLabel();
	
	private JLabel lblAnswer = new JLabel("정답");
	private HintTextField tfAnswer = new HintTextField("정답을 입력하세요");
	
	private JPanel pnlOption = new JPanel();
	private JLabel lblWrongAns = new JLabel("오답 선택지");
	private ArrayList<HintTextField> listOption = new ArrayList<>();
	
	private JLabel lblExplain = new JLabel("해설");
	private HintTextField tfExplain = new HintTextField("해설을 입력하세요 (선택)");
	
	private JButton btnAdd = new JButton("추가하기");
	private JButton btnEdit = new JButton("수정하기");
	private JButton btnDelete = new JButton("삭제하기");
	
	private MyMouseAdapter mouseAdapter = new MyMouseAdapter();
	private MyKeyAdapter keyAdapter = new MyKeyAdapter();
	private ImageIcon image;

	public QuestionDlg(Frame frame, String filePath, int index) {
		super(frame, "", 400, 300);
		this.frame = frame;
		this.filePath = filePath;
		this.wb = FileIO.loadFile(filePath);
		this.index = index;
		boolean flagAdd = index == -1;
		Question question = null;
		
		if (flagAdd) {
			setTitle("문제 추가");
			lblTitle.setText("문제 추가");
		}
		else {
			setTitle("문제 수정");
			lblTitle.setText("문제 수정");
			question = wb.getQuestion().get(index);
		}
		
		JButton[] aryBtn = { btnPicture, btnAdd, btnEdit, btnDelete };
		for (JButton btn : aryBtn) {
			btn.addActionListener(this);
			btn.addMouseListener(mouseAdapter);
		}
		
		add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new BoxLayout(pnlCenter, BoxLayout.Y_AXIS) );
		pnlCenter.setBorder( BorderFactory.createEmptyBorder(10, 10, 10 ,10) );
		
		//문제 유형
		addGroupLabel(pnlCenter, lblCategory);
		addGap(5);
		
		cbCategory.setPreferredSize( new Dimension(400, 35) );
		pnlCenter.add(cbCategory);
		if (!flagAdd) {
			cbCategory.setSelectedIndex( question.getCategory() );
		}
		addGap(15);
		
		//문제
		addGroupLabel(pnlCenter, lblQuestion);
		addGap(5);
		
		addTextField(pnlCenter, tfTitle);
		if (!flagAdd) {
			tfTitle.setText( question.getTitle() );
			tfTitle.focusLost(null);
		}
		addGap(5);
		
		//이미지 선택
		btnPicture.setIcon( new ImageIcon(resizedImage) );
		btnPicture.setFont( new Font(Frame.getFontName(), Font.BOLD, 15) );
		btnPicture.setBackground( Frame.getColor() );
		Border emptyBorder = BorderFactory.createEmptyBorder(3, 8, 3, 8);
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK, 1, false);
		btnPicture.setBorder( BorderFactory.createCompoundBorder(lineBorder, emptyBorder) );
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new FlowLayout(FlowLayout.RIGHT, 10, 0) );
		pnlTemp.add(lblPicture);
		pnlTemp.add(btnPicture);
		pnlCenter.add(pnlTemp);
		if (!flagAdd) {
			image = question.getImage();
			lblPicture.setIcon(image);
		}
		addGap(10);
		
		//정답
		addGroupLabel(pnlCenter, lblAnswer);
		addGap(5);
		
		addTextField(pnlCenter, tfAnswer);
		if (!flagAdd) {
			tfAnswer.setText( question.getAnswer() );
			tfAnswer.focusLost(null);
		}
		addGap(15);
		
		//오답 선택지
		pnlOption.setLayout( new BoxLayout(pnlOption, BoxLayout.Y_AXIS) );
		pnlOption.setMinimumSize( new Dimension() );
		pnlCenter.add(pnlOption);
		
		addGroupLabel(pnlOption, lblWrongAns);
		pnlOption.add( Box.createRigidArea( new Dimension(0, 5) ) );
		
		HintTextField htf = new HintTextField("오답을 입력하세요");
		htf.setPreferredSize( new Dimension(400, 40) );
		htf.setMinimumSize( new Dimension(400, 40) );
		htf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
		htf.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
		htf.addKeyListener(keyAdapter);
		if (!flagAdd && cbCategory.getSelectedIndex() == 0) {
			htf.setText( question.getAryOption()[0] );
		}
		listOption.add(htf);
		pnlOption.add(htf);
		
	
		if (!flagAdd && cbCategory.getSelectedIndex() == 0) {
			String[] aryOption = question.getAryOption();
			for (int i=1; i<aryOption.length; i++) {
				pnlOption.add( Box.createRigidArea( new Dimension(0, 5) ) );
				htf = new HintTextField("오답을 입력하세요 (선택)");
				htf.setPreferredSize( new Dimension(400, 40) );
				htf.setMinimumSize( new Dimension(400, 40) );
				htf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
				htf.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
				htf.setText( aryOption[i] );
				htf.addKeyListener(keyAdapter);
				htf.focusLost(null);
				listOption.add(htf);
				pnlOption.add(htf);
			}
		}
		if(cbCategory.getSelectedIndex() == 1) {
			pnlOption.setVisible(false);
		}
		addGap(15);
		
		//해설
		lblExplain.setFont(boldFont);
		addGroupLabel(pnlCenter, lblExplain);
		addGap(5);
		
		addTextField(pnlCenter, tfExplain);
		if (!flagAdd) {
			tfExplain.setText( question.getExplain() );
			tfExplain.focusLost(null);
		}
		addGap(15);
		
		//버튼
		JPanel pnlBtn = new JPanel();
		pnlBtn.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 0) );
		pnlCenter.add(pnlBtn);
		
		if (flagAdd) {
			btnAdd.setPreferredSize( new Dimension(150, 50) );
			btnAdd.setFont(boldFont);
			btnAdd.setBackground( Frame.getColor() );
			btnAdd.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			pnlBtn.add(btnAdd);
		}
		else {
			JButton[] aryTemp = { btnEdit, btnDelete };
			for (JButton btn : aryTemp) {
				btn.setPreferredSize( new Dimension(150, 50) );
				btn.setFont(boldFont);
				btn.setBackground( Frame.getColor() );
				btn.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
				pnlBtn.add(btn);
			}
			btnDelete.setBackground(Color.RED);
		}
		
		pack();
		setVisible(true);
	} //생성자
	
	class MyComboBox extends CustomComboBox implements ItemListener {
		private static final long serialVersionUID = 1L;
		private String[] aryItem;

		public MyComboBox(String[] aryItem) {
			super(aryItem);
			addItemListener(this);
		} //생성자
		
		public String getText() {
			return aryItem[ getSelectedIndex() ];
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if( e.getStateChange() == ItemEvent.SELECTED ) {
				pnlOption.setVisible(cbCategory.getSelectedIndex() == 0);
				pnlCenter.validate();
				pack();
			}
		}
	} //MyComboBox 클래스
	
	
	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.HAND_CURSOR) );
		}

		@Override
		public void mouseExited(MouseEvent e) {
			getContentPane().setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		}
	} //MyMouseAdapter 클래스
	
	
	class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			drawOption();
		}
	} //MyKeydapter 클래스
	

	//문제 추가
	public static void addQuestion(Frame frame, String filePath) {
		new QuestionDlg(frame, filePath, -1);
	}
	
	//문제 수정
	public static void editQuestion(Frame frame, String filePath, int index) {
		new QuestionDlg(frame, filePath, index);
	}
	
	//공백 추가
	private void addGap(int gap) {
		pnlCenter.add( Box.createRigidArea( new Dimension(0, gap) ) );
	}
	
	//그룹 라벨 추가
	private void addGroupLabel(JPanel pnl, JLabel lbl) {
		lbl.setFont(boldFont);
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new GridLayout() );
		pnlTemp.add(lbl);
		pnl.add(pnlTemp);
	}
	
	//텍스트 필드 추가
	private void addTextField(JPanel pnl, JTextField tf) {
		tf.setPreferredSize( new Dimension(400, 40) );
		tf.setMinimumSize( new Dimension(400, 40) );
		tf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
		tf.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
		pnl.add(tf);
	}
	
	private void drawOption() {
		boolean flagAdd = false;
		for (int i=listOption.size()-1; i>=0; i--) {
			if (i == listOption.size() - 1) {
				if ( !listOption.get(i).getText().isEmpty() ) {
					flagAdd = true;
				}
			}
			if (i > 0) {
				if ( listOption.get(i-1).getText().isEmpty() ) {
					listOption.remove(i);
					pnlOption.remove( (i+1) * 2);
					pnlOption.remove( (i+1) * 2 - 1);
				}
			}
		}
		
		if (flagAdd && listOption.size() < 4) {
			pnlOption.add( Box.createRigidArea( new Dimension(0, 5) ) );
			HintTextField htf = new HintTextField("오답을 입력하세요 (선택)");
			htf.setPreferredSize( new Dimension(400, 40) );
			htf.setMinimumSize( new Dimension(400, 40) );
			htf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
			htf.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			htf.addKeyListener(keyAdapter);
			listOption.add(htf);
			pnlOption.add(htf);
		}
		pnlOption.validate();
		pack();
	}
	
	@Override
	public void pack() {
		super.pack();
		moveToMid();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd || e.getSource() == btnEdit) { //문제 추가 & 문제 수정
			//문제 수정 - 확인 메시지
			if (e.getSource() == btnEdit) {
				if ( MessageBox.show(this, "문제를 수정하시겠습니까?", MessageBox.btnYES_NO,
						MessageBox.iconQUESTION) == MessageBox.idNO ) {
					return;
				}
			}
			
			//문제 유형
			int category = cbCategory.getSelectedIndex();
			
			//문제
			String title = tfTitle.getText().trim();
			tfTitle.warn( title.isEmpty() );
			
			//정답
			String answer = tfAnswer.getText().trim();
			tfAnswer.warn( answer.isEmpty() );
			
			//오답 선택지
			String[] aryOption = null;
			if (category == 0) { //문제 형식이 '선택식'일 경우
				String option = listOption.get(0).getText().trim();
				listOption.get(0).warn( option.isEmpty() );
			
				aryOption = new String[ listOption.size() ];
				for (int i=0; i<listOption.size(); i++) {
					String strOption = listOption.get(i).getText();
					if ( !strOption.isEmpty() ) {
						aryOption[i] = strOption;
					}
				}
			}
			
			//부적절한 입력시 종료
			if ( title.isEmpty() || answer.isEmpty() ) {
				return;
			}
			if (category == 0) {
				if ( listOption.get(0).getText().isEmpty() ) {
					return;
				}
			}
			
			//해설
			String explain = tfExplain.getText();
			
			Question q = new Question(category, title, answer, aryOption, explain, image);
			if (e.getSource() == btnAdd) {
				wb.getQuestion().add(q);
			}
			else if (e.getSource() == btnEdit) {
				wb.getQuestion().remove(index);
				wb.getQuestion().add(index, q);
			}
			FileIO.saveFile(filePath, wb); //생성한 데이터 덮어쓰기
			frame.setMenu("Question", filePath); //문제 불러오기
		}
		else if (e.getSource() == btnDelete) { //문제 삭제
			if ( MessageBox.show(this, "문제를 삭제하시겠습니까?", MessageBox.btnYES_NO,
					MessageBox.iconQUESTION) == MessageBox.idYES ) {
				wb.getQuestion().remove(index);
				FileIO.saveFile(filePath, wb); //생성한 데이터 덮어쓰기
				frame.setMenu("Question", filePath); //문제 불러오기
			}
		}
		else { //이미지 선택
			UIManager.put("FileChooser.cancelButtonText","취소");
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileHidingEnabled(true);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"이미지 파일 (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");
			jfc.setFileFilter(filter);
			jfc.setApproveButtonText("선택");
			jfc.setDialogTitle("이미지 선택");
			
			int result = jfc.showOpenDialog(null); //파일 선택기 열기
			
			if (result == JFileChooser.APPROVE_OPTION) { //파일이 정상적으로 선택됨
				String imagePath = jfc.getSelectedFile().toString();
				image = new ImageIcon(imagePath); //이미지 저장
				//화면 표시용 크기 조절된 이미지
				int width = image.getIconWidth();
				int height = image.getIconHeight();
				int maxSize = Math.max(width, height);
				double multiplier = (double)150 / (double)maxSize;
				Image resizedImage = image.getImage().getScaledInstance(
						(int)(width * multiplier), (int)(height * multiplier),
						Image.SCALE_SMOOTH);
				lblPicture.setIcon( new ImageIcon(resizedImage) );
				pnlCenter.validate();
				pack();
			}
			return;
		}
		
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
	}
	
} //QAddDlg 클래스


class QOptionDlg extends Dialogs implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final Font boldFont = new Font(Frame.getFontName(), Font.BOLD, 20);
	
	private JLabel lblTitle = getLabel();
	private JPanel pnlCenter = new JPanel();
	private Frame frame;
	private String filePath;
	private Workbook wb;
	private boolean flagClose = false;
	
	private JLabel lblOrder = new JLabel("출제 순서");
	private JRadioButton rbOrderNormal = new JRadioButton("일반");
	private JRadioButton rbOrderRandom = new JRadioButton("랜덤");
	private JPanel pnlOrder = new JPanel();
	
	private JLabel lblRange = new JLabel("출제 문제수");
	private JButton btnRangeSub = new JButton("-1");
	private JButton btnRangeSub10 = new JButton("-10");
	private JButton btnRangeSum = new JButton("+1");
	private JButton btnRangeSum10 = new JButton("+10");
	private MyTextField tfRange = new MyTextField("전체");
	private JPanel pnlRange = new JPanel();
	
	private JLabel lblNotice = new JLabel("정답 및 해설");
	private JCheckBox checkNotice = new JCheckBox("정답 여부 및 해설 표시");
	
	private JButton btnOk = new JButton("풀이 시작");

	public QOptionDlg(Frame frame, String filePath) {
		super(frame, "문제 풀이 설정", 300, 500);
		this.frame = frame;
		this.filePath = filePath;
		this.wb = FileIO.loadFile(filePath);
		addWindowListener( new MyWindowAdapter() );
		
		lblTitle.setText( wb.getName() );
		pnlCenter.setLayout( new BoxLayout(pnlCenter, BoxLayout.Y_AXIS) );
		pnlCenter.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		add(pnlCenter, BorderLayout.CENTER);

		//출제 순서
		addGroupLabel(lblOrder);
		
		pnlOrder.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 0) );
		pnlCenter.add(pnlOrder);
		
		JRadioButton[] aryRb = { rbOrderNormal, rbOrderRandom };
		ButtonGroup groupOrder = new ButtonGroup();
		for (JRadioButton rb : aryRb) {
			rb.setFont( new Font(Frame.getFontName(), Font.PLAIN, 18) );
			pnlOrder.add(rb);
			groupOrder.add(rb);
		}
		rbOrderNormal.setSelected(true);
		addGap(15);
		
		//출제 범위
		addGroupLabel(lblRange);
		
		pnlRange.setLayout( new FlowLayout(FlowLayout.CENTER, 5, 0) );
		pnlCenter.add(pnlRange);
		
		Component[] aryRangeObj = { btnRangeSub, btnRangeSub10, tfRange, btnRangeSum10,
				btnRangeSum };
		for (Component obj : aryRangeObj) {
			Dimension size;
			switch ( obj.getClass().getName() ) {
				case "javax.swing.JButton":
					JButton btn = (JButton)obj;
					btn.setFont( new Font(Frame.getFontName(), Font.BOLD, 16) );
					btn.setBackground( Frame.getColor() );
					btn.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
					size = new Dimension(40, 30);
					btn.setPreferredSize(size);
					btn.setMinimumSize(size);
					btn.addActionListener(this);
					break;
				default:
					MyTextField tf = (MyTextField)obj;
					tf.setFont( new Font(Frame.getFontName(), Font.PLAIN, 16) );
					size = new Dimension(100, 30);
					tf.setPreferredSize(size);
					tf.setMinimumSize(size);
					int max = wb.getQuestion().size();
					tf.setMax(max);
					tf.setNum(max);
					break;
			}
			pnlRange.add(obj);
		}
		addGap(15);
		
		//정답 알림 표시
		addGroupLabel(lblNotice);
		
		JPanel pnlCorrect = new JPanel();
		pnlCorrect.setLayout( new FlowLayout(FlowLayout.RIGHT, 0, 0) );
		pnlCenter.add(pnlCorrect);
		
		checkNotice.setFont( new Font(Frame.getFontName(), Font.BOLD, 16) );
		checkNotice.setSelected(true);
		pnlCorrect.add(checkNotice);
		addGap(15);
		
		//구분선
		JPanel line = new JPanel();
		line.setBorder( BorderFactory.createLineBorder(Frame.getColorBorder(), 2, false) );
		Dimension sizeLine = new Dimension(300, 2);
		line.setPreferredSize(sizeLine);
		line.setMaximumSize(sizeLine);
		pnlCenter.add(line);
		addGap(15);
		
		//풀이 시작 버튼
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new FlowLayout(FlowLayout.CENTER, 0, 0) );
		pnlCenter.add(pnlTemp);
		
		btnOk.setFont(boldFont);
		btnOk.setBackground( Frame.getColor() );
		btnOk.setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
		Dimension size = new Dimension(150, 40);
		btnOk.setPreferredSize(size);
		btnOk.setMinimumSize(size);
		btnOk.addActionListener(this);
		pnlTemp.add(btnOk);
		
		pack();
		moveToMid();
		setVisible(true);
	} //생성자

	class MyWindowAdapter extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			if (!flagClose) {
				new WbOptionDlg(frame, filePath);
			}
		}
	} //MyWindowAdapter 클래스
	
	class MyTextField extends JTextField {
		private static final long serialVersionUID = 1L;
		private int num;
		private int min = 1;
		private int max = 100;

		public MyTextField(String text) {
			this(text, 1, 100);
		} //생성자
		
		public MyTextField(String text, int min, int max) {
			super(text);
			this.min = min;
			this.max = max;
			addKeyListener( new MyKeyAdapter() );
			addFocusListener( new MyFocusAdapter() );
		} //생성자
		
		class MyKeyAdapter extends KeyAdapter {
			@Override
			public void keyReleased(KeyEvent e) {
				switch ( e.getKeyCode() ) {
					case KeyEvent.VK_ENTER: case KeyEvent.VK_SPACE: case KeyEvent.VK_BACK_SPACE:
					case KeyEvent.VK_DELETE: case KeyEvent.VK_TAB:
						setNum( getText() );
						break;
				}
			}
		} //MyKeyAdapter 클래스
		
		class MyFocusAdapter extends FocusAdapter {
			@Override
			public void focusLost(FocusEvent e) {
				setNum( getText() );
			}
		} //MyFocusAdapter 클래스
		
		//setter
		public void setNum(int num) {
			this.num = clamp(num, min, max);
			if (this.num >= max) {
				setText("전체");
			}
			else {
				setText( Integer.toString(this.num) );
			}
		}
		
		public void setNum(String num) {
			int parsedNum = this.num;
			try {
				parsedNum = Integer.parseInt( num.replace(" ", "") );
			}
			catch(Exception e1) {}
			setNum(parsedNum);
		}
		
		public void setMin(int min) {
			this.min = min;
		}
		
		public void setMax(int max) {
			this.max = max;
		}
		
		//getter
		public int getNum() {
			return num;
		}
		
		public int clamp(int num, int min, int max) {
			if (num <= min) {
				return min;
			}
			else if (num >= max) {
				return max;
			}
			else {
				return num;
			}
		}
	}
	
	//공백 추가
	private void addGap(int gap) {
		pnlCenter.add( Box.createRigidArea( new Dimension(0, gap) ) );
	}
	
	//그룹 라벨 추가
	private void addGroupLabel(JLabel lbl) {
		lbl.setFont(boldFont);
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new GridLayout() );
		pnlTemp.add(lbl);
		pnlCenter.add(pnlTemp);
		addGap(5);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int num = tfRange.getNum();
		if (e.getSource() == btnRangeSum) {
			tfRange.setNum(num + 1);
		}
		else if (e.getSource() == btnRangeSum10) {
			tfRange.setNum(num + 10);
		}
		else if (e.getSource() == btnRangeSub) {
			tfRange.setNum(num - 1);
		}
		else if (e.getSource() == btnRangeSub10) {
			tfRange.setNum(num - 10);
		}
		else if (e.getSource() == btnOk) {
			boolean order = rbOrderRandom.isSelected();
			int numQuestion = tfRange.getNum();
			boolean notice = checkNotice.isSelected();

			flagClose = true;
			frame.startSolve(filePath, order, numQuestion, notice);
			this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
		}
	}
} //QOptionDlg 클래스


class QExplainDlg extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final ImageIcon imageCorrect = new ImageIcon( getClass().getResource("images/CorrectAnswer.png") );
	private final ImageIcon imageWrong = new ImageIcon( getClass().getResource("images/WrongAnswer.png") );
	
	private Frame frame;
	private JPanel pnlMain = new JPanel();
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JLabel lblCorrectImg = new JLabel();
	private JLabel lblCorrect = new JLabel();
	private JLabel lblAnswer = new JLabel("", SwingConstants.CENTER);
	private JLabel lblExplain = new JLabel("", SwingConstants.CENTER);
	private JButton btnQNext = new JButton("다음 문제");
	
	public QExplainDlg(Frame frame, Question question, boolean correct) {
		super(frame, "", false);
		this.frame = frame;
		setTitle("문제 해설");
		setSize(500, 100);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setMaximumSize( new Dimension(500, 800) );
		addWindowListener( new MyWindowAdapter() );
		
		setContentPane(pnlMain);
		pnlMain.setLayout( new BorderLayout(0, 10) );
		pnlMain.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
		
		pnlNorth.setLayout( new FlowLayout(FlowLayout.CENTER, 5, 0) );
		pnlMain.add(pnlNorth, BorderLayout.NORTH);
		
		//정답 | 오답 아이콘, 라벨
		Image image = null;
		if (correct) {
			image = imageCorrect.getImage();
			lblCorrect.setText("정답입니다!");
		}
		else {
			image = imageWrong.getImage();
			lblCorrect.setText("오답입니다!");
		}
		image = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		lblCorrectImg.setIcon( new ImageIcon(image) );
		pnlNorth.add(lblCorrectImg);
		lblCorrect.setFont( new Font(Frame.getFontName(), Font.BOLD, 20) );
		pnlNorth.add(lblCorrect);
		
		pnlCenter.setLayout( new BorderLayout(0, 10) );
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		//정답 공개
		JPanel pnlTemp = new JPanel();
		pnlTemp.setLayout( new GridLayout() );
		lblAnswer.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
		lblAnswer.setText("<html><p>" + question.getAnswer() + "</p></html>");
		pnlTemp.add(lblAnswer);
		pnlCenter.add(pnlTemp, BorderLayout.NORTH);
		
		//해설 라벨
		if ( !question.getExplain().isEmpty() ) {
			lblExplain.setText("<html><p>" + question.getExplain() + "</p></html>");
			lblExplain.setFont( new Font(Frame.getFontName(), Font.PLAIN, 20) );
			Border borderEmpty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			Border borderLine = BorderFactory.createLineBorder(Color.BLACK, 1, true);
			Border borderTitled = BorderFactory.createTitledBorder( 
					BorderFactory.createCompoundBorder(borderLine, borderEmpty), "해설",
					TitledBorder.LEFT, TitledBorder.TOP, new Font(Frame.getFontName(), Font.BOLD, 16)
					);
			lblExplain.setBorder(borderTitled);
			pnlCenter.add(lblExplain, BorderLayout.CENTER);
		}
		
		//다음 문제 버튼
		btnQNext.setFont( new Font(Frame.getFontName(), Font.BOLD, 20) );
		btnQNext.setPreferredSize( new Dimension(0, 50) );
		btnQNext.setMinimumSize( new Dimension(0, 50) );
		btnQNext.addActionListener(this);
		pnlMain.add(btnQNext, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
		int height = question.getAnswer().length() + question.getExplain().length();
		setSize( new Dimension(500, getHeight() + height) );
		
		//대화상자를 화면 정 중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - getWidth() / 2;
		int y = res.height / 2 - getHeight() / 2;
		setLocation(x, y);
	} //생성자
	
	public void exit() {
		frame.showNextQuestion();
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
	}
	
	class MyWindowAdapter extends WindowAdapter {
		@Override
		public void windowClosed(WindowEvent e) {
			exit();
		}
	} //MyWindowAdapter 클래스	

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispatchEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) ); //대화상자 종료
	}
} //QExplainDlg 클래스

