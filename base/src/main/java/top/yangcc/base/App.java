package top.yangcc.base;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Track;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 */
@Slf4j
public class App extends Application {

    private static boolean musicStatus;

    @Override
    public void init() throws Exception {
        log.info("App.init(){}" + Thread.currentThread().getName());
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent(), 700, 500));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        log.info("App.stop(){}" + Thread.currentThread().getName());
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

        // 底部
        VBox bottomVBox = new VBox();
        borderPane.setBottom(bottomVBox);

        // 进度条
        Slider scheduleSlider = new Slider();
        // 底部按钮
        HBox bottom = new HBox();
        bottomVBox.getChildren().addAll(scheduleSlider, bottom);

        // 声音
        Slider volumeSlider = new Slider(0, 1, 0.5);

        Button left = new Button("上一首");

        Button button = new Button("播放");

        Button right = new Button("下一首");

        scheduleSlider.setStyle("-fx-background-color: #63a05f");

        bottom.setStyle("-fx-background-color: #63a05f");
        bottom.setPrefSize(100, 40);

        Media media = new Media(App.class.getClassLoader().getResource("zjl.mp3").toExternalForm());
        log.info("media source{}" + media.getSource());

        SimpleBooleanProperty isReady = new SimpleBooleanProperty();
        isReady.set(false);

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);

        // 等准备好，则可以播放
        mediaPlayer.setOnReady(() -> {
            log.info("准备完成");
            isReady.set(true);
            // 音乐声音绑定
            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
            // 进度条绑定
            bindSchedule(scheduleSlider, mediaPlayer);

            // 音乐轨道
            int size = media.getTracks().size();
            log.info("音轨长度{}", size);

            for (Track tracks : media.getTracks()) {
                log.info("音轨数据{}", tracks);
            }
            // 可以获取歌曲信息
            ObservableMap<String, Object> metadata = media.getMetadata();
            metadata.forEach((key, value) -> {
                log.info("key:{} value:{}", key, value);
            });
            //可以用来解决歌词自动滚动，但是直接加载歌词，然后放map就好了，没有必要用这个
            // media.getMarkers().put("a", Duration.seconds(5));
            // mediaPlayer.setOnMarker(new EventHandler<MediaMarkerEvent>() {
            //     @Override
            //     public void handle(MediaMarkerEvent event) {
            //     log.info("歌词{}", event.getMarker());
            //     }
            // }
            // );

            mediaPlayer.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                    log.info("频谱数据{} {}", new Gson().toJson(magnitudes),new Gson().toJson(phases));
                }
            });


        });

        button.setOnAction(event -> {
            if (!isReady.get()) {
                log.warn("没有准备好");
            } else {
                musicStatus = !musicStatus;
                if (musicStatus) {
                    mediaPlayer.play();
                } else {
                    mediaPlayer.pause();
                }
            }
        });

        // slider.setPrefWidth(100);

        // 放入
        bottom.getChildren().setAll(left, button, right, volumeSlider);

        return borderPane;
        // borderPane.setBottom(button);

    }

    private void bindSchedule(Slider scheduleSlider, MediaPlayer mediaPlayer) {
        // 进度条初始化
        scheduleSlider.setValue(0);
        scheduleSlider.setMin(0);
        scheduleSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());

        // 鼠标是否按住布尔值
        SimpleBooleanProperty isScheduleMouseCheck = new SimpleBooleanProperty(false);

        // 进度条设置进度
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
                    Duration newValue) {
                // 如果鼠标按住就不设置值
                if (!isScheduleMouseCheck.get()) {
                    scheduleSlider.setValue(newValue.toSeconds());
                }
            }
        });

        // 鼠标按下进度条绑定
        scheduleSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isScheduleMouseCheck.set(true);
            }
        });

        // 鼠标释放进度条绑定
        scheduleSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // 音乐进度到当前值
                mediaPlayer.seek(Duration.seconds(scheduleSlider.getValue()));
                isScheduleMouseCheck.set(false);
            }
        });

    }
}
