package Button;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by wenhaibo on 2017/12/19.
 */

public class AudioManger {
    private MediaRecorder mediaRecorder;
    private String mdir;
    private String mCurrentFilePath;
    private static AudioManger  minstance;
    private boolean isPrepared;
    private AudioManger(String dir){
        mdir=dir;
    }



    public interface AudioStateListener{
        void wellPrepared();
    }
    public AudioStateListener audioStateListener;
    public void setAudioStateListener(AudioStateListener listener){
        audioStateListener=listener;
    }

    public static AudioManger getInstance(String dir){
        if(minstance==null) {
            synchronized (AudioManger.class) {
                if (minstance == null)
                    minstance = new AudioManger(dir);

            }
        }
        return minstance;
    }
    public void preparAudio(){
        try {
            isPrepared=false;
        File dir=new File(mdir);
        if(!dir.exists() )
            dir.mkdirs();
        String fileName=generateFileName();
        File file=new File(dir,fileName);
            mCurrentFilePath=file.getAbsolutePath();



        mediaRecorder=new MediaRecorder();
        //设置输出的绝对路径
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        //设置音频源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置音频格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        //设置音频的编码
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            //准备结束
            isPrepared=true;
            if(audioStateListener!=null){
                audioStateListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+"amr";
    }

    public int getvoicelevel(int maxLevel){

        if(isPrepared){
            try {


            return maxLevel*mediaRecorder.getMaxAmplitude()/32768+1;
            }catch (Exception e){

            }
        }
        return  1;

    }

    public void release(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

    }
    public void cancel(){
        release();
        if(mCurrentFilePath!=null){


        File file=new File(mCurrentFilePath);
        file.delete();
        mCurrentFilePath=null;
        }

    }
    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }


}
