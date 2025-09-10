package com.example.demo;

import com.example.demo.entity.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanEmployees() throws Exception {
        mockMvc.perform(delete("/employees/delete"));
    }

    private void createJohnSmith() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee(21, "MALE", null, "John Smith", 6000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(john));
    }

    private void createJaneDoe() throws Exception {
        Gson gson = new Gson();
        String jane = gson.toJson(new Employee(22, "FEMALE", null, "Jane Doe", 6000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(jane));
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_active_employee_list_when_has_unactivated_employee() throws Exception {
        createJohnSmith();
        createJaneDoe();
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(6000.0));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        createJohnSmith();
        createJaneDoe();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(21))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(6000.0));
    }

    @Test
    void should_create_employee() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee(21, "MALE", null, "John Smith", 6000.0));
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(john))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(21))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.salary").value(6000.0))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(21))
                .andExpect(jsonPath("$[0].gender").value("MALE"))
                .andExpect(jsonPath("$[0].salary").value(6000.0));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        createJohnSmith();

        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        createJohnSmith();
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 29,
                            "gender": "MALE",
                            "salary": 65000.0
                        }
                """;

        mockMvc.perform(put("/employees/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_page_with_active_employee_when_get_page() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(delete("/employees/" + 2))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(3));
    }

    @Test
    void should_throw_exception_when_create_employee_with_age_less_than_18_or_greater_65() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee(16, "MALE", null, "John Smith", 6000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(john))
                .andExpect(jsonPath("$.message").value("employee age less than 18 or greater than 65!"));
    }

    @Test
    void should_throw_exception_when_create_employee_with_age_greater_than_or_equals_30_and_salary_less_than_20000() throws Exception {
        Gson gson = new Gson();
        String john = gson.toJson(new Employee(30, "MALE", null, "John Smith", 8000.0));
        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(john))
                .andExpect(jsonPath("$.message").value("employee age greater than or equal 30 and salary less than 20000!"));
    }

    @Test
    void should_throw_exception_when_update_an_unactivated_employee() throws Exception {
        createJohnSmith();
        mockMvc.perform(delete("/employees/" + 1))
                .andExpect(status().isNoContent());
        String requestBody = """
                        {
                            "name": "John Smith",
                            "age": 29,
                            "gender": "MALE",
                            "salary": 65000.0
                        }
                """;

        mockMvc.perform(put("/employees/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(jsonPath("$.message").value("employee has left the company!"));
    }
}
