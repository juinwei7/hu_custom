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
        String taxbase = "CREATE TABLE IF NOT EXISTS `taxplayer` (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(36), taxmoney VARCHAR(36), expiretime TIMESTAMP);";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement taxstatement = connection.prepareStatement(taxbase);

            statement.executeUpdate();
            taxstatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /*
    bosstime 副本王時間
     */
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


    /*
    Tax稅率
    使用player檢查Used -------------------------------------------------------------------------------------
     */
    public static boolean taxBoolean(String TABLE ,String uuid) {
        try {
            Connection connection = dataSource.getConnection();


            // 检查是否存在相同 UUID 记录
            String checkQuery = "SELECT uuid FROM " + TABLE + " WHERE uuid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, uuid);
                ResultSet resultSet = checkStatement.executeQuery();

                // 如果结果集包含数据，则说明 UUID 存在
                boolean exists = resultSet.next();

                resultSet.close();
                connection.close();
                return exists;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return false;
    }
    //TABLE
    //search_field 想要的答案
    //target 要符合條件才能提供答案
    public static String taxloadData(String TABLE, String search_field, String target) {
        String ans = null;

        try {
            Connection connection = dataSource.getConnection();

            String selectQuery = "SELECT " + search_field + " FROM " + TABLE + " WHERE uuid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, target);  // 设置 UUID 参数

            ResultSet resultSet = preparedStatement.executeQuery();

            // 检查是否有结果
            if (resultSet.next()) {
                ans = resultSet.getString(search_field);  // 替换 "data_column" 为实际的数据列名
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ans;  // 返回从数据库中检索到的数据
    }
//(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(36), taxmoney VARCHAR(36), expiretime TIMESTAMP);";
    public static void taxsaveData(String TABLE,String uuid, String name, String taxmoney, Timestamp expiretime) {
        try (Connection connection = dataSource.getConnection()) {

            // 首先检查是否存在相同 UUID 记录
            String checkQuery = "SELECT uuid FROM " + TABLE + " WHERE uuid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, uuid);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // 存在相同 UUID 记录，执行更新操作
                        String updateQuery = "UPDATE " + TABLE + " SET name = ?, taxmoney = ?, expiretime = ? WHERE uuid = ?";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                            preparedStatement.setString(1, name);
                            preparedStatement.setString(2, taxmoney);
                            preparedStatement.setTimestamp(3, expiretime);
                            preparedStatement.setString(4, uuid);
                            int rowsAffected = preparedStatement.executeUpdate(); // 执行更新操作
                        }
                    } else {
                        // 不存在相同 UUID 记录，执行插入操作
                        String insertQuery = "INSERT INTO " + TABLE + " (uuid, name, taxmoney, expiretime) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                            preparedStatement.setString(1, uuid);
                            preparedStatement.setString(2, name);
                            preparedStatement.setString(3, taxmoney);
                            preparedStatement.setTimestamp(4, expiretime);
                            int rowsAffected = preparedStatement.executeUpdate(); // 执行插入操作
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}