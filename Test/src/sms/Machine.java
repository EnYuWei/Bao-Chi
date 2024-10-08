package sms;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Machine 
{
	//GUI
	public JPanel MachineManu;
	private JLabel lblSchema;
	private Timer timer;
	private CardLayout cardLayout; 
	private RoundedButton btnA;
	private RoundedButton btnB;
	private RoundedButton btnC;
	//連接資料庫
	private String url = "jdbc:mariadb://localhost:3306/SchedulingManagementSystem";
	private String username = "root";
	private String password = "1234";
	public Machine() 
	{
		// 主框架
		MachineManu = new JPanel();
		MachineManu.setLayout(null);
		
		// 表格名稱
		lblSchema = new JLabel("現場生產情形");
		lblSchema.setFont(new Font("微軟正黑體", Font.BOLD, 36));
		lblSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSchema.setBounds(53, 52, 216, 67);
		MachineManu.add(lblSchema);        
		
		// 卡片布局面板
        cardLayout = new CardLayout(0, 0);  // 初始化 CardLayout
        JPanel cardPanel = new JPanel(cardLayout);   
        cardPanel.setBounds(53, 127, 1170, 400);
        MachineManu.add(cardPanel);
        
        // 卡片A、B、C
        JPanel cardA = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel cardB = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JPanel cardC = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        cardPanel.add(cardA, "A");
        cardPanel.add(cardB, "B");
        cardPanel.add(cardC, "C");
        // 按鈕面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(453, 540, 370, 50);
        FlowLayout fl_buttonPanel = new FlowLayout();
        fl_buttonPanel.setHgap(20);
        buttonPanel.setLayout(fl_buttonPanel);
        MachineManu.add(buttonPanel);

        // 按鈕A
        btnA = new RoundedButton("組別A");
        btnA.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnA.setPreferredSize(new Dimension(90, 45));
        btnA.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnA.setBackground(new Color(112, 128, 144) ); // 移入時背景變為灰色
                btnA.setBorderColor(new Color(112, 128, 144) ); // 改變邊框顏色為灰色
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnA.setBackground(new Color(70, 130, 180)); // 移出時恢復為黑色
                btnA.setBorderColor(new Color(70, 130, 180)); // 邊框恢復為黑色
            }
        });

        // 按鈕A點擊事件
        btnA.addActionListener(e -> cardLayout.show(cardPanel, "A"));  // 切換到卡片A
        buttonPanel.add(btnA);

        // 按鈕B
        btnB = new RoundedButton("組別B");
        btnB.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnB.setPreferredSize(new Dimension(90, 45));
        btnB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	btnB.setBackground(new Color(112, 128, 144) ); // 移入時背景變為灰色
            	btnB.setBorderColor(new Color(112, 128, 144) ); // 改變邊框顏色為灰色
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	btnB.setBackground(new Color(70, 130, 180)); // 移出時恢復為黑色
            	btnB.setBorderColor(new Color(70, 130, 180)); // 邊框恢復為黑色
            }
            
        });

        // 按鈕B點擊事件
        btnB.addActionListener(e -> cardLayout.show(cardPanel, "B"));  // 切換到卡片B
        buttonPanel.add(btnB);

        // 按鈕C
        btnC = new RoundedButton("組別C");
        btnC.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        btnC.setPreferredSize(new Dimension(90, 45));
        btnC.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	btnC.setBackground(new Color(112, 128, 144) ); // 移入時背景變為灰色
            	btnC.setBorderColor(new Color(112, 128, 144) ); // 改變邊框顏色為灰色
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	btnC.setBackground(new Color(70, 130, 180)); // 移出時恢復為黑色
            	btnC.setBorderColor(new Color(70, 130, 180)); // 邊框恢復為黑色
            }
        });

        // 按鈕C點擊事件
        btnC.addActionListener(e -> cardLayout.show(cardPanel, "C"));  // 切換到卡片C
        buttonPanel.add(btnC);

        // 現在時間
        JLabel timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setLocation(1036, 10);
        timeLabel.setSize(224, 30);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));  // 設置字體
        timeLabel.setForeground(new Color(112, 128, 144));  // 設置字體顏色
        MachineManu.add(timeLabel);
        // 使用 DateTimeFormatter 格式化 LocalDateTime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        ActionListener updateClockAction = e -> {
            // 獲取當前時間並更新 JLabel
            String currentTime = dtf.format(LocalDateTime.now());
            timeLabel.setText(currentTime);
            
        };
        // 稼動率
        JLabel lblOperationRate = new JLabel("稼動率: ");
        lblOperationRate.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        lblOperationRate.setBounds(927, 94, 100, 25);
        MachineManu.add(lblOperationRate);
        // 當日工作完成率
        JLabel lblDailyTaskCompletionRate = new JLabel("當日工作完成率: ");
        lblDailyTaskCompletionRate.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        lblDailyTaskCompletionRate.setBounds(1045, 94, 178, 25);
        MachineManu.add(lblDailyTaskCompletionRate);
        // 綠色燈號
        JLabel lblStatusTip1 = new JLabel(" 工作中");
        lblStatusTip1.setIcon(new ImageIcon("Img/circle-green.png"));
        lblStatusTip1.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblStatusTip1.setBounds(883, 537, 64, 28);
        MachineManu.add(lblStatusTip1);
        // 紅色燈號
        JLabel lblStatusTip2 = new JLabel(" 停機");
        lblStatusTip2.setIcon(new ImageIcon("Img/circle-red.png"));
        lblStatusTip2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblStatusTip2.setBounds(979, 537, 50, 28);
        MachineManu.add(lblStatusTip2);
        // 黃色燈號
        JLabel lblStatusTip3 = new JLabel(" 待機");
        lblStatusTip3.setIcon(new ImageIcon("Img/circle-yellow.png"));
        lblStatusTip3.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblStatusTip3.setBounds(883, 560, 50, 28);
        MachineManu.add(lblStatusTip3);
        // 黃色燈號(閃爍)
        JLabel lblStatusTip4 = new JLabel("(閃爍) 清洗");
        lblStatusTip4.setIcon(new ImageIcon("Img/circle-yellow.png"));
        lblStatusTip4.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblStatusTip4.setBounds(979, 560, 83, 28);
        MachineManu.add(lblStatusTip4);
        // 定義計時器和時間間隔
        timer = new Timer(1000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	
//                reduceRemainingTime();  // 每秒減少剩餘時間
            	

            	updateMachinePanels(cardA, "A");
            	updateMachinePanels(cardB, "B");
            	updateMachinePanels(cardC, "C");
            	
                updateClockAction.actionPerformed(e);     // 更新時間顯示           
            }
        });
        // 啟動計時器
        timer.start();

	}
	// 根據資料庫的機台分組產生機台面板
	public JPanel updateMachinePanels(JPanel cardPanel, String group) 
	{
		cardPanel.removeAll();
        try 
        {
        	Connection conn = DriverManager.getConnection(url, username, password);       
            String sql = "SELECT "
            			+"	m.machine_code, m.status, TIME_FORMAT(m.remaining_time, '%H:%i:%s') AS remaining_time,"
            			+"  m.current_usage, c.L, c.C, c.H, m.group "
            			+"FROM machine AS m "
            			+ "LEFT JOIN color AS c ON m.current_color = c.id "  // 使用 LEFT JOIN，因為色號可能為空。
            			+"WHERE m.group = ? "
            			+"ORDER BY m.machine_code; ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, group); // 使用 PreparedStatement 設置參數
            ResultSet rs = stmt.executeQuery();

            // 遍歷結果集，動態生成機台面板
            while (rs.next()) 
            {
                String machineCode = rs.getString("machine_code");
                String status = rs.getString("status");
                String remainingTime = rs.getString("remaining_time");
                int usage = rs.getInt("current_usage");
                double lValue = rs.getDouble("L");
                double cValue = rs.getDouble("C");
                double hValue = rs.getDouble("H");

                // 動態創建機台面板
                JPanel machinePanel = createMachinePanel(machineCode, status, remainingTime, usage, lValue, cValue, hValue);
             // 設置右鍵點擊的監聽器
                machinePanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // 建立右鍵選單
                            JPopupMenu popupMenu = new JPopupMenu();
                            
                            // 設置選單字體和顏色
                            JMenuItem item1 = new JMenuItem("啟用");
                            item1.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//                            item1.addActionListener(evt -> updateMachineStatus(machineCode, "待機"));

                            JMenuItem item2 = new JMenuItem("停用");
                            item2.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//                            item2.addActionListener(evt -> updateMachineStatus(machineCode, "停機"));

                            JMenuItem item3 = new JMenuItem("清洗");
                            item3.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//                            item3.addActionListener(evt -> updateMachineCleaning(machineCode));

                            JMenuItem item4 = new JMenuItem("強制結束");
                            item4.setFont(new Font("微軟正黑體", Font.BOLD, 14));
//                            item4.addActionListener(evt -> updateMachineStatus(machineCode, "待機"));
                            
                            JMenuItem item5 = new JMenuItem("結束");
                            item5.setFont(new Font("微軟正黑體", Font.BOLD, 14));

                            JMenuItem item6 = new JMenuItem("查看內缸容量");
                            item6.setFont(new Font("微軟正黑體", Font.BOLD, 14));
                            // 根據機台狀態顯示不同選項
                            if (status.equals("停機")) {
                                popupMenu.add(item1);  // 啟用
                            } 
                            else if (status.equals("待機")) {
                                popupMenu.add(item2);  // 停用
                                popupMenu.add(item3);  // 清洗
                            } 
                            else if (status.equals("工作中")) {
                                popupMenu.add(item4);  // 強制結束
                            }
                            else{
                                popupMenu.add(item5);  // 結束清洗
                            }
                            popupMenu.add(item6);
                            // 設置圓角效果和陰影 並顯示
                            popupMenu.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 80), 2, true));  // 圓角
                            popupMenu.setPopupSize(new Dimension(120, popupMenu.getComponentCount() * 25 + 10)); // 設定大小
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                });
                cardPanel.add(machinePanel);  // 將每個面板添加到主面板中
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        cardPanel.revalidate();  // 刷新面板
        cardPanel.repaint();     // 重繪面板
        return cardPanel;  // 返回包含所有機台面板的主面板
    }
	// 每秒減少剩餘時間
	private void reduceRemainingTime() {
	    try (Connection conn = DriverManager.getConnection(url, username, password)) {
	        String sql = "UPDATE machine SET remaining_time = ADDTIME(remaining_time, '-00:00:01') " 
	        			+"WHERE status IN ('工作中', '清洗') AND remaining_time > '00:00:00'";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	// 自定義圓角按鈕
	public class RoundedButton extends JButton 
	{
	    private Color borderColor; // 新增邊框顏色屬性

	    public RoundedButton(String label) {
	        super(label);
	        setFont(new Font("Arial", Font.BOLD, 18));
	        setContentAreaFilled(false);
	        setOpaque(false);  // 確保透明背景以顯示圓角效果
	        setForeground(Color.WHITE);
	        setBackground(new Color(70, 130, 180));
	        setFocusPainted(false);
	        borderColor = new Color(70, 130, 180); // 初始化邊框顏色為黑色
	    }

	    // 設置邊框顏色的方法
	    public void setBorderColor(Color color) {
	        this.borderColor = color;
	        repaint(); // 重新繪製以更新顯示
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        // 使用 Graphics2D 以啟用抗鋸齒
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        // 填充圓角矩形
	        g2d.setColor(getBackground());
	        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // 設置圓角
	        super.paintComponent(g2d);  // 繪製文本
	    }

	    @Override
	    protected void paintBorder(Graphics g) {
	        // 使用 Graphics2D 以啟用抗鋸齒
	        Graphics2D g2d = (Graphics2D) g;
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        // 繪製圓角邊框
	        g2d.setColor(borderColor); // 使用動態邊框顏色
	        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40); // 圓角邊框
	    }
	}
	// 創建機台面板
	private JPanel createMachinePanel(String machineCode,String status, String remainingTime, int usage, double lValue, double cValue, double hValue) 
	{
	    // 創建 JLayeredPane 來管理不同的圖層
	    JLayeredPane layeredPane = new JLayeredPane();
	    layeredPane.setPreferredSize(new Dimension(140, 200));

	    // 機台圖片
	    JLabel machineImageLabel = new JLabel(new ImageIcon("Img/washing-machine.png"));
	    machineImageLabel.setBounds(20, 10, 100, 100); // 設定圖片的尺寸

	    // 機台代號文字
	    JLabel machineCodeLabel = new JLabel(machineCode, JLabel.CENTER);
	    machineCodeLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    machineCodeLabel.setForeground(new Color(248, 248, 255)); // 設定文字顏色
	    machineCodeLabel.setBounds(20, 15, 100, 100); // 設定文字在圖片的下方

	    // 根據狀態設置燈號
	    JLabel lblStatus = new JLabel();
	    
	    switch (status) 
	    {
	        case "工作中":
	            lblStatus.setIcon(new ImageIcon("Img/circle-green.png"));
	            break;
	        case "停機":
	            lblStatus.setIcon(new ImageIcon("Img/circle-red.png"));
	            break;
	        case "待機":
	            lblStatus.setIcon(new ImageIcon("Img/circle-yellow.png"));
	            break;
	        case "清洗":
	            lblStatus.setIcon(new ImageIcon("Img/circle-yellow.png"));
	            
	            // 使用 Timer 讓清洗狀態閃爍
	            Timer timer = new Timer(250, new ActionListener() {
	                boolean visible = true;
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    visible = !visible; // 切換狀態
	                    lblStatus.setVisible(visible);
	                }
	            });
	            timer.start();
	            break;
	    }
	    lblStatus.setBounds(87, 16, 10, 10);  // 燈號的位置

	    // 顯示剩餘工時、使用次數、L值、C值和H值
	    JLabel lblRemainingTime = new JLabel("剩餘工時: "+ remainingTime);
	    lblRemainingTime.setHorizontalAlignment(SwingConstants.CENTER);
	    lblRemainingTime.setFont(new Font("微軟正黑體 Light", Font.PLAIN, 12));
	    lblRemainingTime.setBounds(0, 115, 140, 15);
	    //
	    JLabel lblUsage = new JLabel("使用次數: " + usage);
	    lblUsage.setHorizontalAlignment(SwingConstants.CENTER);
	    lblUsage.setFont(new Font("微軟正黑體 Light", Font.PLAIN, 12));
	    lblUsage.setBounds(0, 130, 140, 15);
	    //
	    JLabel lblL = new JLabel("L值: " + lValue);
	    lblL.setHorizontalAlignment(SwingConstants.CENTER);
	    lblL.setFont(new Font("微軟正黑體 Light", Font.PLAIN, 12));
	    lblL.setBounds(0, 145, 140, 15);
	    //
	    JLabel lblC = new JLabel("C值: " + cValue);
	    lblC.setHorizontalAlignment(SwingConstants.CENTER);
	    lblC.setFont(new Font("微軟正黑體 Light", Font.PLAIN, 12));
	    lblC.setBounds(0, 160, 140, 15);
	    //
	    JLabel lblH = new JLabel("H值: " + hValue);
	    lblH.setHorizontalAlignment(SwingConstants.CENTER);
	    lblH.setFont(new Font("微軟正黑體 Light", Font.PLAIN, 12));
	    lblH.setBounds(0, 175, 140, 15);
	    //
//	    JLabel lblMachineUtilizationRate = new JLabel("稼動率: " );
//	    lblMachineUtilizationRate.setHorizontalAlignment(SwingConstants.CENTER);
//	    lblMachineUtilizationRate.setFont(new Font("微軟正黑體", Font.BOLD, 12));
//	    lblMachineUtilizationRate.setBounds(0, 190, 140, 10);
	    // 將元件加入 JLayeredPane
	    layeredPane.add(machineImageLabel, JLayeredPane.DEFAULT_LAYER); // 圖片在最底層
	    layeredPane.add(lblStatus, JLayeredPane.POPUP_LAYER);  // 放置燈號在更高層級
	    layeredPane.add(machineCodeLabel, JLayeredPane.PALETTE_LAYER);  // 文字在圖片上方一層
	    layeredPane.add(lblRemainingTime, JLayeredPane.PALETTE_LAYER);
	    layeredPane.add(lblUsage, JLayeredPane.PALETTE_LAYER);          // 使用次數
	    layeredPane.add(lblL, JLayeredPane.PALETTE_LAYER);              // L值
	    layeredPane.add(lblC, JLayeredPane.PALETTE_LAYER);              // C值
	    layeredPane.add(lblH, JLayeredPane.PALETTE_LAYER);              // H值
//	    layeredPane.add(lblMachineUtilizationRate, JLayeredPane.PALETTE_LAYER);  
	    // 創建一個 JPanel 來包含 layeredPane
	    JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout());
	    panel.add(layeredPane, BorderLayout.CENTER);  // 將 JLayeredPane 加入 JPanel
	    
	    return panel;  // 返回 JPanel 而不是 JLayeredPane
	}
	// 繪製圓角方框按鈕
	public BufferedImage getScaledImage(Image srcImg, int width, int height) {
	    
	    // 創建一個空的 BufferedImage
	    BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    // 設定縮放的品質
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

	    // 繪製縮放後的圖片
	    g2.drawImage(srcImg, 0, 0, width, height, null);

	    // 釋放資源
	    g2.dispose();

	    // 在這裡增加額外的抗鋸齒處理
	    return applyAntiAliasing(resizedImg);
	}

	// 附加的抗鋸齒處理方法
	private BufferedImage applyAntiAliasing(BufferedImage img) {
	    BufferedImage antiAliasedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = antiAliasedImg.createGraphics();
	    
	    // 設定抗鋸齒
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    
	    // 繪製抗鋸齒後的圖片
	    g2d.drawImage(img, 0, 0, null);
	    g2d.dispose();

	    return antiAliasedImg;
	}
	// 回傳主框架(之後要添加於選項卡)
	public JPanel getPanel() 
	{
        return MachineManu;
    }
}
