package com.wickedgaminguk.tranxcraft.modules;

import com.wickedgaminguk.tranxcraft.TranxCraft;
import com.wickedgaminguk.tranxcraft.player.Admin;
import com.wickedgaminguk.tranxcraft.player.Rank;
import com.wickedgaminguk.tranxcraft.util.ValidationUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import java.util.ArrayList;
import java.util.List;

public class MailModule extends Module<TranxCraft> {
    
    private final String HOST;
    private final int PORT;
    private final String USER;
    private final String PASSWORD;
    private final String FROM;
    
    private SqlModule sqlModule;
    private boolean isEnabled;

    public MailModule() {
        sqlModule = (SqlModule) ModuleLoader.getModule("SqlModule");
        
        HOST = sqlModule.getConfigEntry("mail_host");
        PORT = Integer.valueOf(sqlModule.getConfigEntry("mail_port"));
        USER = sqlModule.getConfigEntry("mail_username");
        PASSWORD = sqlModule.getConfigEntry("mail_password");
        FROM = sqlModule.getConfigEntry("mail_sender");
    }

    public void sendEmail(Rank rank, String subject, String message) {
        if (!isEnabled) {
            return;
        }
        
        for (String email : getRecipients(rank)) {
            sendEmail(email, subject, message);
        }
    }

    public void sendEmail(String[] emails, String subject, String message) {
        if (!isEnabled) {
            return;
        }
        
        for (String email : emails) {
            sendEmail(email, subject, message);
        }
    }

    public void sendEmail(String address, String subject, String message) {
        if (!isEnabled) {
            return;
        }
        
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(USER, PASSWORD));
            email.setTLS(true);
            email.setFrom(FROM);
            email.setSubject(subject);
            email.setHtmlMsg(message);
            email.addTo(address);
            email.send();
        }
        catch (EmailException ex) {
            plugin.debugUtils.debug(ex.getStackTrace().toString());
        }
    }
    
    private List<String> getRecipients(Rank rank) {
        List<String> recipients = new ArrayList<>();
        
        for (Admin admin : plugin.adminManager.getAdmins()) {
            if (admin.getRank().getRankLevel() >= rank.getRankLevel()) {
                if (ValidationUtils.isValidEmail(admin.getEmail())) {
                    recipients.add(admin.getEmail());
                }
            }
        }
        
        return recipients;
    }
    
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}