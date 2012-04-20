package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.web.MinecraftLogin;

import java.io.IOException;

/**
 * This is supposedly 1.2.3 compliant.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class NeverCastsBot {  
    
	/**
	 * Runs the bot. Pass server, username, and password as arguments.
	 * If password is missing offline mode will be used.
	 * @param args server username [password]
	 */
    public static void main(String[] args) {
        if(args.length < 2) {
        	throw new IllegalArgumentException("You must at least specify a server and username.");
        }
    	
    	String loginName = "";
        String loginPass = "";
        String server = "";
        if(args.length >= 2) {
            server = args[0];
            loginName = args[1];
        }
        
        if(args.length >= 3) {
        	loginPass = args[2];
        }
        
        MinecraftLogin login;
        if(loginPass.equals("")) {
        	System.out.println("Using offline mode.");
        	login = new MinecraftLogin(loginName);
        }
        else {
        	login = new MinecraftLogin(loginName,loginPass);
        	if(!login.getLoggedIn()){
                System.err.println("Login failed!");
                if(login.getErrorMessage() != null){
                    System.err.println(login.getErrorMessage());
                }
                System.exit(1);
            }
        }
        
        MinecraftClient client = new MinecraftClient(login);
        try {
            client.connect(server);
            while(client.isAlive() && client.isRunning()){
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {}

    }
}
