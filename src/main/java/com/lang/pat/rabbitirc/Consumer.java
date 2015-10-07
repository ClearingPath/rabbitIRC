/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lang.pat.rabbitirc;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ClearingPath
 */
public class Consumer {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    
    public Consumer(){
        try {
            factory = new ConnectionFactory();
            factory.setHost(ClientMain.HOSTNAME);
            connection = factory.newConnection();
            channel = connection.createChannel();
            ClientMain.QUEUENAME = channel.queueDeclare().getQueue();
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int AddChannel(String ChannelName){
        int ret = 0;
        try {
            channel.queueBind(ClientMain.QUEUENAME, ClientMain.EXCHANGE_NAME, ChannelName);
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            ret = 1;
        }
        return ret;
    }
    
}
