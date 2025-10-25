package view;

import controller.AuthController;
import util.UIStyleHelper;

import javax.swing.*;
import java.awt.*;

public class RiderSignUpPanel extends JPanel {
    private JTextField usernameField, admissionField;
    private JPasswordField passwordField, confirmPasswordField;
    private final AuthController auth = new AuthController();

    public RiderSignUpPanel(RideShareMobileUI ui) {
        setLayout(new BorderLayout());
        setBackground(UIStyleHelper.BG_COLOR);

        JLabel title = UIStyleHelper.createTitle("🧍 Create Rider Account");
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 240), 1),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(15);
        admissionField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);

        JButton registerBtn = new JButton("✅ Register");
        JButton backBtn = new JButton("← Back");

        UIStyleHelper.styleButton(registerBtn, UIStyleHelper.SUCCESS_COLOR);
        UIStyleHelper.styleButton(backBtn, UIStyleHelper.PRIMARY_COLOR);

        int y = 0;
        addRow(form, gbc, y++, "Username:", usernameField);
        addRow(form, gbc, y++, "Admission No:", admissionField);
        addRow(form, gbc, y++, "Password:", passwordField);
        addRow(form, gbc, y++, "Confirm Password:", confirmPasswordField);
        gbc.gridx = 1; gbc.gridy = y++; form.add(registerBtn, gbc);
        gbc.gridx = 1; gbc.gridy = y; form.add(backBtn, gbc);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIStyleHelper.BG_COLOR);
        centerPanel.add(form);
        add(centerPanel, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> register(ui));
        backBtn.addActionListener(e -> ui.showScreen("signup_choice"));
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; form.add(new JLabel(label), gbc);
        gbc.gridx = 1; form.add(field, gbc);
    }

    private void register(RideShareMobileUI ui) {
        String id = admissionField.getText().trim();
        String name = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (id.isEmpty() || name.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean ok = auth.registerRider(id, name, pass);
            if (ok) {
                JOptionPane.showMessageDialog(this, "✅ Account created successfully! Login now.");
                ui.showScreen("login");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
