package serverapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;




public class DAL_1 {
    
    private static Connection con;
    protected static Vector<Users> userList = new Vector<>();
    private static boolean userExist;
    
    
       public static void stablishConnection()
       {
           try {
               DriverManager.registerDriver(new ClientDriver());
               con = DriverManager.getConnection("jdbc:derby://localhost:1527/UserTable", "tic", "tic");
           } catch (SQLException ex) {
               Logger.getLogger(DAL_1.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
       
    public static void getAllData() throws SQLException {
        
       stablishConnection();
       String query = "SELECT * FROM USERTABLE";
       
       PreparedStatement pst = con.prepareStatement(query);
       ResultSet rs = pst.executeQuery();
       
        while (rs.next()) {
            Users user = new Users();
            
            String userName = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            int score = rs.getInt(4);
            boolean status = rs.getBoolean(5);
            boolean availableity = rs.getBoolean(6);
            user = new Users(userName, password, email, score, status,availableity);
            userList.add(user);

        }
        
            rs.close();
            pst.close();
            con.close();
        
    }

    public static void update(Users user) throws SQLException {
        
        stablishConnection();
        String sql = "UPDATE USERTABLE SET PASSWORD=?, EMAIL=?, SCORE=?, STATUS=? WHERE USERNAME=?";
        PreparedStatement pst = con.prepareStatement(sql); 
        
        pst.setString(1, user.getPassword());
        pst.setString(2, user.getEmail());
        pst.setInt(3, user.getScore());
        pst.setBoolean(4, user.isStatus());
        pst.setString(5, user.getUserName());
        pst.setBoolean(6, true);
        
        pst.executeUpdate();
        
        pst.close();
        con.close();
        
    }

    public static void insert(Users user) throws SQLException {
        
        stablishConnection();
        String query = "INSERT INTO USERTABLE (USERNAME, PASSWORD, EMAIL, SCORE, STATUS,ISAVAILABLE) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        
        pst.setString(1, user.getUserName());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getEmail());
        pst.setInt(4, 0);
        pst.setBoolean(5, false);
        pst.setBoolean(6, true);
        
        pst.executeUpdate();
        
        pst.close();
        con.close();
    }

    public static Users search(String username) throws SQLException {
     
        stablishConnection();
        String query = "SELECT USERTABLE, PASSWORD, EMAIL, SCORE, STATUS FROM UserTable WHERE USERNAME = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);

        ResultSet rs = pst.executeQuery();
        Users user = null;
        if (rs.next()) {
            String userName = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            int score = rs.getInt(4);
            boolean status = rs.getBoolean(5);
            boolean availableity = rs.getBoolean(6);
            user = new Users(userName, password, email, score, status,availableity);
        }
            rs.close();
            pst.close();
            con.close();
            return user;
    }
    
    
    public static boolean isPlayerExist(String userName ,String password) throws SQLException     
    {   
        boolean flag =false;
        
        stablishConnection();

        PreparedStatement pst = con.prepareStatement("SELECT USERNAME, PASSWORD FROM USERTABLE WHERE USERNAME = ? AND PASSWORD = ?");
        
        pst.setString(1, userName);
        pst.setString(2, password);
        
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) 
        {
            String name = rs.getString(1);
            String pass =  rs.getString(2);

            if ((userName.equals(name)) && (password.equals(pass))) {
                flag = true;
                break;
            } 
        }
        rs.close();
        pst.close();
        con.close();
        
       return flag; 
    }
    
     public static void updateStatus(String userName,boolean status) throws SQLException {
         
        stablishConnection();
        
        String sql = "UPDATE USERTABLE SET  STATUS=? WHERE USERNAME=?";
        
        PreparedStatement pstm = con.prepareStatement(sql);  
        
        pstm.setBoolean(1, status);
        pstm.setString(2,userName);
        pstm.executeUpdate();
        
        pstm.close();
        con.close();  
    }
     public static boolean isExist(String signUser)
     {
        try {

                getAllData(); 
                for (Users user:userList)
                {
                    userExist = user.userName.equals(signUser);
                   if (userExist) {break;}
                }
            }catch (SQLException ex) {
            Logger.getLogger(DAL_1.class.getName()).log(Level.SEVERE, null, ex);
            }
         return userExist;
     } 
     public static int getOnlinCount ()
     {
        int count = 0;
        for (Users user : userList)
        {
            if(user.status)
            {
                count++;
            }
        }
        
        return count;
     }
     
     public static int getOfflinecount()
     {
        int count = 0;
        for (Users user : userList)
        {
            if(!user.status)
            {
                count++;
            }
        }
        
        return count;
     }
        
}
