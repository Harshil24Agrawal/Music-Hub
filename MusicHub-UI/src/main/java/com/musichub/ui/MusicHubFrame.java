package com.musichub.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;


public class MusicHubFrame extends JFrame {

    // --- Design Constants ---
    private static final Color DARK_BG = new Color(20, 20, 20); // #141414 - Base
    private static final Color CARD_BG = new Color(35, 35, 35); // Card Background
    private static final Color ACCENT_GREEN = new Color(30, 215, 96); // Spotify Green - Primary Accent
    private static final Color ACCENT_RED = new Color(255, 100, 100); // Recommendation Red - Secondary Accent
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SUBTITLE_COLOR = new Color(150, 150, 150); // Gray for secondary text
    private static final Font TITLE_FONT = new Font("Inter", Font.BOLD, 36); // Made title larger
    private static final Font BUTTON_FONT = new Font("Inter", Font.BOLD, 16);
    private static final Font CONTENT_FONT = new Font("Inter", Font.PLAIN, 14); // New standard content font

    // API Configuration
    private static final String API_BASE_URL = "http://localhost:8080/api";

    public MusicHubFrame() {
        // --- 1. Frame Setup (Main Dashboard) ---
        setTitle("MusicHub | Platform Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);

        // --- 2. Styling (Minimalist look and feel) ---
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("TitledBorder.titleColor", ACCENT_GREEN);
        UIManager.put("TitledBorder.font", new Font("Inter", Font.BOLD, 14));
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Button.background", CARD_BG);
        UIManager.put("Button.foreground", TEXT_COLOR);
        // Apply improved default fonts
        UIManager.put("TextArea.font", CONTENT_FONT);
        UIManager.put("List.font", CONTENT_FONT);
        UIManager.put("TextField.font", CONTENT_FONT);
        UIManager.put("ScrollPane.background", DARK_BG);


        // --- 3. Title Panel (North) - SIMPLIFIED ---
        JLabel titleLabel = new JLabel("MusicHub: Java Full Stack", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ACCENT_GREEN);
        titleLabel.setBorder(new EmptyBorder(70, 0, 70, 0)); // Increased padding for clean space

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_BG);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // --- 4. Feature Buttons (Center) ---
        JPanel buttonPanel = createFeaturePanel();
        add(buttonPanel, BorderLayout.CENTER);

        // --- 5. Status (South) ---
        JLabel statusLabel = new JLabel("Backend: Spring Boot. Frontend: Java Swing.", SwingConstants.CENTER);
        statusLabel.setForeground(SUBTITLE_COLOR);
        statusLabel.setBorder(new EmptyBorder(10, 0, 20, 0)); // Increased bottom padding
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createFeaturePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 20, 20));
        panel.setBackground(DARK_BG);
        panel.setBorder(new EmptyBorder(20, 50, 50, 50));

        // 1. Search Feature
        JButton searchButton = createStyledButton("1. Search (UI -> API -> DB/External)", ACCENT_GREEN);
        searchButton.addActionListener(e -> new SearchWindow().setVisible(true));
        panel.add(searchButton);

        // 2. Recommendation Engine
        JButton recButton = createStyledButton("2. Recommendations (Engine Logic)", ACCENT_RED);
        recButton.addActionListener(e -> new RecommendationWindow().setVisible(true));
        panel.add(recButton);

        // 3. User Persistence
        JButton loginButton = createStyledButton("3. User Persistence (MySQL)", CARD_BG);
        loginButton.addActionListener(e -> showMockDialog("User Management",
                "Authentication and user profiles stored in MySQL and managed via secure Spring Boot endpoints."));
        panel.add(loginButton);

        // 4. External API Showcase
        JButton apiButton = createStyledButton("4. Caching & External Data Flow", CARD_BG);
        apiButton.addActionListener(e -> showMockDialog("Data Layer Showcase",
                "Data is retrieved from fast MySQL Cache or fetched live from external APIs (iTunes, Genius)."));
        panel.add(apiButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(15, 25, 15, 25)); // Minimal border

        // Add subtle hover effect (optional but nice)
        Color finalBgColor = bgColor;
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Change background only
                button.setBackground(finalBgColor.brighter());
                // Ensure text remains WHITE (TEXT_COLOR)
                button.setForeground(TEXT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Restore background only
                button.setBackground(finalBgColor);
                // Ensure text remains WHITE (TEXT_COLOR)
                button.setForeground(TEXT_COLOR);
            }
        });
        return button;
    }

    private void showMockDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Set system L&F for better text rendering on some OSs
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) {/* Ignore */}
        SwingUtilities.invokeLater(MusicHubFrame::new);
    }

    // --- INTERNAL CLASS FOR SEARCH WINDOW (Minimalist Focus) ---
    private class SearchWindow extends JFrame {
        private final HttpClient httpClient = HttpClient.newBuilder().build();
        private JTextField searchField;
        private JList<String> resultsList;
        private JTextArea flowArea; // Renamed from actionOutputArea for clarity
        private JLabel statusLabel;

        public SearchWindow() {
            setTitle("MusicHub | Search (UI -> API -> DB)");
            setSize(1000, 600); // Increased width for clearer split panels
            setLocationRelativeTo(MusicHubFrame.this);
            getContentPane().setBackground(DARK_BG);

            setLayout(new BorderLayout(15, 15));

            // Top Panel (Search Input)
            JPanel topPanel = createSearchInputPanel();
            add(topPanel, BorderLayout.NORTH);

            // Center Panel (Results and Flow Explanation)
            JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createResultsListPanel(), createFlowExplanationPanel());
            centerSplit.setResizeWeight(0.5); // Equal split for clarity
            centerSplit.setBorder(new EmptyBorder(0, 15, 0, 15));
            add(centerSplit, BorderLayout.CENTER);

            // Bottom Status Bar
            statusLabel = new JLabel("Ready. Enter search query above.", SwingConstants.LEFT);
            statusLabel.setForeground(SUBTITLE_COLOR);
            statusLabel.setBorder(new EmptyBorder(10, 15, 10, 15));
            add(statusLabel, BorderLayout.SOUTH);
        }

        private JPanel createSearchInputPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(DARK_BG);
            panel.setBorder(new EmptyBorder(15, 15, 0, 15));

            searchField = new JTextField("e.g., Happy, Rock, Chill Beat...");
            searchField.setFont(new Font("Inter", Font.PLAIN, 18));
            searchField.setForeground(SUBTITLE_COLOR);
            searchField.setBackground(CARD_BG);
            searchField.setCaretColor(ACCENT_GREEN);
            searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(CARD_BG.brighter(), 1), new EmptyBorder(10, 10, 10, 10)
            ));

            searchField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (searchField.getText().startsWith("e.g.")) {
                        searchField.setText("");
                        searchField.setForeground(TEXT_COLOR);
                    }
                }
            });

            JButton searchButton = createStyledButton("Search", ACCENT_GREEN);
            searchButton.addActionListener(this::performSearch);

            panel.add(searchField, BorderLayout.CENTER);
            panel.add(searchButton, BorderLayout.EAST);

            return panel;
        }

        private JPanel createResultsListPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(DARK_BG);
            panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(CARD_BG, 1), "Search Results (Songs & Beats)",
                    0, 2, new Font("Inter", Font.BOLD, 14), ACCENT_GREEN
            ));

            resultsList = new JList<>(new String[]{"Enter a query and hit search..."});
            resultsList.setBackground(CARD_BG);
            resultsList.setForeground(TEXT_COLOR);
            resultsList.setSelectionBackground(ACCENT_GREEN.darker());
            resultsList.setFont(CONTENT_FONT.deriveFont(Font.PLAIN, 14f)); // Use consistent font

            panel.add(new JScrollPane(resultsList), BorderLayout.CENTER);
            return panel;
        }

        private JPanel createFlowExplanationPanel() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(DARK_BG);
            panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(ACCENT_GREEN.darker(), 1), "Architectural Data Flow",
                    0, 2, new Font("Inter", Font.BOLD, 14), ACCENT_GREEN
            ));

            // Output Area for detailed action feedback
            flowArea = new JTextArea("1. UI sends HTTP GET request to Spring Boot (/api/search).\n2. Spring Boot checks MySQL cache.\n3. If cache miss, Spring Boot calls external API (iTunes/Genius).\n4. Spring Boot saves result to MySQL and returns JSON to UI.");
            flowArea.setBackground(CARD_BG);
            flowArea.setForeground(TEXT_COLOR);
            flowArea.setEditable(false);
            flowArea.setFont(CONTENT_FONT.deriveFont(Font.PLAIN, 14f)); // Increased font size for visibility
            flowArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            flowArea.setLineWrap(true); // FIX: Enable line wrapping
            flowArea.setWrapStyleWord(true); // FIX: Wrap at word boundaries

            // Action Buttons
            JButton likeButton = createStyledButton("❤ Like Song (Track User Prefs)", ACCENT_RED);
            JButton detailButton = createStyledButton("▶ Get Details/Lyrics (API Fetch)", new Color(30, 144, 255));

            likeButton.addActionListener(e -> updateFlowArea("LIKES", "POST " + API_BASE_URL + "/users/like",
                    "✅ Success! Stored user preference in MySQL 'likes' table."));
            detailButton.addActionListener(e -> updateFlowArea("MEDIA", "FETCH iTunes/Genius API",
                    "✅ Spring Boot fetched full details/lyrics from external API (not cached)."));

            JPanel actionPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            actionPanel.setBackground(DARK_BG);
            actionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
            actionPanel.add(detailButton);
            actionPanel.add(likeButton);

            panel.add(new JScrollPane(flowArea), BorderLayout.CENTER);
            panel.add(actionPanel, BorderLayout.SOUTH);
            return panel;
        }

        private void updateFlowArea(String feature, String apiAction, String successMessage) {
            flowArea.setText(String.format("--- %s Feature Simulation ---\nAPI Action: %s\n", feature, apiAction));
            statusLabel.setText(String.format("Status: Executing API Call (%s)...", feature));

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    Thread.sleep(1500);
                    return successMessage;
                }

                @Override
                protected void done() {
                    try {
                        flowArea.append("\n" + get());
                        statusLabel.setText("Status: " + feature + " simulation complete. Architecture demonstrated.");
                    } catch (Exception ex) {
                        flowArea.append("\n❌ An unexpected error occurred: " + ex.getMessage());
                        statusLabel.setText("Status: Error during simulation.");
                    }
                }
            }.execute();
        }

        private void performSearch(ActionEvent e) {
            String query = searchField.getText().trim();
            if (query.isEmpty() || query.startsWith("e.g.")) {
                resultsList.setListData(new String[]{"Error: Please enter a valid search query."});
                return;
            }

            statusLabel.setText(String.format("Status: Searching for '%s' via Spring Boot API...", query));
            flowArea.setText(String.format("--- Search Data Flow ---\n1. UI sends GET %s/search?q=%s", API_BASE_URL, query));

            new SwingWorker<String[], Void>() {
                @Override
                protected String[] doInBackground() throws Exception {
                    Thread.sleep(1000); // Simulate network latency

                    String[] dataFlow = {
                            "\n2. Spring Boot checks MySQL cache...",
                            "\n3. Cache MISS! Spring Boot fetches from external APIs...",
                            "\n4. Spring Boot saves new data to MySQL 'songs' table...",
                            "\n5. Spring Boot returns aggregated JSON to Swing UI."
                    };
                    for(String step : dataFlow) {
                        flowArea.append(step);
                        Thread.sleep(500);
                    }

                    List<String> mockResults = Arrays.asList(
                            String.format("Song: 'Groove Machine' (Genre: %s) - [Source: MySQL Cache]", query),
                            String.format("Beat: 'Chill Focus Loop' (Mood: Relax) - [Source: MySQL Cache]"),
                            String.format("Song: 'Epic Rise' (Mood: Cinematic) - [Source: External iTunes API]"),
                            String.format("Beat: 'Hard Drumkit' (For Creators) - [Source: External FreeSound API]"),
                            "Song: 'Trending Hit' (Based on Search Logs) - [Source: MySQL Search Logs]"
                    );

                    return mockResults.toArray(new String[0]);
                }

                @Override
                protected void done() {
                    try {
                        String[] results = get();
                        resultsList.setListData(results);
                        statusLabel.setText("Status: Search complete. Displaying " + results.length + " results.");
                        flowArea.append("\n\n✅ Search successful. Data flow demonstrated.");
                    } catch (Exception ex) {
                        resultsList.setListData(new String[]{"Error loading search results: " + ex.getMessage()});
                        statusLabel.setText("Status: Search failed.");
                    }
                }
            }.execute();
        }
    }

    // --- INTERNAL CLASS FOR RECOMMENDATION WINDOW ---
    private class RecommendationWindow extends JFrame {
        private JTextArea logicArea;

        public RecommendationWindow() {
            setTitle("MusicHub | Recommendation Engine Analysis (Backend Logic)");
            setSize(750, 600);
            setLocationRelativeTo(MusicHubFrame.this);
            getContentPane().setBackground(DARK_BG);

            setLayout(new BorderLayout(15, 15));

            JLabel title = new JLabel("Engine Logic: Spring Boot Business Layer", SwingConstants.CENTER);
            title.setFont(TITLE_FONT.deriveFont(20f));
            title.setForeground(ACCENT_RED);
            title.setBorder(new EmptyBorder(15, 0, 10, 0));
            add(title, BorderLayout.NORTH);

            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createRecommendationList(), createLogicPanel());
            splitPane.setResizeWeight(0.4);
            splitPane.setBorder(new EmptyBorder(0, 15, 15, 15));
            add(splitPane, BorderLayout.CENTER);

            displayLogic();
        }

        private JPanel createRecommendationList() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(DARK_BG);
            panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(ACCENT_RED, 1),
                    "Results from Engine (Backend Query)",
                    0, 2, new Font("Inter", Font.BOLD, 14), ACCENT_RED
            ));

            String[] recs = {
                    "1. Song: 'Midnight Rain' (Mood: Sad/Chill) - [User Match]",
                    "2. Song: 'Fast Car' (Genre: Pop) - [User Match]",
                    "3. Beat: 'Chill LoFi Loop' (Mood: Relax) - [Creator Match]",
                    "4. Song: 'Trending Hit' (Based on Search Logs) - [Popularity Match]",
            };

            JList<String> recommendationList = new JList<>(recs);
            recommendationList.setBackground(CARD_BG);
            recommendationList.setForeground(TEXT_COLOR);
            recommendationList.setSelectionBackground(ACCENT_RED.darker());
            recommendationList.setFont(CONTENT_FONT.deriveFont(Font.PLAIN, 14f)); // Use consistent font
            panel.add(new JScrollPane(recommendationList), BorderLayout.CENTER);

            return panel;
        }

        private JPanel createLogicPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(DARK_BG);
            panel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(ACCENT_RED, 1),
                    "Technical Explanation (How Spring Boot Calculates)",
                    0, 2, new Font("Inter", Font.BOLD, 14), ACCENT_RED
            ));

            logicArea = new JTextArea();
            logicArea.setBackground(CARD_BG);
            logicArea.setForeground(TEXT_COLOR);
            logicArea.setEditable(false);
            logicArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            logicArea.setLineWrap(true); // FIX: Enable line wrapping
            logicArea.setWrapStyleWord(true); // FIX: Wrap at word boundaries
            logicArea.setFont(CONTENT_FONT.deriveFont(Font.PLAIN, 14f)); // Increased font size for visibility
            panel.add(new JScrollPane(logicArea), BorderLayout.CENTER);

            return panel;
        }

        private void displayLogic() {
            String logic =
                    "The **Recommendation Engine** runs entirely within the **Spring Boot Backend** (Business Logic Layer)."
                            + "\n\n1. **Input Data (from MySQL):**"
                            + "\n   - Engine analyzes user's Liked Songs and Search Logs (stored in the MySQL 'likes' and 'search_logs' tables)."
                            + "\n   - Extracts dominant Moods (e.g., 'Chill') and Genres (e.g., 'Pop')."
                            + "\n\n2. **Database Query (Spring Data JPA):**"
                            + "\n   - Executes a highly optimized custom repository query against the MySQL 'songs' and 'beats' tables."
                            + "\n   - Query example: `findByMoodInOrGenreInOrderByPopularityDesc`."
                            + "\n\n3. **Output:**"
                            + "\n   - The refined, filtered list of songs and beats is converted to **JSON** by Spring Boot."
                            + "\n   - This JSON is sent over HTTP to this **Java Swing UI** for display."
                            + "\n\nThis layered architecture ensures **scalability** and **clean separation** required for the project.";

            logicArea.setText(logic);
        }
    }
}
