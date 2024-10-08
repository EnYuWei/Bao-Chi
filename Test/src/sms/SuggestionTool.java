package sms;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class SuggestionTool {
    // 用來存放建議項目的資料模型
    private DefaultListModel<String> listModel;
    // 用來顯示建議列表的彈出框
    private JPopupMenu suggestionPopup;
    // 建議項目列表
    private JList<String> suggestionList;
    // 目前綁定的輸入框
    private JTextField currentTextField;
    // 存放來自資料庫的所有建議字詞
    private Set<String> suggestions;

    /**
     * 建構函數，負責初始化建議工具和從資料庫獲取建議字詞
     * @param tableName 資料庫表格名稱
     * @param fieldName 資料庫中欄位名稱，用來獲取建議字詞
     * @param url 資料庫連接字串
     * @param username 資料庫的使用者名稱
     * @param password 資料庫的密碼
     */
    public SuggestionTool(String tableName, String fieldName, String url, String username, String password) {
        suggestions = new HashSet<>(); // 初始化存放建議字詞的集合
        initialize(); // 初始化 GUI 元素
        fetchSuggestionsFromDatabase(tableName, fieldName, url, username, password); // 從資料庫獲取建議字詞
    }

    /**
     * 初始化建議相關的 GUI 元素
     */
    private void initialize() {
        listModel = new DefaultListModel<>(); // 建立列表模型
        suggestionPopup = new JPopupMenu(); // 建立彈出式選單
        suggestionList = new JList<>(listModel); // 建立列表並綁定資料模型
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 設定為單一選擇
        suggestionList.setVisibleRowCount(10); // 設定顯示的列數為 10 列
        suggestionPopup.add(new JScrollPane(suggestionList)); // 在彈出選單中加入滾動條和列表
        
        // 當使用者滑鼠點擊選單中的項目時，更新輸入框的內容
        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedSuggestion = suggestionList.getSelectedValue(); // 取得選中的項目
                if (selectedSuggestion != null && currentTextField != null) {
                    currentTextField.setText(selectedSuggestion); // 更新輸入框
                    suggestionPopup.setVisible(false); // 隱藏彈出式選單
                }
            }
        });
        
        // 當使用者按下 Enter 鍵時，更新輸入框的內容
        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String selectedSuggestion = suggestionList.getSelectedValue();
                    if (selectedSuggestion != null && currentTextField != null) {
                        currentTextField.setText(selectedSuggestion);
                        suggestionPopup.setVisible(false);
                    }
                }
            }
        });
    }

    /**
     * 從資料庫中獲取建議字詞
     * @param tableName 資料表名稱
     * @param fieldName 欄位名稱
     * @param url 資料庫連接字串
     * @param username 資料庫使用者名稱
     * @param password 資料庫密碼
     */
    private void fetchSuggestionsFromDatabase(String tableName, String fieldName, String url, String username, String password) {
        String sql = "SELECT " + fieldName + " FROM " + tableName; // SQL 查詢語句

        // 使用 try-with-resources 自動關閉資料庫連線
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            
            // 遍歷結果集，將每個值加入 suggestions 集合
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    suggestions.add(rs.getString(i)); // 加入建議字詞
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 輸出 SQL 錯誤訊息
        }
    }

    /**
     * 綁定輸入框，使其能顯示建議的彈出框
     * @param textField 欲綁定的 JTextField
     */
    public void addTextComponent(JTextComponent textComponent) {
        textComponent.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || 
                    e.getKeyCode() == KeyEvent.VK_UP || 
                    e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }

                currentTextField = (JTextField) (textComponent instanceof JTextField ? textComponent : null); // 若為 JTextField 則設定
                String input = textComponent.getText(); // 取得使用者輸入的內容
                List<String> filteredSuggestions = updateSuggestionList(input);

                if (!input.isBlank() && !filteredSuggestions.isEmpty()) {
                    suggestionList.setListData(filteredSuggestions.toArray(new String[0])); // 更新建議列表
                    showSuggestionPopup(textComponent); // 顯示建議
                    suggestionList.setSelectedIndex(0); // 預設選擇第一項
                    SwingUtilities.invokeLater(textComponent::requestFocusInWindow); // 保持焦點
                } else {
                    suggestionPopup.setVisible(false); // 隱藏建議彈出框
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (suggestionPopup.isVisible()) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        int index = suggestionList.getSelectedIndex();
                        if (index < suggestionList.getModel().getSize() - 1) {
                            suggestionList.setSelectedIndex(index + 1);
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        int index = suggestionList.getSelectedIndex();
                        if (index > 0) {
                            suggestionList.setSelectedIndex(index - 1);
                        }
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        String selectedSuggestion = suggestionList.getSelectedValue();
                        if (selectedSuggestion != null) {
                            textComponent.setText(selectedSuggestion); // 更新 JTextComponent 的文本
                            suggestionPopup.setVisible(false);
                        }
                    }
                }
            }
        });
    }


    /**
     * 根據使用者輸入，過濾出符合的建議字詞
     * @param input 使用者的輸入內容
     * @return 符合條件的建議字詞列表
     */
    private List<String> updateSuggestionList(String input) {
        List<String> filteredSuggestions = new ArrayList<>();
        // 過濾符合輸入字首的建議字詞
        for (String suggestion : suggestions) {
            if (suggestion != null && suggestion.startsWith(input)) {
                filteredSuggestions.add(suggestion); // 加入符合條件的建議
            }
        }
        return filteredSuggestions;
    }

    /**
     * 顯示建議彈出框
     * @param textField 當前的輸入框
     */
    private void showSuggestionPopup(JTextComponent textComponent) {
        suggestionPopup.show(textComponent, 0, textComponent.getHeight()); // 在輸入框下方顯示彈出框
        suggestionPopup.setPopupSize(textComponent.getWidth(), 200); // 設定彈出框的寬度
    }

}








