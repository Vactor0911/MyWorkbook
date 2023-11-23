import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.*;

public class Frame extends JFrame implements ActionListener, MouseListener {
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
	private final static Color COLOR = Color.lightGray;
	private final static Color COLOR_BORDER = Color.gray;
	
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
	
	private WbSortDlg dlgWbSort = new WbSortDlg(this);
	private WbAddDlg dlgWbAdd = new WbAddDlg(this);
	
	private String strMenu = "";
	
	public Frame() {
		setTitle("나만의 문제집");
		setSize(500, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
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
		
		//Gui 그리기
		c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add(pnlNorth, BorderLayout.NORTH);
		
		pnlNorth.setBackground(COLOR);
		pnlNorth.setBorder( new MatteBorder(0,0,2,0,Color.gray) );
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
	}
	
	public static String getFontName() {
		return FONT;
	}
	
	public static Color getColor() {
		return COLOR;
	}
	
	public static Color getColorBorder() {
		return COLOR_BORDER;
	}
	
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
	}
	
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
		}
		
		for (ImageButton k : listImageButton) {
			pnlNorthEast.add(k);
		}
		
		c.repaint();
		strMenu = menu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//상단 메뉴
		if (strMenu == "Workbook") {
			if ( source.equals(btnSort) ) { //정렬
				dlgWbSort.setVisible(true);
			}
			if ( source.equals(btnAddWorkbook) ) {
				dlgWbAdd.setVisible(true);
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
	}

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
}