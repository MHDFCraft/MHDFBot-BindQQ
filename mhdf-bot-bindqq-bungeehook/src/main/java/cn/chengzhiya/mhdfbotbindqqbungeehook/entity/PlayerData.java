package cn.chengzhiya.mhdfbotbindqqbungeehook.entity;

import lombok.Data;

@Data
public final class PlayerData {
    private String PlayerName;
    private Long QQ;
    private Long ChatTimes;
    private Integer DayChatTimes;

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
