package com.wickedgaminguk.tranxcraft.bridge.ipboard;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.util.DebugUtils;
import com.wickedgaminguk.tranxcraft.util.StrUtils;
import net.pravian.bukkitlib.util.LoggerUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IpBoard {

    private TranxCraft plugin;

    public IpBoard(TranxCraft plugin) {
        this.plugin = plugin;
    }

    public User getUser(String username) {
        User user = new User();

        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `TranxCraftForum` WHERE name=?", username);

        try {
            user.setUsername(result.getString("name"))
                    .setId(result.getInt("member_id"))
                    .setMemberGroupId(result.getInt("member_group_id"))
                    .setEmail(result.getString("email"))
                    .setJoined(Long.valueOf(result.getInt("joined")))
                    .setIpAddress(result.getString("ip_address"))
                    .setPosts(result.getInt("posts"))
                    .setTitle(result.getString("title"))
                    .setBirthday(StrUtils.concatenate(result.getString("bday_day"), "/", result.getString("bday_month"), "/", result.getString("bday_year")))
                    .setDisplayName(result.getString("members_display_name"))
                    .setWarnLevel(result.getInt("warn_level"))
                    .setLastWarn(result.getInt("warn_lastwarn"))
                    .setLastPost(Long.valueOf(result.getInt("last_post")));
        }
        catch (SQLException ex) {
            LoggerUtils.warning(plugin, StrUtils.concatenate("Error getting user information for user ", username, ex.getCause()));
        }

        return user;
    }

    public boolean isUser(String username) {
        ResultSet result = plugin.sqlModule.getDatabase().query("SELECT * FROM `TranxCraftForum` WHERE name=?", username);

        try {
            if (!result.isBeforeFirst()) {
                return false;
            }
            else {
                return true;
            }
        }
        catch (SQLException ex) {
            DebugUtils.debug(2, ex);
            return false;
        }
    }
}
