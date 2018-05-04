package com.pep.dubsdk.event;

public class PlayControlEvent {
    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;
    private int eventType;
    private String fileName;
    private String videoUrl;

    public PlayControlEvent(int eventType) {
        this.eventType = eventType;
    }

    public int getEventType() {
        return eventType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
