import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;

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
	 * @param override - 덮어쓰기 여부
	 * @return 파일 저장 성공시 true 반환, 실패시 false 반환
	*/
    public static boolean saveFile(String filePath, Workbook wb, boolean override) {
        try {
        	if (override) { //덮어쓰기
        		String path = filePath.replace(" ", "_");
        		FileOutputStream fileOutput = new FileOutputStream(path);
				ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
	        	objOutput.writeObject(wb);
	            
	            fileOutput.close();
	            objOutput.close();
	            return true;
        	}
        	else { //이어쓰기
        		for (int i=0; i<=10; i++) {
        			String path = filePath.replace(" ", "_");
        			
        			if (i != 0) {
        				String[] aryPath = filePath.split("\\.");
        				path = String.format( "%s(%s).%s", aryPath[0], i, aryPath[1] );
        			}
        			
        			File file = new File(path);
        			if( !file.exists() ) { //중복되는 파일이 없다면 저장
        				FileOutputStream fileOutput = new FileOutputStream(path);
        				ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
        	        	objOutput.writeObject(wb);
        	            
        	            fileOutput.close();
        	            objOutput.close();
        	            return true;
        			}
    	    	}
        	}
    		
    		//파일을 생성하지 못했다면 오류 반환
    		System.out.println("파일을 생성하지 못했습니다.");
    		return false;
        }
        catch (Exception e) {
            System.out.println("파일을 생성하지 못했습니다.");
            return false;
        }
    }
    
    /**
	 * 문제집 파일을 지정된 경로에 덮어쓰지 않고 저장한다.
	 * @param filePath - 이름과 확장자가 포함된 저장할 파일의 경로
	 * @param wb - Workbook 객체
	 * @return 파일 저장 성공시 true 반환, 실패시 false 반환
	*/
    public static boolean saveFile(String filePath, Workbook wb) {
    	return saveFile(filePath, wb, false);
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
            //TODO 파일 불러오기 오류 메시지 팝업
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
}
