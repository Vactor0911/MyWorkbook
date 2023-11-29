import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
 

public class Test {

	public static void main(String[] args) {
		// 이미지 파일 경로
        String imagePath = "D:\\Java\\MyWorkbook\\images\\WorkbookTest.png";

        // 이미지를 읽어옴
        ImageIcon originalImageIcon = new ImageIcon(imagePath);
        Image originalImage = originalImageIcon.getImage();

        // 이미지에 입힐 색
        Color colorToApply = Color.GREEN;

        // 이미지에 색을 입히는 메서드 호출
        Image coloredImage = colorizeImage(originalImage, colorToApply);

        // 결과 이미지를 JFrame에 표시
        displayImage(coloredImage);
	}
	
	private static Image colorizeImage(Image originalImage, Color color) {
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        // 이미지의 RGB 값을 수정하기 위해 RGBImageFilter를 사용
        RGBImageFilter filter = new ColorizeFilter(color.getRGB());

        // FilteredImageSource를 통해 필터 적용
        ImageProducer producer = new FilteredImageSource(originalImage.getSource(), filter);

        // 새 이미지 생성
        Image coloredImage = Toolkit.getDefaultToolkit().createImage(producer);

        return coloredImage;
    }

    private static void displayImage(Image image) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 이미지를 JLabel에 표시
        JLabel label = new JLabel(new ImageIcon(image));
        frame.add(label);

        frame.pack();
        frame.setVisible(true);
    }

    // RGBImageFilter를 상속받아 특정 색으로 이미지를 색상화하는 필터 정의
    static class ColorizeFilter extends RGBImageFilter {
        private int color;

        public ColorizeFilter(int color) {
            // 필터를 통해 색상화할 색상 지정
            this.color = color;
            canFilterIndexColorModel = true;
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            // 기존 픽셀의 알파 채널과 지정된 색상의 RGB 값을 결합하여 새로운 RGB 값을 생성
            int alpha = (rgb >> 24) & 0xFF;
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;
            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }

}
