package com.pep.dubsdk.media;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;

import com.iflytek.aiet.AietListener;
import com.pep.dubsdk.AppApplication;
import com.pep.dubsdk.FileUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 完成语音的录制
 *
 * @ClassName: SoundRecord
 * @date 2012-11-28 下午1:59:57
 */
public class SoundRecorder implements AudioManager.OnAudioFocusChangeListener {

    public static final int RECORDING_MP3 = 1;

    public static final int RECORDING_DEF = 0;

    private int recordingType = 0;

    /**
     * 调用Mp3 Flush所需的最小缓冲区大小
     */
    private static final int LEAST_MP3_FLUSH_LENGTH = 7200;

    /**
     * 最大音量值
     */
    public static final int MAX_VOICE_STRENGTH = 200;

    private AudioRecord mAudioRecorder = null;

    // private Thread mRecordThread = null;

    private AtomicBoolean mIsRecording = new AtomicBoolean(false);

    private Object mLockObj = new Object();

    private int bufferSizeInBytes;

    private AietListener mRecordListener = null; // 录音监听

    /**
     * 设置录音监听 在开始录音之前调用，否则收不到录音数据
     */
    public void setRecordListener(AietListener listener) {
        mRecordListener = listener;
    }

    /**
     * 录音文件key
     */
    private String mFileURL;

    public String getmFileURL() {
        return mFileURL;
    }

    public void setmFileURL(String mFileURL) {
        this.mFileURL = mFileURL;
    }

    /* 单例* */
    private static SoundRecorder mSoundRecord = new SoundRecorder();

    public static SoundRecorder getInstance() {
        return mSoundRecord;
    }

    private SoundRecorder() {
    }

    /* 判断是否增在录音* */
    public boolean isRecording() {
        return mIsRecording.get();
    }

    /**
     * 开始录音
     *
     * @param recordingType 录音类型
     */
    public void startRecording(int recordingType) {
        this.requestAudioFocus();
        this.recordingType = recordingType;
        startRecord();
    }

    /**
     * 开始录音
     *
     * @param
     * @return void
     * @throws
     * @Title: startRecording
     * @date 2012-11-28 下午2:47:09
     */
    public void startRecording() {
        startRecording(RECORDING_DEF);
    }

    private void startRecord() {
        if (mIsRecording.get()) { // 防止同一时刻连续启动多次录音
            return;
        }
        mIsRecording.set(true);
        if (TextUtils.isEmpty(mFileURL)) {
            mFileURL = FileUtils.getTempRecordPath(AppApplication
                    .getInstance());
        }
        if (TextUtils.isEmpty(mFileURL)) {
            ErrorEvent e = new ErrorEvent(ErrorEvent.FILE_CREATE_ERROR_CODE,
                    ErrorEvent.FILE_CREATE_ERROR_TEXT); // 通知UI文件创建出错
            notifyListenerRecordError(e);
            return;
        }
        // 创建AudioRecord对象
        bufferSizeInBytes = AudioRecord.getMinBufferSize(
                SoundConfig.SAMPLE_RATE, SoundConfig.CHANNEL_IN,
                AudioFormat.ENCODING_PCM_16BIT);

        if (mAudioRecorder == null) {
            mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    SoundConfig.SAMPLE_RATE, SoundConfig.CHANNEL_IN,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes * 2);
        }
        if (bufferSizeInBytes > 2048) {
            bufferSizeInBytes = 2048;
        }
        if (mAudioRecorder.getState() != AudioRecord.STATE_INITIALIZED) {
            if (mAudioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                mAudioRecorder.stop();
            }
            mAudioRecorder.release();
            mAudioRecorder = null;
            mIsRecording.set(false); // 录音状态出错，不再进行录音
            ErrorEvent e = new ErrorEvent(
                    ErrorEvent.SYSTEM_RECORDER_ERROR_CODE,
                    ErrorEvent.SYSTEM_RECORDER_ERROR_TEXT);
            notifyListenerRecordError(e);
            return;
        }

