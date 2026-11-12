package it.unicam.cs.ids.hackhub.application.port.in;

import it.unicam.cs.ids.hackhub.application.dto.command.ListHackathonEvaluationsCommand;
import it.unicam.cs.ids.hackhub.application.dto.result.EvaluationResult;

import java.util.List;

public interface ListHackathonEvaluationsUseCase {
    List<EvaluationResult> list(ListHackathonEvaluationsCommand command);
}
