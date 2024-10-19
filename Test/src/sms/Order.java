package sms;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Date;
import java.util.Properties;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.awt.event.*;

import javax.swing.border.EtchedBorder;

import java.awt.FlowLayout;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.RenderingHints;

public class Order 
{
	//GUI
	public JPanel OrderManu;
	private JLabel lblSchema;
	private JLabel lblOrderID;
    private JLabel lblClient;
    private JLabel lblOrderDate;
    private JLabel lblProcessStatus;
    private JLabel lblProductID;
    private JLabel lblDeliveryDate;
    private JLabel lblDeliveryTo;
    private JTextField tfOrderID;
    private JTextField tfClient;
    private JComboBox<String> cbProcessStatus;
    private JTextField tfProductID;
    private String placeholderProductID;
    private JPanel panelSearchCondition ;
	private JTable table;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JButton btnClear;
	private JComboBox<String> cbSorting;
	private JButton btnInsert;

    //其他
    private DefaultTableModel tableModel;
    private JDatePickerImpl dpOrderStartDate ;
    private JDatePickerImpl dpOrderEndDate;
    private JDatePickerImpl dpDeliveryStartDate;
    private JDatePickerImpl dpDeliveryEndDate;

    private JLabel lblOrderTo;
    public Boolean canInsertOrder;
	public Order() 
	{
		
		// 主框架
		OrderManu = new JPanel();
		OrderManu.setLayout(null);
		// 等比例縮放元件尺寸
		LayoutScaler scaler = new LayoutScaler();
		// 表格名稱
		lblSchema = new JLabel("訂單紀錄表");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(scaler.scaleX(53), scaler.scaleY(88),scaler.scaleX(185), scaler.scaleY(67));
		OrderManu.add(lblSchema);        

		// 篩選條件面板
		panelSearchCondition = new JPanel();
		panelSearchCondition.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, 
		    new Color(255, 255, 255), new Color(160, 160, 160)), "篩選條件", 
		    TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		panelSearchCondition.setBounds(scaler.scaleX(293), scaler.scaleY(46), scaler.scaleX(919), scaler.scaleY(192));
		OrderManu.add(panelSearchCondition);
		panelSearchCondition.setLayout(null);

		// 上半部 (訂單條件)
		JPanel panelOrderCondition = new JPanel();
		panelOrderCondition.setBounds(scaler.scaleX(10), scaler.scaleY(21), scaler.scaleX(885), scaler.scaleY(114));
		panelSearchCondition.add(panelOrderCondition);
		panelOrderCondition.setLayout(null);

		// 訂單編號
		lblOrderID = new JLabel("訂單編號:");
		lblOrderID.setBounds(scaler.scaleX(10), scaler.scaleY(14), scaler.scaleX(68), scaler.scaleY(23));
		lblOrderID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelOrderCondition.add(lblOrderID);

		tfOrderID = new JTextField();
		tfOrderID.setBounds(scaler.scaleX(120), scaler.scaleY(14), scaler.scaleX(136), scaler.scaleY(25));
		tfOrderID.setFont(new Font("Arial", Font.PLAIN, 16));
		tfOrderID.setColumns(10);
		panelOrderCondition.add(tfOrderID);
		
		// 處理狀態
		lblProcessStatus = new JLabel("狀態:");
		lblProcessStatus.setBounds(scaler.scaleX(296), scaler.scaleY(14), scaler.scaleX(36), scaler.scaleY(23));
		panelOrderCondition.add(lblProcessStatus);
		lblProcessStatus.setFont(new Font("微軟正黑體", Font.BOLD, 16));

		cbProcessStatus = new JComboBox<String>();
		cbProcessStatus.setBounds(scaler.scaleX(353), scaler.scaleY(10), scaler.scaleX(91), scaler.scaleY(31));
		panelOrderCondition.add(cbProcessStatus);
		cbProcessStatus.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		cbProcessStatus.setBackground(Color.WHITE);
		cbProcessStatus.setFocusable(false); // 禁用焦點邊框
		cbProcessStatus.setMaximumRowCount(11); // 可顯示10個選項
		cbProcessStatus.addItem("");
		cbProcessStatus.addItem("草稿");
		cbProcessStatus.addItem("待拋轉");
		cbProcessStatus.addItem("待處理");
		cbProcessStatus.addItem("處理中");
		cbProcessStatus.addItem("待出貨");
		cbProcessStatus.addItem("已發貨");
		cbProcessStatus.addItem("已送達");
		cbProcessStatus.addItem("已完成");
		cbProcessStatus.addItem("退回");
		cbProcessStatus.addItem("取消");
		
