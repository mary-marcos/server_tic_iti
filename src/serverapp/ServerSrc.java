package serverapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class ServerSrc extends AnchorPane implements Runnable {

    protected final ImageView icon;
    protected final Label serverStatusTxt;
    protected final Label label;
    protected final MotionBlur motionBlur;
    protected final Label label0;
    protected final MotionBlur motionBlur0;
    protected final Button switchOnBtn;
    protected final Button switchOffBtn;
    protected final Label label1;
    protected final Label numOnlinTxt;
    protected final Label label2;
    protected final Label numOfflinTxt;
    protected final Button refreshBtn;
    ServerApp myServer;
    Thread myThread;
    boolean threadFlag;
    ServerSocket ss;
    Socket mySocket;
    public ServerSrc(Stage stage) {

        icon = new ImageView();
        serverStatusTxt = new Label();
        label = new Label();
        motionBlur = new MotionBlur();
        label0 = new Label();
        motionBlur0 = new MotionBlur();
        switchOnBtn = new Button();
        switchOffBtn = new Button();
        label1 = new Label();
        numOnlinTxt = new Label();
        label2 = new Label();
        numOfflinTxt = new Label();
        refreshBtn = new Button();
        
        myServer = new ServerApp();
        myThread = new Thread(this);
        threadFlag =false;

        setId("backG");
        setPrefHeight(570.0);
        setPrefWidth(750.0);

        icon.setFitHeight(150.0);
        icon.setFitWidth(200.0);
        icon.setLayoutX(283.0);
        icon.setLayoutY(14.0);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);
        icon.setImage(new Image(getClass().getResource("/images/logo.png").toExternalForm()));

        serverStatusTxt.setAlignment(javafx.geometry.Pos.CENTER);
        serverStatusTxt.setId("ticTacToe");
        serverStatusTxt.setLayoutX(156.0);
        serverStatusTxt.setLayoutY(146.0);
        serverStatusTxt.setPrefHeight(103.0);
        serverStatusTxt.setPrefWidth(475.0);
        serverStatusTxt.setText("Server Online");
        serverStatusTxt.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        serverStatusTxt.setTextFill(javafx.scene.paint.Color.valueOf("#d72978"));
        serverStatusTxt.setWrapText(true);
        serverStatusTxt.setFont(new Font(70.0));

        label.setId("ticTacToe");
        label.setLayoutX(83.0);
        label.setLayoutY(58.0);
        label.setText("X");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setTextFill(javafx.scene.paint.Color.valueOf("#ffc201"));

        motionBlur.setRadius(3.0);
        label.setEffect(motionBlur);

        label0.setId("ticTacToe");
        label0.setLayoutX(674.0);
        label0.setLayoutY(115.0);
        label0.setText("O");
        label0.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label0.setTextFill(javafx.scene.paint.Color.valueOf("#d72978"));

        motionBlur0.setRadius(3.0);
        label0.setEffect(motionBlur0);

        switchOnBtn.setLayoutX(80.0);
        switchOnBtn.setLayoutY(292.0);
        switchOnBtn.setMnemonicParsing(false);
        switchOnBtn.setOnAction(this::switchOn);
        switchOnBtn.setStyle("-fx-background-color: #FFC201; -fx-background-radius: 40;");
        switchOnBtn.setText("Switch On");
        switchOnBtn.setTextFill(javafx.scene.paint.Color.WHITE);
        switchOnBtn.setFont(new Font(22.0));

        switchOffBtn.setLayoutX(525.0);
        switchOffBtn.setLayoutY(292.0);
        switchOffBtn.setMnemonicParsing(false);
        switchOffBtn.setOnAction(this::switchOff);
        switchOffBtn.setStyle("-fx-background-color: #FFC201; -fx-background-radius: 40;");
        switchOffBtn.setText("Switch Off");
        switchOffBtn.setTextFill(javafx.scene.paint.Color.WHITE);
        switchOffBtn.setFont(new Font(22.0));

        label1.setAlignment(javafx.geometry.Pos.CENTER);
        label1.setLayoutX(41.0);
        label1.setLayoutY(402.0);
        label1.setPrefHeight(42.0);
        label1.setPrefWidth(207.0);
        label1.setStyle("-fx-border-width: 2; -fx-border-color: #000; -fx-border-radius: 40; -fx-background-color: #fff; -fx-background-radius: 40;");
        label1.setText("Online Players:");
        label1.setFont(new Font(28.0));

        numOnlinTxt.setAlignment(javafx.geometry.Pos.CENTER);
        numOnlinTxt.setLayoutX(260.0);
        numOnlinTxt.setLayoutY(402.0);
        numOnlinTxt.setPrefHeight(44.0);
        numOnlinTxt.setPrefWidth(70.0);
        numOnlinTxt.setStyle("-fx-border-color: #000; -fx-background-color: #fff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-border-width: 2;");
        numOnlinTxt.setText("0");
        numOnlinTxt.setFont(new Font(28.0));

        label2.setAlignment(javafx.geometry.Pos.CENTER);
        label2.setLayoutX(422.0);
        label2.setLayoutY(402.0);
        label2.setPrefHeight(42.0);
        label2.setPrefWidth(207.0);
        label2.setStyle("-fx-border-width: 2; -fx-border-color: #000; -fx-border-radius: 40; -fx-background-color: #fff; -fx-background-radius: 40;");
        label2.setText("Offline Players:");
        label2.setFont(new Font(28.0));

        numOfflinTxt.setAlignment(javafx.geometry.Pos.CENTER);
        numOfflinTxt.setLayoutX(639.0);
        numOfflinTxt.setLayoutY(402.0);
        numOfflinTxt.setPrefHeight(44.0);
        numOfflinTxt.setPrefWidth(70.0);
        numOfflinTxt.setStyle("-fx-border-color: #000; -fx-background-color: #fff; -fx-background-radius: 40; -fx-border-radius: 40; -fx-border-width: 2;");
        numOfflinTxt.setText("0");
        numOfflinTxt.setFont(new Font(28.0));

        refreshBtn.setAlignment(javafx.geometry.Pos.CENTER);
        refreshBtn.setLayoutX(360.0);
        refreshBtn.setLayoutY(301.0);
        refreshBtn.setMnemonicParsing(false);
        refreshBtn.setOnAction(this::refresh);
        refreshBtn.setStyle("-fx-background-color: #d72978; -fx-background-radius: 40;");
        refreshBtn.setText("Refresh");
        refreshBtn.setTextFill(javafx.scene.paint.Color.WHITE);

        getChildren().add(icon);
        getChildren().add(serverStatusTxt);
        getChildren().add(label);
        getChildren().add(label0);
        getChildren().add(switchOnBtn);
        getChildren().add(switchOffBtn);
        getChildren().add(label1);
        getChildren().add(numOnlinTxt);
        getChildren().add(label2);
        getChildren().add(numOfflinTxt);
        getChildren().add(refreshBtn);

    }
    @Override
    public void run() 
    {
        try {
            ss = new ServerSocket(5005);
            while (true)
            {
                mySocket = ss.accept();
                new ClintsHandler(mySocket);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
   }
    

    protected void switchOn(javafx.event.ActionEvent actionEvent)
    {
        if (!threadFlag)
        {
             myThread.start();
            threadFlag =true;
            System.out.println("server started");
        }
      else
        {
           System.out.println("server resumed");
             myThread.resume();
        }
    }

    protected void switchOff(javafx.event.ActionEvent actionEvent)
    {
        myThread.suspend();
        System.out.println("server susbend");

    }

    protected void refresh(javafx.event.ActionEvent actionEvent)
    {
        try {
            DAL_1.userList.removeAllElements();
            DAL_1.getAllData();
        } catch (SQLException ex) {
            Logger.getLogger(ServerSrc.class.getName()).log(Level.SEVERE, null, ex);
        }
      int online = ClintsHandler.clintsVector.size();
      int offline = DAL_1.userList.size()-online;
      numOnlinTxt.setText(""+online);
      numOfflinTxt.setText(""+offline);
      
              
    }

}
