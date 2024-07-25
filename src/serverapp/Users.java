
package serverapp;

public class Users {
    String userName;
    String password;
    String email;
    int score;
    boolean status;
    boolean availableity;
    
    
    public Users()
    {
        
    }

    public Users(String userName, String password, String email, int score, boolean status,boolean availableity) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.score = score;
        this.status = status;
        this.availableity = availableity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

   
    
}
