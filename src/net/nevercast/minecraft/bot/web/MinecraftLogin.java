package net.nevercast.minecraft.bot.web;

import java.io.IOException;
import java.net.URLEncoder;
import net.nevercast.minecraft.bot.MinecraftClient;
import com.esotericsoftware.minlog.Log;

/**
 * Facilitates secure and insecure Minecraft logins.
 * Used to hold credentials for use by the client.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author Josh
 */
public class MinecraftLogin {

    private String username;
    private String latestVersion;
    private String downloadTicket;
    private String sessionId;
    private boolean isLoggedIn;
    private String errorMessage = null;
    
    private static final String LOG_PREFIX = MinecraftLogin.class.getSimpleName();

    /**
     * Hold username for an insecure login.
     * 
     * @param username The username to log in as.
     */
    public MinecraftLogin(String username){
        this.username = username;
        isLoggedIn = false;
    }

    /**
     * Execute a secure login and hold the credentials.
     * 
     * @param username Username to log in with.
     * @param password Password for this username.
     */
    public MinecraftLogin(String username, String password) {
    	//TODO: Throw exceptions in exceptional conditions.
    	
        try {
            String parameters = "user=" + URLEncoder.encode(username, "UTF-8")
            		+ "&password=" + URLEncoder.encode(password, "UTF-8")
            		+ "&version=" + MinecraftClient.CLIENT_VERSION;
            String result = WebUtil.excutePost("https://login.minecraft.net/", parameters);
            
            Log.debug(LOG_PREFIX, "Authorization result: " + result);
            
            if (result == null) {
                setErrorMessage("Can't connect to minecraft.net");
                return;
            }
            if (!result.contains(":")) {
                if (result.trim().equals("Bad login")) {
                    setErrorMessage("Login failed");
                } else if (result.trim().equals("Old version")) {
                    setErrorMessage("Outdated!");
                } else {
                    setErrorMessage(result);
                }
                return;
            }
            String[] values = result.split(":");

            this.username = values[2].trim();
            this.latestVersion = values[0].trim();
            this.downloadTicket = values[1].trim();
            this.sessionId = values[3].trim();
            isLoggedIn = true;
        } catch(IOException e) {
            Log.warn("Error logging in:", e);
            setErrorMessage(e.toString());
        }
    }

    public String getLatestVersion(){
        return latestVersion;
    }

    public String getDownloadTicket(){
        return downloadTicket;
    }

    public String getSessionId() {
        return sessionId;
    }


    private void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }


    public String getUsername(){
        return username;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }
}
