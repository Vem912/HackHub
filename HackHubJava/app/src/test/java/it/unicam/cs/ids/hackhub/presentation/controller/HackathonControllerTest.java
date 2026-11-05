package it.unicam.cs.ids.hackhub.presentation.controller;

import it.unicam.cs.ids.hackhub.application.dto.result.RegisterTeamToHackathonResult;
import it.unicam.cs.ids.hackhub.application.port.in.CreateHackathonUseCase;
import it.unicam.cs.ids.hackhub.application.port.in.RegisterTeamToHackathonUseCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HackathonController.class)
@RunWith(SpringRunner.class)
public class HackathonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateHackathonUseCase createHackathonUseCase;

    @MockBean
    private RegisterTeamToHackathonUseCase registerTeamToHackathonUseCase;

    @Test
    public void registersTeamFromRequestDto() throws Exception {
        var hackathonId = UUID.randomUUID();
        var teamId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        when(registerTeamToHackathonUseCase.register(any()))
                .thenReturn(new RegisterTeamToHackathonResult(teamId, hackathonId));

        mockMvc.perform(post("/hackathons/{hackathonId}/teams", hackathonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":"%s","teamId":"%s","hackathonId":"%s"}
                                """.formatted(userId, teamId, hackathonId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamId").value(teamId.toString()))
                .andExpect(jsonPath("$.hackathonId").value(hackathonId.toString()));

        verify(registerTeamToHackathonUseCase).register(any());
    }

    @Test
    public void rejectsInvalidRegistrationRequest() throws Exception {
        mockMvc.perform(post("/hackathons/{hackathonId}/teams", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":"","teamId":"","hackathonId":""}
                                """))
                .andExpect(status().isBadRequest());
    }
}
