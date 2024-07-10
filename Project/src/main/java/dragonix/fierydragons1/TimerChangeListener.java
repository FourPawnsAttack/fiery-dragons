package dragonix.fierydragons1;

/**
 * The TimerChangeListener interface should be implemented by classes that wish to receive
 * notifications when the timer changes in the game.
 */
public interface TimerChangeListener {
    /**
     * This method is called whenever the timer changes.
     *
     * @param timeRemaining the amount of time remaining, in seconds
     */
    void onTimerChange(int timeRemaining);
}
