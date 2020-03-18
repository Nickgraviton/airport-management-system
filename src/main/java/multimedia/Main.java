package multimedia;

import java.io.IOException;
import multimedia.controllers.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Loads the main window and lets the controller handle the application
     * @param stage stage of the program
     * @throws IOException is thrown if "MainWindow.fxml" is not found or cannot
     * be opened
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/fxml/MainWindow.fxml"));
        Parent root = loader.load();

        MainWindowController controller = loader.getController();
        controller.setStage(stage);

        stage.getIcons().add(new Image(getClass()
                .getResourceAsStream("/pngs/plane_32x32.png")));
        stage.setTitle("Airport Management System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Launches the application.
     * @param args 
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

}