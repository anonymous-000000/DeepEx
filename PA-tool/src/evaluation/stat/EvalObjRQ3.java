package evaluation.stat;

import utils.GetSourcePaths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EvalObjRQ3 {
    public int numE, numP, numOverlap;

    public EvalObjRQ3(int numE, int numP, int numOverlap) {
        this.numE = numE;
        this.numP = numP;
        this.numOverlap = numOverlap;
    }

    public static void main(String[] args) throws IOException {
        Properties config = new Properties();
        config.load(Files.newInputStream(Path.of("./src/evaluation/config.properties")));

        GetSourcePaths GetRootPaths = new GetSourcePaths();
        String[] sourcePaths = GetRootPaths.getSourcePaths(config.getProperty("inputDir"), new String[]{".log"}, true);


        List<EvalObjRQ3> objRQ3s = new ArrayList<>();
        for (String file : sourcePaths) {
            List<String> lines = Files.readAllLines(Path.of(file));
            int numE = Integer.valueOf(lines.get(0).split("=")[1]);
            int numP = Integer.valueOf(lines.get(1).split("=")[1]);
            int numOverlap = Integer.valueOf(lines.get(2).split("=")[1]);
            EvalObjRQ3 objRQ3 = new EvalObjRQ3(numE, numP, numOverlap);
            objRQ3s.add(objRQ3);
        }

        EvalRQ3 evaluator = new EvalRQ3(objRQ3s);
        evaluator.evalHitAll();
    }
}



