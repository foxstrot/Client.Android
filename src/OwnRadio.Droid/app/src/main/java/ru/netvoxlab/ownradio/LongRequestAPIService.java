package ru.netvoxlab.ownradio;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import static ru.netvoxlab.ownradio.Constants.ACTION_FILLCACHE;
import static ru.netvoxlab.ownradio.Constants.ACTION_GETNEXTTRACK;
import static ru.netvoxlab.ownradio.Constants.EXTRA_COUNT;
import static ru.netvoxlab.ownradio.Constants.EXTRA_DEVICEID;
import static ru.netvoxlab.ownradio.Constants.TAG;

/**
 * Created by a.polunina on 20.07.2017.
 */

public class LongRequestAPIService extends IntentService {
	public int waitingIntentCount;
	
	public LongRequestAPIService() {
		super("LongRequestAPIService");
	}
	
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		waitingIntentCount++;
		Log.i(TAG, "onStartCommand, waitingIntentCount = " + waitingIntentCount);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		waitingIntentCount--;
		Log.i(TAG, "onHandleIntent, waitingIntentCount = " + waitingIntentCount);
		
		if (intent != null) {
			
			if(!new CheckConnection().CheckInetConnection(getApplicationContext()))
				return;
			
			final String action = intent.getAction();
			if (ACTION_GETNEXTTRACK.equals(action)) {
				//Получение информации о следующем треке и его загрузка
				final String deviceId = intent.getStringExtra(EXTRA_DEVICEID);
				final Integer countTracks = intent.getIntExtra(EXTRA_COUNT, 3);
				new TrackToCache(getApplicationContext()).SaveTrackToCache(deviceId, countTracks);
			} else if(ACTION_FILLCACHE.equals(action)) {
				new TrackToCache(getApplicationContext()).FillCache();
			}
		}
		Log.i(TAG, "onHandleIntent end");
		
	}
	
	public void onDestroy() {
		super.onDestroy();
	}
	
	public int getWaitingIntentCount(){
		Log.e(TAG, "waitingIntentCount = " + waitingIntentCount);
		return waitingIntentCount;
	}
}
