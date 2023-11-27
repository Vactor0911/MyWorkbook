import java.awt.Color;
import java.io.*;
import java.util.Dictionary;
import java.util.Hashtable;

class Workbook implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = "";
	private Color color = null;
	private Dictionary<String, Object> dictQ = new Hashtable<String, Object>();
	
	public Workbook(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Dictionary<String, Object> getDictionary() {
		return dictQ;
	}
	
	@Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(name);
        buffer.append("\n");
        buffer.append(color);
        buffer.append("\n");
        buffer.append(dictQ);
        buffer.append("\n");
        return buffer.toString();
    }
} //Workbook 클래스


class FileIO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 문제집 파일을 지정된 경로에 저장한다.
	 * @param filePath - 이름과 확장자가 포함된 저장할 파일의 경로
	 * @param wb - Workbook 객체
	 * @return 파일 저장 성공시 true 반환, 실패시 false 반환
	*/
    public boolean saveFile(String filePath, Workbook wb) {
        try {
    		for (int i=0; i<=10; i++) {
    			String path = filePath;
    			
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
    		//파일을 생성하지 못했다면 오류 반환
    		System.out.println("파일을 생성하지 못했습니다.");
    		return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 지정된 경로에서 파일을 불러온다.
	 * @param filePath - 이름과 확장자가 포함된 저장할 파일의 경로
	 * @return 파일 불러오기 성공시 Workbook 객체 반환, 실패시 null 반환
	*/
    public Workbook loadFile(String filePath) {
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
