package sms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ColourOperation 
{
	private JFrame frame;
	private JLabel lblColorID;
	private JTextField tfColorID;	
	private JLabel lblColor;
	private JTextField tfColor;
	private JLabel lblL;
	private JTextField tfL;
	private JLabel lblC;
	private JTextField tfC;
	private JLabel lblH;
	private JTextField tfH;
	private JButton btnClose;
	private JButton btnSave;
	
	// 沒傳參數 用於新增顏色資料
	public ColourOperation() 
	{
		// TODO Auto-generated constructor stub
		initColorFrame();
		btnSave.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    { 
		    	String colorID = tfColorID.getText().trim();
		        String color = tfColor.getText().trim();
		        String l = tfL.getText().trim();   
		        String c = tfC.getText().trim(); 
		        String h = tfH.getText().trim();  
		        if (colorID.isBlank() || l.isBlank() || c.isBlank() || h.isBlank()) 
		        {
		        	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "色號、L、C、H 為必填欄位！", "了解");
		        	return;
		        }
		        try 
		        {
					String sql = "INSERT INTO color (id, name, L, C, H) "
							   + "VALUES (?, ?, ?, ?, ?);";
					PreparedStatement stmt = Overview.conn.prepareStatement(sql);	
					
					stmt.setString(1, colorID);
					stmt.setString(2, color);
					stmt.setBigDecimal(3, new BigDecimal(l));
					stmt.setBigDecimal(4, new BigDecimal(c));
					stmt.setBigDecimal(5, new BigDecimal(h));
					stmt.executeUpdate();
					stmt.close();
			        frame.dispose();
				} 
		        catch (SQLException e1) 
		        {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});

	}
	// 有傳參數 用於修改顏色資料
	public ColourOperation(String initData[]) 
	{
		// TODO Auto-generated constructor stub
		initColorFrame();
		lblColorID.setText("色號");
		tfColorID.setEditable(false);
		tfColorID.setText(initData[1]);
		tfColor.setText(initData[2]);
		tfL.setText(initData[3]);
		tfC.setText(initData[4]);
		tfH.setText(initData[5]);
		btnSave.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {     	    
		        String colorID = tfColorID.getText().trim();
		        String color = tfColor.getText().trim();
		        String l = tfL.getText().trim();   
		        String c = tfC.getText().trim(); 
		        String h = tfH.getText().trim();  
		        if ( l.isBlank() || c.isBlank() || h.isBlank()) 
		        {
		        	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "L、C、H 不能為空值！", "了解");
		        }
		        try 
		        {		            
		            // 修改客戶資料的SQL語句
		            String sql = "UPDATE color SET  name = ?, L = ?, C = ?, H = ? WHERE id = ?;";
		            
		            PreparedStatement stmt = Overview.conn.prepareStatement(sql);
		            stmt.setString(1, color);
		            stmt.setString(2, l);
		            stmt.setString(3, c);
		            stmt.setString(4, h);
		            stmt.setString(5, colorID);
		            
		            stmt.executeUpdate();
		            stmt.close();
		            
		            // 關閉當前視窗
		            frame.dispose();
		            
		            
		        } 
		        catch (SQLException e1) 
		        {
		            e1.printStackTrace();
		            // 顯示錯誤消息
		            OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE, "修改客戶資料時發生錯誤：" + e1.getMessage(), "了解");
		        }
		    }
		});

	}
	// 
	private void initColorFrame()
	{
		// 建立全螢幕的 JFrame
	    frame = new JFrame();
	    frame.setBackground(Color.DARK_GRAY);
	    frame.setForeground(Color.DARK_GRAY);
	    frame.setUndecorated(true); // 移除邊框
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 設定為全屏
	    frame.getContentPane().setBackground(Color.DARK_GRAY); // 黑色背景，帶有透明度

	    // 設定關閉按鈕（右上角大叉叉）
	    btnClose = new JButton("X 關閉");
	    btnClose.setFont(new Font("微軟正黑體", Font.BOLD, 28));
	    btnClose.setForeground(Color.WHITE);
	    btnClose.setBorderPainted(false); // 不顯示邊框
	    btnClose.setFocusPainted(false);
	    btnClose.setContentAreaFilled(false); // 按鈕透明背景
	    btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    btnClose.addActionListener(e -> frame.dispose()); // 點擊關閉視窗

	    // 將關閉按鈕置於右上角
	    JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    closePanel.setOpaque(false); // 設定透明背景
	    closePanel.add(btnClose);

	    // 建立 GridBagLayout 來排版
	    JPanel gridPanel = new JPanel(new GridBagLayout());
	    gridPanel.setOpaque(false); // 背景透明
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 5, 10, 5); // 設置元件間的邊距
	    // 色號
	    lblColorID = new JLabel("<html>色號<font color='red'>*</font></html>");
	    lblColorID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblColorID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblColorID, gbc);
	    // 
	    tfColorID = new JTextField();
	    tfColorID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfColorID.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gridPanel.add(tfColorID, gbc);
	
	    // 顏色
	    lblColor = new JLabel("顏色");
	    lblColor.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblColor.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gridPanel.add(lblColor, gbc);
	    //
	    tfColor = new JTextField();
	    tfColor.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfColor.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gridPanel.add(tfColor, gbc);
	    // L
	    lblL = new JLabel("<html>L<font color='red'>*</font></html>");
	    lblL.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblL.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblL, gbc);
	    //
	    tfL = new JTextField();
	    tfL.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfL.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gridPanel.add(tfL, gbc);
	    // C
	    lblC = new JLabel("<html>C<font color='red'>*</font></html>");
	    lblC.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblC.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gridPanel.add(lblC, gbc);
	    //
	    tfC = new JTextField();
	    tfC.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfC.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gridPanel.add(tfC, gbc);
	    // H
	    lblH = new JLabel("<html>H<font color='red'>*</font></html>");
	    lblH.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblH.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gridPanel.add(lblH, gbc);
	    //
	    tfH = new JTextField();
	    tfH.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfH.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gridPanel.add(tfH, gbc);
	    // L值驗證：只能輸入0~100，且最多兩位小數
	    tfL.setInputVerifier(new InputVerifier() {
	        @Override
	        public boolean verify(JComponent input) {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            // 如果輸入為空白，允許通過驗證，不強制使用者填寫
	            if (text.trim().isBlank()) {
	                return true;
	            }
	            try {
	                double value = Double.parseDouble(text);
	                // 檢查範圍和小數位數
	                if (value >= 0 && value <= 100 && text.matches("\\d+(\\.\\d{1,2})?")) {
	                    return true;
	                } 
	            } catch (NumberFormatException e) {
	                // 處理非數字輸入
	            }
	            OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "L值必須在0到100之間，並且最多兩位小數！", "確認");
	            return false;
	        }
	    });

	    // C值驗證：只能輸入0~100，且最多兩位小數
	    tfC.setInputVerifier(new InputVerifier() {
	        @Override
	        public boolean verify(JComponent input) {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            // 如果輸入為空白，允許通過驗證，不強制使用者填寫
	            if (text.trim().isBlank()) {
	                return true;
	            }
	            try {
	                double value = Double.parseDouble(text);
	                // 檢查範圍和小數位數
	                if (value >= 0 && value <= 100 && text.matches("\\d+(\\.\\d{1,2})?")) {
	                    return true;
	                } 
	            } catch (NumberFormatException e) {
	                // 處理非數字輸入
	            }
	            OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "C值必須在0到100之間，並且最多兩位小數！", "確認");
	            return false;
	        }
	    });

	    // H值驗證：只能輸入0~360，且最多兩位小數
	    tfH.setInputVerifier(new InputVerifier() {
	        @Override
	        public boolean verify(JComponent input) {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            // 如果輸入為空白，允許通過驗證，不強制使用者填寫
	            if (text.trim().isBlank()) {
	                return true;
	            }
	            try {
	                double value = Double.parseDouble(text);
	                // 檢查範圍和小數位數
	                if (value >= 0 && value <= 360 && text.matches("\\d+(\\.\\d{1,2})?")) {
	                    return true;
	                } 
	            } catch (NumberFormatException e) {
	                // 處理非數字輸入
	            }
	            OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "H值必須在0到360之間，並且最多兩位小數！", "確認");
	            return false;
	        }
	    });

	    // 設置按鈕面板
	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    buttonPanel.setOpaque(false); // 背景透明
	    // 儲存按鈕
	    btnSave = new JButton("儲存");
	    btnSave.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnSave.setBackground(Color.WHITE);
	    btnSave.setForeground(Color.BLACK);
	    btnSave.setFocusPainted(false);  
	    buttonPanel.add(btnSave);
	    // 使用 BorderLayout 來排版，將元素置於不同區域
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setOpaque(false); // 背景透明
	    mainPanel.add(closePanel, BorderLayout.NORTH); // 關閉按鈕在上方
	    mainPanel.add(gridPanel, BorderLayout.CENTER); // 表格在中間
	    mainPanel.add(buttonPanel, BorderLayout.SOUTH); // 按鈕區域在下方
	    
	    
	    // 設置 frame 的內容
	    frame.setContentPane(mainPanel);

	    // 顯示視窗
	    frame.setVisible(true);
	}
	
}
