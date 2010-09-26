/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2hackervaillantclient.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author judu
 */
public class Client {

   public static final int PORT = 1025;

   Socket clientSocket;

   DataOutputStream outToServer;

   BufferedReader inFromServer;

   public Client() {
   }

   public void connect(String ip) {
      try {
         clientSocket = new Socket(ip, PORT);
         outToServer = new DataOutputStream(clientSocket.getOutputStream());
         inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      } catch (UnknownHostException ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void send(String request) {
      try {
         outToServer.writeBytes(request);
      } catch (IOException ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void receiveAndDisplay() {
      try {
         String response = inFromServer.readLine();
         System.out.println(response);
      } catch (IOException ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void close() {
      try {
         inFromServer.close();
      } catch (IOException ex) {
         Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
