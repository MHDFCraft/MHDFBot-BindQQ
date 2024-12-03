package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.event.request.FriendRequestEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;

public final class FriendAddRequest implements Listener {
    @EventHandler
    public void onFriendRequest(FriendRequestEvent event) {
        event.handlingFriendRequest(true);
    }
}
