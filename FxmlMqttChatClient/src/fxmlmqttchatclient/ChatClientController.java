/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlmqttchatclient;

import be.vives.oop.mqtt.chatservice.IMqttMessageHandler;
import be.vives.oop.mqtt.chatservice.MqttChatService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatClientController implements Initializable, IMqttMessageHandler {

    @FXML
    private VBox root;
    @FXML
    private Button send;
    @FXML
    private TextArea messageField;
    @FXML
    private TextArea conversation;
    @FXML
    private TextField channelName;
    @FXML
    private TextField nickname;
    @FXML
    private RadioButton general;
    @FXML
    private RadioButton jop;
    @FXML
    private RadioButton jens;
    @FXML
    private RadioButton willy;
    @FXML
    private ListView connectedList;
    @FXML
    private Message message;
    private Gson gson = new Gson();
    private  ObservableSet<String> connectedArray = FXCollections.observableSet();
    private ListView<String> addToConnectedArray = new ListView<>();
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
    private Date dateTime;


    // Allows us to use the wrapper for sending chat messages via MQTT
    private MqttChatService chatService;

    @FXML
    private void handleSend(ActionEvent event) {
        sendMessage();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create a chat service and allow this class to receive messages
        chatService = new MqttChatService(nickname.getText(), general.getId());
        chatService.setMessageHandler(this);

        // When the user closes the window we need to disconnect the client
        disconnectClientOnClose();
        conversation.setEditable(false);
        
    }

    // This method is called if a chat message is received from mqtt
    @Override
    public void messageArrived(String channel, String message) {
        try {
            Message messageFromJson = gson.fromJson(message, Message.class);
            conversation.appendText("[" + channel + "] : " + messageFromJson + "\n\r");
            connectedArray.add(messageFromJson.getNickname());
            connectedList.setItems(FXCollections.observableArrayList(connectedArray));
        } catch (JsonSyntaxException mje) {
            conversation.appendText("[" + channel + "] : " + message + "\n\r");
        }

    }

    private void disconnectClientOnClose() {
        // Source: https://stackoverflow.com/a/30910015
        send.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).setOnCloseRequest((event) -> {
                            chatService.disconnect();
                        });
                    }
                });
            }
        });
    }

    public void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
            event.consume();
        }

    }
    
    @FXML
    private void handleSwitchChannelRadioButtonJop(ActionEvent event) {
        chatService.switchChannel(jop.getId());
        conversation.clear();
    }

    @FXML
    private void handleSwitchChannel(ActionEvent event) {
        chatService.switchChannel(channelName.getText());
        conversation.clear();
    }

    @FXML
    private void handleSwitchChannelRadioButtonGeneral(ActionEvent event) {
        chatService.switchChannel(general.getId());
        conversation.clear();
    }

    @FXML
    private void handleSwitchChannelRadioButtonJens(ActionEvent event) {
        chatService.switchChannel(jens.getId());
        conversation.clear();
    }

    @FXML
    private void handleSwitchChannelRadioButtonWilly(ActionEvent event) {
        chatService.switchChannel(willy.getId());
        conversation.clear();
    }
    
    @FXML
    private void handleSelectThemeDarkRed(ActionEvent event) {
        File f = new File("src/fxmlmqttchatclient/DarkRed_theme.css");
        root.getStylesheets().clear();
        root.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
    }
    
    @FXML
    private void handleSelectThemeDarkBlue(ActionEvent event) {
        File f = new File("src/fxmlmqttchatclient/DarkBlue_theme.css");
        root.getStylesheets().clear();
        root.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
    }

    @FXML
    private void handleSelectThemeDarkYellow(ActionEvent event) {
        File f = new File("src/fxmlmqttchatclient/DarkYellow_theme.css");
        root.getStylesheets().clear();
        root.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
    }
    @FXML
    private void handleSelectThemeBasic(ActionEvent event) {
        root.getStylesheets().clear();
        
    }

    private void sendMessage() {
            message = new Message(nickname.getText(), messageField.getText(), dateFormat.format(dateTime = new Date()));
            messageField.clear();
            String messageJson = gson.toJson(message);
            chatService.sendMessage(messageJson);
            System.out.println(messageJson);  
    }
}
