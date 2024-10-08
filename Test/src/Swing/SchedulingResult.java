package Swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SchedulingResult {

    private JFrame frame;
    private GanttChartPanel chartPanel;
    private JPanel placeholderPanel;
    private Timer timer;
    private int currentPage = 0;
    private JPanel indicatorPanel;

    private final int AUTO_PLAY_INTERVAL = 3000; // 自動輪播時間間隔3秒
    private final int PAUSE_INTERVAL = 6000; // 點擊後暫停6秒

    public SchedulingResult() {
        frame = new JFrame("排程結果");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        placeholderPanel = new JPanel();
        placeholderPanel.setBounds(50, 50, 1070, 400);
        placeholderPanel.setBackground(Color.LIGHT_GRAY);
        frame.getContentPane().add(placeholderPanel);

        indicatorPanel = new JPanel();
        indicatorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        indicatorPanel.setBounds(50, 456, 1070, 44);
        frame.getContentPane().add(indicatorPanel);

        for (int i = 0; i < 3; i++) {
            JLabel indicator = new JLabel("•");
            indicator.setFont(new Font("Arial", Font.BOLD, 24));
            indicator.setForeground(Color.GRAY);
            indicator.setOpaque(true);
            indicator.setPreferredSize(new Dimension(20, 20));
            final int pageIndex = i;
            indicator.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    switchPage(pageIndex, PAUSE_INTERVAL); // 點擊後暫停6秒
                }
            });
            indicatorPanel.add(indicator);
        }

        updatePageIndicator();

        timer = new Timer(AUTO_PLAY_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPage((currentPage + 1) % 3, AUTO_PLAY_INTERVAL); // 自動輪播
            }
        });
        timer.start();

        frame.setSize(1200, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        initializeGanttChart();
    }

    private void initializeGanttChart() {
        chartPanel = new GanttChartPanel();
        chartPanel.setBounds(100, 100, 1000, 400);
        placeholderPanel.setLayout(new BorderLayout());
        placeholderPanel.add(chartPanel, BorderLayout.CENTER);
        placeholderPanel.revalidate();
        placeholderPanel.repaint();
    }

    private void switchPage(int page, int delay) {
        currentPage = page;
        chartPanel.updatePage(currentPage);
        updatePageIndicator();
        resetTimer(delay); // 重設計時器
    }

    private void updatePageIndicator() {
        for (int i = 0; i < indicatorPanel.getComponentCount(); i++) {
            JLabel indicator = (JLabel) indicatorPanel.getComponent(i);
            indicator.setForeground(i == currentPage ? Color.BLACK : Color.GRAY);
        }
    }

    private void resetTimer(int delay) {
        if (timer != null) {
            timer.stop(); // 先停止目前的計時器
        }
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPage((currentPage + 1) % 3, AUTO_PLAY_INTERVAL); // 自動切換到下一頁，恢復自動播放時間
            }
        });
        timer.setInitialDelay(delay); // 設置初始延遲
        timer.start(); // 重新啟動計時器
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SchedulingResult::new);
    }
}


