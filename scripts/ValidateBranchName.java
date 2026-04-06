import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateBranchName {
    private static final List<String> VALID_TYPES = List.of(
            "feat",
            "fix",
            "refactor",
            "style",
            "test",
            "build",
            "perf",
            "ci",
            "revert",
            "hotfix"
    );

    private static final List<String> SPECIAL_BRANCHES = List.of("main", "development", "staging");
    private static final List<String> PROTECTED_PATTERNS = List.of("release/", "hotfix/");

    public static void main(String[] args) {
        boolean shouldFail = false;
        for (String arg : args) {
            if ("--strict".equals(arg)) {
                shouldFail = true;
                break;
            }
        }

        try {
            String branchName = getCurrentBranch();

            if (SPECIAL_BRANCHES.contains(branchName)) {
                System.out.println("Branch \"" + branchName + "\" is a protected branch");
                System.exit(0);
            }

            for (String pattern : PROTECTED_PATTERNS) {
                if (branchName.startsWith(pattern)) {
                    System.out.println("Branch \"" + branchName + "\" matches protected pattern");
                    System.exit(0);
                }
            }

            String regex = "^(" + String.join("|", VALID_TYPES) + ")/(\\d+|no-ref)/.+$";
            if (!branchName.matches(regex)) {
                if (shouldFail) {
                    printInvalidMessage(branchName, true);
                    System.exit(1);
                }

                printInvalidMessage(branchName, false);
                System.exit(0);
            }

            Matcher typeMatcher = Pattern.compile("^([^/]+)/").matcher(branchName);
            String type = typeMatcher.find() ? typeMatcher.group(1) : "no-ref";

            Matcher issueMatcher = Pattern.compile("^[^/]+/([^/]+)/").matcher(branchName);
            String issue = issueMatcher.find() ? issueMatcher.group(1) : "no-ref";

            System.out.println("Branch name is valid");
            System.out.println("Type: " + type);
            System.out.println("Issue: " + issue);
            System.out.println("Name: " + branchName);
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Error validating branch name: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static String getCurrentBranch() throws Exception {
        Process process = new ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD").start();
        int exitCode = process.waitFor();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
        );
        String branch = reader.readLine();

        if (exitCode != 0 || branch == null || branch.isBlank()) {
            BufferedReader errReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)
            );
            String error = errReader.readLine();
            throw new RuntimeException(error == null ? "Could not determine current branch" : error);
        }

        return branch.trim();
    }

    private static void printInvalidMessage(String branchName, boolean strict) {
        printLine("\nInvalid branch name format\n", strict);
        printLine("Expected format: <type>/<issue-number>/<short-description>", strict);
        printLine("Use \"no-ref\" for issue-number if no issue is applicable\n", strict);
        printLine("Valid types: " + String.join(", ", VALID_TYPES) + "\n", strict);
        printLine("Examples:", strict);
        printLine("  feat/1234/add-user-authentication", strict);
        printLine("  fix/5678/sidebar-alignment-issue", strict);
        printLine("  hotfix/3456/critical-bug-fix", strict);
        printLine("  refactor/no-ref/simplify-form-logic\n", strict);
        printLine("Your branch: \"" + branchName + "\"\n", strict);

        if (strict) {
            printLine("Commit/push blocked: Please rename your branch to match the pattern.\n", true);
            printLine("Rename with: git branch -m <type>/<issue>/<description>\n", true);
            return;
        }

        printLine("WARNING: This branch name does not follow the naming convention.\n", false);
        printLine("Commits will be blocked until you rename it.\n", false);
        printLine("Rename with: git branch -m <type>/<issue>/<description>\n", false);
    }

    private static void printLine(String text, boolean error) {
        if (error) {
            System.err.println(text);
            return;
        }

        System.out.println(text);
    }
}
