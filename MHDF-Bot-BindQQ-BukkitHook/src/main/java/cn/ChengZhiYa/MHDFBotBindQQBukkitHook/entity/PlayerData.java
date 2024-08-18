package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity;

import lombok.Data;

@Data
public final class PlayerData {
    public String PlayerName;
    public Long QQ;
    public Long ChatTimes;
    public Integer DayChatTimes;

    public PlayerData(String PlayerName, Long QQ) {
        this.PlayerName = PlayerName;
        this.QQ = QQ;
    }

    public PlayerData(String PlayerName, Long QQ, Long ChatTimes, Integer DayChatTimes) {
        this.PlayerName = PlayerName;
        this.QQ = QQ;
        this.ChatTimes = ChatTimes;
        this.DayChatTimes = DayChatTimes;
    }
}
