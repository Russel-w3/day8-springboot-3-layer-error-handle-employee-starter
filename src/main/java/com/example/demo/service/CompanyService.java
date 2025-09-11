package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page - 1, size);
            return companyRepository.findAll(pageable).stream().toList();
        }
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(int id, Company updatedCompany) {
        Optional<Company> companyById = companyRepository.findById(id);
        if (companyById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        updatedCompany.setId(id);
        return companyRepository.save(updatedCompany);
    }

    public Company getCompanyById(int id) {
        Optional<Company> companyById = companyRepository.findById(id);
        if (companyById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return companyById.get();
    }

    public void deleteCompany(int id) {
        Optional<Company> companyById = companyRepository.findById(id);
        if (companyById.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        companyRepository.delete(companyById.get());
    }

//    public void deleteAll() {
//        companyRepository.deleteAll();
//    }
}
