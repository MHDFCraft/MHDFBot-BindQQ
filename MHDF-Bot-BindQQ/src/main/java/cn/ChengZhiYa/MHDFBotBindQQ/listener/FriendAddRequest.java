package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.request.FriendRequestEvent;

public final class FriendAddRequest implements Listener {
    @EventHandler
    public void onFriendRequest(FriendRequestEvent event) {
        event.handlingFriendRequest(true);
    }
}
