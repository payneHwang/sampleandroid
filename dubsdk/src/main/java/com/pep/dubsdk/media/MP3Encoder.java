package com.pep.dubsdk.media;

/**
 * MP3编码器
 * 
 * @ClassName: MP3Encoder
 * @Description: TODO
 * @author Xiaopo
 * @date 2012-11-28 下午3:20:17
 */
public class MP3Encoder {
    static {
        System.loadLibrary("mp3lame");
    }

    private native void init(int inSampleRate,
            int numChannels, int outSamplerate, int outBitrate, int quality);

    private native void close();

    public native void encodeFile(String sourcePath, String targetPath);

    private native int getId3V2Tag(byte[] buffer, int size);


    /**
     * mp3buf_size in bytes = 1.25*num_samples + 7200
     * 
     * @param gfp
     * @param leftPcm
     * @param rightPcm
     * @param numSamples
     * @param mp3Bufferlame_init_params
     *            (glf)
     * @param mp3BufferSize
     * @return
     */
    private native int encode(short[] leftPcm, short[] rightPcm,
            int numSamples, byte[] mp3Buffer);

    private native int flush(byte[] mp3Buffer);


    public MP3Encoder(int inSampleRate, int numChannels) {
        init(inSampleRate, numChannels,inSampleRate,32,7);
    }

    public void dispose() {
        close();
    }


    public int getId3V2Tag(byte[] buffer) {
        return getId3V2Tag(buffer, buffer.length);
    }

    public int encode(short[] leftPcm, short[] rightPcm, int numSamples,
            byte[] mp3Buffer, int mp3BufferSize) {
        return encode(leftPcm, rightPcm, numSamples, mp3Buffer);
    }

    public int flush(byte[] mp3Buffer, int mp3BufferSize) {
        return flush(mp3Buffer);
    }

}
