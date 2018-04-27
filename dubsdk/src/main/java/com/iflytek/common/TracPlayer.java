package com.iflytek.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;

public class TracPlayer {
	private static final int DEFAULT_SAMPLE_RATE_16K = 16 * 1000;// 默认采样率
	private static String TAG = "TracPlayer";
	private static float mfVolme = 1.0f;
	public boolean mIsPlaying = false;
	FileInputStream m_fIn = null;
	AudioTrack m_atrack;
	byte[] m_Buffer;
	private int m_sampleRateInHz;
	public TracPlayer() {
		this(DEFAULT_SAMPLE_RATE_16K);

	}
	public TracPlayer(int sampleRate) {
		m_sampleRateInHz = sampleRate;
	}

	public void Play(String wavpath) {
		// 在MODE_STREAM模式下成功创建AudioTrack对象所需最小缓冲区大小
		int iMinBufSize = AudioTrack.getMinBufferSize(m_sampleRateInHz,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		if (iMinBufSize == AudioTrack.ERROR_BAD_VALUE
				|| iMinBufSize == AudioTrack.ERROR) {
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 创建AudioTrack对象
		m_atrack = new AudioTrack(AudioManager.STREAM_MUSIC, m_sampleRateInHz,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, iMinBufSize * 2,
				AudioTrack.MODE_STREAM);
		m_atrack.setStereoVolume(mfVolme, mfVolme);
		int fileFlag = 0;
		m_Buffer = new byte[iMinBufSize];

		try {
			m_fIn = new FileInputStream(wavpath);
			int len = m_fIn.available();
			if (deleteWavHead(wavpath)) {
				byte headBuffer[] = new byte[44];
				fileFlag = m_fIn.read(headBuffer);
				len -=44;
				//Log.i(TAG, "deleteWavHead");
				//Log.i(TAG, new String(headBuffer));
			}
			mIsPlaying = true;
			m_atrack.play();
			while (mIsPlaying) {
				if(m_atrack ==null){
					break;
				}
				fileFlag = m_fIn.read(m_Buffer);
				if (fileFlag == -1) {
					m_fIn.close();
					mIsPlaying = false;
					if(m_atrack.getPlayState() == AudioTrack .PLAYSTATE_PLAYING ){
						m_atrack.stop();
					}
					break;
				}
				if(m_atrack!= null && m_Buffer!=null){
				    m_atrack.write(m_Buffer, 0, fileFlag);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 释放要在同一个线程中
		mIsPlaying = false;
		m_atrack.release();
		m_atrack = null;
	}

	public void Stop() {
		if (m_atrack != null) {
			if(m_atrack.getPlayState() == AudioTrack .PLAYSTATE_PLAYING ){
				m_atrack.stop();
			}
			mIsPlaying = false;
		}
	}

	// pcm 编码的话如果有文件投的话去文件头
	public static boolean deleteWavHead(String wavpath) {
		try {
			FileInputStream fIn = new FileInputStream(wavpath);
			int headLen = 44;
			byte headBuffer[] = new byte[headLen];
			try {
				fIn.read(headBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String strWavHeand = new String(headBuffer);
			try {
				fIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (strWavHeand.contains("RIFF")) {
				return true;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
