package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import net.pushover.client.MessagePriority;
import net.pushover.client.PushoverClient;
import net.pushover.client.PushoverException;
import net.pushover.client.PushoverMessage;
import net.pushover.client.PushoverRestClient;

public class PushoverModule extends Module<TranxCraft> {

    private PushoverClient client;
    private String apiKey;

    @Override
    public void onLoad() {
        SqlModule sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
        client = new PushoverRestClient();
        apiKey = sqlModule.getConfigEntry("pushover_apikey");
    }

    public void sendNotification(MessagePriority priority, String userId, String title, String message) {
        try {
            client.pushMessage(PushoverMessage.builderWithApiToken(apiKey)
                    .setUserId(userId)
                    .setTitle(title)
                    .setMessage(message)
                    .setPriority(priority)
                    .build());
        }
        catch (PushoverException ex) {
            ex.printStackTrace();
        }
    }

    public void sendNotifications(Rank rank, MessagePriority priority, String title, String message) {
        for (Admin admin : plugin.adminManager.getAdmins()) {
            if (admin.getRank().getRankLevel() >= rank.getRankLevel() && ValidationUtils.isValidPushKey(admin.getPushKey())) {
                sendNotification(priority, admin.getPushKey(), message, title);
            }
        }
    }
}
