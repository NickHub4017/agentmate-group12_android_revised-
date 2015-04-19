package ucsc.group12.agentmate.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by NRV on 10/7/2014.
 */

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/7/2014.
 */


public class MessageReceiver extends BroadcastReceiver {
    static String  msg="Gps Position Not set";
    static long time=0;
    static double speed=0;
    static double MoneyInHand=0;
    static double Total=0;
    static int xy;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("group12.tutorialspoint.CUSTOM_INTENT")) {
            msg = "" + intent.getExtras().getString("new_location").toString();
            time = intent.getExtras().getLong("updated_time");
            writeToFile(msg,MoneyInHand,Total);
            // speed=intent.getExtras().getDouble("speed");
            // Toast.makeText(context, "Intent Detected." + intent.getExtras().getString("new_location").toString() + msg, Toast.LENGTH_LONG).show();
            // Toast.makeText(context, "Intent Detected." + time+"  "+speed+" " + msg, Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equals("group12.tutorialspoint.PaymentIntent")) {
            MoneyInHand=MoneyInHand+intent.getDoubleExtra("mnh",-1);
            Total=Total+intent.getDoubleExtra("tot",0);
            writeToFile(msg,MoneyInHand,Total);
            //Toast.makeText(context, "Intent Detected. Payment"+MoneyInHand+" "+Total , Toast.LENGTH_LONG).show();
        }

        else if (intent.getAction().equals("group12.tutorialspoint.Order")) {
            writeToFile(msg,MoneyInHand,Total);
            Toast.makeText(context, "Intent Detected. Order", Toast.LENGTH_LONG).show();
        }
        else if (intent.getAction().equals("group12.tutorialspoint.StartLoc")) {
            //Toast.makeText(context, "Intent Detected. LOC", Toast.LENGTH_LONG).show();
            readFromFile(context);
        }
        else if (intent.getAction().equals("group12.tutorialspoint.StartSync")) {
            //Toast.makeText(context, "Intent Detected. SYNC", Toast.LENGTH_LONG).show();
            readFromFile(context);
        }
        else if (intent.getAction().equals("Errors")) {
            //Toast.makeText(context, "Socket not connected", Toast.LENGTH_LONG).show();
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
                    if (recvnum.contains("7")){
                        String toSendphoneNo = recvnum;//"0719720470";//0712626607  //Password request send to agent.
                        String toSendmsg=null;
//create data to send  at

                        String[] replypart=reply.split("%");
                        //Toast.makeText(context,"String-->"+reply+"---><><>"+replypart[0],Toast.LENGTH_LONG);
                        if (replypart[0].equals("loc")){
                            toSendmsg="zcurrent position of "+"R01"+"is "+msg+" and last upadeted at "+time;
                        }
                        else if (replypart[0].equals("mnh")){
                            toSendmsg="Money in hand for "+"R01"+"is "+MoneyInHand;
                        }
                        else if (replypart[0].equals("ttl")){
                            toSendmsg="Total in hand for "+"R01"+"is "+Total;
                        }
                        else if (replypart[0].equals("vanqty")){
                            String item=replypart[1];
                            DatabaseControl dbc=new DatabaseControl(context);

                            toSendmsg="Total qty in van for item "+item+" is "+dbc.getItemQtyByItemID(item);
                        }
                        else if (replypart[0].equals("repstrqty")){
                            String item=replypart[1];
                            toSendmsg="Total qty in store for item "+item+" is "+replypart[2];
                            Toast.makeText(context,toSendmsg,Toast.LENGTH_LONG).show();
                        }




                        try {
                            if (toSendmsg!=null && !replypart[0].equals("repstrqty")) {
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
                //Toast.makeText(context, str + "--" + intent.getAction() + "Fgf", Toast.LENGTH_SHORT).show();
            }

        }

        else{
            Toast.makeText(context, "Else"+intent.getAction(), Toast.LENGTH_LONG).show();

        }


    }



    public static String getData(){
        return msg+"#"+time+"#"+speed;
    }
    public void writeToFile(String msg,Double mnh,Double tot){
       File sdcard = Environment.getExternalStorageDirectory();
       File  datafile = new File(sdcard,"/AgentMate/IN/data.txt");
        Calendar c = Calendar.getInstance();
        String date = "" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
        if (!datafile.exists()) {

            try {
                datafile.createNewFile();
                FileWriter fw = null;
                fw = new FileWriter(datafile, false);


                fw.write(date + "\n");

                fw.append(msg + "#" + mnh + "#" + tot + "\n");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else{
            try {
                FileWriter fw = null;
                    fw = new FileWriter(datafile, false);
                    fw.write(date+"\n");
                    fw.append(msg + "#" + mnh + "#" + tot + "\n");
                    fw.flush();
                    fw.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    public void readFromFile(Context context){
        File sdcard = Environment.getExternalStorageDirectory();
        File  datafile = new File(sdcard,"/AgentMate/IN/data.txt");
        Calendar c = Calendar.getInstance();
        String date = "" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
        if (!datafile.exists()) {
            //Toast.makeText(context,"Read if file Not here",Toast.LENGTH_LONG).show();
            try {
                datafile.createNewFile();
                FileWriter fw = null;
                fw = new FileWriter(datafile, false);


                fw.write(date + "\n");

                fw.append("default" + "#" + 0 + "#" + 0 + "\n");
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else{
            //Toast.makeText(context,"File is found",Toast.LENGTH_LONG).show();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(datafile));
                String first=br.readLine();
                if (date.contains(first)){
                    //Toast.makeText(context,"File conains cur date",Toast.LENGTH_LONG).show();
                    String second=br.readLine();
                    String[] data=second.split("#");
                    msg=data[0];
                    MoneyInHand=Double.parseDouble(data[1]);
                    Total=Double.parseDouble(data[2]);
                    //Toast.makeText(context,msg+" "+MoneyInHand+" "+Total,Toast.LENGTH_LONG).show();
                }
                else{
              //      Toast.makeText(context,"File contains another date",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
