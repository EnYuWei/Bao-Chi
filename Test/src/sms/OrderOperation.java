package sms;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import javax.swing.text.NumberFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.awt.GridLayout;
import javax.swing.border.EtchedBorder;
public class OrderOperation 
{
	private JFrame frame;
	private JLabel lblOrderID;
	private JTextField tfOrderID;
	private JLabel lblProcessStatus;
	private JComboBox<String> cbProcessStatus;
	private JLabel lblOrderDate;
	private JTextField tfOrderDate;
	private JLabel lblSales;
	private JTextField tfSales;
	private JLabel lblAmountPayable;
	private JTextField tfAmountPayable;
	private JLabel lblETA;
	private JDatePickerImpl dpETA ;
	private JLabel lblATA;
	private JDatePickerImpl dpATA ;
	private JLabel lblDeliveryAddress;
	private JTextArea taDeliveryAddress;
	
	private JLabel lblMiddlemen;
	private JTextField tfMiddlemen;
	private JLabel lblClientID;
	private JComboBox<String> cbClientID;
	private JLabel lblClient;
	private JTextField tfClient;
	private JLabel lblUnifiedNumber;
	private JTextField tfUnifiedNumber;
	private JLabel lblContactPerson;
	private JTextField tfContactPerson;
	private JLabel lblContactPhone;
	private JTextField tfContactPhone;
	private JLabel lblEmail;
	private JTextField tfEmail;
	private JLabel lblAddress;
	private JTextArea taAddress;
	
	private JTable table;
	private JTableHeader tableHeaderT;
	private JScrollPane spTable;
	private JScrollPane spFrame;
	private JPanel buttonPanel;

	// 沒傳參數新增訂單
	public OrderOperation() 
	{
	    initOrderFrame();
	    frame.setTitle("建立訂單");
	    // 預設訂單編號
	    String defaultOrderID = null;
        try 
        {
        	//訂單編碼
			Calendar today = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            
            defaultOrderID = sdf.format(today.getTime());
            
			String sql = "SELECT  COUNT(o.id) AS '當日訂單數'  FROM `order` AS o WHERE order_date = CURDATE();";
			PreparedStatement stmt = Overview.conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			// 根據下單日期取得當日訂單數，並計算最新的訂單編號。例如: 2024100201 西元年月日+兩位流水號
			if (rs.next()) 
			{
                int orderCount = rs.getInt(1);  
                orderCount++;   
                String temp = Integer.toString(orderCount);
                if(orderCount>=10)
                {
                	defaultOrderID = defaultOrderID + temp;
                }
                else
                {
                	defaultOrderID = defaultOrderID+"0"+temp;	
                }
                
            } 
			else // 如果當天尚未建立任何訂單
			{
				defaultOrderID = defaultOrderID+"01";
            }
			
		} 
        catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        tfOrderID.setText(defaultOrderID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");          
        tfOrderDate.setText(sdf.format(new Date()));
        
        // 建議字詞
		SuggestionTool sugSales = new SuggestionTool("`order`","sales", Overview.url, Overview.username, Overview.password);
		SuggestionTool sugDeliveryAddress = new SuggestionTool("`order`","delivery_address", Overview.url, Overview.username, Overview.password);
		sugSales.addTextComponent(tfSales);
		sugDeliveryAddress.addTextComponent(taDeliveryAddress);
		//
		String[] columnNames = {"項次", "產品編號", "色號", "紗線規格", "紗線批號", "重量", "粒數", "小計", "對色光源", "後處理方式", "備註"};
	    Object[][] data = new Object[30][columnNames.length];
	    for (int i = 0; i < 30; i++) {
	          data[i][0] = i + 1; // 最多30個產品
	    }
	    // 創建表格模型
	    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames)
	    {		
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        // 只允許第5, 6, 7欄 (重量, 粒數, 小計) 可以編輯
		        return column == 1 || column == 5 || column == 6 || column == 7 || column == 8 || column == 9 || column == 10;
		    }
		};
		
	    table.setModel(tableModel);
	    table.setEnabled(true);
	
