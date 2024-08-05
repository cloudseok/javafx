package com.itgroup.application;

import com.itgroup.controller.UFCController;
import com.itgroup.utility.Utility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;


public class UFC extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = Utility.FXML_PATH + "UFC.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load();

        if (container instanceof BorderPane) {
            BorderPane borderPane = (BorderPane) container;  // 캐스팅

            // GIF 파일을 배경으로 설정
            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/itgroup/images/bigThunder.gif")));
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.REPEAT,  // 수평
                    BackgroundRepeat.REPEAT,  // 수직
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT
            );
            borderPane.setBackground(new Background(bgImage));
        }

        Scene scene = new Scene(container);

        // 컨트롤러에 HostServices 설정(홈페이지 열기 위함)
        UFCController controller = fxmlLoader.getController();
        controller.setHostServices(getHostServices());

        // CSS 파일 추가
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/itgroup/css/UFC.css")).toExternalForm());

        stage.setTitle("UFC Players");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
