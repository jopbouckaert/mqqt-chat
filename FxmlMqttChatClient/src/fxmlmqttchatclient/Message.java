/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlmqttchatclient;

/**
 *
 * @author jopbo_000
 */
public class Message {

    private String nickname;
    private String messageContent;
    private String dateTime;

    public Message(String nickname, String messageContent, String dateTime) {
        this.nickname = nickname;
        this.messageContent = messageContent;
        this.dateTime = dateTime;
    }
    
    public String getNickname(){
        return this.nickname;
    }
    
    public String getdateTime(){
        return this.dateTime;
    }
    
    public void setNickame(String nickname){
        this.nickname = nickname;
    }
    

    @Override
    public String toString() {
        String output = dateTime + "\n\t" +nickname + " : " + messageContent + "\n" ;
        return output;
    }

}
