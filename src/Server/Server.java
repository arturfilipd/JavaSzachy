package Server;
import java.net.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class Server implements Runnable{

    Socket s1, s2;

    @Override
    synchronized public void run() {

        ServerSocket ss = null;
        try {
            ss = new ServerSocket(4999);
        } catch (IOException e) {
            e.printStackTrace();
        }

        s1 = null;
        try {
            s1 = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Thread.currentThread().isInterrupted()) return;
        InputStreamReader s1_in = null;
        try {
            s1_in = new InputStreamReader(s1.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader s1_bf = new BufferedReader(s1_in);
        PrintWriter s1_pr = null;
        try {
            s1_pr = new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Thread.currentThread().isInterrupted()) return;

        s2 = null;

        try {
            s2 = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Thread.currentThread().isInterrupted()){
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        InputStreamReader s2_in = null;
        try {
            s2_in = new InputStreamReader(s2.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader s2_bf = new BufferedReader(s2_in);
        PrintWriter s2_pr = null;
        try {
            s2_pr = new PrintWriter(s2.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int firstPlayerColor = ThreadLocalRandom.current().nextInt(0, 1000);
        if(firstPlayerColor > 500) firstPlayerColor = 0; else firstPlayerColor = 1;
        s1_pr.println(firstPlayerColor);
        s1_pr.flush();
        s2_pr.println(-firstPlayerColor+1);
        s2_pr.flush();
        boolean exit = false;
        int move = (firstPlayerColor == 0)?1:2;

        do{
            if(move == 1){
                //Ruch pierwszego gracza
                String str = null;
                try {
                    str = s1_bf.readLine();
                } catch (IOException e) {
                    exit = true;
                }
                s2_pr.println(str);
                s2_pr.flush();
                move = 2;
                if(str.equals("END")) exit = true;

            }
            else{
                //Ruch drugiego gracza
                String str = null;
                try {
                    str = s2_bf.readLine();
                } catch (IOException e) {
                    exit = true;
                }
                s1_pr.println(str);
                s1_pr.flush();
                move = 1;
                if(str == null || str.equals("END")) exit = true;
            }

            if (Thread.currentThread().isInterrupted()) {
                exit = true;
            }

        }while(!exit);
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void cancel() throws IOException {
        s1.close();
        s2.close();
    }
}