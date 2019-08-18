package com.maxfour.music.helper;

import java.util.Locale;

/**
 * Simple thread safe stop watch.
 */
public class StopWatch {//Đồng hồ bấm giờ

    /**
     *Thời gian đồng hồ bấm giờ đã bắt đầu.
     */
    private long startTime;

    /**
     *Thời gian trôi qua trước hiện tại {@link #startTime}.
     */
    private long previousElapsedTime;

    /**
     * Cho dù đồng hồ bấm giờ hiện đang chạy hay không.
     */
    private boolean isRunning;

    /**
     * Bắt đầu hoặc tiếp tục đồng hồ bấm giờ.
     */
    public void start() {
        synchronized (this) {
            startTime = System.currentTimeMillis();
            isRunning = true;
        }
    }

    /**
     * Tạm dừng đồng hồ bấm giờ. Nó có thể được tiếp tục sau {@link #start()}.
     */
    public void pause() {
        synchronized (this) {
            previousElapsedTime += System.currentTimeMillis() - startTime;
            isRunning = false;
        }
    }

    /**
     * Stops and resets the stop watch to zero milliseconds.
     */
    public void reset() {
        synchronized (this) {
            startTime = 0;
            previousElapsedTime = 0;
            isRunning = false;
        }
    }

    /**
     * @return the total elapsed time in milliseconds
     */
    public final long getElapsedTime() {
        synchronized (this) {
            long currentElapsedTime = 0;
            if (isRunning) {
                currentElapsedTime = System.currentTimeMillis() - startTime;
            }
            return previousElapsedTime + currentElapsedTime;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%d millis", getElapsedTime());
    }
}
