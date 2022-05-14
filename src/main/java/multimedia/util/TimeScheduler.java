package multimedia.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import multimedia.controllers.MainWindowController;

import java.util.Timer;

/**
 * Scheduler class that handles timer tasks and updates the UI at regular intervals.
 * Singleton class since we need only once instance.
 */
public class TimeScheduler {
    // 5 real world seconds correspond to 1 simulation minute
    private final int minuteDuration = 5;
    private Timer timer;
    private Timeline timeline;
    private int currentTime;
    private static final TimeScheduler timeScheduler = new TimeScheduler();

    private TimeScheduler() {
        currentTime = 0;
        timer = new Timer();
    }

    public static TimeScheduler getInstance() {
        return timeScheduler;
    }

    public int getMinuteDuration() {
        return minuteDuration;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(MainWindowController controller) {
        this.timeline = new Timeline(
                new KeyFrame(Duration.seconds(minuteDuration), e -> {
                    currentTime++;
                    Platform.runLater(() -> controller.updateUI(null));
                })
        );
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }
}
