package cn.chengzhiya.mhdfbotbindqq.util;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbotbindqq.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerVerify;
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
     * 判断指定QQ号的绑定数据是否存在
     *
     * @param qq 目标QQ号
     */
    public static boolean ifPlayerDataExist(Long qq) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where QQ=?;")) {
                ps.setLong(1, qq);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定QQ号的绑定游戏ID列表
     *
     * @param qq 目标QQ号
     * @return 绑定游戏ID列表
     */
    public static List<String> getQqBindList(Long qq) {
        if (getQqBindHashMap().get(qq) != null) {
            return getQqBindHashMap().get(qq);
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where QQ=?;")) {
                ps.setLong(1, qq);

                try (ResultSet rs = ps.executeQuery()) {
                    List<String> qqBindList = new ArrayList<>();
                    while (rs.next()) {
                        qqBindList.add(rs.getString("PlayerName"));
                    }
                    getQqBindHashMap().put(qq, qqBindList);
                    return qqBindList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 绑定指定绑定数据实例
     *
     * @param playerData 绑定数据实例
     */
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

                MHDFBot.getMinecraftWebSocketServer().send("bindDone", data);
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

    /**
     * 解除绑定指定绑定数据实例
     *
     * @param playerData 绑定数据实例
     */
    public static void unbind(PlayerData playerData) {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            getPlayerDataHashMap().remove(playerData.getPlayerName());

            List<String> qqBindList = getQqBindList(playerData.getQQ());
            qqBindList.remove(playerData.getPlayerName());
            getQqBindHashMap().put(playerData.getQQ(), qqBindList);

            {
                JSONObject data = new JSONObject();
                data.put("playerName", playerData.getPlayerName());
                data.put("qq", playerData.getQQ());

                MHDFBot.getMinecraftWebSocketServer().send("unBindDone", data);
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

    /**
     * 更新指定数据实例
     *
     * @param playerData 绑定数据实例
     */
    public static void updatePlayerData(PlayerData playerData) {
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

    /**
     * 获取指定玩家ID的绑定数据实例
     *
     * @param playerName 目标玩家ID
     * @return 绑定数据实例
     */
    public static PlayerData getPlayerData(String playerName) {
        if (getPlayerDataHashMap().get(playerName) != null) {
            return getPlayerDataHashMap().get(playerName);
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_bindqq where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    PlayerData playerData = new PlayerData(
                            rs.getString("PlayerName"),
                            rs.getLong("QQ"),
                            rs.getLong("ChatTimes"),
                            rs.getInt("DayChatTimes")
                    );
                    getPlayerDataHashMap().put(playerName, playerData);
                    return playerData;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新验证码数据缓存
     */
    public static void updatePlayerVerify() {
        MHDFBot.getScheduler().runTaskAsynchronously(() -> {
            try (Connection connection = dataSource.getConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify;")) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            PlayerVerify playerData = new PlayerVerify(
                                    rs.getString("PlayerName"),
                                    rs.getString("Verify")
                            );
                            getPlayerVerifyHashMap().put(playerData.playerName(), playerData);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 清空验证码数据
     */
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

    /**
     * 获取指定游戏ID的验证码数据实例
     *
     * @param playerName 目标游戏ID
     * @return 验证码数据实例
     */
    public static PlayerVerify getPlayerVerify(String playerName) {
        if (getPlayerVerifyHashMap().get(playerName) != null) {
            return getPlayerVerifyHashMap().get(playerName);
        }

        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("select * from mhdfbot_verify where PlayerName=?;")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    PlayerVerify playerData = new PlayerVerify(
                            rs.getString("PlayerName"),
                            rs.getString("Verify")
                    );
                    getPlayerVerifyHashMap().put(playerName, playerData);

                    return playerData;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}