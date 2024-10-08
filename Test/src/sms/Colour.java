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
import java.sql.SQLException;
import java.util.Arrays;

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
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Colour 
{
	public JPanel ColorManu;
	private JLabel lblSchema;
	private JPanel panelSearchCondition;
	private JLabel lblColorID;
	private JTextField tfColorID;
	private JLabel lblColor;
	private JTextField tfColor;
	private JLabel lblBrightness;
	private JComboBox<String> cbBrightness;
	private JLabel lblVividness;
	private JComboBox<String> cbVividness;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JToolBar toolBar;
    private JButton btnInsert;
    private JButton btnClear;
//    private JButton btnSearch;
    private JComboBox<String> cbSorting;
    //連接資料庫
  	private String url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
  	private String username = "root";
  	private String password = "1234";
  	private Timer timer;
	public Colour() 
	{
		// 主框架
		ColorManu = new JPanel();
		ColorManu.setLayout(null);
		
		// 主框架
		ColorManu = new JPanel();
		ColorManu.setLayout(null);
		
		// 表格名稱
		lblSchema = new JLabel("顏色資料表");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(53, 88, 185, 67);
		ColorManu.add(lblSchema); 
		
		// 篩選條件面板
		panelSearchCondition = new JPanel();
		panelSearchCondition.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
		    new Color(255, 255, 255), new Color(160, 160, 160)), "篩選條件", 
		    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		panelSearchCondition.setBounds(304, 88, 908, 88);
		panelSearchCondition.setLayout(null);
		ColorManu.add(panelSearchCondition);
		JPanel panelColorCondition = new JPanel();
		panelColorCondition.setBounds(10, 22, 888, 55);  // 設置面板的位置和大小
		panelColorCondition.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));  // 使用 FlowLayout 排版
		panelSearchCondition.add(panelColorCondition);

		// 色號
		lblColorID = new JLabel("色號:");
		lblColorID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelColorCondition.add(lblColorID);

		tfColorID = new JTextField();
		tfColorID.setFont(new Font("Arial", Font.PLAIN, 16));
		tfColorID.setColumns(10);
		panelColorCondition.add(tfColorID);
		// 顏色
		lblColor = new JLabel("顏色:");
		lblColor.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelColorCondition.add(lblColor);

		tfColor = new JTextField();
		tfColor.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfColor.setColumns(10);
		panelColorCondition.add(tfColor);
		
		// 明度
		lblBrightness = new JLabel("明度:");
		lblBrightness.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelColorCondition.add(lblBrightness);
		
		cbBrightness = new JComboBox<String>();
		cbBrightness.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		cbBrightness.setBackground(Color.WHITE);
		cbBrightness.setFocusable(false); // 禁用焦點邊框
		cbBrightness.addItem("");
		cbBrightness.addItem("淺色");
		cbBrightness.addItem("中間色");
		cbBrightness.addItem("深色");
		panelColorCondition.add(cbBrightness);
		// 鮮豔度
		lblVividness = new JLabel("鮮豔度:");
		lblVividness.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelColorCondition.add(lblVividness);
		
		cbVividness = new JComboBox<String>();
		cbVividness.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		cbVividness.setBackground(Color.WHITE);
		cbVividness.setFocusable(false); // 禁用焦點邊框
		cbVividness.setEnabled(false);
		cbVividness.addItem("");
		cbVividness.addItem("低鮮豔度");
		cbVividness.addItem("高鮮豔度");
		panelColorCondition.add(cbVividness);
		
		// 明度選擇監聽器
		cbBrightness.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) 
		    {
		        String selectedBrightness = (String) cbBrightness.getSelectedItem();
		        
		        if ("".equals(selectedBrightness) || "深色".equals(selectedBrightness) ) {
		            cbVividness.setSelectedItem(""); // 將鮮豔度設為空
		            cbVividness.setEnabled(false);   // 禁用鮮豔度選擇
		        } else {
		            cbVividness.setEnabled(true);    // 啟用鮮豔度選擇
		        }
		    }
		});

		// 建議字詞
