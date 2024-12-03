package cn.chengzhiya.mhdfbotbindqqbungeehook.util;

import cn.chengzhiya.mhdfbotbindqqbungeehook.Main;
import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.PlayerVerify;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public final class DatabaseUtil {
    @Getter
    static final HashMap<String, PlayerData> playerDataHashMap = new HashMap<>();
    @Getter
    static final HashMap<Long, List<String>> qqBindHashMap = new HashMap<>();
    @Getter
    static final HashMap<String, PlayerVerify> playerVerifyHashMap = new HashMap<>();

    private static HikariDataSource dataSource;

    /**
     * 连接指定数据库配置实例
     *
     * @param database 数据库配置实例
     */
    public static void connectDatabase(DatabaseConfig database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + database.host() + "/" + database.database() + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
        config.setUsername(database.username());
        config.setPassword(database.password());
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    /**
     * 初始化数据库
     */
    public static void intiDatabase() {
        try {
            // 初始化绑定数据表
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS mhdfbot_bindqq" +
                                "(" +
                                "    PlayerName VARCHAR(50) NOT NULL COMMENT '玩家ID'," +
                                "    QQ BIGINT DEFAULT 0 NOT NULL COMMENT 'QQ号'," +
                                "    ChatTimes BIGINT DEFAULT 0 NOT NULL COMMENT '积累聊天数'," +
                                "    DayChatTimes INT DEFAULT 0 NOT NULL COMMENT '今日聊天数'," +
                                "    PRIMARY KEY (PlayerName)," +
                                "    INDEX `QQ` (`QQ`)" +
                                ")" +
                                "COLLATE=utf8mb4_bin;"
                )) {
                    ps.executeUpdate();
                }
            }

            // 初始化验证码数据表
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS mhdfbot_verify" +
                                "(" +
                                "    PlayerName VARCHAR(50) NOT NULL COMMENT '玩家ID'," +
                                "    Verify INT DEFAULT 0 NOT NULL COMMENT '验证码'," +
                                "    PRIMARY KEY (PlayerName)" +
                                ")" +
                                "COLLATE=utf8mb4_bin;"
                )) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断指定玩家ID的绑定数据是否存在
     *
     * @param playerName 目标玩家ID
     */
    public static boolean ifPlayerDataExist(String playerName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新指定玩家ID的验证码数据实例缓存
     *
     * @param playerName 目标玩家ID
     */
    public static void updatePlayerVerify(String playerName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        PlayerVerify playerData = new PlayerVerify(
                                rs.getString("PlayerName"),
                                rs.getString("Verify")
                        );
                        getPlayerVerifyHashMap().put(playerName, playerData);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定游戏ID的验证码数据实例
     *
     * @param playerName 目标游戏ID
     * @return 验证码数据实例
     */
    public static PlayerVerify getPlayerVerify(String playerName) {
        Main.instance.getProxy().getScheduler().runAsync(Main.instance, () -> updatePlayerVerify(playerName));
        return getPlayerVerifyHashMap().get(playerName);
    }
}