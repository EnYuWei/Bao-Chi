package sms;

import javax.swing.*;
import java.awt.*;

public class Schedule 
{
    private JFrame frame;
    private JPanel panelContainer;  // 用於存放所有步驟的面板
    private CardLayout cardLayout;  // 用於切換面板的布局
    private int currentStep = 1;    // 當前步驟
    private JButton btnNext;        // 「下一步」按鈕
    private JButton btnPrev;        // 「上一步」按鈕
    
    public Schedule() 
    {
        frame = new JFrame("執行排程程序");
        frame.setBounds(0, 0, 1000, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 設定 CardLayout
        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout);

        // 步驟 1 到 3 的面板
        JPanel step1Panel = createStepPanel("Step 1: 確認可用機台及待排製令");
        JPanel step2Panel = createStepPanel("Step 2: 分配製令至相應機群");
        JPanel step3Panel = createStepPanel("Step 3: 輸出排程結果");

        // 將步驟面板加入到 CardLayout 的容器中
        panelContainer.add(step1Panel, "Step1");
        panelContainer.add(step2Panel, "Step2");
        panelContainer.add(step3Panel, "Step3");

        // 底部導航按鈕
        btnPrev = new JButton("上一步");
        btnPrev.setBackground(new Color(255, 255, 255));
        btnPrev.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        btnPrev.setFocusPainted(false);
        btnPrev.addActionListener(e -> goToPreviousStep());
        
        btnPrev.setVisible(false);  // 初始化時隱藏「上一步」按鈕

        btnNext = new JButton("下一步");
        btnNext.setBackground(new Color(255, 255, 255));
        btnNext.setFont(new Font("微軟正黑體", Font.BOLD, 16));
        btnNext.setFocusPainted(false);
        btnNext.addActionListener(e -> goToNextStep());

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setHgap(55);
        buttonPanel.add(btnPrev);
        buttonPanel.add(btnNext);

        // 將面板和按鈕加入到框架中
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panelContainer, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }

    // 創建每個步驟的面板
    private JPanel createStepPanel(String stepText) 
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(stepText);
        panel.add(label);
        return panel;
    }

    // 切換到下一步
    private void goToNextStep() 
    {
        if (currentStep < 3) 
        {
            currentStep++;
            cardLayout.show(panelContainer, "Step" + currentStep);
            
            if (currentStep == 3) {
                btnNext.setText("完成");
                btnNext.removeActionListener(btnNext.getActionListeners()[0]); // 移除原有的事件處理
                btnNext.addActionListener(e -> frame.dispose()); // 設定「完成」按鈕的動作
            }
            
            // 當不在第一步時，顯示「上一步」按鈕
            if (currentStep > 1) {
                btnPrev.setVisible(true);
            }
        }
    }

    // 切換到上一步
    private void goToPreviousStep() 
    {
        if (currentStep > 1) 
        {
            currentStep--;
            cardLayout.show(panelContainer, "Step" + currentStep);

            if (currentStep < 3) {
                btnNext.setText("下一步");
                btnNext.removeActionListener(btnNext.getActionListeners()[0]); // 移除「完成」的事件處理
                btnNext.addActionListener(e -> goToNextStep()); // 回復到「下一步」的動作
            }

            // 如果回到第一步，隱藏「上一步」按鈕
            if (currentStep == 1) {
                btnPrev.setVisible(false);
            }
        }
    }
}


