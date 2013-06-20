import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;


/**
 * Created with IntelliJ IDEA.
 * User: cagil
 * Date: 3/24/13
 * Time: 6:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class Exporter {
    //jdbcDriver=org.sqlite.JDBC


    public static void main(String[] args) throws Exception {
        FastVector atts;
        FastVector sentVals;
        atts = new FastVector();
        sentVals = new FastVector();
        atts.addElement(new Attribute("reviewid"));
        atts.addElement(new Attribute("sentenceid"));
        atts.addElement(new Attribute("sentence", (FastVector) null));
        atts.addElement(new Attribute("rating"));
        sentVals.addElement("negative");
        sentVals.addElement("neutral");
        sentVals.addElement("positive");
        sentVals.addElement("irrelevant");  //-99
        atts.addElement(new Attribute("sentiment", sentVals));
        InstanceQuery query = null;

        try {
            query = new InstanceQuery();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        query.setDatabaseURL("jdbc:sqlite:resources/db/movie.db");
        query.setQuery("select * from Sentence");
        Instances data = null;
        try {
            data = query.retrieveInstances();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Instance inst;
        //while ((inst = loader.getNextInstance(structure)) != null)
          //  data.add(inst);
        for (int i = 0; i < data.numAttributes(); i++) {
            System.out.print(data.instance(i).attribute(i).name()+" ");
        }
        System.out.println("");
        for (int i = 0; i < 1000; i++) {//data.numInstances(); i++) {
            //double[] values = new double[data.numAttributes()];
            //values[1] = data.attribute(1).parseDate("2001-11-09");
            //System.out.println(data.instance(i).attribute(1));
            System.out.println(data.instance(i));
        }
          /*
        data.setClassIndex(data.numAttributes()-1);

        Classifier cModel = (Classifier)new NaiveBayes();
        cModel.buildClassifier(data);
             */
        query.close();
    }
}
