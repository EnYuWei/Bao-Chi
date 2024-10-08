package Swing;

import java.awt.EventQueue;

public class main {

	public static void main(String[] args) {
		// 使用 EventQueue 確保 Swing 組件的線程安全
        EventQueue.invokeLater(() -> new SchedulingResult());
	}

}
