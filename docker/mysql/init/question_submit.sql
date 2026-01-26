INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1, 1, 1903672737810321410, 'java', '{}', 0, '{}', '2025-03-27 16:40:02', '2025-03-27 16:40:02', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (2, 1, 1903672737810321410, 'js', '{}', 0, '{}', '2025-03-28 21:30:25', '2025-03-28 21:30:25', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (3, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码', 0, '{}', '2025-04-04 22:30:00', '2025-04-04 22:30:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (4, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码', 0, '{}', '2025-04-04 22:30:15', '2025-04-04 22:30:15', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (5, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码', 0, '{}', '2025-04-04 22:30:50', '2025-04-04 22:30:50', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (6, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码', 0, '{}', '2025-04-04 22:32:33', '2025-04-04 22:32:33', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (7, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码', 0, '{}', '2025-04-04 22:32:42', '2025-04-04 22:32:42', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (8, 1906716870829342722, 1903672737810321410, 'java', ' /*
 * @author JavierChen
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("fun.javierchen.smarteroj.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-08 08:53:19', '2025-04-08 08:53:19', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (9, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码
', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-21 22:17:16', '2025-04-21 22:17:16', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (10, 1906715718448513025, 1903672737810321410, 'java', '// 在这里编写你的代码









', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-21 22:18:08', '2025-04-21 22:18:08', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (11, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-25 13:39:19', '2025-04-25 13:39:25', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (12, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-25 13:40:42', '2025-04-25 13:40:46', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (13, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"答案错误","time":0,"memory":0}', '2025-04-25 13:43:17', '2025-04-25 13:43:19', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (14, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"答案错误","time":0,"memory":0}', '2025-04-25 14:06:17', '2025-04-25 14:06:18', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (15, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"答案错误","time":0,"memory":0}', '2025-04-25 19:14:03', '2025-04-25 19:14:07', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (16, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-26 12:05:02', '2025-04-26 12:05:09', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (17, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-26 12:19:36', '2025-04-26 12:20:41', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (18, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"内存超限","time":0,"memory":0}', '2025-04-26 12:26:02', '2025-04-26 12:26:04', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (19, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:27:37', '2025-04-26 12:27:38', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (20, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:28:25', '2025-04-26 12:28:27', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (21, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:33:09', '2025-04-26 12:33:10', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (22, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:36:13', '2025-04-26 12:36:14', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (23, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:37:35', '2025-04-26 12:37:37', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (24, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:43:27', '2025-04-26 12:43:28', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (25, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:48:32', '2025-04-26 12:48:33', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (26, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:51:15', '2025-04-26 12:51:16', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (27, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:52:38', '2025-04-26 12:52:39', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (28, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:52:54', '2025-04-26 12:52:55', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (29, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:53:01', '2025-04-26 12:53:02', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (30, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:56:46', '2025-04-26 12:56:47', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (31, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:58:44', '2025-04-26 12:58:46', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (32, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 12:59:39', '2025-04-26 12:59:40', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (33, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":0,"memory":0}', '2025-04-26 13:00:14', '2025-04-26 13:00:43', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (34, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:07:47', '2025-04-26 13:07:48', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (35, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:09:37', '2025-04-26 13:09:39', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (36, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:09:55', '2025-04-26 13:09:57', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (37, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:13:15', '2025-04-26 13:13:17', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (38, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:15:22', '2025-04-26 13:15:23', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (39, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:16:55', '2025-04-26 13:16:56', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (40, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:22:10', '2025-04-26 13:22:11', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (41, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:24:08', '2025-04-26 13:24:10', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (42, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:24:48', '2025-04-26 13:24:49', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (43, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:28:28', '2025-04-26 13:28:29', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (44, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:32:06', '2025-04-26 13:32:07', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (45, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:33:38', '2025-04-26 13:33:40', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (46, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:34:01', '2025-04-26 13:34:03', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (47, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:34:17', '2025-04-26 13:34:19', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (48, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 13:35:15', '2025-04-26 13:35:16', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (49, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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

                maxGlobal = maxCurrent + 1;
            }
        }
        
        System.out.println(maxGlobal);
    }
}', 2, '{"message":"答案错误","time":168,"memory":155504640}', '2025-04-26 13:35:33', '2025-04-26 13:35:34', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (50, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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

                maxGlobal = maxCurrent + 1;
            }
        }
        
        System.out.println(maxGlobal);
    }
}', 2, '{"message":"答案错误","time":168,"memory":155504640}', '2025-04-26 14:55:47', '2025-04-26 14:55:49', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (51, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 14:56:07', '2025-04-26 14:56:08', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (52, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":168,"memory":155504640}', '2025-04-26 15:09:06', '2025-04-26 15:09:07', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (53, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":117,"memory":1753088}', '2025-04-26 16:40:12', '2025-04-26 16:40:14', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (54, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":249,"memory":6410240}', '2025-04-26 17:31:04', '2025-04-26 17:31:07', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (55, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":179,"memory":12685312}', '2025-04-27 16:53:59', '2025-04-27 16:54:05', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (56, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":179,"memory":141123584}', '2025-04-27 16:57:12', '2025-04-27 16:57:16', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (57, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":179,"memory":143110144}', '2025-04-27 16:57:30', '2025-04-27 16:57:32', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (58, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":179,"memory":143110144}', '2025-04-27 17:06:01', '2025-04-27 17:06:03', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (59, 1915640659701104642, 1903672737810321410, 'java', '// 在这里编写你的代码








', 1, '{}', '2025-04-28 22:25:16', '2025-04-28 22:25:16', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (60, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":225,"memory":128798720}', '2025-05-05 19:59:22', '2025-05-05 19:59:29', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (61, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":399,"memory":128798720}', '2025-05-05 20:05:37', '2025-05-05 20:05:45', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (62, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":399,"memory":128798720}', '2025-05-05 20:08:52', '2025-05-05 20:08:54', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (63, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":399,"memory":128798720}', '2025-05-05 20:09:11', '2025-05-05 20:09:14', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (64, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":258,"memory":272363520}', '2025-05-07 19:08:47', '2025-05-07 19:08:55', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (65, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":700,"memory":272363520}', '2025-05-07 19:09:24', '2025-05-07 19:09:28', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (66, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":700,"memory":272363520}', '2025-05-07 19:47:33', '2025-05-07 19:47:35', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (67, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":700,"memory":272363520}', '2025-05-07 19:47:43', '2025-05-07 19:47:45', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (68, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":700,"memory":272363520}', '2025-05-07 19:53:14', '2025-05-07 19:53:18', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (69, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":700,"memory":272363520}', '2025-05-07 19:53:36', '2025-05-07 19:53:38', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (70, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2025-05-13 21:07:43', '2025-05-13 21:07:43', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (71, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2025-05-13 21:07:51', '2025-05-13 21:07:51', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (72, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":11112448}', '2025-05-14 10:50:47', '2025-05-14 10:50:51', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (73, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 10:51:20', '2025-05-14 10:51:22', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (74, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:55', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (75, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:54', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (76, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:57', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (77, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:51', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (78, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:53', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (79, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:18', '2025-05-14 11:23:58', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (80, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:19', '2025-05-14 11:24:00', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (81, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":145,"memory":137711616}', '2025-05-14 11:22:19', '2025-05-14 11:24:01', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (82, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":109,"memory":15192064}', '2025-05-16 17:39:38', '2025-05-16 17:39:43', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (83, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":496,"memory":129404928}', '2025-05-16 17:39:43', '2025-05-16 17:39:48', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (84, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
                maxGlobal = maxCurrent + 1;
            }
        }
    
        
        System.out.println(maxGlobal);
    }
}', 2, '{"message":"答案错误","time":496,"memory":255426560}', '2025-05-16 17:40:01', '2025-05-16 17:40:03', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (85, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
                maxGlobal = maxCurrent + 1;
            }
        }
    
        
        System.out.println(maxGlobal);
    }
}', 2, '{"message":"答案错误","time":496,"memory":255426560}', '2025-05-16 17:40:04', '2025-05-16 17:40:06', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (86, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2025-11-23 15:26:20', '2025-11-23 15:26:21', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (87, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"成功","time":184,"memory":7180288}', '2025-11-23 15:28:10', '2025-11-23 15:28:14', 0, '["6","-1","5","3","5"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286500, 1906, 1985166201437286403, 'java', 'public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i-1]) continue;
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    res.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left+1]) left++;
                    while (left < right && nums[right] == nums[right-1]) right--;
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return res;
    }
}', 2, '{"message":"成功","time":45,"memory":32768}', '2025-12-01 14:00:00', '2025-12-01 14:00:00', 0, '[[0,1,2],[-2,-1,2],[-2,0,1],[-1,0,1]]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286501, 1908, 1985166201437286404, 'java', 'public class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        return new int[] {-1, -1};
    }
}', 1, '{}', '2025-12-01 14:05:00', '2025-12-01 14:05:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286502, 1906715718448513025, 1985166201437286405, 'java', 'class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}', 2, '{"message":"成功","time":28,"memory":16384}', '2025-12-01 14:10:00', '2025-12-01 14:10:00', 0, 'null');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286503, 1985166201437286421, 1985166201437286406, 'java', 'class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }
}', 2, '{"message":"成功","time":32,"memory":20480}', '2025-12-01 14:15:00', '2025-12-01 14:15:00', 0, 'null');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286504, 1985166201437286422, 1985166201437286407, 'java', 'class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == \'(\' or c == \'[\' or c == \'{\') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) return false;
                char top = stack.pop();
                if ((c == \')\' && top != \'(\')
                    (c == \']\' && top != \'[\')
                    (c == \'}\' && top != \'{\')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
}', 2, '{"message":"成功","time":18,"memory":12288}', '2025-12-01 14:20:00', '2025-12-01 14:20:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286505, 1985166201437286423, 1985166201437286408, 'java', 'class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int val) { this.val = val; }
}

class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            result.add(level);
        }

        return result;
    }
}', 2, '{"message":"成功","time":42,"memory":28672}', '2025-12-01 14:25:00', '2025-12-01 14:25:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286506, 1985166201437286424, 1985166201437286409, 'java', 'class Solution {
    public int[] sortArray(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }

    private void quickSort(int[] nums, int low, int high) {
        if (low < high) {
            int pi = partition(nums, low, high);
            quickSort(nums, low, pi - 1);
            quickSort(nums, pi + 1, high);
        }
    }

    private int partition(int[] nums, int low, int high) {
        int pivot = nums[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (nums[j] <= pivot) {
                i++;
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
        }
        int temp = nums[i + 1];
        nums[i + 1] = nums[high];
        nums[high] = temp;
        return i + 1;
    }
}', 1, '{}', '2025-12-01 14:30:00', '2025-12-01 14:30:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286507, 1985166201437286425, 1985166201437286410, 'java', 'class Solution {
    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }
}', 2, '{"message":"成功","time":15,"memory":10240}', '2025-12-01 14:35:00', '2025-12-01 14:35:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286508, 1985166201437286426, 1985166201437286411, 'java', 'class Solution {
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];

        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> list = new ArrayList<>();
        list.add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            int[] last = list.get(list.size() - 1);
            if (intervals[i][0] <= last[1]) {
                last[1] = Math.max(last[1], intervals[i][1]);
            } else {
                list.add(intervals[i]);
            }
        }

        return list.toArray(new int[0][]);
    }
}', 2, '{"message":"答案错误","time":120,"memory":45056}', '2025-12-01 14:40:00', '2025-12-01 14:40:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286509, 1985166201437286427, 1985166201437286412, 'java', 'class LRUCache {
    private class Node {
        int key, value;
        Node prev, next;
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Map<Integer, Node> map;
    private Node head, tail;
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void add(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        remove(node);
        add(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            remove(map.get(key));
        }
        Node node = new Node(key, value);
        add(node);
        map.put(key, node);
        if (map.size() > capacity) {
            Node lru = tail.prev;
            remove(lru);
            map.remove(lru.key);
        }
    }
}', 2, '{"message":"成功","time":85,"memory":51200}', '2025-12-01 14:45:00', '2025-12-01 14:45:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286510, 1985166201437286428, 1985166201437286413, 'java', 'class Solution {
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == \'1\') {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }

        return count;
    }

    private void dfs(char[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;

        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == \'0\') {
            return;
        }

        grid[r][c] = \'0\';
        dfs(grid, r + 1, c);
        dfs(grid, r - 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
}', 2, '{"message":"成功","time":52,"memory":36864}', '2025-12-01 14:50:00', '2025-12-01 14:50:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286511, 1985166201437286429, 1985166201437286414, 'java', 'class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;

    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        if (!stack.isEmpty()) {
            int val = stack.pop();
            if (val == minStack.peek()) {
                minStack.pop();
            }
        }
    }

    public int top() {
        return stack.isEmpty() ? -1 : stack.peek();
    }

    public int getMin() {
        return minStack.isEmpty() ? -1 : minStack.peek();
    }
}', 2, '{"message":"成功","time":22,"memory":14336}', '2025-12-01 14:55:00', '2025-12-01 14:55:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286512, 1985166201437286430, 1985166201437286415, 'java', 'class Solution {
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
    }
}', 2, '{"message":"成功","time":38,"memory":24576}', '2025-12-01 15:00:00', '2025-12-01 15:00:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286513, 1985166201437286421, 1903672737810321410, 'python', 'class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def reverseList(head):
    prev = None
    curr = head
    while curr:
        next_temp = curr.next
        curr.next = prev
        prev = curr
        curr = next_temp
    return prev', 2, '{"message":"成功","time":18,"memory":8192}', '2025-12-01 15:05:00', '2025-12-01 15:05:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286514, 1985166201437286422, 1903672737810321410, 'python', 'def isValid(s):
    stack = []
    mapping = {")": "(", "}": "{", "]": "["}
    for char in s:
        if char in mapping:
            if not stack or stack.pop() != mapping[char]:
                return False
        else:
            stack.append(char)
    return not stack', 2, '{"message":"成功","time":12,"memory":6144}', '2025-12-01 15:10:00', '2025-12-01 15:10:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286515, 1985166201437286423, 1903672737810321410, 'python', 'from collections import deque

