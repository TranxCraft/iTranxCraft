package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import net.pravian.bukkitlib.util.LoggerUtils;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterModule extends Module {
    
    private TranxCraft plugin;
    private SqlModule sqlModule;

    private final String CONSUMER_KEY;
    private final String CONSUMER_KEY_SECRET;
    private final String ACCESS_TOKEN;
    private final String ACCESS_TOKEN_SECRET;
    
    public TwitterModule(TranxCraft plugin) {
        this.plugin = plugin;
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
            LoggerUtils.info(plugin, "Tweeting: " + tweet);
            twitter.updateStatus(tweet);
            LoggerUtils.info(plugin, "Successfully tweeted.");
        }
        catch (TwitterException ex) {
            LoggerUtils.warning(plugin, "Failed to Submit Tweet.");
            plugin.debugUtils.debug(ex);
        }
    }
}
