INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1, 'A+B', '# A+B Problem

## 题目描述

计算两个整数 A 和 B 的和。这是最经典的入门题目。

## 输入格式

输入两个整数 A 和 B，用空格分隔 (-10^9 ≤ A, B ≤ 10^9)

## 输出格式

输出一个整数，表示 A + B 的结果。

## 示例 1

**输入：**
```
1 2
```

**输出：**
```
3
```

## 示例 2

**输入：**
```
-5 10
```

**输出：**
```
5
```

## 示例 3

**输入：**
```
0 0
```

**输出：**
```
0
```', '["简单"]', 0, 0, '{"timeLimit":10000,"memoryLimit":128000,"stackLimit":128000}', '[{"input":"1 2","output":"3"},{"input":"-5 10","output":"5"},{"input":"0 0","output":"0"},{"input":"100 200","output":"300"}]', 'def answer():
    return []', 0, 1903672737810321410, '2025-03-26 22:38:15', '2026-01-25 14:14:54', 1, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906, '三数之和', '# 三数之和

## 题目描述

给你一个整数数组 `nums`，判断是否存在三个元素 a、b、c，使得 a + b + c = 0。请找出所有和为 0 且不重复的三元组。

注意：答案中不可以包含重复的三元组。

## 输入格式

第一行输入一个整数 n，表示数组长度 (3 ≤ n ≤ 3000)
第二行输入 n 个整数，用空格分隔 (-10^5 ≤ nums[i] ≤ 10^5)

## 输出格式

输出所有和为 0 的三元组，每个三元组占一行，元素按升序排列，三元组之间也按字典序排列。如果不存在这样的三元组，输出空行。

## 示例 1

**输入：**
```
6
-1 0 1 2 -1 -4
```

**输出：**
```
-1 -1 2
-1 0 1
```

## 示例 2

**输入：**
```
3
0 0 0
```

**输出：**
```
0 0 0
```

## 示例 3

**输入：**
```
3
1 2 3
```

**输出：**
```

```

## 提示

- 使用排序 + 双指针解决
- 注意去重处理', '["数组","双指针","中等"]', 0, 0, '{"timeLimit":4000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"6\\n-1 0 1 2 -1 -4","output":"-1 -1 2\\n-1 0 1"},{"input":"3\\n0 0 0","output":"0 0 0"},{"input":"3\\n1 2 3","output":""}]', 'def fourSum(nums: list[int], target: int) -> list[list[int]]:
    nums.sort()
    res = []
    for i in range(len(nums)-3):
        if i > 0 and nums[i] == nums[i-1]:
            continue
        for j in range(i+1, len(nums)-2):
            if j > i+1 and nums[j] == nums[j-1]:
                continue
            l, r = j+1, len(nums)-1
            while l < r:
                s = nums[i] + nums[j] + nums[l] + nums[r]
                if s < target:
                    l += 1
                elif s > target:
                    r -= 1
                else:
                    res.append([nums[i], nums[j], nums[l], nums[r]])
                    while l < r and nums[l] == nums[l+1]:
                        l += 1
                    while l < r and nums[r] == nums[r-1]:
                        r -= 1
                    l += 1
                    r -= 1
    return res', 0, 1903672737810321410, '2025-03-31 21:48:56', '2026-01-25 14:15:37', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1907, 'C + D', '# C + D Problem

## 题目描述

计算两个整数 C 和 D 的和。

## 输入格式

输入两个整数 C 和 D，用空格分隔 (-10^9 ≤ C, D ≤ 10^9)

## 输出格式

输出一个整数，表示 C + D 的结果。

## 示例 1

**输入：**
```
3 4
```

**输出：**
```
7
```

## 示例 2

**输入：**
```
-10 20
```

**输出：**
```
10
```

## 示例 3

**输入：**
```
999 1
```

**输出：**
```
1000
```', '["栈","简单"]', 0, 0, '{"timeLimit":10000,"memoryLimit":128000,"stackLimit":128000}', '[{"input":"3 4","output":"7"},{"input":"-10 20","output":"10"},{"input":"999 1","output":"1000"},{"input":"0 0","output":"0"}]', 'HAHAHA', 0, 1903672737810321410, '2025-03-27 15:20:23', '2026-01-25 14:18:23', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1908, '两数之和', '# 两数之和

## 题目描述

给定一个整数数组 `nums` 和一个整数目标值 `target`，请你在该数组中找出 **和为目标值** `target` 的那 **两个** 整数，并返回它们的数组下标。

你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。

你可以按任意顺序返回答案。

## 输入格式

第一行输入整数 n 和 target，用空格分隔
第二行输入 n 个整数，用空格分隔

## 输出格式

输出两个整数（下标），用空格分隔，表示和为 target 的两个数的下标（从0开始）

## 示例 1

**输入：**
```
4 9
2 7 11 15
```

**输出：**
```
0 1
```

**解释：** nums[0] + nums[1] = 2 + 7 = 9

## 示例 2

**输入：**
```
3 6
3 2 4
```

**输出：**
```
1 2
```

**解释：** nums[1] + nums[2] = 2 + 4 = 6

## 示例 3

**输入：**
```
2 6
3 3
```

**输出：**
```
0 1
```

## 提示

