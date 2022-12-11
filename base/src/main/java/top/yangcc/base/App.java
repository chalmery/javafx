package top.yangcc.base;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 */
@Slf4j
public class App extends Application {

    private static boolean musicStatus;

    @Override
    public void init() throws Exception {
        log.info("App.init()" + Thread.currentThread().getName());
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(), 700, 500));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        log.info("App.stop()" + Thread.currentThread().getName());
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 返回布局
     *
     * @return
     */
    private Parent createContent() {
        BorderPane borderPane = new BorderPane();
        HBox center = new HBox();
        center.setStyle("-fx-background-color: cadetblue");
        center.setPrefSize(100, 100);
        borderPane.setCenter(center);

        HBox leftHBox = new HBox();
        leftHBox.setStyle("-fx-background-color: #8a5fa0");
        leftHBox.setPrefSize(100, 100);
        borderPane.setLeft(leftHBox);


        //底部
        VBox bottomVBox = new VBox();
        borderPane.setBottom(bottomVBox);


        //进度条
        Slider volumeSlider =  new Slider(0,1,0);
        //底部按钮
        HBox bottom = new HBox();
        bottomVBox.getChildren().addAll(volumeSlider,bottom);


        bottom.setStyle("-fx-background-color: #63a05f");
        bottom.setPrefSize(100, 40);

        Media media = new Media(App.class.getClassLoader().getResource("zjl.mp3").toExternalForm());
        log.info("media source:" + media.getSource());
        
        SimpleBooleanProperty isReady = new SimpleBooleanProperty();
        isReady.set(false);

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);

        //等准备好，则可以播放
        mediaPlayer.setOnReady(()->{
            log.info("准备完成");
            isReady.set(true);
        });

        Button left = new Button("上一首");


        Button button = new Button("播放");
        button.setOnAction(event -> {
            if(!isReady.get()){
                log.warn("没有准备好");
            }else{
                musicStatus = !musicStatus;
                if (musicStatus) {
                    mediaPlayer.play();
                } else {
                    mediaPlayer.pause();
                }
            }
        });



        Button right = new Button("下一首");

        Slider slider = new Slider(0, 1, 0);
        slider.setPrefWidth(100);

        // 放入
        bottom.getChildren().setAll(left, button, right, slider);

        return borderPane;
        // borderPane.setBottom(button);

    }
}
