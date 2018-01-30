import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Sophia Handel
 * @userid shandel3
 * @GTID 903201992
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data cannot be null.");
        }
        for (T i : data) {
            if (i == null) {
                throw new IllegalArgumentException("The data contains "
                        + " null element(s).");
            }
            this.add(i);
        }
    }

    @Override
    public void add(T data) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        } else if (size == 0) {
            root = new AVLNode<T>(data);
            size++;
        } else {
            addHelper(data, root);
        }
    }

    /**
     * Helper method for add using recursion
     *
     * @param data data to be added to new node
     * @param curr current node
     * @return the current added node
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            AVLNode<T> add = new AVLNode<>(data);
            size++;
            return add;
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(addHelper(data, curr.getLeft()));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(addHelper(data, curr.getRight()));
        } else {
            return curr;
        }
        update(curr);
        int bf = curr.getBalanceFactor();
        if (bf <= -2) {
            if (curr.getRight().getBalanceFactor() < 0) {
                return rotateLeft(curr);
            } else {
                curr.setRight(rotateRight(curr.getRight()));
                return rotateLeft(curr);
            }
        }
        if (bf >= 2) {
            if (curr.getLeft().getBalanceFactor() > 0) {
                return rotateRight(curr);
            } else {
                curr.setLeft(rotateLeft(curr.getLeft()));
                return rotateRight(curr);
            }
        }
        return curr;
    }



    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        } else if (root == null) {
            throw new NoSuchElementException("The tree is empty.");
        }
        AVLNode<T> dumdum = new AVLNode<T>(null);
        removeHelper(dumdum, root, data);
        size--;
        return dumdum.getData();
    }

    /**
     * Remove helper method
     * @throws IllegalArgumentException if data is Null
     * @throws java.util.NoSuchElementException if element is not in tree
     * @param data data to be removed
     * @param dumdum dummy node
     * @param curr current node
     * @return AVVLNode that is being removed
     */
    private AVLNode<T> removeHelper(AVLNode<T> dumdum,
                                    AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("Data "
                    + " not found.");
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(removeHelper(dumdum, curr.getLeft(), data));
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(removeHelper(dumdum, curr.getRight(), data));
        } else if (curr.getData().equals(data)) {
            dumdum.setData(curr.getData());
            if (curr.getLeft() == null && curr.getRight() == null) {
                curr = null;
            } else if (curr.getLeft() != null && curr.getRight() == null) {
                AVLNode<T> temp = curr.getLeft();
                curr = temp;
            } else if (curr.getLeft() == null && curr.getRight() != null) {
                AVLNode<T> temp = curr.getRight();
                curr = temp;
            } else if (curr.getLeft() != null && curr.getRight() != null) {
                AVLNode<T> dumdum1 = new AVLNode<T>(null);
                curr.setLeft(predHelp(curr, dumdum1));
                curr.setData(dumdum1.getData());
                update(curr);
            }
            return curr;
        }
        update(curr);
        int bf = curr.getBalanceFactor();
        if (bf <= -2) {
            if (curr.getRight().getBalanceFactor() < 0) {
                return rotateLeft(curr);
            } else {
                curr.setRight(rotateRight(curr.getRight()));
                return rotateLeft(curr);
            }
        }
        if (bf >= 2) {
            if (curr.getLeft().getBalanceFactor() > 0) {
                return rotateRight(curr);
            } else {
                curr.setLeft(rotateLeft(curr.getLeft()));
                return rotateRight(curr);
            }
        }
        return curr;
    }


    /**
     * Remove helper method that find the predecessor of the node to be removed
     * @param dumdum1 dummy node
     * @param curr current node
     * @return AVLNode the predecessor
     */
    private AVLNode<T> predHelp(AVLNode<T> curr, AVLNode<T> dumdum1) {
        if (curr.getRight() != null) {
            curr.setRight(predHelp(curr.getRight(), dumdum1));
            return curr;
        }
        dumdum1.setData(curr.getData());
        return curr.getLeft();
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null.");
        }
        AVLNode<T> dumdum = new AVLNode<>(null);
        getHelper(dumdum, root, data);
        return dumdum.getData();
    }

    /**
     * Get helper method
     * @param dumdum dummy node
     * @param curr current node
     * @param data data being searched for
     * @return AVLNode the found data
     */
    private AVLNode<T> getHelper(AVLNode<T> dumdum, AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("Data not found.");
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(getHelper(dumdum, curr.getLeft(), data));
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(getHelper(dumdum, curr.getRight(), data));
        } else if (curr.getData().equals(data)) {
            dumdum.setData(curr.getData());
        }
        return curr;
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        return containsHelper(root, data);
    }

    /**
     *
     * Helper method for contains
     *
     * @param curr current node
     * @param data data to found
     * @return true if contains data, false otherwise
     * */
    private boolean containsHelper(AVLNode<T> curr, T data) {
        if (curr == null) {
            return false;
        } else if (curr.getData().equals(data)) {
            return true;
        } else if (curr.getData().compareTo(data) > 0) {
            return containsHelper(curr.getLeft(), data);
        } else {
            return containsHelper(curr.getRight(), data);
        }
    }

    @Override
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    @Override
    public List<T> preorder() {
        List<T> fin = new ArrayList<>();
        preorderHelper(root, fin);
        return fin;
    }

    /**
     * Helper for preorder
     * @param curr current node
     * @param order list so far
     */
    private void preorderHelper(AVLNode<T> curr, List<T> order) {
        if (curr != null) {
            order.add(curr.getData());
            preorderHelper(curr.getLeft(), order);
            preorderHelper(curr.getRight(), order);
        }
    }

    @Override
    public List<T> postorder() {
        List<T> fin = new ArrayList<>();
        postorderHelper(root, fin);
        return fin;
    }

    /**
     * Helper for postorder
     * @param curr current node
     * @param order list so far
     */
    private void postorderHelper(AVLNode<T> curr, List<T> order) {
        if (curr != null) {
            postorderHelper(curr.getLeft(), order);
            postorderHelper(curr.getRight(), order);
            order.add(curr.getData());
        }
    }

    @Override
    public List<T> inorder() {
        List<T> list = new LinkedList<T>();
        if (root == null) {
            return list;
        }
        list.addAll(inorderHelper(root));
        return list;
    }

    /**
     * Returns list of data from nodes in order so far
     * @param curr current node
     * @return list of items in order
     */
    public List<T> inorderHelper(AVLNode<T> curr) {
        List<T> list = new LinkedList<T>();
        if (curr.getLeft() != null) {
            list.addAll(inorderHelper(curr.getLeft()));
        }
        list.add(curr.getData());
        if (curr.getRight() != null) {
            list.addAll(inorderHelper(curr.getRight()));
        }
        return list;
    }

    @Override
    public List<T> levelorder() {
        List<T> fin = new ArrayList<>();
        LinkedList<AVLNode<T>> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            AVLNode<T> curr = q.removeFirst();
            if (curr != null) {
                fin.add(curr.getData());
                q.add(curr.getLeft());
                q.add(curr.getRight());
            }
        }
        return fin;
    }

    @Override
    public List<T> listLeavesDescending() {
        ArrayList<T> l = new ArrayList<>();
        if (size == 0) {
            return l;
        }
        listLeavesDescendingHelper(root, l);
        return l;
    }

    /**
     * Creates a list of all leaf nodes present in the tree in
     * descending order.
     * @param curr the node being inspected
     * @param list the list to return
     */
    private void listLeavesDescendingHelper(AVLNode<T> curr, List<T> list) {
        if (curr.getRight() != null) {
            listLeavesDescendingHelper(curr.getRight(), list);
        }
        if (curr.getLeft() != null) {
            listLeavesDescendingHelper(curr.getLeft(), list);
        } else {
            list.add(curr.getData());
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int height() {
        if (root == null) {
            return -1;
        }
        return heightHelper(root);
    }

    /**
     * Helper for height method
     * @param curr current node
     * @return height so far
     */
    public int heightHelper(AVLNode<T> curr) {
        if (curr == null) {
            return -1;
        } else {
            return curr.getHeight();
        }
    }

    @Override
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
    /**
     * Helper method that rotates the Avl tree right
     * @param node1 the unbalanced node
     * @return AVLNode the new root
     */
    private AVLNode<T> rotateRight(AVLNode<T> node1) {
        AVLNode<T> node2 = node1.getLeft();
        node1.setLeft(node2.getRight());
        node2.setRight(node1);
        update(node1);
        update(node2);
        if (node1.equals(root)) {
            root = node2;
        }
        return node2;
    }

    /**
     * Helper method that rotates the AVL tree left
     * @param node1 the unbalanced node
     * @return AVLNode the new root
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node1) {
        AVLNode<T> node2 = node1.getRight();
        node1.setRight(node2.getLeft());
        node2.setLeft(node1);
        update(node1);
        update(node2);
        if (node1.equals(root)) {
            root = node2;
        }
        return node2;
    }

    /**
     * Helper method that updates height and balance factor for nodes
     * after rotation
     * @param nodey node to be updated
     */
    private void update(AVLNode<T> nodey) {
        nodey.setHeight(Math.max(heightHelper(nodey.getLeft()),
                heightHelper(nodey.getRight())) + 1);
        nodey.setBalanceFactor(heightHelper(nodey.getLeft())
                - heightHelper(nodey.getRight()));
    }
}
