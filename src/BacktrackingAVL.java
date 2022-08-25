import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

public class BacktrackingAVL extends AVLTree {
    public BacktrackingAVL() {
        super();
    }

    public void Backtrack() {
        if (!deq.isEmpty()) {
            Object[] curr = (Object[]) deq.pollLast();
            int action = (int) curr[0];
            //actions that occurred: (0) = insertion, (1) = right rotation, (2) = left rotation
            while (action != 0) {//the current backtracking ends after deleting the inserted node
                Node toRotate = (Node) curr[1];
                Node par = toRotate.parent;
                if (par != null) {//node is not a root
                    int side = toRotate.value - toRotate.parent.value;
                    //right rotation occurred, now rotate left
                    if (action == 1) {
                        if (side > 0)//node is the right child of par
                            par.right = rotateLeft(toRotate);
                        else//node is the left child of par
                            par.left = rotateLeft(toRotate);
                    //left rotation occurred, now rotate right
                    } else {
                        if (side > 0)//node is the right child of par
                            par.right = rotateRight(toRotate);
                        else//node is the left child of par
                            par.left = rotateRight(toRotate);
                    }
                } else {//node is a root
                    //right rotation occurred, now rotate left
                    if (action == 1)
                        root = rotateLeft(toRotate);
                    //left rotation occurred, now rotate right
                    else
                        root = rotateRight(toRotate);
                }
                curr = (Object[]) deq.pollLast();//move to the next action
                action = (int) curr[0];
            }
            //the actual deletion of the inserted node
            Node toDelete = (Node) curr[1];
            if (toDelete.parent != null) {
                Node par = toDelete.parent;
                int side = toDelete.value - par.value;
                if (side > 0)//node is the right child of par
                    par.right = null;
                else//node is the left child of par
                    par.left = null;
                while (par != null) {//update size and height of the nodes on the way to the root
                    par.updateHeight();
                    par.updateSize();
                    par = par.parent;
                }
            } else//node is the root
                root = null;
        }
    }

    public static List<Integer> AVLTreeBacktrackingCounterExample() {
        List<Integer> answer = new LinkedList<>();
        answer.add(1);
        answer.add(2);
        answer.add(3);
        return answer;
    }

    public int Select(int index) {
        return root.select(index);
    }

    public int Rank(int value) {
        return root.rank(value);
    }
}
