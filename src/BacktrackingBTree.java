import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

public class BacktrackingBTree<T extends Comparable<T>> extends BTree<T> {
    // For clarity only, this is the default ctor created implicitly.
    public BacktrackingBTree() {
        super();
    }

    public BacktrackingBTree(int order) {
        super(order);
    }

    public void Backtrack() {
        if (!deq.isEmpty()) { //there are actions to backtrack
            deq.pollLast();
            Object[] curr = null;
            //copy relevant nodes in deque to a separate deq
            ArrayDeque<Object[]> actionDeq = new ArrayDeque<>();
            while (!deq.isEmpty() && !(deq.peekLast() instanceof String))
                actionDeq.addLast((Object[]) deq.pollLast());
            while (!actionDeq.isEmpty()) {
                curr = actionDeq.pollLast();
                if (curr[0] == null)        //means the tree was empty before this action (insertion)
                    root = null;
                else if (curr[1] == null)   //means curr[0] holds a split node
                    reMerge((Node<T>) curr[0]);
                else                        //means curr[1] holds the last inserted value
                    ((Node<T>) curr[0]).removeKey((T) curr[1]);
            }
            --size;
        }
    }
    //this function returns the original node (before the split) to the tree
    private void reMerge(Node<T> node) {
        int medianIndex = node.numOfKeys / 2;
        T median = node.getKey(medianIndex);
        Node<T> myParent = node.parent;
        //if node was not the root, delete Median from Parent(1), delete redundant children(2) and re-add Node as Parent's child(3)
        if ((myParent.numOfKeys != 1)) {
            int i = myParent.indexOf(median);
            if (i != -1) { //if i=-1 it means that the median was not found in parent- that happens when node's parent
                //was a split node
                myParent.removeKey(median); //(1)
                myParent.removeChild(i);
                myParent.removeChild(i); //(2)
                myParent.addChild(node); //(3)
            }
        }

        //if node was the root, make it root again
        else
            root = node;

        //remind Node's children who their parent is
        for (int i = 0; i < node.numOfChildren; i++)
            node.children[i].parent = node;
    }

    public static List<Integer> BTreeBacktrackingCounterExample() {
        List<Integer> answer = new LinkedList<>();
        answer.add(1);
        answer.add(2);
        answer.add(3);
        answer.add(4);
        answer.add(5);
        answer.add(6);
        return answer;
    }
}
