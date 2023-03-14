package com.app.testproject;

import com.app.testproject.dto.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestProjectApplicationTests {

    private static final List<UserRequest> userRequests = List.of(
            new UserRequest("Boris", "Johnson", "01.01.1991"),
            new UserRequest("Alex", "Muller", "01.01.2025"),
            new UserRequest("John", "Doe", "01.01.1978"),
            new UserRequest("John", "Doe", "12.11.2021"));
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void addAll() {
        userRequests.forEach(userRequest -> {
            try {
                mvc.perform(post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRequest))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testGet() throws Exception {
        AtomicInteger id = new AtomicInteger(1);
        userRequests.forEach(userRequest -> {
            try {
                mvc.perform(get("/api/user/" + id.getAndIncrement() + "/")
                                .contentType(MediaType.APPLICATION_JSON))

                        .andExpect(jsonPath("$.name").value(userRequest.getName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        mvc.perform(get("/api/user/9999999/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPost() throws Exception {

        UserRequest userRequest = new UserRequest("Name", "Surname", "03.07.1996");

        mvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Name"));
    }

}
