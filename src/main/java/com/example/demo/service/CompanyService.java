package com.example.demo.service;

import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
//
//    public Company updateCompany(int id, Company updatedCompany) {
//        Company companyById = companyRepository.getCompanyById(id);
//        if (companyById == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
//        }
//        return companyRepository.updateCompany(id, updatedCompany);
//    }
//
//    public Company getCompanyById(int id) {
//        Company companyById = companyRepository.getCompanyById(id);
//        if (companyById == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
//        }
//        return companyById;
//    }
//
//    public void deleteCompany(int id) {
//        Company companyById = companyRepository.getCompanyById(id);
//        if (companyById == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
//        }
//        companyRepository.deleteCompany(id);
//    }
//
//    public void deleteAll() {
//        companyRepository.deleteAll();
//    }
}
