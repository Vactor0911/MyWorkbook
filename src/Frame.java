import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.border.*;
import java.io.*;

public class Frame extends JFrame implements ActionListener, MouseListener, KeyListener {
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private final ImageIcon imageTest = new ImageIcon("images/GridboxPrototypeTexture.jpg");
	private final ImageIcon imageWorkbook = new ImageIcon("images/Workbook.png");
	private final ImageIcon imageReview = new ImageIcon("images/Review.png");
	private final ImageIcon imageHistory = new ImageIcon("images/History.png");
	private final ImageIcon imageSetting = new ImageIcon("images/Setting.png");
	private final ImageIcon imageSort = new ImageIcon("images/Sort.png");
	private final ImageIcon imageAddWorkbook = new ImageIcon("images/AddWorkbook.png");
	private final static String FONT = "맑은 고딕";
	private final static Color COLOR = Color.LIGHT_GRAY;
	private final static Color COLOR_BORDER = Color.GRAY;
	private final static Color COLOR_FONT = Color.GRAY;
	private final static String SEPARATOR = File.separator;
	private final static String FOLDER_PATH = System.getProperty("user.home") +
			File.separator + "Documents" + File.separator + "MyWorkbook";
	
	private Container c;
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlNorthEast = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlCenterMain = new JPanel();
	private JLabel lblMenuName = new JLabel(" 문제집");
	private ImageButton btnSort = new ImageButton(imageSort, 50);
	private ImageButton btnAddWorkbook = new ImageButton(imageAddWorkbook, 50);
	private ImageButton btnWorkbook = new ImageButton(imageWorkbook, 80);
	private ImageButton btnReview = new ImageButton(imageReview, 80);
	private ImageButton btnHistory = new ImageButton(imageHistory, 80);
	private ImageButton btnSetting = new ImageButton(imageSetting, 80);
	private JTextField tfSearch = new JTextField(15);
	private JPanel pnlScroll = new JPanel();
	private JScrollPane sPnl = new JScrollPane(pnlScroll, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	private String strMenu = "";
	private ArrayList<File> listWbFile;
	private HashMap<WorkbookButton, File> hashMapWbFile = new HashMap<>();
	private ArrayList<WorkbookButton> listWbBtn = new ArrayList<>();
	
	public Frame() {
		setTitle("나만의 문제집");
		setSize(500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize( new Dimension(500, 700) );
		
		//프레임을 화면 정중앙에 위치
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		int x = res.width / 2 - getWidth() / 2;
		int y = res.height / 2 - getHeight() / 2;
		setLocation(x, y);
		
		//ImageButton에 ActionListener 추가
		ImageButton[] aryImageButton = {
				btnSort, btnAddWorkbook, btnWorkbook, btnReview, btnHistory, btnSetting
				};
		for (int i=0; i<aryImageButton.length; i++) {
			ImageButton imgBtn = aryImageButton[i];
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
		
		pnlNorth.add(lblMenuName, BorderLayout.WEST);
		lblMenuName.setFont( new Font(FONT, Font.BOLD, 30) );
		
		pnlNorth.add(pnlNorthEast, BorderLayout.EAST);
		pnlNorthEast.setBackground(null);
		pnlNorthEast.setLayout( new FlowLayout(FlowLayout.RIGHT, 10, 10) );
		
		c.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setBackground(COLOR);
		pnlSouth.setBorder( new MatteBorder(2,0,0,0,COLOR_BORDER) );
		pnlSouth.setPreferredSize( new Dimension(0, 130) );
		pnlSouth.setLayout( new FlowLayout(FlowLayout.CENTER, 25, 10) );
		
		String[] aryButtonName = { "문제집", "오답 노트", "기록", "설정" };
		for (int i=2; i<aryImageButton.length; i++) {
			JPanel pnlTemp = new JPanel();
			pnlSouth.add(pnlTemp);
			pnlTemp.setPreferredSize( new Dimension(80, 110) );
			pnlTemp.setLayout( new BorderLayout() );
			pnlTemp.setBackground(null);
			pnlTemp.add(aryImageButton[i], BorderLayout.CENTER);
			
			JLabel lblTemp = new JLabel(aryButtonName[i-2], SwingConstants.CENTER);
			pnlTemp.add(lblTemp, BorderLayout.SOUTH);
			lblTemp.setPreferredSize( new Dimension(80, 30) );
			lblTemp.setFont( new Font(FONT, Font.BOLD, 16) );
		}
		
		c.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout( new BorderLayout() );
		pnlCenter.add(pnlCenterMain, BorderLayout.CENTER);
		
		pnlCenterMain.setLayout( new GridBagLayout() );
		setMenu("Workbook");
		setVisible(true);
	} //생성자
	
	
	//getter
	public static String getFontName() {
		return FONT;
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
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File f, String name) {
				return name.endsWith(extension);
			}
		});
		return files;
	}
	
