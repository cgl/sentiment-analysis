import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.Kelime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: cagil
 * Date: 4/23/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class kok {
    static Statement statement = null;
    static ResultSet resultSet = null;
    static Connection connection = null;
    static String[] words;
    static Zemberek z = new Zemberek(new TurkiyeTurkcesi());

    public static void main(String [] args) throws Exception{
        kokbul();
    }

    public static void oneriler(String dizi[], Zemberek z) {
        int b = 0;
        while (b < dizi.length) {
            String[] oneriler = z.oner(dizi[b]);
            System.out.println(">>>" + dizi[b] + " kelimesi icin oneriler:");
            for (int i = 0; i < oneriler.length; i++) {
                System.out.println(oneriler[i]);
            }
            b++;
        }
        System.out.println("\n");
    }

    public static String [] oner(String kelime, Zemberek z) {
        String[] oneriler = z.oner(kelime);
        return oneriler;
    }

    public static void kelimeCozumle(String dizi[], Zemberek z) {
        int cozumlenen = 0,cozumlenemeyen = 0,onerilen= 0,onerilemeyen = 0;
        int d = 0;
        while (d < dizi.length) {
            //System.out.println(">>>" + dizi[d] + " için cozumlemeler:");
            Kelime[] cozumler = z.kelimeCozumle(dizi[d]);
            if(cozumler.length != 0){
                cozumlenen++;
                System.out.println(cozumler[0]);
                for (Kelime kelime : cozumler) {
                    //System.out.println(kelime);
                }
            }
            else{
                cozumlenemeyen++;
                String [] oneriler = oner(dizi[d],z);
                if (oneriler.length == 0 ){
                    onerilemeyen++;
                    //System.out.println(dizi[d]);
                    //System.out.println(">>> " + dizi[d] + " için önerme bulunamamıştır.");
                }
                else {
                    onerilen++;

                    System.out.println(">>>" + dizi[d] + " kelimesi icin oneriler:");
                    System.out.println("Uygulanacak öneriyi seçip numarasnı giriniz:");
                    System.out.println("0. " + dizi[d]);
                    for (int i = 0; i < oneriler.length; i++) {
                        System.out.println((i+1) + ". " + oneriler[i]);
                    }
                    int l;
                    Scanner v = new Scanner(System.in);
                    l = v.nextInt();
                    System.out.println(dizi[d]+" "+  oneriler[l-1] + " olarak değiştirilecek");

                }
                //System.out.println(dizi[d]);
                //System.out.println(">>> " + dizi[d] + " için çözümleme sonucu bulunamamıştır.");
            }

            d++;
        }
        //System.out.println("Çözümlenen "+cozumlenen+", Çözümlenemeyen "+cozumlenemeyen+", Önerilen "+onerilen+", Önerilemeyen "+onerilemeyen+" kelime bulunmaktadır.");
        //System.out.println("\n");
    }
    public static void kelimeAyristir(String dizi[], Zemberek z) {
        int h = 0;
        while (h < dizi.length) {

            List<String[]> ayrisimlar = z.kelimeAyristir(dizi[h]);
            if(!ayrisimlar.isEmpty()){
                //System.out.println(">>>" + dizi[h] + " için ayristirma sonuclari:");
                System.out.println(ayrisimlar.get(0)[0]);
                for (String[] strings : ayrisimlar)   {
                    //System.out.println(Arrays.toString(strings));
                }
            }
            else {//if(! dizi[h].equals(".") | dizi[h] != "," ){
                //System.out.println(dizi[h]);
                //System.out.println(">>> " + dizi[h] + " için ayristirma sonucu bulunamamıştır.");
            }
            h++;
        }
    }
    public static void kokbul() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            //connection = DriverManager.getConnection("jdbc:sqlite:/Users/cagil/Documents/thesis-mac/scripts/movie.db");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/movie.db");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Sentence");
            while (resultSet.next()) {
                String body = resultSet.getString("body");

                if (body.contains(" ")) {
                    words = body.split(" ");
                } else {
                    words = new String[] {body};
                }
                /*
                for (String word : words){
                    System.out.println(word);
                }         */
                kelimeCozumle(words,z);
                //kelimeAyristir(words, z  );

            }
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