//		SuggestionTool sugColorID = new SuggestionTool("color","id", url, username, password);
//		SuggestionTool sugColor = new SuggestionTool("color","name", url, username, password);
//		sugColorID.addTextComponent(tfColorID);
//		sugColor.addTextComponent(tfColor);

		// 表格模型初始化
		tableModel = new DefaultTableModel();
		try 
		{
		    Connection conn = DriverManager.getConnection(url, username, password);
		    String sql = "SELECT `id` AS '色號', `name` AS '顏色', `L` AS 'L(明度)', `C` AS 'C(鮮豔度)', `H` AS 'H(色相)' "
		    			+"FROM `color` "
		    			+"ORDER BY `L`  DESC";
		    PreparedStatement stmt = conn.prepareStatement(sql);
		    ResultSet rs = stmt.executeQuery();

		    
		    tableModel.addColumn("顏色圖塊");
		    tableModel.addColumn("色號");
		    tableModel.addColumn("顏色");
		    tableModel.addColumn("L(明度)");
		    tableModel.addColumn("C(鮮豔度)");
		    tableModel.addColumn("H(色相)");

		    while (rs.next()) 
		    {
		        // 獲取 LCH 值
		        double L = rs.getDouble("L(明度)");  
		        double C = rs.getDouble("C(鮮豔度)");
		        double H = rs.getDouble("H(色相)");   
		        
		        // 轉換 LCH 為 RGB
		        int[] rgb = ColorConverter.LCHtoRGB(L, C, H);
		        Color color = new Color(rgb[0], rgb[1], rgb[2]); // 在這裡創建 Color 物件
		        Object[] rowData = 
		        {
		            color, // 將 Color 物件放入 rowData 中
		            rs.getString("色號"),          
		            rs.getString("顏色"),
		            rs.getString("L(明度)"),
		            rs.getString("C(鮮豔度)"),
		            rs.getString("H(色相)")
		        };
		        tableModel.addRow(rowData); // 將 rowData 添加到表格模型
		    }

		    conn.close();
		    stmt.close();
		    rs.close();
		} 
		catch (SQLException e) 
		{
		    e.printStackTrace();
		}

		// 設定表格並使用顏色渲染器
		table = new JTable(tableModel);
		table.getTableHeader().setBackground(Color.YELLOW);
		table.getTableHeader().setFont(new Font("微軟正黑體", Font.BOLD, 14));
		table.setEnabled(false);
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
		// 使用 DefaultTableCellRenderer 來顯示顏色圖塊
		table.getColumn("顏色圖塊").setCellRenderer(new DefaultTableCellRenderer() 
		{
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			    JPanel panel = new JPanel();
			    
			    // 確保值是 Color 類型
			    if (value instanceof Color) {
			        Color color = (Color) value;
			        panel.setBackground(color);
			    } 
			    // 設定邊框與面板大小
			    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			    panel.setPreferredSize(new Dimension(30, 30));
			    
			    return panel;
			}

		});
		// 自訂儲存格文字顏色及懸停效果
        class CustomTableCellRenderer extends DefaultTableCellRenderer 
        {
            private int hoveredRow = -1; // 儲存滑鼠懸停的行索引

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                //設定文字樣式        
                if(column == 1)
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
                int column = table.columnAtPoint(e.getPoint()); 
                // 是否選取資料列
                if (row != -1 && column != 0) 
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

                    new ColourOperation(rowData);
                }
            }
        });
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(80, 266, 1132, 310);
        ColorManu.add(scrollPane);
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
                new ColourOperation();
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
        		tfColorID.setText("");
        		tfColor.setText("");
        		cbBrightness.setSelectedItem("");
        		cbVividness.setSelectedItem("");
        		
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
        cbSorting = new JComboBox<>(new String[]{"最淺", "最深"});     
        cbSorting.setSelectedItem("最淺"); // 設置預設為 "最淺"
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
        ColorManu.add(toolBar);
        
        timer = new Timer(300, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();  // 每 5 秒執行一次更新表格操作
            }
        });
	}
	// 啟動計時器
    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // 停止計時器
    public void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
	// 更新表格
	public void updateTable()
	{
		String colorID = tfColorID.getText().trim();  
	    String color = tfColor.getText().trim(); 
	    String brightness = (String) cbBrightness.getSelectedItem();
	    String vividness = (String) cbVividness.getSelectedItem();
	    String sorting = (String) cbSorting.getSelectedItem();
	    String sortRule="";
	    switch(sorting) 
	    { 
	    	case "最淺":
	    		sortRule = "ORDER BY color.`L` DESC;";
	    		break;
	    	case "最深":
	    		sortRule = "ORDER BY color.`L` ASC;";
	    		break;
	    }
	    tableModel.setRowCount(0);
	    tableModel.setColumnCount(0);
	    
	    try 
	    {
			Connection conn = DriverManager.getConnection(url, username, password);
	
			String sql =
				    "SELECT "
				   + "     `id` AS '色號', `name` AS '顏色', `L` AS 'L(明度)', `C` AS 'C(鮮豔度)', `H` AS 'H(色相)' "
				   + "FROM "
				   + "     color "
				   + "WHERE 1=1 "
				   + " AND (CASE WHEN ? != '' THEN color.`id` LIKE ? ELSE TRUE END)  "
				   + " AND (CASE WHEN ? != '' THEN color.`name` LIKE ? ELSE TRUE END)  "
				   + " AND (CASE "
				   + "         WHEN ? = '淺色' THEN "
				   + "             (color.`L` >= 80 AND "
				   + "              (CASE WHEN ? = '低鮮豔度' THEN color.`C` <= 20 "
				   + "                    WHEN ? = '高鮮豔度' THEN color.`C` > 20 "
				   + "                    ELSE TRUE END)) "
				   + "         WHEN ? = '中間色' THEN "
				   + "             (color.`L` >= 40 AND color.`L` < 80 AND "
				   + "              (CASE WHEN ? = '低鮮豔度' THEN color.`C` <= 50 "
				   + "                    WHEN ? = '高鮮豔度' THEN color.`C` > 50 "
				   + "                    ELSE TRUE END)) "
				   + "         WHEN ? = '深色' THEN color.`L` < 40 "
				   + "         ELSE TRUE "
				   + "     END) "
				   + sortRule;

			PreparedStatement stmt = conn.prepareStatement(sql);

			// 設定參數
			stmt.setString(1, colorID);
			stmt.setString(2, "%" + colorID + "%");
			stmt.setString(3, color);
			stmt.setString(4, "%" + color + "%");
			stmt.setString(5, brightness); // 明度篩選
			stmt.setString(6, vividness);   // 鮮豔度篩選（低或高）
			stmt.setString(7, vividness);   // 用於淺色的低鮮豔度篩選
			stmt.setString(8, brightness);   // 中間色的明度篩選
			stmt.setString(9, vividness);   // 用於中間色的低鮮豔度篩選
			stmt.setString(10, vividness);   // 用於中間色的高鮮豔度篩選
			stmt.setString(11, brightness);   // 深色的明度篩選
			ResultSet rs = stmt.executeQuery();
			tableModel.addColumn("顏色圖塊");
		    tableModel.addColumn("色號");
		    tableModel.addColumn("顏色");
		    tableModel.addColumn("L(明度)");
		    tableModel.addColumn("C(鮮豔度)");
		    tableModel.addColumn("H(色相)");

		    while (rs.next()) 
		    {
		        // 獲取 LCH 值
		        double L = rs.getDouble("L(明度)");  
		        double C = rs.getDouble("C(鮮豔度)");
		        double H = rs.getDouble("H(色相)");   
		        
		        // 轉換 LCH 為 RGB
		        int[] rgb = ColorConverter.LCHtoRGB(L, C, H);
		        Color colorData = new Color(rgb[0], rgb[1], rgb[2]); // 在這裡創建 Color 物件
		        Object[] rowData = 
		        {
		        	colorData, // 將 Color 物件放入 rowData 中
		            rs.getString("色號"),          
		            rs.getString("顏色"),
		            rs.getString("L(明度)"),
		            rs.getString("C(鮮豔度)"),
		            rs.getString("H(色相)")
		        };
		        tableModel.addRow(rowData); // 將 rowData 添加到表格模型
		    }

		    conn.close();
		    stmt.close();
		    rs.close();
		} 
	    catch (SQLException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    // 使用 DefaultTableCellRenderer 來顯示顏色圖塊
 		table.getColumn("顏色圖塊").setCellRenderer(new DefaultTableCellRenderer() 
 		{
 			@Override
 			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
 			    JPanel panel = new JPanel();
 			    
 			    // 確保值是 Color 類型
 			    if (value instanceof Color) {
 			        Color color = (Color) value;
 			        panel.setBackground(color);
 			    } 
 			    // 設定邊框與面板大小
 			    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
 			    panel.setPreferredSize(new Dimension(30, 30));
 			    
 			    return panel;
 			}

 		});
	}
	// 回傳主框架(之後要添加於選項卡)
	public JPanel getPanel() 
	{
        return ColorManu;
    }
}