	/**
	 * [문제집] Documents/MyWorkbook/Workbooks 폴더에 저장된 모든 문제집 파일을 다시 불러와 ArrayList에 삽입한다.
	*/
	public void reloadWb() {
		//초기화
		hashMapWbFile.clear();
		listWbBtn.clear();
		
		String filePath = FOLDER_PATH + SEPARATOR + "Workbooks";
		File f = new File(filePath);
		File[] aryFile = getFiles(f, "workbook");
		listWbFile = new ArrayList<File>( Arrays.asList(aryFile) );
		
		//딕셔너리에 문제집 버튼 삽입
		for (File file : listWbFile) {
			FileIO fIO = new FileIO();
			Workbook wb = fIO.loadFile( file.getPath() );
			WorkbookButton wbBtn = new WorkbookButton(wb);
			hashMapWbFile.put(wbBtn, file);
			
			//리스트에 문제집 버튼 삽입
			listWbBtn.add(wbBtn);
		}
		
		sort(); //정렬 및 문제집 버튼 부착
	}
	
	/**
	 * [문제집] 메뉴에서 표시되는 문제집 목록을 정렬한다.
	 * @param type - 정렬 종류 [ 0 : 이름순 (오름차순), 1 : 이름순 (내림차순), 2 : 문제 많은순,
	 * 3 : 문제 적은순, 4 : 정답률 높은순, 5 : 정답률 낮은순 ]
	*/
	public void sort(int type) {
		pnlCenter.removeAll();
		pnlScroll.removeAll();
		
		//문제집 버튼 ArrayList 정렬
		switch (type) {
		//TODO 문제집 정렬 기능 추가
			case 0: //이름순 (오름차순)
				System.out.println("이름순 (오름차순)");
				break;
			case 1: //이름순 (내림차순)
				System.out.println("이름순 (내림차순)");
				break;
			case 2: //문제 많은순
				System.out.println("문제 많은순");
				break;
			case 3: //문제 적은순
				System.out.println("문제 적은순");
				break;
			case 4: //정답률 높은순
				System.out.println("정답률 높은순");
				break;
			default: //정답률 낮은순
				System.out.println("정답률 낮은순");
				break;
		} //switch()
		
		//문제집 버튼을 화면에 부착
		pnlCenter.setLayout( new GridLayout(1, 1) );
		pnlCenter.add(sPnl);
		
		pnlScroll.setLayout( new BoxLayout(pnlScroll, BoxLayout.Y_AXIS) );
		
		for (int i=0; i<listWbBtn.size(); i++) {
			pnlScroll.add( listWbBtn.get(i) );
		}
	} //sort()
	
	public void sort() {
		this.sort(0);
	} //sort()
	
	
	class WorkbookButton extends JButton implements MouseListener {
		private static final long serialVersionUID = 1L;
		private Workbook wb = null;
		private ImageIcon iconWb;
		private Font font = new Font(FONT, Font.BOLD, 40);
		
