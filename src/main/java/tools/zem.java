package tools;

import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: cagilulusahin
 * Date: 6/24/13
 * Time: 7:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class zem {
    static Zemberek z = new Zemberek(new TurkiyeTurkcesi());
    public static void main(String[] args) throws Exception{
        String[] s = z.oner("içermak");
        System.out.println(Arrays.toString(s));

    }


}