		// 客戶
		lblClient = new JLabel("客戶:");
		lblClient.setBounds(scaler.scaleX(525), scaler.scaleY(14), scaler.scaleX(36), scaler.scaleY(23));
		lblClient.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelOrderCondition.add(lblClient);

		tfClient = new JTextField();
		tfClient.setBounds(scaler.scaleX(582), scaler.scaleY(11), scaler.scaleX(156), scaler.scaleY(29));
		tfClient.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfClient.setColumns(10);
		panelOrderCondition.add(tfClient);

		// 下單日期
		lblOrderDate = new JLabel("下單日期:");
		lblOrderDate.setBounds(scaler.scaleX(10), scaler.scaleY(51), scaler.scaleX(100), scaler.scaleY(23));
		lblOrderDate.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelOrderCondition.add(lblOrderDate);
		// 下單日期開始選擇器
		UtilDateModel dmOrderStartDate = new UtilDateModel();
		Properties orderStartDateProperties = new Properties();
		JDatePanelImpl datePanelOrderStart = new JDatePanelImpl(dmOrderStartDate, orderStartDateProperties);
		dpOrderStartDate = new JDatePickerImpl(datePanelOrderStart, null);
		dpOrderStartDate.setBounds(scaler.scaleX(120), scaler.scaleY(51), scaler.scaleX(163), scaler.scaleY(23));
		dpOrderStartDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpOrderStartDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpOrderStartDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpOrderStartDate.setButtonFocusable(false);
		dpOrderStartDate.setTextEditable(false);	
		panelOrderCondition.add(dpOrderStartDate);
		
		// ~
		lblOrderTo = new JLabel("~");
		lblOrderTo.setBounds(scaler.scaleX(296), scaler.scaleY(51), scaler.scaleX(12), scaler.scaleY(23));
		lblOrderTo.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelOrderCondition.add(lblOrderTo);
		
