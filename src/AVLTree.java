import java.util.*;

public class AVLTree implements Iterable<Integer> {
    // You may edit the following nested class:
    protected class Node {
        public Node left = null;
        public Node right = null;
        public Node parent = null;
        public int height = 0;
        public int value;
        protected int size = 1; //size of the subtree rooted in this node

        public Node(int val) {
            this.value = val;
        }

        protected int rank(int val) {
            //this function returns the number of nodes with smaller value than val
            if (value == val)
                return left != null ? left.size : 0;
            else if (value > val)//this node and the nodes in the right subtree are irrelevant
                return left != null ? left.rank(val) : 0;
            else {
                if (right != null)//count this and the left subtree, and go right
                    return right.rank(val) + (left != null ? left.size + 1 : 1);
                else
                    return left != null ? left.size + 1 : 1;
            }
        }

        //this function returns the valueof the node with (index) smaller/ equal values
        //we assume that 1 <= index <= tree.size
        protected int select(int index) {
            int rank = left != null ? left.size + 1 : 1;
            if (rank == index)
                return value;
            else if (index < rank)//this value is bigger than index, go left
                return left.select(index);
            else//this value is smaller than index, go right and reduce me and my left subtree
                return right.select(index - rank);
        }

        protected void updateSize() { //this function updates Node's size
            int leftSize = (left == null) ? 0 : left.size;
            int rightSize = (right == null) ? 0 : right.size;

            size = leftSize + rightSize + 1;
        }

        public void updateHeight() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            height = Math.max(leftHeight, rightHeight) + 1;
        }

