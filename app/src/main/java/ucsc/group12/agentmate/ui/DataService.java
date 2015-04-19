package ucsc.group12.agentmate.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by NRV on 9/3/2014.
 */
public class DataService extends Service{
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
