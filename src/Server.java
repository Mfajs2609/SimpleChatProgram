import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws IOException {
        //Starter min server..
        //Datagramsocket på port 20202.
        DatagramSocket serverSocket = new DatagramSocket(20202);

        ArrayList<BrugerInformation> participants = new ArrayList<>();

        //Buffers til at modtage og sende data.
        byte[] dataReceiver = new byte[1024];
        byte[] dataSender = new byte[1024];

        //Datagrampacket til at modtage data fra brugeren
        DatagramPacket dataPacketReceiver = new DatagramPacket(dataReceiver, dataReceiver.length);
        System.out.println("Server starter...");

        while (true){
            //Fortæller min socket at den skal pakke dataen ud
            serverSocket.receive(dataPacketReceiver);

            //Finder client informationen jeg ønsker
            InetAddress IPAddress = dataPacketReceiver.getAddress();
            int port = dataPacketReceiver.getPort();
            String clientMessage = new String(dataPacketReceiver.getData(),0, dataPacketReceiver.getLength()).trim();

            //JOIN
            if(clientMessage.startsWith("JOIN: ")){
                //Hvis brugeren eksistere
                boolean brugerExsistere = false;

                for (int i = 0 ; i < participants.size(); i++){
                    if(participants.get(i).getBrugernavn().contains(clientMessage.substring(6))){
                        brugerExsistere = true;
                    }
                }

                //Hvis brugeren ikke eksistere
                //Vil sende JOIN OK til client
                //Pakker min Accept String til bytes
                if(!brugerExsistere) {
                    String Accept = "JOIN OK";
                    dataSender = Accept.getBytes();

                    //Tilføjer den nye client til min liste.
                    //Printer listen ud.
                    participants.add(new BrugerInformation(clientMessage.substring(6), IPAddress, port));
                    System.out.println(participants.toString());

                    //datagrampacket der skal sende en bekræftelse tilbage til brugeren.
                    DatagramPacket serverRespons = new DatagramPacket(dataSender, dataSender.length, IPAddress, port);
                    serverSocket.send(serverRespons);
                    System.out.println("JOIN OK: " + clientMessage.substring(6));

                    for (int l = 0; l < participants.size(); l++) {
                        String nyBruger = "NEW CLIENT: " + clientMessage.substring(6);
                        dataSender = nyBruger.getBytes();

                        if (dataPacketReceiver.getPort() != participants.get(l).getPort() || dataPacketReceiver.getAddress() != participants.get(l).getIPAddress()) {
                            DatagramPacket nyBrugerOnline = new DatagramPacket(dataSender, dataSender.length, participants.get(l).getIPAddress(), participants.get(l).getPort());
                            serverSocket.send(nyBrugerOnline);
                        }
                    }
                    dataSender = clientMessage.getBytes();
                    DatagramPacket sendMessage = new DatagramPacket(dataSender, dataSender.length, IPAddress, port);
                    serverSocket.send(sendMessage);
                }else {
                    String brugerNavnBesat = "JOIN ERROR: Brugernavn er besat ";
                    dataSender = brugerNavnBesat.getBytes();

                    DatagramPacket fejlBesked = new DatagramPacket(dataSender, dataSender.length, IPAddress, port);
                    serverSocket.send(fejlBesked);
                }
                }
            if (clientMessage.startsWith("MESSAGE: ")){
                String userName = null;
                for (int l = 0; l < participants.size(); l++){
                    if(participants.get(l).getIPAddress().equals(dataPacketReceiver.getAddress())){
                        if(participants.get(l).getPort() == (dataPacketReceiver.getPort())){
                            userName = participants.get(l).getBrugernavn().trim();
                        }
                    }
                }
                if(userName != null){
                    String sendTilBrugerne = "FROM: " + userName + " Message: " + clientMessage.substring(9);
                    System.out.println(sendTilBrugerne);
                    dataSender = sendTilBrugerne.getBytes();

                    for (int l = 0; l < participants.size(); l++){
                        DatagramPacket sendBeskedTilBruger = new DatagramPacket(dataSender, dataSender.length, participants.get(l).IPAddress, participants.get(l).getPort());
                        serverSocket.send(sendBeskedTilBruger);

                    }
                }
            }

            if(clientMessage.startsWith("QUIT")){
                for (int l = 0; l < participants.size(); l++){
                    if(dataPacketReceiver.getPort() == participants.get(l).getPort() && dataPacketReceiver.getAddress() == participants.get(l).getIPAddress()){
                        participants.remove(l);
                        System.out.println("Brugere på chatten: ");
                        System.out.println(participants.toString());
                    }
                }
            }
        }
    }
}
