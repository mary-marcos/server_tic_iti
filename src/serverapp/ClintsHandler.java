package serverapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClintsHandler extends Thread {

    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    String userName;
    static String received = "";
    static Vector<OnlineUsers> clintsVector = new Vector<>();
    OnlineUsers onlineUser;
    String reciever;
    String sender;

    ClintsHandler(Socket s) {
        try {
            socket = s;
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
            start();
        } catch (IOException ex) {
            Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        while (true) {

            try {
                 if (received!=null)
                 {
                 received = dis.readUTF();
                 }
                if (received != null) {
                    System.out.println("Received: " + received);
                    String [] parts = received.split(",");
                    switch (parts[0]) {
                        case "move":
                            sendMessageToAll(received);
                            
                        break;

                        case "signup":

                            Users user = new Users();
                            user.setUserName(parts[1]);
                            user.setPassword(parts[2]);
                            user.setEmail(parts[3]);
                            user.setScore(0);
                            user.setStatus(false);

                            try {
                                DAL_1.insert(user);
                            } catch (SQLException ex) {
                                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;

                        case "checkUserName":
                            if (DAL_1.isExist(parts[1])) {
                                dos.writeUTF("exist");
                                System.out.println(parts[1]);
                            } else {
                                dos.writeUTF("notExist");
                            }
                            break;
                        case "signIn":
                            if (DAL_1.isPlayerExist(parts[1], parts[2])) {
                                DAL_1.updateStatus(parts[1], true);
                               onlineUser = new OnlineUsers(this,parts[1]);
                                clintsVector.add(onlineUser);
                                dos.writeUTF("true");
                            } else {
                                dos.writeUTF("false");
                            }
                            break;
                        
                        case "signOut":
                            if (parts.length > 1)
                            {
                            DAL_1.updateStatus(parts[1], false);
                            }
                            System.out.println("Client disconnected");
                            clintsVector.remove(onlineUser);
                            break;
                            
                        case "getUsersData":
                            
                            sendVectorSize();
                            sendUsersData();
                            
                            break;
                        case "Invitation":
                            sender= parts[1];
                            reciever = parts[2];
                            System.out.println("reciever = "+reciever+","+sender);
                            sendInvitation(reciever,sender);
                            
                         break;
                         case "Accepted":
                             reciever = parts[1];
                             sender = parts[2];
                             acceptedInvitation("Challenge accepted");
                             System.out.println("cha accepted"+clintsVector.size()+reciever+sender);
                    
                            break;
                           
                           case"refused":
                               reciever = parts[1];
                                sender = parts[2];
                                refusedInvitation("Challenge rejected");
                                System.out.println("cha rejected"+clintsVector.size());
                               break;
                    }
                    
                    
                   if (parts[0].equals("signOut"))
                   {
                       break;
                   }
                }
            } catch (EOFException ef) {
                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ef);
                break;
            } catch (IOException ex) {
                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                break;
            } catch (SQLException ex) {
                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

    }
    
    private void sendVectorSize()
    {
        try {
            DAL_1.userList.removeAllElements();
            DAL_1.getAllData();
            System.out.println("launched");
        } catch (SQLException ex) {
            Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            dos.writeUTF("vectorSize,"+DAL_1.userList.size());
            System.out.println("launched2"+DAL_1.userList.size());
        } catch (IOException ex) {
            Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sendUsersData()
    {
        for (Users user : DAL_1.userList)
        {
            System.out.println(user.userName);
            String data = "userData,"+user.userName+","
                    +user.status+","+user.availableity;
            try {
                dos.writeUTF(data);
            } catch (IOException ex) {
                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    protected void sendInvitation(String receiver,String sender)
    {
        for (OnlineUsers onlineU : clintsVector)
        {
            if (onlineU.getUserName().equals(receiver))
            {
                try {
                    onlineU.getClint().dos.writeUTF("invitation recieved,"+receiver+","+sender);
                } catch (IOException ex) {
                    Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    protected void acceptedInvitation(String reply)
    {
        for (OnlineUsers onlineU : clintsVector)
        {
            System.out.println(reciever + sender + "dfdfdf");
            if (onlineU.getUserName().equals(reciever) || onlineU.getUserName().equals(sender))
            {
                try {
                    onlineU.getClint().dos.writeUTF(reply);
                    System.out.println(reciever +"==="+ sender );
                } catch (IOException ex) {
                    Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    protected void refusedInvitation(String reply)
    {
        for (OnlineUsers onlineU : clintsVector)
        {
            System.out.println(reciever + sender + "dfdfdf");
            if (onlineU.getUserName().equals(sender))
            {
                try {
                    onlineU.getClint().dos.writeUTF(reply);
                    System.out.println("==="+ sender );
                } catch (IOException ex) {
                    Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    
    private void sendMessageToAll(String receved) {
        for (OnlineUsers oS : clintsVector) {
            try {
               oS.getClint().dos.writeUTF(receved);
                System.out.println("sending to client : "+ receved);
            } catch (IOException ex) {
                Logger.getLogger(ClintsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }}

}
