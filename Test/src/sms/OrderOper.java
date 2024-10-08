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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class OrderOper 
{
	//連接資料庫
	private String url = "jdbc:mariadb://localhost:3306/bao-chi";
	private String username = "root";
	private String password = "1234";

	public OrderOper(Object[] rowData,String orderId) 
	{
	    // 建立全螢幕的 JFrame
	    JFrame dataFrame = new JFrame();
	    dataFrame.setBackground(Color.DARK_GRAY);
	    dataFrame.setForeground(Color.DARK_GRAY);
	    dataFrame.setUndecorated(true); // 移除邊框
	    dataFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // 設定為全屏
	    dataFrame.getContentPane().setBackground(Color.DARK_GRAY); // 黑色背景，帶有透明度
	    dataFrame.setOpacity(0.95f); // 整體透明度

	    // 設定關閉按鈕（右上角大叉叉）
	    JButton closeButton = new JButton("X 關閉");
	    closeButton.setFont(new Font("微軟正黑體", Font.BOLD, 28));
	    closeButton.setForeground(Color.WHITE);
	    closeButton.setBorderPainted(false); // 不顯示邊框
	    closeButton.setFocusPainted(false);
	    closeButton.setContentAreaFilled(false); // 按鈕透明背景
	    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    closeButton.addActionListener(e -> dataFrame.dispose()); // 點擊關閉視窗

	    // 將關閉按鈕置於右上角
	    JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    closePanel.setOpaque(false); // 設定透明背景
	    closePanel.add(closeButton);

	    // 建立 GridBagLayout 來排版
	    JPanel gridPanel = new JPanel(new GridBagLayout());
	    gridPanel.setOpaque(false); // 背景透明
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // 設置元件間的邊距

	    Dimension fixedSize = new Dimension(200, 30); // 設定整體固定寬度

	    // 項次
	    JLabel lblItemNum = new JLabel("項次");
	    lblItemNum.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblItemNum.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblItemNum, gbc);
	    // 
	    JTextField tfItemNum = new JTextField();
	    tfItemNum.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfItemNum.setText((String) rowData[0]);
	    tfItemNum.setEditable(false);
	    tfItemNum.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gridPanel.add(tfItemNum, gbc);
	    // 狀態
	    JLabel lblStatus = new JLabel("狀態");
	    lblStatus.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblStatus.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gridPanel.add(lblStatus, gbc);
	    //
	    JComboBox<String> cbStatus = new JComboBox<String>();    
	    cbStatus.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    cbStatus.setBackground(Color.WHITE);
	    cbStatus.setFocusable(false); // 禁用焦點邊框
	    cbStatus.setPreferredSize(fixedSize);  // 設定寬高
	    cbStatus.addItem((String) rowData[1]);
	    cbStatus.setEditable(false);
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gridPanel.add(cbStatus, gbc);
	    // 顏色類型
	    JLabel lblColorType = new JLabel("顏色類型");
	    lblColorType.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblColorType.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gridPanel.add(lblColorType, gbc);
	    //
	    String color[] = {"舊色下染","新色下染","新色打樣"};
	    JComboBox<String> cbColorType = new JComboBox<String>(color); // 使用現有的選項
	    cbColorType.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    cbColorType.setBackground(Color.WHITE);
	    cbColorType.setFocusable(false); // 禁用焦點邊框
	    cbColorType.setPreferredSize(fixedSize);  // 設定寬高
	    cbColorType.setEditable(false);
	    cbColorType.setSelectedItem((String) rowData[2]); // 設定預設選項
	    gbc.gridx = 1;
	    gbc.gridy = 2;
	    gridPanel.add(cbColorType, gbc);
	    // 色號
	    JLabel lblColorID = new JLabel("色號");
	    lblColorID.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblColorID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.anchor = GridBagConstraints.WEST;
	    gridPanel.add(lblColorID, gbc);
	    //
	    JTextField tfColorID = new JTextField();
	    tfColorID.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfColorID.setText((String) rowData[3]);
	    tfColorID.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 3;
	    gridPanel.add(tfColorID, gbc);
	    // 顏色名稱
	    JLabel lblColorName = new JLabel("顏色名稱");
	    lblColorName.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblColorName.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 4;
	    gridPanel.add(lblColorName, gbc);
	    //
	    JTextField tfColorName = new JTextField();
	    tfColorName.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfColorName.setText((String) rowData[4]);
	    tfColorName.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 4;
	    gridPanel.add(tfColorName, gbc);
	    // 紗線編號
	    JLabel lblYarnID = new JLabel("紗線編號");
	    lblYarnID.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblYarnID.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 5;
	    gridPanel.add(lblYarnID, gbc);
	    //
	    JTextField tfYarnID = new JTextField();
	    tfYarnID.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfYarnID.setText((String) rowData[5]);
	    tfYarnID.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 5;
	    gridPanel.add(tfYarnID, gbc);
	    // 紗線規格
	    JLabel lblYarnSpecification = new JLabel("紗線編號");
	    lblYarnSpecification.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblYarnSpecification.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 6;
	    gridPanel.add(lblYarnID, gbc);
	    //
	    JTextField tfYarnSpecification = new JTextField();
	    tfYarnSpecification.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfYarnSpecification.setText((String) rowData[6]);
	    tfYarnSpecification.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 6;
	    gridPanel.add(tfYarnID, gbc);
	    // 重量
	    JLabel lblWeight = new JLabel("重量");
	    lblWeight.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblWeight.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 7;
	    gridPanel.add(lblWeight, gbc);
	    //    
	    float initialWeight = Float.parseFloat((String) rowData[7]);// 取得初始重量，並將 rowData[7] 轉換為浮點數
	    SpinnerNumberModel spinnerWeightModel = new SpinnerNumberModel(initialWeight, 0.0, 99999.0, 0.1);// 設定範圍為 0 到 99999，步進值為 0.1 
	    JSpinner spinnerWeight = new JSpinner(spinnerWeightModel);
	    spinnerWeight.setFont(new Font("微軟正黑體", Font.BOLD, 18));
	    JSpinner.NumberEditor editorWight = new JSpinner.NumberEditor(spinnerWeight, "0.0");// 設置 JSpinner 的 NumberEditor，並使用 DecimalFormat 格式 "0.0"
	    spinnerWeight.setEditor(editorWight);	    
	    spinnerWeight.setValue(initialWeight);// 設定初始值
        gbc.gridx = 1;
	    gbc.gridy = 7;
	    gridPanel.add(spinnerWeight, gbc);
	    // 粒數
	    JLabel lblGrains = new JLabel("粒數");
	    lblGrains.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblGrains.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 8;
	    gridPanel.add(lblGrains, gbc);
	    //
	    float initialGrains = Float.parseFloat((String) rowData[8]);// 取得初始重量，並將 rowData[7] 轉換為浮點數
	    SpinnerNumberModel spinnerGrainsModel = new SpinnerNumberModel(initialGrains, 0.0, 99999.0, 0.1);// 設定範圍為 0 到 99999，步進值為 0.1 
	    JSpinner spinnerGrains = new JSpinner(spinnerGrainsModel);
	    spinnerGrains.setFont(new Font("微軟正黑體", Font.BOLD, 18));
	    JSpinner.NumberEditor editorGrains = new JSpinner.NumberEditor(spinnerGrains, "0.0");// 設置 JSpinner 的 NumberEditor，並使用 DecimalFormat 格式 "0.0"
	    spinnerGrains.setEditor(editorGrains);	    
	    spinnerGrains.setValue(initialGrains);// 設定初始值
        gbc.gridx = 1;
	    gbc.gridy = 8;
	    gridPanel.add(spinnerGrains, gbc);
	    // 金額
	    JLabel lblAmount = new JLabel("金額");
	    lblAmount.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblAmount.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 9;
	    gridPanel.add(lblAmount, gbc);
	    // 創建金額格式，最多12位整數和2位小數
	    NumberFormat amountFormat = NumberFormat.getNumberInstance();
	    amountFormat.setMaximumIntegerDigits(12); // 最大12位整數
	    amountFormat.setMinimumFractionDigits(2); // 最少2位小數
	    amountFormat.setMaximumFractionDigits(2); // 最多2位小數
	    // 確保 rowData[9] 是 String，並轉換為 Double
	    double initialAmount = Double.parseDouble((String) rowData[9]);
	    // 使用這個格式來創建 JFormattedTextField
	    JFormattedTextField ftfAmount = new JFormattedTextField(amountFormat);
	    ftfAmount.setValue(initialAmount); // 設定初始值為 0.00
	    ftfAmount.setColumns(14); // 設定欄位大小
	    ftfAmount.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    // 將元件添加到界面
	    gbc.gridx = 1;
	    gbc.gridy = 9;
	    gridPanel.add(ftfAmount, gbc);
	    // 對色光源
	    JLabel lblOppositeColorLightSource = new JLabel("對色光源");
	    lblOppositeColorLightSource.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblOppositeColorLightSource.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 10;
	    gridPanel.add(lblOppositeColorLightSource, gbc);
	    //
	    JTextField tfOppositeColorLightSource = new JTextField();
	    tfOppositeColorLightSource.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfOppositeColorLightSource.setText((String) rowData[10]);
	    tfOppositeColorLightSource.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 10;
	    gridPanel.add(tfOppositeColorLightSource, gbc);
	    // 預計交貨日期
	    JLabel lblEstimatedDeliveryDate = new JLabel("預計交貨日期");
	    lblEstimatedDeliveryDate.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblEstimatedDeliveryDate.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 11;
	    gridPanel.add(lblEstimatedDeliveryDate, gbc);
	    // 日期模型
	    UtilDateModel dmDeliveryDate = new UtilDateModel();
	    Properties deliveryDateProperties = new Properties();
	    JDatePanelImpl datePanelDelivery = new JDatePanelImpl(dmDeliveryDate, deliveryDateProperties);
	    JDatePickerImpl dpDeliveryDate = new JDatePickerImpl(datePanelDelivery, null);
	    // 設置外觀
	    dpDeliveryDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
	    dpDeliveryDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
	    dpDeliveryDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 18));
	    dpDeliveryDate.setButtonFocusable(false);
	    // 設置初始日期（假設 rowData[11] 是字串格式的日期）
	    if (rowData[11] != null) {
	        dpDeliveryDate.getJFormattedTextField().setText((String) rowData[11]);
	    }
	    // 監聽日期選擇變化
	    dmDeliveryDate.addPropertyChangeListener(new PropertyChangeListener() 
	    {
	        public void propertyChange(PropertyChangeEvent evt) {
	            if (evt.getPropertyName().equals("value")) {
	                Object source = evt.getSource();
	                if (source instanceof UtilDateModel) {
	                    UtilDateModel model = (UtilDateModel) source;
	                    Date selectedDate = (Date) model.getValue();
	                    
	                    // 日期格式化
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                    String formattedDate = selectedDate != null ? dateFormat.format(selectedDate) : "";

	                    // 更新文字框內容
	                    dpDeliveryDate.getJFormattedTextField().setText(formattedDate);
	                }
	            }
	        }
	    });
	    gbc.gridx = 1;
	    gbc.gridy = 11;
	    gridPanel.add(dpDeliveryDate, gbc);
	    // 完成日期
	    JLabel lblCompletionDate = new JLabel("完成日期");
	    lblCompletionDate.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblCompletionDate.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 12;
	    gridPanel.add(lblCompletionDate, gbc);

	    // 日期模型
	    UtilDateModel dmCompletionDate = new UtilDateModel();
	    Properties completionDateProperties = new Properties();
	    JDatePanelImpl datePanelCompletion = new JDatePanelImpl(dmCompletionDate, completionDateProperties);
	    JDatePickerImpl dpCompletionDate = new JDatePickerImpl(datePanelCompletion, null);

	    // 設置外觀
	    dpCompletionDate.getJFormattedTextField().setBackground(new Color(255, 255, 255));
	    dpCompletionDate.getJFormattedTextField().setHorizontalAlignment(SwingConstants.CENTER);
	    dpCompletionDate.getJFormattedTextField().setFont(new Font("Arial", Font.BOLD, 18));
	    dpCompletionDate.setButtonFocusable(false);

	    // 設置初始日期（假設 rowData[12] 是字串格式的日期）
	    if (rowData[12] != null) {
	        dpCompletionDate.getJFormattedTextField().setText((String) rowData[12]);
	    }

	    // 監聽日期選擇變化
	    dmCompletionDate.addPropertyChangeListener(new PropertyChangeListener() {
	        public void propertyChange(PropertyChangeEvent evt) {
	            if (evt.getPropertyName().equals("value")) {
	                Object source = evt.getSource();
	                if (source instanceof UtilDateModel) {
	                    UtilDateModel model = (UtilDateModel) source;
	                    Date selectedDate = (Date) model.getValue();
	                    
	                    // 日期格式化
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                    String formattedDate = selectedDate != null ? dateFormat.format(selectedDate) : "";

	                    // 更新文字框內容
	                    dpCompletionDate.getJFormattedTextField().setText(formattedDate);
	                }
	            }
	        }
	    });
	    gbc.gridx = 1;
	    gbc.gridy = 12;
	    gridPanel.add(dpCompletionDate, gbc);
	    // 後處理方式
	    JLabel lblPostProcessMethod = new JLabel("後處理方式");
	    lblPostProcessMethod.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblPostProcessMethod.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 13;
	    gridPanel.add(lblPostProcessMethod, gbc);
	    //
	    JTextField tfPostProcessMethod = new JTextField();
	    tfPostProcessMethod.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfPostProcessMethod.setText((String) rowData[13]);
	    tfPostProcessMethod.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 13;
	    gridPanel.add(tfPostProcessMethod, gbc);
	    // 備註
	    JLabel lblRemark = new JLabel("備註");
	    lblRemark.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    lblRemark.setForeground(Color.WHITE);
	    gbc.gridx = 0;
	    gbc.gridy = 14;
	    gridPanel.add(lblRemark, gbc);
	    //
	    JTextField tfRemark = new JTextField();
	    tfRemark.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
	    tfRemark.setText((String) rowData[14]);
	    tfRemark.setPreferredSize(fixedSize);
	    gbc.gridx = 1;
	    gbc.gridy = 14;
	    gridPanel.add(tfRemark, gbc);
	        
	    
	    // 取消按鈕
	    JButton btnCancel = new JButton("取消");
	    btnCancel.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnCancel.setBackground(Color.WHITE);
	    btnCancel.setForeground(Color.BLACK);
	    btnCancel.setFocusPainted(false);
	    btnCancel.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                cancel();
            }
			private void cancel() 
			{
				// TODO Auto-generated method stub
				try 
				{
					Connection conn = DriverManager.getConnection(url, username, password);
					String sql = "UPDATE 訂單品項明細表 SET `狀態` = '取消' WHERE `訂單編號` = ?  AND `品號` = ?";
					
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, orderId);
					stmt.setString(2, (String)rowData[0]);
					stmt.executeUpdate();
					stmt.close();
					conn.close();
					dataFrame.dispose();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"取消訂單時發生錯誤：" + e.getMessage(),"了解");
				}
			}
        });
	    // 變更按鈕
	    JButton btnChange = new JButton("變更");
	    btnChange.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnChange.setBackground(Color.WHITE);
	    btnChange.setForeground(Color.BLACK);
	    btnChange.setFocusPainted(false);
	    btnChange.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                change();
            }
			private void change() 
			{
				// TODO Auto-generated method stub
				try 
				{
					String colorType = (String) cbColorType.getSelectedItem();
					String colorID = tfColorID.getText().trim();
					String colorName = tfColorName.getText().trim();
					String yarnID = tfYarnID.getText().trim();
					String yarnSpecification = tfYarnSpecification.getText().trim();
					Object weightValue = spinnerWeight.getValue();
	        		String weight = String.valueOf(weightValue); 
	        		Object grainsValue = spinnerGrains.getValue();
	        		String grains = String.valueOf(grainsValue); 
	        		String amount = ftfAmount.getText().trim();
	        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                String deliveryDate = (dpDeliveryDate.getModel().getValue() != null) ? sdf.format((Date) dpDeliveryDate.getModel().getValue()) : null;
	                String completionDate = (dpCompletionDate.getModel().getValue() != null) ? sdf.format((Date) dpCompletionDate.getModel().getValue()) : null;
	                String postProcessMethod = tfPostProcessMethod.getText().trim();
	                String remark = tfRemark.getText().trim();
					Connection conn = DriverManager.getConnection(url, username, password);
					String sql = "UPDATE 訂單品項明細表 SET `顏色類型` = ?, `色號` = ?, `顏色名稱` = ?, `紗線編號` = ?, `紗線規格` = ?, `重量` = ?, `粒數` = ?, `金額` = ?, `預計交貨日期` = ?, `完成日期` = ?, `後處理方式` = ?, `備註` = ? WHERE `訂單編號` = ?  AND `品號` = ?";
					
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, colorType);
					if (colorID.equals(""))
					{
						stmt.setNull(2, java.sql.Types.VARCHAR);// 如果值非空，進行轉換
                    } 
					else
                    {
                    	stmt.setString(2, colorID); // 插入有效的色號
                    }
					stmt.setString(3, colorName);
					if (yarnID.equals("")) 
					{
					    stmt.setNull(4, java.sql.Types.INTEGER);
					} 
					else 
					{
					    stmt.setInt(4, Integer.parseInt(yarnID));
					}

					stmt.setString(5, yarnSpecification);
					stmt.setString(6, weight);
					stmt.setString(7, grains);
					stmt.setString(8, amount);
					if (amount.equals("0.0")) 
					{
					    stmt.setBigDecimal(8, BigDecimal.ZERO);
					} 
					else 
					{
					    stmt.setBigDecimal(8, new BigDecimal(amount));
					}
					stmt.setString(9, deliveryDate);
					stmt.setString(10, completionDate);
					stmt.setString(11, postProcessMethod);
					stmt.setString(12, remark);

					stmt.setString(13, orderId);
					stmt.setString(14, (String)rowData[0]);
					stmt.executeUpdate();
					stmt.close();
					conn.close();
					dataFrame.dispose();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"變更品項時發生錯誤：" + e.getMessage(),"了解");
				}
			}
        });
	    // 提交按鈕
	    JButton btnSubmit = new JButton("提交");
	    btnSubmit.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnSubmit.setBackground(Color.WHITE);
	    btnSubmit.setForeground(Color.BLACK);
	    btnSubmit.setFocusPainted(false);
	    btnSubmit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                submit();
            }
			private void submit() 
			{
				// TODO Auto-generated method stub
				try 
				{
					// 檢查所有必填欄位
					
					Connection conn = DriverManager.getConnection(url, username, password);
					String sql = "UPDATE 訂單品項明細表 SET `狀態` = '待拋轉' WHERE `訂單編號` = ?  AND `品號` = ?";
					
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, orderId);
					stmt.setString(2, (String)rowData[0]);
					stmt.executeUpdate();
					stmt.close();
					conn.close();
					dataFrame.dispose();
				} 
				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 顯示錯誤消息
                    OptionPaneTool.showMessageDialog(JOptionPane.WARNING_MESSAGE,"取消訂單時發生錯誤：" + e.getMessage(),"了解");
				}
				
			}
        });
	    
	    // 撤回草稿按鈕
	    JButton btnWithdrawDraft = new JButton("撤回草稿");
	    btnWithdrawDraft.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnWithdrawDraft.setBackground(Color.WHITE);
	    btnWithdrawDraft.setForeground(Color.BLACK);
	    btnWithdrawDraft.setFocusPainted(false);    
	    	    
	    // 完成按鈕
	    JButton btnFinish = new JButton("完成");
	    btnFinish.setFont(new Font("微軟正黑體", Font.BOLD, 16));
	    btnFinish.setBackground(Color.WHITE);
	    btnFinish.setForeground(Color.BLACK);
	    btnFinish.setFocusPainted(false);  
	    // 設置按鈕面板
	    JPanel buttonPanel = new JPanel(new FlowLayout());
	    buttonPanel.setOpaque(false); // 背景透明
	    
	    // 根據 rowData[1] 的值判斷顯示哪些按鈕
	    String initStatus = (String) rowData[1];
	    if (initStatus.equals("草稿")) 
	    {
	        buttonPanel.add(btnCancel);
	        buttonPanel.add(btnChange);
	        buttonPanel.add(btnSubmit);
	    } 
	    else if (initStatus.equals("待拋轉") || initStatus.equals("待處理")) 
	    {
	        buttonPanel.add(btnWithdrawDraft);
	    }
	    else if (initStatus.equals("待出貨")) 
	    {
	        buttonPanel.add(btnFinish);
	    }

	    // 使用 BorderLayout 來排版，將元素置於不同區域
	    JPanel mainPanel = new JPanel(new BorderLayout());
	    mainPanel.setOpaque(false); // 背景透明
	    mainPanel.add(closePanel, BorderLayout.NORTH); // 關閉按鈕在上方
	    mainPanel.add(gridPanel, BorderLayout.CENTER); // 表格在中間
	    mainPanel.add(buttonPanel, BorderLayout.SOUTH); // 按鈕區域在下方

	    // 設置 frame 的內容
	    dataFrame.setContentPane(mainPanel);

	    // 顯示視窗
	    dataFrame.setVisible(true);
		
	}


}
