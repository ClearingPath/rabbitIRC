/**
 * TODO : - Implement main menu and UI - Implement Thread for consumer (just run
 * consumer.consume()) - Implement user checking - Implement message constructor
 * & destructor (JSON Preferable)
 */
package com.lang.pat.rabbitirc;

import java.util.ArrayList;
import java.util.Scanner;
import org.json.simple.JSONObject;

/**
 *
 * @author ClearingPath
 */
public class ClientMain {

    public static String HOSTNAME = "localhost";
    public static int PORT = 5672;
    public static ArrayList<String> ChannelList = new ArrayList<>();
    public static String USERNAME;
    public static String QUEUENAME;
    public static String EXCHANGE_NAME = "lang.pat.rabbitIRC";
    public static boolean exit = false;

    private Consumer consumer;
    private Producer producer;

    public ClientMain() {
	  System.out.println("* Init consumer...");
	  consumer = new Consumer();
	  System.out.println("* Consumer initialized successfully...");
	  System.out.println("* Init producer...");
	  producer = new Producer();
	  System.out.println("* Producer initialized successfully...");
    }

    public int JoinChannel(String Channel) {
	  for (String item : ChannelList) {
		if (item.equals(Channel)) {
//                System.out.println("!!! : Already join the "+ Channel +" !");
		    return 1;
		}
	  }

	  ClientMain.ChannelList.add(Channel);
	  consumer.AddChannel(Channel);
//        System.out.println("Successful join : " + Channel);
	  return 0;
    }

    public int LeaveChannel(String Channel) {
	  for (String item : ChannelList) {
		if (item.equals(Channel)) {
		    ClientMain.ChannelList.remove(Channel);
		    consumer.RemoveChannel(Channel);
//                System.out.println("Successful leaving : " + Channel);
		    return 0;
		}
	  }
//        System.out.println("!!! : Error leaving " + Channel + " !");
	  return 1;
    }

    public int ChangeNick(String Nick) {
	  if (USERNAME.equals(Nick)) {
		//error same username
		return 1;
	  } else {
		ChannelList.clear();
		USERNAME = Nick;
		return 0;
	  }
    }

    public int Send(String Message) {
	  int ret = 0;
	  JSONObject JSONMessage = new JSONObject();
	  JSONMessage.put("username", USERNAME);
	  JSONMessage.put("message", Message);
	  JSONMessage.put("timestamp", System.currentTimeMillis());

	  producer.send(JSONMessage.toJSONString());
	  return ret;
    }

    public int Send(String Message, String ChannelName) {
	  int ret = 0;
	  JSONObject JSONMessage = new JSONObject();
	  JSONMessage.put("username", USERNAME);
	  JSONMessage.put("message", Message);
	  JSONMessage.put("timestamp", System.currentTimeMillis());
	  
	  if (ChannelList.contains(ChannelName) )
		producer.send(JSONMessage.toJSONString(), ChannelName);
	  else 
		ret = 1;
	  
	  return ret;
    }

    public static void Exit() {
	  System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void generateUname() {
	  String commonUsername[] = {"Earthshaker", "Sven", "Tiny", "Kunkka", "Beastmaster", "DragonKnight", "Axe", "Pudge", "SandKing", "Slardar", "Tidehunter", "WraithKing", "Bloodseeker", "Windranger", "StormSpirit", "Lina", "ShadowFiend", "AntiMage", "PhantomAssassin"};
	  String uname;
	  System.out.println("# Generating random username...");
	  
	  int randIndex = (int) Math.round(Math.random() * (commonUsername.length - 1));
	  int randEnd = (int) (Math.random() * 999);
	  uname = commonUsername[randIndex] + randEnd;
	  System.out.println("# Generated new username: " + uname);
	  
	  USERNAME = uname;
    }

    public static void main(String[] args) {
	  // TODO implements menu and application
	  final ClientMain clientmain = new ClientMain();
	  Scanner input = new Scanner(System.in);
	  
	  generateUname();
	  
	  Runnable consumerThread;
	  consumerThread = new Runnable() {
		public void run() {
		    try {
			  while (!exit) {
				Thread.sleep(1000);
				clientmain.consumer.consume();
			  }
		    } catch (Exception E) {
			  E.printStackTrace();
		    }
		}
	  };
	  new Thread(consumerThread).start();
	  
	  while (!exit){
		System.out.print("> ");
		
		String inputCommand = input.nextLine();
		String[] resSplit = inputCommand.split(" ", 2);
		String Command = resSplit[0].toUpperCase();
		int res;
		
		switch (Command){
		    case "/NICK":
			  res = clientmain.ChangeNick(resSplit[1]);
			  if (res == 1){System.out.println("! Entered username is currently active username!"); }
			  else if (res == 0) { System.out.println("# Username changed to " + resSplit[1]); }
			  break;
			  
		    case "/JOIN":
			  res = clientmain.JoinChannel(resSplit[1]);
			  if (res == 0) {System.out.println("# User " + USERNAME + " has entered channel " + resSplit[1]); }
			  else if (res == 1) {System.out.println("! User " + USERNAME + " already entered channel " + resSplit[1] + "!"); }
			  else {System.out.println("! User " + USERNAME + " failed to enter channel " + resSplit[1] + "!");}
			  break;
			  
		    case "/LEAVE":
			  res = clientmain.LeaveChannel(resSplit[1]);
			  if (res == 0) {System.out.println("# User " + USERNAME + " has left channel " + resSplit[1]); }
			  else {System.out.println("! User " + USERNAME + " failed to leave channel " + resSplit[1]); }
			  break;
			  
		    case "/EXIT":
			  exit = true;
			  break;
			  
		    default:
			  if (resSplit[0].startsWith("@")){
				String msg, channelName;
				channelName = resSplit[0].substring(1);
				
				if (resSplit.length < 2){
				    System.out.println("! No input message!");
				    System.out.print("# Masukkan message: ");
				    msg = input.nextLine();
				}
				else {
				    msg = resSplit[1];
				}
				
				res = clientmain.Send(msg, channelName);
				if (res == 1) {
				    System.out.println("! User " + USERNAME + " did not join channel " + channelName + "!");
				} 
			  } else {
				res = clientmain.Send(inputCommand);
			  }
			  break;
		}
	  }
	  
	  if (exit) { Exit(); }
    }

}
