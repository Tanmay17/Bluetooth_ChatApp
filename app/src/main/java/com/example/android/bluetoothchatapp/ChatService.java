package com.example.android.bluetoothchatapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ChatService {
    Handler h;
    ReadWriteThread rwt;
    ChatService(boolean isClient, BluetoothDevice bd, Handler h)
    {
        this.h=h;
        if(!isClient){
            ServerThread st=new ServerThread();
            st.start();
        }else{
            ClientThread ct=new ClientThread(bd);
            ct.start();
        }
    }
    public class ServerThread extends Thread
    {
        BluetoothAdapter adapter;
        ServerThread()
        {
            adapter=BluetoothAdapter.getDefaultAdapter();
        }
        @Override
        public void run()
        {
            try {
                BluetoothServerSocket server = adapter.listenUsingRfcommWithServiceRecord(
                        "server", UUID.fromString("4e5d48e0-75df-11e3-981f-0800200c9a66"));
                BluetoothSocket client=server.accept();
                BluetoothDevice bd=client.getRemoteDevice();
                String name=bd.getName();
                h.obtainMessage(1,name).sendToTarget();
                rwt=new ReadWriteThread(client);
                rwt.start();
            }catch(Exception e)
            {}
        }
    }
    public class ClientThread extends Thread
    {
        BluetoothDevice bd;
        ClientThread(BluetoothDevice bd)
        {
            this.bd=bd;
        }
        @Override
        public void run() {
            try {
                BluetoothSocket client = bd.createRfcommSocketToServiceRecord(
                        UUID.fromString("4e5d48e0-75df-11e3-981f-0800200c9a66"));
                client.connect();
                rwt=new ReadWriteThread(client);
                rwt.start();
            }catch(Exception e)
            {}
        }
    }
    public class ReadWriteThread extends Thread
    {
        InputStream in;
        OutputStream out;
        ReadWriteThread(BluetoothSocket client)
        {
            try {
                in = client.getInputStream();
                out = client.getOutputStream();
            }catch(Exception e)
            {}
        }
        @Override
        public void run() {
            byte b[]=new byte[100];
            while(true){
                try {

                    in.read(b);
                    String msg = new String(b).trim();
                    if (!msg.equals("")) {
                        h.obtainMessage(2, msg).sendToTarget();
                    }
                    for (int i = 0; i < 100; i++)
                        b[i] = ' ';
                }catch(Exception e)
                {}
            }
        }
        public void writeMessage(String msg){
            try {
                out.write( msg.getBytes() ) ;
            }catch(Exception e)
            {}
        }
    }
}
