package com.example.android.bluetoothchatapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Front extends AppCompatActivity {
    ArrayList<String> al1=new ArrayList<String>();
    ArrayList<String> al2=new ArrayList<String>();
    ArrayList<BluetoothDevice> al3=new ArrayList<BluetoothDevice>();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        lv=(ListView)findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Front.this,Chat.class);
                intent.putExtra("isClient",true);
                intent.putExtra("serverDevice",al3.get(i));
                startActivity(intent);
            }
        });
        PairedDevices();
    }
    public void PairedDevices(){
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices=adapter.getBondedDevices();
        Iterator<BluetoothDevice> itr=devices.iterator();
        while(itr.hasNext()){
            BluetoothDevice bd=itr.next();
            al3.add(bd);
            al1.add(bd.getName());
            al2.add(bd.getAddress());
        }
        lv.setAdapter(new MyAdapter(Front.this,R.layout.cust_list));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.front_menu,menu);
        return true;
    }
    public class MyAdapter extends ArrayAdapter {
        MyAdapter(Context ctx, int res)
        {
            super(ctx,res);
        }

        @Override
        public int getCount() {
            return al1.size();
        }

        @Override
        public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.cust_list,parent,false);
            TextView tv1=(TextView)v.findViewById(R.id.t1);
            TextView tv2=(TextView)v.findViewById(R.id.t2);
            tv1.setText(al1.get(position));
            tv2.setText(al2.get(position));
            return v;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(Front.this,Chat.class);
        intent.putExtra("isClient",false);
        startActivity(intent);
        return true;
    }

}
