-- 为question表添加代码模板字段（使用驼峰命名保持与原表风格一致）
ALTER TABLE question ADD COLUMN codeTemplate TEXT COMMENT '代码模板(JSON对象)';

-- 为现有题目添加默认代码模板
UPDATE question 
SET codeTemplate = '{"java":"import java.util.*;\\n\\npublic class Main {\\n    public static void main(String[] args) {\\n        Scanner sc = new Scanner(System.in);\\n        // TODO: 在此编写你的代码\\n        \\n    }\\n}","cpp":"#include <iostream>\\nusing namespace std;\\n\\nint main() {\\n    // TODO: 在此编写你的代码\\n    \\n    return 0;\\n}","python":"# TODO: 在此编写你的代码\\n"}'
WHERE codeTemplate IS NULL OR codeTemplate = '';
