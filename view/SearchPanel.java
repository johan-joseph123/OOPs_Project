package view;

import util.UIStyleHelper;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class SearchPanel extends JPanel {
    private final RideShareMobileUI ui;
    private final JComboBox<String> toField;
    private final JTextField dateField;

    public SearchPanel(RideShareMobileUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🔍 Search Rides");
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        toField = new JComboBox<>(new String[]{
                "Pala", "Pravithanam", "Kottayam RS", "Erattupetta", "Ponkunnam", "Thodupuzha"
        });

        dateField = new JTextField(LocalDate.now().toString());

        JButton searchButton = UIStyleHelper.styleButton(new JButton("🔎 Search Rides"), UIStyleHelper.PRIMARY_COLOR);
        searchButton.addActionListener(e -> openResults());

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1; formPanel.add(toField, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; formPanel.add(dateField, gbc);
        row++;
        gbc.gridx = 1; gbc.gridy = row; gbc.insets = new Insets(30, 15, 15, 15);
        formPanel.add(searchButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void openResults() {
        String to = (String) toField.getSelectedItem();
        String date = dateField.getText().trim();

        if (to == null || to.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both destination and date.");
            return;
        }

        // ✅ Dynamically show ResultPanel
        ui.showScreen("results", new ResultPanel(ui, to, date));
    }
}
