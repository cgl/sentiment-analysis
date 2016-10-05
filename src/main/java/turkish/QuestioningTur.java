/**
 * Created with IntelliJ IDEA.
 * User: cagil
 * Date: 8/14/13
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
package turkish;

import constants.constants.Constants;
import org.apache.log4j.PropertyConfigurator;
import semantic.orientation.graph.Configuration;
import semantic.orientation.graph.Vertex;
import semantic.orientation.graph.traverse.RandomWalkIterator;
import semantic.orientation.main.SemanticOrientationManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class QuestioningTur {

    public static void main(final String[] args) throws Exception {
        PropertyConfigurator.configure(Constants.log4j);
        final Configuration conf = new Configuration(Constants.dictionaryTur, Constants.negativeSeedTur, Constants.positiveSeedTur);
        final SemanticOrientationManager manager = new SemanticOrientationManager(conf);
        manager.initialize();
        //
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Vertex v;
        String id;
        while (true) {
            try {
                System.out.println("Enter word#pos:\n");
                id = br.readLine();
                if (id.equals("q")) {
                    break;
                }
                v = manager.getVertexMap().get(id);
                if (v == null) {
                    System.out.println("No such Word exists ! ");
                    continue;
                }
                System.out.println(v.getId() + " Polarity : " + v.getRealOrientation());
                v.setOrientation(0);
                for (int orientation = -1; orientation < 2; orientation += 2) {
                    System.out.println("Orientation : " + orientation);
                    for (int i = 0; i < 10; i++) {
                        final RandomWalkIterator iterator = new RandomWalkIterator(manager.getGraph(), v, conf, manager.getVertexMap());
                        boolean found = false;
                        while (iterator.hasNext()) {
                            final Vertex next = iterator.next();
                            if (next.getOrientation() == orientation) {
                                System.out.println(" Succes ! " + "orientation : " + orientation + " Path " + iterator.path());
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println(" Fail   ! " + "orientation : " + orientation + " No Path ! Last Seen path: " + iterator.path());
                        }
                    }
                }
                v.setOrientation(v.getRealOrientation());
            } catch (final Exception e) {
                System.out.println(" Error : " + e.getMessage());
                continue;
            }
        }
        System.out.println("Exits :");
    }
}
