/**
 * TODO :
 * - Implement main menu and UI
 * - Implement Thread for consumer (just run consumer.consume())
 * - Implement user checking
 * - Implement message constructor & destructor (JSON Preferable)
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
    public static String QUEUENAME;
    public static String EXCHANGE_NAME = "lang.pat.rabbitIRC";
    private Consumer consumer;
    private Producer producer;
    
    public ClientMain(){
        consumer = new Consumer();
        producer = new Producer();
    }
    
    public int JoinChannel(String Channel){
        for (String item : ChannelList){
            if (item.equals(Channel)){
//                System.out.println("!!! : Already join the "+ Channel +" !");
                return 1;
            }
        }
        
        ClientMain.ChannelList.add(Channel);
        consumer.AddChannel(Channel);
//        System.out.println("Successful join : " + Channel);
        return 0;
    }
    
    public int LeaveChannel(String Channel){
        for (String item : ChannelList){
            if (item.equals(Channel)){
                ClientMain.ChannelList.remove(Channel);
                consumer.RemoveChannel(Channel);
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
    
    public int Send(String Message){
        int ret = 0;
        //construct message first!
        producer.send(Message);
        return ret;
    }
    
    public int Send(String Message, String ChannelName){
        int ret = 0;
        //construct message first!
        producer.send(Message, ChannelName);
        return ret;
    }
    
    public void Exit(){
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO implements menu and application
        
    }
    
}
