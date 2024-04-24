import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public Connection connection;

    public DatabaseManager() {
        connectToDatabase();
    }

    public void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/event";
            String user = "root";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL database.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database.");
        }
    }

    public void addTask(String task, String from, String to, String day) {
        try {
            String sql = "INSERT INTO events (task, start_time, end_time, day) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, task);
            statement.setString(2, from);
            statement.setString(3, to);
            statement.setString(4, day);
            statement.executeUpdate();
            System.out.println("Task added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add task.");
        }
    }
    public void deleteTask(String task, String from, String to, String day) {
        try {
            String sql = "DELETE FROM events WHERE task = ? AND start_time = ? AND end_time = ? AND day = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, task);
            statement.setString(2, from);
            statement.setString(3, to);
            statement.setString(4, day);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task not found or delete failed.");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to delete task.");
        }
    }

    public void resetTask(String day) {
        try {
            String sql = "DELETE FROM events WHERE day = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, day);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("All tasks for " + day + " deleted successfully.");
            } else {
                System.out.println("No tasks found for " + day + " to delete.");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to reset tasks for " + day + ".");
        }
    }


    public List<String[]> getTasksForDay(String day) {
        List<String[]> tasks = new ArrayList<>();
        try {
            String sql = "SELECT * FROM events WHERE day = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, day);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] row = {
                        resultSet.getString("task"),
                        resultSet.getString("start_time"),
                        resultSet.getString("end_time")
                };
                tasks.add(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to fetch tasks.");
        }
        return tasks;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to close database connection.");
        }
    }
}