- 使用哈希表可以在 O(n) 时间复杂度内解决', '["数组","哈希表","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"4 9\\n2 7 11 15","output":"0 1"},{"input":"3 6\\n3 2 4","output":"1 2"},{"input":"2 6\\n3 3","output":"0 1"}]', 'def twoSum(nums: list[int], target: int) -> list[int]:
    hashmap = {}
    for i, num in enumerate(nums):
        complement = target - num
        if complement in hashmap:
            return [hashmap[complement], i]
        hashmap[num] = i
    return [] # 理论上根据题目描述不会执行到这里 实际上我也不知道', 0, 1903672737810321410, '2025-03-31 21:30:50', '2026-01-25 14:18:38', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906715718448513025, '合并两个有序链表', '# 合并两个有序链表

## 题目描述

将两个升序链表合并为一个新的**升序**链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。

## 输入格式

第一行输入整数 n，表示第一个链表的长度
第二行输入 n 个整数，表示第一个链表的节点值（升序）
第三行输入整数 m，表示第二个链表的长度
第四行输入 m 个整数，表示第二个链表的节点值（升序）

## 输出格式

输出合并后的链表，节点值用空格分隔。

## 示例 1

**输入：**
```
3
1 2 4
3
1 3 4
```

**输出：**
```
1 1 2 3 4 4
```

## 示例 2

**输入：**
```
0

0

```

**输出：**
```

```

## 示例 3

**输入：**
```
0

1
0
```

**输出：**
```
0
```

## 提示

- 可以使用递归或迭代
- 时间复杂度 O(n + m)', '["链表","递归","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"[1,2,4]\\n[1,3,4]","output":"[1,1,2,3,4,4]"},{"input":"[]\\n[]","output":"[]"}]', 'class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def mergeTwoLists(l1: ListNode, l2: ListNode) -> ListNode:
    dummy = ListNode()
    curr = dummy
    while l1 and l2:
        if l1.val < l2.val:
            curr.next = l1
            l1 = l1.next
        else:
            curr.next = l2
            l2 = l2.next
        curr = curr.next
    curr.next = l1 or l2
    return dummy.next', 0, 1903672737810321410, '2025-03-31 22:30:27', '2026-01-25 14:58:16', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906715785817423874, '最长回文子串', '# 最长回文子串

## 题目描述

给你一个字符串 `s`，找到 `s` 中最长的回文子串。

**回文串**是正着读和反着读都一样的字符串。

## 输入格式

输入一个字符串 s (1 ≤ s.length ≤ 1000)

## 输出格式

输出最长的回文子串。如果有多个答案，返回任意一个。

## 示例 1

**输入：**
```
babad
```

**输出：**
```
bab
```

**解释：** "aba" 也是有效答案

## 示例 2

**输入：**
```
cbbd
```

**输出：**
```
bb
```

## 示例 3

**输入：**
```
a
```

**输出：**
```
a
```

## 示例 4

**输入：**
```
ac
```

**输出：**
```
a
```

## 提示

- 方法1：中心扩展法 O(n²)
- 方法2：动态规划 O(n²)
- 方法3：Manacher算法 O(n)
- 注意处理奇数和偶数长度的回文串', '["字符串","动态规划","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":64000,"stackLimit":32000}', '[{"input":"\\"babad\\"","output":"\\"bab\\""},{"input":"\\"cbbd\\"","output":"\\"bb\\""}]', 'def longestPalindrome(s: str) -> str:
    if not s:
        return ""
    res = s[0]
    for i in range(len(s)):
        # 奇数长度
        l = r = i
        while l >=0 and r < len(s) and s[l] == s[r]:
            if r - l + 1 > len(res):
                res = s[l:r+1]
            l -= 1
            r += 1
        # 偶数长度
        l, r = i, i+1
        while l >=0 and r < len(s) and s[l] == s[r]:
            if r - l + 1 > len(res):
                res = s[l:r+1]
            l -= 1
            r += 1
    return res', 0, 1903672737810321410, '2025-03-31 22:30:43', '2026-01-25 15:00:28', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906716773123031042, '二叉树的最大深度', '# 二叉树的最大深度

## 题目描述

给定一个二叉树，找出其最大深度。

二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

**说明：** 叶子节点是指没有子节点的节点。

## 输入格式

输入一行，表示二叉树的层序遍历，用空格分隔，null 表示空节点

## 输出格式

输出一个整数，表示二叉树的最大深度。

## 示例 1

**输入：**
```
3 9 20 null null 15 7
```

**输出：**
```
3
```

**解释：** 树的结构为：
```
    3
   / \\
  9  20
    /  \\
   15   7
```
最大深度为 3

## 示例 2

**输入：**
```
1 null 2
```

**输出：**
```
2
```

## 示例 3

**输入：**
```
1
```

**输出：**
```
1
```

## 提示

- 方法1：深度优先搜索（DFS）递归
- 方法2：广度优先搜索（BFS）层序遍历
- 递归公式：depth = max(leftDepth, rightDepth) + 1', '["树","深度优先搜索","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":64000,"stackLimit":32000}', '[{"input":"[3,9,20,null,null,15,7]","output":"3"},{"input":"[]","output":"0"}]', 'class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def maxDepth(root: TreeNode) -> int:
    if not root:
        return 0
    return 1 + max(maxDepth(root.left), maxDepth(root.right))', 0, 1903672737810321410, '2025-03-31 22:34:38', '2026-01-25 15:01:45', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906716809412149250, '前 K 个高频元素', '# 前 K 个高频元素

## 题目描述

给定一个整数数组 `nums` 和一个整数 `k`，请返回出现频率前 `k` 高的元素。答案可以按任意顺序返回。

## 输入格式

第一行输入整数 n 和 k，用空格分隔
第二行输入 n 个整数，用空格分隔

## 输出格式

输出 k 个整数，表示出现频率最高的 k 个元素，用空格分隔。

## 示例 1

**输入：**
```
6 2
1 1 1 2 2 3
```

**输出：**
```
1 2
```

**解释：** 1 出现 3 次，2 出现 2 次

## 示例 2

**输入：**
```
1 1
1
```

**输出：**
```
1
```

## 示例 3

**输入：**
```
7 3
4 1 4 4 2 2 3
```

**输出：**
```
4 2 1
```

## 提示

- 方法1：哈希表 + 排序 O(n log n)
- 方法2：哈希表 + 堆 O(n log k)
- 方法3：桶排序 O(n)
- 要求时间复杂度优于 O(n log n)', '["堆","哈希表","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"[1,1,1,2,2,3]\\n2","output":"[1,2]"},{"input":"[1]\\n1","output":"[1]"}]', 'import heapq
from collections import Counter

def topKFrequent(nums: list[int], k: int) -> list[int]:
    count = Counter(nums)
    return heapq.nlargest(k, count.keys(), key=lambda x: count[x])', 0, 1903672737810321410, '2025-03-31 22:34:47', '2026-01-25 15:04:59', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906716870829342722, '最长递增子序列', '# 最长递增子序列 (LIS)

## 题目描述

给你一个整数数组 `nums`，找到其中最长严格递增子序列的长度。

**子序列**是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。

## 输入格式

第一行输入一个整数 n，表示数组长度 (1 ≤ n ≤ 2500)
第二行输入 n 个整数，用空格分隔 (-10^4 ≤ nums[i] ≤ 10^4)

## 输出格式

输出一个整数，表示最长递增子序列的长度。

## 示例 1

**输入：**
```
8
10 9 2 5 3 7 101 18
```

**输出：**
```
4
```

**解释：** 最长递增子序列是 [2,3,7,101] 或 [2,3,7,18]，长度为 4

## 示例 2

**输入：**
```
7
0 1 0 3 2 3 7
```

**输出：**
```
4
```

**解释：** 最长递增子序列是 [0,1,2,3] 或 [0,1,2,7]

## 示例 3

**输入：**
```
1
1
```

**输出：**
```
1
```

## 提示

- 方法1：动态规划 O(n²)
  - dp[i] 表示以 nums[i] 结尾的最长递增子序列长度
- 方法2：二分查找 + 贪心 O(n log n)
  - 维护一个递增数组 tails', '["动态规划","数组","中等"]', 0, 0, '{"timeLimit":3000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"8\\n10 9 2 5 3 7 101 18","output":"4"},{"input":"7\\n0 1 0 3 2 3 7","output":"4"},{"input":"1\\n1","output":"1"}]', 'def lengthOfLIS(nums: list[int]) -> int:
    dp = [1] * len(nums)
    for i in range(len(nums)):
        for j in range(i):
            if nums[i] > nums[j]:
                dp[i] = max(dp[i], dp[j]+1)
    return max(dp)', 0, 1903672737810321410, '2025-03-31 22:35:01', '2026-01-25 14:54:30', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906716923727904769, '跳跃游戏', '# 跳跃游戏

## 题目描述

给定一个非负整数数组 `nums`，你最初位于数组的**第一个下标**。

数组中的每个元素代表你在该位置可以跳跃的**最大长度**。

判断你是否能够到达最后一个下标。

## 输入格式

第一行输入整数 n，表示数组长度 (1 ≤ n ≤ 10^4)
第二行输入 n 个非负整数，用空格分隔

## 输出格式

如果能到达最后一个下标，输出 "true"，否则输出 "false"。

## 示例 1

**输入：**
```
5
2 3 1 1 4
```

**输出：**
```
true
```

**解释：** 从下标 0 跳 1 步到下标 1，再跳 3 步到最后一个下标

## 示例 2

**输入：**
```
5
3 2 1 0 4
```

**输出：**
```
false
```

**解释：** 无论怎样，总会到达下标 3。但该下标的最大跳跃长度是 0，所以永远不可能到达最后一个下标

## 示例 3

**输入：**
```
1
0
```

**输出：**
```
true
```

**解释：** 已经在最后一个位置

## 提示

- 贪心算法
- 维护当前能到达的最远位置
- 时间复杂度 O(n)', '["贪心算法","数组","中等"]', 0, 0, '{"timeLimit":1000,"memoryLimit":64000,"stackLimit":32000}', '[{"input":"[2,3,1,1,4]","output":"true"},{"input":"[3,2,1,0,4]","output":"false"}]', 'def canJump(nums: list[int]) -> bool:
    max_reach = 0
    for i in range(len(nums)):
        if i > max_reach:
            return False
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= len(nums)-1:
            return True
    return True', 0, 1903672737810321410, '2025-03-31 22:35:14', '2026-01-25 15:05:16', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906716960751026178, '全排列', '# 全排列

## 题目描述

给定一个不含重复数字的数组 `nums`，返回其所有可能的全排列。你可以按**任意顺序**返回答案。

## 输入格式

第一行输入整数 n，表示数组长度 (1 ≤ n ≤ 6)
第二行输入 n 个不同的整数，用空格分隔

## 输出格式

输出所有排列，每个排列占一行，数字用空格分隔。

## 示例 1

**输入：**
```
3
1 2 3
```

**输出：**
```
1 2 3
1 3 2
2 1 3
2 3 1
3 1 2
3 2 1
```

## 示例 2

**输入：**
```
2
0 1
```

**输出：**
```
0 1
1 0
```

## 示例 3

**输入：**
```
1
1
```

**输出：**
```
1
```

## 提示

- 使用回溯算法（Backtracking）
- 时间复杂度 O(n × n!)
- 需要标记已使用的元素', '["回溯算法","递归","中等"]', 0, 0, '{"timeLimit":3000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"[1,2,3]","output":"[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]"},{"input":"[0]","output":"[[0]]"}]', 'def permute(nums: list[int]) -> list[list[int]]:
    def backtrack(path, used):
        if len(path) == len(nums):
            res.append(path.copy())
            return
        for num in nums:
            if num not in used:
                used.add(num)
                path.append(num)
                backtrack(path, used)
                path.pop()
                used.remove(num)
    res = []
    backtrack([], set())
    return res', 0, 1903672737810321410, '2025-03-31 22:35:23', '2026-01-25 15:05:47', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1906717033719332866, '课程表', '# 课程表

## 题目描述

你总共需要选 `numCourses` 门课程，课程编号为 `0` 到 `numCourses - 1`。

给你一个数组 `prerequisites`，其中 `prerequisites[i] = [ai, bi]` 表示如果要学习课程 `ai`，你必须**先**学习课程 `bi`。

判断是否可能完成所有课程的学习。

## 输入格式

第一行输入整数 numCourses 和 m，表示课程数量和先修关系数量
接下来 m 行，每行两个整数 a 和 b，表示要学 a 必须先学 b

## 输出格式

如果可以完成所有课程，输出 "true"，否则输出 "false"。

## 示例 1

**输入：**
```
2 1
1 0
```

**输出：**
```
true
```

**解释：** 总共 2 门课。学习课程 1 前，需要先完成课程 0。这是可能的。

## 示例 2

**输入：**
```
2 2
1 0
0 1
```

**输出：**
```
false
```

**解释：** 存在循环依赖，无法完成

## 示例 3

**输入：**
```
3 0
```

**输出：**
```
true
```

**解释：** 没有先修要求，都可以学

## 提示

- 拓扑排序问题
- 方法1：DFS 检测环
- 方法2：BFS + 入度统计
- 如果存在环，则无法完成', '["拓扑排序","图论","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"2\\n[[1,0]]","output":"true"},{"input":"2\\n[[1,0],[0,1]]","output":"false"}]', 'def canFinish(numCourses: int, prerequisites: list[list[int]]) -> bool:
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    for a, b in prerequisites:
        graph[b].append(a)
        in_degree[a] += 1
    queue = [i for i in range(numCourses) if in_degree[i] == 0]
    count = 0
    while queue:
        node = queue.pop(0)
        count += 1
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    return count == numCourses', 0, 1903672737810321410, '2025-03-31 22:35:40', '2026-01-25 15:06:25', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1915640659701104642, '最大子数组和', '# 最大子数组和（最大连续子序列和）

## 题目描述

给定一个整数数组，找出一个具有最大和的连续子数组（至少包含一个元素），返回其最大和。

这是著名的 **Kadane 算法** 经典问题。

## 输入格式

第一行输入一个整数 n，表示数组长度 (1 ≤ n ≤ 10^5)
第二行输入 n 个整数，用空格分隔 (-10^4 ≤ nums[i] ≤ 10^4)

## 输出格式

输出一个整数，表示最大子数组和。

## 示例 1

**输入：**

```
9 -2 1 -3 4 -1 2 1 -5 4
```

**输出：**

```
6
```

**解释：** 连续子数组 [4,-1,2,1] 的和最大，为 6

## 示例 2

**输入：**

```
1 5
```

**输出：**

```
5
```

## 示例 3

**输入：**

```
5 -1 -2 -3 -4 -5
```

**输出：**

```
-1
```

**解释：** 全是负数时，返回最大的那个负数

## 提示

- 动态规划：dp[i] = max(dp[i-1] + nums[i], nums[i])
- 也可以用 Kadane 算法，空间复杂度 O(1)
', '["动态规划","Kadane算法","入门算法","简单"]', 0, 0, '{"timeLimit":300000,"memoryLimit":512000,"stackLimit":512000}', '[{"input":"9 -2 1 -3 4 -1 2 1 -5 4","output":"6"},{"input":"1 5","output":"5"},{"input":"5 -1 -2 -3 -4 -5","output":"-1"},{"input":"5 1 2 3 4 5","output":"15"}]', 'public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(0);
            return;
        }
        
        int[] nums = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            nums[i] = Integer.parseInt(args[i]);
        }
        
        int maxCurrent = nums[0];
        int maxGlobal = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            maxCurrent = Math.max(nums[i], maxCurrent + nums[i]);
            if (maxCurrent > maxGlobal) {
                maxGlobal = maxCurrent;
            }
        }
        
        System.out.println(maxGlobal);
    }
}', 0, 1903672737810321410, '2025-04-25 13:34:58', '2026-01-25 22:12:36', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286421, '反转链表', '# 反转链表

