package ucsc.group12.agentmate.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.TimeZone;

import ucsc.group12.agentmate.R;
import ucsc.group12.agentmate.bll.Representative;
import ucsc.group12.agentmate.dbc.DatabaseControl;


/**
 * Created by NRV on 10/6/2014.
 */
public class UnLoadData extends Activity {
    DatabaseControl dbc = new DatabaseControl(this);
    File unloadfile;
    File sdcard;
    ProgressBar p_br;
    Representative logged_rep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.out_data);
        p_br=(ProgressBar)findViewById(R.id.sync_out_progress_bar);
        Button btn_sync_out=(Button)findViewById(R.id.btn_sync_out);
        logged_rep = (Representative) getIntent().getExtras().getSerializable("logged_user");
        btn_sync_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Syncer();
            }
        });


    }

    public void Syncer() {
        sdcard = Environment.getExternalStorageDirectory();
        unloadfile = new File(sdcard,"/AgentMate/OUT/unload.txt");
        if (unloadfile.exists()){
            unloadfile.delete();
        }
        try {
            unloadfile.createNewFile();
        }
        catch(Exception e){

        }


        final Thread t = new Thread() {

            @Override
            public void run() {
                try {

                    FileWriter fw = new FileWriter(unloadfile, true);
                    fw.append("false\n");
                    fw.append(logged_rep.Emp_id+"\n");
                    fw.append(Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30")).getInstance().getTime().toString()+"\n");
                    String[] tables={"login","vendor","item","venOrder","discount","measure","complain","bill","Myorder","payment","return","demandMyorder","demandvenOrder"};
                    //String[] tables={"venOrder","bill","Myorder","payment"};
                    p_br.setMax(tables.length);
                    int j=0;
                    for (String name:tables){//Going throgh the tables
                        fw.append(name+"\n");
                        Cursor cur=dbc.OutTableData(name);
                        int num_cols=cur.getColumnCount();
                        if(cur.moveToFirst()){//Going throgh the cursors
                            do{//Going through the columns
                                String temp="";
                                for (int i=0;i<num_cols;i++){

                                    temp=temp+"#"+cur.getString(i);
                                }
                                fw.append(temp+"\n");
                            }while (cur.moveToNext());
                        }
                        j++;
                        p_br.setProgress(j);
                       Thread.sleep(200);
                    }
                    fw.append("Synced_OK");
                    fw.flush();
                    fw.close();
                }
                catch (Exception e){
                    Log.d("UnloadWriteError","Catch"+e.toString());
                }

            }
        };
        t.start();

    }


}
