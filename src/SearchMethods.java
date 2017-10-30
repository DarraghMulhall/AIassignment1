
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchMethods {

    private static int evalCount;
    private static ArrayList<Node> globalPV;


//    private static Node getBestDaughter(Node node){
////        Node best = node.getOriginalChildren().get(0);
////        for(int i=1; i<node.getOriginalChildren().size(); i++){
////            if(best.getVal())
////        }
//    }

    private static ArrayList<Node> makePV(ArrayList<Node> childPV, Node child){
        ArrayList<Node> parentsPV = new ArrayList<>();
        parentsPV.add(child);
        for(int i=0; i<childPV.size(); i++){
            parentsPV.add(childPV.get(i));
        }
        return parentsPV;
    }


    public static ScoreAndPV alphaBeta(Node node, int height, ScoreAndPV achievable, ScoreAndPV hope, boolean useModifiableChildren) {
        ArrayList<Node> children;
        if (useModifiableChildren) {
            children = node.getModifiableChildren();
        } else {
            children = node.getOriginalChildren();
        }
        ArrayList<Node> pv;

        if (height == 0 || children.isEmpty()) {
           // System.out.println(node.getVal());
            evalCount++;
            return new ScoreAndPV(node.getVal(), new ArrayList<Node>());
        } else {
            ScoreAndPV temp;

            for (int i = 0; i < children.size(); i++) {
                Node newNode = children.get(i);
                if(!globalPV.contains(newNode)){
                    useModifiableChildren = false;
                }
                temp = ScoreAndPV.getPVandNegatedScore(alphaBeta(newNode, height - 1,  ScoreAndPV.getPVandNegatedScore(hope), ScoreAndPV.getPVandNegatedScore(achievable), useModifiableChildren));
                if (temp.getScore() >= hope.getScore()) {
                    pv = makePV(temp.getPv(), newNode);
                    return new ScoreAndPV(temp.getScore(), pv);
                }
                if(temp.getScore() > achievable.getScore()){
                    achievable = temp;
                }
            }
            int minIndex = 0;
            for (int i = 1; i < children.size(); i++) {

                if(children.get(i).getVal() < children.get(minIndex).getVal()){
                    minIndex = i;
                }
            }
            pv = makePV(achievable.getPv(), children.get(minIndex));
        }
            //System.out.println(achievable.getScore());
            return new ScoreAndPV(achievable.getScore(), pv);
    }



    public static ScoreAndPV alphaBetaIterative(Tree tree, boolean reOrdering){
        globalPV = new ArrayList<>();
        evalCount = 0;
        int depth = 1;
        ScoreAndPV score = new ScoreAndPV(0);
        int alpha = -10000;
        int beta = 10000;
        while(depth<=tree.getHeight()){
            score = alphaBeta(tree.getRoot(), depth, new ScoreAndPV(alpha), new ScoreAndPV(beta), reOrdering);
            globalPV = score.getPv();
            depth++;

            //modify children with best first
            Node lastInPV = globalPV.get(globalPV.size()-1);
            //System.out.println(lastInPV.getVal() +" "+lastInPV.getOriginalChildren().get(0).getVal());
            lastInPV.getParent().modifyChildren();
            //System.out.println(lastInPV.getVal() +" "+lastInPV.getModifiableChildren().get(0).getVal());
        }
        ArrayList<Node> parents = new ArrayList<>();
        //creating list of nodes whose children need to be reset to original
        for(int i=0; i<globalPV.size(); i++){
            parents.add(globalPV.get(i).getParent());
        }
        //tree.getRoot().print();
        tree.resetModifiedChildren(parents);
        return score;

    }


    public static ScoreAndPV pvs(Node node, int alpha, int beta, int depth, boolean useModifiableChildren){
        ScoreAndPV score;

        ArrayList<Node> children;
        if (useModifiableChildren) {
            children = node.getModifiableChildren();
        } else {
            children = node.getOriginalChildren();
        }

        if(depth == 0 || children.isEmpty()){
            evalCount++;
            return new ScoreAndPV(node.getVal(), new ArrayList<Node>());
        }
        else {
            Node newNode = children.get(0);
            if(!globalPV.contains(newNode)){
                useModifiableChildren = false;
            }
            score = ScoreAndPV.getPVandNegatedScore(pvs(newNode, -beta, -alpha, depth-1, useModifiableChildren));
            if(score.getScore() < beta){
                ScoreAndPV temp;
                for(int i=1; i<children.size(); i++) {
                    int lb = Math.max(alpha, score.getScore());
                    int ub = lb+1;
                    temp = ScoreAndPV.getPVandNegatedScore(pvs(children.get(i), -ub, -lb, depth-1, useModifiableChildren));
                    if(temp.getScore() >= ub && temp.getScore() < beta){
                        temp = ScoreAndPV.getPVandNegatedScore(pvs(children.get(i), -beta, -temp.getScore(), depth-1, useModifiableChildren));
                    }
                    score.setScore(Math.max(score.getScore(), temp.getScore()));
                    if(temp.getScore() >= beta){
                        break;
                    }
                }

            }

        }
        int minIndex = 0;
        for (int i = 1; i < children.size(); i++) {

            if(children.get(i).getVal() < children.get(minIndex).getVal()){
                minIndex = i;
            }
        }
        ArrayList<Node> pv = makePV(score.getPv(), children.get(minIndex));
        return new ScoreAndPV(score.getScore(), pv);
    }


    public static ScoreAndPV pvsIterative(Tree tree, boolean reOrdering){
        globalPV = new ArrayList<>();
        evalCount = 0;
        int depth = 1;
        ScoreAndPV score = new ScoreAndPV(0);
        int alpha = -10000;
        int beta = 10000;
        while(depth<=tree.getHeight()){
            score = pvs(tree.getRoot(), alpha, beta, depth, reOrdering);
            globalPV = score.getPv();
            depth++;
            //modify children with best first
            Node lastInPV = globalPV.get(globalPV.size()-1);
            //System.out.println(lastInPV.getVal() +" "+lastInPV.getOriginalChildren().get(0).getVal());
            lastInPV.getParent().modifyChildren();
            //System.out.println(lastInPV.getVal() +" "+lastInPV.getModifiableChildren().get(0).getVal());
        }
        ArrayList<Node> parents = new ArrayList<>();
        //creating list of nodes whose children need to be reset to original
        for(int i=0; i<globalPV.size(); i++){
            parents.add(globalPV.get(i).getParent());
        }
        tree.resetModifiedChildren(parents);
        return score;
    }






    public static void main(String[] args) {

        BufferedWriter out = null;

        FileWriter fstream = null; //true tells to append data.
        try {
            fstream = new FileWriter("AIresults.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out = new BufferedWriter(fstream);
        try {
            out.write("HEIGHT,BRANCHING-FACTOR,APPROX,ALPHABETA(NO-REORDERING),ALPHABETA(REORDERING),PVS(NO-REORDERING),PVS(REORDERING)");
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }




        evalCount = 0;

        ScoreAndPV spv;
        Tree tree;
        for(int height = 4; height <=6; height++){
            for(int b=3; b<=15; b+=3){
                for(int approx = 0; approx <= 300; approx+=50){
                    System.out.println("[Height: "+height + " branching factor: "+b + " approx: "+approx+"]");
                    int evalCount1 = 0, evalCount2 = 0, evalCount3 = 0, evalCount4 = 0;
                    for(int i=0; i<25; i++) {
                        tree = new Tree(b, height, approx);
                        tree.generateTree();
                        spv = alphaBetaIterative(tree, false);
                        evalCount1 += evalCount;
                        spv = alphaBetaIterative(tree, true);
                        evalCount2 += evalCount;
                        spv = pvsIterative(tree, false);
                        evalCount3 += evalCount;
                        spv = pvsIterative(tree, true);
                        evalCount4 += evalCount;
                    }
                    evalCount1 = evalCount1/25;
                    evalCount2 = evalCount2/25;
                    evalCount3 = evalCount3/25;
                    evalCount4 = evalCount4/25;


                    //spv = alphaBetaIterative(tree, false);
                    try {
                        out.write(height+","+b+","+approx +","+evalCount1+","+evalCount2+","+evalCount3+","+evalCount4);
                        out.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("alphaBeta - no reordering = "+evalCount1+"\n");
                    //spv = alphaBetaIterative(tree, true);
                   // System.out.println("alphaBeta - reordering = "+evalCount2+"\n");
                   // spv = pvsIterative(tree, false);
                   // System.out.println("PVS - no reordering = "+evalCount3+"\n");
                    //spv = pvsIterative(tree, true);
                   // System.out.println("PVS - reordering = "+evalCount4+"\n\n");
                }
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

