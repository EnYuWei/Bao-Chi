package sms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Client 
{
	public JPanel ClientManu;
	private JLabel lblSchema;
	private JPanel panelSearchCondition;
	private JLabel lblClientID;
    private JLabel lblClient;
    private JTextField tfClientID;
    private JTextField tfClient;
    private JLabel lblContactPerson;
    private JTextField tfContactPerson;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JToolBar toolBar;
    private JButton btnInsert;
    private JButton btnClear;
//    private JButton btnSearch;
    private JComboBox<String> cbSorting;
  	private JLabel lblAddress;
  	private JTextField tfAddress;
  	
	public Client() 
	{
		// 主框架
		ClientManu = new JPanel();
		ClientManu.setLayout(null);
		
		// 表格名稱
		lblSchema = new JLabel("客戶資料表");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(53, 88, 185, 67);
		ClientManu.add(lblSchema); 
		
		// 篩選條件面板
		panelSearchCondition = new JPanel();
		panelSearchCondition.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
		    new Color(255, 255, 255), new Color(160, 160, 160)), "篩選條件", 
		    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		panelSearchCondition.setBounds(304, 88, 908, 88);
		panelSearchCondition.setLayout(null);
		ClientManu.add(panelSearchCondition);
		JPanel panelClientCondition = new JPanel();
		panelClientCondition.setBounds(10, 22, 888, 55);  // 設置面板的位置和大小
		panelClientCondition.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));  // 使用 FlowLayout 排版
		panelSearchCondition.add(panelClientCondition);

		// 客戶編號
		lblClientID = new JLabel("客戶編號:");
		lblClientID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelClientCondition.add(lblClientID);

		tfClientID = new JTextField();
		tfClientID.setFont(new Font("Arial", Font.PLAIN, 16));
		tfClientID.setColumns(10);
		panelClientCondition.add(tfClientID);
		// 客戶
		lblClient = new JLabel("客戶:");
		lblClient.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelClientCondition.add(lblClient);

		tfClient = new JTextField();
		tfClient.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfClient.setColumns(10);
		panelClientCondition.add(tfClient);
		//聯絡人
		lblContactPerson = new JLabel("聯絡人:");
		lblContactPerson.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelClientCondition.add(lblContactPerson);
		
		tfContactPerson = new JTextField();
		tfContactPerson.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfContactPerson.setColumns(10);
		panelClientCondition.add(tfContactPerson);

		// 地址
		lblAddress = new JLabel("地址:");
		lblAddress.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelClientCondition.add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfAddress.setColumns(10);
		panelClientCondition.add(tfAddress);
		
		
		// 建議字詞
