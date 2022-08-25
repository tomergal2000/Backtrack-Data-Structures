public class tesTomer {
    public static void main(String[] args) {
        int[] nums = {39, 55, 140, 56, 78, 5, 2, 98, 160, 59};
        BacktrackingAVL test = create(nums);
        test.printTree();
        test.Backtrack();
        test.printTree();
        test.Backtrack();
        test.printTree();
        test.Backtrack();
        test.printTree();
        test.Backtrack();
        test.printTree();
        test.Backtrack();
        test.printTree();

        for (int i = 1; i<6; i++){
            System.out.println(test.Select(i));
        }


    }

    public static BacktrackingAVL create(int[] nums) {
        BacktrackingAVL tes = new BacktrackingAVL();
        for (int i = 0; i < nums.length; i++) {
            tes.insert(nums[i]);
        }
        return tes;
    }
}
