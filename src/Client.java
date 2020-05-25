import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public static void main (String[]   args) throws IOException {

        try {

            DatagramSocket clientSocket = new DatagramSocket();

            //valg af ip
            System.out.println("Skriv IP adresse: ");
            BufferedReader brugerIP = new BufferedReader(new InputStreamReader(System.in));
            String valgtBrugerIP = brugerIP.readLine();

            //valg af brugernavn
            System.out.println("Skriv brugernavn: ");
            BufferedReader brugerNavn = new BufferedReader(new InputStreamReader(System.in));
            String valgtBrugerNavn = brugerNavn.readLine();


            InetAddress brugerIPAdress = InetAddress.getByName(valgtBrugerIP);


            //Buffers
            byte[] sendToServer = new byte[1024];
            byte[] receivedFromServer = new byte[1024];


            String joinServer = "JOIN: " + valgtBrugerNavn;
            sendToServer = joinServer.getBytes();


            DatagramPacket bekraeftBrugerNavn = new DatagramPacket(sendToServer, sendToServer.length, brugerIPAdress, 20202);
            clientSocket.send(bekraeftBrugerNavn);


            DatagramPacket serverTilbageMelding = new DatagramPacket(receivedFromServer, receivedFromServer.length);
            clientSocket.receive(serverTilbageMelding);

            //Kontrollere om brugernavnet er taget
            if (receivedFromServer.equals("JOIN ERROR: Brugernavn er besat ")) {
                System.out.println("Brugernavn er besat, prøv igen...");

            } else {
                //Printer til brugeren og starter en Thread
                System.out.println("JOIN OK: Velkommen " + valgtBrugerNavn);
                System.out.println("Du kan nu chatte");

                Thread messages = new Thread(new Receiver(clientSocket));
                messages.start();

                while (true) {

                    BufferedReader beskedFraBruger = new BufferedReader(new InputStreamReader(System.in));
                    String beskedDerSendes = "MESSAGE: " + beskedFraBruger.readLine();

                    byte[] dataSender;

                    dataSender = beskedDerSendes.getBytes();

                    DatagramPacket beskedSender = new DatagramPacket(dataSender, dataSender.length, brugerIPAdress, 20202);
                    clientSocket.send(beskedSender);

                    if (beskedDerSendes.contains("QUIT")) {
                        String farvelBesked = beskedDerSendes;
                        dataSender = farvelBesked.getBytes();

                        DatagramPacket farvelBeskedSender = new DatagramPacket(dataSender, dataSender.length, brugerIPAdress, 20202);
                        clientSocket.send(farvelBeskedSender);

                        Runtime.getRuntime().exit(0);
                    }
                }
            }

            System.out.println("Du har nu forladt chatten!");

            clientSocket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
