package com.example.sefa.sqlitedeneme;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ListView listView;
    TextView aci,kutu,deger;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listview);
        aci = (TextView)findViewById(R.id.aci);
        kutu = (TextView)findViewById(R.id.textView3);
        deger = (TextView)findViewById(R.id.deger);

        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        Sensor s = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sm.registerListener(this,s,100);

        button = (Button)findViewById(R.id.button);
        final Database database = new Database(getApplicationContext());
        final List<String> vVeriler = database.VeriListele();
        registerForContextMenu(listView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,vVeriler);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if(v.getId() == R.id.listview) {
                    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                    menu.setHeaderTitle(listView.getItemAtPosition(info.position).toString());
                    menu.add(0, 0, 0, "Sil");
                }
            }
        });

    }
    float x=0.0f;
    float y=0.0f;
    float z=0.0f;

    float lx=0.0f;
    float ly=0.0f;
    float lz=0.0f;

     float aci1=0.0f;
     float aci2=0.0f;
     float aci3=0.0f;


    @Override
    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if(Math.abs(x-lx)<0.0f && Math.abs(y-ly)<0.0f && Math.abs(z-lz)<0.0f){
            return;
        }

        lx = x ; ly=y; lz=z;

        deger.setText("x :" + x + "y : " + y + "z : " + z);

        aci1 = (float)(180.0f*Math.atan(x/y)/Math.PI);
        aci2 = (float)(180.0f*Math.atan(y/z)/Math.PI);
        aci3 = (float)(180.0f*Math.atan(z/x)/Math.PI);

        aci.setText("aci1 :" + aci1 + "aci2 :" + aci2 + "aci3 :" + aci3 );

        kutu.setRotation(aci1);
        kutu.setRotation(aci2);
        kutu.setRotation(aci3);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Database database = new Database(MainActivity.this);
        database.veriekle(aci1,aci2,aci3);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean onContextItemSelected(MenuItem item){

        boolean donus;
        switch (item.getItemId()){

            case 0:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final String secili = listView.getItemAtPosition(info.position).toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Veri Silme");
                alert.setMessage("\"" + secili + "\" adli veriyi silmek istediğinizden emin misiniz ?");
                alert.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] dizi = secili.split("-");
                        long id = Long.parseLong(dizi[0].trim());
                        Database database = new Database(getApplicationContext());
                        database.VeriSil(id);
                    }
                });
                alert.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

                donus=true;
                break;
                default:
                donus=false;
                    break;
        }
        return donus;

    }

}
