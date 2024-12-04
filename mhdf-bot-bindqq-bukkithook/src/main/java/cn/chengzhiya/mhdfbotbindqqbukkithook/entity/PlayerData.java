package cn.chengzhiya.mhdfbotbindqqbukkithook.entity;

import lombok.Data;

@Data
public final class PlayerData {
    private final String playerName;
    private final Long qq;
    private Long chatTimes;
    private Integer dayChatTimes;


    public PlayerData(String playerName, Long qq) {
        this(playerName, qq, 0L, 0);
    }

    public PlayerData(String playerName, Long qq, Long chatTimes, Integer dayChatTimes) {
        this.playerName = playerName;
        this.qq = qq;
        this.chatTimes = chatTimes;
        this.dayChatTimes = dayChatTimes;
    }
}
