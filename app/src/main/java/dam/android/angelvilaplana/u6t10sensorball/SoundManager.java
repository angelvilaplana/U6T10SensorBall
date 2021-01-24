package dam.android.angelvilaplana.u6t10sensorball;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;

import java.util.Random;

public class SoundManager {

    private SoundPool mySoundPool;
    private final int MAX_SOUNDPOOL_STREAMS = 6;

    private SparseIntArray mySoundPoolList;
    private SparseIntArray soundIDList;
    private AudioManager audioManager;
    private Context context;
    private int soundsLoaded;

    public void initSoundManager(Context theContext) {
        context = theContext;
        buildSoundPool();
        mySoundPoolList = new SparseIntArray();
        soundIDList = new SparseIntArray();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    private void buildSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mySoundPool = new SoundPool(MAX_SOUNDPOOL_STREAMS, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            mySoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_SOUNDPOOL_STREAMS)
                    .build();
        }

        mySoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) soundsLoaded++;
            }
        });
    }

    public void addSound(int index, int soundID) {
        soundIDList.put(index, soundID);
        mySoundPoolList.put(index, mySoundPool.load(context, soundID, 1));
    }

    public void playSound(int index) {
        if (soundsLoaded == mySoundPoolList.size()) {
            mySoundPool.play(mySoundPoolList.get(index), .5f, .5f, 1, 0, 1);
            Log.i("SOUND", "playing");
        }
    }

    // TODO - Activity 4 - Play random sounds
    public void playCollisionSound() {
        Random random = new Random();
        int randomKey = mySoundPoolList.keyAt(random.nextInt(mySoundPoolList.size()));
        if (randomKey == 6) randomKey--;
        playSound(randomKey);
    }

    public void releaseSoundPool() {
        if (null != mySoundPool) {
            for (int i = 0; i < mySoundPoolList.size(); i++) {
                int key = mySoundPoolList.keyAt(i);
                mySoundPool.unload(mySoundPoolList.get(key));
            }
            mySoundPool.release();
            mySoundPool = null;
        }

        audioManager.setSpeakerphoneOn(false);
        audioManager.unloadSoundEffects();
    }

    public void loadSoundPool() {
        if (soundsLoaded == 0) {
            return;
        }

        buildSoundPool();
        soundsLoaded = 0;
        mySoundPoolList = new SparseIntArray();
        for (int i = 0; i < soundIDList.size(); i++) {
            int key = soundIDList.keyAt(i);
            mySoundPoolList.put(key, mySoundPool.load(context, soundIDList.get(key), 1));
        }

        audioManager.setSpeakerphoneOn(true);
        audioManager.loadSoundEffects();
    }

}