## 题目描述

给你单链表的头节点 `head`，请你反转链表，并返回反转后的链表的头节点。

## 输入格式

第一行输入一个整数 n，表示链表长度 (0 ≤ n ≤ 5000)
第二行输入 n 个整数，表示链表节点的值，用空格分隔 (-5000 ≤ val ≤ 5000)

## 输出格式

输出反转后的链表，节点值用空格分隔。

## 示例 1

**输入：**
```
5
1 2 3 4 5
```

**输出：**
```
5 4 3 2 1
```

## 示例 2

**输入：**
```
2
1 2
```

**输出：**
```
2 1
```

## 示例 3

**输入：**
```
0

```

**输出：**
```

```

**解释：** 空链表，返回空

## 提示

- 方法1：迭代法，使用三个指针 prev, curr, next
- 方法2：递归法
- 时间复杂度 O(n)，空间复杂度 O(1)（迭代）或 O(n)（递归）', '["链表","双指针","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"5\\n1 2 3 4 5","output":"5 4 3 2 1"},{"input":"2\\n1 2","output":"2 1"},{"input":"0\\n","output":""}]', 'class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def reverseList(head: ListNode) -> ListNode:
    prev = None
    curr = head
    while curr:
        next_temp = curr.next
        curr.next = prev
        prev = curr
        curr = next_temp
    return prev', 0, 1903672737810321410, '2025-12-01 12:00:00', '2026-01-25 14:52:10', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286422, '有效括号', '# 有效的括号

## 题目描述

给定一个只包括 `(`，`)`，`{`，`}`，`[`，`]` 的字符串 `s`，判断字符串是否有效。

有效字符串需满足：
1. 左括号必须用相同类型的右括号闭合
2. 左括号必须以正确的顺序闭合
3. 每个右括号都有一个对应的相同类型的左括号

## 输入格式

输入一个字符串 s (1 ≤ s.length ≤ 10^4)

## 输出格式

如果字符串有效输出 "true"，否则输出 "false"。

## 示例 1

**输入：**
```
()
```

**输出：**
```
true
```

## 示例 2

**输入：**
```
()[]{}
```

**输出：**
```
true
```

## 示例 3

**输入：**
```
(]
```

**输出：**
```
false
```

**解释：** 左括号类型和右括号类型不匹配

## 示例 4

**输入：**
```
([)]
```

**输出：**
```
false
```

**解释：** 括号没有以正确的顺序闭合

## 示例 5

**输入：**
```
{[]}
```

**输出：**
```
true
```

## 提示

- 使用栈（Stack）数据结构
- 遇到左括号入栈
- 遇到右括号时，检查栈顶是否为对应的左括号
- 最后检查栈是否为空', '["栈","字符串","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"()","output":"true"},{"input":"()[]{}","output":"true"},{"input":"(]","output":"false"},{"input":"([)]","output":"false"},{"input":"{[]}","output":"true"}]', 'def isValid(s: str) -> bool:
    stack = []
    mapping = {")": "(", "}": "{", "]": "["}
    for char in s:
        if char in mapping:
            if not stack or stack.pop() != mapping[char]:
                return False
        else:
            stack.append(char)
    return not stack', 0, 1903672737810321410, '2025-12-01 12:05:00', '2026-01-25 14:52:26', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286423, '二叉树的层序遍历', '# 二叉树的层序遍历

## 题目描述

给你二叉树的根节点 `root`，返回其节点值的**层序遍历**。即逐层地，从左到右访问所有节点。

## 输入格式

输入一行，表示二叉树的层序遍历，用空格分隔，null 表示空节点

## 输出格式

输出多行，每行表示一层的节点值，用空格分隔。

## 示例 1

**输入：**
```
3 9 20 null null 15 7
```

**输出：**
```
3
9 20
15 7
```

**解释：** 树的结构为：
```
    3
   / \\
  9  20
    /  \\
   15   7
```

## 示例 2

**输入：**
```
1
```

**输出：**
```
1
```

## 示例 3

**输入：**
```
null
```

**输出：**
```

```

## 提示

- 使用广度优先搜索（BFS）
- 借助队列实现
- 需要记录每层的节点数量', '["树","广度优先搜索","中等"]', 0, 0, '{"timeLimit":10000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"[3,9,20,null,null,15,7]","output":"[[3],[9,20],[15,7]]"}]', 'from collections import deque
from typing import List, Optional

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def levelOrder(root: Optional[TreeNode]) -> List[List[int]]:
    if not root:
        return []

    result = []
    queue = deque([root])

    while queue:
        level_size = len(queue)
        current_level = []

        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)

            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)

        result.append(current_level)

    return result', 0, 1903672737810321410, '2025-12-01 12:10:00', '2026-01-25 16:58:14', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286424, '快速排序', '# 快速排序

