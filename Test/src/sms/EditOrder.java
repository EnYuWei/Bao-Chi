package sms;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class EditOrder extends OrderOperation 
{
	private JButton btnSave;
    
//    public EditOrder(String orderId) 
//    {
////    	super(orderId);
//        
//        frame.setTitle("編輯訂單");
//        tfClient.setEditable(true);
//        tfMiddlemen.setEditable(true);
//        taDeliveryAddress.setEditable(true);
//        // 建議字詞
// 		SuggestionTool sugClient = new SuggestionTool("訂單紀錄表","客戶", url, username, password);
// 		SuggestionTool sugMiddlemen = new SuggestionTool("訂單紀錄表","代訂廠商", url, username, password);
// 		SuggestionTool sugDeliveryAddress = new SuggestionTool("訂單紀錄表","出貨地址", url, username, password);
// 		sugClient.addTextComponent(tfClient);
// 		sugMiddlemen.addTextComponent(tfMiddlemen);
// 		sugDeliveryAddress.addTextComponent(taDeliveryAddress);
//        
//       
// 		// 表格 hover
// 		table.setEnabled(true);
//
//	 	// 自定義渲染器類
//	 	class CustomTableCellRenderer extends DefaultTableCellRenderer {
//	 	    private int hoveredRow = -1; // 儲存滑鼠懸停的行索引
//	 	    
//	 	    @Override
//	 	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//	 	                                                   boolean hasFocus, int row, int column) {
//	 	        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//	
//	 	        // 設定選中狀態的背景顏色
//	 	        if (isSelected) {
//	 	            cellComponent.setBackground(Color.LIGHT_GRAY); // 選中行的背景色
//	 	        } 
//	 	        // 設定懸停行的背景顏色
//	 	        else if (row == hoveredRow) {
//	 	            cellComponent.setBackground(Color.LIGHT_GRAY); // 懸停行的背景色
//	 	        } 
//	 	        else {
//	 	            cellComponent.setBackground(Color.WHITE); // 其他行的背景色
//	 	        }
//	 	        // 設定文字垂直和水平對齊
//                ((JLabel) cellComponent).setHorizontalAlignment(SwingConstants.CENTER);
//                ((JLabel) cellComponent).setVerticalAlignment(SwingConstants.CENTER);
//
//	 	        return cellComponent;
//	 	    }
//	
//	 	    // 設定懸停行
//	 	    public void setHoveredRow(int row) {
//	 	        this.hoveredRow = row;
//	 	    }
//	 	}
//	
//	 	// 創建一個自定義的 Renderer 實例
//	 	CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer();
//	 	// 確保每一列都應用渲染器
//	 	for (int i = 0; i < table.getColumnCount(); i++) {
//	 	    table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
//	 	}
//	
//	 	// 滑鼠懸停事件：監聽滑鼠移動並更新懸停的行
//	 	table.addMouseMotionListener(new MouseMotionAdapter() {
//	 	    @Override
//	 	    public void mouseMoved(MouseEvent e) {
//	 	        int row = table.rowAtPoint(e.getPoint());
//	 	        if (row != -1 && row != cellRenderer.hoveredRow) {
//	 	            cellRenderer.setHoveredRow(row); // 更新懸停行
//	 	            table.repaint(); // 重新繪製表格以更新效果
//	 	        }
//	 	    }
//	 	});
//	
//	 	// 滑鼠移出事件：當滑鼠移出表格時重設懸停行
//	 	table.addMouseListener(new MouseAdapter() 
//	 	{
//	 	    @Override
//	 	    public void mouseExited(MouseEvent e) 
//	 	    {
//	 	        cellRenderer.setHoveredRow(-1); // 當滑鼠移出表格時將懸停行設為 -1
//	 	        table.repaint(); // 重新繪製表格以取消懸停效果
//	 	    }
//	
//	 	   @Override
//	 	  public void mouseClicked(MouseEvent e) 
//	 	   {
//	 	      int row = table.rowAtPoint(e.getPoint()); // 使用 mouse event 的位置來獲取行索引
//	 	      
//	 	      if (row != -1) { // 確認點擊了有效的行
//	 	          // 獲取該行的所有資料
//	 	          Object[] rowData = new Object[table.getColumnCount()];
//	 	          for (int col = 0; col < table.getColumnCount(); col++) 
//	 	          {
//	 	              rowData[col] = table.getValueAt(row, col); // 獲取每一列的資料
//	 	          }
//	 	          
//	 	          // 將整行資料傳入 operate 方法
//	 	          new OrderOperation(rowData,orderId); 
//
//	 	      }
//	 	  }
//
//	 	});
//
//
//	    // 儲存按鈕
//	    btnSave = new JButton("儲存");
//	    btnSave.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//	    btnSave.setBounds(891, 549, 71, 23);
//	    btnSave.setBackground(Color.WHITE);
//	    btnSave.setFocusPainted(false);
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
//        		
//        	}
//        });
//	    
//	    frame.getContentPane().add(btnSave);
//        frame.setVisible(true);
//    }
//     

}
