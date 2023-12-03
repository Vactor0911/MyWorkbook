import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.border.*;
import java.io.*;

public class Frame extends JFrame implements ActionListener, MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	//이미지
	private static final ImageIcon imageProgramIcon = new ImageIcon("images/ProgramIcon.png");
	private static final ImageIcon imageWorkbook = new ImageIcon("images/Workbook.png");
	private static final ImageIcon imageReview = new ImageIcon("images/Review.png");
	private static final ImageIcon imageHistory = new ImageIcon("images/History.png");
	private static final ImageIcon imageSetting = new ImageIcon("images/Setting.png");
	private static final ImageIcon imageSort = new ImageIcon("images/Sort.png");
	private static final ImageIcon imageAddWorkbook = new ImageIcon("images/AddWorkbook.png");
	private static final ImageIcon imageRevert = new ImageIcon("images/Revert.png");
	private static final ImageIcon imageAddQuestion = new ImageIcon("images/AddQuestion.png");
	
	private static final String FONT_NAME = "맑은 고딕";
	private static final Color COLOR = Color.LIGHT_GRAY;
	private static final Color COLOR_BORDER = Color.GRAY;
	private static final Color COLOR_FONT = Color.GRAY;
	private static final String SEPARATOR = File.separator;
	private static final String FOLDER_PATH = System.getProperty("user.home") +
			File.separator + "Documents" + File.separator + "MyWorkbook";

	private Frame frame = this;
	private Container c;
	
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlNorthEast = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlCenterMain = new JPanel();
	private JLabel lblMenuName = new JLabel(" 문제집");
	
	private ImageButton btnWorkbook = new ImageButton(imageWorkbook, 80);
	private ImageButton btnReview = new ImageButton(imageReview, 80);
	private ImageButton btnHistory = new ImageButton(imageHistory, 80);
	private ImageButton btnSetting = new ImageButton(imageSetting, 80);
	private ImageButton btnSort = new ImageButton(imageSort, 50);
	private ImageButton btnAddWorkbook = new ImageButton(imageAddWorkbook, 50);
	private ImageButton btnRevert = new ImageButton(imageRevert, 50);
	private QuestionButton btnAddQuestion = new QuestionButton();
	
	private JTextField tfSearch = new JTextField(15);
	private JPanel pnlScroll = new JPanel();
	private JScrollPane sPnl = new JScrollPane(pnlScroll, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private String strMenu = "";
	private ArrayList<File> listWbFile;
	private HashMap<WorkbookButton, File> hashMapWbFile = new HashMap<>();
	private ArrayList<WorkbookButton> listWbBtn = new ArrayList<>();
	private String filePathBuffer = "";
	
	public Frame() {
		setTitle("나만의 문제집");
		setSize(500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize( new Dimension(500, 700) );
		setIconImage( imageProgramIcon.getImage() );
		
		//프레임을 화면 정중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - getWidth() / 2;
		int y = res.height / 2 - getHeight() / 2;
		setLocation(x, y);
		
		//ImageButton에 ActionListener 추가
		ImageButton[] aryImgBtn = { btnWorkbook, btnReview, btnHistory, btnSetting, btnSort,
				btnAddWorkbook, btnRevert };
		for (ImageButton imgBtn : aryImgBtn) {
			imgBtn.setPreferredSize( imgBtn.getSize() );
			imgBtn.addActionListener(this);
			imgBtn.addMouseListener(this);
		}
		
		//객체 초기화
		tfSearch.addKeyListener(this);
		sPnl.getVerticalScrollBar().setUnitIncrement(20);
		
		//파일 경로 초기화
		String separator = File.separator;
    	
    	File f = new File(FOLDER_PATH);
    	if( !f.exists() ) {
    		f.mkdirs();
    	}
    	
    	String[] aryFolder = { "Workbooks", "ReviewNotes" };
    	for (String k : aryFolder) {
    		String path = FOLDER_PATH + separator + k;
    		File fTemp = new File(path);
    		if ( !fTemp.exists() ) {
    			fTemp.mkdirs();
    		}
    	}
		
		//Gui 그리기
    	c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add(pnlNorth, BorderLayout.NORTH);
		
		pnlNorth.setBackground(COLOR);
		pnlNorth.setBorder( new MatteBorder( 0, 0, 2, 0, COLOR_BORDER ) );
		pnlNorth.setPreferredSize( new Dimension(0, 70) );
		pnlNorth.setLayout( new BorderLayout() );
		
		pnlNorth.add(lblMenuName, BorderLayout.CENTER);
		lblMenuName.setFont( new Font(FONT_NAME, Font.BOLD, 30) );
		
		pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
		pnlNorthEast.setBackground(null);
		pnlNorthEast.setLayout( new FlowLayout(FlowLayout.RIGHT, 10, 10) );
		
		c.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setBackground(COLOR);
		pnlSouth.setBorder( new MatteBorder(2,0,0,0,COLOR_BORDER) );
		pnlSouth.setPreferredSize( new Dimension(0, 130) );
		pnlSouth.setLayout( new FlowLayout(FlowLayout.CENTER, 25, 10) );
		
		ImageButton[] aryImageButton = { btnWorkbook, btnReview, btnHistory, btnSetting };
		String[] aryButtonName = { "문제집", "오답 노트", "기록", "설정" };
		for (int i=0; i<aryImageButton.length; i++) {
			JPanel pnlTemp = new JPanel();
			pnlSouth.add(pnlTemp);
			pnlTemp.setPreferredSize( new Dimension(80, 110) );
			pnlTemp.setLayout( new BorderLayout() );
			pnlTemp.setBackground(null);
			pnlTemp.add(aryImageButton[i], BorderLayout.CENTER);
			
			JLabel lblTemp = new JLabel(aryButtonName[i], SwingConstants.CENTER);
			pnlTemp.add(lblTemp, BorderLayout.SOUTH);
			lblTemp.setPreferredSize( new Dimension(80, 30) );
			lblTemp.setFont( new Font(Frame.getFontName(), Font.BOLD, 16) );
		}
		
		c.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new BorderLayout() );
		pnlCenter.add(pnlCenterMain, BorderLayout.CENTER);
		
		pnlCenterMain.setLayout( new GridBagLayout() );
		setMenu("Workbook");
		setVisible(true);
	} //생성자
	
	
	class ImageButton extends JButton {
		private static final long serialVersionUID = 1L;
		private ImageIcon image;
		private int size = 0;
		
		public ImageButton(ImageIcon image, int size) {
			this.image = image;
			this.size = size;
			
			setSize( new Dimension(size, size) );
			setBackground(null);
			setBorder(null);
			setContentAreaFilled(false);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image.getImage(), 0, 0, size, size, this);
		}
	} //ImageButton 클래스
	
	
	class WorkbookButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		private String filePath;
		private Workbook wb = null;
		
		private JPanel pnlCenter = new JPanel();
		private JLabel lblIcon;
		private JLabel lblName = new JLabel("", SwingConstants.CENTER);
		private JLabel lblNumQuestion = new JLabel("", SwingConstants.CENTER);
		
		public WorkbookButton(String filePath) {
			this.filePath = filePath;
			this.wb = FileIO.loadFile(filePath);
			
			setBackground(null);
			setBorder(null);
			setContentAreaFilled(false);
			setPreferredSize( new Dimension(0, 100) );
			setMaximumSize( new Dimension(10000, 100) );
			setVisible(true);
			addActionListener(this);
			addMouseListener( new MyMouseAdapter() );
			
			JPanel pnlBase = new JPanel();
			add(pnlBase, BorderLayout.CENTER);
			pnlBase.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );
			pnlBase.setLayout( new BorderLayout() );
			
			Image originImg = imageWorkbook.getImage();
			Image coloredImge = ColorizeFilter.colorizeImg( originImg, wb.getColor() );
			Image iconWb = coloredImge.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
			
			lblIcon = new JLabel( new ImageIcon(iconWb) );
			pnlBase.add(lblIcon, BorderLayout.WEST);
			
			pnlBase.add(pnlCenter, BorderLayout.CENTER);
			pnlCenter.setLayout( new GridLayout(2, 1, 0, 0) );
			
			String name = wb.getName();
			lblName.setText(name);
			lblName.setFont( new Font(FONT_NAME, Font.BOLD, 30) );
			pnlCenter.add(lblName);
			
			String numQuestion = "문제 개수 : " + Integer.toString( wb.getQuestion().size() ) + "개";
			lblNumQuestion.setText(numQuestion);
			lblNumQuestion.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
			pnlCenter.add(lblNumQuestion);
		} //생성자
		
		private class MyMouseAdapter extends MouseAdapter {
			@Override
			public void mouseEntered(MouseEvent e) {
				getContentPane().setCursor( new Cursor(Cursor.HAND_CURSOR) );
				JButton btn = (JButton)e.getComponent();
				btn.setBorder( new MatteBorder(1, 1, 1, 1, COLOR_BORDER) );
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
			new WbOptionDlg(frame, filePath);
		}
		
		public Workbook getWb() {
			return wb;
		}
	} //WorkbookButton 클래스
	
	
	class QuestionButton extends JButton implements ActionListener {
		private static final long serialVersionUID = 1L;
		
		private Workbook workbook = null;
		private int index = 0;
		
		//문제 추가 버튼
		public QuestionButton() {
			init();
			
			JPanel pnlBase = new JPanel();
			pnlBase.setLayout( new GridLayout() );
			add(pnlBase);
			
			Image resizedImage = imageAddQuestion.getImage().getScaledInstance(70, 70,
					Image.SCALE_SMOOTH);
			JLabel lblAddQuestion = new JLabel( new ImageIcon(resizedImage) );
			pnlBase.add(lblAddQuestion);
		} //생성자
		
		//문제 버튼
		public QuestionButton(Workbook workbook, int index) {
			init();
			this.workbook = workbook;
			this.index = index;
			
			JPanel pnlBase = new JPanel();
			pnlBase.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) );
			pnlBase.setLayout( new BorderLayout(10, 0) );
			add(pnlBase);
			
			JLabel lblIndex = new JLabel(Integer.toString(index + 1) );
			lblIndex.setFont( new Font(FONT_NAME, Font.BOLD, 40) );
			lblIndex.setPreferredSize( new Dimension(80, 80) );
			pnlBase.add(lblIndex, BorderLayout.WEST);
			
			JPanel pnlCenter = new JPanel();
			pnlCenter.setLayout( new GridLayout(2, 1, 0, 0) );
			pnlBase.add(pnlCenter, BorderLayout.CENTER);
			
			Question q = workbook.getQuestion().get(index);
			JLabel lblTitle = new JLabel( q.getTitle() );
			lblTitle.setFont( new Font(FONT_NAME, Font.PLAIN, 25) );
			JLabel lblAnswer = new JLabel( q.getAnswer() );
			lblAnswer.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
			lblAnswer.setForeground(Color.GRAY);
			pnlCenter.add(lblTitle);
			pnlCenter.add(lblAnswer);
		} //생성자
		
		//초기화
		public void init() {
			setBorder(null);
			setBackground(null);
			setContentAreaFilled(false);
			setPreferredSize( new Dimension(0, 100) );
			setMaximumSize( new Dimension(10000, 100) );
			setVisible(true);
			addActionListener(this);
			addMouseListener(frame);
		}

		@Override
		public void actionPerformed(ActionEvent e) { //문제 버튼 클릭
			if (workbook == null) { //문제 추가
				QuestionDlg.addQuestion(frame, filePathBuffer);
			}
			else { //문제 수정
				QuestionDlg.editQuestion(frame, filePathBuffer, index);
			}
		}
	} //QuestionButton 클래스
	
	
	//getter
	public static String getFontName() {
		return FONT_NAME;
	}
	
	public static Color getColor() {
		return COLOR;
	}
	
	public static Color getColorBorder() {
		return COLOR_BORDER;
	}
	
	public static Color getColorFont() {
		return COLOR_FONT;
	}
	
	public static String getFolderPath() {
		return FOLDER_PATH;
	}

	
	/**
	 * 지정된 경로의 폴더 내에서 원하는 확장자를 가진 파일만 File[]로 반환한다.
	 * @param dir - 가져올 파일이 저장된 상위 폴더의 File 객체
	 * @param extension - 가져올 파일의 확장자
	 * @return File[] - dir 내부에서 일치하는 확장자를 가진 File 객체의 배열
	*/
	public File[] getFiles(File dir, String extension) {
		File[] files = dir.listFiles( new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(extension);
			}
		} );
		return files;
	}
	
	
	/**
	 * [문제집] Documents/MyWorkbook/Workbooks 폴더에 저장된 모든 문제집 파일을 다시 불러와 ArrayList에 삽입한다.
	*/
	public void reloadWb(String filter) {
		//초기화
		hashMapWbFile.clear();
		listWbBtn.clear();
		
		String filePath = FOLDER_PATH + SEPARATOR + "Workbooks";
		File f = new File(filePath);
		File[] aryFile = getFiles(f, "workbook");
		listWbFile = new ArrayList<File>( Arrays.asList(aryFile) );
		
		//딕셔너리에 문제집 버튼 삽입
		for (File file : listWbFile) {
			Workbook wb = FileIO.loadFile( file.getPath() );
			
			//검색 필터 적용
			if (wb.getName().toLowerCase().startsWith( filter.toLowerCase() ) || filter == "") {
				WorkbookButton wbBtn = new WorkbookButton( file.getPath() );
				hashMapWbFile.put(wbBtn, file);
				listWbBtn.add(wbBtn); //리스트에 문제집 버튼 삽입
			}
		}
		
		sort(); //정렬 및 문제집 버튼 부착
	}
	
	public void reloadWb() {
		tfSearch.setText("");
		reloadWb( tfSearch.getText() );
	}
	
	
	/**
	 * [문제집] 메뉴에서 표시되는 문제집 목록을 정렬한다.
	 * @param type - 정렬 종류 [ 0 : 이름순 (오름차순), 1 : 이름순 (내림차순), 2 : 문제 많은순,
	 * 3 : 문제 적은순, 4 : 정답률 높은순, 5 : 정답률 낮은순 ]
	*/
	@SuppressWarnings("unchecked")
	public void sort(int type) {
		pnlCenterMain.removeAll();
		pnlScroll.removeAll();
		
		//문제집 버튼 ArrayList 정렬
		switch (type) {
		//TODO 문제집 정렬 기능 추가
			case 0: case 1: //이름순
				//오름차순 정렬
				Collections.sort(listWbBtn, new Comparator<WorkbookButton>() {
		            @Override
		            public int compare(WorkbookButton btn1, WorkbookButton btn2) {
		                return btn1.getWb().getName().compareTo( btn2.getWb().getName() );
		            }
		        });
				
				//내림 차순 정렬
				if (type == 1) {
					Collections.reverse(listWbBtn);
				}
				break;
			case 2: case 3: //문제 개수
				//적은순 정렬
				Collections.sort(listWbBtn, new Comparator<WorkbookButton>() {
		            @Override
		            public int compare(WorkbookButton btn1, WorkbookButton btn2) {
		                return Integer.toString( btn1.getWb().getQuestion().size() )
		                		.compareTo( Integer.toString( btn2.getWb().getQuestion().size() ) );
		            }
		        });
				
				//많은순 정렬
				if (type == 2) {
					Collections.reverse(listWbBtn);
				}
				break;
		} //switch()
		
		//문제집 버튼을 화면에 부착
		pnlCenterMain.setLayout( new GridLayout(1, 1) );
		pnlCenterMain.add(sPnl);
		
		pnlScroll.setLayout( new BoxLayout(pnlScroll, BoxLayout.Y_AXIS) );
		for (int i=0; i<listWbBtn.size(); i++) {
			pnlScroll.add( listWbBtn.get(i) );
		}
		
		//스크롤 패널 새로고침
		sPnl.repaint();
		sPnl.validate();
	} //sort()
	
	public void sort() {
		this.sort(0);
	} //sort()
	
	
	/**
	 * Frame 객체에서 표시할 메뉴를 설정한다.
	 * @param menu - 화면에 표시할 메뉴의 이름; [ Workbook : 문제집, Review : 오답노트, History : 기록,
	 * Setting : 설정, Question : [문제집] - 문제, AddQuestion : [문제집] - 문제 추가 ]
	 * @param workbook - 문제집 구조체
	 * @param question - 문제 구조체
	*/
	public void setMenu(String menu, String filePath) {
		c.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		pnlNorthEast.removeAll();
		pnlCenter.removeAll();
		
		Workbook workbook = null;
		if ( filePath != null && !filePath.isEmpty() ) {
			workbook = FileIO.loadFile(filePath);
		}
		
		switch (menu) {
			case "Workbook": default: //문제집 메뉴
				lblMenuName.setText(" 문제집");
				pnlNorthEast.add(btnSort);
				pnlNorthEast.add(btnAddWorkbook);
				
				JPanel pnlWorkbookSearch = new JPanel();
				pnlWorkbookSearch.setBackground( Frame.getColor() );
				pnlWorkbookSearch.setBorder( new MatteBorder( 0, 0, 2, 0, Frame.getColorBorder() ) );
				pnlWorkbookSearch.setPreferredSize( new Dimension(0, 30) );
				pnlWorkbookSearch.setLayout( new FlowLayout(FlowLayout.RIGHT, 10, 2) );
				pnlCenter.add(pnlWorkbookSearch, BorderLayout.NORTH);
				
				JLabel lblSearch = new JLabel("검색", SwingConstants.CENTER);
				lblSearch.setFont( new Font(Frame.getFontName(), Font.BOLD, 18) );
				pnlWorkbookSearch.add(lblSearch);
				pnlWorkbookSearch.add(tfSearch);
				
				pnlCenter.add(pnlCenterMain, BorderLayout.CENTER);
				reloadWb(); //문제집 파일 로드 및 버튼 그리기
				break;
			case "Review": //오답 노트 메뉴
				lblMenuName.setText(" 오답 노트");
				break;
			case "History": //기록 메뉴
				lblMenuName.setText(" 기록");
				break;
			case "Setting": //설정 메뉴
				lblMenuName.setText(" 설정");
				break;
			case "Question": //[문제집] - 문제 메뉴
				lblMenuName.setText( " " + workbook.getName() );
				pnlNorthEast.add(btnRevert);
				
				pnlCenter.add(sPnl, BorderLayout.CENTER);
				
				pnlScroll.removeAll();
				pnlScroll.setLayout( new BoxLayout(pnlScroll, BoxLayout.Y_AXIS) );
				for (int i=0; i<workbook.getQuestion().size(); i++) {
					pnlScroll.add( new QuestionButton(workbook, i) );
				}
				pnlScroll.add(btnAddQuestion);
				break;
			case "Solve": //문제 풀이
				break;
		} //switch()
		
		c.repaint();
		c.validate();
		this.filePathBuffer = filePath;
		strMenu = menu;
		System.out.println(menu);
	} //setMenu()
	
	public void setMenu(String menu) {
		setMenu(menu, null);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//상단 메뉴
		switch (strMenu) {
			case "Workbook": //문제집 메뉴
				if ( source.equals(btnSort) ) { //정렬
					new WbSortDlg(this);
				}
				if ( source.equals(btnAddWorkbook) ) {
					new WbAddDlg(this);
				}
				break;
			case "Question": //문제 메뉴
				if( source.equals(btnRevert) ) { //되돌리기
					setMenu("Workbook");
				}
				break;
			case "AddQuestion": //문제 추가
				if ( source.equals(btnRevert) ) { //되돌릭
					setMenu("Question", filePathBuffer);
				}
				break;
		}
		
		//하단 메뉴
		if ( source.equals(btnWorkbook) ) {
			setMenu("Workbook");
		}
		if ( source.equals(btnReview) ) {
			setMenu("Review");
		}
		if ( source.equals(btnHistory) ) {
			setMenu("History");
		}
		if ( source.equals(btnSetting) ) {
			setMenu("Setting");
		}
	} //actionPerformed()

	
	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) {
		c.setCursor( new Cursor(Cursor.HAND_CURSOR) );
		JButton btn = (JButton)e.getComponent();
		btn.setBorder( new MatteBorder(1, 1, 1, 1, COLOR_BORDER) );
	}

	@Override
	public void mouseExited(MouseEvent e) {
		c.setCursor( new Cursor(Cursor.DEFAULT_CURSOR) );
		JButton btn = (JButton)e.getComponent();
		btn.setBorder(null);
	}

	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_ESCAPE: case KeyEvent.VK_CONTROL: case KeyEvent.VK_ALT:
			case KeyEvent.VK_WINDOWS: case KeyEvent.VK_TAB: case KeyEvent.VK_CAPS_LOCK:
			case KeyEvent.VK_NUM_LOCK: case KeyEvent.VK_SCROLL_LOCK: case KeyEvent.VK_INSERT:
			case KeyEvent.VK_HOME: case KeyEvent.VK_END: case KeyEvent.VK_PAGE_UP:
			case KeyEvent.VK_PAGE_DOWN: case KeyEvent.VK_CONTEXT_MENU:
				break;
			default:
				reloadWb( tfSearch.getText() );
				break;
		}
	}
} //Frame 클래스
