package com.wickedgaminguk.tranxcraft.utils;

import com.google.common.net.InternetDomainName;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.util.IpUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.configuration.ConfigurationSection;

public class ValidationUtils {

    public static boolean isValidHostname(String hostname) {
        if (hostname.isEmpty()) {
            return false;
        }
        
        if (!IpUtils.isValidIp(hostname)) {
            if (!InternetDomainName.isValid(hostname)) {
                return false;
            }
        }

        return true;
    }
    
    public static boolean isValidPort(String port) {
        if (!NumberUtils.isInt(port)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
    
    public static boolean exists(String... toCheck) {
        for (String string : toCheck) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isValidSql(YamlConfig config) {
        ConfigurationSection mySql = config.getConfigurationSection("mysql");

        if (!exists(mySql.getString("hostname"), mySql.getString("port"), mySql.getString("username"), mySql.getString("password"), mySql.getString("database"))) {
            return false;
        }
        
        if (isValidHostname(mySql.getString("hostname")) == false) {
            return false;
        }

        if (isValidPort(mySql.getString("port")) == false) {
            return false;
        }

        return true;
    }
    
    public static boolean isValidEmailConfig(SqlModule sqlModule) {
        String host = sqlModule.getConfigEntry("mail_host");
        String port = sqlModule.getConfigEntry("mail_port");
        String username = sqlModule.getConfigEntry("mail_username");
        String password = sqlModule.getConfigEntry("mail_password");
        String sender = sqlModule.getConfigEntry("mail_sender");

        if (!exists(host, port, username, password, sender)) {
            return false;
        }
        
        if (!isValidHostname(host)) {
            return false;
        }
        
        if (!isValidPort(port)) {
            return false;    
        }
        
        if(!isValidEmail(sender)) {
            return false;
        }
        
        return true;
    }
}
