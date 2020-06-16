package wsm.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class PostingListOperation {

    /**
     * insert a docId into a posting list
     * @param postingList the posting list, TreeSet<Integer>, containing the merge result
     * @param docID the docId to insert
     */
    public static void insertDocIdToPostingList(TreeSet<Integer> postingList, Integer docID) {

        // create a new linked list if there is no existing list
        if (postingList == null || postingList.size() == 0) {
            postingList = new TreeSet<>();
        }
        // the actual insertion process
        postingList.add(docID);
    }

    /**
     * merge two posting lists (operation OR)
     * @param postingList1 the first posting list, containing the merge result
     * @param postingList2 the second posting list, will not change state
     */
    public static void opORPostingLists(TreeSet<Integer> postingList1,
                                            TreeSet<Integer> postingList2) {

        // create a new postingList1 if its a null
        if (postingList1 == null){
            postingList1 = new TreeSet<>();
        }
        // if postingList2 has no elements, then postingList1 does not change state
        if (postingList2 == null || postingList2.size() == 0) {
            return;
        }
        postingList1.addAll(postingList2);

    }

    /**
     * intersect two posting lists (operation AND)
     * @param postingList1 the first posting list, containing the merge result
     * @param postingList2 the second posting list, will not change state
     */
    public static void opANDPostingLists(TreeSet<Integer> postingList1,
                                           TreeSet<Integer> postingList2) {

        // create a new postingList1 if its a null
        if (postingList1 == null){
            postingList1 = new TreeSet<>();
        }
        // if postingList2 has no elements, then postingList1 does not change state
        if (postingList2 == null || postingList2.size() == 0) {
            postingList1.clear();
            return;
        }
        postingList1.retainAll(postingList2);

    }

    /**
     * intersect two posting lists (operation SUB)
     * @param postingList1 the first posting list, containing the subtraction result
     * @param postingList2 the second posting list, will not change state
     */
    public static void opSUBPostingLists(TreeSet<Integer> postingList1,
                                         TreeSet<Integer> postingList2) {

        // create a new postingList1 if its a null
        if (postingList1 == null){
            postingList1 = new TreeSet<>();
        }
        // if postingList2 has no elements, then postingList1 does not change state
        if (postingList2 == null || postingList2.size() == 0) {
            return;
        }
        postingList1.removeAll(postingList2);

    }

    /**
     * compute symmetry difference between two posting lists (operation SYMDIF)
     * @param postingList1 the first posting list, containing the symmetric difference result
     * @param postingList2 the second posting list, will not change state
     */
    public static void opSYMDIFPostingLists(TreeSet<Integer> postingList1,
                                            TreeSet<Integer> postingList2) {

        // create a new postingList1 if its a null
        if (postingList1 == null){
            postingList1 = new TreeSet<>();
        }
        // if postingList2 has no elements, then postingList1 does not change state
        if (postingList2 == null || postingList2.size() == 0) {
            return;
        }
        @SuppressWarnings("unchecked")
        TreeSet<Integer> tmp = (TreeSet<Integer>) postingList2.clone();
        // symmetric difference
        tmp.removeAll(postingList1);
        postingList1.removeAll(postingList2);
        postingList1.addAll(tmp);

    }

    public static void main(String[] args) {
        TreeSet<Integer> postingList1 = new TreeSet<>();
        TreeSet<Integer> postingList2 = new TreeSet<>();
        for (int i = 0; i < 30; i++) {
            if (i % 2 == 0){
                postingList1.add(i);
            }
            if (i % 3 == 0) {
                postingList2.add(i);
            }
        }

        // before operations
        System.out.println("Before operations: ");
        System.out.println(postingList1);
        System.out.println(postingList2);

        // PostingListOperation.opANDPostingLists(postingList1, postingList2);
        // PostingListOperation.opORPostingLists(postingList1, postingList2);
        // PostingListOperation.opSUBPostingLists(postingList1, postingList2);
        PostingListOperation.opSYMDIFPostingLists(postingList1, postingList2);

        // after operations
        System.out.println("After operations: ");
        System.out.println(postingList1);

    }

}