        // 开启录音线程
        AppThreadPool
                .executeOrderTask(new RecordingTask(
                        (int) ((1.25 * SoundConfig.SAMPLE_RATE * (16 / 8) * 1
                                * 5 * 2) + LEAST_MP3_FLUSH_LENGTH)));
        // 开启录音线程（旧方法）
        // mRecordThread = new Thread(
        // new RecordingTask((int) ((1.25 * SoundConfig.SAMPLE_RATE
        // * (16 / 8) * 1 * 5 * 2) + LEAST_MP3_FLUSH_LENGTH)));
        // mRecordThread.start();
    }

    /**
     * 停止录音
     *
     * @param
     * @return void
     * @throws
     * @Title: stopRecording
     * @date 2012-11-28 下午2:48:16
     */
    public void stopRecording() {
        clearAudioFocus();
        if (mIsRecording.get()) {
            mIsRecording.set(false);
        }

        AppThreadPool.closeThreadPool();
        // 旧方法
        // if (mRecordThread != null) {
        // try {
        // mRecordThread.join(1000);
        // } catch (InterruptedException e) {
        // }
        // mRecordThread = null;
        // }
        release();
        notifyListenerRecordStop(mFileURL);
    }

    /* 释放mAudioRecorder* */
    private void release() {
        synchronized (mLockObj) {
            if (mAudioRecorder != null) {
                if (mAudioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    mAudioRecorder.stop();
                }
                mAudioRecorder.release();
                mAudioRecorder = null;
            }
        }
    }

    /**
     * 录音线程
     *
     * @ClassName: RecordingTask
     * @date 2012-11-28 下午2:33:51
     */
    public class RecordingTask implements Runnable {

        private OutputStream outStream;

        private MP3Encoder mMp3Encoder = null; // 编码器

        private DataOutputStream mMp3File = null; // 文件输出流

        private short[] leftPcm = null; // 左声道数据

        private short[] rightPcm = null; // 右声道数据

        private byte[] mp3Buffer = null;

        private byte[] mMp3Buffer = null;

        private short[] mBuffer = null;

        private byte[] mByteBuffer = null;

        private int mBufferSize = 0;

        private long mLengthHasRead = 0;

        private int timeout = 5;

        public RecordingTask(int bufferSize) {
            this.mBufferSize = bufferSize;
            mBuffer = new short[mBufferSize];
            mByteBuffer = new byte[mBufferSize * 2];
        }

        private void shortToByte(short[] s, byte[] b) {

            for (int i = 0, j = 0; i < s.length; i++) {

                short temp = s[i];
                b[j++] = (byte) (temp);
                b[j++] = (byte) (temp >> 8);

            }

        }

        /*
         * private void byteToShort(short[] s,byte[] b){
         *
         * int i=0,j=0; while(i<b.length){
         *
         * s[j++] = (short) (b[i++]&0xff00|b[i++]&0x00ff); }
         *
         * }
         */

        /**
         * 计算声音强度(目前以样本点平方平均值为准)
         */
        private int calculateStrength(byte[] buffer, int length, int channels) {
            if (buffer == null || buffer.length == 0 || channels == 0) {
                return 0;
            }
            int lengthInShort = length / (2 * channels);
            if (lengthInShort == 0) {
                return 0;
            }
            double total = 0;
            int scale = 2 * channels;
            for (int i = 0; i < lengthInShort; i++) {
                byte low = buffer[i * scale];
                byte high = buffer[i * scale + 1];
                short value = (short) (((high & 0xFF) << 8) | (low & 0xFF));
                total += value * value;
            }
            int strength = (int) (Math.sqrt(total) / lengthInShort);
            double dStrength = 10 * Math.log10(strength);
            strength = (dStrength > Integer.MAX_VALUE) ? Integer.MAX_VALUE
                    : (int) dStrength;
            strength = 3 * strength + 15;
            if (strength > MAX_VOICE_STRENGTH) {
                strength = MAX_VOICE_STRENGTH;
            }
            if (strength < 0) {
                strength = 0;
            }
            return (int) strength;
        }

        /**
         * 计算声音强度(目前以样本点平方平均值为准)
         */
        private int calculateStrength(short[] buffer, int length, int channels) {
            if (buffer == null || buffer.length == 0 || channels == 0) {
                return 0;
            }
            int lengthInShort = length / (channels);
            if (lengthInShort == 0) {
                return 0;
            }
            double total = 0;
            for (int i = 0; i < lengthInShort; i++) {
                short value = buffer[i];
                total += value * value;
            }
            int strength = (int) (Math.sqrt(total) / lengthInShort);
            double dStrength = 10 * Math.log10(strength);
            strength = (dStrength > Integer.MAX_VALUE) ? Integer.MAX_VALUE
                    : (int) dStrength;
            strength = 3 * strength + 15;
            if (strength > MAX_VOICE_STRENGTH) {
                strength = MAX_VOICE_STRENGTH;
            }
            if (strength < 0) {
                strength = 0;
            }
            return (int) strength;
        }

        private void sleepQuietly(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
            }
        }

        /**
         * 初始化写入文件头信息
         */
        private boolean initAndWriteFileHead() {
            leftPcm = new short[SoundConfig.SAMPLE_RATE * (16 / 8) * 1 * 5];
            rightPcm = new short[SoundConfig.SAMPLE_RATE * (16 / 8) * 1 * 5];
            int len = (int) ((1.25 * leftPcm.length * 2) + LEAST_MP3_FLUSH_LENGTH);
            mp3Buffer = new byte[len];
            mMp3Buffer = new byte[len];

            mMp3Encoder = new MP3Encoder(SoundConfig.SAMPLE_RATE,
                    SoundConfig.NUM_CHANNEL);
            // mMp3Encoder = new MP3Encoder();
            // mMp3Encoder.setInSampleRate(SoundConfig.SAMPLE_RATE);
            // mMp3Encoder.setNumChannels(SoundConfig.NUM_CHANNEL);
            /** 打开文件输出流 */
            outStream = getFileOutputStream(mFileURL);
            if (outStream == null) { // 打开文件输出流失败
                return false;
            }
            mMp3File = new DataOutputStream(new BufferedOutputStream(outStream));
            /** 写入MP3头文件 */
            byte[] id3V2TagBuffer = new byte[4096];
            int size = mMp3Encoder.getId3V2Tag(id3V2TagBuffer);
            while (size > id3V2TagBuffer.length) {
                id3V2TagBuffer = new byte[size];
                mMp3Encoder.getId3V2Tag(id3V2TagBuffer);
            }
            try {
                mMp3File.write(id3V2TagBuffer, 0, size);
            } catch (IOException e) { // 写文件流出错，需要关闭文件流
                try {
                    outStream.close();
                    mMp3File.close();
                } catch (Exception ex) {
                }
                return false;
            }
            return true;
        }

        /**
         * 编码写文件
         */
        private boolean encodPcmBuffer(short[] buffer, int length) {
            try {
                System.arraycopy(buffer, 0, leftPcm, 0, length);
                System.arraycopy(buffer, 0, rightPcm, 0, length);
                int len = mMp3Encoder.encode(leftPcm, rightPcm, length,
                        mp3Buffer, mp3Buffer.length);
                if (len > 0) {
                    mMp3File.write(mp3Buffer, 0, len);
                    mMp3File.flush();
                }
                return true;
            } catch (Exception e) { // 写文件流出错，需要关闭文件流
                try {
                    outStream.close();
                    mMp3File.close();
                } catch (Exception ex) {
                }
                mMp3File = null;
                return false;
            }
        }

        /**
         * 关闭写文件
         */
        private boolean flushAndTags() {
            try {
                int length = mMp3Encoder.flush(mMp3Buffer, bufferSizeInBytes);
                if (length > 0) {
                    mMp3File.write(mMp3Buffer, 0, length);
                    mMp3File.flush();
                }
            } catch (Exception e) { // 写文件流出错，需要关闭文件流
                try {
                    outStream.close();
                    mMp3File.close();
                } catch (IOException ex) {
                }
                return false;
            }
            if (mMp3File != null) {
                try {
                    outStream.close();
                    mMp3File.close();
                } catch (IOException e) {
                }
                mMp3File = null;
            }
            return true;
        }

        public void run() {
            notifyListenerRecordStart();
            switch (recordingType) {
                case RECORDING_DEF:
                    recordPcm();
                    break;
                case RECORDING_MP3:
                    recordMp3();
                    break;
            }
        }

        private void recordPcm() {
            if (mAudioRecorder != null) {
                synchronized (mLockObj) {
                    // 暂停100毫秒再开始录音
                    sleepQuietly(100);
                    try {
                        mAudioRecorder.startRecording();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 初始化输出文件以及codec
                outStream = getFileOutputStream(mFileURL);
                if (outStream == null) { // 打开文件输出流失败
                    ErrorEvent errorEvent = new ErrorEvent(
                            ErrorEvent.FILE_CREATE_ERROR_CODE,
                            ErrorEvent.FILE_CREATE_ERROR_TEXT);
                    notifyListenerRecordError(errorEvent);
                    return;
                }
                mMp3File = new DataOutputStream(new BufferedOutputStream(
                        outStream));
                int i = 0;
                while (mIsRecording.get()) {
                    int perLength = mBufferSize;
                    int length = 0;
                    synchronized (mLockObj) {
                        if (mAudioRecorder != null) {
                            length = mAudioRecorder.read(mBuffer, 0,
                                    bufferSizeInBytes);
                            shortToByte(mBuffer, mByteBuffer);
                            // Log.i("PCM recorder","readRecordData="+count);
                            if (length > 0 && mRecordListener != null) {
                                // 录音数据回调
                                mRecordListener.onBufferReceived(mByteBuffer,
                                        2 * length);
                            }
                        }
                    }
                    if (length <= 0) {
                        // 没有读到数据，停100毫秒再录
                        sleepQuietly(100);
                        // 读取超时时，直接结束录音
                        if (--timeout < 0) {
                            stopRecording();
                            break;
                        }
                        continue;
                    } else {
                        timeout = 3;
                    }
                    if (i++ < 2) {
                        continue;
                    }
                    int strength = calculateStrength(mBuffer, length,
                            SoundConfig.NUM_CHANNEL);
                    mLengthHasRead += length;
                    // 通知上层音量、 已经录制文件的大小
                    int time = (int) mLengthHasRead
                            / (SoundConfig.NUM_CHANNEL * SoundConfig.SAMPLE_RATE);
                    notifyListeneronVolumeAndTime(strength, time);

                    // 对Buffer的相应处理(暂时未保存原始PCM，直接编码MP3)
                    try {
                        for (int j = 0; j < length; j++) {
                            mMp3File.writeShort(mBuffer[j]);
                        }
                        mMp3File.flush();
                    } catch (IOException e) {
                        ErrorEvent errorEvent = new ErrorEvent(
                                ErrorEvent.FILE_WRITE_ERROR_CODE,
                                ErrorEvent.FILE_WRITE_ERROR_TEXT);
                        notifyListenerRecordError(errorEvent);
                        return;
                    }
                }
                // 结束之后
                try {
                    outStream.close();
                    mMp3File.close();
                } catch (IOException ex) {
                    ErrorEvent errorEvent = new ErrorEvent(
                            ErrorEvent.FILE_WRITE_ERROR_CODE,
                            ErrorEvent.FILE_WRITE_ERROR_TEXT);
                    notifyListenerRecordError(errorEvent);
                }
            }
        }

        private void recordMp3() {
            if (mAudioRecorder != null) {
                synchronized (mLockObj) {
                    mAudioRecorder.startRecording();
                }
                // 初始化输出文件以及codec
                boolean result = initAndWriteFileHead();
                if (!result) {
                    ErrorEvent errorEvent = new ErrorEvent(
                            ErrorEvent.FILE_CREATE_ERROR_CODE,
                            ErrorEvent.FILE_CREATE_ERROR_TEXT);
                    notifyListenerRecordError(errorEvent);
                    return;
                }
                int i = 0;
                while (mIsRecording.get()) {

                    // int perLength = mBufferSize;
                    int length = 0;
                    synchronized (mLockObj) {
                        if (mAudioRecorder != null) {
                            length = mAudioRecorder.read(mBuffer, 0,
                                    bufferSizeInBytes);
                        }
                        shortToByte(mBuffer, mByteBuffer);
                        // Log.i("PCM recorder","readRecordData="+count);
                        if (length > 0 && mRecordListener != null) {
                            // 录音数据回调
                            mRecordListener.onBufferReceived(mByteBuffer,
                                    2 * length);
                        }
                    }
                    if (length <= 0) {
                        // 没有读到数据，停100毫秒再录
                        sleepQuietly(100);
                        // 读取超时时，直接结束录音
                        if (--timeout < 0) {
                            stopRecording();
                            break;
                        }
                        continue;
                    } else {
                        timeout = 3;
                    }
                    if (i++ < 2) {
                        continue;
                    }
                    int strength = calculateStrength(mBuffer, length,
                            SoundConfig.NUM_CHANNEL);
                    mLengthHasRead += length;
                    // 通知上层音量、 已经录制文件的大小
                    int time = (int) mLengthHasRead
                            / (SoundConfig.NUM_CHANNEL * SoundConfig.SAMPLE_RATE);
                    notifyListeneronVolumeAndTime(strength, time);

                    // 对Buffer的相应处理(暂时未保存原始PCM，直接编码MP3)
                    boolean encodeResult = encodPcmBuffer(mBuffer, length);
                    if (!encodeResult) {
                        ErrorEvent errorEvent = new ErrorEvent(
                                ErrorEvent.FILE_WRITE_ERROR_CODE,
                                ErrorEvent.FILE_WRITE_ERROR_TEXT);
                        notifyListenerRecordError(errorEvent);
                        return;
                    }
                }
                // 结束之后
                boolean endResult = flushAndTags();
                if (!endResult) {
                    ErrorEvent errorEvent = new ErrorEvent(
                            ErrorEvent.FILE_WRITE_ERROR_CODE,
                            ErrorEvent.FILE_WRITE_ERROR_TEXT);
                    notifyListenerRecordError(errorEvent);
                    return;
                }
            }

        }
    }// 结束录音线程

    // -------------以下为外部监听代码
    // ------------------------------
    private List<SoundRecordListerner> mRecordListenerList = new ArrayList<SoundRecordListerner>();

    private List<SoundRecordErrorListerner> mErrorListenerList = new ArrayList<SoundRecordErrorListerner>();

    /**
     * 播放过程中各种事件的监听(事件类型稍后定义)
     *
     * @author Xiaopo
     * @ClassName: SoundPlayListerner
     * @date 2012-11-29 下午4:25:19
     */
    public static interface SoundRecordListerner {

        public void onRecordStart();

        /**
         * @param @param path
         * @return void
         * @throws
         * @Title: onRecordStop
         * @Description: 录制结束回调
         * @author Xiaopo
         * @date 2012-12-4 下午7:32:28
         */
        public void onRecordStop(String path);

        /**
         * @param @param volume
         * @param @param time
         * @return void
         * @throws
         * @Title: onVolumeAndTime
         * @Description: 录制过程中音量和时间的回调
         * @author Xiaopo
         * @date 2012-12-4 下午7:33:14
         */
        public void onVolumeAndTime(int volume, int time);

    }

    /**
     * @author Xiaopo
     * @ClassName: SoundRecordErrorListerner
     * @Description: 录制过程中错误时间的监听
     * @date 2012-12-4 下午7:34:01
     */
    public static interface SoundRecordErrorListerner {

        /**
         * @param @param envent
         * @return void
         * @throws
         * @Title: onRecordError
         * @Description: 录制过程中错误回调
         * @author Xiaopo
         * @date 2012-12-4 下午7:33:40
         */
        public void onRecordError(ErrorEvent envent);

    }

    /**
     * @param @param listener
     * @return void
     * @throws
     * @Title: registerSoundRecordErrorListerner
     * @Description: 注册错误事件监听
     * @author Xiaopo
     * @date 2012-12-4 下午7:37:48
     */
    public void registerSoundRecordErrorListerner(
            SoundRecordErrorListerner listener) {
        if (listener != null && !mErrorListenerList.contains(listener)) {
            mErrorListenerList.add(listener);
        }
    }

    /**
     * @param @param listener
     * @return void
     * @throws
     * @Title: unRegisterSoundRecordErrorListerner
     * @Description: 取消错误事件的监听
     * @author Xiaopo
     * @date 2012-12-4 下午7:38:30
     */
    public void unRegisterSoundRecordErrorListerner(
            SoundRecordErrorListerner listener) {
        if (listener != null && mErrorListenerList.contains(listener)) {
            mErrorListenerList.remove(listener);
        }
    }

    /**
     * @param @param listener
     * @return void
     * @throws
     * @Title: registerSoundRecordListener
     * @Description: 注册录制事件监听
     * @author Xiaopo
     * @date 2012-12-4 下午7:38:50
     */
    public void registerSoundRecordListener(SoundRecordListerner listener) {
        if (listener != null && !mRecordListenerList.contains(listener)) {
            mRecordListenerList.add(listener);
        }
    }

    /**
     * @param @param listener
     * @return void
     * @throws
     * @Title: unRegisterSoundRecordListener
     * @Description: 取消录制事件监听
     * @author Xiaopo
     * @date 2012-12-4 下午7:39:12
     */
    public void unRegisterSoundRecordListener(SoundRecordListerner listener) {
        if (listener != null && mRecordListenerList.contains(listener)) {
            mRecordListenerList.remove(listener);
        }
    }

    private void notifyListenerRecordStart() {
        int size = mRecordListenerList.size();
        for (int i = 0; i < size; i++) {
            if (i > mRecordListenerList.size() - 1) {
                break;
            }
            mRecordListenerList.get(i).onRecordStart();
        }
    }

    /**
     * 通知录制停止
     */
    private void notifyListenerRecordStop(String path) {
        int size = mRecordListenerList.size();
        for (int i = 0; i < size; i++) {
            if (i > mRecordListenerList.size() - 1) {
                break;
            }
            mRecordListenerList.get(i).onRecordStop(path);
        }
    }

    /**
     * 通知音量和录制时间
     */
    private void notifyListeneronVolumeAndTime(int volume, int time) {
        int size = mRecordListenerList.size();
        for (int i = 0; i < size; i++) {
            mRecordListenerList.get(i).onVolumeAndTime(volume, time);
        }
    }

    /**
     * 通知录制错误 同时停止录音通知UI停止
     */
    private void notifyListenerRecordError(ErrorEvent errorEvent) {
        int size = mErrorListenerList.size();
        for (int i = 0; i < size; i++) {
            mErrorListenerList.get(i).onRecordError(errorEvent);
        }
        stopRecording();
    }

    AudioManager mAudioManager = (AudioManager) AppApplication.getInstance()
            .getSystemService(Context.AUDIO_SERVICE);

    private boolean requestAudioFocus() {
        int res = mAudioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == res;
    }

    private void clearAudioFocus() {
        mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                stopRecording();
                break;
        }
    }

    private OutputStream getFileOutputStream(String absPath) {
        if (absPath == null) {
            return null;
        }
        File mFile = new File(absPath);
        File parentFile = mFile.getParentFile();
        if (parentFile == null) {
            return null;
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        OutputStream input = null;
        try {
            input = new FileOutputStream(mFile);
            return input;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
