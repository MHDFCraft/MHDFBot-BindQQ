package cn.ChengZhiYa.MHDFBotBindQQ.util;

import cn.ChengZhiYa.MHDFBot.api.MHDFBot;
import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.DatabaseConfig;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerVerify;
import com.alibaba.fastjson2.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public static boolean ifPlayerDataExist(Long QQ) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where QQ=?;")) {
                ps.setLong(1, QQ);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getQqBindList(Long QQ) {
        List<String> qqBindList = new ArrayList<>();
        if (getQqBindHashMap().get(QQ) == null) {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where QQ=?;")) {
                    ps.setLong(1, QQ);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            qqBindList.add(rs.getString("PlayerName"));
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getQqBindHashMap().get(QQ);
        }
        getQqBindHashMap().put(QQ, qqBindList);
        return qqBindList;
    }

    public static void bind(PlayerData playerData) {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            getPlayerDataHashMap().put(playerData.getPlayerName(), playerData);
            List<String> qqBindList = getQqBindList(playerData.getQQ());
            qqBindList.add(playerData.getPlayerName());
            getQqBindHashMap().put(playerData.getQQ(), qqBindList);
            {
                JSONObject data = new JSONObject();
                data.put("playerName", playerData.getPlayerName());
                data.put("qq", playerData.getQQ());
                WebSocket.send("bindDone", data);
            }
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("insert into mhdfbot_bindqq (PlayerName, QQ) values (?,?);")) {
                    ps.setString(1, playerData.getPlayerName());
                    ps.setLong(2, playerData.getQQ());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void unbind(PlayerData playerData) {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            List<String> qqBindList = getQqBindList(playerData.getQQ());
            qqBindList.remove(playerData.getPlayerName());
            getQqBindHashMap().put(playerData.getQQ(), qqBindList);
            getPlayerDataHashMap().remove(playerData.getPlayerName());
            {
                JSONObject data = new JSONObject();
                data.put("playerName", playerData.getPlayerName());
                data.put("qq", playerData.getQQ());
                WebSocket.send("unBindDone", data);
            }
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("delete from mhdfbot_bindqq where PlayerName=?;")) {
                    ps.setString(1, playerData.getPlayerName());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void setPlayerData(PlayerData playerData) {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            getPlayerDataHashMap().put(playerData.getPlayerName(), playerData);
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("update mhdfbot_bindqq set QQ=?, ChatTimes=?, DayChatTimes=? where PlayerName=?;")) {
                    ps.setLong(1, playerData.getQQ());
                    ps.setLong(2, playerData.getChatTimes());
                    ps.setLong(3, playerData.getDayChatTimes());
                    ps.setString(4, playerData.getPlayerName());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static PlayerData getPlayerData(String playerName) {
        if (getPlayerDataHashMap().get(playerName) == null) {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where PlayerName=?;")) {
                    ps.setString(1, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            PlayerData playerData = new PlayerData(rs.getString("PlayerName"), rs.getLong("QQ"), rs.getLong("ChatTimes"), rs.getInt("DayChatTimes"));
                            getPlayerDataHashMap().put(playerName, playerData);
                            return playerData;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getPlayerDataHashMap().get(playerName);
        }
        return null;
    }

    public static void updatePlayerVerify() {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify;")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            PlayerVerify playerData = new PlayerVerify(rs.getString("PlayerName"), rs.getString("Verify"));
                            getPlayerVerifyHashMap().put(playerData.getPlayerName(), playerData);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void clearPlayerVerify() {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            getPlayerVerifyHashMap().clear();
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("delete from mhdfbot_verify")) {
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static PlayerVerify getPlayerVerify(String playerName) {
        if (getPlayerVerifyHashMap().get(playerName) == null) {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify where PlayerName=?;")) {
                    ps.setString(1, playerName);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            PlayerVerify playerData = new PlayerVerify(rs.getString("PlayerName"), rs.getString("Verify"));
                            getPlayerVerifyHashMap().put(playerName, playerData);
                            return playerData;
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getPlayerVerifyHashMap().get(playerName);
        }
        return null;
    }
}
