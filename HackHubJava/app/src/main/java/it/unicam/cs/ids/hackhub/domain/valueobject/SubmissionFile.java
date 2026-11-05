package it.unicam.cs.ids.hackhub.domain.valueobject;

import java.util.Arrays;
import java.util.Objects;

/** Immutable upload received for a team submission. */
public record SubmissionFile(String fileName, byte[] content) {
    public SubmissionFile {
        Objects.requireNonNull(fileName, "fileName cannot be null");
        Objects.requireNonNull(content, "content cannot be null");
        content = Arrays.copyOf(content, content.length);
    }

    @Override
    public byte[] content() {
        return Arrays.copyOf(content, content.length);
    }
}
