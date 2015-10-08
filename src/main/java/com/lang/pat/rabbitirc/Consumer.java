/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lang.pat.rabbitirc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
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
    
    public int RemoveChannel(String ChannelName){
        int ret = 0;
        try {
            channel.queueUnbind(ClientMain.QUEUENAME, ClientMain.EXCHANGE_NAME, ChannelName);
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            ret = 1;
        }
        return ret;
    }
    
    public void consume(){
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        try {
            channel.basicConsume(ClientMain.QUEUENAME, true, consumer);
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
