import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HomePage extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JLabel dateLabel;
    private JLabel dayLabel;
    private JTable taskTable;
    private JTextField taskField;
    private JButton addButton;
    private JButton prevButton;
    private JButton nextButton;
    private DatabaseManager dbManager;
    private DefaultTableModel tableModel;
    private LocalDate currentDate;
    private JPanel centerPanel;

    public HomePage() {
        super("Weekly Task Scheduler");
        setPreferredSize(new Dimension(1300, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocation(300, 100);
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon.png"));
        setIconImage(imageIcon.getImage());
        dbManager = new DatabaseManager(); // Initialize DatabaseManager

        // Initialize currentDate to today's date
        currentDate = LocalDate.now();

        // Initialize dateLabel and dayLabel with empty text
        dateLabel = new JLabel();
        dayLabel = new JLabel();
        // Get formatted date and day
        updateDateAndDayLabels(currentDate);

        // Create top panel with GridBagLayout
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 15, 15, 15);

        // Prev button
        prevButton = new JButton("Prev");
        prevButton.addActionListener(this);
        prevButton.setFont(new Font("Sans Serif", Font.BOLD, 20)); // Increase font size
        prevButton.setBackground(new Color(30, 105, 34)); // Change button color
        prevButton.setForeground(Color.white); // Change text color
        topPanel.add(prevButton, gbc);


        // Date label
        gbc.gridx = 1;
        dateLabel.setFont(new Font("Sans Serif", Font.BOLD, 30)); // Increase font size
        topPanel.add(dateLabel, gbc);

        // Day label
        gbc.gridx = 2;
        dayLabel.setFont(new Font("Sans Serif", Font.BOLD, 30)); // Increase font size
        topPanel.add(dayLabel, gbc);

        // Next button
        gbc.gridx = 3;
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        nextButton.setFont(new Font("Sans Serif", Font.BOLD, 20)); // Increase font size
        nextButton.setBackground(new Color(30, 105, 34)); // Change button color
        nextButton.setForeground(Color.WHITE); // Change text color
        topPanel.add(nextButton, gbc);

        topPanel.setBackground(new Color(210, 158, 222)); // Change background color
        add(topPanel, BorderLayout.PAGE_START);

        centerPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        String[] columnNames = {"Task", "From", "To"};
        String[][] data = {};
        tableModel = new DefaultTableModel(data, columnNames);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Increase font size of table data
        Font tableFont = new Font("Sans Serif", Font.PLAIN, 24); // Increased font size
        taskTable.setFont(tableFont);

        // Increase row height
        taskTable.setRowHeight(50); // Increased row height

        // Increase font size of column headers
        Font headerFont = new Font("Sans Serif", Font.BOLD, 24); // Increased font size
        taskTable.getTableHeader().setFont(headerFont);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/edit.png"));
        Image originalImage = originalIcon.getImage();
        int width = 32; // Specify the desired width
        int height = 32; // Specify the desired height
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        addButton = new JButton(resizedIcon);
        addButton.addActionListener(this);
        addButton.setFont(new Font("Sans Serif", Font.BOLD, 20)); // Increase font size
        addButton.setBackground(Color.GREEN); // Change button color
        addButton.setForeground(Color.WHITE); // Change text color
        bottomPanel.add(addButton);

        centerPanel.setBackground(Color.WHITE); // Change background color
        bottomPanel.setBackground(Color.WHITE); // Change background color

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        fetchDataForDay(currentDate.getDayOfWeek().toString()); // Fetch data for today's day

        pack();
        setVisible(true);
    }

    private void updateDateAndDayLabels(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy");
        String formattedDate = date.format(dateFormatter);
        dateLabel.setText(formattedDate); // Update text of existing dateLabel

        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
        String formattedDay = date.format(dayFormatter).toUpperCase();
        dayLabel.setText(formattedDay); // Update text of existing dayLabel
    }

    private void fetchDataForDay(String dayOfWeek) {
        // Fetch tasks for the given day of the week from the database
        List<String[]> tasks = dbManager.getTasksForDay(dayOfWeek);

        // If tasks list is empty, display an image indicating no tasks for today
        if (tasks.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/tasks.jpg"));
            // Replace "notasks.jpg" with your image file path
            JLabel imageLabel = new JLabel(imageIcon);
            centerPanel.removeAll(); // Remove any existing components
            centerPanel.add(imageLabel, BorderLayout.CENTER);
            centerPanel.revalidate(); // Revalidate the panel to reflect the changes
        } else {
            // Clear existing rows in table
            tableModel.setRowCount(0);
            // Add rows from the fetched data to the tableModel
            for (String[] row : tasks) {
                tableModel.addRow(row);
            }
            // Display the table
            JScrollPane scrollPane = new JScrollPane(taskTable);
            centerPanel.removeAll(); // Remove any existing components
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            centerPanel.revalidate(); // Revalidate the panel to reflect the changes
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Add your action handling here if needed
            TaskScheduler taskScheduler = new TaskScheduler();
            taskScheduler.setVisible(true);
            dispose();
        } else if (e.getSource() == prevButton) {
            currentDate = currentDate.minusDays(1); // Move to previous day
            updateDateAndDayLabels(currentDate);
            fetchDataForDay(currentDate.getDayOfWeek().toString());
        } else if (e.getSource() == nextButton) {
            currentDate = currentDate.plusDays(1); // Move to next day
            updateDateAndDayLabels(currentDate);
            fetchDataForDay(currentDate.getDayOfWeek().toString());
        }
    }

    public static void main(String[] args) {
        try {
            // Set Nimbus look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new HomePage();
    }
}
