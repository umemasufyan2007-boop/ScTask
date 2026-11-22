package todo;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Swing GUI for the Task Management System, with colors and a clean layout.
 * @author Zainab Khalid
 */
public class TaskApp extends JFrame {

    private static final Color NAVY = new Color(31, 58, 95);
    private static final Color BACKGROUND = new Color(244, 246, 250);
    private static final Color GREEN = new Color(46, 158, 107);
    private static final Color BLUE = new Color(47, 111, 222);
    private static final Color RED = new Color(214, 69, 69);
    private static final Color GRAY = new Color(107, 114, 128);

    private final TaskManager manager = new TaskManager();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Title", "Description", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField titleField = new JTextField(14);
    private final JTextField descField = new JTextField(20);
    private final JComboBox<Task.Status> statusBox = new JComboBox<>(Task.Status.values());
    private final JLabel messageLabel = new JLabel(" ");
    private int selectedId = -1;

    /** Builds the window and all its components. */
    public TaskApp() {
        super("Task Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(880, 540);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());

        // Top: title banner and input form
        JLabel banner = new JLabel("Task Management System");
        banner.setFont(new Font("SansSerif", Font.BOLD, 24));
        banner.setForeground(Color.WHITE);
        banner.setOpaque(true);
        banner.setBackground(NAVY);
        banner.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 14));
        form.setBackground(BACKGROUND);
        form.add(makeLabel("Title:"));
        form.add(titleField);
        form.add(makeLabel("Description:"));
        form.add(descField);
        form.add(makeLabel("Status:"));
        form.add(statusBox);
        titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusBox.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel top = new JPanel(new BorderLayout());
        top.add(banner, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Center: table
        setupTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 214, 222)));
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BACKGROUND);
        center.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // Bottom: buttons and message
        JButton addBtn = makeButton("Add", GREEN);
        JButton updateBtn = makeButton("Update", BLUE);
        JButton deleteBtn = makeButton("Delete", RED);
        JButton refreshBtn = makeButton("Refresh", GRAY);
        addBtn.addActionListener(e -> addTask());
        updateBtn.addActionListener(e -> updateTask());
        deleteBtn.addActionListener(e -> deleteTask());
        refreshBtn.addActionListener(e -> refreshTable());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        buttons.setBackground(BACKGROUND);
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);

        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BACKGROUND);
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(messageLabel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
    }

    /** Sets the table look: row height, header color, row colors and selection. */
    private void setupTable() {
        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(190, 210, 245));
        table.setSelectionForeground(Color.BLACK);
        table.setDefaultRenderer(Object.class, new RowRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(340);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setOpaque(true);
                setHorizontalAlignment(CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean selected, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, value, selected, focus, row, col);
                setBackground(NAVY);
                setForeground(Color.WHITE);
                setFont(new Font("SansSerif", Font.BOLD, 14));
                setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                return this;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onRowSelected();
        });
    }

    /**
     * Creates a bold label for the form.
     * @param text the label text
     * @return the styled label
     */
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(NAVY);
        return label;
    }

    /**
     * Creates a colored button.
     * @param text the button text
     * @param color the background color
     * @return the styled button
     */
    private JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(110, 36));
        return button;
    }

    /**
     * Shows a colored message at the bottom of the window.
     * @param text the message
     * @param color the text color
     */
    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
    }

    /** Adds a task using the text fields and the chosen status. */
    private void addTask() {
        try {
            manager.addTask(titleField.getText(), descField.getText(),
                    (Task.Status) statusBox.getSelectedItem());
            refreshTable();
            showMessage("Task added successfully.", GREEN);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            showMessage(ex.getMessage(), RED);
        }
    }

    /** Updates the selected task with the values in the form. */
    private void updateTask() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Click a task in the table first, change the details, then press Update.",
                    "No task selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            boolean ok = manager.updateTask(selectedId, titleField.getText(),
                    descField.getText(), (Task.Status) statusBox.getSelectedItem());
            refreshTable();
            if (ok) {
                showMessage("Task updated successfully.", BLUE);
            } else {
                showMessage("Task not found.", RED);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            showMessage(ex.getMessage(), RED);
        }
    }

    /** Deletes the selected task after confirmation. */
    private void deleteTask() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Click a task in the table first, then press Delete.",
                    "No task selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Delete this task?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            boolean ok = manager.deleteTask(selectedId);
            refreshTable();
            if (ok) {
                showMessage("Task deleted.", RED);
            } else {
                showMessage("Task not found.", RED);
            }
        }
    }

    /** Reloads the table from the manager and clears the form. */
    private void refreshTable() {
        model.setRowCount(0);
        for (Task t : manager.getAllTasks()) {
            model.addRow(new Object[]{t.getId(), t.getTitle(),
                    t.getDescription(), t.getStatus()});
        }
        selectedId = -1;
        titleField.setText("");
        descField.setText("");
        statusBox.setSelectedItem(Task.Status.PENDING);
        if (model.getRowCount() == 0) {
            showMessage("No tasks to display.", GRAY);
        }
    }

    /** Copies the selected row into the form fields. */
    private void onRowSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = (int) model.getValueAt(row, 0);
        Task t = manager.findById(selectedId);
        if (t != null) {
            titleField.setText(t.getTitle());
            descField.setText(t.getDescription());
            statusBox.setSelectedItem(t.getStatus());
        }
    }

    /** Draws striped rows and colors the Status column by value. */
    private static class RowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value,
                boolean selected, boolean focus, int row, int col) {
            super.getTableCellRendererComponent(t, value, selected, focus, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setHorizontalAlignment((col == 0 || col == 3) ? CENTER : LEFT);
            boolean statusCell = (col == 3 && value instanceof Task.Status);
            setFont(t.getFont().deriveFont(statusCell ? Font.BOLD : Font.PLAIN));
            if (!selected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                setForeground(Color.BLACK);
                if (statusCell) {
                    switch ((Task.Status) value) {
                        case PENDING:
                            setBackground(new Color(255, 236, 179));
                            setForeground(new Color(133, 100, 4));
                            break;
                        case IN_PROGRESS:
                            setBackground(new Color(207, 226, 255));
                            setForeground(new Color(20, 70, 150));
                            break;
                        default:
                            setBackground(new Color(212, 237, 218));
                            setForeground(new Color(21, 100, 50));
                            break;
                    }
                }
            }
            return this;
        }
    }

    /**
     * Starts the application.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskApp().setVisible(true));
    }
}