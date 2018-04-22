package com.modofo.recordr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;
import javax.microedition.midlet.MIDlet;

import com.modofo.jmeutil.Logger;

/**
 * recording player wrapper
 * @author zhangv
 *
 */
public class Recorder {
	private MIDlet midlet;
	private Displayable parent;
	protected Player mRecordingPlayer;
	protected Player mPlayingPlayer;
	protected RecordControl mRecordControl;
	protected ByteArrayOutputStream mRecordingOutput;
	protected String mContentType;
	protected byte mAudioData[];
	private RecorderForm recorderForm;
	private Vector listeners = new Vector();
	public Recorder() {
	}
	public Recorder(Displayable d) {
		parent = d;
	}
	public Recorder(MIDlet midlet ,Displayable d) {
		parent = d;
		this.midlet = midlet;
	}
	public void addListener(RecorderListener l){
		listeners.addElement(l);
	}
	
	public void showRecorder( ){
		Display.getDisplay(midlet).setCurrent(getRecorderForm());
	}
	
	public RecorderForm getRecorderForm(){
		if(recorderForm == null){
			recorderForm = new RecorderForm(this.midlet,this);
			recorderForm.setBackForm(parent);
		}
		return recorderForm;
	}
	public byte[] getAudioData(){
		return mAudioData;
	}
	public void setAudioData(byte[] audioData){
		mAudioData = audioData;
	}
	
	/**
	 * Cancel the capture, save nothing 
	 */
	public void cancelAudioCapture(){
		Logger.log("cancelAudioCapture()::BEGIN");
		if (mRecordControl != null){
			try {
				mRecordControl.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RecorderEvent re = new RecorderEvent();
		re.setType(RecorderEvent.RECORD_CANCEL);
		notifyListener(re);
		Logger.log("cancelAudioCapture()::END");
	}
	public void stopAudioCapture() {
		Logger.log("stopAudioCapture() BEGIN");
		if (mRecordControl != null)
			try {
				mRecordControl.stopRecord();
				mRecordControl.commit();
				mRecordingPlayer.stop();
				mContentType = mRecordControl.getContentType();
				mRecordingOutput.close();
				mRecordingPlayer.close();
				mAudioData = mRecordingOutput.toByteArray();
				mRecordingOutput = null;
				mRecordingPlayer = null;
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (MediaException ex) {
				ex.printStackTrace();
			} catch (IllegalStateException ex) {
				ex.printStackTrace();
			}
		else
			Logger.log("ERROR! RecordControl is null.");
		//notify listener
		RecorderEvent re = new RecorderEvent();
		re.setType(RecorderEvent.RECORD_STOP);
		re.setBytes(mAudioData);
		notifyListener(re);
		Logger.log("stopAudioCapture() END");
	}
	
	private void notifyListener(RecorderEvent re) {
		int tp = re.getType();
		for(int i=0;i<listeners.size();i++){
			RecorderListener l = (RecorderListener) listeners.elementAt(i);
			if(tp == RecorderEvent.RECORD_STOP){
				l.onAudioRecordStop(re);
			}
			if(tp == RecorderEvent.PLAY_START){
				l.onAudioPlayStart(re);
			}
			if(tp == RecorderEvent.PLAY_STOP){
				l.onAudioPlayStop(re);
			}
			if(tp == RecorderEvent.RECORD_CANCEL){
				l.onAudioRecordCancel(re);
			}
		}
	}
	public void stopAudioPlay() throws MediaException{
		if (mPlayingPlayer != null && mPlayingPlayer.getState() == 400)
			mPlayingPlayer.stop();
		RecorderEvent re = new RecorderEvent();
		re.setType(RecorderEvent.PLAY_STOP);
		notifyListener(re);
	}
	public void playSound(String pContentType, byte pAudioData[]) {
		Logger.log("startAudioPlaying() BEGIN");
		try {
			if (mPlayingPlayer != null && mPlayingPlayer.getState() == 400)
				mPlayingPlayer.stop();
			java.io.InputStream is = new ByteArrayInputStream(mAudioData);
			mPlayingPlayer = Manager.createPlayer(is, mContentType);
			mPlayingPlayer.realize();
			//launch a thread to check the player state
			new Thread(){
				public void run(){
					while(true){
						try {
							System.out.println(mPlayingPlayer.getState());
							if(mPlayingPlayer.getState() == 300 ){ //300 - playback is over
								stopAudioPlay();
								break;
							}else
								Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (MediaException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			mPlayingPlayer.start();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (MediaException ex) {
			ex.printStackTrace();
		}
		//notify listener
		RecorderEvent re = new RecorderEvent();
		re.setType(RecorderEvent.PLAY_START);
		notifyListener(re);
		Logger.log("startAudioPlaying() END");
	}

	public void startCapture() {
		new Thread(new Runnable() {
			public void run() {
				Logger.log("startAudioCapture()->Thread.run() BEGIN");
				try {
					mRecordingPlayer = Manager.createPlayer("capture://audio");
					mRecordingPlayer.realize();
					mRecordControl = (RecordControl) mRecordingPlayer
							.getControl("RecordControl");
					mRecordingOutput = new ByteArrayOutputStream();
					mRecordControl.setRecordStream(mRecordingOutput);
					mRecordControl.startRecord();
					mRecordingPlayer.start();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (MediaException ex) {
					ex.printStackTrace();
				}
				Logger.log("startAudioCapture()->Thread.run() END");
			}
		}).start();
	}

}
