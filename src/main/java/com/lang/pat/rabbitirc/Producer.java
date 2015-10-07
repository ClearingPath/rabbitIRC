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
public class Producer {
    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private String EXCHANGE_NAME = "lang.pat.rabbitIRC";
    
    Producer(){
        try {
            factory = new ConnectionFactory();
            factory.setHost(ClientMain.HOSTNAME);
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
}