def levelOrder(root):
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

    return result', 2, '{"message":"成功","time":25,"memory":12288}', '2025-12-01 15:15:00', '2025-12-01 15:15:00', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286516, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:31:55', '2026-01-25 16:31:55', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286517, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:32:46', '2026-01-25 16:32:46', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286518, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:35:59', '2026-01-25 16:35:59', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286519, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:37:09', '2026-01-25 16:37:09', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286520, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:37:36', '2026-01-25 16:37:36', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286521, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 0, '{}', '2026-01-25 16:40:06', '2026-01-25 16:40:06', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286522, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 16:54:14', '2026-01-25 16:54:22', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286523, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 16:58:27', '2026-01-25 16:58:35', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286524, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:00:50', '2026-01-25 17:00:50', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286525, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:01:06', '2026-01-25 17:01:07', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286526, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:04:47', '2026-01-25 17:04:50', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286527, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:05:01', '2026-01-25 17:05:01', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286528, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:06:59', '2026-01-25 17:06:59', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286529, 1985166201437286423, 1903672737810321410, 'java', 'import java.util.*;

public class Main {

    // 二叉树节点定义
    static class TreeNode {
        String val;
        TreeNode left;
        TreeNode right;

        TreeNode(String val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行输入
        if (!sc.hasNextLine()) return;
        String line = sc.nextLine().trim();

        // 特判：输入为 null
        if (line.equals("null") || line.length() == 0) {
            return;
        }

        String[] nodes = line.split("\\\\s+");

        // 构建二叉树
        TreeNode root = buildTree(nodes);

        // 层序遍历并输出
        levelOrderPrint(root);
    }

