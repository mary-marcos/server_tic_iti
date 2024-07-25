package serverapp;

public class OnlineUsers 
{
    private ClintsHandler clint;
    private String userName;

    public OnlineUsers(ClintsHandler clint, String userName) {
        this.clint = clint;
        this.userName = userName;
    }

    public OnlineUsers() {
    }

    public ClintsHandler getClint() {
        return clint;
    }

    public void setClint(ClintsHandler clint) {
        this.clint = clint;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    
}
