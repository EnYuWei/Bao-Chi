package Swing;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class GanttChartPanel extends JPanel {

    // 每頁顯示的機台數
    private static final int PAGE_SIZE = 9;
    // 當前頁數索引
    private int currentPage = 0;
    // 存儲機台與對應的甘特任務數據
    private final Map<String, List<GanttTask>> tasks = new HashMap<>();
    // 今天的日期
    private final LocalDate today = LocalDate.now();
    // 接下來的五個工作日
    private final LocalDate[] workDays = getNextFiveWorkDays(today);

    public GanttChartPanel() {
        // 設定面板大小，寬度 1200，高度自動調整
        setPreferredSize(new Dimension(1200, 600)); // 設置面板的尺寸
        setBackground(Color.WHITE); // 背景色設為白色
        initializeTasks(); // 初始化任務數據
    }

    // 計算從今天開始的未來五個工作日（排除週六和週日）
    private LocalDate[] getNextFiveWorkDays(LocalDate start) {
        LocalDate[] days = new LocalDate[5];
        int count = 0;
        LocalDate date = start;
        // 迭代日期，直到找到五個工作日
        while (count < 5) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days[count++] = date; // 如果是工作日，加入陣列
            }
            date = date.plusDays(1); // 日期加一天
        }
        return days;
    }

    // 初始化假設的機台與製令數據
    private void initializeTasks() {
        // 機台名稱列表
        String[] machines = {"S1", "S2", "S3", "S4", "A", "B", "E", "W", "N", "F", "G", "L", "H", "0", "1", "2", "3", "5", "6", "7", "C", "D", "J", "K", "4", "8", "9"};

        // 為每個機台生成隨機的製令
        for (String machine : machines) {
            List<GanttTask> taskList = new ArrayList<>();
            for (int i = 0; i < 3; i++) { // 每個機台有 3 個製令
                // 隨機生成開始日期和結束日期
                LocalDate taskStart = workDays[(int) (Math.random() * workDays.length)];
                LocalDate taskEnd = taskStart.plusDays((int) (Math.random() * 3) + 1);
                // 隨機生成顏色
                Color taskColor = new Color((int) (Math.random() * 0x1000000));
                // 將製令加入機台任務列表
                taskList.add(new GanttTask(taskStart, taskEnd, taskColor));
            }
            // 存儲每個機台的任務
            tasks.put(machine, taskList);
        }
    }

    // 重繪面板時調用
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGanttChart(g2d); // 繪製甘特圖
    }

    // 繪製甘特圖
    private void drawGanttChart(Graphics2D g2d) {
        int yStart = 50; // Y 軸起點
        int taskHeight = 20; // 任務條的高度
        int taskSpacing = 15; // 任務之間的間距
        int xStart = 200; // X 軸起點
        int xSpacing = 140; // 日期之間的間距

        // 設定字體和顏色，用於顯示日期
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        int xEnd = xStart + workDays.length * xSpacing; // X 軸終點

        // 繪製 X 軸線
        g2d.drawLine(xStart, getHeight() - 40, xEnd, getHeight() - 40);

        // 繪製 X 軸上的日期標記
        for (int i = 0; i < workDays.length; i++) {
            LocalDate currentDate = workDays[i];
            g2d.drawLine(xStart + i * xSpacing, getHeight() - 40, xStart + i * xSpacing, getHeight() - 40);
            g2d.drawString(currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth(), xStart + i * xSpacing + 5, getHeight() - 20);
        }

        // 繪製 Y 軸
        g2d.drawLine(xStart , yStart, xStart , getHeight() - 40);

        // 繪製機台標記
        String[] machines = {"S1", "S2", "S3", "S4", "A", "B", "E", "W", "N", "F", "G", "L", "H", "0", "1", "2", "3", "5", "6", "7", "C", "D", "J", "K", "4", "8", "9"};
        for (int i = 0; i < PAGE_SIZE && i + currentPage * PAGE_SIZE < machines.length; i++) {
            int taskIndex = currentPage * PAGE_SIZE + i;
            String machine = machines[taskIndex];
            g2d.drawString(machine, xStart -25 , yStart + i * (taskHeight + taskSpacing) + taskHeight / 2); // 顯示機台名稱
        }

        // 繪製每個機台的任務
        for (int i = 0; i < PAGE_SIZE && i + currentPage * PAGE_SIZE < machines.length; i++) {
            int taskIndex = currentPage * PAGE_SIZE + i;
            String machine = machines[taskIndex];
            List<GanttTask> taskList = tasks.get(machine);
            for (int j = 0; j < taskList.size(); j++) {
                GanttTask task = taskList.get(j);
                // 繪製每個任務的甘特條
                drawTask(g2d, task, xStart, yStart + i * (taskHeight + taskSpacing), taskHeight, xSpacing);
            }
        }
    }

    // 繪製單個甘特任務
    private void drawTask(Graphics2D g2d, GanttTask task, int xStart, int yPosition, int height, int xSpacing) {
        // 計算任務開始位置和寬度
        int startX = dateToX(task.getStartDate(), xStart, xSpacing);
        int width = dateToWidth(task.getStartDate(), task.getEndDate(), xSpacing);

        // 填充任務的顏色
        g2d.setColor(task.getColor());
        g2d.fillRect(startX, yPosition, width, height);

        // 繪製任務邊框
        g2d.setColor(Color.BLACK);
        g2d.drawRect(startX, yPosition, width, height);
    }

    // 計算日期在 X 軸的位置
    private int dateToX(LocalDate date, int xStart, int xSpacing) {
        int index = 0;
        for (LocalDate workDay : workDays) {
            if (date.equals(workDay)) {
                return xStart + index * xSpacing; // 將日期轉換為對應的 X 軸位置
            }
            index++;
        }
        return xStart + (workDays.length - 1) * xSpacing; // 如果日期不在工作日內，將其顯示在最後一天
    }

    // 計算任務的寬度（根據開始日期和結束日期）
    private int dateToWidth(LocalDate startDate, LocalDate endDate, int xSpacing) {
        int startIdx = getIndex(startDate);
        int endIdx = getIndex(endDate);
        return (endIdx - startIdx + 1) * xSpacing; // 計算寬度，以日期跨度為基礎
    }

    // 獲取日期在工作日陣列中的索引
    private int getIndex(LocalDate date) {
        for (int i = 0; i < workDays.length; i++) {
            if (date.equals(workDays[i])) {
                return i;
            }
        }
        return workDays.length - 1; // 如果日期不在工作日內，返回最後一天的索引
    }

    // 更新當前頁面並重新繪製
    public void updatePage(int pageIndex) {
        currentPage = pageIndex;
        repaint(); // 重新繪製面板
    }

    // 甘特任務的數據結構
    private static class GanttTask {
        private final LocalDate startDate; // 任務開始日期
        private final LocalDate endDate; // 任務結束日期
        private final Color color; // 任務的顏色

        public GanttTask(LocalDate startDate, LocalDate endDate, Color color) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.color = color;
        }

        // 獲取任務的開始日期
        public LocalDate getStartDate() {
            return startDate;
        }

        // 獲取任務的結束日期
        public LocalDate getEndDate() {
            return endDate;
        }

        // 獲取任務的顏色
        public Color getColor() {
            return color;
        }
    }
}

