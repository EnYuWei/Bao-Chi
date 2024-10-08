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
import java.sql.*;

import javax.swing.*;


public class ClientOperation 
{
	private Client client;
	private JFrame frame;
	private JLabel lblClientID;
	private JTextField tfClientID;	
	private JLabel lblUnifiedNumber;
	private JTextField tfUnifiedNumber;
	private JLabel lblClient;
	private JTextField tfClient;
	private JLabel lblContactPerson;
	private JTextField tfContactPerson;
	private JLabel lblContactPhone;
	private JTextField tfContactPhone;
	private JLabel lblEmail;
	private JTextField tfEmail;
	private JLabel lblAddress;
	private JTextArea taAddress;
	private JScrollPane scrollPane;
	private JButton btnClose;
	private JButton btnSave;
	//連接資料庫
	private String url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
	private String username = "root";
	private String password = "1234";
	
	// 沒有傳參數 用來新增客戶資料
	public ClientOperation() 
	{
		initClientFrame();
		try 
		{
	        Connection conn = DriverManager.getConnection(url, username, password);

	        // 查詢客戶總數
	        String sql = "SELECT COUNT(*) AS total_clients FROM client";
	        PreparedStatement countStmt = conn.prepareStatement(sql);
	        ResultSet rs = countStmt.executeQuery();
	        
	        int nextClientID = 1; // 預設客戶編號從 1 開始
	        if (rs.next()) 
	        {
	            int totalClients = rs.getInt("total_clients");
	            nextClientID = totalClients + 1; // 下一個客戶編號
	        }

	        // 將結果顯示在 tfClientID
	        tfClientID.setText(String.valueOf(nextClientID));

	        // 關閉資源
	        rs.close();
	        countStmt.close();
	        conn.close();
	        
	    } 
		catch (SQLException e) 
		{
	        e.printStackTrace();
	    }
		btnSave.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {     	    
        	    String unifiedNumber = tfUnifiedNumber.getText().trim();
        	    String client = tfClient.getText().trim();
        	    String contactPerson = tfContactPerson.getText().trim();   
        	    String contactPhone = tfContactPhone.getText().trim(); 
        	    String email = tfEmail.getText().trim();  
        	    String address = taAddress.getText().trim();  
        	    if(client.isBlank())
        	    {
        	    	// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"客戶為必填欄位!" ,"了解");
                    return;
        	    }
            	try 
            	{
            		
					Connection conn = DriverManager.getConnection(url, username, password);
					String sql = "INSERT INTO client (unified_number, name, contact_person, contact_phone, email, address) VALUES (?, ?, ?, ?, ?, ?);";
						
					PreparedStatement stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, unifiedNumber);
					stmt.setString(2, client);
					stmt.setString(3, contactPerson);
					stmt.setString(4, contactPhone);
					stmt.setString(5, email);
					stmt.setString(6, address);
					stmt.executeUpdate();
					stmt.close();
			        conn.close();
			        frame.dispose();
//			        client.updateTable();
				} 
            	catch (SQLException e1) 
            	{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"新增客戶資料時發生錯誤：" + e1.getMessage(),"了解");
				}
            }
        });
	}
	// 有傳參數 用來修改客戶資料
	public ClientOperation(String[] initData) 
	{
		initClientFrame();
		tfClientID.setText(initData[0]);
		tfUnifiedNumber.setText(initData[1]);
		tfClient.setText(initData[2]);
		tfContactPerson.setText(initData[3]);
		tfContactPhone.setText(initData[4]);
		tfEmail.setText(initData[5]);
		taAddress.setText(initData[6]);
		btnSave.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {     	    
		    	String clientID = tfClientID.getText().trim(); 
		        String unifiedNumber = tfUnifiedNumber.getText().trim();
		        String client = tfClient.getText().trim();
		        String contactPerson = tfContactPerson.getText().trim();   
		        String contactPhone = tfContactPhone.getText().trim(); 
		        String email = tfEmail.getText().trim();  
		        String address = taAddress.getText().trim();  

		        try 
		        {
		            Connection conn = DriverManager.getConnection(url, username, password);
		            
		            // 修改客戶資料的SQL語句
		            String sql = "UPDATE client SET unified_number = ?, name = ?, contact_person = ?, contact_phone = ?, email = ?, address = ? WHERE id = ?;";
		            
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            stmt.setString(1, unifiedNumber);
		            stmt.setString(2, client);
		            stmt.setString(3, contactPerson);
		            stmt.setString(4, contactPhone);
		            stmt.setString(5, email);
		            stmt.setString(6, address);
		            stmt.setString(7, clientID);  // 根據ID來定位要修改的客戶
		            
		            stmt.executeUpdate();
		            stmt.close();
		            conn.close();
		            
		            // 關閉當前視窗
		            frame.dispose();
		            
		            // 刷新主頁面的表格
//		            client.updateTable();
		            
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
	
	private void initClientFrame()
	{
		// 建立全螢幕的 JFrame
	    frame = new JFrame();
	    frame.setBackground(Color.DARK_GRAY);
	    frame.setForeground(Color.DARK_GRAY);
	    frame.setUndecorated(true); // 移除邊框
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 設定為全屏
	    frame.getContentPane().setBackground(Color.DARK_GRAY); // 深灰色背景

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


	    // 客戶編號
	    lblClientID = new JLabel("客戶編號");
	    lblClientID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblClientID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblClientID, gbc);
	    // 
	    tfClientID = new JTextField();
	    tfClientID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfClientID.setEditable(false);
	    tfClientID.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gridPanel.add(tfClientID, gbc);
	    
	    // 統一編號
	    lblUnifiedNumber = new JLabel("統一編號");
	    lblUnifiedNumber.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblUnifiedNumber.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gridPanel.add(lblUnifiedNumber, gbc);
	    //
	    tfUnifiedNumber = new JTextField();
	    tfUnifiedNumber.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfUnifiedNumber.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gridPanel.add(tfUnifiedNumber, gbc);
	    // 客戶（使用 JLabel，並用 HTML 設置 * 為紅色）
	    lblClient = new JLabel("<html>客戶<font color='red'>*</font></html>");
	    lblClient.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    lblClient.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblClient, gbc);
	    // 
	    tfClient = new JTextField();
	    tfClient.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    tfClient.setPreferredSize(new Dimension(250, 30));
	    gbc.gridx = 1; // 調整輸入框的位置，與紅色星號配合
	    gbc.gridy = 2;
	    gridPanel.add(tfClient, gbc);
	    // 聯絡人
	    lblContactPerson = new JLabel("聯絡人");
	    lblContactPerson.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblContactPerson.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gridPanel.add(lblContactPerson, gbc);
	    //
	    tfContactPerson = new JTextField();
	    tfContactPerson.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfContactPerson.setPreferredSize(new Dimension(250, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gridPanel.add(tfContactPerson, gbc);
	    // 連絡電話
	    lblContactPhone = new JLabel("連絡電話");
	    lblContactPhone.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblContactPhone.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gridPanel.add(lblContactPhone, gbc);
	    //
	    tfContactPhone = new JTextField();
	    tfContactPhone.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfContactPhone.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gridPanel.add(tfContactPhone, gbc);
	    // email
	    lblEmail = new JLabel("Email");
	    lblEmail.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblEmail.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gridPanel.add(lblEmail, gbc);
	    //
	    tfEmail = new JTextField();
	    tfEmail.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfEmail.setPreferredSize(new Dimension(400, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 5;
	    gridPanel.add(tfEmail, gbc);
	    // 地址
	    lblAddress = new JLabel("地址");
	    lblAddress.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    lblAddress.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 6;
	    gridPanel.add(lblAddress, gbc);

	    // 使用 JTextArea 設置三行地址顯示
	    taAddress = new JTextArea();
	    taAddress.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    taAddress.setRows(3);  // 設定顯示 3 行
	    taAddress.setLineWrap(true);  // 自動換行
	    taAddress.setWrapStyleWord(true);  // 依照單詞換行
	    scrollPane = new JScrollPane(taAddress);  // 添加滾動條
	    scrollPane.setPreferredSize(new Dimension(600, 100));  // 設定合適的高度和寬度
	    gbc.gridx = 1;
	    gbc.gridy = 6;
	    gridPanel.add(scrollPane, gbc);

	    // 統一編號檢查：只能輸入8位數字或留空
	    tfUnifiedNumber.setInputVerifier(new InputVerifier() 
	    {
	        @Override
	        public boolean verify(JComponent input) {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            // 檢查是否為空或是否為8位數字
	            if (text.isBlank() || text.matches("\\d{8}")) 
	            {
	                return true;
	            } 
	            else 
	            {
	            	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE,"統一編號必須是8位數字!","確認");
	                return false;
	            }
	        }
	    });

	    // 連絡電話檢查：只能輸入數字與減號
	    tfContactPhone.setInputVerifier(new InputVerifier() 
	    {
	        @Override
	        public boolean verify(JComponent input) 
	        {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            // 檢查是否只包含數字和 '-'
	            if (text.isBlank() || text.matches("[0-9\\-]+" )) 
	            {
	                return true;
	            } 
	            else 
	            {
	            	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE,"連絡電話只能包含數字和'-'!","確認");
	                return false;
	            }
	        }
	    });

	    // Email 檢查：使用正規表達式來檢查 Email 格式
	    tfEmail.setInputVerifier(new InputVerifier() 
	    {
	        @Override
	        public boolean verify(JComponent input) 
	        {
	            JTextField textField = (JTextField) input;
	            String email = textField.getText();
	            // 簡單的 Email 格式檢查
	            if (email.isBlank() || email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) 
	            {
	                return true;
	            } 
	            else 
	            {
	            	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE,"請輸入有效的 Email 地址!","確認");
	                return false;
	            }
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
