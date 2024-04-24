import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TaskScheduler extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField taskField;
    private JTextField fromField;
    private JTextField toField;
    private DatabaseManager databaseManager;
    private DefaultTableModel tableModel;
    private String lastClickedDay; // Store the last clicked day
    private JTable taskTable; // Declare taskTable as a class variable

    public TaskScheduler() {
        super("Weekly Task Scheduler");
        setPreferredSize(new Dimension(1300,900));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.GREEN);
        setLocation(300,100);
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icon.png"));
        setIconImage(imageIcon.getImage());


        // Increase font size for components
        Font bigFont = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 105, 34));
        JLabel l1=new JLabel("💗 Weekly Task Scheduler");
        l1.setForeground(Color.RED);
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(l1,BorderLayout.CENTER);
        headerPanel.setFont(bigFont);

        JPanel dayOfWeekPanel = new JPanel(new GridLayout(1, 7));
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        // Add buttons for each day
        for (String day : daysOfWeek) {
            JButton button = new JButton(day);
            // Enable all buttons
            button.setEnabled(true);
            button.setFont(bigFont);
            // Set color based on weekday or weekend
            if (day.equals("Saturday") || day.equals("Sunday")) {
                button.setBackground(Color.RED);
            } else {
                button.setBackground(Color.GREEN);
            }
            dayOfWeekPanel.add(button);
            // Add ActionListener to handle button clicks
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle button click here
                    JButton clickedButton = (JButton) e.getSource();
                    lastClickedDay = clickedButton.getText(); // Store the clicked day
                    System.out.println("Clicked: " + lastClickedDay);
                    fetchDataForDay(lastClickedDay);
                    // No need to add task here, it's added when "Add" button is clicked
                }
            });
        }

        String[] columnNames = {"Task", "From", "To"};
        Object[][] data = {};

        // Create JTable and JScrollPane
        tableModel = new DefaultTableModel(data, columnNames);
        taskTable = new JTable(tableModel); // Initialize taskTable
        // Set font for the
        taskTable.setFont(bigFont);
        taskTable.setBackground(new Color(188,167,25));
        taskTable.setForeground(Color.blue);

        // Set font for table headers
        JTableHeader header = taskTable.getTableHeader();
        header.setFont(bigFont);

        // Set custom cell renderer to make text bigger in the table
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setFont(bigFont);
        taskTable.setDefaultRenderer(Object.class, cellRenderer);

        // Increase row height to accommodate larger font
        taskTable.setRowHeight(40); // Set the row height to 40 pixels

        JScrollPane scrollPane = new JScrollPane(taskTable);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(1, 5, 5, 5); // Add some padding

        // Increase font size for labels
        JLabel taskLabel = new JLabel("Task:");
        taskLabel.setFont(bigFont);
        buttonPanel.add(taskLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2; // Span across 2 columns
        taskField = new JTextField(15);
        taskField.setPreferredSize(new Dimension(taskField.getPreferredSize().width, 40));
        taskField.setFont(bigFont);
        buttonPanel.add(taskField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(bigFont);
        buttonPanel.add(fromLabel, gbc);

        gbc.gridx = 1;
        fromField = new JTextField(3);
        fromField.setPreferredSize(new Dimension(fromField.getPreferredSize().width, 30));
        fromField.setFont(bigFont);
        buttonPanel.add(fromField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(bigFont);
        buttonPanel.add(toLabel, gbc);

        gbc.gridx = 1;
        toField = new JTextField(3);
        toField.setPreferredSize(new Dimension(toField.getPreferredSize().width, 30));
        toField.setFont(bigFont);
        buttonPanel.add(toField, gbc);

        // Adjusting insets again to create space before buttons
        gbc.insets = new Insets(30, 5, 5, 5); // Add more space before buttons

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across 2 columns
        ImageIcon originalResetIcon = new ImageIcon(getClass().getResource("/reset.png"));
        Image originalResetImage = originalResetIcon.getImage();
        int width = 92; // Specify the desired width
        int height = 92; // Specify the desired height
        Image resizedResetImage = originalResetImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedResetImage);
        JButton Reset = new JButton(resizedIcon);
//        Reset.setFont(bigFont);
        buttonPanel.add(Reset, gbc);
        //Reset.setBackground(new Color(255, 215, 0));;
        Reset.setBackground(new Color(0,0,0,0));


        gbc.gridx=2;
        gbc.gridy = 3;

        ImageIcon originaldeleteIcon = new ImageIcon(getClass().getResource("/delete.png"));
        Image originaldeleteImage = originaldeleteIcon.getImage();
        Image resizedImage = originaldeleteImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedDeleteIcon = new ImageIcon(resizedImage);
        JButton delete = new JButton(resizedDeleteIcon);
//        delete.setFont(bigFont);
        buttonPanel.add(delete, gbc);
        delete.setBackground(new Color(0,0,0,0));

        gbc.insets = new Insets(2, 5, 5, 5);


        gbc.gridx=1;
        gbc.gridy =6;
        ImageIcon originalAddIcon = new ImageIcon(getClass().getResource("/add.png"));
        Image originalAddImage = originalAddIcon.getImage();
        Image resizedAddImage = originalAddImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedAddIcon = new ImageIcon(resizedAddImage);
        JButton addButton = new JButton(resizedAddIcon);
//        addButton.setFont(bigFont);
        buttonPanel.add(addButton, gbc);
        addButton.setBackground(new Color(0,0,0,0));

        gbc.gridx=2;
        gbc.gridy = 6;
        ImageIcon originalHomeIcon = new ImageIcon(getClass().getResource("/house-icon.png"));
        Image originalHomeImage = originalHomeIcon.getImage();
        Image resizedHomeImage = originalHomeImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedHomeIcon = new ImageIcon(resizedHomeImage);
        JButton homeButton = new JButton(resizedHomeIcon);
//        homeButton.setFont(bigFont);
        headerPanel.add(homeButton,BorderLayout.EAST);
        homeButton.setBackground(new Color(0,0,0,0));


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click here
                addTask(lastClickedDay); // Add task for the last clicked day
            }
        });
        Reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTask(lastClickedDay); // Pass the lastClickedDay parameter
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask(lastClickedDay);
            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click to navigate to homepage
                // Close current frame and open a new instance of TaskScheduler
                dispose(); // Close current frame
                HomePage frame = new HomePage(); // Create new instance
                frame.setVisible(true); // Show the new frame
            }
        });
        // Create JPanel for task details
        // Create contentPanel and set BorderLayout
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Add dayOfWeekPanel to NORTH
        contentPanel.add(dayOfWeekPanel, BorderLayout.NORTH);

        // Add scrollPane to CENTER
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttonPanel to WEST
        contentPanel.add(buttonPanel, BorderLayout.WEST);
        contentPanel.add(headerPanel,BorderLayout.SOUTH);
        buttonPanel.setBackground(new Color(154, 235, 143));


        // Set contentPanel as content pane
        this.setContentPane(contentPanel);
        this.pack();

        // Create an instance of DatabaseManager
        databaseManager = new DatabaseManager();
    }

    private void addTask(String dayOfWeek) {
        String task = taskField.getText();
        String from = fromField.getText();
        String to = toField.getText();

        // Add the task to the database
        if(task.equals("")){
            JOptionPane.showMessageDialog(this, "Please write a task name to add.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if(from.equals("")|| to.equals("")){
            JOptionPane.showMessageDialog(this,"Please insert time correctly","Error",JOptionPane.ERROR_MESSAGE);
        }
        databaseManager.addTask(task, from, to, dayOfWeek);

        // Refresh table after adding task
        if (dayOfWeek != null) {
            fetchDataForDay(dayOfWeek);
        }
    }

    private void deleteTask(String dayOfWeek) {
        // Get the selected row index
        int selectedRowIndex = taskTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRowIndex == -1) {
            // No row selected, display an error message
            JOptionPane.showMessageDialog(this, "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get task details from the selected row
        String task = (String) tableModel.getValueAt(selectedRowIndex, 0);
        String from = (String) tableModel.getValueAt(selectedRowIndex, 1);
        String to = (String) tableModel.getValueAt(selectedRowIndex, 2);

        // Delete the task from the database
        databaseManager.deleteTask(task, from, to, dayOfWeek);

        // Refresh table after deleting task
        if (dayOfWeek != null) {
            fetchDataForDay(dayOfWeek);
        }
    }

    private void resetTask(String dayOfWeek) {
        // Check if there are tasks to delete
        if (tableModel.getRowCount() == 0) {
            // If there are no tasks, display a message
            JOptionPane.showMessageDialog(this, "No tasks to reset.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Confirm with the user before resetting all tasks
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset all tasks?", "Confirm Reset", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // If user confirms, delete all tasks from the database
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                String task = (String) tableModel.getValueAt(i, 0);
                String from = (String) tableModel.getValueAt(i, 1);
                String to = (String) tableModel.getValueAt(i, 2);
                databaseManager.resetTask(dayOfWeek); // Assuming there's no dayOfWeek for reset
            }

            // Clear the table model to reflect the changes
            tableModel.setRowCount(0);

            // Display a message indicating tasks have been reset
            JOptionPane.showMessageDialog(this, "All tasks have been reset.", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void fetchDataForDay(String dayOfWeek) {
        // Fetch tasks for the given day of the week from the database
        List<String[]> tasks = databaseManager.getTasksForDay(dayOfWeek);

        // Clear existing rows in table
        tableModel.setRowCount(0);

        // Add rows from the fetched data to the tableModel
        for (String[] row : tasks) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        try {
            // Set Nimbus look and feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        TaskScheduler frame = new TaskScheduler();
        frame.setVisible(true);
    }
}
