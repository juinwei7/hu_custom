package me.hu_custom.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataBase {
    private static HikariDataSource dataSource = null;

    /*
    DataBase 資料庫連線
    createTable 建立資料庫
     */
    public DataBase(FileConfiguration configuration) {
        String path = "mysql.";
        String host = configuration.getString(path + "host");
        int port = configuration.getInt(path + "port");
        String database = configuration.getString(path + "database");
        String user = configuration.getString(path + "user");
        String password = configuration.getString(path + "password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.setPoolName(configuration.getString(path + "database"));
        config.setConnectionTestQuery("SELECT 1;");

        dataSource = new HikariDataSource(config);
        createTable();
    }


    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS `bosstime` (timeboss VARCHAR(36) PRIMARY KEY, expiretime TIMESTAMP, killname VARCHAR(36));";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void saveData(String timeboss, Timestamp expiretime, String killname) {
        try {
            Connection connection = dataSource.getConnection();

            // 首先检查是否存在相同 timeboss 记录
            String checkQuery = "SELECT timeboss FROM bosstime WHERE timeboss = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, timeboss);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // 存在相同 timeboss 记录，执行更新操作
                String updateQuery = "UPDATE bosstime SET expiretime = ?, killname = ? WHERE timeboss = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setTimestamp(1, expiretime);
                preparedStatement.setString(2, killname);
                preparedStatement.setString(3, timeboss);

                preparedStatement.executeUpdate(); // 执行更新操作
                preparedStatement.close();
            } else {
                // 不存在相同 timeboss 记录，执行插入操作
                String insertQuery = "INSERT INTO bosstime (timeboss, expiretime, killname) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, timeboss);
                preparedStatement.setTimestamp(2, expiretime);
                preparedStatement.setString(3, killname);

                preparedStatement.executeUpdate(); // 执行插入操作
                preparedStatement.close();
            }

            resultSet.close();
            checkStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Map<String, Object> loadData(String timebossd) {
        Map<String, Object> bossData = new HashMap<>();

        try {
            Connection connection = dataSource.getConnection();

            String selectQuery = "SELECT expiretime, killname FROM bosstime WHERE timeboss = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, timebossd);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp expiretime = resultSet.getTimestamp("expiretime");
                String killname = resultSet.getString("killname");

                bossData.put("expiretime", expiretime);
                bossData.put("killname", killname);
            }else {
                bossData.put("expiretime", null);
                bossData.put("killname", null);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bossData;
    }


}