
package com.iflytek.recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import com.iflytek.aiet.AietListener;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaRecorder.AudioSource;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {

    private static final int DEFAULT_SAMPLE_RATE_16K = 16 * 1000;// 默认采样率
    private static final short DEFAULT_BIT_SAMPLES_16bit = 16; // 默认单个采样编码的比特数
    private static final int RECORD_BUFFER_TIMES_FOR_FRAME = 10; // 录音器缓冲区大小为间隔的帧长度的倍数10模
    // private static final int DEFAULE_TIMES_INTERVAL = 20; //
    // 默认每次读取数据的时间间隔（ms）
    private static final short DEFAULT_CHANNELS = 1; // 默认声道数单通道

    /* private static final int DEFAULT_MAX_VOLUME_LEVEL = 10; */
    private AudioRecord m_Recorder = null; // 录音器
    private byte[] m_dataBuffer = null; // 录音数据缓存
    private boolean m_bisWriteFile = true; // 写文件标志
    private static String m_Curdir = "/sdcard/aiet/record/";
    private static String TAG = "PCM Recorder";
    private static int m_currentNumber = 0; // 录音数据写文件的编号
    private static String m_CurWavPath = null;
    private FileOutputStream fOut = null;
    private File foutfile = null;
    private AietListener m_recordListener = null; // 录音监听
    private short m_channels = DEFAULT_CHANNELS;
    private short m_bitSamples = DEFAULT_BIT_SAMPLES_16bit; // 采样的bit位
    private int m_sampleRateInHz = DEFAULT_SAMPLE_RATE_16K;
    private int m_timeInterval = 50;
    private int m_in_buf_size = 0;

    public AudioRecorder() {
        // this(DEFAULT_SAMPLE_RATE_16K);
        m_Recorder = findAudioRecord();
    }

    public AudioRecorder(int bitSamples) {
        m_sampleRateInHz = bitSamples; // 采样率
        //Log.i("PCM Recorder", "m_sampleRateInHz=" + m_sampleRateInHz);
        int framePeriod = m_sampleRateInHz * m_timeInterval / 1000; // periodInFrames
                                                                    // update
                                                                    // period
                                                                    // expressed
                                                                    // in frames

        int recordBufferSize = 10 * framePeriod * RECORD_BUFFER_TIMES_FOR_FRAME * m_bitSamples
                * m_channels / 8;
        m_in_buf_size = AudioRecord.getMinBufferSize(m_sampleRateInHz,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        //Log.i("PCM Recorder", "getMinBufferSize = " + m_in_buf_size);
        // if (m_in_buf_size < 4096)
        // m_in_buf_size = 4096;
        if (AudioRecord.ERROR_BAD_VALUE != m_in_buf_size) {
            if (recordBufferSize < m_in_buf_size) {
                recordBufferSize = m_in_buf_size * 2;
//                Log.w("PCM recorder", "Increasing buffer size to "
//                        + Integer.toString(recordBufferSize));
            }
            //Log.i("PCM Recorder", "new AudioRecord");
            m_Recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    m_sampleRateInHz, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size + 100);

            // 创建失败则停止服务
            if (m_Recorder.getState() != AudioRecord.STATE_INITIALIZED) {
                m_Recorder.release();
                m_Recorder = null;
            }

            // 设置录音进度更新监听
            //Log.i("PCM recorder", " framePeriod =" + framePeriod);
            m_Recorder.setRecordPositionUpdateListener(_mRecordListener);
            // 检测的时间
            m_Recorder.setPositionNotificationPeriod(framePeriod);

            // 初始化缓冲区和评测结果数组
            m_dataBuffer = new byte[framePeriod * m_channels * m_bitSamples / 8];
        }
        else {
        }
    }

    //44100, 22050 and 11025
    private static int[] mSampleRates = new int[] {
            8000, 16000, 11025, 22050, 44100
    };

    public AudioRecord findAudioRecord() {
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] {
                    AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT
            }) {
                for (short channelConfig : new short[] {
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_CONFIGURATION_MONO
                }) {
                    try {
                        
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig,
                                audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(AudioSource.DEFAULT, rate,
                                    channelConfig, audioFormat, bufferSize+100);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                m_bitSamples = audioFormat;
                                m_sampleRateInHz = rate;
                                // 初始化缓冲区和评测结果数组
                                int framePeriod = rate * m_timeInterval / 1000; // periodInFrames
                                                                                // update
                                                                                // period
                                                                                // expressed
                                                                                // in
                                                                                // frames
                                //xiao mi 1s 09-13 15:07:59.179: D/PCM Recorder(12494): Attempting rate 8000Hz, bits: 2, channel: 16
                                m_dataBuffer = new byte[framePeriod * m_channels * m_bitSamples / 8];
//                                Log.d(TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat
//                                        + ", channel: "
//                                        + channelConfig);
                                
                                
                                return recorder;
                            }else {
                                recorder.release();
                                recorder = null;
                            }
                        }

                    } catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * Scan for the best configuration parameter for AudioRecord object on this
     * device. Constants value are the audio source, the encoding and the number
     * of channels. That means were are actually looking for the fitting sample
     * rate and the minimum buffer size. Once both values have been determined,
     * the corresponding program variable are initialized (audioSource,
     * sampleRate, channelConfig, audioFormat) For each tested sample rate we
     * request the minimum allowed buffer size. Testing the return value let us
     * know if the configuration parameter are good to go on this device or not.
     * This should be called in at start of the application in onCreate().
     */
    public void initRecorderParameters(int[] sampleRates) {

        for (int i = 0; i < sampleRates.length; ++i) {
            try {
                //Log.i(TAG, "Indexing " + sampleRates[i] + "Hz Sample Rate");
                int tmpBufferSize = AudioRecord.getMinBufferSize(sampleRates[i],
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                // Test the minimum allowed buffer size with this configuration
                // on this device.
                if (tmpBufferSize != AudioRecord.ERROR_BAD_VALUE) {
                    // Seems like we have ourself the optimum AudioRecord
                    // parameter for this device.
                    AudioRecord tmpRecoder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            sampleRates[i],
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            tmpBufferSize);
                    // Test if an AudioRecord instance can be initialized with
                    // the given parameters.
                    if (tmpRecoder.getState() == AudioRecord.STATE_INITIALIZED) {
                        String configResume = "initRecorderParameters(sRates) has found recorder settings supported by the device:"
                                + "\nSource   = MICROPHONE"
                                + "\nsRate    = " + sampleRates[i] + "Hz"
                                + "\nChannel  = MONO"
                                + "\nEncoding = 16BIT";
                        //Log.i(TAG, configResume);

                        // +++Release temporary recorder resources and leave.
                        tmpRecoder.release();
                        tmpRecoder = null;

                        return;
                    }
                } else {
                    //Log.w(TAG, "Incorrect buffer size. Continue sweeping Sampling Rate...");
                }
            } catch (IllegalArgumentException e) {
            }
        }
    }

    // 录音构造函数

    /**
     * 开始录音
     * 
     * @throws FileNotFoundException
     */
    public void startRecording() throws FileNotFoundException {
        if (m_Recorder != null) {
            if (m_bisWriteFile) {
                foutfile = new File(getRecordFilePath());
                fOut = new FileOutputStream(foutfile);
                //Log.d("PCM recorder", m_CurWavPath);
            }
            m_Recorder.startRecording();
            readRecordData();
        }
    }

    /**
     * 停止录音
     * 
     * @throws IOException
     */
    public void stopRecording() throws IOException {
        if (m_Recorder != null) {
            m_Recorder.stop();
            if (m_bisWriteFile) {
                if (fOut != null) {
                    fOut.close();
                    fOut = null;
                }
            }
        }
    }

    /**
     * 释放录音器
     */
    public void release() {
        if (m_Recorder != null) {
            m_Recorder.release();
            try {
                if (m_bisWriteFile) {
                    if (fOut != null) {
                        fOut.close();
                        fOut = null;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            m_Recorder = null;
        }
    }

    /**
     * 取得当前的录音状态
     * 
     * @return RECORDSTATE_STOPPED：停止录音 RECORDSTATE_RECORDING:正在录音
     */
    public int getRecordingState() {
        if (m_Recorder != null) {
            return m_Recorder.getRecordingState();
        } else {
            return AudioRecord.RECORDSTATE_STOPPED;
        }
    }

    private AudioRecord.OnRecordPositionUpdateListener _mRecordListener = new OnRecordPositionUpdateListener() {

        public void onMarkerReached(AudioRecord recorder) {
            // not used

        }

        public void onPeriodicNotification(AudioRecord recorder) {
            // 周期性的读取数据
            readRecordData();
        }
    };

    /**
     * 设置录音监听 在开始录音之前调用，否则收不到录音数据
     */
    public void setRecordListener(AietListener listener) {
        m_recordListener = listener;
    }

    public void readRecordData() {
        if (m_Recorder != null) {
            int count = m_Recorder.read(m_dataBuffer, 0, m_dataBuffer.length);
            // Log.i("PCM recorder","readRecordData="+count);
            if (count > 0 && m_recordListener != null) {
                // 录音数据回调
                m_recordListener.onBufferReceived(m_dataBuffer, count);
                if (m_bisWriteFile) {
                    writeDate(m_dataBuffer);
                }
            }
        }
    }

    // 是否将语音数据写入文件，默认为false不写
    public void setWriteFileFlag(boolean flag) {
        m_bisWriteFile = flag;
    }

    /**
     * 写PCM数据到文件中
     * 
     * @param bufffer
     */
    private void writeDate(byte[] bufffer) {
        if (isExternalStorageAvailabl()) {
            try {
                fOut.write(bufffer);
                // Log.i("PCM recorder", "writeDate");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 设置音频文件的编号，作为文件名区别
     */
    private String getRecordFilePath() {
        // 计算音频文件下标
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        // 判断当前的路径存不存？
        newFolder(m_Curdir);
        m_CurWavPath = m_Curdir + "Aiet" + formatter.format(curDate) + ".pcm";
        m_currentNumber++;
        //Log.i(TAG, m_CurWavPath);
        ;
        return m_CurWavPath;
    }

    public static String getCurWavPath() {
        return m_CurWavPath;
    }

    public static int getCurrentNumber() {
        return m_currentNumber;
    }

    /**
     * 判断存储卡状态是否可用
     * 
     * @return 可用则返回true，否则返回false
     */
    private static boolean isExternalStorageAvailabl() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    // 新建一个文件夹()
    public static void newFolder(String folderPath) {
        if (!isExternalStorageAvailabl()) {
            return;
        }
        try {
            File destDir = new File(folderPath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } catch (Exception e) {
            System.out.println("新建文件夹操作出错--" + folderPath);
            e.printStackTrace();
        }
    }

    // 删除文件夹
    public static void delFolder(String folderPath) {
        if (!isExternalStorageAvailabl()) {
            return;
        }
        try {
            String filePath = folderPath;
            File delPath = new File(filePath);
            delPath.delete();
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错--" + folderPath);
            e.printStackTrace();
        }
    }

    // 设置语音保存的文件根目录
    public static void setSaveDir(String dir) {
        m_Curdir = dir;
        //Log.i(TAG, m_Curdir);
        newFolder(m_Curdir);
    }

    // 计算音量的大小
    public static short[] byteArray2ShortArray(byte[] data, int items) {
        short[] retVal = new short[items];
        for (int i = 0; i < retVal.length; i++)
            retVal[i] = (short) ((data[i * 2] & 0xff) | (data[i * 2 + 1] & 0xff) << 8);
        return retVal;
    }

    // 200个点的最大值是：最小值是40，最大值为：72.3
    public static int GetAudioLevel(byte[] audio, int dwSamplesPreFrame)
    {
        float fEnergy;
        int nAudioLevel;

        dwSamplesPreFrame /= 2;
        short[] fFrame = byteArray2ShortArray(audio, dwSamplesPreFrame);
        fEnergy = calEnergy(fFrame, dwSamplesPreFrame);

        fEnergy = (float) (8 * Math.log10(fEnergy / dwSamplesPreFrame));
        // 值是由目的(计算到0-10)
        if (fEnergy < 40) {
            nAudioLevel = 0;
        }
        else if (fEnergy > 65) {
            nAudioLevel = 10;
        }
        else
        {
            // 为什么要这么算
            nAudioLevel = (int) ((fEnergy - 40) * (10) / (72.3 - 40));
        }
        return (int) (nAudioLevel * 10);
    }

    static// 1k：500点
    float calEnergy(short[] pfFrame, int dwSamplesPerFrame)
    {
        float fEnergy;
        // 峰值为对数为：log(32768)=4.52;
        float fDirectOffset = 0;
        int i;
        for (i = 0; i < dwSamplesPerFrame; i++) {
            fDirectOffset += pfFrame[i];
        }
        fDirectOffset /= dwSamplesPerFrame; // 均值
        fEnergy = 0.0f;

        for (i = 0; i < dwSamplesPerFrame; i++) {
            fEnergy += (pfFrame[i] - fDirectOffset) * (pfFrame[i] - fDirectOffset);// 方差之和
        }
        fEnergy += 400000; // 加基本能量值
        return fEnergy;

    }

    // 200个点的最大值是：最小值是40，最大值为：72.3
    int getAudioLevel(short[] pfFrame, int dwSamplesPreFrame)
    {
        int nAudioLevel;

        dwSamplesPreFrame /= 2;
        float fEnergy = calEnergy(pfFrame, dwSamplesPreFrame);

        fEnergy = (float) (8 * Math.log10(fEnergy / dwSamplesPreFrame));
        // 值是由目的(计算到0-10)
        if (fEnergy < 40) {
            nAudioLevel = 0;
        }
        else if (fEnergy > 65) {
            nAudioLevel = 10;
        }
        else
        {
            // 为什么要这么算
            nAudioLevel = (int) ((fEnergy - 40) * (10) / (25));
        }
        return nAudioLevel;
    }
}
