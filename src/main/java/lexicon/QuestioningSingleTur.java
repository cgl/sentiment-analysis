package lexicon;

import constants.Constants;
import constants.Lexicons;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import semantic.orientation.graph.Configuration;
import semantic.orientation.graph.Vertex;
import semantic.orientation.graph.seeds.SeedManager;
import semantic.orientation.graph.traverse.RandomWalkIterator;
import semantic.orientation.main.SemanticOrientationManager;

import java.io.BufferedReader;
import java.io.FileReader;

public class QuestioningSingleTur {


    public static void main(final String[] args) throws Exception {
        times();

    }

    public static void times()   throws Exception {
        PropertyConfigurator.configure(Constants.log4j);
        final Configuration conf = new Configuration(Constants.dictionaryTur, Constants.negativeSeedTur, Constants.positiveSeedTur);
        final SemanticOrientationManager manager = new SemanticOrientationManager(conf);
        manager.initialize();
        final BufferedReader br = new BufferedReader(new FileReader(Lexicons.wordsDictFile));
        //
        SeedManager seedManager = new SeedManager(manager.getVertexMap(), manager.getGraph(), conf);
        seedManager.logger.setLevel(Level.ALL);
        seedManager.initialize();

        String word;
        Vertex v;
        while ((word = br.readLine()) != null) {
            v = manager.getVertexMap().get(word);
            if (v == null) {
                System.out.println(word+": No such Word exists ! ");
                continue;
            }
            if(seedManager.findPolarity(word))  {
                //System.out.println(v.getId() + " Polarity : " + v.getRealOrientation());
            }
            //else
                //System.out.println(v.getId() + " Polarity False: " + v.getRealOrientation());

        }

    }

        public static void randomWalk()   throws Exception {
        PropertyConfigurator.configure(Constants.log4j);
        final Configuration conf = new Configuration(Constants.dictionaryTur, Constants.negativeSeedTur, Constants.positiveSeedTur);
        final SemanticOrientationManager manager = new SemanticOrientationManager(conf);
        manager.initialize();
        final BufferedReader br = new BufferedReader(new FileReader(Lexicons.wordsDictFile));
        //

        String word;
        Vertex v;
        while ((word = br.readLine()) != null) {
            v = manager.getVertexMap().get(word);
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
                            //System.out.println(" Succes ! " + "orientation : " + orientation + " Path " + iterator.path());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        //System.out.println(" Fail   ! " + "orientation : " + orientation + " No Path ! Last Seen path: " + iterator.path());
                    }
                }
            }
            v.setOrientation(v.getRealOrientation());
        }

    }


    public static void allVertices()   throws Exception {
        PropertyConfigurator.configure(Constants.log4j);
        final Configuration conf = new Configuration(Constants.dictionaryTur, Constants.negativeSeedTur, Constants.positiveSeedTur);
        final SemanticOrientationManager manager = new SemanticOrientationManager(conf);
        manager.initialize();
        int count = 0;
        for (Vertex vertex : manager.getVertexMap().values()) {
            System.out.println(vertex.toString());
            count++;
        }
        System.out.println(count);
    }
    public static void interactive()   throws Exception {
        final Configuration conf = new Configuration(Constants.dictionaryTur, Constants.negativeSeedTur, Constants.positiveSeedTur);
        final SemanticOrientationManager manager = new SemanticOrientationManager(conf);
        final BufferedReader br = new BufferedReader(new FileReader(Lexicons.wordsDictFile));
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
