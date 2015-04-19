package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.SellItem;
import ucsc.group12.agentmate.bll.Vendor;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/6/2014.
 */
public class Loader extends Activity {
    DatabaseControl dbc;
    ProgressBar p_br;
    File loadfile;
    File sdcard;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_window);
        try{    //Check weather the GPS and internet activated
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||(activeNetwork!=null)&&(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE)){
                dbc=new DatabaseControl(this);
                p_br=(ProgressBar)findViewById(R.id.loading_bar);//Create a progress bar to show the current progress of the file reading
                try {
                    readFile();///Read the file (*)
                }
                catch (Exception e){

                }
            }
            else{       //Show the error messages
                Toast.makeText(getApplicationContext(),"Please Activate GPS and Internet connection to Proceed",Toast.LENGTH_LONG).show();
                TextView editTv=(TextView)findViewById(R.id.txt_loading);
                editTv.setText("Please Activate GPS and INTERNET then Restart the Application");
                editTv.setTextColor(Color.BLACK);
                editTv.setBackgroundColor(Color.LTGRAY);
                setTitle("Resource Error..");
            }
        }
        catch(Exception e){

        }




    }


    public int readFile(){  //Read the file
        String filename="load.txt";
        sdcard = Environment.getExternalStorageDirectory();
        loadfile = new File(sdcard,"/AgentMate/IN/load.txt");//Where the file is
        StringBuilder text = new StringBuilder();

        final Thread t = new Thread(){

            @Override
            public void run(){//On another thread

                try {
                    BufferedReader br = new BufferedReader(new FileReader(loadfile));
                    String line;
                    int slptime=300;
                    int table=-1;
                    String first=br.readLine();
                    if (first.contains("false")) {
                        while ((line = br.readLine()) != null) {
                            line=line.trim();
                            Log.d("MYLINE",line);
                            if (line.equals("login")) {
                                //dbc.DeleteTableData("login");
                                table=0;
                                p_br.setProgress(0);
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("vendor")){
                                Log.d("ERROR DATABSE",line);
                                //dbc.DeleteTableData("vendor");
                                table=1;
                                p_br.setProgress(10);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("item")){
                                //dbc.DeleteTableData("item");
                                //Toast.makeText(getApplicationContext(),"bill",Toast.LENGTH_SHORT).show();
                                table=2;
                                p_br.setProgress(20);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("venOrder")){
                                //dbc.DeleteTableData("venOrder");
                                table=3;
                                p_br.setProgress(30);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("discount")){
                                //dbc.DeleteTableData("discount");
                                table=4;
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }

                            }
                            else if (line.equals("measure")){
                                //dbc.DeleteTableData("measure");
                                table=5;
                                p_br.setProgress(40);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("complain")){
                                //dbc.DeleteTableData("complain");
                                table=6;
                                p_br.setProgress(50);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("bill")){
                                //Toast.makeText(getApplicationContext(),"bill",Toast.LENGTH_SHORT).show();
                                //dbc.DeleteTableData("bill");
                                table=7;
                                p_br.setProgress(60);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("Myorder")){
                                //dbc.DeleteTableData("Myorder");
                                table=8;
                                p_br.setProgress(70);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else if (line.equals("payment")){
                                //dbc.DeleteTableData("payment");
                                table=9;
                                p_br.setProgress(90);
                                Log.d("srgsrb","rvererb");
                                try {
                                    Thread.sleep(slptime);
                                }
                                catch (Exception e){

                                }
                            }
                            else{

                                tableSwitcher(table,line);
                                //Send line to add to table
                                Log.d("ELSE",line);
                                //Toast.makeText(Loader.this,"ELSE",Toast.LENGTH_LONG);
                            }

                        }
                        p_br.setProgress(100);
                        br.close();



                        //Toast.makeText(getApplicationContext(),"BRCLOSE",Toast.LENGTH_LONG).show();
                        try {
                            File editFile = new File(sdcard, "/AgentMate/IN/load.txt");
                            FileWriter fw = new FileWriter(editFile, false);
                            fw.write("true\n");
                            fw.flush();
                            fw.close();
                        }
                        catch (Exception e){

                        }
                        Intent intent2 = new Intent(Loader.this, LocationService.class);
                        startService(intent2);
                        Intent intent3 = new Intent(Loader.this, NetSync.class);
                        startService(intent3);
                        Intent loginIntent=new Intent(Loader.this,Login_Activity.class);
                        startActivity(loginIntent);

                        //startthenext();

                    }
                    else if(first.contains("true")){
                        Log.d("srgsrb","Can load");
                        try {
                            Thread.sleep(500);
                        }
                        catch (Exception e){

                        }
                        Intent intent2 = new Intent(Loader.this, LocationService.class);
                        startService(intent2);
                        Intent intent3 = new Intent(Loader.this, NetSync.class);
                        startService(intent3);
                        Intent loginIntent=new Intent(Loader.this,Login_Activity.class);
                        startActivity(loginIntent);
                    }
                    else{

                    }

                }
                catch (Exception e) {
                    //Toast.makeText(getApplicationContext(),"NoFile Found "+sdcard.toString(),Toast.LENGTH_SHORT).show();
                    Log.d("FileRead","NoFile "+e.toString());
                    e.printStackTrace();
                    //You'll need to add proper error handling here
                }

            }
        };
        t.start();


    return 1;
    }

    //If the current read line in the file is not a table name then it means current line is data.
    //So enter that data in to the relavant table.
    public void tableSwitcher(int table,String line){

        String[] data=line.split("#");
        Log.d("Split check",data[0]);
        if (table==0){
            dbc.insertToLogin(data[0],data[1],data[2],data[3],data[4]);
        }
        else if (table==1){
            Log.d("ERROR DATABSE",line);
            Vendor vn=new Vendor(data[0],data[1],data[2],data[3],data[4],data[5],Double.parseDouble(data[6]),Boolean.parseBoolean(data[7]));
            vn.setConfirm();
            dbc.addVendor(vn);

        }

        else if (table==2){
            //ToDo drop the item table
            SellItem item=new SellItem(data[0],data[1],Double.parseDouble(data[2]),Integer.parseInt(data[3]),Double.parseDouble(data[4]),data[5],data[6],data[7],Boolean.parseBoolean(data[8]));
            item.setSync(true);
            dbc.AddItem(item);
        }
        else if (table==3){
            //dbc.LoadToVenOrder(data[0], data[1], data[2], data[3], true);

        }
        else if (table==4){
            dbc.LoadToDiscount(data[0],Integer.parseInt(data[1]),Integer.parseInt(data[2]),Double.parseDouble(data[3]),true);

        }
        else if (table==5){
            dbc.LoadToMeasure(data[0],data[1],Integer.parseInt(data[2]));
        }
        else if (table==6){

        }
        else if (table==7){
            dbc.LoadToBill(data[0],data[1],data[2],data[3],Integer.parseInt(data[4]),data[5],true);
        }
        else if (table==8){
           dbc.LoadToMyorder(data[0],data[1],Integer.parseInt(data[2]),Double.parseDouble(data[3]),true);

        }
        else if (table==9){
            //dbc.LoadTopayment(data[0],data[1],data[2],Double.parseDouble(data[3]),data[4],data[5],true);

        }
    }

    public void Progress(){

    }
    public void startthenext(){

    }
}



