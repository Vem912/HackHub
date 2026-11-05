package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.result.SubmitTeamSubmissionResult;
import it.unicam.cs.ids.hackhub.application.port.in.SubmitTeamSubmissionUseCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubmissionController.class)
@RunWith(SpringRunner.class)
public class SubmissionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmitTeamSubmissionUseCase submitUseCase;

    @Test
    public void submitsMultipartRequestDto() throws Exception {
        var hackathonId = UUID.randomUUID();
        var teamId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var submissionId = UUID.randomUUID();
        when(submitUseCase.submit(any()))
                .thenReturn(new SubmitTeamSubmissionResult(
                        submissionId,
                        teamId,
                        hackathonId,
                        "submission.zip",
                        LocalDateTime.of(2026, 5, 2, 10, 0),
                        false));

        var file = new MockMultipartFile(
                "file",
                "submission.zip",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                new byte[]{1, 2, 3});

        mockMvc.perform(multipart("/hackathons/{hackathonId}/teams/{teamId}/submission",
                                hackathonId, teamId)
                        .file(file)
                        .param("hackathonId", hackathonId.toString())
                        .param("teamId", teamId.toString())
                        .param("userId", userId.toString())
                        .param("title", "Title")
                        .param("description", "Description")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(submissionId.toString()))
                .andExpect(jsonPath("$.fileName").value("submission.zip"));

        verify(submitUseCase).submit(any());
    }

    @Test
    public void rejectsMultipartRequestWithoutFile() throws Exception {
        mockMvc.perform(multipart("/hackathons/{hackathonId}/teams/{teamId}/submission",
                                UUID.randomUUID(), UUID.randomUUID())
                        .param("userId", UUID.randomUUID().toString())
                        .param("hackathonId", UUID.randomUUID().toString())
                        .param("teamId", UUID.randomUUID().toString())
                        .param("title", "Title")
                        .param("description", "Description")
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isBadRequest());
    }
}
