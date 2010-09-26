package tp2hackervaillantclient.net;

import com.google.gson.GsonBuilder;
import hackervaillant.util.Person;

/**
 *
 * @author judu
 */
public class RequestBuilder {

   public enum Verb {

      POST, GET;

      @Override
      public String toString() {
         return name();
      }
   }

   public enum Elem {

      P, CBN, N;

      @Override
      public String toString() {
         return name();
      }
   }
   private Verb type;

   private String cbn;

   private String n;

   private Person p;

   private String what;

   private String from;

   private String value;

   public RequestBuilder() {
   }

   public RequestBuilder type(Verb type) {
      this.type = type;
      return this;
   }

   public RequestBuilder cbn(String value) {
      cbn = value;
      return this;
   }

   public RequestBuilder n(String value) {
      n = value;
      return this;
   }

   public RequestBuilder p(Person value) {
      p = value;
      return this;
   }

   public RequestBuilder what(Elem type) {
      if (type == Elem.CBN || type == Elem.P) {
         what = type.toString();
      }
      return this;
   }

   public RequestBuilder from(Elem elem) {
      if (elem == Elem.N || elem == Elem.P) {
         from = elem.toString();
      }
      return this;
   }

   public RequestBuilder value(String value) {
      this.value = value;
      return this;
   }

   public String create() {
      StringBuilder builder = new StringBuilder();
      if (type == Verb.POST) {
         builder.append("POST\n");
         builder.append("CBN:").append(cbn).append('\n');
         builder.append("N:").append(n).append('\n');
         builder.append("P:").append(new GsonBuilder().setDateFormat("dd/MM/yyyy").create().toJson(p)).append('\n');
      } else {
         builder.append("GET\n");
         builder.append("what:").append(what).append('\n');
         builder.append("from:").append(from).append('\n');
         builder.append("value:").append(value).append('\n');
      }
      return builder.toString();
   }
}