//		SuggestionTool sugClientID = new SuggestionTool("client","id", url, username, password);
//		SuggestionTool sugClient = new SuggestionTool("client","name", url, username, password);
//		SuggestionTool sugContactPerson = new SuggestionTool("client","contact_person", url, username, password);
//		sugClientID.addTextComponent(tfClientID);
//		sugClient.addTextComponent(tfClient);
//		sugContactPerson.addTextComponent(tfContactPerson);
		//初始表格資料
        tableModel = new DefaultTableModel();
		try 
		{
			 // 
			 String sql =
				  "SELECT "
		 		+ "    `id` AS '客戶編號', `unified_number` AS '統一編號', `name` AS '客戶', `contact_person` AS '聯絡人', `contact_phone` AS '連絡電話', `email` AS 'Email', `address` AS '地址'"
		 		+ "FROM "
		 		+ "    `client`"
		 		+ "ORDER BY client.id DESC;";
			 PreparedStatement stmt = Overview.conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery();
			       
	         tableModel.addColumn("客戶編號");
	         tableModel.addColumn("統一編號");
	         tableModel.addColumn("客戶");
	         tableModel.addColumn("聯絡人");
	         tableModel.addColumn("連絡電話");
	         tableModel.addColumn("Email");
	         tableModel.addColumn("地址");
	
		      // 逐筆取得資料
		      while (rs.next()) {
		          Object[] rowData = {
		              rs.getString("客戶編號"),
		              rs.getString("統一編號"),
		              rs.getString("客戶"),
		              rs.getString("聯絡人"),
		              rs.getString("連絡電話"),
		              rs.getString("Email"),
		              rs.getString("地址")
		          };
		          tableModel.addRow(rowData);
		      }      
			 //關閉資源
	         stmt.close();
	         rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		//表格
		table = new JTable();
		table.getTableHeader().setBackground(Color.YELLOW);
		table.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
		table.setEnabled(false);
		table.setModel(tableModel);

        // 設置表頭樣式
        JTableHeader tableHeaderT = table.getTableHeader();
        tableHeaderT.setBackground(new Color(70, 130, 180));
        tableHeaderT.setForeground(Color.WHITE);
        tableHeaderT.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        // 設置表格框線樣式
        table.setShowVerticalLines(false);
        table.setGridColor(Color.GRAY);
        table.setIntercellSpacing(new Dimension(1, 1)); // 設定框線的粗細
        // 設置列高
        table.setRowHeight(35);
        table.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        // 自訂儲存格文字顏色及懸停效果
        class CustomTableCellRenderer extends DefaultTableCellRenderer 
        {
            private int hoveredRow = -1; // 儲存滑鼠懸停的行索引

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //設定文字樣式        
                if(column == 0)
                {
                	cellComponent.setForeground(new Color(70, 130, 180)); // 設定文字顏色為藍色
                	cellComponent.setFont(new Font("微軟正黑體",Font.BOLD, 14)); // 設定文字為粗體
                }
                else 
                {
                    cellComponent.setForeground(Color.BLACK); // 其他列的文字顏色為黑色
                }
                
                // 設定選中狀態的背景顏色或懸停效果
                if (isSelected) {
                    cellComponent.setBackground(new Color(184, 207, 229));
                } else if (row == hoveredRow) { // 如果滑鼠懸停在該行
                    cellComponent.setBackground(new Color(230, 240, 255));
                } else {
                    cellComponent.setBackground(Color.WHITE); // 未選中和未懸停狀態的背景顏色
                }

                // 設定文字垂直和水平對齊
                ((JLabel) cellComponent).setHorizontalAlignment(SwingConstants.CENTER);
                ((JLabel) cellComponent).setVerticalAlignment(SwingConstants.CENTER);

                return cellComponent;
            }

            public void setHoveredRow(int row) {
                this.hoveredRow = row;
            }
        }
        // 創建一個自定義的 Renderer 實例
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);
        
        // 滑鼠懸停事件：監聽滑鼠移動並更新懸停的行
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != -1) {
                    cellRenderer.setHoveredRow(row); // 更新懸停行
                    table.repaint(); // 重新繪製表格以更新效果
                }
            }
        });

        // 滑鼠移出事件：當滑鼠移出表格時重設懸停行
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                cellRenderer.setHoveredRow(-1); // 當滑鼠移出表格時將懸停行設為 -1
                table.repaint(); // 重新繪製表格以取消懸停效果
            }
        });

        // 添加滑鼠點擊監聽器
        table.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
                int row = table.rowAtPoint(e.getPoint()); // 使用 mouse event 的位置來獲取行索引
                // 是否選取資料列
                if (row != -1) 
                { 
                    // 獲取資料行的列數
                    int columnCount = table.getColumnCount();
                    String[] rowData = new String[columnCount]; // 用於存儲行資料

                    // 迭代每一列，獲取值並轉換為 String[]
                    for (int col = 0; col < columnCount; col++) 
                    {
                        Object value = table.getValueAt(row, col); // 獲取當前單元格的值
                        // 如果值為 null，則存儲為空字串
                        rowData[col] = (value != null) ? value.toString() : ""; 
                    }

                    new ClientOperation(rowData);
                }
            }
        });


        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(80, 266, 1132, 310);
        ClientManu.add(scrollPane);
        //工具列
        toolBar = new JToolBar("工具列");
        toolBar.setBounds(80, 236, 172, 30);
        
        
        ImageIcon iconInsert = new ImageIcon("Img/plus.png");
        Image imgInsert = iconInsert.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon Icon1 = new ImageIcon(imgInsert);
        ImageIcon iconClear = new ImageIcon("Img/eraser.png");
        Image imgClear = iconClear.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon Icon2 = new ImageIcon(imgClear);
        ImageIcon iconSearch = new ImageIcon("Img/search.png");
        Image imgSearch = iconSearch.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon Icon3 = new ImageIcon(imgSearch);
        //新增按鈕
        btnInsert = new JButton() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                if (getModel().isPressed()) {
                    g.setColor(Color.LIGHT_GRAY); // 設定被按下時的背景顏色
                } else {
                    g.setColor(getBackground()); // 使用預設背景顏色
                }
                g.fillRect(0, 0, getWidth(), getHeight()); // 繪製背景
                super.paintComponent(g); // 繪製按鈕的其他內容
            }
        };
        btnInsert.setIcon(Icon1);
        btnInsert.setFocusPainted(false);
        btnInsert.setContentAreaFilled(false); // 取消預設背景
        btnInsert.setOpaque(false); // 使背景透明
        btnInsert.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                new ClientOperation();
            }
        });
        // 清除按鈕
        btnClear = new JButton() 
        {
        	@Override
            protected void paintComponent(Graphics g) 
        	{
                if (getModel().isPressed()) {
                    g.setColor(Color.LIGHT_GRAY); // 設定被按下時的背景顏色
                } else {
                    g.setColor(getBackground()); // 使用預設背景顏色
                }
                g.fillRect(0, 0, getWidth(), getHeight()); // 繪製背景
                super.paintComponent(g); // 繪製按鈕的其他內容
            }
        };
        btnClear.setIcon(Icon2);
        btnClear.setFocusPainted(false);
        btnClear.setContentAreaFilled(false); // 取消預設背景
        btnClear.setOpaque(false); // 使背景透明
        btnClear.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		// 清空文本框
                tfClientID.setText("");
                tfClient.setText("");
                tfContactPerson.setText("");
        	}
        });  
        
