package ucsc.group12.agentmate.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;


/**
 * Created by NRV on 1/19/2015.
 */
public class SmsReceiver extends BroadcastReceiver{
    String msg;
    long time;

    @Override
    public void onReceive(Context context, Intent intent) {

    }
    //  @Override
/*    public void onReceive(Context context, Intent intent) {
        {
            if (intent.getAction().equals("group12.tutorialspoint.CUSTOM_INTENT")) {
                msg = "" + intent.getExtras().getString("new_location").toString();
                time = intent.getExtras().getLong("updated_time");
//                Toast.makeText(context,"tevv",Toast.LENGTH_LONG).show();
            }
          else if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            //---get the SMS message passed in---
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String str = "";
            if (bundle != null)
            {
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                for (int i=0; i<msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    str += "SMS from " + msgs[i].getOriginatingAddress();
                    str += " :";
                    str += msgs[i].getMessageBody().toString();
                    str += "\n";

                    String recvnum=msgs[i].getOriginatingAddress();
                    String reply=msgs[i].getMessageBody().toString();
                    Toast.makeText(context,"recv "+recvnum+" "+reply,Toast.LENGTH_LONG).show();
                    if (recvnum.contains("719720470")){
                        String toSendphoneNo = "0719720470";//0712626607  //Password request send to agent.
                        String toSendmsg=null;


                        String[] replypart=reply.split("%");
                        Toast.makeText(context,"String-->"+reply+"---><><>"+replypart[0],Toast.LENGTH_LONG);
                        if (replypart[0].equals("loc")){
                            toSendmsg="zcurrent position of "+"R01"+"is "+msg+" and last upadeted at "+time;
                        }
                        try {
                            if (toSendmsg!=null) {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(toSendphoneNo, null, toSendmsg, null, null);
                            }
                        } catch (Exception ex) {
                            Toast.makeText(context,"caught",Toast.LENGTH_LONG);
                            ex.printStackTrace();
                        }

                    }

                }

                //---display the new SMS message---
                Toast.makeText(context, str + "--" + intent.getAction() + "Fgf", Toast.LENGTH_SHORT).show();
            }
        }
        }
    }*/
}
