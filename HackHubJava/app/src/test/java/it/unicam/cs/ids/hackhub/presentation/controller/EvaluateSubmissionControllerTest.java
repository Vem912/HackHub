package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.result.EvaluationResult;
import it.unicam.cs.ids.hackhub.application.port.in.EvaluateSubmissionUseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EvaluateSubmissionController.class)
@RunWith(SpringRunner.class)
public class EvaluateSubmissionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvaluateSubmissionUseCase evaluateUseCase;

    @Test
    public void evaluatesSubmissionUsingPathAndBodyData() throws Exception {
        var submissionId = UUID.randomUUID();
        var judgeId = UUID.randomUUID();
        var evaluationId = UUID.randomUUID();
        when(evaluateUseCase.evaluate(any()))
                .thenReturn(new EvaluationResult(
                        evaluationId, submissionId, judgeId, 8.5, "Good solution."));

        mockMvc.perform(post("/submissions/{submissionId}/evaluation", submissionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"judgeId":"%s","score":8.5,"judgment":"Good solution."}
                                """.formatted(judgeId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.evaluationId").value(evaluationId.toString()))
                .andExpect(jsonPath("$.submissionId").value(submissionId.toString()))
                .andExpect(jsonPath("$.judgeId").value(judgeId.toString()))
                .andExpect(jsonPath("$.score").value(8.5))
                .andExpect(jsonPath("$.judgment").value("Good solution."));

        verify(evaluateUseCase).evaluate(any());
    }

    @Test
    public void rejectsInvalidScore() throws Exception {
        mockMvc.perform(post("/submissions/{submissionId}/evaluation", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"judgeId":"%s","score":11,"judgment":"Invalid."}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void rejectsMissingJudgeAndJudgment() throws Exception {
        mockMvc.perform(post("/submissions/{submissionId}/evaluation", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"score":8}
                                """))
                .andExpect(status().isBadRequest());
    }
}
