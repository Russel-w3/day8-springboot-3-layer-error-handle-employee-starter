package com.example.demo;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import com.example.demo.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private ICompanyRepository companyRepository;

    @Test
    void should_return_employee_when_create_an_employee() {
        Company company = new Company(null,"oracle");
        when(companyRepository.save(any())).thenReturn(company);
        Company companyResult = companyService.createCompany(company);
        assertEquals(company.getName(), companyResult.getName());
    }
}
