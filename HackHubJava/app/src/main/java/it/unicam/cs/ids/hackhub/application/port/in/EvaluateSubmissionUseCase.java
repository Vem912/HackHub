package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.EvaluateSubmissionCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.EvaluateSubmissionResult;

/** Inbound application port for evaluating a submission. */
public interface EvaluateSubmissionUseCase {
    EvaluateSubmissionResult evaluate(EvaluateSubmissionCommand command);
}
