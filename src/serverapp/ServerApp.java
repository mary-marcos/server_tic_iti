
package serverapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ServerApp extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Font.loadFont(getClass().getResource("/fonts/MooLahLah-Regular.ttf").toExternalForm(), 10);
        ServerSrc root = new ServerSrc(stage);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass()
                .getResource("/style/CSS_StyleSheet.css").toExternalForm());
        stage.setTitle("ServerApp");
        Image icon = new Image(getClass().getResourceAsStream("/images/logo.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
    
  
    public static void main(String[] args) {
        launch(args);
        
    }
    
}
