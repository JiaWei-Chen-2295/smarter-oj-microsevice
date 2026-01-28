package fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox;

import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxRequest;
import fun.javierchen.jcojbackendmodel.codesandbox.CodeSandBoxResponse;
import fun.javierchen.jcojbackendmodel.dto.question.JudgeConfig;
import fun.javierchen.jcsmarterojbackendjudgeservice.codesandbox.impl.Judge0CodeSandBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class Judge0CodeSandBoxTest {

        @Resource
        private Judge0CodeSandBox judge0CodeSandBox;

        @Resource
        private SandboxConfig sandboxConfig;

        @Resource
        private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

        @Test
        void runCodeJava() {
                ReflectionTestUtils.setField(judge0CodeSandBox, "sandboxConfig", sandboxConfig);
                ReflectionTestUtils.setField(judge0CodeSandBox, "objectMapper", objectMapper);

                String code = "import java.util.Scanner;\n" +
                                "public class Main {\n" +
                                "    public static void main(String[] args) {\n" +
                                "        Scanner sc = new Scanner(System.in);\n" +
                                "        int a = sc.nextInt();\n" +
                                "        int b = sc.nextInt();\n" +
                                "        System.out.println(a + b);\n" +
                                "    }\n" +
                                "}";
                String language = "java";
                List<String> inputList = Arrays.asList("1 2", "3 4");
                JudgeConfig judgeConfig = new JudgeConfig();
                judgeConfig.setTimeLimit(2000L);
                judgeConfig.setMemoryLimit(128000L);
                judgeConfig.setStackLimit(64000L);
                CodeSandBoxRequest codeSandBoxRequest = CodeSandBoxRequest.builder()
                                .code(code)
                                .language(language)
                                .inputCase(inputList)
                                .judgeConfig(judgeConfig)
                                .build();
                CodeSandBoxResponse codeSandBoxResponse = judge0CodeSandBox.runCode(codeSandBoxRequest);
                System.out.println(codeSandBoxResponse);
                Assertions.assertNotNull(codeSandBoxResponse);
                Assertions.assertEquals(Arrays.asList("3", "7"), codeSandBoxResponse.getOutputList());
        }

        @Test
        void runCodeCpp() {
                ReflectionTestUtils.setField(judge0CodeSandBox, "sandboxConfig", sandboxConfig);
                ReflectionTestUtils.setField(judge0CodeSandBox, "objectMapper", objectMapper);

                String code = "#include <iostream>\n" +
                                "using namespace std;\n" +
                                "int main() {\n" +
                                "    int a, b;\n" +
                                "    while(cin >> a >> b) cout << a + b << endl;\n" +
                                "    return 0;\n" +
                                "}";
                String language = "cpp";
                List<String> inputList = Arrays.asList("1 2", "3 4");
                JudgeConfig judgeConfig = new JudgeConfig();
                judgeConfig.setTimeLimit(2000L);
                judgeConfig.setMemoryLimit(128000L);
                judgeConfig.setStackLimit(64000L);
                CodeSandBoxRequest codeSandBoxRequest = CodeSandBoxRequest.builder()
                                .code(code)
                                .language(language)
                                .inputCase(inputList)
                                .judgeConfig(judgeConfig)
                                .build();
                CodeSandBoxResponse codeSandBoxResponse = judge0CodeSandBox.runCode(codeSandBoxRequest);
                System.out.println(codeSandBoxResponse);
                Assertions.assertNotNull(codeSandBoxResponse);
                Assertions.assertEquals(Arrays.asList("3", "7"), codeSandBoxResponse.getOutputList());
        }

        @Test
        void runCodeJavaLinkedList() {
                ReflectionTestUtils.setField(judge0CodeSandBox, "sandboxConfig", sandboxConfig);
                ReflectionTestUtils.setField(judge0CodeSandBox, "objectMapper", objectMapper);

                String code = "import java.util.*;\n" +
                                "\n" +
                                "class ListNode {\n" +
                                "    int val;\n" +
                                "    ListNode next;\n" +
                                "    ListNode(int val) { this.val = val; }\n" +
                                "}\n" +
                                "\n" +
                                "public class Main {\n" +
                                "    public static void main(String[] args) {\n" +
                                "        Scanner sc = new Scanner(System.in);\n" +
                                "        if (!sc.hasNextInt()) return;\n" +
                                "        int n = sc.nextInt();\n" +
                                "        if (n == 0) return;\n" +
                                "\n" +
                                "        ListNode dummy = new ListNode(0);\n" +
                                "        ListNode tail = dummy;\n" +
                                "        for (int i = 0; i < n; i++) {\n" +
                                "            tail.next = new ListNode(sc.nextInt());\n" +
                                "            tail = tail.next;\n" +
                                "        }\n" +
                                "\n" +
                                "        ListNode head = dummy.next;\n" +
                                "        ListNode prev = null;\n" +
                                "        ListNode curr = head;\n" +
                                "        while (curr != null) {\n" +
                                "            ListNode nextTemp = curr.next;\n" +
                                "            curr.next = prev;\n" +
                                "            prev = curr;\n" +
                                "            curr = nextTemp;\n" +
                                "        }\n" +
                                "\n" +
                                "        ListNode temp = prev;\n" +
                                "        while (temp != null) {\n" +
                                "            System.out.print(temp.val + (temp.next != null ? \" \" : \"\"));\n" +
                                "            temp = temp.next;\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";
                String language = "java";
                List<String> inputList = Arrays.asList("5 1 2 3 4 5", "3 10 20 30");
                JudgeConfig judgeConfig = new JudgeConfig();
                judgeConfig.setTimeLimit(2000L);
                judgeConfig.setMemoryLimit(128000L);
                judgeConfig.setStackLimit(64000L);
                CodeSandBoxRequest codeSandBoxRequest = CodeSandBoxRequest.builder()
                                .code(code)
                                .language(language)
                                .inputCase(inputList)
                                .judgeConfig(judgeConfig)
                                .build();
                CodeSandBoxResponse codeSandBoxResponse = judge0CodeSandBox.runCode(codeSandBoxRequest);
                System.out.println(codeSandBoxResponse);
                Assertions.assertNotNull(codeSandBoxResponse);
                Assertions.assertEquals(Arrays.asList("5 4 3 2 1", "30 20 10"), codeSandBoxResponse.getOutputList());
        }
}
