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
	
	private SolveQuestion solve = null;
	
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
			
			Image resizedImage = imageAddQuestion.getImage().getScaledInstance(50, 50,
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
			lblIndex.setFont( new Font(FONT_NAME, Font.BOLD, 30) );
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
			setPreferredSize( new Dimension(0, 70) );
			setMaximumSize( new Dimension(10000, 70) );
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
	
	
	class SolveQuestion implements ActionListener {
		private String filePath;
		private ArrayList<Question> listQuestion;
		private int questionNum;
		private boolean notice;
		private int index = -1;
		private int pnlHeight = 0;
		private boolean flagAnswer = true;
		
		private JPanel pnlQMain = new JPanel();
		private JPanel pnlQNorth = new JPanel();
		private JPanel pnlQCenter = new JPanel();
		private JScrollPane sPnlQ = new JScrollPane();
		
		private JLabel lblQNum = new JLabel("No. 1");
		private JSlider slQProgress = new JSlider();
		private JLabel lblQTitle = new JLabel();
		private JLabel lblQImage = new JLabel();
		private JButton btnQAnswer = new JButton();
		private JTextField tfQAnswer = new JTextField();
		private JButton btnQSubmit = new JButton("정답 제출");
		
		private ArrayList<JButton> listQOption = new ArrayList<>();
		private QExplainDlg dialogObj = null;
		
		public SolveQuestion(String filePath, boolean random, int questionNum, boolean notice) {
			this.filePath = filePath;
			listQuestion = FileIO.loadFile(filePath).getQuestion();
			this.questionNum = questionNum;
			this.notice = notice;
			
			//랜덤 선택시 출제 순서 랜덤화
			if (random) {
				Collections.shuffle(listQuestion);
			}
			
			//객체 초기화
			lblQTitle.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
			btnQAnswer.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
			btnQAnswer.addActionListener(this);
			btnQSubmit.addActionListener(this);
			
			//GUI 그리기
			//초기화
			pnlQMain.removeAll();
			pnlQNorth.removeAll();
			pnlQCenter.removeAll();
			
			pnlQMain.setBorder( BorderFactory.createEmptyBorder(10, 30, 10, 30) );
			pnlQMain.setLayout( new BoxLayout(pnlQMain, BoxLayout.Y_AXIS) );
			
			//스크롤 패널
			sPnlQ.setViewportView(pnlQMain);
			pnlQMain.setPreferredSize( new Dimension( pnlCenter.getWidth(), 10000) );
			sPnlQ.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			sPnlQ.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			sPnlQ.getVerticalScrollBar().setUnitIncrement(20);
			pnlCenter.add(sPnlQ, BorderLayout.CENTER);
			pnlQNorth.setPreferredSize( new Dimension(0, 60) );
			pnlQNorth.setMinimumSize( new Dimension(0, 60) );
			pnlQNorth.setMaximumSize( new Dimension(10000, 60) );
			
			//North
			pnlQNorth.setLayout( new BorderLayout(50, 0) );
			pnlQMain.add(pnlQNorth);
			pnlQNorth.add(lblQNum, BorderLayout.WEST);
			lblQNum.setText( "No. " + Integer.toString(index + 1) );
			lblQNum.setFont( new Font(FONT_NAME, Font.BOLD, 25) );
			
			slQProgress.setMinimum(1);
			slQProgress.setMaximum(questionNum);
			slQProgress.setPaintLabels(true);
			slQProgress.setPaintTicks(true);
			slQProgress.setMajorTickSpacing(questionNum - 1);
			slQProgress.setValue(1);
			slQProgress.setEnabled(false);
			pnlQNorth.add(slQProgress, BorderLayout.CENTER);
			
			//Center
			pnlQCenter.setLayout( new BoxLayout(pnlQCenter, BoxLayout.Y_AXIS) );
			pnlQMain.add(pnlQCenter);
			
			showNextQuestion(); //문제를 화면에 출력
		} //생성자
		
		public void showNextQuestion() {
			index++;
			flagAnswer = true;
			//스크롤 패널 초기화
			pnlQCenter.removeAll();
			Question question = listQuestion.get(index);
			sPnlQ.getVerticalScrollBar().setValue(0);
			tfQAnswer.setText("");
			
			//문제 번호 및 진행도
			lblQNum.setText( "No. " + Integer.toString(index + 1) );
			slQProgress.setValue(index + 1);
			
			//문제
			String strTitle = question.getTitle() + question.getTitle() + question.getTitle() + question.getTitle();
			lblQTitle.setText("<html><p>" + strTitle + "</p></html>");
			pnlQCenter.add(lblQTitle);
			addGap(15);
			
			validate();
			
			//사진
			if (question.getImage() != null) {
				ImageIcon image = question.getImage();
				double width = image.getIconWidth();
				double height = image.getIconHeight();
				double multiplier = height / width;
				int imgWidth = pnlQCenter.getWidth() - 50;
				Image resizedImage = question.getImage().getImage().getScaledInstance(
						imgWidth, (int)(imgWidth * multiplier),
						Image.SCALE_SMOOTH);
				lblQImage.setIcon( new ImageIcon(resizedImage) );
				pnlQCenter.add(lblQImage);
				addGap(15);
			}
			
			//답안
			if (question.getCategory() == 0) { //선택형 문제
				//리스트에 버튼 삽입
				ArrayList<JButton> listBtn = new ArrayList<>();
				btnQAnswer.setText("<html><p>" + question.getAnswer() + "</p></html>");
				listBtn.add(btnQAnswer);
				for ( String k :question.getAryOption() ) {
					if (k == null) {
						continue;
					}
					listBtn.add( new JButton("<html><p>" + k + "</p></html>") );
				}
				
				//리스트 셔플
				Collections.shuffle(listBtn);
				
				//패널에 버튼 삽입
				for (JButton btn : listBtn) {
					Dimension size = new Dimension(0, 20);
					btn.setPreferredSize(size);
					btn.setMinimumSize(size);
					btn.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
					btn.addActionListener(this);
					pnlQCenter.add(btn);
					addGap(10);
				}
			}
			else { //서술형 문제
				tfQAnswer.setPreferredSize( new Dimension(0, 50) );
				tfQAnswer.setMaximumSize( new Dimension(10000, 50) );
				tfQAnswer.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
				pnlQCenter.add(tfQAnswer);
				addGap(15);
				
				Dimension size = new Dimension(0, 50);
				btnQSubmit.setPreferredSize(size);
				btnQSubmit.setMinimumSize(size);
				btnQSubmit.setFont( new Font(FONT_NAME, Font.PLAIN, 20) );
				pnlQCenter.add(btnQSubmit);
			}
			System.out.println("현재 문제 >> " + index);
			
			repaint();
			validate();
			
			pnlHeight = pnlQNorth.getHeight() + pnlQCenter.getHeight();
			pnlHeight += lblQTitle.getHeight() * Math.ceil(lblQTitle.getText().length() / 20);
			if (question.getAryOption() != null) {
				for ( String k :question.getAryOption() ) {
					if (k != null) {
						pnlHeight += Math.ceil(k.length() / 20);
					}
				}
			}
			pnlQMain.setPreferredSize( new Dimension(pnlCenter.getWidth(), pnlHeight) );
			
			System.out.println("다음 문제 >> " + index);
		} //showNextQuestion()
		
		//공백 추가
		private void addGap(int gap) {
			pnlQCenter.add( Box.createRigidArea( new Dimension(0, gap) ) );
		}
		
		//문제 풀이 종료
		public void stop() {
			//대화상자 종료
			dialogObj.dispatchEvent( new WindowEvent(dialogObj, WindowEvent.WINDOW_CLOSING) );
		}
		
		private void correct(boolean correct) {
			if (flagAnswer) {
				flagAnswer = false;
				
				if (notice) { //정답 여부 및 해설 표시
					if (correct) { //정답
						
					}
					else { //오답
						
					}
					Question question = listQuestion.get(index);
					dialogObj = new QExplainDlg(frame, question, correct);
				}
				else { //정답 여부 및 해설 표시하지 않음
					if (correct) { //정답
						
					}
					else { //오답
						
					}
					showNextQuestion();
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ( e.getSource().equals(btnQAnswer) ) { //정답
				correct(true);
			}
			else if ( e.getSource().equals(btnQSubmit) ) { //정답 제출
				//공백이면 종료
				if ( tfQAnswer.getText().isEmpty() ) {
					return;
				}
				
				//제출한 답안과 저장된 정답 가공
				Question question = listQuestion.get(index);
				String[] aryAnswer = { tfQAnswer.getText(), question.getAnswer() };
				for (int i=0; i<aryAnswer.length; i++) {
					aryAnswer[i] = aryAnswer[i].replace(" ", ""); //공백 제거
					aryAnswer[i] = aryAnswer[i].toLowerCase(); //영문 소문자로 변경
					//탈출문자 제거
					String[] arySpecialChar = { "\n", "\t", "\'", "\"" };
					for (String k : arySpecialChar) {
						aryAnswer[i].replace(k, "");
					}
				}
				
				//정답 확인
				correct( aryAnswer[0].equals( aryAnswer[1] ) );
			}
			else { //선택형 오답
				correct(false);
			}
		}
	} //SolveQuestion 클래스
	
	
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
	public void sort(int type) {
		pnlCenterMain.removeAll();
		pnlScroll.removeAll();
		
		//문제집 버튼 ArrayList 정렬
		switch (type) {
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
		//풀이하고 있던 문제가 있던 경우
		if (strMenu == "SolveQuestion") {
			if ( MessageBox.show(this, "문제 풀이를 종료하시겠습니까?", MessageBox.btnYES_NO,
					MessageBox.iconQUESTION) == MessageBox.idNO ) {
				return; //풀이 종료를 원치 않는다면 메뉴 변경 중지
			}
			System.out.println("문제 풀이 종료");
			solve.stop();
			solve = null;
		}
		
		//객체 초기화
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
			case "SolveQuestion": //문제 풀이
				lblMenuName.setText("문제 풀이");
				pnlNorthEast.add(btnRevert);
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
	
	
	/**
	 * 문제 풀이 메뉴를 시작합니다.
	 * @param filePath - 선택한 문제집이 저장된 경로
	 * @param random - 문제 출제 순서의 랜덤 여부
	 * @param questionNum - 출제할 문제의 개수
	 * @param notice - 정답 알림 표시 여부
	 */
	public void startSolve(String filePath, boolean random, int questionNum, boolean notice) {
		setMenu("SolveQuestion", filePath);
		solve = new SolveQuestion(filePath, random, questionNum, notice);
	}
	
	
	public void showNextQuestion() {
		try {
			solve.showNextQuestion();
		}
		catch (Exception e) {
			//TODO 문제풀이 종료 및 결과
			solve = null;
		}
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
				if ( source.equals(btnRevert) ) { //되돌리기
					setMenu("Question", filePathBuffer);
				}
				break;
			case "SolveQuestion":
				if ( source.equals(btnRevert) ) { //되돌리기
					setMenu("Workbook");
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
