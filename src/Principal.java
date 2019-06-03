import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import static java.util.Comparator.comparing;

import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Principal extends MainWindow
{
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	Socket s = null;
	ServerSocket ss = null;
	public static boolean es_servidor = false;
	public static boolean segunda_vuelta = false;
	public static boolean first_iteration = true;

	Principal () {
		MyLabel l_titulo = new MyLabel("¿Mi computadora es?");
		JPanel loginBox = new JPanel();

		JButton btn_host = new JButton("Host");
		JButton btn_server = new JButton("Server");

		btn_host.addActionListener(this);
		btn_server.addActionListener(this);

		loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
		loginBox.add(l_titulo);
		loginBox.add(btn_host);
		loginBox.add(btn_server);

		int x = 70,y = 70, b = 700,h = 300;
		loginBox.setBounds(x, y, b, h+20);
		loginBox.setBackground(colores.get(0));
		panelCentro.add(loginBox);
	}

	public void Switcher() {
		while (true) {
			if (first_iteration) {
				this.finGUI();
			}
			
			while (Principal.segunda_vuelta) {
				System.out.println("Entro while");
				
				if (Principal.es_servidor) {
					System.out.println("Entro server");
					Server s = new Server();
					s.finGUI();
					this.dispose();
		            try{
		                s.run_me = true;
		                Thread t1 = new Thread(s);
		                t1.start();
		            } catch(Exception ex){
		                ex.printStackTrace();
		            }
					System.out.println("salio server");

				}
				
				else if (!Principal.es_servidor) {
					System.out.println("Entro host");
					Host h = new Host();
					h.finGUI();
					this.dispose();
		            try{
		                h.run_me = true;
		                Thread t1 = new Thread(h);
		                t1.start();
		            } catch(Exception ex){
		                ex.printStackTrace();
		            }
					System.out.println("Salio host");

				}
			}

		}
	}

	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		if (boton == "Host")
		{
			Principal.es_servidor = false;
			Host h = new Host();
			h.finGUI();
			this.dispose();
            try{
                h.run_me = true;
                Thread t1 = new Thread(h);
                t1.start();
            } catch(Exception ex){
                ex.printStackTrace();
            }
			return;
		}

		if (boton == "Server")
		{
			Principal.es_servidor = true;
			Server s = new Server();
			s.finGUI();
			this.dispose();
            try{
                s.run_me = true;
                Thread t1 = new Thread(s);
                t1.start();
            } catch(Exception ex){
                ex.printStackTrace();
            }
			return;
		}
	}
}
