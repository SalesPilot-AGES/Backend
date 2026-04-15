package com.salespilot.api.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import com.salespilot.api.application.dto.CompanyResponseDTO;
import com.salespilot.api.presentation.dto.CompanyRequestDTO;
import com.salespilot.api.presentation.dto.UpdateCompanyRequestDTO;
import com.salespilot.api.application.usecase.PostCompanyUseCase;
import com.salespilot.api.application.usecase.GetCompanyByIdUseCase;
import com.salespilot.api.application.usecase.GetAllCompaniesUseCase;
import com.salespilot.api.application.usecase.UpdateCompanyUseCase;
import com.salespilot.api.domain.enums.CompanyPlan;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final PostCompanyUseCase postCompanyUseCase;
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final GetAllCompaniesUseCase getCompanyUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;

    private static final int MAX_PAGE_SIZE = 100;

    public CompanyController(PostCompanyUseCase postCompanyUseCase,
                             GetCompanyByIdUseCase getCompanyByIdUseCase,
                             GetAllCompaniesUseCase getCompanyUseCase,
                             UpdateCompanyUseCase updateCompanyUseCase) {
        this.postCompanyUseCase = postCompanyUseCase;
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
    }
    
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO request) {
        CompanyResponseDTO response = postCompanyUseCase.create(request.name(), request.taxId(), request.plan(), request.active());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable UUID id) {
        return getCompanyByIdUseCase.execute(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequestDTO request
    ) {
        CompanyResponseDTO company = updateCompanyUseCase.execute(
                id,
                request.name(),
                request.plan(),
                request.active()
        );

        return ResponseEntity.ok(company);
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
