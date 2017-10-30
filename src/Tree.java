import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Tree {


    private Node root;
    private int branchingFactor;
    private int height;
    private int approx;

    private Random randGen;


    public Tree(int b, int h , int approx){
        root = new Node();
        this.branchingFactor = b;
        this.height = h;
        this.approx = approx;
        randGen = new Random();
    }


    public Node getRoot(){
        return root;
    }


    public int getHeight(){
        return height;
    }


    public int generateBranchingFactor(){
        int actualBranchFactor;
        int randNum  = randGen.nextInt(20)+1;

        if(randNum <=18) {
            actualBranchFactor = branchingFactor;
        }
        else if(randNum == 19){
            actualBranchFactor = branchingFactor+1;
        }
        else {
            actualBranchFactor = branchingFactor-1;
        }
        return actualBranchFactor;
    }


    public int getDepthOfNode(Node node){
        Node curr = node;
        int depth = 0;
        while(curr.getParent() != null){
            depth++;
            curr = curr.getParent();
        }
        return depth;
    }



    public void generateNodeDaughters(Node parent, int b, int T){

        if(getDepthOfNode(parent) == height){
            parent.setStaticEvalFunc(T,true, 0);
        }
        else {
            int chosenNum = randGen.nextInt(b);
            for(int i=0; i<b; i++){
                Node node = new Node();
                parent.addChild(node);
                node.setParent(parent);

                int t;

                if(i==chosenNum){
                    t = -T;
                }
                else {
                    t  = randGen.nextInt((10001 - (-T)) + 1) + (-T);
                }
                int num  = randGen.nextInt((approx - (-approx)) + 1) + (-approx);
                node.setStaticEvalFunc(t,false, num);
                generateNodeDaughters(node, generateBranchingFactor(), t);
            }
        }
    }


    public void generateTree(){
        int T  = randGen.nextInt((2500 - (-2500)) + 1) + (-2500);
        int num  = randGen.nextInt((approx - (-approx)) + 1) + (-approx);
        root.setStaticEvalFunc(T, false, num);
        generateNodeDaughters(root, branchingFactor, T);
    }


    public void resetModifiedChildren(ArrayList<Node> nodes){
        for(int i=0; i<nodes.size(); i++){
            nodes.get(i).setModifiableChildren(nodes.get(i).getOriginalChildren());
        }
    }

//    protected class Node {
//        private int val;
//        private ArrayList<Node> originalChildren;
//        private ArrayList<Node> modifiableChildren;
//        private Node parent;
//
//        public Node(){
//            originalChildren = new ArrayList<>();
//            modifiableChildren = new ArrayList<>();
//        }
//
//
//        public ArrayList<Node> getModifiableChildren(){
//            return modifiableChildren;
//        }
//
//
//        public ArrayList<Node> getOriginalChildren(){
//            return originalChildren;
//        }
//
//
//        public void addChild(Node child){
//            modifiableChildren.add(child);
//            originalChildren.add(child);
//        }
//
//
//        public void setParent(Node parent){
//            this.parent = parent;
//        }
//
//
//        public Node getParent(){
//            return parent;
//        }
//
//
//        public int getVal(){
//            return val;
//        }
//
//
//        public void setStaticEvalFunc(int T, boolean isLeaf){
//            if(isLeaf){
//                val = T;
//            }
//            else {
//                int num  = randGen.nextInt((approx - (-approx)) + 1) + (-approx);
//                val = T + num;
//                System.out.println(val);
//            }
//
//        }
//
//        public void print() {
//            print("", true);
//        }
//
//        private void print(String prefix, boolean isTail) {
//            System.out.println(prefix + (isTail ? "└── " : "├── ") + val);
//            for (int i = 0; i < modifiableChildren.size() - 1; i++) {
//                modifiableChildren.get(i).print(prefix + (isTail ? "    " : "│   "), false);
//            }
//            if (modifiableChildren.size() > 0) {
//                modifiableChildren.get(modifiableChildren.size() - 1)
//                        .print(prefix + (isTail ?"    " : "│   "), true);
//            }
//        }
//
//
//
//    }

    public static void main(String[] args){
        Tree tree = new Tree(2, 3, 100);
        tree.generateTree();
        System.out.println("");
        tree.root.print();


    }



}