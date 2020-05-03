package com.drone.util;

public class SrtFrame {

    private long frame;
    private long startTime;
    private long endTime;
    private String text;

    public SrtFrame() {
    }

    public SrtFrame(long frame, long startTime, long endTime, String text) {
        this.frame = frame;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getFrame() {
        return frame;
    }

    public SrtFrame setFrame(long frame) {
        this.frame = frame;
        return this;
    }

    public SrtFrame setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getEndTime() {
        return endTime;
    }

    public SrtFrame setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getText() {
        return text;
    }

    public SrtFrame setText(String text) {
        this.text = text;
        return this;
    }

    public String toString() {
        String r = Long.toString(this.frame) + "\n";
        r = r + parseTime(this.startTime) + " --> " + parseTime(this.endTime) + "\n" +
                this.text + "\n\n";
        return r;
    }

    private String parseTime(long time) {

        int hours = (int) time/1000/60/60;
        time = time - (hours*60*60*1000);
        int minutes = (int) time/1000/60;
        time = time - (minutes*60*1000);
        int seconds = (int) time/1000;
        time = time - (seconds*1000);
        int milliSeconds = (int) time;

        return padIntWithZeros(hours,2) + ":" +
                padIntWithZeros(minutes, 2) + ":" +
                padIntWithZeros(seconds, 2) + "," +
                padIntWithZeros(milliSeconds, 3);
    }

    private String padIntWithZeros(int value, int totalDigits) {
        String r = null;

        r = Integer.toString(value);
        if (r.length() < totalDigits) {
            int zerosToAdd = totalDigits - r.length();
            for (int j = 0; j < zerosToAdd; j++) {
                r = "0" + r;
            }
        }

        return r;
    }

}
