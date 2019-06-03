import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Comparator.comparing;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Server extends MainWindow implements Runnable
{
	public MyLabel l_m1, l_m2, l_m3, l_m4, l_m5;
	public int pc = 0;
	public ArrayList<MyLabel> labels;
	public ArrayList<Client> clients;
    public Client oClient;

	public boolean run_me = false;
	public int puerto = 5400;
	public boolean terminado = false;
	
	Server ()
	{
		labels = new ArrayList<MyLabel>();
		MyLabel l_titulo = new MyLabel("CPU | RAM | SO | Version SO | Ancho de banda | IP");
		l_m1 = new MyLabel("-");
		l_m2 = new MyLabel("-");
		l_m3 = new MyLabel("-");
		l_m4 = new MyLabel("-");
		l_m5 = new MyLabel("-");
		JPanel loginBox = new JPanel();

		loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
		loginBox.add(l_titulo);

		labels.add(l_m1);
		labels.add(l_m2);
		labels.add(l_m3);
		labels.add(l_m4);
		labels.add(l_m5);

        this.oClient = new Client();

		clients = new ArrayList<Client>();
        clients.add(oClient);
        this.orderClients();
        this.setLabelsText(this.clients);

		loginBox.add(l_m1);
		loginBox.add(l_m2);
		loginBox.add(l_m3);
		loginBox.add(l_m4);
		loginBox.add(l_m5);

		int x = 70,y = 70, b = 700,h = 300;
		loginBox.setBounds(x, y, b, h+20);
		loginBox.setBackground(colores.get(0));
		panelCentro.add(loginBox);
	}

	public void run () {
        try {
            // TODO Auto-generated method stub
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;

            Socket s = null;
            ServerSocket ss = new ServerSocket(this.puerto);
            while (this.run_me)
            {
                try
                {
                    // el ServerSocket me da el Socket
                    s = ss.accept();
                    // informacion en la consola
                    //System.out.println("Se conectaron desde la IP: " +s.getInetAddress());
                    // enmascaro la entrada y salida de bytes
                    ois = new ObjectInputStream( s.getInputStream() );
                    oos = new ObjectOutputStream( s.getOutputStream() );
                    // leo el nombre que envia el cliente
                    String nom = (String)ois.readObject();
                    if (this.pc < this.labels.size() - 2) {
                        String ip = "" + s.getInetAddress();
                        String[] respuesta = nom.split(",");
                        this.clients.add(new Client(respuesta[0], respuesta[1], respuesta[2], respuesta[3], respuesta[4]));
                        this.orderClients();
                        this.setLabelsText(this.clients);
                        //server.labels.get(server.pc).setText(
                        //	String.format("%s %s %s %s", respuesta[0], respuesta[1], respuesta[2], respuesta[3])
                        //);
                        this.pc++;
                    }
                    else if(!this.clients.get(0).ip.equals(this.oClient.ip)) {
                        if( oos !=null ) oos.close();
                        if( ois !=null ) ois.close();
                        if( s != null ) s.close();
                        sendIPs(this.clients.get(0).ip);
                        this.run_me = false;
                        break;
                    }
                    //System.out.println(nom);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                } finally {
                    if( oos !=null ) oos.close();
                    if( ois !=null ) ois.close();
                    if( s != null ) s.close();
                }
            }
        } catch(BindException ex){

        } catch(Exception ex){
            ex.printStackTrace();
        }
		Principal.segunda_vuelta = true;
        Principal.es_servidor = false;
	}

	public void setLabelsText(ArrayList<Client> clients) {
		for (int i = 0; i < clients.size(); i++) {
			labels.get(i).setText(clients.get(i).labelText() + " 10 Mbps");
		}
	}

	public void orderClients() {
		clients.sort(comparing(Client::getCpuDouble).thenComparing(Client::getRam));
	}

    public void sendIPs(String message){
    	ArrayList<String> ipes = new ArrayList<String>();
    	
        for(int i=0; i < this.clients.size(); i++){
            if (clients.get(i).ip != this.oClient.ip && !searchArrayList(ipes, clients.get(i).ip) ) {
            	ipes.add(clients.get(i).ip);
                RequestServer(clients.get(i).ip, message);
            }
        }
    }
    
    public boolean searchArrayList(ArrayList<String> array, String texto)
    {
    	boolean encontrado = false;
    	for (String txt : array)
    	{
    		if (txt.equals(texto)) {
    			encontrado = true;
    			break;
    		}
    	}
    	return encontrado;
    }

	public void RequestServer (String ip, String message)
	{
		// TODO Auto-generated method stub
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		Socket s = null;

		try
		{
			s = new Socket(ip, this.puerto);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			oos.writeObject(message);
			//this.btn_enviar.setEnabled(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if( ois != null )
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if( oos != null )
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if( s != null )
				try {
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
