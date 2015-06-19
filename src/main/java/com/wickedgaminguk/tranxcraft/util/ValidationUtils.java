package com.wickedgaminguk.tranxcraft.util;

import com.google.common.net.InternetDomainName;
import com.wickedgaminguk.tranxcraft.modules.SqlModule;
import net.pravian.bukkitlib.config.YamlConfig;
import net.pravian.bukkitlib.util.IpUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.bukkit.configuration.ConfigurationSection;

public class ValidationUtils extends Util {

    /** Checks if a given hostname is a valid IP or hostname.
     * @param hostname The hostname that you want to check.
     * @return Whether the hostname is valid or not.
     */
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

    /** Checks to see if a given string is a valid port.
     * @param port The port that you want to check.
     * @return Whether the port is valid or not.
     */
    public static boolean isValidPort(String port) {
        return NumberUtils.isInt(port) && Integer.valueOf(port) > 0 && Integer.valueOf(port) <= 65535;
    }

    /** Checks to see if a given string is a valid e-mail.
     * @param email The string that you want to check.
     * @return Whether the e-mail is valid or not.
     */
    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /** Checks to see if any of the given strings are null or empty.
     * @param toCheck The strings that you want to check.
     * @return If any given strings do not exist, it will return false.
     */
    public static boolean exists(String... toCheck) {
        for (String string : toCheck) {
            if (string == null || string.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }

    /** Checks to see if a string is in an enumeration.
     * @param value The string you wish to check.
     * @param enumClass The class of the enumeration.
     * @return Whether the string is in the enum or not.
     */
    public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.name().equals(value)) {
                return true;
            }
        }
        
        return false;
    }

    /** Checks to see if the given config contains valid MySQL details.
     * @param config The config that you wish to check.
     * @return Whether the config has valid MySQL details.
     */
    public static boolean isValidSql(YamlConfig config) {
        ConfigurationSection mySql = config.getConfigurationSection("mysql");

        if (!exists(mySql.getString("hostname"), mySql.getString("port"), mySql.getString("username"), mySql.getString("password"), mySql.getString("database"))) {
            return false;
        }
        
        if (!isValidHostname(mySql.getString("hostname"))) {
            return false;
        }

        if (!isValidPort(mySql.getString("port"))) {
            return false;
        }

        return true;
    }

    /** Checks to see if the MySQL server contains a valid mail server configuration.
     * @param sqlModule The SqlModule for access to the database.
     * @return Whether if the configuration is correct.
     */
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

    /** Checks to see if the given UUID is of a correct format.
     * @param uuid The UUID you wish to check.
     * @return Whether the UUID is valid or not.
     */
    public static boolean isValidUuid(String uuid) {
        return uuid.matches("[a-f0-9]{8}-[a-f0-9]{4}-4[0-9]{3}-[89ab][a-f0-9]{3}-[0-9a-f]{12}");
    }

    /** Checks to see if the given PushOver Key is of a correct format.
     * @param key The key you wish to check.
     * @return Whether the key is valid or not.
     */
    public static boolean isValidPushKey(String key) {
        return key.length() == 30;
    }
}
