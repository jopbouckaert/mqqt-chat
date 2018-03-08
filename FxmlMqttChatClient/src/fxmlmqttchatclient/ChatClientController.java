/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlmqttchatclient;

import be.vives.oop.mqtt.chatservice.IMqttMessageHandler;
import be.vives.oop.mqtt.chatservice.MqttChatService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ChatClientController implements Initializable, IMqttMessageHandler {
    
    @FXML private Button send;
    @FXML private TextArea message;
    @FXML private TextArea conversation;
    
    // Allows us to use the wrapper for sending chat messages via MQTT
    private MqttChatService chatService;
    
    @FXML
    private void handleSend(ActionEvent event) {
        // Use the sendMessage() method to send a message to an mqtt channel
        chatService.sendMessage("Hello World");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create a chat service and allow this class to receive messages
        chatService = new MqttChatService();
        chatService.setMessageHandler(this);
    }    

    // This method is called if a chat message is received from mqtt
    @Override
    public void messageArrived(String channel, String message) {
        System.out.println("Received chat message (on channel = " + channel
                + "): " + message);
    }
}
