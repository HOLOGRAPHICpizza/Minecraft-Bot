package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.web.MinecraftLogin;

import java.io.IOException;

/**
 * Main program for starting client.
 * This is supposedly 1.2.3 compliant.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class NeverCastsBot {  
    
	/**
	 * Runs the client. Pass server, port, username, and password as arguments.
	 * If port is missing default port will be used.
	 * If password is missing offline mode will be used.
	 * 
	 * Example: localhost:1337 HOLOGRAPHICpizza tacos27
	 * Example: 158.56.25.48 UberGreifer9001
	 * 
	 * @param args server[:port] username [password]
	 */
    public static void main(String[] args) {
        if(args.length < 2) {
        	System.err.println("Usage: java NeverCastsBot server[:port] username [password]");
        	throw new IllegalArgumentException("No server or username specified.");
        }
    	
    	String loginName = "";
        String loginPass = "";
        String server = "";
        int port = -1;
        if(args.length >= 2) {
            server = args[0];
            if(server.contains(":")) {
            	String[] split = server.split(":");
            	server = split[0];
            	port = Integer.parseInt(split[1]);
            }
            
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
        	login = new MinecraftLogin(loginName, loginPass);
        	if(!login.isLoggedIn()){
                System.err.println("Login failed!");
                throw new RuntimeException(login.getErrorMessage());
            }
        }
        
        MinecraftClient client = new MinecraftClient(login);
        try {
        	if(port > 0)
        		client.connect(server, port);
        	else
        		client.connect(server);
            while(client.isAlive() && client.isRunning()){
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
            client.kill();
            
        } catch (InterruptedException e) {}
    }
}