//        //查詢按鈕
//        btnSearch = new JButton() 
//        {
//            @Override
//            protected void paintComponent(Graphics g) 
//            {
//                if (getModel().isPressed()) {
//                    g.setColor(Color.LIGHT_GRAY); // 設定被按下時的背景顏色
//                } else {
//                    g.setColor(getBackground()); // 使用預設背景顏色
//                }
//                g.fillRect(0, 0, getWidth(), getHeight()); // 繪製背景
//                super.paintComponent(g); // 繪製按鈕的其他內容
//            }
//        };
//        btnSearch.setIcon(Icon3);
//        btnSearch.setFocusPainted(false);
//        btnSearch.setContentAreaFilled(false); // 取消預設背景
//        btnSearch.setOpaque(false); // 使背景透明
//        btnSearch.addActionListener(new ActionListener() 
//        {
//            public void actionPerformed(ActionEvent e) 
//            {
//            	updateTable();
//            }
//        });
        //排序選單
        cbSorting = new JComboBox<>(new String[]{"最新", "最舊"});     
        cbSorting.setSelectedItem("最新"); // 設置預設為 "最舊"
        cbSorting.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        cbSorting.setBackground(Color.WHITE);
        cbSorting.setForeground(Color.GRAY);
        cbSorting.setFocusable(false); // 禁用焦點邊框
        // 自訂 ComboBox UI
        cbSorting.setUI(new BasicComboBoxUI() 
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
                      g2.setColor(Color.GRAY);
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
      
        toolBar.add(btnInsert);
        toolBar.add(btnClear);
//        toolBar.add(btnSearch);
        toolBar.add(cbSorting);
        ClientManu.add(toolBar);
        
        // 添加監聽器到各個元件
        addTextFieldListener(tfClientID);
        addTextFieldListener(tfClient);
        addTextFieldListener(tfContactPerson);
        addTextFieldListener(tfAddress);
        addComboBoxListener(cbSorting);
	}
	// 添加 JTextField 的 DocumentListener
    private void addTextFieldListener(JTextField textField) {
    	// 添加 DocumentListener 監聽輸入變化
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            	updateTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            	updateTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            	updateTable();
            }
        });
    }
    // 添加 JComboBox 的 ActionListener
    private void addComboBoxListener(JComboBox<String> comboBox) {
        comboBox.addActionListener(e -> updateTable());
    }

	public void updateTable()
	{
		String clientID = tfClientID.getText().trim();  
	    String client = tfClient.getText().trim();    
	    String contactPerson =tfContactPerson.getText().trim();   
	    String address = tfAddress.getText().trim(); 
	    String sorting = (String) cbSorting.getSelectedItem();
	    String sortRule="";
	    switch(sorting) 
	    {
	    	case "最新":
	    		sortRule = "ORDER BY client.id DESC;";
	    		break;
	    	case "最舊":
	    		sortRule = "ORDER BY client.id ASC;";
	    		break;
	    }
	    tableModel.setRowCount(0);
	    tableModel.setColumnCount(0);
	    
	    try 
	    {
			 // 
			String sql =
				    "SELECT "
				    + "    `id` AS '客戶編號', `unified_number` AS '統一編號', `name` AS '客戶', `contact_person` AS '聯絡人', `contact_phone` AS '連絡電話', `email` AS 'Email', `address` AS '地址' "
				    + "FROM "
				    + "    `client` "
				    + "WHERE 1=1 "
				    + " AND (CASE WHEN ? != '' THEN client.`id` LIKE ? ELSE TRUE END)  "
				    + " AND (CASE WHEN ? != '' THEN client.`name` LIKE ? ELSE TRUE END)  "
				    + " AND (CASE WHEN ? != '' THEN client.`contact_person` LIKE ? ELSE TRUE END)"
				    + " AND (CASE WHEN ? != '' THEN client.`address` LIKE ? ELSE TRUE END) "
				    + sortRule;
			PreparedStatement stmt = Overview.conn.prepareStatement(sql);
			
			stmt.setString(1, clientID );
			stmt.setString(2, "%" + clientID + "%");
			stmt.setString(3, client);
			stmt.setString(4, "%" + client + "%");
			stmt.setString(5, contactPerson);
			stmt.setString(6, "%" + contactPerson + "%");
			stmt.setString(7, address);
			stmt.setString(8, "%" + address + "%");
			 ResultSet rs = stmt.executeQuery();
			         
	         tableModel.addColumn("客戶編號");
	         tableModel.addColumn("統一編號");
	         tableModel.addColumn("客戶");
	         tableModel.addColumn("聯絡人");
	         tableModel.addColumn("連絡電話");
	         tableModel.addColumn("Email");
	         tableModel.addColumn("地址");
	         
		      // 逐筆取得資料
		      while (rs.next()) 
		      {
		          Object[] rowData = {
		              rs.getString("客戶編號"),
		              rs.getString("統一編號"),
		              rs.getString("客戶"),
		              rs.getString("聯絡人"),
		              rs.getString("連絡電話"),
		              rs.getString("Email"),
		              rs.getString("地址")
		          };
		          tableModel.addRow(rowData);
		      }      
			 //關閉資源
	         stmt.close();
	         rs.close();
		} 
	    catch (SQLException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 回傳主框架(之後要添加於選項卡)
	public JPanel getPanel() 
	{
        return ClientManu;
    }
}
