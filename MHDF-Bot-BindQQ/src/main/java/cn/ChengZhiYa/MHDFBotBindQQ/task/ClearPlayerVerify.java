package cn.ChengZhiYa.MHDFBotBindQQ.task;

import cn.ChengZhiYa.MHDFBot.api.MHDFBotRunnable;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil.clearPlayerVerify;

public final class ClearPlayerVerify extends MHDFBotRunnable {
    @Override
    public void run() {
        clearPlayerVerify();
    }
}
