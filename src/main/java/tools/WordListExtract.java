package tools;

import java.util.TreeMap;

import static tools.Denetleme.duzenle;

/**
 * Created with IntelliJ IDEA.
 * User: cagil
 * Date: 5/7/13
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordListExtract {
    static TreeMap<String, String> dictionary = new TreeMap<String,String>();
    static TreeMap<String, String> words = new TreeMap<String,String>();
    public WordListExtract() {
        try {

            Denetleme denetim = new Denetleme();
            dictionary = denetim.load(Denetleme.onerilerDictFile);
            duzenle();
            kok.connection.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
