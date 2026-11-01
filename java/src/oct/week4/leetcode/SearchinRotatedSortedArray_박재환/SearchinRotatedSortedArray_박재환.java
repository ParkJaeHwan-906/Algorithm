package oct.week4.leetcode.SearchinRotatedSortedArray_박재환;

public class SearchinRotatedSortedArray_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0)); // 4
        System.out.println(solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3)); // -1
        System.out.println(solution.search(new int[]{1}, 0)); // -1
    }
}

class Solution {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            if(nums[mid] == target)
                return mid;

            if(nums[l] <= nums[mid]) {      // 왼쪽 구간 오름차순 정렬
                if(nums[l] <= target && target <= nums[mid])
                    r = mid - 1;
                else
                    l = mid + 1;
            } else if(nums[mid] <= nums[r]) {       // 오른쪽 구간 오름차순 정렬
                if(nums[mid] < target && target <= nums[r])
                    l = mid + 1;
                else
                    r = mid - 1;
            }
        }
        return -1;
    }
}
