package sms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class CreateOrder extends OrderOperation 
{
	private JButton btnSave;
//
//    public CreateOrder() 
//    {
////    	super(null);
//    	frame.setTitle("建立訂單");
//        String defaultOrderID = null;
//        try 
//        {
//        	//訂單編碼
//			Calendar today = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            
//            defaultOrderID = sdf.format(today.getTime());
//            
//			Connection conn = DriverManager.getConnection(url, username, password);
//			String sql = "SELECT  COUNT(訂單編號) AS 當日訂單數  FROM 訂單紀錄表 WHERE 下單日期 = CURDATE();";
//			PreparedStatement stmt = conn.prepareStatement(sql);
//			ResultSet rs = stmt.executeQuery();
//
//			if (rs.next()) 
//			{
//                int orderCount = rs.getInt(1);
//                orderCount++;
//                String temp = Integer.toString(orderCount);
//                if(orderCount>=10)
//                {
//                	defaultOrderID = defaultOrderID + temp;
//                }
//                else
//                {
//                	defaultOrderID = defaultOrderID+"000"+temp;	
//                }
//                
//            } 
//			else 
//			{
//				defaultOrderID = defaultOrderID+"001";
//            }
//			
//		} 
//        catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        
//        tfOrderID.setText(defaultOrderID);
//        tfClient.setEditable(true);
//        tfMiddlemen.setEditable(true);
//        taDeliveryAddress.setEditable(true);
//		// 建議字詞
//		SuggestionTool sugClient = new SuggestionTool("訂單紀錄表","客戶", url, username, password);
//		SuggestionTool sugMiddlemen = new SuggestionTool("訂單紀錄表","代訂廠商", url, username, password);
//		SuggestionTool sugDeliveryAddress = new SuggestionTool("訂單紀錄表","出貨地址", url, username, password);
//		sugClient.addTextComponent(tfClient);
//		sugMiddlemen.addTextComponent(tfMiddlemen);
//		sugDeliveryAddress.addTextComponent(taDeliveryAddress);
//        // 設置當天日期
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String todayDate = dateFormat.format(new Date());
//        tfOrderDate.setText(todayDate);
//        //
//        String[] columnNames = {"項次", "狀態", "顏色類型", "色號", "顏色名稱", "紗線編號", "紗線規格", "重量", "粒數", "金額", "對色光源", "預計交貨日期", "完成日期", "後處理方式","備註"};
//        Object[][] data = new Object[99][columnNames.length];
//        for (int i = 0; i < 99; i++) {
//            data[i][0] = i + 1; // ID列初始化為1到99
//        }
//        // 創建表格模型
//        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
//        table.setModel(tableModel);
//        table.setEnabled(true);
//        
//        setTableEditors();
//        
//        btnSave = new JButton("儲存");
//	    btnSave.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//	    btnSave.setBounds(891, 549, 71, 23);
//	    btnSave.setBackground(Color.WHITE);
//	    btnSave.setFocusPainted(false);
//	    // 增加滑鼠事件監聽器
//	    btnSave.addMouseListener(new java.awt.event.MouseAdapter() 
//	    {
//	        @Override
//	        public void mouseEntered(java.awt.event.MouseEvent evt) {
//	            // 滑鼠移到按鈕時，背景變成淺灰色
//	            btnSave.setBackground(Color.LIGHT_GRAY);
//	        }
//
//	        @Override
//	        public void mouseExited(java.awt.event.MouseEvent evt) {
//	            // 滑鼠離開按鈕時，背景變回白色
//	            btnSave.setBackground(Color.WHITE);
//	        }
//	    });
//	    // 儲存事件
//	    btnSave.addActionListener(new ActionListener() 
//        {
//        	public void actionPerformed(ActionEvent e) 
//        	{
//        		// 檢查必填欄位(客戶、出貨地址)
//        		if(tfClient.getText().isBlank() ||  taDeliveryAddress.getText().isBlank())
//        		{
//        			OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE,"訂單資訊未填寫完整:客戶,出貨地址","確認");
//        			return;
//        		}
//        		// 取得表格模型
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                int rowCount = model.getRowCount(); // 取得行數
//                int columnCount = model.getColumnCount(); // 取得列數
//
//                // 儲存過濾後的資料 (String 陣列)
//                List<String[]> filteredDataList = new ArrayList<>();
//
//                // 取得每行的數據
//                for (int row = 0; row < rowCount; row++) 
//                {
//                    // 檢查狀態和顏色類型是否有輸入
//                    if (model.getValueAt(row, 1) != null && !model.getValueAt(row, 1).toString().trim().isEmpty() &&
//                        model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().trim().isEmpty()) 
//                    {
//                        String[] rowData = new String[columnCount];
//                        for (int col = 0; col < columnCount; col++) 
//                        {
//                            Object cellValue = model.getValueAt(row, col);
//                            rowData[col] = (cellValue != null && !cellValue.toString().trim().isEmpty()) ? cellValue.toString() : "\"\""; // 空值替換為 ""
//                        }
//                        filteredDataList.add(rowData);
//                    }
//                    else if(model.getValueAt(row, 2) != null && !model.getValueAt(row, 2).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 3) != null && !model.getValueAt(row, 3).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 4) != null && !model.getValueAt(row, 4).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 5) != null && !model.getValueAt(row, 5).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 6) != null && !model.getValueAt(row, 6).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 7) != null && !model.getValueAt(row, 7).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 8) != null && !model.getValueAt(row, 8).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 9) != null && !model.getValueAt(row, 9).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 10) != null && !model.getValueAt(row, 10).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 11) != null && !model.getValueAt(row, 11).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 12) != null && !model.getValueAt(row, 12).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 13) != null && !model.getValueAt(row, 13).toString().trim().isEmpty() ||
//                    		model.getValueAt(row, 14) != null && !model.getValueAt(row, 14).toString().trim().isEmpty() 
//                    		)
//                    {
//                    	row += 1;
//                    	String errorRow = Integer.toString(row);
//                    	OptionPaneTool.showMessageDialog(JOptionPane.INFORMATION_MESSAGE,"項次"+errorRow+"資料填寫不完整!","OK");
//            			return;
//                    }
//                    
//                }
//
//                // 打印過濾後的資料
////              filteredDataList.forEach(row -> System.out.println(Arrays.toString(row)));
//
//
//                try (Connection conn = DriverManager.getConnection(url, username, password)) 
//                {
//                    // 關閉自動提交，開始交易
//                    conn.setAutoCommit(false);
//
//                    // 插入訂單紀錄表
//                    String sqlOrder = "INSERT INTO `訂單紀錄表`  (`訂單編號`, `下單日期`, `客戶`, `代訂廠商`, `出貨地址`) VALUES (?, ?, ?, ?, ?);";
//                    try (PreparedStatement stmtOrder = conn.prepareStatement(sqlOrder)) 
//                    {
//                        String orderID = tfOrderID.getText().trim();
//                        String orderDate = tfOrderDate.getText().trim();
//                        String client = tfClient.getText().trim();
//                        String middlemen = tfMiddlemen.getText().trim();
//                        String deliveryAddress = taDeliveryAddress.getText().trim();
//
//                        // 設置訂單資料
//                        stmtOrder.setString(1, orderID);
//                        stmtOrder.setString(2, orderDate);
//                        stmtOrder.setString(3, client);
//                        stmtOrder.setString(4, middlemen);
//                        stmtOrder.setString(5, deliveryAddress);
//
//                        stmtOrder.executeUpdate();
//                    }
//
//                    // 插入訂單品項明細表
//                    String sqlDetail = "INSERT INTO `訂單品項明細表` (`訂單編號`, `品號`, `狀態`, `顏色類型`, `色號`, `顏色名稱`, `紗線編號`, `紗線規格`, `重量`, `粒數`, `金額`, `對色光源`, `預計交貨日期`, `完成日期`, `後處理方式`, `備註`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//                    try (PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail)) 
//                    {
//                        for (String[] rowData : filteredDataList) 
//                        {
//                            stmtDetail.setString(1, tfOrderID.getText().trim()); // 設定訂單編號
//                            for (int col = 0; col < rowData.length; col++) 
//                            {
//                                String data = rowData[col];
//                                switch (col) 
//                                {
//                                    case 0: // 品號
//                                        stmtDetail.setInt(2, Integer.parseInt(data));
//                                        break;
//                                    case 1: // 狀態
//                                        stmtDetail.setString(3, data);
//                                        break;
//                                    case 2: // 顏色類型
//                                        stmtDetail.setString(4, data);
//                                        break;
//                                    case 3: // 色號
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(5, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(5, data); // 插入有效的色號
//                                        }
//                                        break;
//                                    case 4: // 顏色名稱
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(6, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(6, data); // 插入有效的色號
//                                        }
//                                        break;
//                                    case 5: // 紗線編號
//                                        try 
//                                        {
//                                            stmtDetail.setInt(7, Integer.parseInt(data)); // 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                            stmtDetail.setNull(7, java.sql.Types.INTEGER);
//                                        }
//                                        break;
//                                    case 6: // 紗線規格
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(8, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(8, data); // 插入有效的色號
//                                        }
//                                        break;
//                                    case 7: // 重量
//                                        try 
//                                        {
//                                            stmtDetail.setFloat(9, Float.parseFloat(data));
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            stmtDetail.setFloat(9, 0.0f); // 當格式錯誤時，設置預設值
//                                        }
//                                        break;
//
//                                    case 8: // 粒數
//                                        try 
//                                        {
//                                            stmtDetail.setFloat(10, Float.parseFloat(data));
//                                        } 
//                                        catch (NumberFormatException e1)
//                                        {
//                                            stmtDetail.setFloat(10, 0.0f); // 當格式錯誤時，設置預設值
//                                        }
//                                        break;
//
//                                    case 9: // 金額
//                                        try 
//                                        {
//                                            stmtDetail.setBigDecimal(11, new BigDecimal(data));
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            stmtDetail.setBigDecimal(11, BigDecimal.ZERO); // 當格式錯誤時，設置為 0
//                                        }
//                                        break;
//                                    case 10: // 對色光源
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(12, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(12, data); // 插入有效的色號
//                                        }
//                                        break;
//                                    case 11: // 預計交貨日期
//                                    	try 
//                                        {
//                                            stmtDetail.setDate(13, java.sql.Date.valueOf(data)); 
//                                        } 
//                                        catch (IllegalArgumentException e1)
//                                        {
//                                            // 處理日期格式錯誤，設定 null 或記錄錯誤
//                                            stmtDetail.setNull(13, java.sql.Types.DATE);
//                                        }
//                                        break;
//                                    case 12: // 完成日期
//                                    	try 
//                                        {
//                                            stmtDetail.setDate(14, java.sql.Date.valueOf(data)); 
//                                        } 
//                                        catch (IllegalArgumentException e1)
//                                        {
//                                            // 處理日期格式錯誤，設定 null 或記錄錯誤
//                                            stmtDetail.setNull(14, java.sql.Types.DATE);
//                                        }
//                                        break;
//
//                                    case 13: // 後處理方式
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(15, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(15, data); // 插入有效的色號
//                                        }
//                                        break;
//                                    case 14: // 備註
//                                        try 
//                                        {
//                                        	stmtDetail.setNull(16, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
//                                        } 
//                                        catch (NumberFormatException e1) 
//                                        {
//                                            // 處理數字格式異常的情況，如記錄錯誤或設置預設值
//                                        	stmtDetail.setString(16, data); // 插入有效的色號
//                                        }
//                                        break;
//                                }
//                            }
//                            stmtDetail.addBatch();
//                        }
//
//                        // 批次插入訂單品項明細
//                        stmtDetail.executeBatch();
//                    }
//
//                    // 提交交易
//                    conn.commit();                    
//                    frame.dispose();  // 關閉當前視窗
//                } 
//                catch (SQLException e1) 
//                {
//                    // 如果其中一個提交發生錯誤，回滾整個交易
//                    try (Connection conn = DriverManager.getConnection(url, username, password)) 
//                    {
//                        if (conn != null) 
//                        {
//                            conn.rollback();
//                        }
//                    } 
//                    catch (SQLException rollbackEx) 
//                    {
//                        rollbackEx.printStackTrace();
//                    }
//                    e1.printStackTrace();
//                    // 顯示錯誤消息
//                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"提交訂單時發生錯誤：" + e1.getMessage(),"了解");
//                }
//        	}
//        });
//	    
//	    // 創建一個用於置中的渲染器
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // 將此渲染器應用到表格的每一列
//        for (int i = 0; i < table.getColumnCount(); i++) 
//        {
//            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//	    frame.getContentPane().add(btnSave);
//        frame.setVisible(true);
//    }
//
//
//    
//    private void setTableEditors() 
//    {
//        // 創建狀態下拉選單
//        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"草稿", "待拋轉", "待處理", "處理中", "待出貨","已完成",  "退回", "取消"});
//        cbStatus.addItemListener(e -> 
//        {
//            if (e.getStateChange() == ItemEvent.SELECTED && !"草稿".equals(cbStatus.getSelectedItem())) {
//                // 如果選擇了非 "草稿" 的選項，強制回到 "草稿"
//                cbStatus.setSelectedItem("草稿");
//            }
//        });
//        // 創建狀態下拉選單
//        JComboBox<String> cbColorType = new JComboBox<>(new String[]{"舊色下染", "新色下染", "新色打樣"});
//
//        // 獲取表格的狀態列（第1列），並設置其編輯器為下拉選單
//        TableColumn statusColumn = table.getColumnModel().getColumn(1); 
//        statusColumn.setCellEditor(new DefaultCellEditor(cbStatus));
//        
//        // 獲取表格的狀態列（第2列），並設置其編輯器為下拉選單
//        TableColumn colorTypeColumn = table.getColumnModel().getColumn(2); 
//        colorTypeColumn.setCellEditor(new DefaultCellEditor(cbColorType));
//
//        // 獲取表格的交貨日期列（第11列）和完成日期列（第12列）
//        TableColumn deliveryDateColumn = table.getColumnModel().getColumn(11); 
//        TableColumn completionDateColumn = table.getColumnModel().getColumn(12); 
//
//        // 設置交貨日期列和完成日期列的編輯器為自訂的日期選擇器編輯器
//        deliveryDateColumn.setCellEditor(new DatePickerCellEditor());
//        completionDateColumn.setCellEditor(new DatePickerCellEditor());
//
//        // 設置交貨日期列和完成日期列的顯示寬度
//        deliveryDateColumn.setPreferredWidth(100);  
//        completionDateColumn.setPreferredWidth(100);  
//    }
//
//
//    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter 
//    {
//        private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); // 設置日期格式為 yyyy/MM/dd
//
//        @Override
//        public Object stringToValue(String text) throws java.text.ParseException 
//        {
//            // 將字串轉換為日期對象
//            return dateFormatter.parse(text);
//        }
//
//        @Override
//        public String valueToString(Object value) 
//        {
//            if (value instanceof Date) {
//                // 將日期對象格式化為字串
//                return dateFormatter.format((Date) value);
//            }
//            return "";
//        }
//    }
//
//    public class DatePickerCellEditor extends AbstractCellEditor implements TableCellEditor 
//    {
//        private JDatePickerImpl datePicker;
//        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        public DatePickerCellEditor() 
//        {
//            UtilDateModel model = new UtilDateModel();
//            Properties p = new Properties();
//            JDatePanelImpl datePanel = new JDatePanelImpl(model, p);      
//            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
//
//            // 註冊 PropertyChangeListener 監聽日期變化事件
//            model.addPropertyChangeListener(new PropertyChangeListener() 
//            {
//                @Override
//                public void propertyChange(PropertyChangeEvent evt) {
//                    if ("value".equals(evt.getPropertyName())) {
//                        Date selectedDate = (Date) model.getValue();
//                        String formattedDate = selectedDate != null ? dateFormat.format(selectedDate) : "";
//                        datePicker.getJFormattedTextField().setText(formattedDate);
//                    }
//                }
//            });
//
//            // 設置日期選擇器的樣式
//            datePicker.getJFormattedTextField().setBackground(new Color(255, 255, 255));
//            datePicker.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
//            datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 14));
//            datePicker.setButtonFocusable(false);
//            datePicker.setTextEditable(false);  // 確保日期欄位不可編輯
//        }
//
//        @Override
//        public Object getCellEditorValue() 
//        {
//            // 格式化並返回選擇的日期值
//            Date selectedDate = (Date) datePicker.getModel().getValue();
//            return selectedDate != null ? dateFormat.format(selectedDate) : "";
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
//        {
//            if (value instanceof Date) 
//            {
//                Date dateValue = (Date) value;
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(dateValue);
//                datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//                datePicker.getModel().setSelected(true);
//            }
//            return datePicker;
//        }
//    }

}