		// 下單日期開始選擇器
		UtilDateModel dmOrderEndDate = new UtilDateModel();
		Properties orderEndDateProperties = new Properties();
		JDatePanelImpl datePanelOrderEnd = new JDatePanelImpl(dmOrderEndDate, orderEndDateProperties);
		dpOrderEndDate = new JDatePickerImpl(datePanelOrderEnd, null);
		dpOrderEndDate.setBounds(scaler.scaleX(328), scaler.scaleY(51), scaler.scaleX(163), scaler.scaleY(23));
		dpOrderEndDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpOrderEndDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpOrderEndDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpOrderEndDate.setButtonFocusable(false);
		dpOrderEndDate.setTextEditable(false);
		panelOrderCondition.add(dpOrderEndDate);
		// 虛線分隔線
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL) 
		{
		    @Override
		    protected void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D) g;
		        float[] dash = { 5.0f, 5.0f };
		        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		        g2.setColor(new Color(200, 200, 200)); // 改為淺灰色
		        super.paintComponent(g2);
		    }
		};
		separator.setBounds(scaler.scaleX(10), scaler.scaleY(141), scaler.scaleX(885), scaler.scaleY(2));
		separator.setBackground(Color.DARK_GRAY);
		panelSearchCondition.add(separator);


		// 交貨日期
		lblDeliveryDate = new JLabel("預計交貨日期:");
		lblDeliveryDate.setBounds(scaler.scaleX(10), scaler.scaleY(84), scaler.scaleX(100), scaler.scaleY(23));
		lblDeliveryDate.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelOrderCondition.add(lblDeliveryDate);
		

		// 交貨日期開始選擇器
		UtilDateModel dmDeliveryStartDate = new UtilDateModel();
		Properties deliveryStartDateProperties = new Properties();
		JDatePanelImpl datePanelDeliveryStart = new JDatePanelImpl(dmDeliveryStartDate, deliveryStartDateProperties);
		dpDeliveryStartDate = new JDatePickerImpl(datePanelDeliveryStart, null);
		dpDeliveryStartDate.setBounds(scaler.scaleX(120), scaler.scaleY(84), scaler.scaleX(163), scaler.scaleY(23));
		panelOrderCondition.add(dpDeliveryStartDate);
		dpDeliveryStartDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpDeliveryStartDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpDeliveryStartDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpDeliveryStartDate.setButtonFocusable(false);
		dpDeliveryStartDate.setTextEditable(false);

		// ~
		lblDeliveryTo = new JLabel("~");
		lblDeliveryTo.setBounds(scaler.scaleX(296), scaler.scaleY(84), scaler.scaleX(12), scaler.scaleY(23));
		panelOrderCondition.add(lblDeliveryTo);
		lblDeliveryTo.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		
		// 交貨日期結束選擇器
		UtilDateModel dmDeliveryEndDate = new UtilDateModel();
		Properties deliveryEndDateProperties = new Properties();
		JDatePanelImpl datePanelDeliveryEnd = new JDatePanelImpl(dmDeliveryEndDate, deliveryEndDateProperties);
		
		dpDeliveryEndDate =  new JDatePickerImpl(datePanelDeliveryEnd, null);
		dpDeliveryEndDate.setBounds(scaler.scaleX(328), scaler.scaleY(84), scaler.scaleX(163), scaler.scaleY(23));
		dpDeliveryEndDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpDeliveryEndDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpDeliveryEndDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpDeliveryEndDate.setButtonFocusable(false);
		dpDeliveryEndDate.setTextEditable(false);
		panelOrderCondition.add(dpDeliveryEndDate);
		// 選擇日期觸發器
		PropertyChangeListener dateChangeListener = new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		        if (evt.getPropertyName().equals("value")) {
		            Object source = evt.getSource();
		            if (source instanceof UtilDateModel) {
		                UtilDateModel model = (UtilDateModel) source;
		                Date selectedDate = (Date) model.getValue();
		                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		                String formattedDate = selectedDate != null ? dateFormat.format(selectedDate) : "";
		                
		                // 設定日期格式
		                if (evt.getSource() == dmOrderStartDate) {
		                    dpOrderStartDate.getJFormattedTextField().setText(formattedDate);
		                } 
		                else if (evt.getSource() == dmOrderEndDate) {
		                	dpOrderEndDate.getJFormattedTextField().setText(formattedDate);
		                }
		                else if (evt.getSource() == dmDeliveryStartDate) {
		                	dpDeliveryStartDate.getJFormattedTextField().setText(formattedDate);
		                } 
		                else if (evt.getSource() == dmDeliveryEndDate) {
		                	dpDeliveryEndDate.getJFormattedTextField().setText(formattedDate);
		                }
		            }
		        }
		    }
		};

		dmOrderStartDate.addPropertyChangeListener(dateChangeListener);
		dmOrderEndDate.addPropertyChangeListener(dateChangeListener);
		dmDeliveryStartDate.addPropertyChangeListener(dateChangeListener);
		dmDeliveryEndDate.addPropertyChangeListener(dateChangeListener);
		// 下半部 (品項條件)
		JPanel panelProductCondition = new JPanel();
		panelProductCondition.setBounds(scaler.scaleX(10), scaler.scaleY(141), scaler.scaleX(885), scaler.scaleY(41));
		panelSearchCondition.add(panelProductCondition);
		panelProductCondition.setLayout(null);

		// 產品編號
		lblProductID = new JLabel("包含產品:");
		lblProductID.setBounds(scaler.scaleX(10), scaler.scaleY(13), scaler.scaleX(68), scaler.scaleY(23));
		lblProductID.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		panelProductCondition.add(lblProductID);
		//
		tfProductID = new JTextField();
		tfProductID.setBounds(scaler.scaleX(120), scaler.scaleY(10), scaler.scaleX(156), scaler.scaleY(29));
		tfProductID.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
		tfProductID.setColumns(10);
		panelProductCondition.add(tfProductID);
		// 初始提示文字
        placeholderProductID = "請輸入產品編號";

        // 設定初始灰色提示
        tfProductID.setText(placeholderProductID);
        tfProductID.setForeground(Color.GRAY);

        // 增加 FocusListener 來監聽焦點變化
        tfProductID.addFocusListener(new FocusListener() 
        {
            @Override
            public void focusGained(FocusEvent e) {
                // 當獲得焦點時，如果顯示的是提示字詞，就清除並恢復正常字體顏色
                if (tfProductID.getText().equals(placeholderProductID)) {
                    tfProductID.setText("");
                    tfProductID.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 當失去焦點且輸入框為空時，顯示提示字詞並設定為灰色
                if (tfProductID.getText().isBlank()) {
                    tfProductID.setText(placeholderProductID);
                    tfProductID.setForeground(Color.GRAY);
                }
            }
        });
		// 增加分隔線並重新調整佈局
		OrderManu.add(panelSearchCondition);

		//初始表格資料
        tableModel = new DefaultTableModel();
        try 
        {
            // 修改 SQL 查詢，連接 `order` 和 `client` 表來取得客戶名稱
            String sql = "SELECT "
	                    + "    o.id AS '訂單編號', "
	                    + "    o.status AS '狀態', "
	                    + "    c.name AS '客戶', "
	                    + "    o.amount_payable AS '應付金額', "
	                    + "    o.ETA AS '預計交貨日期' "
	                    + "FROM `order` AS o "
	                    + "JOIN `client` AS c ON o.client_id = c.id "
	                    + "GROUP BY o.id "
	                    + "ORDER BY o.id DESC;";
            
            PreparedStatement stmt = Overview.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            // 加入欄位
            tableModel.addColumn("訂單編號");
            tableModel.addColumn("狀態");
            tableModel.addColumn("客戶");
            tableModel.addColumn("應付金額");
            tableModel.addColumn("預計交貨日期");

            // 逐筆取得資料
            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("訂單編號"),
                    rs.getString("狀態"),
                    rs.getString("客戶") ,
                    rs.getString("應付金額"),
                    rs.getString("預計交貨日期")
                };
                tableModel.addRow(rowData);
            }
            
            // 關閉資源
            rs.close();
            stmt.close();
        } catch (SQLException e) {
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
                
                // 設定文字樣式
                if (column == 0) 
                { 
                	// 訂單編號是藍色粗體
                    cellComponent.setForeground(new Color(70, 130, 180)); // 設定文字顏色為藍色
                    cellComponent.setFont(new Font("微軟正黑體", Font.BOLD, 14)); // 設定文字為粗體
                } 
                else if (column == 1) 
                {
                	// 狀態是黑色粗體
                    cellComponent.setForeground(Color.BLACK); // 設定文字顏色為藍色
                    cellComponent.setFont(new Font("微軟正黑體", Font.BOLD, 15)); // 設定文字為粗體
                }
                else if (column == 4) 
                {
                    String status = table.getValueAt(row, 1) != null ? table.getValueAt(row, 1).toString() : ""; // 檢查狀態
                    String etaString = table.getValueAt(row, 4) != null ? table.getValueAt(row, 4).toString() : ""; // 檢查交期

                    if (!etaString.equals("") && (!status.equals("已完成") || !status.equals("取消"))) 
                    {
                    	// 如果預計交貨日期距今天5天到期，而且狀態不是已完成，將交期設為紅色。
                        try {
                            LocalDate eta = LocalDate.parse(etaString); // 轉換交期為 LocalDate
                            LocalDate today = LocalDate.now();
                            long daysRemaining = ChronoUnit.DAYS.between(today, eta); // 計算剩餘天數

                            if (daysRemaining < 5) {
                                cellComponent.setForeground(Color.RED); // 五天內到期，顯示為紅色
                            } 
                            else {
                                cellComponent.setForeground(Color.GRAY); // 否則顯示為灰色
                            }
                        } 
                        catch (DateTimeParseException e) {
                            // 處理日期格式錯誤
                            cellComponent.setForeground(Color.GRAY); // 格式錯誤時，設為灰色
                        }
                    } 
                    else {
                        cellComponent.setForeground(Color.GRAY); // 狀態為已完成或無交期時，設為灰色
                    }
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
                int column = table.columnAtPoint(e.getPoint()); // 使用 mouse event 的位置來獲取列索引
                // 是否選取資料列
                if (row != -1 ) 
                { 
                	String orderId = table.getValueAt(row, 0).toString(); // 獲取訂單編號
                    new OrderOperation(orderId);


                }
            }
        });

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(scaler.scaleX(80), scaler.scaleY(266), scaler.scaleX(1132), scaler.scaleY(310));
        OrderManu.add(scrollPane);

        //工具列
        toolBar = new JToolBar("工具列");
        toolBar.setBounds(scaler.scaleX(80), scaler.scaleY(236), scaler.scaleX(172), scaler.scaleY(30));
        OrderManu.add(toolBar);
        //圖示
        ImageIcon iconRefresh = new ImageIcon("Img/eraser.png");
        Image imgRefresh = iconRefresh.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon Icon1 = new ImageIcon(imgRefresh);
        ImageIcon iconInsert = new ImageIcon("Img/plus.png");
        Image imgInsert = iconInsert.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon Icon2 = new ImageIcon(imgInsert);
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
        btnClear.setIcon(Icon1);
        btnClear.setFocusPainted(false);
        btnClear.setContentAreaFilled(false); // 取消預設背景
        btnClear.setOpaque(false); // 使背景透明
        btnClear.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		// 清空文本框
                tfOrderID.setText("");
                tfClient.setText("");
                tfProductID.setText(placeholderProductID);
                
                // 重置日期選擇器
                dmOrderStartDate.setValue(null);
                dmOrderEndDate.setValue(null);
                dmDeliveryStartDate.setValue(null);
                dmDeliveryEndDate.setValue(null);

                // 重置處理狀態下拉選單
                cbProcessStatus.setSelectedIndex(0);  // 選擇第一個空白項目
        	}
        });  
        
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
        btnInsert.setIcon(Icon2);
        btnInsert.setFocusPainted(false);
        btnInsert.setContentAreaFilled(false); // 取消預設背景
        btnInsert.setOpaque(false); // 使背景透明
        btnInsert.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	new OrderOperation();
            }
        });

        // 排序選單
        cbSorting = new JComboBox<>(new String[]{"最新", "最舊", "金額最高", "金額最低"});     
        cbSorting.setSelectedItem("最新"); // 設置預設為 "最新"
        cbSorting.setFont(new Font("微軟正黑體", Font.BOLD, 12));
        cbSorting.setBackground(Color.WHITE);
        cbSorting.setForeground(Color.GRAY);
        cbSorting.setFocusable(false); // 禁用焦點邊框
        // 自訂 ComboBox UI
        cbSorting.setUI(new BasicComboBoxUI() 
        {
            @Override
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
        
        //
        toolBar.add(btnInsert);
        toolBar.add(btnClear);
        toolBar.add(cbSorting);
        
        // 添加監聽器到各個元件
        addTextFieldListener(tfOrderID);
        addTextFieldListener(tfClient);
        addTextFieldListener(tfProductID);

        addComboBoxListener(cbProcessStatus);
        addComboBoxListener(cbSorting);

        addDatePickerListener(dpOrderStartDate);
        addDatePickerListener(dpOrderEndDate);
        addDatePickerListener(dpDeliveryStartDate);
        addDatePickerListener(dpDeliveryEndDate);
        
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

    // 添加 JDatePicker 的 PropertyChangeListener
    private void addDatePickerListener(JDatePickerImpl datePicker) {
        datePicker.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateTable();
            }
        });
    }
	// 更新表格資料
	private void updateTable()
	{
		String orderID = tfOrderID.getText().trim();  // 訂單編號篩選
		String status = (String) cbProcessStatus.getSelectedItem();  // 狀態篩選
	    String client = tfClient.getText().trim();    // 客戶篩選
	    Date selectedOrderStartDate = (Date) dpOrderStartDate.getModel().getValue();  // 下單開始日期
	    Date selectedOrderEndDate = (Date) dpOrderEndDate.getModel().getValue();  // 下單結束日期
	    
	    Date selectedDeliveryStartDate = (Date) dpDeliveryStartDate.getModel().getValue(); // 開始日期
	    Date selectedDeliveryEndDate = (Date) dpDeliveryEndDate.getModel().getValue();     // 結束日期
	    String productID = tfProductID.getText().trim().equals(placeholderProductID) ? "" : tfProductID.getText().trim(); // 產品篩選
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String orderStartDate = selectedOrderStartDate != null ? sdf.format(selectedOrderStartDate) : ""; // 如果日期是 null，則設置為空字串
	    String orderEndDate = selectedOrderEndDate != null ? sdf.format(selectedOrderEndDate) : ""; // 如果日期是 null，則設置為空字串
	    String deliveryStartDate = selectedDeliveryStartDate != null ? sdf.format(selectedDeliveryStartDate) : ""; // 如果日期是 null，則設置為空字串
	    String deliveryEndDate = selectedDeliveryEndDate != null ? sdf.format(selectedDeliveryEndDate) : ""; // 如果日期是 null，則設置為空字串
		
	    String sorting = (String) cbSorting.getSelectedItem();
	    String sortRule="";
	    switch(sorting) 
	    {
	    	case "最新":
	    		sortRule = "ORDER BY o.id DESC;";
	    		break;
	    	case "最舊":
	    		sortRule = "ORDER BY o.id ASC;";
	    		break;
	    	case "金額最高":
	    		sortRule = "ORDER BY o.amount_payable DESC;";
	    		break;
	    	case "金額最低":
	    		sortRule = "ORDER BY o.amount_payable ASC;";
	    		break; 		
	    }
	    try 
		{		 
			 //SQL 查詢語句
			 String sql ="SELECT "
	                    + "    o.id AS '訂單編號', "
	                    + "    o.status AS '狀態', "
	                    + "    c.name AS '客戶', "
	                    + "    o.amount_payable AS '應付金額', "
	                    + "    o.ETA AS '預計交貨日期' "
				 		+"FROM `order` AS o "
				 		+"JOIN `order_product` AS op ON o.id = op.order_id  "
				 		+"JOIN `client` AS c ON o.client_id = c.id "
				        +"WHERE 1=1 "
				 		+"AND (CASE WHEN ? != '' THEN o.id LIKE ? ELSE TRUE END)"
				        +"AND (CASE WHEN ? != '' THEN o.status = ? ELSE TRUE END)"
				 		+"AND (CASE WHEN ? != '' THEN c.name LIKE ? ELSE TRUE END)"
				 		+"AND (CASE " 
				 		+"        WHEN ? != '' AND ? != '' THEN o.order_date BETWEEN ? AND ? " 
				 		+"        WHEN ? != '' THEN o.order_date >= ? " 
				 		+"        WHEN ? != '' THEN o.order_date <= ? " 
				 		+"        ELSE TRUE "    // 如果下單日期開始和結束都未填入，則不進行篩選。
				 		+"     END)" 
				 		+"AND (CASE " 
				 		+"        WHEN ? != '' AND ? != '' THEN o.ETA BETWEEN ? AND ? " 
				 		+"        WHEN ? != '' THEN o.ETA >= ? " 
				 		+"        WHEN ? != '' THEN o.ETA <= ? " 
				 		+"        ELSE TRUE "    // 如果預計交貨日期開始和結束都未填入，則不進行篩選。
				 		+"     END)" 
				 		+"AND (CASE WHEN ? != '' THEN op.product_id LIKE ? ELSE TRUE END)"
	                    +"GROUP BY o.id "
				 		+ sortRule;
			 
		     PreparedStatement stmt = Overview.conn.prepareStatement(sql);
		     stmt.setString(1, orderID);
		     stmt.setString(2, "%" + orderID + "%"); // 設置帶有 % 的參數
		     stmt.setString(3, status);
		     stmt.setString(4, status);
		     stmt.setString(5, client);
		     stmt.setString(6, "%" + client + "%"); // 設置帶有 % 的參數
		     stmt.setString(7, orderStartDate);
		     stmt.setString(8, orderEndDate);
		     stmt.setString(9, orderStartDate);
		     stmt.setString(10,orderEndDate);
		     stmt.setString(11,orderStartDate);
		     stmt.setString(12,orderStartDate);
		     stmt.setString(13,orderEndDate);
		     stmt.setString(14,orderEndDate);
		     stmt.setString(15,deliveryStartDate);
		     stmt.setString(16,deliveryEndDate);
		     stmt.setString(17,deliveryStartDate);
		     stmt.setString(18,deliveryEndDate);
		     stmt.setString(19,deliveryStartDate);
		     stmt.setString(20,deliveryStartDate);
		     stmt.setString(21,deliveryEndDate);
		     stmt.setString(22,deliveryEndDate);
		     stmt.setString(23, productID);
		     stmt.setString(24, "%" + productID + "%"); // 設置帶有 % 的參數
			 ResultSet rs = stmt.executeQuery();
			 
			 tableModel.setRowCount(0);
			 // 逐筆取得資料
			 while (rs.next()) 
			 {
                Object[] rowData = 
                {
            		rs.getString("訂單編號"),
                    rs.getString("狀態"),
                    rs.getString("客戶") ,
                    rs.getString("應付金額"),
                    rs.getString("預計交貨日期")
                };
                tableModel.addRow(rowData);
	         }
		     table.setModel(tableModel);
			 //關閉資源
			 stmt.close();
	         rs.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	// 回傳主框架(之後要添加於選項卡)
	public JPanel getPanel() 
	{
        // 將絕對定位的面板放入一個中心面板
        JPanel centeredPanel = new JPanel();
        centeredPanel.setLayout(new BorderLayout()); // 使用 BorderLayout
        centeredPanel.add(OrderManu, BorderLayout.CENTER); // 將 OrderManu 添加到中心位置
        return centeredPanel;
    }
}



