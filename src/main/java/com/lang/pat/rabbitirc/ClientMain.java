/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lang.pat.rabbitirc;

import java.util.ArrayList;

/**
 *
 * @author ClearingPath
 */
public class ClientMain {
    public static String HOSTNAME = "localhost";
    public static ArrayList<String> ChannelList = new ArrayList<>();
    public static String USERNAME;
    
    public int JoinChannel(String Channel){
        for (String item : ChannelList){
            if (item.equals(Channel)){
//                System.out.println("!!! : Already join the "+ Channel +" !");
                return 1;
            }
        }
        ClientMain.ChannelList.add(Channel);
//        System.out.println("Successful join : " + Channel);
        return 0;
    }
    
    public int LeaveChannel(String Channel){
        for (String item : ChannelList){
            if (item.equals(Channel)){
                ClientMain.ChannelList.remove(Channel);
//                System.out.println("Successful leaving : " + Channel);
                return 0;
            }
        }
//        System.out.println("!!! : Error leaving " + Channel + " !");
        return 1;
    }
    
    public int ChangeNick(String Nick){
        if (USERNAME.equals(Nick)){
            //error same username
            return 1;
        }
        else{
            USERNAME = Nick;
            return 0;
        }
    }
    
    public void Exit(){
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
