package sample;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    //FXML File attributes
    @FXML
    Pane pane;
    @FXML
    Label textL;
    @FXML
    Button play,pause,next,previous,reset;
    @FXML
    Slider slider;
    @FXML
    ComboBox<String> speed;
    @FXML
    ProgressBar bar;

    //File of Audio
    File dir;
    File[] files;
    ArrayList<File> nasheed;
    private int no;
    int [] speeds = {25,50,75,100,125,150,175,200};
    Timer timer;
    TimerTask task;
    boolean running;

    //For playing audio
    Media media;
    MediaPlayer player;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nasheed = new ArrayList<File>();
        no = 0;
        dir = new File("music");

        //If no files found
        files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                nasheed.add(file);
            }
        }

        //initializing media
        media = new Media(nasheed.get(no).toURI().toString());
        player = new MediaPlayer(media);
        textL.setText(nasheed.get(no).getName());

        for (int i = 0; i < speeds.length; i++) {
            speed.getItems().add(String.valueOf(speeds[i]) + "%");
        }
        speed.setOnAction(this::speed);

        //slider of speed of audio
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                player.setVolume(slider.getValue() * 0.01);
            }
        });
    }


    //Play Button Action
    public void play(){
        timer();
        player.setVolume(slider.getValue()*0.01);
        speed(null);
        player.play();;
    }
    //Pause Button
    public void pause(){
        cancelTimer();;
        player.pause();
    }
    //Reset
    public void reset(){
        player.stop();
        media = new Media(nasheed.get(no).toURI().toString());
        player = new MediaPlayer(media);
        play();

    }
    //Previous audio
    public void previous(){
        if(running)
            cancelTimer();
        if(no>0){
            no--;

            player.stop();;

            media = new Media(nasheed.get(no).toURI().toString());
            player = new MediaPlayer(media);

            textL.setText(nasheed.get(no).getName());
        }
        else{
            no = nasheed.size()-1;

            player.stop();;

            media = new Media(nasheed.get(no).toURI().toString());
            player = new MediaPlayer(media);

            textL.setText(nasheed.get(no).getName());
        }
        play();;

    }
    //Next audio
    public void next(){
        if(running)
            cancelTimer();
        if(no<nasheed.size()-1){
            no++;

            player.stop();;

            media = new Media(nasheed.get(no).toURI().toString());
            player = new MediaPlayer(media);

            textL.setText(nasheed.get(no).getName());
        }
        else{
            no = 0;

            player.stop();;

            media = new Media(nasheed.get(no).toURI().toString());
            player = new MediaPlayer(media);

            textL.setText(nasheed.get(no).getName());
        }
        play();;

    }

    //increasing speed
    public void speed(ActionEvent event){

        if(speed.getValue() == null){
            player.setRate(1);
        }
        else {
            player.setRate(Integer.parseInt(speed.getValue().substring(0, speed.getValue().length() - 1)) * 0.01);
        }
    }

    //timer for progress bar
    public void timer(){

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                running = true;
                double cur = player.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                bar.setProgress(cur/end);

                if(cur/end == 1){
                    cancelTimer();
                }

            }
        };
        timer.scheduleAtFixedRate(task, 1000,1000);
    }
    public void cancelTimer(){
        running = false;
    }

}

