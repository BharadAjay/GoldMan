package com.balaji.calculator.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class TCPClient {

    public static Bitmap bitmap;
    public static byte[] pdfdecoded;
    private Context context;
    private String serverMessage, lastMsg;
    private OnMessageReceived mMessageListener = null;
    private OnSocketError onSocketError = null;
    private boolean mRun = false;
    private Socket socket;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    public static boolean isUTF = true;
    public static boolean isPDF = false;

    public String fileName;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(Context context, final OnMessageReceived listener, final OnSocketError onSocketError) {
        this.context = context;
        mMessageListener = listener;
        this.onSocketError = onSocketError;
    }

    public Socket getSocket() {
        return socket;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessageGetImage(String message) {
        isUTF = false;
        isPDF = false;

        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
                lastMsg = message;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageGetPDF(String message) {
        isPDF = true;
        isUTF = false;

        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
                lastMsg = message;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessageGetUTF(String message) {
        isUTF = true;
        isPDF = false;

        if (out != null) {
            try {
                out.writeObject(message);
                out.flush();
                lastMsg = message;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopClient() {
        mRun = false;
        if (socket != null) {
            try {
                if (socket.isConnected()) {
                    socket.shutdownOutput();
                    socket.shutdownInput();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run(String ip, int port) {
        mRun = true;

        try {
            Log.e("TCP SI Client", "SI: Connecting...");

            //create a socket to make the connection with the server

            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            //connect() method connects the specified socket to the server
            socket.connect(socketAddress, 1000);
            try {
                //send the message to the server
                //out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out = new ObjectOutputStream(socket.getOutputStream());

                Log.e("TCP SI Client", "SI: Sent.");

                Log.e("TCP SI Client", "SI: Done.");

                //receive the message which the server sends back
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                in = new ObjectInputStream(socket.getInputStream());

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    //serverMessage = in.readLine();
                    serverMessage = null;
                    Object obj = in.readObject();

                    if (obj instanceof String) {
                        try {
                            serverMessage = (String) obj;

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("exe", "run: " + lastMsg);
                        }
                    } else if (isPDF) {
                        try {
                            byte[] encoded = (byte[]) obj;
                            isUTF = true;

                            pdfdecoded = Base64.getDecoder().decode(encoded);


                            SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh:mm:aa_");
                            Date now=new Date();
                            fileName = formatter.format(now) +"Invoice.pdf";
                            File appSpecificInternalStorageDirectory = context.getFilesDir();
                            File file = new File(appSpecificInternalStorageDirectory, fileName);






                            //File appSpecificInternalStorageDirectory = context.getFilesDir();
                            //File file = new File(appSpecificInternalStorageDirectory, "Invoice.pdf");

                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(TCPClient.pdfdecoded);
                            fos.flush();
                            fos.close();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        try {
                            byte[] b = (byte[]) obj;

                            isUTF = true;

                            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        } catch (Exception ep) {
                            ep.printStackTrace();
                        }
                    }
                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                    }
                }
            } catch (Exception e) {
                Log.e("TCP SI Error", "SI: Error", e);
                e.printStackTrace();
                onSocketError.onError(e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {
            onSocketError.onError(e);
            Log.e("TCP SI Error", "SI: Error", e);
        }
    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}