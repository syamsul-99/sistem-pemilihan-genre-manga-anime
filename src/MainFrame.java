import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;

public class MainFrame extends JFrame {

    CardLayout cl;
    JPanel mainPanel;
    AnimeRepository repo = new AnimeRepository();
    DefaultTableModel model;
    JTextField searchField;

    // ===== VARIABLE INPUT GLOBAL =====
    JTextField inpId = new JTextField();
    JTextField inpTitle = new JTextField();
    JTextField inpDate = new JTextField();
    JComboBox<String> inpGenre = new JComboBox<>(new String[]{
            "Action","Romance","Fantasy","Comedy","Drama","Horror"
    });

    // Area Teks untuk Laporan
    JTextArea reportArea;

    Color primary = new Color(52,143,235);
    Color softBg = new Color(245,247,255);

    String currentRole;

    public MainFrame(String role){
        this.currentRole = role;
        setTitle("Sistem Anime - Mode: " + role);
        setSize(900,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ============================================
        // 1. NAVBAR (TOMBOL KEMBALI & LOGOUT)
        // ============================================
        GradientPanel menu = new GradientPanel(
                new Color(52,143,235),
                new Color(32,89,201)
        );
        menu.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 12));

        JButton backBtn = navButton(" < Kembali Menu Utama");
        JButton logoutBtn = navButton("Logout");

