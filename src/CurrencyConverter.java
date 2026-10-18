import javax.swing.*;
import javax.swing.border.*;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class CurrencyConverter extends JFrame implements ActionListener {

    // UI Components
    private JTextField amountField, resultField;
    private JComboBox<String> fromCurrency, toCurrency;
    private JButton convertButton, swapButton, clearButton;
    private JLabel statusLabel, rateInfoLabel, lastUpdatedLabel;

    // Enhanced Theme
    private final Color PRIMARY = new Color(99, 102, 241);
    private final Color PRIMARY_DARK = new Color(79, 70, 229);
    private final Color SECONDARY = new Color(16, 185, 129);
    private final Color BG = new Color(249, 250, 251);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(31, 41, 55);
    private final Color TEXT_SECONDARY = new Color(107, 114, 128);
    private final Color BORDER = new Color(229, 231, 235);
    private final Color SUCCESS = new Color(16, 185, 129);
    private final Color ERROR = new Color(239, 68, 68);
    private final Color HOVER_BG = new Color(243, 244, 246);

    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 15);
    private final Font RESULT_FONT = new Font("Segoe UI", Font.BOLD, 24);

    private final String[] currencies = {
            "USD - US Dollar", "EUR - Euro", "GBP - British Pound", "JPY - Japanese Yen",
            "INR - Indian Rupee", "AUD - Australian Dollar", "CAD - Canadian Dollar",
            "CHF - Swiss Franc", "CNY - Chinese Yuan", "SGD - Singapore Dollar",
            "NZD - New Zealand Dollar", "ZAR - South African Rand", "KRW - South Korean Won",
            "MXN - Mexican Peso", "BRL - Brazilian Real", "HKD - Hong Kong Dollar"
    };

    private final String[] currencyCodes = {
            "USD", "EUR", "GBP", "JPY", "INR", "AUD", "CAD", "CHF",
            "CNY", "SGD", "NZD", "ZAR", "KRW", "MXN", "BRL", "HKD"
    };

    private final String[] currencySymbols = {
            "$", "€", "£", "¥", "₹", "A$", "C$", "CHF",
            "¥", "S$", "NZ$", "R", "₩", "$", "R$", "HK$"
    };

    public CurrencyConverter() {
        setTitle("Currency Converter Pro");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main background panel
        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        setContentPane(background);

        // Main content container
        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setOpaque(false);

        // Create card panel with shadow effect
        JPanel cardContainer = new JPanel(new BorderLayout());
        cardContainer.setBackground(CARD);
        cardContainer.setBorder(createShadowBorder());

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        cardContainer.add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = createContentPanel();
        cardContainer.add(contentPanel, BorderLayout.CENTER);

        // Footer panel
        JPanel footerPanel = createFooterPanel();
        cardContainer.add(footerPanel, BorderLayout.SOUTH);

        mainContainer.add(cardContainer);
        background.add(mainContainer, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 35, 20, 35));

        // Title section
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(CARD);
        GridBagConstraints gbc = new GridBagConstraints();

        // Icon
        JLabel iconLabel = new JLabel("💱");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.insets = new Insets(0, 0, 0, 15);
        titlePanel.add(iconLabel, gbc);

        // Title
        JLabel title = new JLabel("Currency Converter");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        titlePanel.add(title, gbc);

        // Subtitle
        JLabel subtitle = new JLabel("Real-time exchange rates • Fast & Accurate");
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(TEXT_SECONDARY);
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        titlePanel.add(subtitle, gbc);

        panel.add(titlePanel, BorderLayout.WEST);

        // Last updated label
        lastUpdatedLabel = new JLabel("Ready to convert");
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lastUpdatedLabel.setForeground(TEXT_SECONDARY);
        lastUpdatedLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panel.add(lastUpdatedLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 35, 30, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Amount input section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(createInputSection("Amount", amountField = createStyledTextField("0.00")), gbc);

        // From currency section
        gbc.gridy++;
        panel.add(createCurrencySection("From", fromCurrency = createStyledComboBox(), 0), gbc);

        // Swap button in the middle
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(createSwapButton(), gbc);

        // To currency section
        gbc.gridy++;
        panel.add(createCurrencySection("To", toCurrency = createStyledComboBox(), 1), gbc);

        // Exchange rate info
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 10, 0);
        rateInfoLabel = new JLabel(" ");
        rateInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rateInfoLabel.setForeground(TEXT_SECONDARY);
        rateInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(rateInfoLabel, gbc);

        // Convert button
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 15, 0);
        panel.add(createConvertButton(), gbc);

        // Result section
        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 8, 0);
        panel.add(createResultSection(), gbc);

        // Status label
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel, gbc);

        // Action buttons (Clear)
        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 0, 0);
        panel.add(createActionButtons(), gbc);

        return panel;
    }

    private JPanel createInputSection(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(CARD);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(LABEL_FONT);
        labelComponent.setForeground(TEXT);
        panel.add(labelComponent, BorderLayout.NORTH);

        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCurrencySection(String label, JComboBox<String> combo, int defaultIndex) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(CARD);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(LABEL_FONT);
        labelComponent.setForeground(TEXT);
        panel.add(labelComponent, BorderLayout.NORTH);

        combo.setSelectedIndex(defaultIndex);
        panel.add(combo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSwapButton() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(CARD);

        swapButton = new JButton("⇅");
        swapButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        swapButton.setForeground(PRIMARY);
        swapButton.setBackground(HOVER_BG);
        swapButton.setFocusPainted(false);
        swapButton.setBorderPainted(false);
        swapButton.setPreferredSize(new Dimension(50, 50));
        swapButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        swapButton.setToolTipText("Swap currencies");

        // Circular button
        swapButton.setBorder(new EmptyBorder(10, 10, 10, 10));
        swapButton.setContentAreaFilled(false);
        swapButton.setOpaque(true);

        swapButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                swapButton.setBackground(PRIMARY);
                swapButton.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                swapButton.setBackground(HOVER_BG);
                swapButton.setForeground(PRIMARY);
            }
        });

        swapButton.addActionListener(e -> {
            int fromIndex = fromCurrency.getSelectedIndex();
            fromCurrency.setSelectedIndex(toCurrency.getSelectedIndex());
            toCurrency.setSelectedIndex(fromIndex);
        });

        panel.add(swapButton);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return panel;
    }

    private JPanel createConvertButton() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD);

        convertButton = new JButton("Convert Currency");
        convertButton.setFont(BUTTON_FONT);
        convertButton.setBackground(PRIMARY);
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        convertButton.setBorderPainted(false);
        convertButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        convertButton.setPreferredSize(new Dimension(0, 50));
        convertButton.addActionListener(this);

        convertButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (convertButton.isEnabled()) {
                    convertButton.setBackground(PRIMARY_DARK);
                }
            }
            public void mouseExited(MouseEvent e) {
                convertButton.setBackground(PRIMARY);
            }
        });

        panel.add(convertButton, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultSection() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(new Color(240, 253, 244));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(167, 243, 208), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel("Converted Amount");
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT);
        panel.add(label, BorderLayout.NORTH);

        resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setFont(RESULT_FONT);
        resultField.setForeground(SUCCESS);
        resultField.setBackground(new Color(240, 253, 244));
        resultField.setBorder(null);
        resultField.setHorizontalAlignment(JTextField.CENTER);
        resultField.setText("0.00");

        panel.add(resultField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(CARD);

        clearButton = new JButton("Clear All");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clearButton.setForeground(TEXT_SECONDARY);
        clearButton.setBackground(HOVER_BG);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setPreferredSize(new Dimension(120, 35));

        clearButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                clearButton.setBackground(new Color(229, 231, 235));
            }
            public void mouseExited(MouseEvent e) {
                clearButton.setBackground(HOVER_BG);
            }
        });

        clearButton.addActionListener(e -> {
            amountField.setText("0.00");
            resultField.setText("0.00");
            rateInfoLabel.setText(" ");
            statusLabel.setText(" ");
            lastUpdatedLabel.setText("Ready to convert");
        });

        panel.add(clearButton);
        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 35, 20, 35));

        JLabel footerText = new JLabel("💡 Tip: Exchange rates update in real-time");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(TEXT_SECONDARY);
        panel.add(footerText, BorderLayout.WEST);

        JLabel versionLabel = new JLabel("v2.0 Pro");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        versionLabel.setForeground(PRIMARY);
        panel.add(versionLabel, BorderLayout.EAST);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(INPUT_FONT);
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY, 2),
                        BorderFactory.createEmptyBorder(11, 14, 11, 14)
                ));
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        BorderFactory.createEmptyBorder(12, 15, 12, 15)
                ));
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> combo = new JComboBox<>(currencies);
        combo.setFont(INPUT_FONT);
        combo.setForeground(TEXT);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Custom renderer for better appearance
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                if (isSelected) {
                    label.setBackground(PRIMARY);
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(TEXT);
                }
                return label;
            }
        });

        return combo;
    }

    private Border createShadowBorder() {
        return BorderFactory.createCompoundBorder(
                new AbstractBorder() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Shadow effect
                        for (int i = 0; i < 5; i++) {
                            g2.setColor(new Color(0, 0, 0, 5));
                            g2.drawRoundRect(x + i, y + i, width - 1 - (i * 2), height - 1 - (i * 2), 15, 15);
                        }
                        g2.dispose();
                    }
                    
                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(5, 5, 5, 5);
                    }
                },
                BorderFactory.createLineBorder(BORDER, 1)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String amountText = amountField.getText();
            if (amountText.isEmpty() || amountText.equals("0.00")) {
                showError("Please enter an amount");
                return;
            }

            double amount = Double.parseDouble(amountText);
            String fromCode = currencyCodes[fromCurrency.getSelectedIndex()];
            String toCode = currencyCodes[toCurrency.getSelectedIndex()];

            if (fromCode.equals(toCode)) {
                resultField.setText(String.format("%.2f", amount));
                rateInfoLabel.setText("Same currency - no conversion needed");
                statusLabel.setText("✓ Complete");
                statusLabel.setForeground(SUCCESS);
                return;
            }

            convertButton.setEnabled(false);
            convertButton.setText("Converting...");
            statusLabel.setText("Fetching live exchange rate...");
            statusLabel.setForeground(TEXT_SECONDARY);

            // Call the API through CurrencyService
            double rate = CurrencyService.getExchangeRate(fromCode, toCode);
            double result = amount * rate;

            // Update UI with results
            String fromSymbol = currencySymbols[fromCurrency.getSelectedIndex()];
            String toSymbol = currencySymbols[toCurrency.getSelectedIndex()];

            resultField.setText(String.format("%s %.2f", toSymbol, result));
            rateInfoLabel.setText(String.format("1 %s = %.4f %s", fromCode, rate, toCode));
            statusLabel.setText("✓ Conversion successful");
            statusLabel.setForeground(SUCCESS);
            lastUpdatedLabel.setText("Updated just now");

        } catch (NumberFormatException ex) {
            showError("Please enter a valid amount");
            statusLabel.setText("✗ Invalid input");
            statusLabel.setForeground(ERROR);
        } catch (Exception ex) {
            showError("Unable to fetch exchange rate. Please check your internet connection.");
            statusLabel.setText("✗ Connection failed");
            statusLabel.setForeground(ERROR);
        } finally {
            convertButton.setEnabled(true);
            convertButton.setText("Convert Currency");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new CurrencyConverter());
    }
}