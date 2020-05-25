import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver implements Runnable{

    DatagramSocket receiverSocket;
    //Constructor
    public Receiver(DatagramSocket receiverSocket){
        this.receiverSocket = receiverSocket;
    }

    @Override
    public void run() {
        try{

            byte[] dataFraServer = new byte[1024];

            DatagramPacket modtagBesked = new DatagramPacket(dataFraServer, dataFraServer.length);

            while (true){
                receiverSocket.receive(modtagBesked);
                String fraServer = new String (modtagBesked.getData(), 0, modtagBesked.getLength());

                //Beskeder som client skal se
                if(fraServer.startsWith("FROM: ")){
                    System.out.println(fraServer);
                }

                if(fraServer.startsWith("NEW CLIENT: ")){
                    System.out.println(fraServer);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
