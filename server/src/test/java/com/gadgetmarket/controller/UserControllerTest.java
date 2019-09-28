package com.gadgetmarket.controller;

import com.gadgetmarket.GadgetMarketApplication;
import com.gadgetmarket.repository.UserRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DatabaseSetup(value = "classpath:dataset/dataset.xml")
@AutoConfigureMockMvc
@SpringBootTest(classes = GadgetMarketApplication.class)
@Ignore
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Test
    public void checkLoadUserController() {
        assertThat(userController).isNotNull();
    }


    // POST
    @Test
    @ExpectedDatabase("classpath:dataset/datasetAfterPost.xml")
    public void postRequestTest() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead4\"," +
                        "\"password\": \"SuttyRead4\"," +
                        "\"confirmPassword\": \"SuttyRead4\"," +
                        "\"email\": \"SuttyRead4@gmail.com\"," +
                        "\"firstName\": \"Sutty\"," +
                        "\"lastName\": \"Read\"," +
                        "\"birthday\": \"1980-10-10\"}"))
                .andDo(print())
                .andExpect(content().json("{\"id\":4," +
                        "\"username\":\"SuttyRead4\"," +
                        "\"password\":\"SuttyRead4\"," +
                        "\"email\":\"SuttyRead4@gmail.com\"," +
                        "\"firstName\":\"Sutty\"," +
                        "\"lastName\":\"Read\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"}"))
                .andExpect(status().isCreated());
        System.out.println(userRepository.findAll());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void postRequestWithAlreadyExistUsernameTest() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead1\"," +
                        "\"password\": \"SuttyRead4\"," +
                        "\"confirmPassword\": \"SuttyRead4\"," +
                        "\"email\": \"SuttyRead4@gmail.com\"," +
                        "\"firstName\": \"Sutty\"," +
                        "\"lastName\": \"Read\"," +
                        "\"birthday\": \"1980-10-10\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void postRequestWithAlreadyExistEmailTest() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead4\"," +
                        "\"password\": \"SuttyRead4\"," +
                        "\"confirmPassword\": \"SuttyRead4\"," +
                        "\"email\": \"SuttyRead1@gmail.com\"," +
                        "\"firstName\": \"Sutty\"," +
                        "\"lastName\": \"Read\"," +
                        "\"birthday\": \"1980-10-10\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void postRequestWithDontMatchPasswordTest() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead4\"," +
                        "\"password\": \"SuttyRead14\"," +
                        "\"confirmPassword\": \"SuttyRead4\"," +
                        "\"email\": \"SuttyRead1@gmail.com\"," +
                        "\"firstName\": \"Sutty\"," +
                        "\"lastName\": \"Read\"," +
                        "\"birthday\": \"1980-10-10\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void postRequestWithIncorrectBirthdayTest() throws Exception {
        this.mockMvc.perform(post("/users")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead4\"," +
                        "\"password\": \"SuttyRead14\"," +
                        "\"confirmPassword\": \"SuttyRead4\"," +
                        "\"email\": \"SuttyRead1@gmail.com\"," +
                        "\"firstName\": \"Sutty\"," +
                        "\"lastName\": \"Read\"," +
                        "\"birthday\": \"2020-10-10\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void postRequestWithEmptyBodyTest() throws Exception {
        this.mockMvc.perform(post("/users"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    // DELETE
    @Test
    @ExpectedDatabase(value = "classpath:dataset/datasetAfterDelete.xml")
    public void deleteRequestTest() throws Exception {
        this.mockMvc.perform(delete("/users/3"))
                .andDo(print())
                .andExpect(status().isNoContent());
        System.out.println(userRepository.findAll());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dataset/dataset.xml")
    public void deleteRequestWithUnknownIdTest() throws Exception {
        this.mockMvc.perform(delete("/users/4"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @ExpectedDatabase(value = "classpath:dataset/dataset.xml")
    public void deleteRequestWithInvalidIdTest() throws Exception {
        this.mockMvc.perform(delete("/users/nix"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    // PUT
    @Test
    @ExpectedDatabase("classpath:dataset/datasetAfterPut.xml")
    public void putRequestTest() throws Exception {
        this.mockMvc.perform(put("/users/3")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead3\"," +
                        "\"password\": \"SuttyRead3\"," +
                        "\"confirmPassword\": \"SuttyRead3\"," +
                        "\"email\": \"SuttyRead3@gmail.com\"," +
                        "\"firstName\": \"Suttyqqq\"," +
                        "\"lastName\": \"Readqqq\"," +
                        "\"birthday\": \"1980-10-10\"," +
                        "\"role\": \"USER\"}"))
                .andDo(print())
                .andExpect(content().json("{\"id\":3," +
                        "\"username\":\"SuttyRead3\"," +
                        "\"password\":\"SuttyRead3\"," +
                        "\"email\":\"SuttyRead3@gmail.com\"," +
                        "\"firstName\":\"Suttyqqq\"," +
                        "\"lastName\":\"Readqqq\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"}"))
                .andExpect(status().isOk());
        System.out.println(userRepository.findAll());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void putRequestWithEmptyBodyTest() throws Exception {
        this.mockMvc.perform(put("/users/3"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void putRequestWithUnknownIdTest() throws Exception {
        this.mockMvc.perform(put("/users/10")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead3\"," +
                        "\"password\": \"SuttyRead3\"," +
                        "\"confirmPassword\": \"SuttyRead3\"," +
                        "\"email\": \"SuttyRead311@gmail.com\"," +
                        "\"firstName\": \"Suttyqqq\"," +
                        "\"lastName\": \"Readqqq\"," +
                        "\"birthday\": \"1980-10-10\"," +
                        "\"role\": \"USER\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @ExpectedDatabase("classpath:dataset/dataset.xml")
    public void putRequestWithIncorrectBirthdayTest() throws Exception {
        this.mockMvc.perform(put("/users/3")
                .contentType("application/json")
                .content("{\"username\": \"SuttyRead3\"," +
                        "\"password\": \"SuttyRead3\"," +
                        "\"confirmPassword\": \"SuttyRead3\"," +
                        "\"email\": \"SuttyRead3@gmail.com\"," +
                        "\"firstName\": \"Suttyqqq\"," +
                        "\"lastName\": \"Readqqq\"," +
                        "\"birthday\": \"2020-10-10\"," +
                        "\"role\": \"USER\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    //GET
    @Test
    public void getRequestTest() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1," +
                        "\"username\":\"SuttyRead1\"," +
                        "\"password\":\"SuttyRead1\"," +
                        "\"email\":\"SuttyRead1@gmail.com\"," +
                        "\"firstName\":\"Sutty\"," +
                        "\"lastName\":\"Read\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"}," +
                        "{\"id\":2," +
                        "\"username\":\"SuttyRead2\"," +
                        "\"password\":\"SuttyRead2\"," +
                        "\"email\":\"SuttyRead2@gmail.com\"," +
                        "\"firstName\":\"Sutty\"," +
                        "\"lastName\":\"Read\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"}," +
                        "{\"id\":3," +
                        "\"username\":\"SuttyRead3\"," +
                        "\"password\":\"SuttyRead3\"," +
                        "\"email\":\"SuttyRead3@gmail.com\"," +
                        "\"firstName\":\"Sutty\"," +
                        "\"lastName\":\"Read\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"}" +
                        "]"));
    }

    @Test
    @DatabaseSetup(type = DatabaseOperation.DELETE_ALL)
    public void getRequestIfUsersNotExistsTest() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void getOneUserRequestTest() throws Exception {
        this.mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1," +
                        "\"username\":\"SuttyRead1\"," +
                        "\"password\":\"SuttyRead1\"," +
                        "\"email\":\"SuttyRead1@gmail.com\"," +
                        "\"firstName\":\"Sutty\"," +
                        "\"lastName\":\"Read\"," +
                        "\"birthday\":\"1980-10-10\"," +
                        "\"role\":\"USER\"},"));
    }

    @Test
    public void getOneUserRequestWithUnknownIdTest() throws Exception {
        this.mockMvc.perform(get("/users/10"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOneUserRequestWithInvalidIdTest() throws Exception {
        this.mockMvc.perform(get("/users/nix"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOneUserRequestWithNullIdTest() throws Exception {
        this.mockMvc.perform(get("/users/%"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