    // 根据层序数组构建二叉树
    private static TreeNode buildTree(String[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode current = queue.poll();

            // 左子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.left = new TreeNode(nodes[index]);
                queue.offer(current.left);
            }
            index++;

            // 右子节点
            if (index < nodes.length && !nodes[index].equals("null")) {
                current.right = new TreeNode(nodes[index]);
                queue.offer(current.right);
            }
            index++;
        }

        return root;
    }

    // 层序遍历并按层输出
    private static void levelOrderPrint(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size(); // 当前层节点数
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                sb.append(node.val);

                if (i < size - 1) sb.append(" ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            System.out.println(sb.toString());
        }
    }
}
', 1, '{}', '2026-01-25 17:07:04', '2026-01-25 17:07:05', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286530, 1985166201437286425, 1903672737810321410, 'java', 'import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int target = sc.nextInt();

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // 防止溢出

            if (nums[mid] == target) {
                System.out.println(mid);
                return;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // 没找到
        System.out.println(-1);
    }
}
', 1, '{}', '2026-01-25 17:11:26', '2026-01-25 17:11:27', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286531, 1985166201437286425, 1903672737810321410, 'java', 'import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int target = sc.nextInt();

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = sc.nextInt();
        }

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // 防止溢出

            if (nums[mid] == target) {
                System.out.println(mid);
                return;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // 没找到
        System.out.println(-1);
    }
}
', 1, '{}', '2026-01-25 17:11:31', '2026-01-25 17:11:31', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286532, 1985166201437286425, 1903672737810321410, 'java', 'import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行
        String[] parts = sc.nextLine().trim().split("\\\\s+");

        int n = Integer.parseInt(parts[0]);
        int target = Integer.parseInt(parts[1]);

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(parts[i + 2]);
        }

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                System.out.println(mid);
                return;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        System.out.println(-1);
    }
}
', 1, '{}', '2026-01-25 17:13:26', '2026-01-25 17:13:27', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286533, 1985166201437286425, 1903672737810321410, 'java', 'import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 读取整行
        String[] parts = sc.nextLine().trim().split("\\\\s+");

        int n = Integer.parseInt(parts[0]);
        int target = Integer.parseInt(parts[1]);

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = Integer.parseInt(parts[i + 2]);
        }

        int left = 0, right = n - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                System.out.println(mid);
                return;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        System.out.println(-1);
    }
}
', 1, '{}', '2026-01-25 17:13:30', '2026-01-25 17:13:30', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286534, 1907, 1903672737810321410, 'java', 'import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        long C = sc.nextLong();
        long D = sc.nextLong();

        System.out.println(C + D);
    }
}
', 1, '{}', '2026-01-25 17:15:08', '2026-01-25 17:15:08', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286535, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:17:16', '2026-01-25 17:17:16', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286536, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:17:20', '2026-01-25 17:17:20', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286537, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:23:27', '2026-01-25 17:23:28', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286538, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:26:28', '2026-01-25 17:26:28', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286539, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:28:49', '2026-01-25 17:28:49', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286540, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:36:24', '2026-01-25 17:36:24', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286541, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:42:04', '2026-01-25 17:42:04', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286542, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:43:49', '2026-01-25 17:43:50', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286543, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:45:13', '2026-01-25 17:45:14', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286544, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:48:51', '2026-01-25 17:48:51', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286545, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:51:26', '2026-01-25 17:51:26', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286546, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:54:09', '2026-01-25 17:54:09', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286547, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 1, '{}', '2026-01-25 17:59:04', '2026-01-25 17:59:04', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286548, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
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
}', 2, '{"message":"答案错误","time":265,"memory":712704}', '2026-01-25 20:50:29', '2026-01-25 20:50:36', 0, '["6","5","-1","15"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286549, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 20:53:38', '2026-01-25 20:53:38', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286550, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 20:54:19', '2026-01-25 20:54:19', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286551, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:08:43', '2026-01-25 21:08:44', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286552, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:09:42', '2026-01-25 21:09:42', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286553, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:30:12', '2026-01-25 21:30:13', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286554, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine().trim());
        st = new StringTokenizer(br.readLine());

        long cur = Long.parseLong(st.nextToken());
        long ans = cur;

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            cur = Math.max(x, cur + x);
            ans = Math.max(ans, cur);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:30:51', '2026-01-25 21:30:51', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286555, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] dp = new long[n];

        dp[0] = Long.parseLong(st.nextToken());
        long ans = dp[0];

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:43:32', '2026-01-25 21:43:33', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286556, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] dp = new long[n];

        dp[0] = Long.parseLong(st.nextToken());
        long ans = dp[0];

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:47:50', '2026-01-25 21:47:50', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286557, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] dp = new long[n];

        dp[0] = Long.parseLong(st.nextToken());
        long ans = dp[0];

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:50:01', '2026-01-25 21:50:01', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286558, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] dp = new long[n];

        dp[0] = Long.parseLong(st.nextToken());
        long ans = dp[0];

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:50:31', '2026-01-25 21:50:31', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286559, 1915640659701104642, 1903672737810321410, 'java', 'import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        long[] dp = new long[n];

        dp[0] = Long.parseLong(st.nextToken());
        long ans = dp[0];

        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(st.nextToken());
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 1, '{}', '2026-01-25 21:55:02', '2026-01-25 21:55:02', 0, null);
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286560, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
    public static void main(String[] args) {
        // 参数合法性检查
        if (args.length < 1) {
            System.out.println("Invalid input");
            return;
        }

        int n = Integer.parseInt(args[0]);

        if (args.length != n + 1) {
            System.out.println("Invalid input");
            return;
        }

        long[] dp = new long[n];

        // 第一个元素
        dp[0] = Long.parseLong(args[1]);
        long ans = dp[0];

        // DP 转移
        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(args[i + 1]);
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 2, '{"message":"答案错误","time":9997,"memory":12615680}', '2026-01-25 22:11:11', '2026-01-25 22:11:14', 0, '["6","5","-1","15"]');
INSERT INTO smarter_oj_db.question_submit (id, questionId, userId, language, code, status, judgeInfo, createTime, updateTime, isDelete, outputResult) VALUES (1985166201437286561, 1915640659701104642, 1903672737810321410, 'java', 'public class Main {
    public static void main(String[] args) {
        // 参数合法性检查
        if (args.length < 1) {
            System.out.println("Invalid input");
            return;
        }

        int n = Integer.parseInt(args[0]);

        if (args.length != n + 1) {
            System.out.println("Invalid input");
            return;
        }

        long[] dp = new long[n];

        // 第一个元素
        dp[0] = Long.parseLong(args[1]);
        long ans = dp[0];

        // DP 转移
        for (int i = 1; i < n; i++) {
            long x = Long.parseLong(args[i + 1]);
            dp[i] = Math.max(x, dp[i - 1] + x);
            ans = Math.max(ans, dp[i]);
        }

        System.out.println(ans);
    }
}
', 2, '{"message":"成功","time":237,"memory":819200}', '2026-01-25 22:22:17', '2026-01-25 22:22:32', 0, '["6","5","-1","15"]');