## 题目描述

实现快速排序算法（QuickSort），将给定数组按升序排列。

快速排序是一种高效的排序算法，采用**分治法**策略，平均时间复杂度为 O(n log n)。

## 输入格式

第一行输入一个整数 n，表示数组长度 (1 ≤ n ≤ 10^5)
第二行输入 n 个整数，用空格分隔

## 输出格式

输出排序后的数组，数字之间用空格分隔。

## 示例 1

**输入：**
```
4
5 2 3 1
```

**输出：**
```
1 2 3 5
```

## 示例 2

**输入：**
```
5
3 1 4 1 5
```

**输出：**
```
1 1 3 4 5
```

## 示例 3

**输入：**
```
1
9
```

**输出：**
```
9
```

## 示例 4

**输入：**
```
6
-3 5 0 -1 8 2
```

**输出：**
```
-3 -1 0 2 5 8
```

## 算法思想

1. 选择一个基准元素（pivot）
2. 将小于基准的元素放左边，大于基准的放右边（分区操作）
3. 递归地对左右两个子数组进行快速排序

## 提示

- 分区函数是关键
- 可以选择第一个、最后一个或随机元素作为 pivot
- 注意处理重复元素
- 最坏情况时间复杂度 O(n²)，平均 O(n log n)', '["排序","分治","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"4\\n5 2 3 1","output":"1 2 3 5"},{"input":"5\\n3 1 4 1 5","output":"1 1 3 4 5"},{"input":"1\\n9","output":"9"},{"input":"6\\n-3 5 0 -1 8 2","output":"-3 -1 0 2 5 8"}]', 'def quickSort(nums, low, high):
    if low < high:
        pi = partition(nums, low, high)
        quickSort(nums, low, pi - 1)
        quickSort(nums, pi + 1, high)

def partition(nums, low, high):
    pivot = nums[high]
    i = low - 1
    for j in range(low, high):
        if nums[j] <= pivot:
            i += 1
            nums[i], nums[j] = nums[j], nums[i]
    nums[i + 1], nums[high] = nums[high], nums[i + 1]
    return i + 1

def sortArray(nums):
    quickSort(nums, 0, len(nums) - 1)
    return nums', 0, 1903672737810321410, '2025-12-01 12:15:00', '2026-01-25 14:54:14', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286425, '二分查找', '# 二分查找（标准模板）

## 题目描述

给定一个 n 个元素有序的（升序）整型数组 `nums` 和一个目标值 `target`，请你找到并返回 `target` 在 `nums` 中的索引。

如果目标值不存在于数组中，返回 -1。

你必须使用时间复杂度为 O(log n) 的算法。

## 输入格式

第一行输入两个整数 n 和 target，分别表示数组长度和目标值
第二行输入 n 个升序整数，用空格分隔

## 输出格式

输出一个整数，表示目标值的下标（从0开始），如果不存在则输出 -1。

## 示例 1

**输入：**
```
5 9
-1 0 3 5 9
```

**输出：**
```
4
```

## 示例 2

**输入：**
```
5 2
-1 0 3 5 9
```

**输出：**
```
-1
```

## 示例 3

**输入：**
```
1 5
5
```

**输出：**
```
0
```

## 提示

- 经典二分查找模板
- left = 0, right = n - 1
- 每次取 mid = (left + right) / 2
- 根据 nums[mid] 与 target 的大小关系调整边界', '["二分查找","数组","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"[-1,0,3,5,9,12] 9","output":"4"},{"input":"[-1,0,3,5,9,12] 2","output":"-1"}]', 'def search(nums, target):
    left, right = 0, len(nums) - 1

    while left <= right:
        mid = left + (right - left) // 2

        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1

    return -1', 0, 1903672737810321410, '2025-12-01 12:20:00', '2026-01-25 15:09:46', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286426, '合并区间', '# 合并区间

## 题目描述

以数组 `intervals` 表示若干个区间的集合，其中单个区间为 `intervals[i] = [starti, endi]`。

请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

## 输入格式

第一行输入整数 n，表示区间数量
接下来 n 行，每行两个整数表示一个区间的起始和结束位置

## 输出格式

输出合并后的区间，每行两个整数。

## 示例 1

**输入：**
```
4
1 3
2 6
8 10
15 18
```

**输出：**
```
1 6
8 10
15 18
```

**解释：** 区间 [1,3] 和 [2,6] 重叠，合并为 [1,6]

## 示例 2

**输入：**
```
2
1 4
4 5
```

**输出：**
```
1 5
```

**解释：** 区间 [1,4] 和 [4,5] 可被视为重叠区间

## 提示

- 先按起始位置排序
- 遍历合并相邻的重叠区间
- 时间复杂度 O(n log n)', '["数组","排序","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"[[1,3],[2,6],[8,10],[15,18]]","output":"[[1,6],[8,10],[15,18]]"}]', 'def merge(intervals):
    if not intervals:
        return []

    intervals.sort(key=lambda x: x[0])
    result = [intervals[0]]

    for i in range(1, len(intervals)):
        if intervals[i][0] <= result[-1][1]:
            result[-1][1] = max(result[-1][1], intervals[i][1])
        else:
            result.append(intervals[i])

    return result', 0, 1903672737810321410, '2025-12-01 12:25:00', '2026-01-25 15:07:24', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286427, 'LRU缓存机制', '# LRU 缓存机制

## 题目描述

请你设计并实现一个满足 **LRU (最近最少使用)** 缓存约束的数据结构。

实现 `LRUCache` 类：
- `LRUCache(int capacity)` 以正整数作为容量初始化 LRU 缓存
- `int get(int key)` 如果关键字 key 存在，返回其值，否则返回 -1
- `void put(int key, int value)` 如果关键字存在，则变更其值；如果不存在，则插入该键值对。当缓存容量达到上限时，应该在写入新数据之前删除最久未使用的数据

## 输入格式

第一行输入操作序列长度 n 和缓存容量 capacity
接下来 n 行，每行一个操作：
- get key
- put key value

## 输出格式

对每个 get 操作，输出一个整数结果。

## 示例 1

**输入：**
```
5 2
put 1 1
put 2 2
get 1
put 3 3
get 2
```

**输出：**
```
1
-1
```

**解释：**
- put(1,1) put(2,2) 缓存：{1=1, 2=2}
- get(1) → 1，缓存：{2=2, 1=1}
- put(3,3) 移除 key 2，缓存：{1=1, 3=3}
- get(2) → -1（未找到）

## 提示

- 使用哈希表 + 双向链表
- 哈希表保证 O(1) 查找
- 双向链表维护访问顺序
- 时间复杂度：get 和 put 都是 O(1)', '["设计","哈希表","链表","困难"]', 0, 0, '{"timeLimit":3000,"memoryLimit":512000,"stackLimit":128000}', '[{"input":"capacity=2, put(1,1), put(2,2), get(1), put(3,3), get(2)","output":"1, -1"}]', 'class Node:
    def __init__(self, key=0, value=0):
        self.key = key
        self.value = value
        self.prev = None
        self.next = None

class LRUCache:
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.cache = {}
        self.head = Node()
        self.tail = Node()
        self.head.next = self.tail
        self.tail.prev = self.head

    def _remove(self, node):
        node.prev.next = node.next
        node.next.prev = node.prev

    def _add(self, node):
        node.next = self.head.next
        node.prev = self.head
        self.head.next.prev = node
        self.head.next = node

    def get(self, key: int) -> int:
        if key in self.cache:
            node = self.cache[key]
            self._remove(node)
            self._add(node)
            return node.value
        return -1

    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            self._remove(self.cache[key])
        node = Node(key, value)
        self._add(node)
        self.cache[key] = node
        if len(self.cache) > self.capacity:
            lru = self.tail.prev
            self._remove(lru)
            del self.cache[lru.key]', 0, 1903672737810321410, '2025-12-01 12:30:00', '2026-01-25 15:07:36', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286428, '岛屿数量', '# 岛屿数量

## 题目描述

给你一个由 `1`（陆地）和 `0`（水）组成的二维网格，请你计算网格中岛屿的数量。

岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。

此外，你可以假设该网格的四条边均被水包围。

## 输入格式

第一行输入两个整数 m 和 n，表示网格的行数和列数
接下来 m 行，每行 n 个字符（0 或 1），用空格分隔

## 输出格式

输出一个整数，表示岛屿数量。

## 示例 1

**输入：**
```
4 5
1 1 1 1 0
1 1 0 1 0
1 1 0 0 0
0 0 0 0 0
```

**输出：**
```
1
```

## 示例 2

**输入：**
```
4 5
1 1 0 0 0
1 1 0 0 0
0 0 1 0 0
0 0 0 1 1
```

**输出：**
```
3
```

## 提示

- 方法1：深度优先搜索（DFS）
- 方法2：广度优先搜索（BFS）
- 方法3：并查集
- 遍历每个陆地，标记连通的陆地', '["深度优先搜索","广度优先搜索","并查集","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"[[\\"1\\",\\"1\\",\\"1\\",\\"1\\",\\"0\\"],[\\"1\\",\\"1\\",\\"0\\",\\"1\\",\\"0\\"],[\\"1\\",\\"1\\",\\"0\\",\\"0\\",\\"0\\"],[\\"0\\",\\"0\\",\\"0\\",\\"0\\",\\"0\\"]]","output":"1"}]', 'def numIslands(grid):
    if not grid:
        return 0

    rows, cols = len(grid), len(grid[0])
    count = 0

    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == "0":
            return
        grid[r][c] = "0"
        dfs(r + 1, c)
        dfs(r - 1, c)
        dfs(r, c + 1)
        dfs(r, c - 1)

    for i in range(rows):
        for j in range(cols):
            if grid[i][j] == "1":
                dfs(i, j)
                count += 1

    return count', 0, 1903672737810321410, '2025-12-01 12:35:00', '2026-01-25 15:07:59', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286429, '最小栈', '# 最小栈

## 题目描述

设计一个支持 `push`、`pop`、`top` 操作，并能在**常数时间**内检索到最小元素的栈。

实现 `MinStack` 类：
- `MinStack()` 初始化栈对象
- `void push(int val)` 将元素 val 推入栈中
- `void pop()` 删除栈顶元素
- `int top()` 获取栈顶元素
- `int getMin()` 获取栈中的最小元素

## 输入格式

第一行输入操作数量 n
接下来 n 行，每行一个操作：
- push val
- pop
- top
- getMin

## 输出格式

对每个 top 和 getMin 操作，输出一个整数结果。

## 示例 1

**输入：**
```
7
push -2
push 0
push -3
getMin
pop
top
getMin
```

**输出：**
```
-3
0
-2
```

**解释：**
- push(-2) push(0) push(-3)，栈：[-2, 0, -3]
- getMin() → -3
- pop()，栈：[-2, 0]
- top() → 0
- getMin() → -2

## 提示

- 使用辅助栈存储最小值
- 或者每个节点同时存储当前最小值
- 所有操作时间复杂度 O(1)', '["栈","设计","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":128000,"stackLimit":64000}', '[{"input":"push(-2), push(0), push(-3), getMin(), pop(), top(), getMin()","output":"-3, 0, -2"}]', 'class MinStack:
    def __init__(self):
        self.stack = []
        self.min_stack = []

    def push(self, val: int) -> None:
        self.stack.append(val)
        if not self.min_stack or val <= self.min_stack[-1]:
            self.min_stack.append(val)

    def pop(self) -> None:
        if self.stack:
            val = self.stack.pop()
            if val == self.min_stack[-1]:
                self.min_stack.pop()

    def top(self) -> int:
        return self.stack[-1] if self.stack else -1

    def getMin(self) -> int:
        return self.min_stack[-1] if self.min_stack else -1', 0, 1903672737810321410, '2025-12-01 12:40:00', '2026-01-25 15:08:26', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286430, '第K个最大元素', '# 第 K 个最大元素

## 题目描述

给定整数数组 `nums` 和整数 `k`，请返回数组中第 `k` 个最大的元素。

注意，你需要找的是数组排序后的第 `k` 个最大的元素，而不是第 `k` 个不同的元素。

## 输入格式

第一行输入整数 n 和 k
第二行输入 n 个整数，用空格分隔

## 输出格式

输出一个整数，表示第 k 个最大元素。

## 示例 1

**输入：**
```
6 2
3 2 1 5 6 4
```

**输出：**
```
5
```

**解释：** 排序后为 [6,5,4,3,2,1]，第 2 大的元素是 5

## 示例 2

**输入：**
```
9 4
3 2 3 1 2 4 5 5 6
```

**输出：**
```
4
```

**解释：** 排序后为 [6,5,5,4,3,3,2,2,1]，第 4 大的元素是 4

## 示例 3

**输入：**
```
1 1
1
```

**输出：**
```
1
```

## 提示

- 方法1：排序 O(n log n)
- 方法2：小顶堆 O(n log k)
- 方法3：快速选择 O(n) 平均情况
- 推荐使用堆或快速选择', '["堆","分治","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":64000}', '[{"input":"[3,2,1,5,6,4] 2","output":"5"},{"input":"[3,2,3,1,2,4,5,5,6] 4","output":"4"}]', 'import heapq

def findKthLargest(nums, k):
    return heapq.nlargest(k, nums)[-1]', 0, 1903672737810321410, '2025-12-01 12:45:00', '2026-01-25 15:08:37', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286431, '斐波那契数列', '# 斐波那契数列

## 题目描述

斐波那契数列是一个经典的数列，定义如下：

- F(0) = 0
- F(1) = 1
- F(n) = F(n-1) + F(n-2) (n ≥ 2)

数列为：0, 1, 1, 2, 3, 5, 8, 13, 21, 34, ...

给定一个整数 n，请计算斐波那契数列的第 n 项。

## 输入格式

输入一个整数 n (0 ≤ n ≤ 40)

## 输出格式

输出一个整数，表示斐波那契数列的第 n 项。

## 示例 1

**输入：**
```
5
```

**输出：**
```
5
```

**解释：** F(5) = F(4) + F(3) = 3 + 2 = 5

## 示例 2

**输入：**
```
10
```

**输出：**
```
55
```

## 示例 3

**输入：**
```
0
```

**输出：**
```
0
```

## 示例 4

**输入：**
```
1
```

**输出：**
```
1
```

## 提示

- 可以使用递归（但效率低）
- 推荐使用动态规划或迭代
- 空间复杂度可以优化到 O(1)', '["数学","递归","动态规划","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"5","output":"5"},{"input":"10","output":"55"},{"input":"0","output":"0"},{"input":"1","output":"1"},{"input":"20","output":"6765"}]', '动态规划或递归实现', 0, 1, '2026-01-25 13:51:13', '2026-01-25 14:22:08', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        int n = sc.nextInt();\\n        // TODO: 计算斐波那契数列的第n项\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    int n;\\n    cin >> n;\\n    // TODO: 计算斐波那契数列的第n项\\n    \\n    return 0;\\n}","python":"n = int(input())\\n# TODO: 计算斐波那契数列的第n项\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286432, '最大公约数', '# 最大公约数 (GCD)

## 题目描述

给定两个正整数 a 和 b，请计算它们的最大公约数（Greatest Common Divisor）。

最大公约数是指能够同时整除两个数的最大正整数。

## 输入格式

输入两个正整数 a 和 b，用空格分隔 (1 ≤ a, b ≤ 10^9)

## 输出格式

输出一个整数，表示 a 和 b 的最大公约数。

## 示例 1

**输入：**
```
12 8
```

**输出：**
```
4
```

**解释：** 12 和 8 的公约数有 1, 2, 4，最大的是 4

## 示例 2

**输入：**
```
100 50
```

**输出：**
```
50
```

**解释：** 50 能整除 100，所以最大公约数就是 50

## 示例 3

**输入：**
```
17 19
```

**输出：**
```
1
```

**解释：** 17 和 19 都是质数，最大公约数是 1

## 示例 4

**输入：**
```
1071 462
```

**输出：**
```
21
```

## 提示

- 使用欧几里得算法（辗转相除法）
- gcd(a, b) = gcd(b, a mod b)
- 递归终止条件：b = 0 时，返回 a', '["数学","递归","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"12 8","output":"4"},{"input":"100 50","output":"50"},{"input":"17 19","output":"1"},{"input":"1071 462","output":"21"},{"input":"1000000 500000","output":"500000"}]', '欧几里得算法（辗转相除法）', 0, 1, '2026-01-25 13:51:31', '2026-01-25 14:22:24', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        int a = sc.nextInt();\\n        int b = sc.nextInt();\\n        // TODO: 计算最大公约数\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    int a, b;\\n    cin >> a >> b;\\n    // TODO: 计算最大公约数\\n    \\n    return 0;\\n}","python":"a, b = map(int, input().split())\\n# TODO: 计算最大公约数\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286433, '数组排序', '# 数组排序

## 题目描述

给定一个整数数组，请将其按升序排序后输出。

## 输入格式

第一行输入一个整数 n，表示数组长度 (1 ≤ n ≤ 10000)
第二行输入 n 个整数，用空格分隔 (-10^6 ≤ nums[i] ≤ 10^6)

## 输出格式

输出排序后的数组，数字之间用空格分隔。

## 示例 1

**输入：**
```
5
3 1 4 1 5
```

**输出：**
```
1 1 3 4 5
```

## 示例 2

**输入：**
```
3
9 2 7
```

**输出：**
```
2 7 9
```

## 示例 3

**输入：**
```
1
42
```

**输出：**
```
42
```

## 示例 4

**输入：**
```
6
-5 0 3 -2 8 1
```

**输出：**
```
-5 -2 0 1 3 8
```

## 提示

- 可以使用快速排序、归并排序、堆排序等
- 也可以直接使用语言内置的排序函数
- 注意处理重复元素和负数', '["数组","排序","简单"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"5\\n3 1 4 1 5","output":"1 1 3 4 5"},{"input":"3\\n9 2 7","output":"2 7 9"},{"input":"1\\n42","output":"42"},{"input":"6\\n-5 0 3 -2 8 1","output":"-5 -2 0 1 3 8"}]', '使用快速排序、归并排序或内置排序函数', 0, 1, '2026-01-25 13:51:53', '2026-01-25 14:29:38', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        int n = sc.nextInt();\\n        int[] arr = new int[n];\\n        for (int i = 0; i < n; i++) {\\n            arr[i] = sc.nextInt();\\n        }\\n        // TODO: 对数组进行排序并输出\\n        \\n    }\\n}","cpp":"#include <iostream>\\n#include <algorithm>\\nusing namespace std;\\n\\nint main() {\\n    int n;\\n    cin >> n;\\n    int arr[n];\\n    for (int i = 0; i < n; i++) {\\n        cin >> arr[i];\\n    }\\n    // TODO: 对数组进行排序并输出\\n    \\n    return 0;\\n}","python":"n = int(input())\\narr = list(map(int, input().split()))\\n# TODO: 对数组进行排序并输出\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286434, '回文数判断', '# 回文数判断

## 题目描述

给定一个整数，判断它是否是回文数。

**回文数**是指正序（从左向右）和倒序（从右向左）读都是一样的整数。

## 输入格式

输入一个整数 x (-2^31 ≤ x ≤ 2^31 - 1)

## 输出格式

如果是回文数输出 "true"，否则输出 "false"。

注意：负数不是回文数。

## 示例 1

**输入：**
```
121
```

**输出：**
```
true
```

**解释：** 121 从左向右读是 121，从右向左读也是 121

## 示例 2

**输入：**
```
-121
```

**输出：**
```
false
```

**解释：** 从左向右读是 -121，从右向左读是 121-，不是回文数

## 示例 3

**输入：**
```
10
```

**输出：**
```
false
```

**解释：** 从右向左读是 01，不同于 10

## 示例 4

**输入：**
```
12321
```

**输出：**
```
true
```

## 提示

- 方法1：转换为字符串判断
- 方法2：反转数字的后半部分进行比较
- 注意边界情况：负数、个位数、末尾为0的数', '["数学","字符串","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"121","output":"true"},{"input":"-121","output":"false"},{"input":"10","output":"false"},{"input":"12321","output":"true"},{"input":"0","output":"true"}]', '将数字反转或转为字符串判断', 0, 1, '2026-01-25 13:52:45', '2026-01-25 14:31:36', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        int x = sc.nextInt();\\n        // TODO: 判断是否为回文数\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    int x;\\n    cin >> x;\\n    // TODO: 判断是否为回文数\\n    \\n    return 0;\\n}","python":"x = int(input())\\n# TODO: 判断是否为回文数\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286435, '二分查找', '# 二分查找

## 题目描述

给定一个升序排列的整数数组 `nums` 和一个目标值 `target`，请在数组中找到 target 并返回其下标。

如果目标值不存在于数组中，返回 -1。

你必须使用时间复杂度为 O(log n) 的算法。

## 输入格式

第一行输入两个整数 n 和 target，分别表示数组长度和目标值 (1 ≤ n ≤ 10^4, -10^4 ≤ target ≤ 10^4)
第二行输入 n 个升序排列的整数，用空格分隔

## 输出格式

输出一个整数，表示目标值的下标（从0开始），如果不存在则输出 -1。

## 示例 1

**输入：**
```
5 9
-1 0 3 5 9
```

**输出：**
```
4
```

**解释：** 9 在数组中的下标是 4

## 示例 2

**输入：**
```
5 2
-1 0 3 5 9
```

**输出：**
```
-1
```

**解释：** 2 不在数组中，返回 -1

## 示例 3

**输入：**
```
1 5
5
```

**输出：**
```
0
```

## 示例 4

**输入：**
```
6 0
-5 -2 0 3 7 10
```

**输出：**
```
2
```

## 提示

- 二分查找的标准模板题
- left = 0, right = n - 1
- 每次比较 nums[mid] 和 target
- 注意边界条件和循环终止条件', '["数组","二分查找","简单"]', 0, 0, '{"timeLimit":1000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"5 9\\n-1 0 3 5 9","output":"4"},{"input":"5 2\\n-1 0 3 5 9","output":"-1"},{"input":"1 5\\n5","output":"0"},{"input":"6 0\\n-5 -2 0 3 7 10","output":"2"}]', '使用二分查找算法', 0, 1, '2026-01-25 13:53:36', '2026-01-25 14:32:43', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        int n = sc.nextInt();\\n        int target = sc.nextInt();\\n        int[] nums = new int[n];\\n        for (int i = 0; i < n; i++) {\\n            nums[i] = sc.nextInt();\\n        }\\n        // TODO: 使用二分查找\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    int n, target;\\n    cin >> n >> target;\\n    int nums[n];\\n    for (int i = 0; i < n; i++) {\\n        cin >> nums[i];\\n    }\\n    // TODO: 使用二分查找\\n    \\n    return 0;\\n}","python":"n, target = map(int, input().split())\\nnums = list(map(int, input().split()))\\n# TODO: 使用二分查找\\n"}');
INSERT INTO smarter_oj_db.question (id, title, content, tags, submitNum, acceptedNum, judgeConfig, judgeCase, answer, favourNum, userId, createTime, updateTime, isDelete, codeTemplate) VALUES (1985166201437286436, '最长公共子序列', '# 最长公共子序列 (LCS)

## 题目描述

给定两个字符串 `text1` 和 `text2`，返回这两个字符串的**最长公共子序列**的长度。如果不存在公共子序列，返回 0。

一个字符串的**子序列**是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。

例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。

## 输入格式

第一行输入字符串 text1
第二行输入字符串 text2
(1 ≤ text1.length, text2.length ≤ 1000)

## 输出格式

输出一个整数，表示最长公共子序列的长度。

## 示例 1

**输入：**
```
abcde
ace
```

**输出：**
```
3
```

**解释：** 最长公共子序列是 "ace"，长度为 3

## 示例 2

**输入：**
```
abc
abc
```

**输出：**
```
3
```

**解释：** 两个字符串完全相同，LCS 就是 "abc"

## 示例 3

**输入：**
```
abc
def
```

**输出：**
```
0
```

**解释：** 没有公共子序列

## 示例 4

**输入：**
```
abcdef
fedcba
```

**输出：**
```
1
```

**解释：** 任意一个字符都可以作为 LCS，例如 "a" 或 "b"

## 提示

- 经典的二维动态规划问题
- dp[i][j] 表示 text1 前 i 个字符和 text2 前 j 个字符的 LCS 长度
- 状态转移方程：
  - 如果 text1[i-1] == text2[j-1]，则 dp[i][j] = dp[i-1][j-1] + 1
  - 否则 dp[i][j] = max(dp[i-1][j], dp[i][j-1])', '["字符串","动态规划","中等"]', 0, 0, '{"timeLimit":2000,"memoryLimit":256000,"stackLimit":128000}', '[{"input":"abcde\\nace","output":"3"},{"input":"abc\\nabc","output":"3"},{"input":"abc\\ndef","output":"0"},{"input":"abcdef\\nfedcba","output":"1"}]', '使用动态规划，dp[i][j]表示text1前i个字符和text2前j个字符的LCS长度', 0, 1, '2026-01-25 13:55:14', '2026-01-25 14:50:03', 0, '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        String text1 = sc.nextLine();\\n        String text2 = sc.nextLine();\\n        // TODO: 计算最长公共子序列\\n        \\n    }\\n}","cpp":"#include <iostream>\\n#include <string>\\nusing namespace std;\\n\\nint main() {\\n    string text1, text2;\\n    getline(cin, text1);\\n    getline(cin, text2);\\n    // TODO: 计算最长公共子序列\\n    \\n    return 0;\\n}","python":"text1 = input()\\ntext2 = input()\\n# TODO: 计算最长公共子序列\\n"}');
