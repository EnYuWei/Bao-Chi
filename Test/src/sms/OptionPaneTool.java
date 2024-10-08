package sms;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class OptionPaneTool 
{
	/**
     * 顯示自訂的 JOptionPane 提示對話框。
     * @param messageType 提示訊息類型
     * （
     *    JOptionPane.ERROR_MESSAGE【錯誤】,
     *    JOptionPane.INFORMATION_MESSAGE【通知】,
     *    JOptionPane.WARNING_MESSAGE【警告】,
     *    JOptionPane.QUESTION_MESSAGE【問題】,
     *    JOptionPane.PLAIN_MESSAGE【普通】
     *  ）
     * @param message 提示內容文字
     * @param buttonText 按鈕顯示的文字
     */
	public static void showMessageDialog(int messageType, String message, String buttonText) 
	{
        SwingUtilities.invokeLater(() -> 
        {
            // 創建一個 JOptionPane 並設定訊息
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage(message);
            optionPane.setMessageType(messageType);
            
            // 自訂按鈕文字
            Object[] options = { buttonText };
            optionPane.setOptions(options);
            
            // 遞迴設置所有按鈕不可聚焦
            recursiveUnfocusButtons(optionPane);
            
            // 創建對話框
            JDialog dialog = optionPane.createDialog(null, "提示");
            dialog.setVisible(true);
        });
    }

    // 遞迴方法，用於取消所有 JButton 的焦點
    private static void recursiveUnfocusButtons(Component component) 
    {
        if (component instanceof JButton) 
        {
            JButton button = (JButton) component;
            button.setFocusable(false); // 設置按鈕不可聚焦
        } 
        else if (component instanceof Container) 
        {
            // 遍歷所有容器中的元件，繼續檢查
            for (Component c : ((Container) component).getComponents()) 
            {
                recursiveUnfocusButtons(c);
            }
        }
    }

}
