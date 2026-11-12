package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.CompleteHackathonEvaluationCommand;

public interface CompleteHackathonEvaluationUseCase {
    void completeEvaluation(CompleteHackathonEvaluationCommand command);
}
