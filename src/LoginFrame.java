import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login System");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Background
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(new Color(245, 247, 255));

        // Komponen
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("LOGIN");

        // Styling sederhana
        loginBtn.setBackground(new Color(52, 143, 235));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        add(panel, BorderLayout.CENTER);
        add(loginBtn, BorderLayout.SOUTH);

        // LOGIKA LOGIN
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                // MASUK SEBAGAI ADMIN
                new MainFrame("ADMIN").setVisible(true);
                this.dispose(); // Tutup jendela login
            }
            else if (user.equals("user") && pass.equals("user123")) {
                // MASUK SEBAGAI USER BIASA
                new MainFrame("USER").setVisible(true);
                this.dispose(); // Tutup jendela login
            }
            else {
                JOptionPane.showMessageDialog(this, "Username atau Password Salah!");
            }
        });
    }

    public static void main(String[] args) {
        // Jalankan LoginFrame dulu, bukan MainFrame
        new LoginFrame().setVisible(true);
    }
}