        logoutBtn.setBackground(new Color(220, 53, 69, 80));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 200), 2, true));

        menu.add(backBtn);
        menu.add(logoutBtn);

        add(menu, BorderLayout.NORTH);

        // ============================================
        // 2. MAIN PANEL (CARD LAYOUT - 4 HALAMAN)
        // ============================================
        cl = new CardLayout();
        mainPanel = new JPanel(cl);
        mainPanel.setBackground(softBg);

        mainPanel.add(dashboardPanel(), "dash");   // Halaman 1
        mainPanel.add(listPanel(), "list");        // Halaman 2
        mainPanel.add(inputPanel(), "input");      // Halaman 3
        mainPanel.add(reportPanel(), "report");    // Halaman 4 (BARU)

        add(mainPanel);
        cl.show(mainPanel, "dash");

        // ============================================
        // 3. LOGIKA NAVIGASI ATAS
        // ============================================
        backBtn.addActionListener(e -> {
            cl.show(mainPanel, "dash");
            clearForm();
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);

            if(confirm == JOptionPane.YES_OPTION){
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
    }

    // ==========================================================
    // HALAMAN 1: DASHBOARD
    // ==========================================================
    JPanel dashboardPanel(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(softBg);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("Sistem Pemilihan Genre Anime");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(primary);
        p.add(title, gbc);

        gbc.gridy++;
        JLabel subtitle = new JLabel("Role Aktif: " + currentRole);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(Color.GRAY);
        p.add(subtitle, gbc);

        gbc.gridy++;
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionPanel.setBackground(softBg);

        if(currentRole.equals("USER")){
            JButton btnStart = createBigButton("🚀 Mulai Cari Anime");
            btnStart.addActionListener(e -> cl.show(mainPanel, "list"));
            actionPanel.add(btnStart);

            // User juga bisa lihat report sederhana
            JButton btnRep = createBigButton("📊 Info Statistik");
            btnRep.addActionListener(e -> {
                generateReport(); // Generate data dulu
                cl.show(mainPanel, "report");
            });
            actionPanel.add(btnRep);

        } else if(currentRole.equals("ADMIN")){
            JButton btnManage = createBigButton("📂 Kelola List");
            JButton btnInput = createBigButton("✍️ Input Data");
            JButton btnReport = createBigButton("📊 Laporan Data"); // TOMBOL BARU

            btnManage.addActionListener(e -> cl.show(mainPanel, "list"));
            btnInput.addActionListener(e -> cl.show(mainPanel, "input"));

            // Masuk ke halaman Report
            btnReport.addActionListener(e -> {
                generateReport(); // Hitung ulang statistik sebelum tampil
                cl.show(mainPanel, "report");
            });

            actionPanel.add(btnManage);
            actionPanel.add(btnInput);
            actionPanel.add(btnReport);
        }

        p.add(actionPanel, gbc);
        return p;
    }

    // ===== LIST PANEL (FIX SORTING ANGKA) =====
    JPanel listPanel(){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(softBg);

        model = new DefaultTableModel(new String[]{"ID","Title","Genre","Release"}, 0){
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI",Font.PLAIN,14));
        table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,14));

        loadTable();

        // Event Klik Tabel (Admin Edit)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && currentRole.equals("ADMIN")) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int row = table.convertRowIndexToModel(selectedRow);
                        String id = model.getValueAt(row, 0).toString();
                        String title = model.getValueAt(row, 1).toString();
                        String genre = model.getValueAt(row, 2).toString();
                        String date = model.getValueAt(row, 3).toString();

                        inpId.setText(id);
                        inpTitle.setText(title);
                        inpGenre.setSelectedItem(genre);
                        inpDate.setText(date);
                        inpId.setEditable(false);

                        cl.show(mainPanel, "input");
                        JOptionPane.showMessageDialog(null, "Mode Edit Aktif.");
                    }
                }
            }
        });

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200,30));

        JComboBox<String> genreFilter = new JComboBox<>(new String[]{
                "All","Action","Romance","Fantasy","Comedy","Drama","Horror"
        });
        genreFilter.setPreferredSize(new Dimension(120,30));

        // ===== BAGIAN PERBAIKAN SORTING =====
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());

        // Tambahkan Comparator Khusus untuk Kolom 0 (ID)
        sorter.setComparator(0, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                try {
                    // Coba ubah text jadi angka (Long supaya muat angka besar)
                    Long n1 = Long.parseLong(s1);
                    Long n2 = Long.parseLong(s2);
                    return n1.compareTo(n2);
                } catch (NumberFormatException e) {
                    // Jika ID mengandung huruf (misal "A123"), gunakan sort teks biasa
                    return s1.compareTo(s2);
                }
            }
        });

        table.setRowSorter(sorter);
        // ====================================

        searchField.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e){ applyFilter(sorter, genreFilter); }
        });
        genreFilter.addActionListener(e -> applyFilter(sorter, genreFilter));

        JButton sortBtn = new JButton("Sort Title");
        sortBtn.addActionListener(e -> {
            repo.getAll().sort(Comparator.comparing(Anime::getTitle));
            loadTable();
        });

        JPanel top = new JPanel();
        top.setBackground(softBg);
        top.add(new JLabel("Search Title: "));
        top.add(searchField);
        top.add(new JLabel("Genre: "));
        top.add(genreFilter);
        top.add(sortBtn);

        p.add(new JScrollPane(table), BorderLayout.CENTER);
        p.add(top, BorderLayout.SOUTH);

        return p;
    }

    void applyFilter(TableRowSorter<TableModel> sorter, JComboBox<String> genreFilter){
        String text = searchField.getText().toLowerCase();
        String selectedGenre = genreFilter.getSelectedItem().toString();
        RowFilter<TableModel,Object> filter;

        if(selectedGenre.equals("All")){
            filter = RowFilter.regexFilter("(?i)" + text, 1);
        } else {
            filter = RowFilter.andFilter(Arrays.asList(
                    RowFilter.regexFilter("(?i)" + text, 1),
                    RowFilter.regexFilter(selectedGenre, 2)
            ));
        }
        sorter.setRowFilter(filter);
    }

    // ==========================================================
    // HALAMAN 3: INPUT DATA
    // ==========================================================
    // ===== INPUT PANEL (VALIDASI ANGKA) =====
    JPanel inputPanel(){
        JPanel p = new JPanel(new GridLayout(7,2,10,10));
        p.setBackground(softBg);
        p.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));

        inpDate.setText(LocalDate.now().toString());

        // --- FITUR BARU: Mencegah Huruf di Kolom ID ---
        inpId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Jika yang diketik BUKAN angka, batalkan (consume)
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        JButton add = new JButton("Tambah");
        JButton del = new JButton("Hapus");
        JButton upd = new JButton("Update");
        JButton clear = new JButton("Clear Form");

        p.add(new JLabel("ID (Angka)")); p.add(inpId); // Label diperjelas
        p.add(new JLabel("Judul Anime")); p.add(inpTitle);
        p.add(new JLabel("Genre")); p.add(inpGenre);
        p.add(new JLabel("Release Date")); p.add(inpDate);
        p.add(add); p.add(del);
        p.add(upd); p.add(clear);

        // --- LOGIKA TAMBAH ---
        add.addActionListener(e -> {
            // 1. Cek Kosong
            if(inpId.getText().isEmpty() || inpTitle.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "ID dan Judul wajib diisi!");
                return;
            }

            // 2. VALIDASI ANGKA (Penting!)
            // Regex "[0-9]+" artinya: Pastikan isinya hanya angka 0 sampai 9
            if (!inpId.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(this, "ID harus berupa angka saja!", "Error Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Cek Duplikasi ID
            for(Anime a : repo.getAll()){
                if(a.getId().equals(inpId.getText())){
                    JOptionPane.showMessageDialog(this, "ID sudah ada! Gunakan tombol Update.");
                    return;
                }
            }

            // Simpan
            repo.add(new Anime(inpId.getText(),inpTitle.getText(),
                    inpGenre.getSelectedItem().toString(),inpDate.getText()));
            JOptionPane.showMessageDialog(this,"Berhasil Ditambahkan!");
            clearForm();
            loadTable();
        });

        // --- LOGIKA HAPUS ---
        del.addActionListener(e -> {
            if(inpId.getText().isEmpty()) return;
            if(JOptionPane.showConfirmDialog(this, "Hapus ID: " + inpId.getText() + "?") == JOptionPane.YES_OPTION){
                repo.delete(inpId.getText());
                JOptionPane.showMessageDialog(this,"Berhasil Dihapus!");
                clearForm();
                loadTable();
            }
        });

        // --- LOGIKA UPDATE ---
        upd.addActionListener(e -> {
            if(inpId.getText().isEmpty()) return;

            // Validasi Angka juga di sini (jaga-jaga)
            if (!inpId.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(this, "ID harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            repo.update(inpId.getText(),inpTitle.getText(),
                    inpGenre.getSelectedItem().toString(),inpDate.getText());
            JOptionPane.showMessageDialog(this,"Berhasil Diupdate!");
            clearForm();
            loadTable();
        });

        clear.addActionListener(e -> clearForm());

        return p;
    }

    // ==========================================================
    // HALAMAN 4: LAPORAN / HISTORY (HALAMAN BARU)
    // ==========================================================
    JPanel reportPanel(){
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(softBg);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Judul Halaman
        JLabel title = new JLabel("Laporan Ringkasan Data Anime", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(primary);
        title.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        // Area Teks untuk Statistik
        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setEditable(false);
        reportArea.setMargin(new Insets(10,10,10,10));

        // Tombol Refresh
        JButton refreshBtn = new JButton("Refresh Data");
        refreshBtn.addActionListener(e -> generateReport());

        p.add(title, BorderLayout.NORTH);
        p.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        p.add(refreshBtn, BorderLayout.SOUTH);

        return p;
    }

    // Method untuk Menghitung Statistik
    void generateReport(){
        ArrayList<Anime> allData = repo.getAll();
        StringBuilder sb = new StringBuilder();

        sb.append("========= RINGKASAN DATA =========\n");
        sb.append("Total Anime Tersimpan : " + allData.size() + "\n");
        sb.append("Tanggal Laporan       : " + LocalDate.now() + "\n\n");

        sb.append("========= RINCIAN PER GENRE =========\n");

        // Hitung Genre Manual
        Map<String, Integer> genreCount = new HashMap<>();
        for(Anime a : allData){
            String g = a.getGenre();
            if(genreCount.containsKey(g)){
                genreCount.put(g, genreCount.get(g) + 1);
            } else {
                genreCount.put(g, 1);
            }
        }

        // Tampilkan Genre
        for(String key : genreCount.keySet()){
            sb.append(String.format("- %-10s : %d Anime\n", key, genreCount.get(key)));
        }

        sb.append("\n========= DATA TERAKHIR =========\n");
        if(!allData.isEmpty()){
            Anime last = allData.get(allData.size()-1);
            sb.append("ID    : " + last.getId() + "\n");
            sb.append("Judul : " + last.getTitle() + "\n");
            sb.append("Rilis : " + last.getReleaseDate() + "\n");
        } else {
            sb.append("(Belum ada data)\n");
        }

        reportArea.setText(sb.toString());
    }

    // ==========================================================
    // UTILITIES
    // ==========================================================
    void clearForm(){
        inpId.setText("");
        inpId.setEditable(true);
        inpTitle.setText("");
        inpDate.setText(LocalDate.now().toString());
        inpGenre.setSelectedIndex(0);
    }

    void loadTable(){
        model.setRowCount(0);
        for(Anime a : repo.getAll()){
            model.addRow(new Object[]{a.getId(), a.getTitle(), a.getGenre(), a.getReleaseDate()});
        }
    }

    JButton navButton(String text){
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(255,255,255,30));
        btn.setFont(new Font("Segoe UI",Font.BOLD,14));
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE,2,true));
        btn.setPreferredSize(new Dimension(180,40));
        btn.setFocusPainted(false);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt){ btn.setBackground(new Color(255,255,255,60)); }
            public void mouseExited(MouseEvent evt){
                if(text.equals("Logout")) btn.setBackground(new Color(220, 53, 69, 80));
                else btn.setBackground(new Color(255,255,255,30));
            }
        });
        return btn;
    }

    JButton createBigButton(String text){
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(primary);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(220, 60));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(32,89,201)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));}
            public void mouseExited(MouseEvent e) { btn.setBackground(primary); }
        });
        return btn;
    }
}