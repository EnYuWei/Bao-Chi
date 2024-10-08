package sms;

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
	public Overview()
	{
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
		order.startTimer(); 
		// 偵測選項卡變更事件
		tbp.addChangeListener(new ChangeListener() 
		{
		    @Override
		    public void stateChanged(ChangeEvent e) 
		    {
		        int selectedIndex = tbp.getSelectedIndex();
		        String selectedTab = tbp.getTitleAt(selectedIndex);

		        switch (selectedTab) 
		        {
		            case "訂單管理":
		                order.startTimer();  // 啟動訂單計時器
		                break;
		            case "客戶管理":
		                client.startTimer();  // 啟動客戶計時器
		                break;
		            case "產品管理":
		                product.startTimer(); // 啟動產品計時器
		                break;
		            case "顏色管理":
		                color.startTimer(); // 啟動顏色計時器		                
		                break;
		            default:
		                // 如果選擇任何選項卡，可以選擇停止所有計時器
		                order.stopTimer();
		                client.stopTimer();
		                product.stopTimer();
		                color.stopTimer();
		                break;
		        }
		    }
		});
		
		
		frame.setVisible(true);
	}

}
