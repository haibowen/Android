package com.example.myapplicationtest01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {

    private static final int PORT = 8888;
    private List<Socket> mlist = new ArrayList<Socket>();
    private ServerSocket serverSocket = null;
    private ExecutorService executorService = null;
    private String receiveMessage;
    private String sendMessage;


    public static void main(String[] args) {


        new SocketServer();
    }


    public SocketServer() {

        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newCachedThreadPool();
            System.out.println("服务已经启动");
            Socket client = null;
            while (true) {

                client = serverSocket.accept();
                mlist.add(client);
                executorService.execute(new Service(client));


            }

        } catch (Exception e) {
            e.printStackTrace();


        }


    }


    class Service implements Runnable {

        private Socket socket;
        private BufferedReader bufferedReader=null;
        private PrintWriter printWriter=null;

        public Service(Socket socket) {
            this.socket = socket;

            try {
                printWriter=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8")),true);
                bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                printWriter.println("成功连接服务器");





            }catch (IOException e) {

                e.printStackTrace();



            }
        }

        @Override
        public void run() {

            try {
                while (true){


                    if ((receiveMessage=bufferedReader.readLine())!=null){
                        System.out.println("收到的信息"+receiveMessage);
                        if (receiveMessage.equals("0")){
                            System.out.println("客户端请求断开连接");
                            printWriter.println("服务端断开连接");
                            mlist.remove(socket);
                            bufferedReader.close();
                            socket.close();
                            break;


                        }else {

                            sendMessage="已经接受的"+receiveMessage;
                            printWriter.println(sendMessage);

                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();

            }

        }



    }



}
