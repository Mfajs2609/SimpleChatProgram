import java.net.InetAddress;

public class BrugerInformation {

    String brugernavn;
    InetAddress IPAddress;
    int port;

    public BrugerInformation(String brugernavn, InetAddress IPAddress, int port){
        this.brugernavn = brugernavn;
        this.IPAddress = IPAddress;
        this.port = port;
    }

    public String getBrugernavn(){
        return brugernavn;
    }

    public void setBrugernavn(String brugernavn){
        this.brugernavn = brugernavn;
    }

    public InetAddress getIPAddress(){
        return IPAddress;
    }

    public void setIPAddress(InetAddress IPAddress){
        this.IPAddress = IPAddress;
    }

    public int getPort(){
        return port;
    }

    public void setPort(int port){
        this.port = port;
    }

    @Override
    public String toString(){
        String brugerInfo = new String("Brugernavn: " + brugernavn +", IP: " + IPAddress + ", Port: " + port );
        return brugerInfo;
    }
}
