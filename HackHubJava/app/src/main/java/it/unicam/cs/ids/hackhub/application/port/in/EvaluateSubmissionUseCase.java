package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.EvaluationResult;

/** Inbound application port for evaluating a submission. */
public interface EvaluateSubmissionUseCase {
    EvaluationResult evaluate(EvaluateSubmissionCommand command);
}
