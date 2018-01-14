package com.mouse.lion.pocketdj;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lionm on 1/13/2018.
 */

public class MediaPlayerService extends Service
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener ,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public static final String KEY_MEDIA_FILE = "media";

    // Binder given to clients
    private IBinder iBinder = new LocalBinder();

    private MediaPlayer mediaPlayer;

    // Path to the audio file
    private String mediaFile;

    // Used to pause/resume MediaPlayer
    private int resumePosition;

    private AudioManager audioManager;

    private ArrayList<Audio> audioList;

    private int audioIndex = CacheUtils.INVALID_AUDIO_INDEX;

    private Audio activeAudio;

    @Override
    public void onCreate() {
        super.onCreate();

        // Perform one-time setup procedures
        setupCallStateListener();
        registerBecomingNoisyReciever();
        registerPlayNewAudioReceiver();
    }

    // The system calls this method when an activity requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        try {
//            // An audio file is passed to the service through putExtra()
//            mediaFile = intent.getExtras().getString(KEY_MEDIA_FILE);
//        } catch (NullPointerException e) {
//            stopSelf();
//        }

        CacheUtils cacheUtils = new CacheUtils(getApplicationContext());
        audioList = cacheUtils.loadAudios();
        audioIndex = cacheUtils.loadAudioIndex();
        if (audioIndex != CacheUtils.INVALID_AUDIO_INDEX && audioIndex < audioList.size()) {
            activeAudio = audioList.get(audioIndex);
        } else {
            stopSelf();
        }

        // Request audio focus
        if (!requestAudioFocus()) stopSelf(); else Log.d("mylog", "focus gained");
//        if (!TextUtils.isEmpty(mediaFile)) initMediaPlayer();
        if (audioIndex != CacheUtils.INVALID_AUDIO_INDEX) initMediaPlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();

        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        unregisterReceiver(BECOMING_NOISY_RECEIVER);
        unregisterReceiver(PLAY_NEW_AUDIO_RECEIVER);
        new CacheUtils(getApplicationContext()).clearCachedAudioPlayList();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // Invoked indicating buffering status of
        // a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // Invoked when playback of a media source has completed.
        stopMedia();
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // Invoked when there has been an error during an asynchronous operation.
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        // Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // Invoked when the media source is ready for playback.
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        // Invoked indicating the completion of a seek operation.
        playMedia();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        // Invoked when the audio focus of the system is update.
        Log.d("mylog","FOCUS CHANGED => ???");
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.d("mylog","FOCUS CHANGED => GAIN");
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                if ((!mediaPlayer.isPlaying())) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d("mylog","FOCUS CHANGED => LOSS");
                // Lost focus for an unbounded amount of time;
                // stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.d("mylog","FOCUS CHANGED => TRANSIENT");
                // Lost focus for a short time, but we have to stop playback.
                // We don't release the media player because playback is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.d("mylog","FOCUS CHANGED => LOSS TRANSIENT CAN DUCK");
                // Lost focus for a short time, but it's ok to keep playback
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result;

        if (audioManager == null) {
            return false;
        } else if (Build.VERSION.SDK_INT < 26) {
            result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } else {
            result = audioManager.requestAudioFocus(new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .build());
        }

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Focus gained
            return true;
        } else {
            // Could not gain focus
            return false;
        }
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager.abandonAudioFocus(this);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        // Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();

        if (Build.VERSION.SDK_INT < 21) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        }

        try {
            // Set the data source to the mediaFile location
//            mediaPlayer.setDataSource(mediaFile);
            mediaPlayer.setDataSource(activeAudio.data);
        } catch(IOException e) {
            e.printStackTrace();
            stopSelf();
        }

        mediaPlayer.prepareAsync();
    }

    private void playMedia() {
        Log.d("mylog", "playMedia!!!!!");
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    // Becoming noisy
    private final BroadcastReceiver BECOMING_NOISY_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseMedia();
        }
    };

    private void registerBecomingNoisyReciever() {
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(BECOMING_NOISY_RECEIVER, intentFilter);
    }

    // Handling incoming calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

    private void setupCallStateListener() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;

                    case TelephonyManager.CALL_STATE_IDLE:
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // Handling a request to play a new track
    private BroadcastReceiver PLAY_NEW_AUDIO_RECEIVER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            audioIndex = new CacheUtils(getApplicationContext()).loadAudioIndex();
            if (audioIndex != CacheUtils.INVALID_AUDIO_INDEX && audioIndex < audioList.size()) {
                // index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }

            // PLAY_NEW_AUDIO action received reset mediaPlayer to play the new audio
            stopMedia();
            mediaPlayer.reset();
            initMediaPlayer();
        }
    };

    private void registerPlayNewAudioReceiver() {
        IntentFilter intentFilter = new IntentFilter(MainActivity.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(PLAY_NEW_AUDIO_RECEIVER, intentFilter);
    }
}
