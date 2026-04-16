import java.util.List;

public final class CommitConventions {
    public static final List<String> VALID_TYPES = List.of(
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

    private CommitConventions() {
    }
}