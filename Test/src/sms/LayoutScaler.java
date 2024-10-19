package sms;

import java.awt.Dimension;
import java.awt.Toolkit;

public class LayoutScaler 
{
	private final double widthRatio, heightRatio;
    private static final double BASE_WIDTH = 1280.0;  // 基準寬度
    private static final double BASE_HEIGHT = 720.0;  // 基準高度
    
    // 建構子：計算螢幕與基準尺寸的比例
    public LayoutScaler() 
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  // 取得螢幕實際尺寸
        widthRatio = screenSize.getWidth() / BASE_WIDTH;     // 計算寬度比例
        heightRatio = screenSize.getHeight() / BASE_HEIGHT;  // 計算高度比例
    }
    // 根據寬度比例縮放座標或大小，並轉換成 int。
    public int scaleX(double value) 
    {
        return (int) (value * widthRatio);
    }
    // 根據高度比例縮放座標或大小，並轉換成 int。
    public int scaleY(double value) 
    {
        return (int) (value * heightRatio);
    }
}
