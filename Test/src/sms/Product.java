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
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.JList;

public class Product  
{
	public JPanel ProductManu;
	private JLabel lblSchema;
	private JPanel panelSearchCondition;
	private JLabel lblProductID;
	private JTextField tfProductID;
	private JLabel lblColorID;
    private JTextField tfColorID;
    private JLabel lblYarnSpecification;
    private JTextField tfYarnSpecification;
    private JLabel lblSupplier;
    private JTextField tfSupplier;
    
    
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JToolBar toolBar;
    private JButton btnInsert;
    private JButton btnClear;
    private JComboBox<String> cbSorting;
    //連接資料庫
  	private String url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
  	private String username = "root";
  	private String password = "1234";
  	private Timer timer;
	public Product() 
	{
		// 主框架
		ProductManu = new JPanel();
		ProductManu.setLayout(null);
		
		// 主框架
		ProductManu = new JPanel();
		ProductManu.setLayout(null);
		
		// 表格名稱
		lblSchema = new JLabel("產品資訊表");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(53, 88, 185, 67);
		ProductManu.add(lblSchema); 
		
		// 篩選條件面板
		panelSearchCondition = new JPanel();
		panelSearchCondition.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
		    new Color(255, 255, 255), new Color(160, 160, 160)), "篩選條件", 
		    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		panelSearchCondition.setBounds(304, 88, 946, 88);
		panelSearchCondition.setLayout(null);
		ProductManu.add(panelSearchCondition);
		JPanel panelProductCondition = new JPanel();
		panelProductCondition.setBounds(10, 22, 926, 55);  // 設置面板的位置和大小
		panelProductCondition.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));  // 使用 FlowLayout 排版
		panelSearchCondition.add(panelProductCondition);

		// 產品編號
		lblProductID = new JLabel("產品編號:");
		lblProductID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelProductCondition.add(lblProductID);

		tfProductID = new JTextField();
		tfProductID.setFont(new Font("Arial", Font.PLAIN, 16));
		tfProductID.setColumns(10);
		panelProductCondition.add(tfProductID);
		
		// 色號
		lblColorID = new JLabel("色號:");
		lblColorID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelProductCondition.add(lblColorID);
		
		tfColorID = new JTextField();
		tfColorID.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfColorID.setColumns(10);
		panelProductCondition.add(tfColorID);
		// 紗線規格
		lblYarnSpecification = new JLabel("紗線規格:");
		lblYarnSpecification.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelProductCondition.add(lblYarnSpecification);

		tfYarnSpecification = new JTextField();
		tfYarnSpecification.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfYarnSpecification.setColumns(10);
		panelProductCondition.add(tfYarnSpecification);
		// 供應來源
		lblSupplier = new JLabel("供應來源:");
		lblSupplier.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelProductCondition.add(lblSupplier);
		
		tfSupplier = new JTextField();
		tfSupplier.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfSupplier.setColumns(10);
		panelProductCondition.add(tfSupplier);
		
		// 表格模型初始化
		tableModel = new DefaultTableModel();
		try 
		{
		    Connection conn = DriverManager.getConnection(url, username, password);
		    String sql = "SELECT "
			            + 	"p.`id` AS '產品編號',"
			            + 	"p.`color_id` AS '色號',"
			            + 	"p.`yarn_specification` AS '紗線規格',"
			            + 	"p.`yarn_lot_num` AS '紗線批號',"
			            + 	"p.`single_grain_weight` AS '單粒重量(KG)',"
			            + 	"p.`suplier` AS '供應來源',"
			            + 	"COUNT(op.`product_id`) AS '訂購次數' "
			            + "FROM `product` p "
			            + "LEFT JOIN `order_product` op ON p.`id` = op.`product_id` "
			            + "GROUP BY p.`id` "
			            + "ORDER BY p.`id` DESC;";

		    PreparedStatement stmt = conn.prepareStatement(sql);
		    ResultSet rs = stmt.executeQuery();

		    
		    tableModel.addColumn("產品編號");
		    tableModel.addColumn("色號");
		    tableModel.addColumn("紗線規格");
		    tableModel.addColumn("紗線批號");
		    tableModel.addColumn("單粒重量(KG)");
		    tableModel.addColumn("供應來源");
		    tableModel.addColumn("訂購次數");


		    while (rs.next()) 
		    {
		       	Object[] rowData = 
		        {
		            rs.getString("產品編號"),
		            rs.getString("色號"),          
		            rs.getString("紗線規格"),
		            rs.getString("紗線批號"),
		            rs.getString("單粒重量(KG)"),
		            rs.getString("供應來源"),
		            rs.getString("訂購次數")
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

                    new ProductOperation(rowData);
                }
            }
        });


        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(80, 266, 1132, 310);
        ProductManu.add(scrollPane);
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
                new ProductOperation(); 
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
        		tfProductID.setText("");
        		tfColorID.setText("");
        		tfYarnSpecification.setText("");
        		tfSupplier.setText("");
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
        cbSorting = new JComboBox<>(new String[]{"最新","最舊","訂購次數最多", "訂購次數最少"});     
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
        ProductManu.add(toolBar);
        
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
	private void updateTable() 
	{
		String productID = tfProductID.getText().trim();  
	    String colorID = tfColorID.getText().trim();    
	    String yarnSpecification = tfYarnSpecification.getText().trim();   
	    String supplier = tfSupplier.getText().trim(); 
	    String sorting = (String) cbSorting.getSelectedItem();
	    String sortRule="";
	    switch(sorting) 
	    {
	    	case "最新":
	    		sortRule = "ORDER BY p.`id` DESC;";
	    		break;
	    	case "最舊":
	    		sortRule = "ORDER BY p.`id` ASC;";
	    		break;
	    	case "訂購次數最多":
	    		sortRule = "ORDER BY COUNT(op.`product_id`) DESC;";
	    		break;
	    	case "訂購次數最少":
	    		sortRule = "ORDER BY COUNT(op.`product_id`) ASC;";
	    		break;
	    }
	    tableModel.setRowCount(0);
	    tableModel.setColumnCount(0);
	    
	    try 
	    {
			Connection conn = DriverManager.getConnection(url, username, password);
			 // 
			String sql =  "SELECT "
			            + "	   p.`id` AS '產品編號',"
			            + "    p.`color_id` AS '色號',"
			            + "    p.`yarn_specification` AS '紗線規格',"
			            + "    p.`yarn_lot_num` AS '紗線批號',"
			            + "    p.`single_grain_weight` AS '單粒重量(KG)',"
			            + "    p.`suplier` AS '供應來源',"
			            + "    COUNT(op.`product_id`) AS '訂購次數' "
			            + "FROM `product` p "
			            + "LEFT JOIN `order_product` op ON p.`id` = op.`product_id` "
			            + "WHERE 1=1"
			            + "    AND (CASE WHEN ? != '' THEN p.`id` LIKE ? ELSE TRUE END)"
			            + "    AND (CASE WHEN ? != '' THEN p.`color_id` LIKE ? ELSE TRUE END)"
			            + "    AND (CASE WHEN ? != '' THEN p.`yarn_specification` LIKE ? ELSE TRUE END)"
			            + "    AND (CASE WHEN ? != '' THEN p.`suplier` LIKE ? ELSE TRUE END)"			          
			            + "GROUP BY p.`id` "
					    + sortRule;
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, productID );
			stmt.setString(2, "%" + productID + "%");
			stmt.setString(3, colorID);
			stmt.setString(4, "%" + colorID + "%");
			stmt.setString(5, yarnSpecification);
			stmt.setString(6, "%" + yarnSpecification + "%");
			stmt.setString(7, supplier);
			stmt.setString(8, "%" + supplier + "%");
			ResultSet rs = stmt.executeQuery();
			         
			tableModel.addColumn("產品編號");
		    tableModel.addColumn("色號");
		    tableModel.addColumn("紗線規格");
		    tableModel.addColumn("紗線批號");
		    tableModel.addColumn("單粒重量(KG)");
		    tableModel.addColumn("供應來源");
		    tableModel.addColumn("訂購次數");
	         
		      // 逐筆取得資料
		      while (rs.next()) 
		      {
		          Object[] rowData = 
		          {
	    		    rs.getString("產品編號"),
		            rs.getString("色號"),          
		            rs.getString("紗線規格"),
		            rs.getString("紗線批號"),
		            rs.getString("單粒重量(KG)"),
		            rs.getString("供應來源"),
		            rs.getString("訂購次數")
		          };
		          tableModel.addRow(rowData);
		      }      
			 //關閉資源
			 conn.close();
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
        return ProductManu;
    }
}
