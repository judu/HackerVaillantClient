/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2hackervaillantclient;

import com.google.gson.GsonBuilder;
import hackervaillant.util.Person;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import tp2hackervaillantclient.net.Client;
import tp2hackervaillantclient.net.RequestBuilder;

/**
 *
 * @author judu
 */
public class Main {

   private static final BufferedReader inFromUser =
           new BufferedReader(new InputStreamReader(System.in));

   public static String host;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {

      if (args.length < 1) {
         System.err.println("Usage: prog SERVERIP");
         return;
      }

      host = args[0];

      while (true) {
         printMenu();
         try {
            String choix = inFromUser.readLine();

            switch (Integer.parseInt(choix)) {
               case 1:
                  getPersonByPseudo();
                  break;
               case 2:
                  getCBN();
                  break;
               case 3:
                  addPerson();
                  break;
               default:
                  return;
            }

         } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         }
      }

   }

   public static void printMenu() {
      System.out.println("Choisissez l'action à effectuer :");
      System.out.println("1 - Récupérer une personne à partir de son pseudo.");
      System.out.println("2 - Récupérer un numéro de carte bleue.");
      System.out.println("3 - Ajouter une personne, ainsi que son numéro de CB et son pseudo.");
      System.out.println("4 - Quitter");
      System.out.print("Choisissez [4] : ");
   }

   public static String askPseudo() {
      System.out.println("Entrez le pseudo de la personne voulue : ");
      System.out.print("> ");
      try {
         return inFromUser.readLine();
      } catch (IOException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }

   private static void getPersonByPseudo() {

      String pseudo = askPseudo();
      if (pseudo == null) {
         getPersonByPseudo();
         return;
      }
      String request = new RequestBuilder().type(RequestBuilder.Verb.GET).
              what(RequestBuilder.Elem.P).
              from(RequestBuilder.Elem.N).
              value(pseudo).create();

      Client client = new Client();
      client.connect(host);

      client.send(request);
      client.receiveAndDisplay();
      client.close();
   }

   private static void getCBN() {
      System.out.println("Voulez-vous le récupérer avec :");
      System.out.println("1 - Le pseudo d'une personne.");
      System.out.println("2 - Les informations d'une personne (nom, prénom, date de naissance).");
      System.out.print("Choisissez [1] : ");
      String choix = null;
      try {
         choix = inFromUser.readLine();
      } catch (IOException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }

      switch (Integer.parseInt(choix)) {
         case 2:
            getCBNByPerson();
            break;
         case 1:
         default:
            GetCBNByPseudo();
            break;
      }

   }

   private static void getCBNByPerson() {

      Person person = askPerson();
      GsonBuilder builder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
      String personS = builder.create().toJson(person);

      String request = new RequestBuilder().type(RequestBuilder.Verb.GET).what(RequestBuilder.Elem.CBN).from(RequestBuilder.Elem.P).value(personS).create();
      System.out.println(request);
      Client client = new Client();
      client.connect(host);
      client.send(request);
      client.receiveAndDisplay();
      client.close();
   }

   private static void GetCBNByPseudo() {
      String pseudo = askPseudo();
      if (pseudo == null) {
         GetCBNByPseudo();
         return;
      }

      String request = new RequestBuilder().type(RequestBuilder.Verb.GET).what(RequestBuilder.Elem.CBN).from(RequestBuilder.Elem.N).value(pseudo).create();
      System.out.println(request);
      Client client = new Client();
      client.connect(host);
      client.send(request);
      client.receiveAndDisplay();
      client.close();
   }

   private static Person askPerson() {
      try {
         Person person = new Person();
         System.out.print("Nom de la personne ? ");
         person.nom = inFromUser.readLine();
         System.out.print("Prénom de la personne ? ");
         person.prenom = inFromUser.readLine();
         boolean nok = false;
         do {
            System.out.print("Date de naissance (jj/mm/aaaa) ? ");
            String dateS = inFromUser.readLine();
            try {
               person.dateNaissance = parseDate(dateS);
            } catch (Exception ex) {
               System.out.println("Date mal écrite.");
               nok = true;
            }
         } while (nok);

         return person;
      } catch (IOException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
         return null;
      }

   }

   private static void addPerson() {
      Person personne = askPerson();
      String pseudo = askPseudo();
      String cbn = askCBN();

      String request = new RequestBuilder().type(RequestBuilder.Verb.POST).cbn(cbn).p(personne).n(pseudo).create();
      System.out.println(request);
      Client client = new Client();
      client.connect(host);
      client.send(request);
      client.close();
   }

   private static Date parseDate(String dateS) throws Exception {
      String[] fields = dateS.split("/");
      if (fields.length != 3) {
         throw new Exception("Mauvaise date");
      }

      Calendar cal = Calendar.getInstance();
      cal.set(Integer.parseInt(fields[2]), Integer.parseInt(fields[1]), Integer.parseInt(fields[0]));
      return cal.getTime();
   }

   private static String askCBN() {
      System.out.println("Entrez le numéro de CB : ");
      System.out.print("> ");
      try {
         return inFromUser.readLine();
      } catch (IOException ex) {
         Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
      return null;
   }
}
