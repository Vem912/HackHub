package it.unicam.cs.ids.hackhub.application.service;

import it.unicam.cs.ids.hackhub.application.exception.InvalidSubmissionFileException;
import it.unicam.cs.ids.hackhub.domain.valueobject.SubmissionFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.zip.ZipInputStream;

/** Validates the extension, size and readability of an uploaded ZIP archive. */
public class SubmissionFileValidator {
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;
    private final long maxSize;

    public SubmissionFileValidator() {
        this(DEFAULT_MAX_SIZE);
    }

    public SubmissionFileValidator(long maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be positive");
        }
        this.maxSize = maxSize;
    }

    public void validate(SubmissionFile file) {
        if (file == null || file.fileName().isBlank()
                || !file.fileName().toLowerCase(Locale.ROOT).endsWith(".zip")) {
            throw new InvalidSubmissionFileException("The submission must be a .zip file.");
        }
        byte[] content = file.content();
        if (content.length > maxSize) {
            throw new InvalidSubmissionFileException("The submission exceeds the maximum size.");
        }
        try (var zip = new ZipInputStream(new ByteArrayInputStream(content))) {
            if (zip.getNextEntry() == null) {
                throw new InvalidSubmissionFileException("The ZIP archive is empty or invalid.");
            }
            byte[] buffer = new byte[8192];
            do {
                while (zip.read(buffer) != -1) {
                    // Reading every entry forces CRC and structural checks.
                }
            } while (zip.getNextEntry() != null);
        } catch (IOException | RuntimeException exception) {
            if (exception instanceof InvalidSubmissionFileException invalid) {
                throw invalid;
            }
            throw new InvalidSubmissionFileException("The ZIP archive is corrupted.");
        }
    }
}
