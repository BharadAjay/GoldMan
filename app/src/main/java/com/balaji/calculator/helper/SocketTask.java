package com.balaji.calculator.helper;

import android.content.Context;
import android.os.AsyncTask;

import com.balaji.calculator.SecurePreferences;

public class SocketTask extends AsyncTask<String, String, TCPClient> {
    Context context;
    private static SocketTask socketTask;
    String ip;
    int port;
    TCPClient.OnMessageReceived tcpListener;
    TCPClient mTcpClient;
    OnSocketError onSocketError;

    public static SocketTask getInstance(Context context) {
        if (socketTask == null) {
            String ip = "";
            int port = 8082;
            if (!SecurePreferences.getStringPreference(context, "ip").isEmpty()) {
                ip = SecurePreferences.getStringPreference(context, "ip");
            } else return null;
//            if (SecurePreferences.getStringPreference(context, "port") != null) {
//                if(!SecurePreferences.getStringPreference(context, "port").isEmpty())
//                    port = Integer.parseInt(SecurePreferences.getStringPreference(context, "port"));
//            }else  return null;

            socketTask = new SocketTask(context, ip, port);
            socketTask.execute();
        }
        return socketTask;
    }

    public SocketTask(Context context, String ip, int port) {
        this.context = context;
        this.ip = ip;
        this.port = port;
    }

    public SocketTask(Context context, String ip, int port, TCPClient.OnMessageReceived tcpListener, TCPClient mTcpClient, OnSocketError onSocketError) {
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.tcpListener = tcpListener;
        this.mTcpClient = mTcpClient;
        this.onSocketError = onSocketError;
    }

    @Override
    protected TCPClient doInBackground(String... message) {
        try {
            mTcpClient = new TCPClient(context, tcpListener, onSocketError);
            mTcpClient.run(ip, port);
        } catch (Exception e) {
            if (onSocketError != null)
                onSocketError.onError(e);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(TCPClient tcpClient) {
        if (mTcpClient != null)
            mTcpClient.stopClient();
        super.onPostExecute(tcpClient);

    }

    @Override
    protected void onCancelled(TCPClient tcpClient) {
        if (mTcpClient != null)
            mTcpClient.stopClient();
        super.onCancelled(tcpClient);
    }

    @Override
    protected void onCancelled() {
        if (mTcpClient != null)
            mTcpClient.stopClient();
        super.onCancelled();

    }

    public TCPClient getTcpClient() {
        return mTcpClient;
    }
}