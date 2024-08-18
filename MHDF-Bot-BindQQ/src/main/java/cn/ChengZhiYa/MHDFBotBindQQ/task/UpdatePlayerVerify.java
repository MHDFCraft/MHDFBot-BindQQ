package cn.ChengZhiYa.MHDFBotBindQQ.task;

import cn.ChengZhiYa.MHDFBot.api.MHDFBotRunnable;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil.updatePlayerVerify;

public final class UpdatePlayerVerify extends MHDFBotRunnable {
    @Override
    public void run() {
        updatePlayerVerify();
    }
}
