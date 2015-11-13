/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package inpresferriesserveurfreetax_xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Sh1fT
 */

public final class DemarrerServeur extends Thread {
    protected Serveur_IFreetax parent;

    /**
     * Creates new instance DemarrerServeur
     * @param parent 
     */
    public DemarrerServeur(Serveur_IFreetax parent) {
        this.setParent(parent);
    }

    public Serveur_IFreetax getParent() {
        return this.parent;
    }

    protected void setParent(Serveur_IFreetax parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            this.getParent().setSSocket(new ServerSocket(this.getParent().getServerPort()));
            while(!this.isInterrupted()) {
                this.getParent().setCSocket(this.getParent().getSSocket().accept());
                this.getParent().getClientLabel().setText(this.getParent().getCSocket().getInetAddress().getHostAddress());
                BufferedReader br = new BufferedReader (new InputStreamReader(this.getParent().getCSocket().getInputStream()));
                ObjectOutputStream oos = new ObjectOutputStream(this.getParent().getCSocket().getOutputStream());
                String infos = br.readLine();
                StringTokenizer st  = new StringTokenizer(infos, ";");
                String commande = st.nextToken();
                String message = null;
                if (commande.compareTo("LOGIN") == 0) {
                    String username = st.nextToken();
                    String password = st.nextToken();
                    if (this.getParent().getProtocole().login(username, password))
                        message = "ok";
                    else
                        message = "ko";
                    oos.writeObject(message);
                } else if (commande.compareTo("GET_STATISTICS_DESCR") == 0) {
                    String category = st.nextToken();
                    String week = st.nextToken();
                    String month = st.nextToken();
                    oos.writeObject(this.getParent().getProtocole().
                        getStatisticsDesc(category, week, month));
                } else if (commande.compareTo("GET_GRAPH_1D") == 0) {
                    String category = st.nextToken();
                    String week = st.nextToken();
                    String month = st.nextToken();
                    String type = st.nextToken();
                    oos.writeObject(new ImageIcon(this.getParent().getProtocole().getGraph1D(
                        category, week, month, type).createBufferedImage(500, 300)));
                } else if (commande.compareTo("GET_GRAPH_2D") == 0) {
                    String category = st.nextToken();
                    String week = st.nextToken();
                    String month = st.nextToken();
                    String type = st.nextToken();
                    oos.writeObject(new ImageIcon(this.getParent().getProtocole().getGraph2D(
                        category, week, month, type).createBufferedImage(500, 300)));
                }
                oos.close();
                br.close();
                this.getParent().getCSocket().close();
                this.getParent().getClientLabel().setText("aucun");
            }
            this.getParent().getSSocket().close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getParent(), ex.getLocalizedMessage(),
                "Aïe Aïe Aïe !", JOptionPane.ERROR_MESSAGE);
        }
    }
}