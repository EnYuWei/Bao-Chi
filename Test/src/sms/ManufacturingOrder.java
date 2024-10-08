package sms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;

public class ManufacturingOrder
{
	//GUI
	public JPanel ManufacturingOrderManu;
	private JLabel lblSchema;
	
	public ManufacturingOrder() 
	{
		// 主框架
		ManufacturingOrderManu = new JPanel();
		ManufacturingOrderManu.setLayout(null);

		// 表格名稱
		lblSchema = new JLabel("製令單");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(53, 88, 185, 67);
		ManufacturingOrderManu.add(lblSchema);     
		
		JButton btnScheduling = new JButton("開始排程");
		btnScheduling.setBackground(new Color(255, 255, 255));
		btnScheduling.setFont(new Font("微軟正黑體", Font.BOLD, 16));
		btnScheduling.setBounds(811, 483, 127, 42);
		btnScheduling.setFocusPainted(false);
		btnScheduling.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	new Schedule();
            }
        });
		ManufacturingOrderManu.add(btnScheduling);
		
	}
	
	// 回傳主框架(之後要添加於選項卡)
	public JPanel getPanel() 
	{
        return ManufacturingOrderManu;
    }
}
