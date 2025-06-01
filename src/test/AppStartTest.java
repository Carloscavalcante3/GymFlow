package src.test;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import src.Main;

public class AppStartTest {
    
    @Test
    public void testAppStart() throws Exception {
        Platform.startup(() -> {});
        
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                Main main = new Main();
                main.start(stage);
                
                Thread.sleep(2000);
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        Thread.sleep(3000);
    }
} 