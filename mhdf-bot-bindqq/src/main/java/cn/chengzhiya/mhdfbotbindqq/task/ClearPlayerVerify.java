package cn.chengzhiya.mhdfbotbindqq.task;

import cn.chengzhiya.mhdfbot.api.runnable.MHDFBotRunnable;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

public final class ClearPlayerVerify extends MHDFBotRunnable {
    @Override
    public void run() {
        DatabaseUtil.clearPlayerVerify();
    }
}
