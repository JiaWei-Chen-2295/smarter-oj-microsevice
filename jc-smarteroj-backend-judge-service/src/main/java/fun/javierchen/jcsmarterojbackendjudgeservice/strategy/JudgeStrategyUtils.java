package fun.javierchen.jcsmarterojbackendjudgeservice.strategy;

import java.util.ArrayList;
import java.util.List;

public class JudgeStrategyUtils {

    /**
     * 稳健的输出比对：忽略行末空格和文末换行
     */
    public static boolean isOutputMatch(String actual, String expected) {
        if (actual == null || expected == null) {
            return actual == expected;
        }

        String[] actualLines = actual.split("\\r?\\n");
        String[] expectedLines = expected.split("\\r?\\n");

        List<String> normalizedActual = normalizeLines(actualLines);
        List<String> normalizedExpected = normalizeLines(expectedLines);

        return normalizedActual.equals(normalizedExpected);
    }

    private static List<String> normalizeLines(String[] lines) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.add(line.replaceAll("\\s+$", "")); // 去除行末空格
        }
        // 去除文末空行
        int i = result.size() - 1;
        while (i >= 0 && result.get(i).isEmpty()) {
            result.remove(i);
            i--;
        }
        return result;
    }
}
