import java.util.ArrayList;
import java.util.List;

public class MinHeap<E extends Comparable<E>> {
    private List<E> myList;

    public MinHeap() {
        myList = new ArrayList<>();
    }

    public void add(E value) {
        myList.add(value);
        swapUp(myList.size() - 1);
    }

    private void swapUp(int index) {
        if (index == 0) return;
        int parentIndex = (index - 1) / 2;
        if (myList.get(index).compareTo(myList.get(parentIndex)) < 0) {
            E temp = myList.get(index);
            myList.set(index, myList.get(parentIndex));
            myList.set(parentIndex, temp);
            swapUp(parentIndex); 
        }
    }
    public E poll(){
        if (myList.isEmpty()) return null;
        E root = myList.get(0);
        myList.set(0, myList.get(myList.size() - 1));
        myList.remove(myList.size() - 1);
        swapDown(0);
        return root;
    }
    private void swapDown(int index){
        int leftIndex = 2 * index + 1;
        int rightIndex = 2 * index + 2;
        int smallestIndex = index;
        if (leftIndex < myList.size() && myList.get(leftIndex).compareTo(myList.get(smallestIndex)) < 0) {
            smallestIndex = leftIndex;
        }
        if (rightIndex < myList.size() && myList.get(rightIndex).compareTo(myList.get(smallestIndex)) < 0) {
            smallestIndex = rightIndex;
        }
        if (smallestIndex != index) {
            E temp = myList.get(index);
            myList.set(index, myList.get(smallestIndex));
            myList.set(smallestIndex, temp);
            swapDown(smallestIndex);
        }
    }
    public void remove(E x){
        int index = myList.indexOf(x);
        if (index == -1) return;
        myList.set(index, myList.get(myList.size() - 1));
        myList.remove(myList.size() - 1);
        swapDown(index);
    }
    public E peek(){
        return myList.get(0);
    }
    public boolean isEmpty(){
        return myList.isEmpty();
    }
    public int size(){
        return myList.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        int count = 0;
        for (int i = 0; i < myList.size(); i++) {
            sb.append(myList.get(i)).append(" ");
            count++;
            if (count == Math.pow(2, level)) {
                sb.append("\n");
                level++;
                count = 0;
            }
        }
        return sb.toString();
    }
}
