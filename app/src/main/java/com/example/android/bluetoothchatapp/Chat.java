package com.example.android.bluetoothchatapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.FOCUS_RIGHT;
import static com.example.android.bluetoothchatapp.R.id.singleMessageContainer;

public class Chat extends AppCompatActivity {
    BluetoothAdapter adapter;
    ActionBar bar;
    ListView lv;
    EditText et;
    ArrayAdapter<String> arr;
    ArrayList<String> al;
    LinearLayout msgContainer;
    ChatService bcs;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        BluetoothDevice bd;
        Intent intn=getIntent();
        Bundle bundle=intn.getExtras();
        bt = ( Button ) findViewById( R.id.button );
        boolean b=(boolean)bundle.get("isClient");
        bar=getSupportActionBar();
        if(b){
            bd=(BluetoothDevice)bundle.get("serverDevice");
            String n=bd.getName();
            bar.setSubtitle("Connected To: "+n);
            bcs=new ChatService(true,bd,h);
        }else{
            bcs=new ChatService(false,null,h);
        }
        adapter= BluetoothAdapter.getDefaultAdapter();
        bar.setTitle(adapter.getName());
        et=(EditText)findViewById(R.id.editText);
        lv=(ListView)findViewById(R.id.List);
        al=new ArrayList<String>();
        arr=new ArrayAdapter<String>(Chat.this,R.layout.support_simple_spinner_dropdown_item,al);
        lv.setAdapter(arr);
    }
    Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    bar.setSubtitle("Connected To: "+msg.obj.toString());
                    break;
                case 2:
                    String m = msg.obj.toString();
                    String full = bar.getSubtitle().toString();
                    String name = full.substring( 13,full.length() );
                    //int pos=m.indexOf( ":" );
                    al.add(name+": "+m);
                    //if(pos == 0){
                        lv.setAdapter( new BubbleAdapterMe( Chat.this, R.layout.bubbleme ) );
                    //}
                    arr.notifyDataSetChanged();
                    break;
            }
        }
    };
    public void sendMessage(View arg)
    {
        String s=et.getText().toString();
        if(s != " ") {
            bcs.rwt.writeMessage( s );
            al.add("Me: "+s);
            arr.notifyDataSetChanged();
            et.setText("");
            lv.setAdapter( new BubbleAdapterMe( Chat.this,R.layout.bubbleme) );
        }
    }
    public class BubbleAdapterMe extends ArrayAdapter{
        BubbleAdapterMe(Context ctx, int res)
        {
            super(ctx,res );
        }
        @Override
        public int getCount() {
            return al.size();
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate( R.layout.bubbleme, viewGroup, false );
            TextView tv1 = ( TextView ) v.findViewById( R.id.MessageMe );
            msgContainer = ( LinearLayout ) v.findViewById( singleMessageContainer );
            tv1.setText( al.get( i ) );
            tv1.setGravity( FOCUS_RIGHT );
            return v;
        }
    }
   /* public class BubbleAdapter extends ArrayAdapter{
        BubbleAdapter(Context ctx, int res)
        {
            super(ctx,res );
        }
        @Override
        public int getCount() {
            return al.size();
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate( R.layout.bubbleyou, viewGroup, false );
            TextView tv1 = ( TextView ) v.findViewById( R.id.MessageYou );
            msgContainer = ( LinearLayout ) v.findViewById( MessageContainer );
            tv1.setText( al.get( i ) );
            tv1.setGravity( FOCUS_LEFT );
            return v;
        }
    }*/
}
