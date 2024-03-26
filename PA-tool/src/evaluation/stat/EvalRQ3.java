package evaluation.stat;

import java.util.List;

public class EvalRQ3 {
    List<EvalObjRQ3> evalObjRQ3s;

    public EvalRQ3 (List<EvalObjRQ3> evalObjRQ3s) {
        this.evalObjRQ3s = evalObjRQ3s;
    }

    public void evalHitAll() {
        evalHitAllForPrecision();
        evalHitAllForRecall();
    }

    private void evalHitAllForPrecision () {
        int all = 0;
        int correct = 0;
        for (EvalObjRQ3 evalObjRQ3 : evalObjRQ3s) {
            if (evalObjRQ3.numP <= 3) {
                all ++;
            }

            if (evalObjRQ3.numP == evalObjRQ3.numOverlap) {
                correct++;
            }
        }

        System.out.println(String.format("Hit-All_precision: %d / %d (%f)", correct, all, ((double)correct)/all));
    }

    private void evalHitAllForRecall () {
        int all = 0;
        int correct = 0;
        for (EvalObjRQ3 evalObjRQ3 : evalObjRQ3s) {
            if (evalObjRQ3.numE <= 3) {
                all ++;
            }

            if (evalObjRQ3.numE == evalObjRQ3.numOverlap) {
                correct++;
            }
        }

        System.out.println(String.format("Hit-All_recall: %d / %d (%f)", correct, all, ((double)correct)/all));
    }

    public void eval(String statType) {
        if (statType.equals("recall")) {
            evalRecall();
        }

        if (statType.equals("precision")) {
            evalPrecision();
        }
    }

    private void evalRecall(){
        for (int kType = 1; kType <= 4; kType++) {
            int numInstances = numType(kType, "recall");
            for (int minHit = 1; minHit <= kType; minHit++) {
                if (minHit == 4) {
                    continue;
                }
                int hit = numHit(minHit, kType, "recall");
                System.out.println("=============");
                System.out.println(String.format("# Ex. Types in catch clause in Oracle Set : %d (%d)", kType, numInstances));
                System.out.println(String.format("Hit-%d, Accuracy=%d (%f)", minHit, hit, ((double)hit)/numInstances));
                System.out.println();
            }
        }
    }

    private void evalPrecision(){
        for (int kType = 1; kType <= 3; kType++) {
            int numInstances = numType(kType, "precision");
            for (int minHit = 1; minHit <= kType; minHit++) {
                int hit = numHit(minHit, kType, "precision");
                System.out.println("=============");
                System.out.println(String.format("# Ex. Types in catch clause in Predicted Set : %d (%d)", kType, numInstances));
                System.out.println(String.format("Hit-%d, Accuracy=%d (%f)", minHit, hit, ((double)hit)/numInstances));
                System.out.println();
            }
        }
    }

    private int numType (int kType, String statType) {
        int nType = 0;

        for (EvalObjRQ3 evalObjRQ3 : evalObjRQ3s) {
            if (statType == "recall") {
                if (kType <= 3) {
                    if (evalObjRQ3.numE == kType) {
                        nType++;
                    }
                } else {
                    if (evalObjRQ3.numE >= kType) {
                        nType++;
                    }
                }
            }

            if (statType == "precision") {
                assert (kType <= 3);
                if (evalObjRQ3.numP == kType) {
                    nType++;
                }
            }
        }

        return nType;
    }

    private int numHit(int minHit, int kType, String statType) {
        int nHit = 0;

        for (EvalObjRQ3 evalObjRQ3 : evalObjRQ3s) {
            if (evalObjRQ3.numOverlap > 3) {
                continue;
            }

            if (statType == "recall") {
                if (evalObjRQ3.numOverlap >= minHit && kType== evalObjRQ3.numE) {
                    nHit++;
                }
            }

            if (statType == "precision") {
                if (evalObjRQ3.numOverlap >= minHit && kType== evalObjRQ3.numP) {
                    nHit++;
                }
            }
        }

        return nHit;
    }
}
