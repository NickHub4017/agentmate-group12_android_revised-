package ucsc.group12.agentmate.ui;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;


/**
 * Created by NRV on 10/21/2014.
 */
public class NetSync extends Service{
    //DatabaseControl dbc=new DatabaseControl(getApplicationContext());
    private final WebSocketConnection mConnection = new WebSocketConnection();
    private static final String TAG = "de.tavendo.test1";
    final String wsuri = "ws://connect.mysensors.mobi:8080";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //WebSocketHandler webhand=;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TAG = "de.tavendo.test1";


        int res = super.onStartCommand(intent, flags, startId);
        Intent intent2 = new Intent();

        intent2.setAction("group12.tutorialspoint.StartSync");
        sendBroadcast(intent2);
        IntentFilter movementFilter;
        movementFilter = new IntentFilter("Req.Store.Intent");
        GetStoreReceiver getstoreReceiver = new GetStoreReceiver();
        registerReceiver(getstoreReceiver, movementFilter);

        //Toast.makeText(getApplicationContext(), "Service SYNC Started", Toast.LENGTH_LONG).show();
        final Thread t = new Thread() {

            @Override
            public void run() {
                mestart();

            }
        };
        t.start();
        //mestart();
        final Thread tt = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!mConnection.isConnected()) {


                        Intent erInt = new Intent();
                        erInt.setAction("Errors");
                        sendBroadcast(erInt);


                    }

                }
            }
        };
        tt.start();

        //Toast.makeText(getApplicationContext(),"Line",Toast.LENGTH_LONG).show();


        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Intent intent2 = new Intent();

        intent2.setAction("group12.tutorialspoint.StartSync");
        sendBroadcast(intent2);
        mestart();
    }

    private void mestart() {
//ws://echo.websocket.org:80
        //final String wsuri = "ws://connect.mysensors.mobi:8080";

        try {
            mConnection.connect(wsuri,new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);
                    mConnection.sendTextMessage("LOGIN #name Randul #skey 123456 @mysensors");//Logging to MySensor

                    mConnection.sendTextMessage("SHARE #loc @nirm");//Sharing sensors
                    mConnection.sendTextMessage("SHARE #mnh @nirm");
                    mConnection.sendTextMessage("SHARE #tot @nirm");
                    mConnection.sendTextMessage("SHARE #vanqty @nirm");

                    //Toast.makeText(getApplicationContext(),"Open",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);

                    String msgs[]=payload.split(" ");
                    //Toast.makeText(getApplicationContext(),"payload "+ payload ,Toast.LENGTH_LONG).show();
                    if (payload.contains("#loc")){
                        mConnection.sendTextMessage("DATA #loc "+MessageReceiver.msg+" @nirm");
                    }
                    else if (payload.contains("#mnh")) {
                        mConnection.sendTextMessage("DATA #mnh "+MessageReceiver.MoneyInHand+" @nirm");
                    }
                    else if (payload.contains("#tot")) {
                        mConnection.sendTextMessage("DATA #tot "+MessageReceiver.Total+" @nirm");
                    }
                    else if (payload.contains("#vanqty")) {
                        String[] list=payload.split(" ");
                        if (list.length==4) {
                            mConnection.sendTextMessage("DATA #vanqty " + "dbc.getItemQtyByItemID(list[3])" + " @nirm");//ToDo change for interact with Database
                            Intent in=new Intent("Get.Store.Intent");
                            try {
                                String tmp[] = list[3].split("%");
                                in.putExtra("Store_QTY", Double.parseDouble(tmp[0]));
                                in.putExtra("Store_Item", tmp[1]);

                                sendBroadcast(in);
                            }
                            catch (Exception e){
                                Toast.makeText(getApplication(),"Error in data connection, Please Try Again after 5 Seconds",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    //else{
                    //  mConnection.sendTextMessage("DATA #loc Invalid_Sensor @nirm");//ToDO change the loc part
                    //}


                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost."+reason);
                    mestart();
                    //Toast.makeText(getApplicationContext(),"^^^^^^^",Toast.LENGTH_LONG).show();
                }


            });
        } catch (WebSocketException e) {
//            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
//            mestart();
            Log.d(TAG, e.toString());
        }
    }

    public class GetStoreReceiver extends BroadcastReceiver

    {
        @Override
        public void onReceive(Context context, Intent intent)//this method receives broadcast messages. Be sure to modify AndroidManifest.xml file in order to enable message receiving
        {
            mConnection.sendTextMessage("DATA #vanqty "+intent.getStringExtra("Get_Store_Item")+" @nirm");
        }
    }




}
