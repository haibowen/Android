package Button;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AuterRecoderButton extends android.support.v7.widget.AppCompatButton implements AudioManger.AudioStateListener {
private static final int STATE_NORMAL=1;
private static final int STATE_RECORDING=2;
private static final int STATE_WANT_TO_CANCEL=3;
private static final  int DISTANCE=50;
boolean isRecording = false;
private int mCurState=STATE_NORMAL;

private DialogManger dialogManger;
private AudioManger audioManger;
private  float mtime;
//是否出发longclick；
private boolean mReady;
//获取音量大小的
private Runnable mGetVoiceLevelRunnable= new Runnable() {
	@Override
	public void run() {
		while (isRecording){
			try {
				Thread.sleep(100);
				mtime +=0.1f;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessage(MSG_VOICE_CHANGED);


		}

	}
};
	public AuterRecoderButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AuterRecoderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		dialogManger=new DialogManger(getContext());
		String dir= Environment.getExternalStorageDirectory()+"/imoc_recorder";
		audioManger =AudioManger.getInstance(dir ) ;
		audioManger.setAudioStateListener(this);




		setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				mReady=true;
				audioManger.preparAudio();


				return false;
			}
		});
	}
	//录音完成后的回调
	public interface  AudioFinshRecoderListener
	{

		void onFinsh(float seconds,String filepath);

	}
	private AudioFinshRecoderListener mListener;
	public void setAudioFinshRecoderListener(AudioFinshRecoderListener listener ){
		mListener=listener;
	}
	private static  final  int MSG_AUDIO_PREPARED=0x110;
	private  static  final  int MSG_VOICE_CHANGED=0x111;
	private  static final int MSG_DIALOG_DIMISS=0x112;

	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what){

				case MSG_AUDIO_PREPARED:
					dialogManger.showRecordingDialog();
					isRecording=true;

					new Thread(mGetVoiceLevelRunnable).start();
					break;
				case MSG_VOICE_CHANGED:

					dialogManger.updateVoiceLevel(audioManger.getvoicelevel(7));
					break;
				case MSG_DIALOG_DIMISS:

					dialogManger.dimissDialog();
					break;

			}

		};

	};
	@Override
	public void wellPrepared() {
		handler.sendEmptyMessage(MSG_AUDIO_PREPARED);

	}
	@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
		int action =event.getAction();
		int x=(int )event.getX();
		int y=(int )event.getY();
		switch(action)
		{
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDING);
			break;
			
			
		case MotionEvent.ACTION_MOVE:
			
			
			if(isRecording){
				if(wantToCancel(x,y))
				{
					changeState(STATE_WANT_TO_CANCEL);
				}else
				{
					changeState(STATE_RECORDING);
				}
				
			}
			
			
			
			
			break;
		case MotionEvent.ACTION_UP:
			if(!mReady)
			{
				reset();
				return super.onTouchEvent(event);
			}
			if(!isRecording||mtime<0.6f){
				dialogManger.tooshort();
				audioManger.cancel();
				handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);

			}else

			
			if(mCurState==STATE_RECORDING){
				
				//relase 
				//callbackToact
				
				dialogManger.dimissDialog();
				audioManger.release();
				if(mListener!=null){
					mListener.onFinsh(mtime,audioManger.getCurrentFilePath());
				}

				
				
			}else if(mCurState==STATE_WANT_TO_CANCEL){
				
				//cancel
				
				dialogManger.dimissDialog();
				audioManger.cancel();
				
			}
			reset();
			break;
		}
			return super.onTouchEvent(event);
		}

	private void reset() {
		// TODO Auto-generated method stub
		isRecording=false;
		mReady=false;

		mtime=0;
		changeState(STATE_NORMAL);
		
	}

	private boolean wantToCancel(int x, int y) {
		// TODO Auto-generated method stub
        if(x<0||x<getWidth()){
            return true;

        }
        if(y<DISTANCE||y>getWidth()+DISTANCE){
            return true;
        }






		return false;
	}

	private void changeState(int state) {
		// TODO Auto-generated method stub
		if(mCurState!=state){
			
			mCurState=state;
			switch(state)
			{
			
			case STATE_NORMAL:
				//setBackgroundResource(R.drawable.btn_default);
				setText("按住说话");
				
				break;
             case STATE_RECORDING:
                 //setBackgroundResource(R.drawable.btn_default);
                 setText("松开结束");
                 if (isRecording){
                     //dialog
					 dialogManger.recoring();




                 }
				break;	
             case STATE_WANT_TO_CANCEL:
                 //setBackgroundResource(R.drawable.btn_default);
                 setText("松开手指，取消发送");
                 dialogManger.wantToCancel();
            	 break;
			}
			
			
		}
	}


}
