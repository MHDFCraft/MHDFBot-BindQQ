package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity.DatabaseConfig;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity.PlayerVerify;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class DatabaseUtil {
    @Getter
    static final HashMap<String, PlayerData> playerDataHashMap = new HashMap<>();
    @Getter
    static final HashMap<Long, List<String>> qqBindHashMap = new HashMap<>();
    @Getter
    static final HashMap<String, PlayerVerify> playerVerifyHashMap = new HashMap<>();

    private static HikariDataSource dataSource;

    public static void connectDatabase(DatabaseConfig database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + database.getHost() + "/" + database.getDatabase() + "?autoReconnect=true&serverTimezone=" + TimeZone.getDefault().getID());
        config.setUsername(database.getUsername());
        config.setPassword(database.getPassword());
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
    }

    public static void intiDatabase() {
        try {
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

    public static void setPlayerVerify(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(main.instance, () -> {
            Random random = new Random();
            String verify = String.valueOf(random.nextInt(1000, 10000));
            PlayerVerify playerVerify = new PlayerVerify(playerName, verify);
            getPlayerVerifyHashMap().put(playerName, playerVerify);
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("insert into mhdfbot_verify (PlayerName, Verify) values (?,?);")) {
                    ps.setString(1, playerName);
                    ps.setString(2, verify);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void updatePlayerVerify(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(main.instance, () -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify where playerName=?;")) {
                    ps.setString(1, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            PlayerVerify playerData = new PlayerVerify(playerName, rs.getString("Verify"));
                            getPlayerVerifyHashMap().put(playerName, playerData);
                        } else {
                            DatabaseUtil.setPlayerVerify(playerName);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void updatePlayerData(String playerName) {
        Bukkit.getScheduler().runTaskAsynchronously(main.instance, () -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where PlayerName=?;")) {
                    ps.setString(1, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            PlayerData playerData = new PlayerData(rs.getString("PlayerName"), rs.getLong("QQ"), rs.getLong("ChatTimes"), rs.getInt("DayChatTimes"));
                            getPlayerDataHashMap().put(playerName, playerData);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void updateQqBindList(Long qq) {
        Bukkit.getScheduler().runTaskAsynchronously(main.instance, () -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where QQ=?;")) {
                    ps.setLong(1, qq);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<String> qqBindList = new ArrayList<>();
                        while (rs.next()) {
                            qqBindList.add(rs.getString("PlayerName"));
                        }
                        getQqBindHashMap().put(qq, qqBindList);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<String> getQqBindList(Long qq) {
        updateQqBindList(qq);
        return getQqBindHashMap().get(qq) != null ? getQqBindHashMap().get(qq) : new ArrayList<>();
    }

    public static PlayerData getPlayerData(String playerName) {
        updatePlayerData(playerName);
        return getPlayerDataHashMap().get(playerName);
    }

    public static PlayerVerify getPlayerVerify(String playerName) {
        updatePlayerVerify(playerName);
        return getPlayerVerifyHashMap().get(playerName);
    }
}
