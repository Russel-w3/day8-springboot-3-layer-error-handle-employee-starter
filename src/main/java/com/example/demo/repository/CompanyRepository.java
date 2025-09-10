package com.example.demo.repository;

import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyRepository {
    List<Company> companies = new ArrayList<>();

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= companies.size()) {
                return new ArrayList<>();
            }
            return companies.subList(start, end);
        }
        return companies;
    }

    public Company createCompany(Company company) {
        company.setId(companies.size());
        companies.add(company);
        return company;
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Company found = null;
        for (Company c : companies) {
            if (c.getId().equals(id)) {
                c.setName(updatedCompany.getName());
                return c;
            }
        }
        return null;
    }

    public Company getCompanyById(int id) {
        for (Company c : companies) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public Company deleteCompany(int id) {
        Company found = null;
        for (Company c : companies) {
            if (c.getId().equals(id)) {
                found = c;
                break;
            }
        }
        if (found != null) {
            companies.remove(found);
            return found;
        }
        return null;
    }

    public void deleteAll(){
        companies.clear();
    }

}
