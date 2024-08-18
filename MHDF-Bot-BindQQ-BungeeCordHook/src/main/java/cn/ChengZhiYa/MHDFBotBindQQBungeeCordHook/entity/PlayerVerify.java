package cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.entity;

import lombok.Data;

@Data
public final class PlayerVerify {
    String playerName;
    String Verify;

    public PlayerVerify(String playerName, String Verify) {
        this.playerName = playerName;
        this.Verify = Verify;
    }
}