        public int getBalanceFactor() {
            int leftHeight = (left == null) ? -1 : left.height;
            int rightHeight = (right == null) ? -1 : right.height;

            return leftHeight - rightHeight;
        }
    }

    protected Node root;

    //You may add fields here.
    protected ArrayDeque deq = new ArrayDeque();

    public AVLTree() {
        this.root = null;
    }

    /*
     * IMPORTANT: You may add code to both "insert" and "insertNode" functions.
     */
    public void insert(int value) {
        root = insertNode(root, value);
    }

    protected Node insertNode(Node node, int value) {
        // Perform regular BST insertion
        if (node == null) {
            Node insertedNode = new Node(value);
            Object[] toInsert0 = {0, insertedNode};//an insertion occurred
            deq.addLast(toInsert0);
            return insertedNode;
        }

        if (value < node.value) {
            node.left = insertNode(node.left, value);
            node.left.parent = node;
        } else {
            node.right = insertNode(node.right, value);
            node.right.parent = node;
        }

        node.updateHeight();
        node.updateSize();//update size of the nodes on the way to the inserted node

        /*
         * Check For Imbalance, and fix according to the AVL-Tree Definition
         * If (balance > 1) -> Left Cases, (balance < -1) -> Right cases
         */

        int balance = node.getBalanceFactor();

        if (balance > 1) {
            if (value > node.left.value) {
                node.left = rotateLeft(node.left);
                Object[] toInsert2 = {2, node.left};//a left rotation occurred
                deq.addLast(toInsert2);
            }

            node = rotateRight(node);
            Object[] toInsert1 = {1, node};//a right rotation occurred
            deq.addLast(toInsert1);
        } else if (balance < -1) {
            if (value < node.right.value) {
                node.right = rotateRight(node.right);
                Object[] toInsert1 = {1, node.right};//a right rotation occurred
                deq.addLast(toInsert1);
            }

            node = rotateLeft(node);
            Object[] toInsert2 = {2, node};//a left rotation occurred
            deq.addLast(toInsert2);
        }
        return node;
    }

    // You may add additional code to the next two functions.
    protected Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        //Update parents
        if (T2 != null) {
            T2.parent = y;
        }

        x.parent = y.parent;
        y.parent = x;

        y.updateHeight();
        x.updateHeight();
        //x and y are the nodes whose size has been changed in the rotation, update it
        y.updateSize();
        x.updateSize();


        // Return new root
        return x;
        //backtrack will use x to undo this rotation
    }

    protected Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        //Update parents
        if (T2 != null) {
            T2.parent = x;
        }

        y.parent = x.parent;
        x.parent = y;

        x.updateHeight();
        y.updateHeight();
        //x and y are the nodes whose size has been changed in the rotation, update it
        x.updateSize();
        y.updateSize();

        // Return new root
        return y;
        //backtrack will use y to undo this rotation
    }

    public void printTree() {
        TreePrinter.print(this.root);
    }

    /***
     * A Printer for the AVL-Tree. Helper Class for the method printTree().
     * Not relevant to the assignment.
     */
    private static class TreePrinter {
        private static void print(Node root) {
            if (root == null) {
                System.out.println("(XXXXXX)");
            } else {
                final int height = root.height + 1;
                final int halfValueWidth = 4;
                int elements = 1;

                List<Node> currentLevel = new ArrayList<Node>(1);
                List<Node> nextLevel = new ArrayList<Node>(2);
                currentLevel.add(root);

                // Iterating through the tree by level
                for (int i = 0; i < height; i++) {
                    String textBuffer = createSpaceBuffer(halfValueWidth * ((int) Math.pow(2, height - 1 - i) - 1));

                    // Print tree node elements
                    for (Node n : currentLevel) {
                        System.out.print(textBuffer);

                        if (n == null) {
                            System.out.print("        ");
                            nextLevel.add(null);
                            nextLevel.add(null);
                        } else {
                            System.out.printf("(%6d)", n.value);
                            nextLevel.add(n.left);
                            nextLevel.add(n.right);
                        }

                        System.out.print(textBuffer);
                    }

                    System.out.println();

                    if (i < height - 1) {
                        printNodeConnectors(currentLevel, textBuffer);
                    }

                    elements *= 2;
                    currentLevel = nextLevel;
                    nextLevel = new ArrayList<Node>(elements);
                }
            }
        }

        private static String createSpaceBuffer(int size) {
            char[] buff = new char[size];
            Arrays.fill(buff, ' ');

            return new String(buff);
        }

        private static void printNodeConnectors(List<Node> current, String textBuffer) {
            for (Node n : current) {
                System.out.print(textBuffer);
                if (n == null) {
                    System.out.print("        ");
                } else {
                    System.out.printf("%s      %s",
                            n.left == null ? " " : "/", n.right == null ? " " : "\\");
                }

                System.out.print(textBuffer);
            }

            System.out.println();
        }
    }

    /***
     * A base class for any Iterator over Binary-Search Tree.
     * Not relevant to the assignment, but may be interesting to read!
     * DO NOT WRITE CODE IN THE ITERATORS, THIS MAY FAIL THE AUTOMATIC TESTS!!!
     */
    private abstract class BaseBSTIterator implements Iterator<Integer> {
        private List<Integer> values;
        private int index;

        public BaseBSTIterator(Node root) {
            values = new ArrayList<>();
            addValues(root);

            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < values.size();
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return values.get(index++);
        }

        protected void addNode(Node node) {
            values.add(node.value);
        }

        abstract protected void addValues(Node node);
    }

    public class InorderIterator extends BaseBSTIterator {
        public InorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(Node node) {
            if (node != null) {
                addValues(node.left);
                addNode(node);
                addValues(node.right);
            }
        }

    }

    public class PreorderIterator extends BaseBSTIterator {

        public PreorderIterator(Node root) {
            super(root);
        }

        @Override
        protected void addValues(Node node) {
            if (node != null) {
                addNode(node);
                addValues(node.left);
                addValues(node.right);
            }
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return getInorderIterator();
    }

    public Iterator<Integer> getInorderIterator() {
        return new InorderIterator(this.root);
    }

    public Iterator<Integer> getPreorderIterator() {
        return new PreorderIterator(this.root);
    }
}
