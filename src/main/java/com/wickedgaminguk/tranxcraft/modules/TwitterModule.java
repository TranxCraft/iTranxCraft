package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterModule extends Module<TranxCraft> {
    
    private SqlModule sqlModule;

    private String CONSUMER_KEY;
    private String CONSUMER_KEY_SECRET;
    private String ACCESS_TOKEN;
    private String ACCESS_TOKEN_SECRET;

    @Override
    public void onLoad() {
        this.sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
        
        this.CONSUMER_KEY = sqlModule.getConfigEntry("twitter_consumerkey");
        this.CONSUMER_KEY_SECRET = sqlModule.getConfigEntry("twitter_consumerkeysecret");
        this.ACCESS_TOKEN = sqlModule.getConfigEntry("twitter_accesstoken");
        this.ACCESS_TOKEN_SECRET = sqlModule.getConfigEntry("twitter_accesstokensecret");
    }

    public void tweet(String tweet) {
        if (tweet.length() > 140) {
            LoggerUtils.info(plugin, "Not tweeting tweet, it's over 140 characters long.");
            return;
        }
        
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        AccessToken oathAccessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);

        twitter.setOAuthAccessToken(oathAccessToken);

        try {
            LoggerUtils.info(plugin, StrUtils.concatenate("Tweeting: ", tweet));
            twitter.updateStatus(tweet);
            LoggerUtils.info(plugin, "Successfully tweeted.");
        }
        catch (TwitterException ex) {
            LoggerUtils.warning(plugin, "Failed to Submit Tweet.");
            plugin.debugUtils.debug(ex);
        }
    }
}
