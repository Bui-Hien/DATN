package com.buihien.datn.service.impl;

import com.buihien.datn.domain.*;
import com.buihien.datn.dto.SalaryResultItemDetailDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.StaffSalaryTemplateHistoryRepository;
import com.buihien.datn.service.SalaryResultItemDetailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryResultItemDetailServiceImpl extends GenericServiceImpl<SalaryResultItemDetail, SalaryResultItemDetailDto, SearchDto> implements SalaryResultItemDetailService {
    private final StaffSalaryTemplateHistoryRepository staffSalaryTemplateHistoryRepository;

    public SalaryResultItemDetailServiceImpl(StaffSalaryTemplateHistoryRepository staffSalaryTemplateHistoryRepository) {
        super();
        this.staffSalaryTemplateHistoryRepository = staffSalaryTemplateHistoryRepository;
    }

    @Override
    protected SalaryResultItemDetailDto convertToDto(SalaryResultItemDetail entity) {
        return new SalaryResultItemDetailDto(entity, true, false);
    }

    @Override
    protected SalaryResultItemDetail convertToEntity(SalaryResultItemDetailDto dto) {
        return null;
    }

    @Override
    public Page<SalaryResultItemDetailDto> pagingSearch(SearchDto dto) {
        return new PageImpl<>(null);
    }

    @Override
    public void handleSetUpSalaryResultItemDetailByStaff(SalaryResultItem salaryResultItem, Staff staff) {
        if (salaryResultItem == null) {
            throw new ResourceNotFoundException("Không tìm thấy bảng lương của nhân viên này");
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên");
        }
        StaffSalaryTemplateHistory staffSalaryTemplateHistory = null;
        if (staff.getId() != null) {
            List<StaffSalaryTemplateHistory> staffSalaryTemplateHistoryList = staffSalaryTemplateHistoryRepository.findStaffSalaryTemplateHistoryByStaffIdAndIsCurrent(staff.getId());
            if (staffSalaryTemplateHistoryList != null && !staffSalaryTemplateHistoryList.isEmpty()) {
                staffSalaryTemplateHistory = staffSalaryTemplateHistoryList.get(0);
            }
            if (staffSalaryTemplateHistory == null || staffSalaryTemplateHistory.getSalaryTemplate() == null) {
                throw new ResourceNotFoundException("Không tìm thấy mẫu bảng lương của nhân viên này");
            }
            if (staffSalaryTemplateHistory.getSalaryTemplate().getTemplateItems() == null) {
                throw new ResourceNotFoundException("Không tìm thấy các thành phần trong mẫu bảng lương của nhân viên này");
            }
            List<SalaryTemplateItem> salaryTemplateItemList = staffSalaryTemplateHistory.getSalaryTemplate().getTemplateItems().stream().toList();
            List<SalaryResultItemDetail> salaryResultItemDetailList = new ArrayList<>();
            for (SalaryTemplateItem salaryTemplateItem : salaryTemplateItemList) {
                SalaryResultItemDetail salaryResultItemDetail = new SalaryResultItemDetail();
                salaryResultItemDetail.setSalaryResultItem(salaryResultItem);
                salaryResultItemDetail.setSalaryTemplateItem(salaryTemplateItem);
                salaryResultItemDetailList.add(salaryResultItemDetail);
                salaryResultItemDetail.setValue(salaryTemplateItem.getDefaultAmount());
            }
            repository.saveAll(salaryResultItemDetailList);
        }
    }
}
