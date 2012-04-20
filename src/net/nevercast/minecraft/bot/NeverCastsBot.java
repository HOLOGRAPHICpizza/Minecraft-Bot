/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.web.MinecraftLogin;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeverCastsBot {  
    
    public static void main(String[] args) {
        String loginName = "iLiveForaMinute";
        if(args.length > 0){
            loginName = args[0];
        }
        MinecraftLogin login = new MinecraftLogin(loginName);
//        MinecraftLogin login = new MinecraftLogin(loginName,loginPass); UNSUPPORTED HANDSHAKE
        /*if(!login.getLoggedIn()){
            System.out.println("Login failed!");
            if(login.getErrorMessage() != null){
                System.out.println(login.getErrorMessage());
            }
            return;
        }*/
        MinecraftClient client = new MinecraftClient(login);
        try {
//            client.connect("localhost");
            client.connect("192.168.1.66");
            while(client.isAlive()){
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
