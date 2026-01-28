package fun.javierchen.jcsmarterojbackendjudgeservice;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;

public class InputParsingTest {

    @Test
    void testBasicParsing() {
        // 模拟 A+B 格式: "1 2"
        String input = "1 2";
        Scanner sc = new Scanner(input);
        int a = sc.nextInt();
        int b = sc.nextInt();
        System.out.println("A+B Result: " + (a + b));
        assert a + b == 3;
    }

    @Test
    void testArrayParsing() {
        // 模拟题目中的数组输入格式: "[1,1,1,2,2,3]"
        String input = "[1,1,1,2,2,3]";

        // 方法 1: 手动解析 (去除括号并分割)
        String cleanInput = input.trim();
        if (cleanInput.startsWith("["))
            cleanInput = cleanInput.substring(1);
        if (cleanInput.endsWith("]"))
            cleanInput = cleanInput.substring(0, cleanInput.length() - 1);

        String[] parts = cleanInput.split(",");
        List<Integer> nums = Arrays.stream(parts)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println("Parsed Array: " + nums);
        assert nums.size() == 6;
        assert nums.get(0) == 1;
    }

    @Test
    void testTreeParsing() {
        // 模拟二叉树层序遍历格式: "[3,9,20,null,null,15,7]"
        String input = "[3,9,20,null,null,15,7]";

        String cleanInput = input.trim();
        if (cleanInput.startsWith("["))
            cleanInput = cleanInput.substring(1);
        if (cleanInput.endsWith("]"))
            cleanInput = cleanInput.substring(0, cleanInput.length() - 1);

        String[] nodes = cleanInput.split(",");
        List<String> nodeList = Arrays.stream(nodes)
                .map(String::trim)
                .collect(Collectors.toList());

        System.out.println("Parsed Tree Nodes: " + nodeList);
        assert nodeList.get(0).equals("3");
        assert nodeList.get(3).equals("null");
    }

    @Test
    void testMultiLineParsing() {
        // 模拟 Top K 格式: "[1,1,1,2,2,3]\n2"
        String input = "[1,1,1,2,2,3]\n2";
        Scanner sc = new Scanner(input);

        if (sc.hasNextLine()) {
            String arrayLine = sc.nextLine();
            System.out.println("Array Line: " + arrayLine);
            // 这里再按照上面的 ArrayParsing 解析即可
        }
        if (sc.hasNextLine()) {
            String kLine = sc.nextLine();
            int k = Integer.parseInt(kLine.trim());
            System.out.println("K: " + k);
            assert k == 2;
        }
    }

    @Test
    void testOJSnowflakeTreeParsing() {
        // 发现数据库中实际的输入是 JSON 格式: "[3,9,20,null,null,15,7]"
        // 但题目描述里写的是空格分隔: "3 9 20 null null 15 7"
        // 这里的测试验证如果是 JSON 格式该如何稳健解析
        String dbInput = "[3,9,20,null,null,15,7]";

        // 稳健解析策略：只保留数字、字母和逗号，其他统统过滤掉（或者简单的 trim 加 substring）
        String clean = dbInput.trim();
        if (clean.startsWith("["))
            clean = clean.substring(1);
        if (clean.endsWith("]"))
            clean = clean.substring(0, clean.length() - 1);

        String[] nodes = clean.split(",");
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = nodes[i].trim();
        }

        System.out.println("Real DB Input Parsed: " + Arrays.toString(nodes));
        assert nodes[0].equals("3");
        assert nodes[nodes.length - 1].equals("7");
    }

    @Test
    void testRobustParsing() {
        // 终极方案：不管是 "1,2,3" 还是 "[1, 2, 3]" 都能解析
        String input1 = "[1, 2, 3]";
        String input2 = "1 2 3";

        autoParse(input1);
        autoParse(input2);
    }

    private void autoParse(String input) {
        // 将所有非数字和非字母（且不是负号）的内容替换为空格，然后 split
        // 注意：这取决于题目具体类型，但对于数字/字符串数组很有效
        String replaced = input.replaceAll("[^a-zA-Z0-9\\-,]", " ");
        String[] parts = replaced.split("[,\\s]+");
        List<String> result = Arrays.stream(parts)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        System.out.println("Auto Parsed: " + result);
    }
}
