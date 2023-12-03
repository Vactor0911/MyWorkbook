import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

class Workbook implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private Color color = null;
	private ArrayList<Question> listQuestion = new ArrayList<>();
	
	public Workbook(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setQuestion( ArrayList<Question> listQuestion ) {
		this.listQuestion = listQuestion;
	}
	
	public ArrayList<Question> getQuestion() {
		return listQuestion;
	}
	
	@Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name);
        buffer.append("\n");
        buffer.append(color);
        buffer.append("\n");
        buffer.append(listQuestion);
        buffer.append("\n");
        return buffer.toString();
    }
} //Workbook 클래스


class Question implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int category;
	private String title;
	private String answer;
	private String[] aryOption;
	private String explain;
	private ImageIcon image;
	
	public Question(int category, String title, String answer, String[] aryOption, String explain,
			ImageIcon image) {
		this.category = category;
		this.title = title;
		this.answer = answer;
		this.aryOption = aryOption;
		this.explain = explain;
		this.image = image;
	}
	
	//setter
	public void setCategory(int category) {
		this.category = category;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public void setAryOption(String[] aryOption) {
		this.aryOption = aryOption;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
	//getter
	public int getCategory() {
		return category;
	}
	public String getTitle() {
		return title;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public String[] getAryOption() {
		return aryOption;
	}
	
	public String getExplain() {
		return explain;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(title);
		buffer.append("\n");
		buffer.append(answer);
		buffer.append("\n");
		buffer.append(aryOption);
		buffer.append("\n");
		buffer.append(explain);
		buffer.append("\n");
		buffer.append(image);
		buffer.append("\n");
		return buffer.toString();
	}
} //Question 클래스


class FileIO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 문제집 파일을 지정된 경로에 저장한다.
	 * @param filePath - 이름과 확장자가 포함된 저장할 파일의 경로
	 * @param wb - Workbook 객체
	 * @return 파일 저장 성공시 true 반환, 실패시 false 반환
	*/
    public static boolean saveFile(String filePath, Workbook wb) {
        try {
    		FileOutputStream fileOutput = new FileOutputStream(filePath);
			ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
        	objOutput.writeObject(wb);
            
            fileOutput.close();
            objOutput.close();
            return true;
        }
        catch (Exception e) {
            System.out.println("파일을 생성하지 못했습니다.");
            return false;
        }
    }
   

    /**
     * 지정된 경로에서 파일을 불러온다.
	 * @param filePath - 이름과 확장자가 포함된 저장할 파일의 경로
	 * @return 파일 불러오기 성공시 Workbook 객체 반환, 실패시 null 반환
	*/
    public static Workbook loadFile(String filePath) {
    	Workbook wb = null;
        try ( FileInputStream fileInPut = new FileInputStream(filePath);
             ObjectInputStream objInput = new ObjectInputStream(fileInPut) ) {
            wb = (Workbook)objInput.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return wb;
    }
} //DataObject 클래스


class ColorizeFilter extends RGBImageFilter implements Serializable {
	private static final long serialVersionUID = 1L;
    private int color;

    public ColorizeFilter(int color) {
        this.color = color;
        canFilterIndexColorModel = true;
    }
    
    public static Image colorizeImg(Image img, Color color) {
		//RGBImageFilter 필터를 생성
		RGBImageFilter filter = new ColorizeFilter( color.getRGB() );
		//FilteredImageSource를 통해 필터 적용
		ImageProducer producer = new FilteredImageSource(img.getSource(), filter);
		//필터를 적용한 이미지 생성
		Image coloredImg = Toolkit.getDefaultToolkit().createImage(producer);
		return coloredImg;
	}

    @Override
    public int filterRGB(int x, int y, int rgb) {
        int alpha = (rgb >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
} //ColorizeFilter 클래스


class HintTextField extends JTextField implements FocusListener {
	private static final long serialVersionUID = 1L;
	private String hint;
	private boolean flagWarn = false;
	
	public HintTextField(String hint) {
		super();
		this.hint = hint;
		addFocusListener(this);
		focusLost(null);
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		setForeground(Color.BLACK);
	}
	
	@Override
	public String getText() {
		String text = super.getText();
		if ( text.trim().equals(hint) ) {
			return "";
		}
		return text;
	}
	
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public String getHint() {
		return hint;
	}
	
	public void warn(boolean flagWarn) {
		this.flagWarn = flagWarn;
		
		if (flagWarn) { //경고 설정
			setBorder( BorderFactory.createLineBorder(Color.RED, 1, false) );
			setForeground(Color.RED);
		}
		else { //경고 해제
			setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			if ( isRequestFocusEnabled() ) {
				setForeground(Color.BLACK);
			}
			else {
				setForeground(Color.GRAY);
			}
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if ( getText().isEmpty() ) {
			setText("");
			if (!flagWarn) {
				setForeground(Color.BLACK);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if ( getText().isEmpty() ) {
			setText(" " + hint);
			if (!flagWarn) {
				setForeground(Color.GRAY);
			}
		}
		else if (flagWarn) {
			warn(false);
		}
	}
} //HintTextField 클래스


class ColorComboBox extends JComboBox<Color> {
	private static final long serialVersionUID = 1L;
	private static final Color PINK = new Color(244, 153, 192);
	private static final Color ORANGE = new Color(247, 148, 30);
	private static final Color YELLOW = new Color(255, 247, 154);
	private static final Color GREEN = new Color(171, 211, 116);
	private static final Color BLUE = new Color(109, 207, 246);
	
	private Color[] aryColor = { PINK, ORANGE, YELLOW, GREEN, BLUE };
	
	private HashMap<Color, Integer> hmColor = new HashMap<>();

	@SuppressWarnings("unchecked")
	public ColorComboBox() {
		super();
		DefaultComboBoxModel<Color> model = new DefaultComboBoxModel<Color>();
		for (int i=0; i<aryColor.length; i++) {
			hmColor.put(aryColor[i], i);
		}
			
		for (Color color : aryColor) {
			model.addElement(color);
		}
		setModel(model);
		setRenderer( new ColorRenderer() );
		setOpaque(true);
		setSelectedIndex(0);
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
			setOpaque(true);
			setPreferredSize( new Dimension(0, 30) );
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setBackground( (Color)value );
			setText(" ");
			setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			return this;
		}
	} //ColorRenderer 클래스
	
	public Color getColor() {
		return aryColor[ getSelectedIndex() ];
	}
	
	public int getColorIndex(Color color) {
		return hmColor.get(color);
	}
	
} //JColorComboBox 클래스


class CustomComboBox extends JComboBox<String> {
	private static final long serialVersionUID = 1L;
	private String[] aryItem;

	@SuppressWarnings("unchecked")
	public CustomComboBox(String[] aryItem) {
		super();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		this.aryItem = aryItem;
		
		for (String k : aryItem) {
			model.addElement(k);
		}
		setBackground( Frame.getColor() );
		setFont( new Font(Frame.getFontName(), Font.BOLD, 20) );
		setModel(model);
		setRenderer( new Renderer() );
		setOpaque(true);
		setSelectedIndex(0);
	} //생성자
	
	@Override
	public void setSelectedItem(Object object) {
		super.setSelectedItem(object);
	}
	
	@SuppressWarnings("rawtypes")
	class Renderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		public Renderer() {
			setOpaque(true);
			setPreferredSize( new Dimension(0, 30) );
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText( " " + (String)value );
			setFont( new Font(Frame.getFontName(), Font.PLAIN, 15) );
			setBorder( BorderFactory.createLineBorder(Color.BLACK, 1, false) );
			setBackground( Frame.getColor() );
			return this;
		}
	} //Renderer 클래스
	
	public String getText() {
		return aryItem[ getSelectedIndex() ];
	}
} //CustomComboBox 클래스
