package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.result.EvaluationResult;
import it.unicam.cs.ids.hackhub.application.port.in.CompleteHackathonEvaluationUseCase;
import it.unicam.cs.ids.hackhub.application.port.in.ListHackathonEvaluationsUseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HackathonEvaluationController.class)
@RunWith(SpringRunner.class)
public class HackathonEvaluationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompleteHackathonEvaluationUseCase completeEvaluationUseCase;

    @MockBean
    private ListHackathonEvaluationsUseCase listEvaluationsUseCase;

    @Test
    public void listsEvaluationsForOrganizer() throws Exception {
        var hackathonId = UUID.randomUUID();
        var evaluationId = UUID.randomUUID();
        var submissionId = UUID.randomUUID();
        var judgeId = UUID.randomUUID();
        var organizerId = UUID.randomUUID();
        when(listEvaluationsUseCase.list(any()))
                .thenReturn(List.of(new EvaluationResult(
                        evaluationId, submissionId, judgeId, BigDecimal.valueOf(9.5), "Excellent.")));

        mockMvc.perform(get("/hackathons/{hackathonId}/evaluation", hackathonId)
                        .param("organizerId", organizerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].evaluationId").value(evaluationId.toString()))
                .andExpect(jsonPath("$[0].submissionId").value(submissionId.toString()))
                .andExpect(jsonPath("$[0].judgeId").value(judgeId.toString()))
                .andExpect(jsonPath("$[0].score").value(9.5))
                .andExpect(jsonPath("$[0].judgment").value("Excellent."));

        verify(listEvaluationsUseCase).list(any());
    }

    @Test
    public void rejectsMissingOrganizerId() throws Exception {
        mockMvc.perform(get("/hackathons/{hackathonId}/evaluation", UUID.randomUUID()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void completesEvaluationUsingPathAndBodyIdentifiers() throws Exception {
        var hackathonId = UUID.randomUUID();
        var judgeId = UUID.randomUUID();

        mockMvc.perform(post("/hackathons/{hackathonId}/evaluation/complete", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"judgeId":"%s"}
                                """.formatted(judgeId)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(completeEvaluationUseCase).completeEvaluation(any());
    }

    @Test
    public void rejectsCompletionWithoutJudgeId() throws Exception {
        mockMvc.perform(post("/hackathons/{hackathonId}/evaluation/complete", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
