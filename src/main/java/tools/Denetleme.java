package tools;

import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.KelimeTipi;
import net.zemberek.yapi.Kok;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: cagil
 * Date: 5/6/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Denetleme {
    static TreeMap<String, String> wordlist = new TreeMap<String,String>();
    static TreeMap<String, String> dictionary = new TreeMap<String,String>();
    static Statement statement = null;
    static ResultSet resultSet = null;
    static Connection connection = null;
    public static List<String[]> sentences = new ArrayList<String[]>();
    static String[] words;
    static Zemberek z = new Zemberek(new TurkiyeTurkcesi());

    public static String onerilerDictFile = "/Users/cagil/IdeaProjects/sentiment-analysis/src/main/resources/oneriler.txt";
    public static String wordsDictFile = "/Users/cagil/IdeaProjects/sentiment-analysis/src/main/resources/wordnet_words_tur";
    public static void main(String[] args) throws Exception{
        try {
            init();
            process();
            duzenle();


            //listele();
            save(wordlist,wordsDictFile);
            connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public static void init() throws ClassNotFoundException, SQLException , IOException{
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:/Users/cagil/Documents/thesis-mac/scripts/movie.db");
        dictionary = load(onerilerDictFile);
        process();
    }

    public static void process() throws SQLException, IOException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM sentence_copy");

        while (resultSet.next()) {
            String body = resultSet.getString("body").replace("&quot;", "");
            if (body.contains(" ")) {
                words = body.split(" +");
            } else {
                words = new String[] {body};
            }
            sentences.add(words);
        }
    }

    public static void duzenle() throws SQLException, IOException {
        for (String[] sentence : sentences) {
            List<String[]> duzeltilecekler = kelimeDuzelt(sentence,z);
            System.out.println("");
            save(dictionary,onerilerDictFile);
        }
    }
    public static void listele() throws SQLException, IOException {
        for (String[] sentence : sentences) {
            //List<String[]> duzeltilecekler =
            //System.out.println(Arrays.toString(sentence));
            kelimeListele(sentence, z);
            System.out.println("");

        }
    }

    public static void kelimeListele(String dizi[], Zemberek z) throws IOException {
        int d = 0;
        String kelime;
        while (d < dizi.length) {

            if (!z.kelimeDenetle(dizi[d]) & !StringUtils.isNumeric(dizi[d]) & !dizi[d].isEmpty())  {

                kelime = StringUtils.strip(dizi[d],"[!?(),-_:.]") ;
                if (!kelime.equals(dizi[d]))
                    System.out.println("*** "+dizi[d]+" "+kelime+" olarak değişti.");
                if (z.kelimeDenetle(kelime) | StringUtils.isNumeric(kelime) | kelime.isEmpty()) {
                    System.out.println("+++ "+kelime+" denetlemeden geçti");
                    dizi[d] = kelime;
                    continue;
                }
                if(dictionary.containsKey(kelime)){
                    System.out.println(">>>" + kelime + " kelimesini sözlükte bulduk");
                    dizi[d] = dictionary.get(kelime);
                    continue;
                }
                else{
                    System.out.println("Houston "+dizi[d]);
                    d++;
                    continue;
                }
            }
            else{
               Kelime[] cozumler = z.kelimeCozumle(dizi[d],CozumlemeSeviyesi.TEK_KOK);
               if(cozumler.length != 0){
                   String type = cozumler[0].kok().tip().name();
                   Kok kok = cozumler[0].kok();
                   String icerik = cozumler[0].kok().icerik();
                   KelimeTipi tip = kok.tip();
                   //System.out.println("****************"+tip);
                   if (tip.equals(KelimeTipi.KISALTMA)) {
                       //kok.setAsil(icerik);
                       //System.out.println("****************"+kok);
                   }
                   if (tip.equals(KelimeTipi.FIIL)) {
                       if(z.kelimeDenetle(icerik+"mak"))
                            wordlist.put(icerik+"mak", type);
                       else
                            wordlist.put(icerik+"mek", type);
                       /*
                       TemelEkYonetici ekyonetici = new TemelEkYonetici();
                       Ek mastar = ekyonetici.ek(TurkceEkAdlari.FIIL_MASTAR_MEK);
                       String sonuc = z.kelimeUret(cozumler[0].kok(), Arrays.asList(mastar));
                       */
                   }
                   else
                        wordlist.put(cozumler[0].kok().icerik(), type);
               }
                else
                   System.out.println("Houston we have a problem "+dizi[d]);
            }
            d++;
        }
    }

    public static List<String[]> kelimeDuzelt(String dizi[], Zemberek z) throws IOException {
        int d = 0;
        List<String[]> dlist;
        dlist = new ArrayList<String[]>();
        while (d < dizi.length) {
            if (!z.kelimeDenetle(dizi[d]) & !StringUtils.isNumeric(dizi[d]))  {
                String kelime = StringUtils.strip(dizi[d],"[!?(),-_:.]") ;
                //String kelime = dizi[d].replaceAll("[!?(),-_:.]+", " ").replaceAll("&quot;", " ");
                if (!kelime.equals(dizi[d]))
                    System.out.println("*** "+dizi[d]+" "+kelime+" olarak değişti.");
                if (z.kelimeDenetle(kelime) | StringUtils.isNumeric(kelime) | kelime.isEmpty()) {
                    d++;
                    continue;
                }
                if(dictionary.containsKey(kelime)){
                    System.out.println(">>>" + kelime + " kelimesini sözlükte bulduk");
                    dlist.add(new String[]{kelime , dictionary.get(kelime)} );
                }
                else{
                    String [] oneriler = z.oner(kelime);
                    System.out.println("");
                    System.out.println(">>> " + kelime + " kelimesi icin oneriler:");
                    System.out.println("Uygulanacak öneriyi seçip numarasnı giriniz:");
                    System.out.println("0. " + kelime);
                    for (int i = 0; i < oneriler.length; i++) {
                        System.out.println((i+1) + ". " + oneriler[i]);
                    }
                    int l;
                    Scanner v = new Scanner(System.in);
                    l = v.nextInt();

                    if(l == 99)
                        save(dictionary,onerilerDictFile);
                    else if (l==0) {
                        System.out.println(Arrays.toString(dizi));
                        String o;
                        Scanner k = new Scanner(System.in);
                        o = k.next();
                        //System.out.println(kelime+" "+  o + " olarak değiştirilecek");
                        dlist.add(new String[]{kelime,o});
                        dictionary.put(kelime,o);
                    }
                    else{
                        System.out.println(kelime+" "+  oneriler[l-1] + " olarak değiştirilecek");
                        dlist.add(new String[]{kelime,oneriler[l-1]});
                        dictionary.put(kelime,oneriler[l-1]);
                    }
                }
            }
            d++;
        }
        return dlist;
    }

    private static void save(TreeMap<String,String> dictionary, String wordlist) throws IOException {
        FileWriter file = new FileWriter(wordlist);
        BufferedWriter writer = new BufferedWriter(file);
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = "+ entry.getValue());
            writer.append(entry.getKey() + ":" + entry.getValue());    //TO DO space required in between
            writer.newLine();
        }

        writer.close();
        file.close();
    }

    protected static TreeMap<String,String> load(String wordlist) throws IOException {
        FileReader file = new FileReader(wordlist);
        BufferedReader reader = new BufferedReader(file);
        String word;

        while((word = reader.readLine()) != null) {
            String []k = word.split(":");
            if(k.length == 2)
                dictionary.put(k[0],k[1]);
            else
                System.out.println("Problem "+ Arrays.toString(k));
        }

        reader.close();
        file.close();
        return dictionary;
    }
}