package com.salespilot.api.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.presentation.dto.GetCompanyByIdResponseDTO;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.domain.enums.CompanyPlan;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final GetAllCompaniesUseCase getCompanyUseCase;

    private static final int MAX_PAGE_SIZE = 100;

    public CompanyController(GetCompanyByIdUseCase getCompanyByIdUseCase, GetAllCompaniesUseCase getCompanyUseCase) {
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCompanyByIdResponseDTO> getCompanyById(@PathVariable UUID id) {
        return getCompanyByIdUseCase.execute(id)
                .map(GetCompanyByIdResponseDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> getAll(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String taxId,
        @RequestParam(required = false) CompanyPlan plan,
        @RequestParam(required = false) Boolean active,
        Pageable pageable)
        {
        Pageable safePageable = normalizePageable(pageable);
        return ResponseEntity.ok(getCompanyUseCase.execute(name, taxId, plan, active, safePageable));
    }

    private Pageable normalizePageable(Pageable pageable) {
        int safePage = Math.max(pageable.getPageNumber(), 0);
        int safeSize = Math.min(Math.max(pageable.getPageSize(), 1), MAX_PAGE_SIZE);
        Sort safeSort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.unsorted();
        return PageRequest.of(safePage, safeSize, safeSort);
    }
}
