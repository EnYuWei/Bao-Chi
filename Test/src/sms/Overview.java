package sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class Overview
{
	private JFrame frame;
	private JTabbedPane tbp;
	private JPanel orderManagement;
	private JPanel productManagement;
	private JPanel clientManagement;
	private JPanel colorManagement;
	private JPanel machineManagement;
	private JPanel manufacturingOrderManagement;
	// 資料庫
	public static String url;
	public static String username;
	public static String password;
	public static Connection conn;
	public Overview()
	{
		url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
		username = "root";
		password = "1234";
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//視窗
		frame = new JFrame("排程管理系統");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        //變更視窗圖示
        ImageIcon arrowIcon = new ImageIcon("Img/dye.png");
        frame.setIconImage(arrowIcon.getImage());
        //選項卡
		tbp = new JTabbedPane();
		frame.add(tbp);
		//選項卡--訂單管理
		Order order= new Order();
		orderManagement = order.getPanel();
		tbp.addTab("訂單管理", orderManagement);
		//選項卡--客戶管理
		Client client= new Client();
		clientManagement = client.getPanel();
		tbp.addTab("客戶管理", clientManagement);
		//選項卡--產品管理
		Product product= new Product();
		productManagement = product.getPanel();
		tbp.addTab("產品管理", productManagement);
		//選項卡--顏色管理
		Colour color= new Colour();
		colorManagement = color.getPanel();
		tbp.addTab("顏色管理", colorManagement);
		//選項卡--製令管理
		ManufacturingOrder manufacturingOrder = new ManufacturingOrder();
		manufacturingOrderManagement = manufacturingOrder.getPanel();
		tbp.addTab("製令管理", manufacturingOrderManagement);
		//選項卡--機台管理
		Machine machine= new Machine();
		machineManagement = machine.getPanel();
		tbp.addTab("機台管理", machineManagement);
		
		// 設定預設選中的選項卡
		tbp.setSelectedIndex(0);
		
		
		
		frame.setVisible(true);
	}

}