		public WorkbookButton(Workbook wb) {
			//TODO 문제집 버튼 추가
			this.wb = wb;
			
			Image originImg = imageWorkbook.getImage();
			Image coloredImge = ColorizeFilter.colorizeImg( originImg, wb.getColor() );
			iconWb = new ImageIcon(coloredImge);
			
			setBackground(null);
			setBorder(null);
			setContentAreaFilled(false);
			setPreferredSize( new Dimension(0, 100) );
			setMaximumSize( new Dimension(10000, 100) );
			setVisible(true);
			addMouseListener(this);
		} //생성자
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			int width = getWidth();
			int height = getHeight();
			String name = wb.getName();
			int fontSize = font.getSize();
			
			g.drawImage(iconWb.getImage(), 5, 5, height - 10, height - 10, this);
			
			g.setFont(font); //폰트 지정
			g.drawString( name, (int)(width * 0.5 - name.length() * fontSize * 0.25 ),
					(int)(height *0.5 + fontSize * 0.3) );
		}

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			int mouse = e.getButton();
			switch (mouse) {
				case MouseEvent.BUTTON1: //마우스 왼쪽 클릭
					
					break;
				case MouseEvent.BUTTON2: //마우스 오른쪽 클릭
					break;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

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
		
	} //WorkbookButton 클래스
	
	
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
	
	
	/**
	 * Frame 객체에서 표시할 메뉴를 설정한다.
	 * @param menu - 화면에 표시할 메뉴의 이름; [ Workbook : 문제집, Review : 오답노트, History : 기록,
	 * Setting : 설정 ]
	*/
	public void setMenu(String menu) {
		ArrayList<ImageButton> listImageButton = new ArrayList<ImageButton>();
		
		//이미 해당 메뉴일 경우 종료
		if (menu == strMenu) {
			return;
		}
		
		pnlNorthEast.removeAll();
		pnlCenter.removeAll();
		
		switch (menu) {
			case "Workbook":
				lblMenuName.setText(" 문제집");
				listImageButton.add(btnSort);
				listImageButton.add(btnAddWorkbook);
				
				JPanel pnlWorkbookSearch = new JPanel();
				pnlWorkbookSearch.setBackground(COLOR);
				pnlWorkbookSearch.setBorder( new MatteBorder(0,0,2,0,COLOR_BORDER) );
				pnlWorkbookSearch.setPreferredSize( new Dimension(0, 30) );
				pnlWorkbookSearch.setLayout( new FlowLayout(FlowLayout.RIGHT, 10, 2) );
				pnlCenter.add(pnlWorkbookSearch, BorderLayout.NORTH);
				
				JLabel lblSearch = new JLabel("검색", SwingConstants.CENTER);
				lblSearch.setFont( new Font(getFontName(), Font.BOLD, 18) );
				pnlWorkbookSearch.add(lblSearch);
				pnlWorkbookSearch.add(tfSearch);
				
				reloadWb();
				break;
			case "Review":
				lblMenuName.setText(" 오답 노트");
				break;
			case "History":
				lblMenuName.setText(" 기록");
				break;
			default:
				lblMenuName.setText(" 설정");
				break;
		} //switch()
		
		for (ImageButton k : listImageButton) {
			pnlNorthEast.add(k);
		}
		
		c.repaint();
		strMenu = menu;
	} //setMenu()
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//상단 메뉴
		if (strMenu == "Workbook") {
			if ( source.equals(btnSort) ) { //정렬
				new WbSortDlg(this);
			}
			if ( source.equals(btnAddWorkbook) ) {
				new WbAddDlg(this);
			}
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
	public void keyPressed(KeyEvent e) {
	}

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
				System.out.println( tfSearch.getText() );
				//TODO 문제집 검색 기능 추가
				break;
		}
	}
} //Frame 클래스
