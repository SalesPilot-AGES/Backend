import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ValidateCommitMsg {
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

    public static void main(String[] args) {
        String commitMsgFile = args.length > 0 ? args[0] : ".git/COMMIT_EDITMSG";

        try {
            String commitMsg = readCommitMessage(commitMsgFile);
            if (commitMsg.isBlank()) {
                System.err.println("Commit message is empty");
                System.exit(1);
            }

            String firstLine = commitMsg.split("\\R", 2)[0];
            String regex = "^(" + String.join("|", VALID_TYPES) + "):\\s+.+";

            if (!firstLine.matches(regex)) {
                System.err.println("Invalid commit message format\n");
                System.err.println("Expected format: <type>: <description>");
                System.err.println("Valid types: " + String.join(", ", VALID_TYPES) + "\n");
                System.err.println("Example: \"feat: add user authentication modal\"\n");
                System.err.println("Your message: \"" + firstLine + "\"\n");
                System.exit(1);
            }

            String[] parts = firstLine.split(":", 2);
            String type = parts[0].trim();
            String description = parts.length > 1 ? parts[1].trim() : "";

            if (description.isEmpty()) {
                System.err.println("Commit message must include a description after the type");
                System.exit(1);
            }

            System.out.println("Commit message is valid");
            System.out.println("Type: " + type);
            System.out.println("Desc: " + description);
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Error reading commit message: " + ex.getMessage());
            System.exit(1);
        }
    }

    private static String readCommitMessage(String commitMsgFile) throws IOException {
        return Files.readAllLines(Path.of(commitMsgFile), StandardCharsets.UTF_8)
                .stream()
                .map(String::trim)
                .filter(line -> !line.startsWith("#"))
                .collect(Collectors.joining("\n"))
                .trim();
    }
}
