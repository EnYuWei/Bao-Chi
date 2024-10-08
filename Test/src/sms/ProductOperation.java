package sms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.JTextComponent;

public class ProductOperation 
{
	private JFrame frame;
	private JLabel lblProductID;
	private JTextField tfProductID;	
	private JLabel lblYarnSpecification;
	private JTextField tfYarnSpecification;
	private JLabel lblYarnLotNum;
	private JTextField tfYarnLotNum;
	private JLabel lblSingleGrainWeight;
	private JTextField tfSingleGrainWeight;
	private JLabel lblSupplier;
	private JTextField tfSupplier;
	private JLabel lblColorID;
	private JComboBox<String> cbColorID;
	private JButton btnClose;
	private JButton btnSave;
	//連接資料庫
	private String url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
	private String username = "root";
	private String password = "1234";
	
	// 沒傳參數 用來新增產品資訊
	public ProductOperation() 
	{
		initProductFrame();
		try 
		{
	        Connection conn = DriverManager.getConnection(url, username, password);

	        // 查詢客戶總數
	        String sql = "SELECT COUNT(*) AS total_products FROM product";
	        PreparedStatement countStmt = conn.prepareStatement(sql);
	        ResultSet rs = countStmt.executeQuery();
	        
	        int nextProductID = 1; // 預設客戶編號從 1 開始
	        if (rs.next()) 
	        {
	            int totalProducts = rs.getInt("total_products");
	            nextProductID = totalProducts + 1; // 下一個客戶編號
	        }

	        // 將結果顯示在 tfClientID
	        tfProductID.setText(String.valueOf(nextProductID));

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
        	    String yarnSpecification = tfYarnSpecification.getText().trim();
        	    String yarnLotNum = tfYarnLotNum.getText().trim();   
        	    String singleGrainWeight = tfSingleGrainWeight.getText().trim(); 
        	    String supplier = tfSingleGrainWeight.getText().trim();  
        	    String colorID = (String)cbColorID.getSelectedItem(); 
        	    if(colorID.isBlank() || yarnSpecification.isBlank())
        	    {
        	    	// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"紗線規格及色號為必填欄位!" ,"了解");
                    return;
        	    }
            	try 
            	{
            		
					Connection conn = DriverManager.getConnection(url, username, password);
					String sql = "INSERT INTO product (yarn_specification, yarn_lot_num, single_grain_weight, suplier, color_id)"
								+"VALUES (?, ?, ?, ?, ?);";
						
					PreparedStatement stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, yarnSpecification);
					stmt.setString(2, yarnLotNum);
					if (!singleGrainWeight.isBlank()) 
					{
					    stmt.setBigDecimal(3, new BigDecimal(singleGrainWeight));  // 如果你想使用 BigDecimal
					} 
					else {
					    stmt.setNull(3, java.sql.Types.DECIMAL);  // 如果欄位允許為 NULL
					}
					stmt.setString(4, supplier);
					stmt.setString(5, colorID);
					
					stmt.executeUpdate();
					stmt.close();
			        conn.close();
			        frame.dispose();
				} 
            	catch (SQLException e1) 
            	{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"新增產品資訊時發生錯誤：" + e1.getMessage(),"了解");
				}
            }
        });
	}
	// 有傳參數 用來修改產品資訊
	public ProductOperation(String initData[]) 
	{
		initProductFrame();
		tfProductID.setText(initData[0]);
		tfYarnSpecification.setText(initData[2]);
		tfYarnLotNum.setText(initData[3]);
		tfSingleGrainWeight.setText(initData[4]);
		tfSupplier.setText(initData[5]);
		cbColorID.setSelectedItem((Object)initData[1]);
		btnSave.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) 
		    {     	    
		    	String productID = tfProductID.getText().trim();
		    	String yarnSpecification = tfYarnSpecification.getText().trim();
        	    String yarnLotNum = tfYarnLotNum.getText().trim();   
        	    String singleGrainWeight = tfSingleGrainWeight.getText().trim(); 
        	    String supplier = tfSupplier.getText().trim();  
        	    String colorID = (String)cbColorID.getSelectedItem();  
        	    if(colorID.isBlank() || yarnSpecification.isBlank())
        	    {
        	    	// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"紗線規格及色號為必填欄位!" ,"了解");
                    return;
        	    }
		        try 
		        {
		            Connection conn = DriverManager.getConnection(url, username, password);
		            
		            // 修改客戶資料的SQL語句
		            String sql = "UPDATE product SET yarn_specification = ?, yarn_lot_num = ?, single_grain_weight = ?, suplier = ?, color_id = ?"
		            		    +"WHERE id = ?;";
		            
		            PreparedStatement stmt = conn.prepareStatement(sql);
		            stmt.setString(1, yarnSpecification);
					stmt.setString(2, yarnLotNum);	
					if (!singleGrainWeight.isBlank()) 
					{
					    stmt.setBigDecimal(3, new BigDecimal(singleGrainWeight));  // 如果你想使用 BigDecimal
					} 
					else {
					    stmt.setNull(3, java.sql.Types.DECIMAL);  // 如果欄位允許為 NULL
					}
					stmt.setString(4, supplier);
					stmt.setString(5, colorID);
					stmt.setString(6, productID);
					
		            stmt.executeUpdate();
		            stmt.close();
		            conn.close();
		            
		            // 關閉當前視窗
		            frame.dispose();
		            
		            
		        } 
		        catch (SQLException e1) 
		        {
		            e1.printStackTrace();
		            // 顯示錯誤消息
		            OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE, "修改產品資訊時發生錯誤：" + e1.getMessage(), "了解");
		        }
		    }
		});

	}
	private void initProductFrame()
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


	    // 產品編號
	    lblProductID = new JLabel("產品編號");
	    lblProductID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblProductID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblProductID, gbc);
	    // 
	    tfProductID = new JTextField();
	    tfProductID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfProductID.setEditable(false);
	    tfProductID.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gridPanel.add(tfProductID, gbc);
	    
	    // 紗線規格
	    lblYarnSpecification = new JLabel("<html>紗線規格<font color='red'>*</font></html>");
	    lblYarnSpecification.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblYarnSpecification.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gridPanel.add(lblYarnSpecification, gbc);
	    //
	    tfYarnSpecification = new JTextField();
	    tfYarnSpecification.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfYarnSpecification.setPreferredSize(new Dimension(300, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gridPanel.add(tfYarnSpecification, gbc);
	    // 紗線批號
	    lblYarnLotNum = new JLabel("紗線批號");
	    lblYarnLotNum.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    lblYarnLotNum.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblYarnLotNum, gbc);
	    // 
	    tfYarnLotNum = new JTextField();
	    tfYarnLotNum.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
	    tfYarnLotNum.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1; // 調整輸入框的位置，與紅色星號配合
	    gbc.gridy = 2;
	    gridPanel.add(tfYarnLotNum, gbc);
	    // 單粒重量
	    lblSingleGrainWeight = new JLabel("單粒重量");
	    lblSingleGrainWeight.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblSingleGrainWeight.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gridPanel.add(lblSingleGrainWeight, gbc);
	    //
	    tfSingleGrainWeight = new JTextField();
	    tfSingleGrainWeight.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfSingleGrainWeight.setPreferredSize(new Dimension(150, 30));
	    tfSingleGrainWeight.setInputVerifier(new InputVerifier() 
	    {
	        @Override
	        public boolean verify(JComponent input) 
	        {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText();
	            try 
	            {
	                // 檢查數字格式：整數最多3位，小數最多2位
	                if (text.matches("\\d{1,3}(\\.\\d{1,2})?") || text == "0.00" || text.isBlank()) {
	                    return true;
	                }
	            } 
	            catch (NumberFormatException e) {
	            	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "僅限填入數字！", "確認");
	            }
	            OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "重量應為最多三位整數且最多兩位小數的數字！", "確認");
	            return false;
	        }
	    });

	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gridPanel.add(tfSingleGrainWeight, gbc);
	    // 供應來源
	    lblSupplier = new JLabel("供應來源");
	    lblSupplier.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblSupplier.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gridPanel.add(lblSupplier, gbc);
	    //
	    tfSupplier = new JTextField();
	    tfSupplier.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    tfSupplier.setPreferredSize(new Dimension(150, 30));
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gridPanel.add(tfSupplier, gbc);
	    // 色號
	    lblColorID = new JLabel("<html>色號<font color='red'>*</font></html>");
	    lblColorID.setFont(new Font("微軟正黑體", Font.PLAIN,  20));
	    lblColorID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gridPanel.add(lblColorID, gbc);
	    //
	    // 創建可編輯的 JComboBox
	    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
	    cbColorID = new JComboBox<>(model);
	    cbColorID.setEditable(true); //設置為可編輯

	    // 從資料庫查詢所有色號
	    List<String> allColorID = new ArrayList<>();  // 用於儲存所有的色號
	    try 
	    {
	        Connection conn = DriverManager.getConnection(url, username, password);
	        
	        // SQL 查詢語句
	        String sql = "SELECT `id` FROM color";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        allColorID.add("");
	        // 將所有查詢結果加入到 allColorID 列表中
	        while (rs.next()) 
	        {
	            allColorID.add(rs.getString("id"));
	        }
	        
	        // 關閉資源
	        rs.close();
	        stmt.close();
	        conn.close();
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }

	    // 將查詢到的色號加入到 JComboBox 中
	    for (String color : allColorID) 
	    {
	        model.addElement(color);
	    }

	    // 自訂 ComboBox UI
	    cbColorID.setUI(new BasicComboBoxUI() 
	    {
	        protected JButton createArrowButton() 
	        {
	            JButton button = new JButton("▼") 
	            {
	                @Override
	                public void paintComponent(Graphics g) 
	                {
	                    super.paintComponent(g);
	                    Graphics2D g2 = (Graphics2D) g.create();
	                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                    int size = Math.min(getWidth(), getHeight());
	                    int arrowWidth = size / 4;
	                    int arrowHeight = size / 8;
	                    g2.translate((getWidth() - arrowWidth) / 2, (getHeight() - arrowHeight) / 2); // 居中箭頭
	                    g2.fillPolygon(new int[]{0, arrowWidth / 2, arrowWidth}, new int[]{0, arrowHeight, 0}, 3); // 自訂箭頭
	                    g2.dispose();
	                }
	            };
	            button.setBorder(BorderFactory.createEmptyBorder());  // 取消邊框
	            button.setContentAreaFilled(false);  // 取消背景
	            return button;
	        }
	    });

	    cbColorID.setMaximumRowCount(8);  // 設置最多顯示 8 行
	    cbColorID.setPreferredSize(new Dimension(150, 30));// 設定 JComboBox 的預設大小
	    gbc.gridx = 1;
	    gbc.gridy = 5;
	    gridPanel.add(cbColorID, gbc);


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