	    // 創建一個用於置中的渲染器
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // 將此渲染器應用到表格的每一列
        for (int i = 0; i < table.getColumnCount(); i++) 
        {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // 品項明細--產品編號下拉選單
        DefaultComboBoxModel<String> cbmodelProductID = new DefaultComboBoxModel<>();
        JComboBox<String> cbProductID = new JComboBox<>();
        List<String> allProductID = new ArrayList<>();  // 用於儲存所有的產品編號
        try 
	    {	        
	        // SQL 查詢語句
	        String sql = "SELECT `id` FROM product";
	        PreparedStatement stmt = Overview.conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        allProductID.add("");
	        // 將所有查詢結果加入到 allColorID 列表中
	        while (rs.next()) 
	        {
	        	allProductID.add(rs.getString("id"));
	        }
	        
	        // 關閉資源
	        rs.close();
	        stmt.close();
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }
	    // 將查詢到的色號加入到 JComboBox 中
	    for (String product : allProductID) 
	    {
	    	cbmodelProductID.addElement(product);
	    }
	    cbProductID.setModel(cbmodelProductID);
	    cbProductID.setBackground(Color.WHITE);
	    cbProductID.setFocusable(false);
	    cbProductID.setMaximumRowCount(8);  // 設置最多顯示 8 行

        // 設置產品編號下拉選單作為列編輯器
        TableColumn productIDColumn = table.getColumnModel().getColumn(1); 
        productIDColumn.setCellEditor(new DefaultCellEditor(cbProductID));
	    // 根據所選項目 自動填入相關欄位
        cbProductID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProductID = (String) cbProductID.getSelectedItem(); // 取得選中的產品編號
                int selectedRow = table.getSelectedRow(); // 獲取當前選中的表格行

                // 檢查是否選中了有效的行
                if (selectedRow >= 0) {
                    if (selectedProductID != null && !selectedProductID.isBlank()) {
                        try 
                        {
                            String sql = "SELECT color_id, yarn_specification, yarn_lot_num FROM product WHERE id = ?";
                            PreparedStatement stmt = Overview.conn.prepareStatement(sql);
                            stmt.setString(1, selectedProductID);

                            ResultSet rs = stmt.executeQuery();
                            if (rs.next()) {
                                // 將查詢結果填入表格對應的欄位
                                table.setValueAt(rs.getString("color_id"), selectedRow, 2);  // 色號
                                table.setValueAt(rs.getString("yarn_specification"), selectedRow, 3);  // 紗線規格
                                table.setValueAt(rs.getString("yarn_lot_num"), selectedRow, 4);  // 紗線批號
                            }

                            rs.close();
                            stmt.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        // 清空對應的欄位
                        table.setValueAt("", selectedRow, 2);  // 色號
                        table.setValueAt("", selectedRow, 3);  // 紗線規格
                        table.setValueAt("", selectedRow, 4);  // 紗線批號
                    }
                }
            }
        });
        
        // 品項明細--色號欄位加寬
        TableColumn colorIDColumn = table.getColumnModel().getColumn(2); 
        colorIDColumn.setPreferredWidth(100); 
        // 品項明細--紗線規格欄位加寬
        TableColumn yarnSpecificationColumn = table.getColumnModel().getColumn(3); 
        yarnSpecificationColumn.setPreferredWidth(150); 
        // 創建格式化的輸入欄位
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(2); // 最小小數位數
        format.setMaximumFractionDigits(2); // 最大小數位數
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setAllowsInvalid(false); // 不允許無效輸入
        numberFormatter.setCommitsOnValidEdit(true); // 有效編輯後提交

        // 品項明細--重量欄位限制輸入數字(小數後兩位)
        TableColumn weightColumn = table.getColumnModel().getColumn(5);
        weightColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        // 品項明細--粒數欄位限制輸入數字(小數後兩位)
        TableColumn grainsColumn = table.getColumnModel().getColumn(6);
        grainsColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        // 品項明細--小計欄位限制輸入數字(小數後兩位)
        TableColumn subtotalColumn = table.getColumnModel().getColumn(7);
        subtotalColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        subtotalColumn.setPreferredWidth(100); 
        // 品項明細--備註欄位加寬
        TableColumn remarkColumn = table.getColumnModel().getColumn(10); 
        remarkColumn.setPreferredWidth(150); 
	    
		// 儲存按鈕
		JButton btnSave = new JButton("儲存");
	    btnSave.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnSave.setBackground(Color.WHITE);
	    btnSave.setForeground(Color.BLACK);
	    btnSave.setFocusPainted(false);    
	    btnSave.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) 
	        {
	        	JFormattedTextField tfETA = ((JFormattedTextField) dpETA.getJFormattedTextField());
	        	String inputDate = tfETA.getText();
	            // 檢查必填欄位(客戶編號和銷售人員)
	            if (cbClientID.getSelectedItem() == null || 
	                cbClientID.getSelectedItem().toString().isBlank() || 
	                tfSales.getText().isBlank() ||
	                inputDate.isBlank()) {
	                OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "客戶編號、銷售人員、預計交貨日期為必填欄位!", "確認");
	                return;
	            }

	            // 取得表格模型
	            DefaultTableModel model = (DefaultTableModel) table.getModel();
	            int rowCount = model.getRowCount(); // 取得行數
	            int columnCount = model.getColumnCount(); // 取得列數

	            // 儲存過濾後的資料 (String 陣列)
	            List<String[]> filteredDataList = new ArrayList<>();
	            Set<String> productCodes = new HashSet<>(); // 用來儲存產品編號，避免重複
	            // 取得每行的數據
	            for (int row = 0; row < rowCount; row++) 
	            {
	            	// 如果有選產品編號
	                if (!isCellEmpty(model, row, 1)) 
	                { 
	                    String productCode = model.getValueAt(row, 1).toString(); // 取得產品編號
	                    if (productCodes.contains(productCode)) {
	                        // 如果產品編號重複，顯示錯誤訊息並返回
	                        OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "產品編號 '" + productCode + "' 重複!", "錯誤");
	                        return;
	                    } else {
	                        // 如果產品編號不重複，將其加入到集合中
	                        productCodes.add(productCode);
	                    }

	                    // 儲存這一列的數據
	                    String[] rowData = new String[columnCount];
	                    for (int col = 0; col < columnCount; col++) {
	                        Object cellValue = model.getValueAt(row, col);
	                        rowData[col] = (cellValue != null) ? cellValue.toString() : ""; // 空值替換為 ""
	                    }
	                    filteredDataList.add(rowData);
	                } 
	                // 如果產品編號以外的欄位有輸入
	                else if (isRowIncomplete(model, row)) { 
	                    String errorRow = Integer.toString(row + 1);
	                    OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "項次" + errorRow + "資料填寫不完整!", "OK");
	                    return;
	                }	   
	                if(model.getValueAt(row, 5) == null && model.getValueAt(row, 5) == "0.00"  && model.getValueAt(row, 6) == null && model.getValueAt(row, 6) == "0.00" )
	                {
	                	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "重量或粒數須擇一填寫!", "OK");
	                    return;
	                }
	            }

	            if (filteredDataList.isEmpty()) {
	                OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "請輸入產品編號", "OK");
	                return;
	            }
	            // 打印過濾後的資料
	            filteredDataList.forEach(row -> System.out.println(Arrays.toString(row)));
	            
	            // 新增至資料庫
	            try 
	            {
	            	Overview.conn.setAutoCommit(false);  // 關閉自動提交，啟用交易
	
	                try 
	                {
	                    // 取得欄位輸入
	                    String orderID = tfOrderID.getText().trim();
	                    String status = (String) cbProcessStatus.getSelectedItem();
	                    String sales = tfSales.getText().trim();
	                    BigDecimal amountPayable = !tfAmountPayable.getText().trim().isBlank() ?  new BigDecimal(tfAmountPayable.getText().trim()) : null;
	                    String ETA = dpETA.getJFormattedTextField().getText().trim(); 
	                    String ATA = !dpATA.getJFormattedTextField().getText().trim().isBlank() ? dpATA.getJFormattedTextField().getText().trim() : null;
	                    String deliveryAddress = taDeliveryAddress.getText().trim();
	                    String middlemen = tfMiddlemen.getText().trim();
	                    String clientID = (String) cbClientID.getSelectedItem();
	
	                    // SQL 插入語句
	                    String sqlOrder = "INSERT INTO `order`  (id, status, sales, amount_payable, ETA, ATA, delivery_address, middlemen, client_id)"
	                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	                    // 準備 SQL 語句
	                    PreparedStatement stmtOrder = Overview.conn.prepareStatement(sqlOrder);
	                    stmtOrder.setString(1, orderID);
	                    stmtOrder.setString(2, status);
	                    stmtOrder.setString(3, sales);
	
	                    // 金額如果為 null 則插入空值
	                    if (amountPayable != null) {
	                        stmtOrder.setBigDecimal(4, amountPayable);
	                    } else {
	                        stmtOrder.setNull(4, java.sql.Types.DECIMAL);
	                    }
	
	                    // 預計交貨日期
	                    stmtOrder.setString(5, ETA);

	                    // 實際交貨日期
	                    if (ATA == null) {
	                        stmtOrder.setNull(6, java.sql.Types.DATE);
	                    } else {
	                        stmtOrder.setString(6, ATA);
	                    }
	
	                    stmtOrder.setString(7, deliveryAddress);
	                    stmtOrder.setString(8, middlemen);
	                    stmtOrder.setString(9, clientID);
	
	                    // 執行訂單新增
	                    stmtOrder.executeUpdate();
	
	                    // 新增訂單產品資料
	                    String sqlDetail = "INSERT INTO `order_product` (`order_id`, product_id, weight, grains, subtotal, light_source, post_process, remark) "
	                                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
	                    PreparedStatement stmtOrderProduct = Overview.conn.prepareStatement(sqlDetail);
	
	                    for (String[] rowData : filteredDataList) {
	                        stmtOrderProduct.setString(1, orderID);  // 設定訂單編號
	                        for (int col = 0; col < rowData.length; col++) {
	                            String data = rowData[col];
	                            switch (col) { // {"項次", "產品編號", "色號", "紗線規格", "紗線批號", "重量", "粒數", "小計", "對色光源", "後處理方式", "備註"};
	                                case 1: // 產品編號
	                                    stmtOrderProduct.setInt(2, Integer.parseInt(data));
	                                    break;
	                                case 5: // 重量
	                                	if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(3, new BigDecimal(data));
	                                	}
	                                	else {
	                                		stmtOrderProduct.setBigDecimal(3, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 6: // 粒數
	                                    if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(4, new BigDecimal(data));
	                                	}
	                                    else {
	                                		stmtOrderProduct.setBigDecimal(4, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 7: // 小計
	                                    if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(5, new BigDecimal(data));
	                                	}
	                                    else {
	                                		stmtOrderProduct.setBigDecimal(5, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 8: // 對色光源
	                                    stmtOrderProduct.setString(6, data);
	                                    break;
	                                case 9: // 後處理方式
	                                    stmtOrderProduct.setString(7, data);
	                                    break;
	                                case 10: // 備註
	                                    stmtOrderProduct.setString(8, data);
	                                    break;
	                            }
	                        }
	                        stmtOrderProduct.addBatch();  // 將每一筆資料加入批次
	                    }
	
	                    // 批次插入訂單產品
	                    stmtOrderProduct.executeBatch();
	
	                    // 如果所有操作成功，提交交易
	                    Overview.conn.commit();
	                    OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "訂單資料新增成功！", "確認");
	                    
	                    frame.dispose();
	    	        	
	                } 
	                catch (SQLException e1) {
	                    // 捕獲 SQL 錯誤，進行回滾操作
	                	Overview.conn.rollback();
	                    e1.printStackTrace();
	                    OptionPaneTool.showMessageDialog(JOptionPane.ERROR_MESSAGE, "資料庫操作錯誤: " + e1.getMessage(), "確認");
	                } 
	                finally {
	                	Overview.conn.setAutoCommit(true);  // 恢復自動提交模式
	                }
	
	            } 
	            catch (SQLException e1) {
	                e1.printStackTrace();
	                OptionPaneTool.showMessageDialog(JOptionPane.ERROR_MESSAGE, "連線失敗: " + e1.getMessage(), "確認");
	            }


	        }

	        private boolean isCellEmpty(DefaultTableModel model, int row, int col) {
	            Object value = model.getValueAt(row, col);
	            return value == null || value.toString().trim().isEmpty();
	        }

	        private boolean isRowIncomplete(DefaultTableModel model, int row) {
	            for (int col = 2; col <= 10; col++) { // 假設從列2到列10需要檢查
	                Object cellValue = model.getValueAt(row, col);
	                // 檢查 cellValue 是否不為 null 並且不為空
	                if (cellValue != null && !cellValue.toString().trim().isEmpty()) {
	                    return true;
	                }
	            }
	            return false;
	        }
	    });

	    buttonPanel.add(btnSave);
		//
        frame.setVisible(true);
	}
	
	
	// 有傳參數  修改訂單
	public OrderOperation(String orderId) 
	{
	    initOrderFrame();
	    frame.setTitle("編輯訂單");
	    String currentStatus;

		cbProcessStatus.setEnabled(false);
	    //
		cbProcessStatus.addItem("待拋轉");
		cbProcessStatus.addItem("待處理");
		cbProcessStatus.addItem("處理中");
		cbProcessStatus.addItem("待出貨");
		cbProcessStatus.addItem("已發貨");
		cbProcessStatus.addItem("已送達");
		cbProcessStatus.addItem("已完成");
		cbProcessStatus.addItem("退回");
		cbProcessStatus.addItem("取消");
	    try 
	    {
	    	String sql = "SELECT "
	    			    +"	o.`id`, o.`status`, o.`sales`, o.`amount_payable`, o.`order_date`, o.`ETA`, o.`ATA`, o.`delivery_address`, o.`middlemen`, o.`client_id` "
	    			    +"FROM `order` AS o "
	    			    +"WHERE  o.id = ?; ";
	        PreparedStatement stmt = Overview.conn.prepareStatement(sql);
	        stmt.setString(1, orderId);
	        ResultSet rs = stmt.executeQuery(); 
	        if(rs.next())
	        {	            
                tfOrderID.setText(rs.getString("o.id"));
                cbProcessStatus.setSelectedItem(rs.getString("o.status"));
                tfSales.setText(rs.getString("o.sales"));
                tfAmountPayable.setText(rs.getString("o.amount_payable"));
                tfOrderDate.setText(rs.getString("o.order_date"));
                dpETA.getJFormattedTextField().setText(rs.getString("o.ETA"));
                dpATA.getJFormattedTextField().setText(rs.getString("o.ATA"));
                taDeliveryAddress.setText(rs.getString("o.delivery_address"));
                cbClientID.setSelectedItem(rs.getString("o.client_id"));
	        }
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    currentStatus = (String) cbProcessStatus.getSelectedItem();
	    tfMiddlemen.setEditable(false);
	    tfSales.setEditable(false);
	    cbClientID.setEnabled(false);
//	    String[] columnNames = {"項次", "產品編號", "色號", "紗線規格", "紗線批號", "重量", "粒數", "小計", "對色光源", "後處理方式", "備註"};
	    // 表格數據處理
	    List<Object[]> dataList = new ArrayList<>();
	    try 
	    {
	    	String sql = "SELECT"
	    			    +"	op.product_id AS '產品編號', p.color_id AS '色號', p.yarn_specification AS '紗線規格', p.yarn_lot_num AS '紗線批號', weight AS '重量', grains AS '粒數', subtotal AS '小計', light_source AS '對色光源', post_process AS '後處理方式', remark AS '備註' "
	    				+"FROM `order_product` AS op "
	    				+"JOIN product AS p ON  op.product_id = p.id "
	    				+"WHERE op.`order_id` = ? "
	    				+"GROUP BY op.product_id  ;";
	        PreparedStatement stmt = Overview.conn.prepareStatement(sql);
	        stmt.setString(1, orderId);
	        ResultSet rs = stmt.executeQuery(); 
	        int itemNum = 1;
            while (rs.next()) 
            {
                Object[] rowData = 
                {
                	itemNum,
                    rs.getString("產品編號"),
                    rs.getString("色號"),
                    rs.getString("紗線規格"),
                    rs.getString("紗線批號"),
                    rs.getBigDecimal("重量").toString(),
                    rs.getBigDecimal("粒數").toString(),
                    rs.getBigDecimal("小計").toString(),
                    rs.getString("對色光源"),
                    rs.getString("後處理方式"),
                    rs.getString("備註")
                };
                
                dataList.add(rowData);
                itemNum++;
	        }
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    DefaultTableModel model = new DefaultTableModel(dataList.toArray(new Object[0][]), new String[]{"項次", "產品編號", "色號", "紗線規格", "紗線批號", "重量", "粒數", "小計", "對色光源", "後處理方式", "備註"})
	    {
	    	@Override
	        public boolean isCellEditable(int row, int column) {
	            // 只允許第5, 6, 7欄 (重量, 粒數, 小計) 可以編輯
	    		if (currentStatus == "草稿")
	    		{
	    			return column == 5 || column == 6 || column == 7 || column == 8 || column == 9 || column == 10;
	    		}
				return false;
	            
	        }
	    };
	
	    table.setModel(model);
	    // 品項明細--色號欄位加寬
        TableColumn colorIDColumn = table.getColumnModel().getColumn(2); 
        colorIDColumn.setPreferredWidth(100); 
        // 品項明細--紗線規格欄位加寬
        TableColumn yarnSpecificationColumn = table.getColumnModel().getColumn(3); 
        yarnSpecificationColumn.setPreferredWidth(150); 
        // 創建格式化的輸入欄位
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(2); // 最小小數位數
        format.setMaximumFractionDigits(2); // 最大小數位數
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Double.class);
        numberFormatter.setAllowsInvalid(false); // 不允許無效輸入
        numberFormatter.setCommitsOnValidEdit(true); // 有效編輯後提交

        // 品項明細--重量欄位限制輸入數字(小數後兩位)
        TableColumn weightColumn = table.getColumnModel().getColumn(5);
        weightColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        // 品項明細--粒數欄位限制輸入數字(小數後兩位)
        TableColumn grainsColumn = table.getColumnModel().getColumn(6);
        grainsColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        // 品項明細--小計欄位限制輸入數字(小數後兩位)
        TableColumn subtotalColumn = table.getColumnModel().getColumn(7);
        subtotalColumn.setCellEditor(new DefaultCellEditor(new JFormattedTextField(numberFormatter)));
        subtotalColumn.setPreferredWidth(100); 
        // 品項明細--備註欄位加寬
        TableColumn remarkColumn = table.getColumnModel().getColumn(10); 
        remarkColumn.setPreferredWidth(150); 
        // 創建一個用於置中的渲染器
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // 將此渲染器應用到表格的每一列
        for (int i = 0; i < table.getColumnCount(); i++) 
        {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // 確保表格的 UI 更新
        table.revalidate();
        table.repaint();
        
        // 儲存按鈕
    	JButton btnSave = new JButton("儲存");
	    btnSave.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnSave.setBackground(Color.WHITE);
	    btnSave.setForeground(Color.LIGHT_GRAY);
	    btnSave.setFocusPainted(false);    
	    btnSave.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) 
	        {
	        	JFormattedTextField tfETA = ((JFormattedTextField) dpETA.getJFormattedTextField());
	        	String inputDate = tfETA.getText();
	        	// 檢查必填欄位(預計交貨日期)
	        	if (inputDate == null || inputDate.isBlank()) {
	        	    OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "預計交貨日期為必填欄位!", "確認");
	        	    return;
	        	}

	            // 取得表格模型
	            DefaultTableModel model = (DefaultTableModel) table.getModel();
	            int rowCount = model.getRowCount(); // 取得行數
	            int columnCount = model.getColumnCount(); // 取得列數

	            // 儲存過濾後的資料 (String 陣列)
	            List<String[]> filteredDataList = new ArrayList<>();

	            // 取得每行的數據
	            for (int row = 0; row < rowCount; row++) 
	            {
	                    String[] rowData = new String[columnCount];
	                    for (int col = 0; col < columnCount; col++) 
	                    {
	                        Object cellValue = model.getValueAt(row, col);
	                        rowData[col] = (cellValue != null) ? cellValue.toString() : ""; // 空值替換為 ""
	                    }
	                    filteredDataList.add(rowData);
	            }

	            // 打印過濾後的資料
	            filteredDataList.forEach(row -> System.out.println(Arrays.toString(row)));
	            
	            // 新增至資料庫
	            try 
	            {
	            	Overview.conn.setAutoCommit(false);  // 關閉自動提交，啟用交易
	                // 修改訂單資料
	                try 
	                {
	                	Overview.conn.setAutoCommit(false);  // 關閉自動提交，啟用交易
	                    // 取得欄位輸入
	                    String orderID = tfOrderID.getText().trim();
	                    String status = (String) cbProcessStatus.getSelectedItem();
	                    BigDecimal amountPayable = !tfAmountPayable.getText().trim().isBlank() ?  new BigDecimal(tfAmountPayable.getText().trim()) : null;
	                    String ETA = dpETA.getJFormattedTextField().getText().trim(); 
	                    String ATA = !dpATA.getJFormattedTextField().getText().trim().isBlank() ? dpATA.getJFormattedTextField().getText().trim() : null;
	                    String deliveryAddress = taDeliveryAddress.getText().trim();
	
	                    // SQL 插入語句
	                    String sqlOrder = "UPDATE `order` SET status = ?, amount_payable = ?, ETA = ?, ATA = ?, delivery_address = ? "
	                    				+ "WHERE id = ? ;";
	
	                    // 準備 SQL 語句
	                    PreparedStatement stmtOrder = Overview.conn.prepareStatement(sqlOrder);
	                    
	                    stmtOrder.setString(1, status);
	                    // 金額如果為 null 則插入空值
	                    if (amountPayable != null) {
	                        stmtOrder.setBigDecimal(2, amountPayable);  // 如果有輸入金額，插入金額
	                    } else {
	                        stmtOrder.setNull(2, java.sql.Types.DECIMAL);  // 如果金額為空，插入 NULL
	                    }	
	                    // 預計交貨日期
	                    stmtOrder.setString(3, ETA);  // 插入有效的日期
	
	                    // 實際交貨日期
	                    if (ATA == null) {
	                        stmtOrder.setNull(4, java.sql.Types.DATE);  // 如果為空，插入 NULL
	                    } else {
	                        stmtOrder.setString(4, ATA);  // 否則插入有效的日期
	                    }

	                    stmtOrder.setString(5, deliveryAddress);
	                    stmtOrder.setString(6, orderID);
	
	                    // 執行訂單修改
	                    stmtOrder.executeUpdate();
	
	                    // 修改訂單產品資料
	                    String sqlDetail = "UPDATE  `order_product` SET weight = ?, grains = ?, subtotal = ?, light_source = ?, post_process = ?, remark = ?  "
	                                     + "WHERE  order_id = ? AND product_id = ?  ;";
	                    PreparedStatement stmtOrderProduct = Overview.conn.prepareStatement(sqlDetail);
	
	                    for (String[] rowData : filteredDataList) 
	                    {
	                        stmtOrderProduct.setString(7, orderID);  // 設定訂單編號
	                        for (int col = 0; col < rowData.length; col++) 
	                        {
	                            String data = rowData[col];
	                            switch (col) { // {"項次", "產品編號", "色號", "紗線規格", "紗線批號", "重量", "粒數", "小計", "對色光源", "後處理方式", "備註"};
	                                case 1: // 產品編號
	                                    stmtOrderProduct.setInt(8, Integer.parseInt(data));
	                                    break;
	                                case 5: // 重量
	                                    if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(1, new BigDecimal(data));
	                                	}
	                                	else {
	                                		stmtOrderProduct.setBigDecimal(1, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 6: // 粒數
	                                    if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(2, new BigDecimal(data));
	                                	}
	                                	else {
	                                		stmtOrderProduct.setBigDecimal(2, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 7: // 小計
	                                	if(!data.isBlank())
	                                	{
	                                		stmtOrderProduct.setBigDecimal(3, new BigDecimal(data));
	                                	}
	                                	else {
	                                		stmtOrderProduct.setBigDecimal(3, new BigDecimal("0.00"));
	                                	}
	                                    break;
	                                case 8: // 對色光源
	                                    stmtOrderProduct.setString(4, data);
	                                    break;
	                                case 9: // 後處理方式
	                                    stmtOrderProduct.setString(5, data);
	                                    break;
	                                case 10: // 備註
	                                    stmtOrderProduct.setString(6, data);
	                                    break;
	                            }
	                        }
	                        stmtOrderProduct.addBatch();  // 將每一筆資料加入批次
	                    }
	
	                    // 批次修改訂單產品
	                    stmtOrderProduct.executeBatch();
	
	                    // 如果所有操作成功，提交交易
	                    Overview.conn.commit();
	                    OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "訂單資料修改成功！", "確認");
	                    
	                } 
	                catch (SQLException e1) {
	                    // 捕獲 SQL 錯誤，進行回滾操作
	                	Overview.conn.rollback();
	                    e1.printStackTrace();
	                    OptionPaneTool.showMessageDialog(JOptionPane.ERROR_MESSAGE, "資料庫操作錯誤: " + e1.getMessage(), "確認");
	                } 
	                finally {
	                	Overview.conn.setAutoCommit(true);  // 恢復自動提交模式
	                }
	
	            } 
	            catch (SQLException e1) {
	                e1.printStackTrace();
	                OptionPaneTool.showMessageDialog(JOptionPane.ERROR_MESSAGE,  "連線失敗: " + e1.getMessage(), "確認");
	            }
	        }	      
	    });
		// 如果訂單狀態是草稿，可以選擇儲存、提交、取消。
        if (currentStatus == "草稿")
        {
    	    // 提交按鈕
    	    JButton btnSubmit = new JButton("提交");
        	btnSubmit.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        	btnSubmit.setBackground(Color.WHITE);
        	btnSubmit.setForeground(new Color(0,128,0));
        	btnSubmit.setFocusPainted(false);    
        	btnSubmit.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	cbProcessStatus.setSelectedItem("待拋轉");
    	        	btnSave.doClick();  // 模擬按下 "儲存" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
        	// 取消按鈕
    	    JButton btnCancel = new JButton("取消");
    	    btnCancel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    	    btnCancel.setBackground(Color.WHITE);
    	    btnCancel.setForeground(Color.RED);
    	    btnCancel.setFocusPainted(false);    
    	    btnCancel.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	cbProcessStatus.setSelectedItem("取消");
    	        	btnSave.doClick();  // 模擬按下 "新增" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
        	buttonPanel.add(btnSave);
    	    buttonPanel.add(btnSubmit);
    	    buttonPanel.add(btnCancel);    	    
        } 
        // 如果訂單狀態是 "待拋轉"，可進行撤回。
        else if (currentStatus == "待拋轉" || currentStatus == "待處理")
        {
        	tfAmountPayable.setEnabled(false);
        	dpETA.setEnabled(false);
        	dpATA.setEnabled(false);
        	taDeliveryAddress.setEnabled(false);
        	// 撤回按鈕
    	    JButton btnWithDraw = new JButton("撤回");
    	    btnWithDraw.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    	    btnWithDraw.setBackground(Color.WHITE);
    	    btnWithDraw.setForeground(Color.GRAY);
    	    btnWithDraw.setFocusPainted(false);    
    	    btnWithDraw.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	cbProcessStatus.setSelectedItem("草稿");
    	        	btnSave.doClick();  // 模擬按下 "新增" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
    	    buttonPanel.add(btnWithDraw);
        }
        // 如果訂單狀態是 "待出貨" or "已發貨"，可進行更新。
        else if (currentStatus == "待出貨" || currentStatus == "已發貨")
        {
        	tfAmountPayable.setEnabled(false);
        	dpETA.setEnabled(false);
        	dpATA.setEnabled(false);
        	taDeliveryAddress.setEnabled(false);
        	// 更新按鈕
    	    JButton btnRenew = new JButton("更新");
    	    btnRenew.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    	    btnRenew.setBackground(Color.WHITE);
    	    btnRenew.setForeground(Color.GRAY);
    	    btnRenew.setFocusPainted(false);    
    	    btnRenew.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	if (currentStatus == "待出貨")
    	        	{
    	        		cbProcessStatus.setSelectedItem("已發貨");
    	        	}
    	        	else
    	        	{
    	        		cbProcessStatus.setSelectedItem("已送達");
    	        	}
    	        	btnSave.doClick();  // 模擬按下 "新增" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
         }
	    // 如果訂單狀態是 "已送達"，可進行完成 or 退回。
        else if (currentStatus == "已送達")
        {
        	tfAmountPayable.setEnabled(false);
        	dpETA.setEnabled(false);
        	dpATA.setEnabled(false);
        	taDeliveryAddress.setEnabled(false);
        	// 完成按鈕
    	    JButton btnFinish = new JButton("完成");
    	    btnFinish.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    	    btnFinish.setBackground(Color.WHITE);
    	    btnFinish.setForeground(Color.GRAY);
    	    btnFinish.setFocusPainted(false);    
    	    btnFinish.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	cbProcessStatus.setSelectedItem("完成");

    	        	btnSave.doClick();  // 模擬按下 "新增" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
    	    buttonPanel.add(btnFinish);
    	    // 退回按鈕
    	    JButton btnReturn = new JButton("退回");
    	    btnReturn.setFont(new Font("微軟正黑體", Font.BOLD, 16));
    	    btnReturn.setBackground(Color.WHITE);
    	    btnReturn.setForeground(Color.GRAY);
    	    btnReturn.setFocusPainted(false);    
    	    btnReturn.addActionListener(new ActionListener() 
    	    {
    	        public void actionPerformed(ActionEvent e) 
    	        {
    	        	cbProcessStatus.setSelectedItem("退回");

    	        	btnSave.doClick();  // 模擬按下 "新增" 按鈕
    	        	frame.revalidate(); // 重新驗證佈局管理器
    	        	frame.repaint(); // 重新繪製元件
    	        }
    	    });
    	    buttonPanel.add(btnReturn);
        }
        
        frame.setVisible(true);
	}

	private void initOrderFrame() 
	{
	    frame = new JFrame("");
	    frame.setBounds(0, 0, 1000, 650);
	    frame.setLocationRelativeTo(null);
	    
	    // 整體視窗排版
	    JPanel panelFrame = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(20, 40, 20, 40); // 設定元件之間的間距
	    gbc.fill = GridBagConstraints.BOTH;  // 填滿水平和垂直空間
	    // 訂單資訊區塊
	    JPanel panelOrderInformation = new JPanel(new GridBagLayout());
	    TitledBorder orderInfoBorder = new TitledBorder(null, "訂單資訊", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
	    orderInfoBorder.setTitleFont(new Font("微軟正黑體", Font.ITALIC, 18));  // 設定字體
	    panelOrderInformation.setBorder(orderInfoBorder);

	    // 設定 GridBagConstraints
	    GridBagConstraints gbcOrderInfo = new GridBagConstraints();
	    gbcOrderInfo.insets = new Insets(10, 10, 10, 10);  // 控制內部元件之間的間距
	    gbcOrderInfo.fill = GridBagConstraints.BOTH;  // 水平和垂直填滿
	    // 訂單編號
	    lblOrderID = new JLabel("訂單編號");
	    lblOrderID.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 0; 
	    gbcOrderInfo.weightx = 0.4;
	    gbcOrderInfo.weighty = 0.1; 
	    panelOrderInformation.add(lblOrderID,gbcOrderInfo);
	    //
	    tfOrderID = new JTextField();
	    tfOrderID.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfOrderID.setEditable(false);
	    tfOrderID.setColumns(10);
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 0; 
	    gbcOrderInfo.weightx = 0.6; // 控制水平權重（可調整）
	    gbcOrderInfo.weighty = 0.2; // 控制垂直權重（調整這個來控制高度）
	    panelOrderInformation.add(tfOrderID,gbcOrderInfo);
	    // 處理狀態
	    lblProcessStatus = new JLabel("處理狀態");
	    lblProcessStatus.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 1; 
	    panelOrderInformation.add(lblProcessStatus,gbcOrderInfo);
	    //
	    cbProcessStatus = new JComboBox<String>();
	    cbProcessStatus.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    cbProcessStatus.setBackground(Color.WHITE);
	    cbProcessStatus.setFocusable(false);
		cbProcessStatus.addItem("草稿");
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 1; 
	    panelOrderInformation.add(cbProcessStatus,gbcOrderInfo);	  
	    // 下單日期
	    lblOrderDate = new JLabel("下單日期");
	    lblOrderDate.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 2; 
	    panelOrderInformation.add(lblOrderDate,gbcOrderInfo);
	    //
	    tfOrderDate = new JTextField();
	    tfOrderDate.setColumns(10);
	    tfOrderDate.setEditable(false);
	    tfOrderDate.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 2; 
	    panelOrderInformation.add(tfOrderDate,gbcOrderInfo);
	    // 銷售人員
	    lblSales = new JLabel("<html>銷售人員<font color='red'>*</font></html>");
	    lblSales.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 3; 
	    panelOrderInformation.add(lblSales,gbcOrderInfo);
	    //
	    tfSales = new JTextField();
	    tfSales.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfSales.setColumns(10);
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 3; 
	    panelOrderInformation.add(tfSales,gbcOrderInfo);
	    // 應付金額
	    lblAmountPayable = new JLabel("應付金額");
	    lblAmountPayable.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 4; 
	    panelOrderInformation.add(lblAmountPayable,gbcOrderInfo);
	    //
	    tfAmountPayable = new JTextField();
	    tfAmountPayable.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfAmountPayable.setColumns(10);
	    // 自訂 InputVerifier，檢查應付金額格式
	    tfAmountPayable.setInputVerifier(new InputVerifier() 
	    {
	        @Override
	        public boolean verify(JComponent input) 
	        {
	            JTextField textField = (JTextField) input;
	            String text = textField.getText().trim(); // 獲取使用者輸入並去除空格
	            
	            // 如果輸入為空白，允許通過驗證，不強制使用者填寫
	            if (text.isBlank()) {
	                return true;
	            }

	            try {
	                // 檢查是否為有效的數字格式
	                BigDecimal amount = new BigDecimal(text);
	                
	                // 檢查是否為正數並且符合格式 (最多10位數，保留2位小數)
	                if (amount.compareTo(BigDecimal.ZERO) < 0 || !text.matches("^[1-9]\\d{0,9}(\\.\\d{1,2})?$")) {
	                    OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE, "應付金額必須為正數，並且最多保留兩位小數!", "確認");
	                    return false;
	                }
	            } catch (NumberFormatException e) {
	                OptionPaneTool.showMessageDialog(JOptionPane.ERROR_MESSAGE, "應付金額必須為正數!", "確認");
	                return false;
	            }

	            // 如果格式正確，返回 true
	            return true;
	        }
	    });
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 4; 
	    panelOrderInformation.add(tfAmountPayable,gbcOrderInfo);
	    // 預計交貨日期
	    lblETA = new JLabel("<html>預計交貨日期<font color='red'>*</font></html>");
	    lblETA.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 5; 
	    panelOrderInformation.add(lblETA,gbcOrderInfo);
	    //
	    UtilDateModel dmETA = new UtilDateModel();
		Properties ETAProperties = new Properties();
		JDatePanelImpl datePanelETA = new JDatePanelImpl(dmETA, ETAProperties);
		dpETA = new JDatePickerImpl(datePanelETA, null);
		dpETA.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpETA.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpETA.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpETA.setButtonFocusable(false);
		dpETA.setTextEditable(false);	
		gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 5; 
		panelOrderInformation.add(dpETA,gbcOrderInfo);
	    //
	    lblATA = new JLabel("實際交貨日期");
	    lblATA.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 6; 
	    panelOrderInformation.add(lblATA,gbcOrderInfo);
	    //
	    UtilDateModel dmATA = new UtilDateModel();
		Properties ATAProperties = new Properties();
		JDatePanelImpl datePanelATA = new JDatePanelImpl(dmATA, ATAProperties);
		dpATA = new JDatePickerImpl(datePanelATA, null);
		dpATA.getJFormattedTextField().setBackground(new Color(255, 255, 255));
		dpATA.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
		dpATA.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
		dpATA.setButtonFocusable(false);
		dpATA.setTextEditable(false);	
		gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 6; 
		panelOrderInformation.add(dpATA,gbcOrderInfo);
		// 選擇日期觸發器
		PropertyChangeListener dateChangeListener = new PropertyChangeListener() 
		{
		    public void propertyChange(PropertyChangeEvent evt) {
		        if (evt.getPropertyName().equals("value")) {
		            Object source = evt.getSource();
		            if (source instanceof UtilDateModel) {
		                UtilDateModel model = (UtilDateModel) source;
		                Date selectedDate = (Date) model.getValue();
		                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		                String formattedDate = selectedDate != null ? dateFormat.format(selectedDate) : "";
		                
		                // 設定日期格式
		                if (evt.getSource() == dmETA) 
		                {
		                    dpETA.getJFormattedTextField().setText(formattedDate);
		                } 
		                else if (evt.getSource() == dmATA) {
		                	dpATA.getJFormattedTextField().setText(formattedDate);
		                }
		            }
		        }
		    }
		};

		dmETA.addPropertyChangeListener(dateChangeListener);
		dmATA.addPropertyChangeListener(dateChangeListener);
	    // 出貨地址
	    lblDeliveryAddress = new JLabel("出貨地址");
	    lblDeliveryAddress.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcOrderInfo.gridx = 0; 
	    gbcOrderInfo.gridy = 7; 
	    panelOrderInformation.add(lblDeliveryAddress,gbcOrderInfo);
	    //
	    taDeliveryAddress = new JTextArea(3, 30);
	    taDeliveryAddress.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    taDeliveryAddress.setLineWrap(true);  // 自動換行
	    taDeliveryAddress.setWrapStyleWord(true);
	    gbcOrderInfo.gridx = 1; 
	    gbcOrderInfo.gridy = 7; 
	    panelOrderInformation.add(taDeliveryAddress,gbcOrderInfo);
	    //
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    panelOrderInformation.setPreferredSize(new Dimension(400, 400));
	    gbc.anchor = GridBagConstraints.WEST; // 左對齊
        gbc.fill = GridBagConstraints.BOTH; // 填滿垂直和水平空間 
	    panelFrame.add(panelOrderInformation,gbc);
	    // 客戶資訊區塊
	    JPanel panelClientInformation = new JPanel(new GridBagLayout());
	    TitledBorder clientInfoBorder = new TitledBorder(null, "客戶資訊", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
	    clientInfoBorder.setTitleFont(new Font("微軟正黑體", Font.ITALIC, 18));  // 設定字體//	    panelClientInformation.setBounds(503, 37, 351, 336);
        panelClientInformation.setBorder(clientInfoBorder);

	    // 設定 GridBagConstraints
	    GridBagConstraints gbcClientInfo = new GridBagConstraints();
	    gbcClientInfo.insets = new Insets(10, 10, 10, 10);  // 控制內部元件之間的間距
	    gbcClientInfo.fill = GridBagConstraints.BOTH;  // 水平和垂直填滿
	    // 代訂廠商
	    lblMiddlemen = new JLabel("代訂廠商"); 
	    lblMiddlemen.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 0; 
	    gbcClientInfo.weightx = 0.4; // 控制水平權重（可調整）
	    gbcClientInfo.weighty = 0.1; // 控制垂直權重（調整這個來控制高度）
	    panelClientInformation.add(lblMiddlemen,gbcClientInfo);
	    //
	    tfMiddlemen = new JTextField();
	    tfMiddlemen.setColumns(10);
	    tfMiddlemen.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 0; 
	    gbcClientInfo.weightx = 0.6; // 控制水平權重（可調整）
	    gbcClientInfo.weighty = 0.2; // 控制垂直權重（調整這個來控制高度）
	    panelClientInformation.add(tfMiddlemen,gbcClientInfo);
	    // 客戶編號
	    lblClientID = new JLabel("<html>客戶編號<font color='red'>*</font></html>");
	    lblClientID.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 1; 
	    panelClientInformation.add(lblClientID,gbcClientInfo);
	    //
	    cbClientID = new JComboBox<String>();
	    // 創建自訂JComboBox
	    DefaultComboBoxModel<String> cbmodelClientID = new DefaultComboBoxModel<>();
	    cbClientID = new JComboBox<>(cbmodelClientID);
	    List<String> allClientID = new ArrayList<>();  // 用於儲存所有的客戶編號
	    try 
	    {
	        // SQL 查詢語句
	        String sql = "SELECT `id` FROM client";
	        PreparedStatement stmt = Overview.conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        allClientID.add("");
	        // 將所有查詢結果加入到 allColorID 列表中
	        while (rs.next()) 
	        {
	        	allClientID.add(rs.getString("id"));
	        }
	        
	        // 關閉資源
	        rs.close();
	        stmt.close();
	    } 
	    catch (SQLException e) 
	    {
	        e.printStackTrace();
	    }

	    // 將查詢到的色號加入到 JComboBox 中
	    for (String client : allClientID) 
	    {
	    	cbmodelClientID.addElement(client);
	    }
	    // 自訂 ComboBox UI
	    cbClientID.setUI(new BasicComboBoxUI() 
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
	    cbClientID.setBackground(Color.WHITE);
	    cbClientID.setFocusable(false);
	    cbClientID.setMaximumRowCount(8);  // 設置最多顯示 8 行
	    cbClientID.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    // 根據所選項目 自動填入相關欄位
	    cbClientID.addActionListener(new ActionListener() 
	    {
	        @Override
	        public void actionPerformed(ActionEvent e) 
	        {
	            String selectedClientID = (String) cbClientID.getSelectedItem(); // 取得選中的客戶編號
	            
	            if (!selectedClientID.isBlank()) 
	            {
	            	try 
	            	{
	                    String sql = "SELECT unified_number, name, contact_person, contact_phone, email, address FROM client WHERE id = ?";
	                    PreparedStatement stmt = Overview.conn.prepareStatement(sql);
	                    stmt.setString(1, selectedClientID);
	                    
	                    ResultSet rs = stmt.executeQuery();
	                    if (rs.next()) 
	                    {
	                        // 將查詢結果填入對應欄位
	                        tfUnifiedNumber.setText(rs.getString("unified_number"));
	                        tfClient.setText(rs.getString("name"));
	                        tfContactPerson.setText(rs.getString("contact_person"));
	                        tfContactPhone.setText(rs.getString("contact_phone"));
	                        tfEmail.setText(rs.getString("email"));
	                        taAddress.setText(rs.getString("address"));
	                    }

	                    rs.close();
	                    stmt.close();
	                } 
	            	catch (SQLException e1) {
	                    e1.printStackTrace();
	                } 
	            } 
	            else 
	            {
	            	tfUnifiedNumber.setText("");
	            	tfClient.setText("");
	                tfContactPerson.setText("");
	                tfContactPhone.setText("");
	                tfEmail.setText("");
	                taAddress.setText("");
	            }
	        }
	    });

	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 1; 
	    panelClientInformation.add(cbClientID,gbcClientInfo);
	    // 統一編號
	    lblUnifiedNumber = new JLabel("統一編號");
	    lblUnifiedNumber.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 2; 
	    panelClientInformation.add(lblUnifiedNumber,gbcClientInfo);   
	    //
	    tfUnifiedNumber = new JTextField();
	    tfUnifiedNumber.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfUnifiedNumber.setEditable(false);
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 2; 
	    panelClientInformation.add(tfUnifiedNumber,gbcClientInfo);
	    // 客戶
	    lblClient = new JLabel("客戶");
	    lblClient.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 3; 
	    panelClientInformation.add(lblClient,gbcClientInfo);
	    //
	    tfClient = new JTextField();
	    tfClient.setEditable(false);
	    tfClient.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 3; 
	    panelClientInformation.add(tfClient,gbcClientInfo);
	    // 聯絡人
	    lblContactPerson = new JLabel("聯絡人");
	    lblContactPerson.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 4; 
	    panelClientInformation.add(lblContactPerson,gbcClientInfo);
	    //
	    tfContactPerson = new JTextField();
	    tfContactPerson.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfContactPerson.setEditable(false);
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 4; 
	    panelClientInformation.add(tfContactPerson,gbcClientInfo);
	    // 連絡電話
	    lblContactPhone = new JLabel("連絡電話");
	    lblContactPhone.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 5; 
	    panelClientInformation.add(lblContactPhone,gbcClientInfo);
	    //
	    tfContactPhone = new JTextField();
	    tfContactPhone.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfContactPhone.setEditable(false);
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 5; 
	    panelClientInformation.add(tfContactPhone,gbcClientInfo);
	    // Email
	    lblEmail = new JLabel("Email");
	    lblEmail.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 6; 
	    panelClientInformation.add(lblEmail,gbcClientInfo);
	    //
	    tfEmail = new JTextField();
	    tfEmail.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    tfEmail.setEditable(false);
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 6; 
	    panelClientInformation.add(tfEmail,gbcClientInfo);
	    // 地址
	    lblAddress = new JLabel("地址");
	    lblAddress.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    gbcClientInfo.gridx = 0; 
	    gbcClientInfo.gridy = 7; 
	    panelClientInformation.add(lblAddress,gbcClientInfo);
	    //
	    taAddress = new JTextArea(3, 30);
	    taAddress.setEditable(false);
	    taAddress.setWrapStyleWord(true);
	    taAddress.setLineWrap(true);
	    taAddress.setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    taAddress.setBackground(new Color(220,220,220));
	    gbcClientInfo.gridx = 1; 
	    gbcClientInfo.gridy = 7; 
	    panelClientInformation.add(taAddress,gbcClientInfo);
	    //
	    gbc.gridx = 1; // 放在右邊的列
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // 右對齊
        panelClientInformation.setPreferredSize(new Dimension(400, 400));
        panelFrame.add(panelClientInformation, gbc);
	    // 品項明細區塊
	    JPanel blockPanel = new JPanel();
	    blockPanel.setBackground(new Color(240, 248, 255));
	    blockPanel.setLayout(new BorderLayout());
	    blockPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    //
	    JLabel lblSchema = new JLabel("品項明細");
	    lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSchema.setFont(new Font("微軟正黑體", Font.ITALIC, 22));
	    lblSchema.setForeground(Color.GRAY);
	    lblSchema.setPreferredSize(new Dimension(700, 40));
	    blockPanel.add(lblSchema, BorderLayout.NORTH);
 	    
	    // 表格部分初始化（佈局）
	    table = new JTable();
	    table.setGridColor(Color.LIGHT_GRAY);
	    table.setIntercellSpacing(new Dimension(1, 1));
	    table.setRowHeight(30);
	    table.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 禁用自動調整列寬
	    	    
	    // 設置表頭樣式
        tableHeaderT = table.getTableHeader();
        tableHeaderT.setForeground(Color.GRAY);
        tableHeaderT.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        //
	    spTable = new JScrollPane(table);
	    spTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    spTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    blockPanel.add(spTable, BorderLayout.CENTER);
	    //
	    buttonPanel = new JPanel(new FlowLayout());
	    blockPanel.add(buttonPanel, BorderLayout.SOUTH);
	    //
	    gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // 占據兩欄
        gbc.anchor = GridBagConstraints.CENTER;
        blockPanel.setPreferredSize(new Dimension(700, 300));
        panelFrame.add(blockPanel, gbc);
	    
	    spFrame = new JScrollPane(panelFrame);
	    spFrame.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    spFrame.getVerticalScrollBar().setUnitIncrement(16); // 每次滾動16像素，可以根據需要調整
	    frame.add(spFrame);
	}


}
