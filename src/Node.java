import java.util.ArrayList;

class Node {
    private int val;
    private ArrayList<Node> originalChildren;
    private ArrayList<Node> modifiableChildren;
    private Node parent;

    public Node(){
        originalChildren = new ArrayList<>();
        modifiableChildren = new ArrayList<>();
    }


    public ArrayList<Node> getModifiableChildren(){
        return modifiableChildren;
    }


    public void setModifiableChildren(ArrayList<Node> children){
        modifiableChildren = new ArrayList<>();

        for(int i=0; i<children.size(); i++){
            modifiableChildren.add(children.get(i));
        }
    }


    public ArrayList<Node> getOriginalChildren(){
        return originalChildren;
    }





    //putting best (lowest value) child first
    public void modifyChildren(){
        int bestIndex = 0;
        for(int i=1; i<originalChildren.size(); i++){
            if(originalChildren.get(i).getVal() < originalChildren.get(bestIndex).getVal()){
                bestIndex = i;
            }
        }
        if(bestIndex != 0){
            Node best = modifiableChildren.get(bestIndex);
            modifiableChildren.remove(best);
            modifiableChildren.add(0, best);
        }
    }


    public void addChild(Node child){
        modifiableChildren.add(child);
        originalChildren.add(child);
    }


    public void setParent(Node parent){
        this.parent = parent;
    }


    public Node getParent(){
        return parent;
    }


    public int getVal(){
        return val;
    }


    public void setStaticEvalFunc(int T, boolean isLeaf, int num){
        if(isLeaf){
            val = T;
        }
        else {
            val = T + num;
        }

    }

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + val);
        for (int i = 0; i < modifiableChildren.size() - 1; i++) {
            modifiableChildren.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (modifiableChildren.size() > 0) {
            modifiableChildren.get(modifiableChildren.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
    }



}