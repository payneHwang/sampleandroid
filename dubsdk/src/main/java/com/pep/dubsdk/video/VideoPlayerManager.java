package com.pep.dubsdk.video;

/**
 * Put JCVideoPlayer into layout
 * From a JCVideoPlayer to another JCVideoPlayer
 * Created by Nathen on 16/7/26.
 */
public class VideoPlayerManager {

    public static VideoPlayer FIRST_FLOOR_JCVD;
    public static VideoPlayer SECOND_FLOOR_JCVD;

    public static void setFirstFloor(VideoPlayer jcVideoPlayer) {
        FIRST_FLOOR_JCVD = jcVideoPlayer;
    }

    public static void setSecondFloor(VideoPlayer jcVideoPlayer) {
        SECOND_FLOOR_JCVD = jcVideoPlayer;
    }

    public static VideoPlayer getFirstFloor() {
        return FIRST_FLOOR_JCVD;
    }

    public static VideoPlayer getSecondFloor() {
        return SECOND_FLOOR_JCVD;
    }

    public static VideoPlayer getCurrentJcvd() {
        if (getSecondFloor() != null) {
            return getSecondFloor();
        }
        return getFirstFloor();
    }

    public static void completeAll() {
        if (SECOND_FLOOR_JCVD != null) {
            SECOND_FLOOR_JCVD.onCompletion();
            SECOND_FLOOR_JCVD = null;
        }
        if (FIRST_FLOOR_JCVD != null) {
            FIRST_FLOOR_JCVD.onCompletion();
            FIRST_FLOOR_JCVD = null;
        }
    }
}
