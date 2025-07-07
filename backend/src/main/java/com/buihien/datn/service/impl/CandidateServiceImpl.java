package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.CandidateDto;
import com.buihien.datn.dto.candidateupdatestatus.CandidateStatusDto;
import com.buihien.datn.dto.candidateupdatestatus.CandidateStatusItemDto;
import com.buihien.datn.dto.search.CandidateSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.*;
import com.buihien.datn.service.CandidateService;
import com.buihien.datn.service.StaffService;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl extends GenericServiceImpl<Candidate, CandidateDto, CandidateSearchDto> implements CandidateService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);
    @Value("${host-ai:http://127.0.0.1:5000}")
    private String hostAI;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private RecruitmentRequestRepository recruitmentRequestRepository;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    protected CandidateDto convertToDto(Candidate entity) {
        return new CandidateDto(entity, true);
    }

    @Override
    protected Candidate convertToEntity(CandidateDto dto) {
        Candidate entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Candidate();
            entity.setCandidateCode(this.generateCandidateCode());
            entity.setCandidateStatus(DatnConstants.CandidateStatus.CREATED.getValue());
        }
        // ----- Thông tin kế thừa từ Person -----
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDisplayName(dto.getDisplayName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setBirthPlace(dto.getBirthPlace());
        entity.setGender(dto.getGender());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setIdNumber(dto.getIdNumber());
        entity.setIdNumberIssueBy(dto.getIdNumberIssueBy());
        entity.setIdNumberIssueDate(dto.getIdNumberIssueDate());
        entity.setEmail(dto.getEmail());

        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setTaxCode(dto.getTaxCode());

        entity.setEducationLevel(dto.getEducationLevel());
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());


        Position position = null;
        if (dto.getPosition() != null && dto.getPosition().getId() != null) {
            position = positionRepository.findById(dto.getPosition().getId()).orElse(null);
        }
        entity.setPosition(position);
        entity.setSubmissionDate(dto.getSubmissionDate());
        entity.setInterviewDate(dto.getInterviewDate());
        entity.setDesiredPay(dto.getDesiredPay());
        entity.setPossibleWorkingDate(dto.getPossibleWorkingDate());
        entity.setOnboardDate(dto.getOnboardDate());

        Staff introducer = null;
        if (dto.getIntroducer() != null && dto.getIntroducer().getId() != null) {
            introducer = staffRepository.findById(dto.getIntroducer().getId()).orElse(null);
        }
        entity.setIntroducer(introducer);
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        entity.setStaff(staff);

        entity.setWorkExperience(dto.getWorkExperience());

        RecruitmentRequest recruitmentRequest = null;
        if (dto.getRecruitmentRequest() != null && dto.getRecruitmentRequest().getId() != null) {
            recruitmentRequest = recruitmentRequestRepository.findById(dto.getRecruitmentRequest().getId()).orElse(null);
        }
        entity.setRecruitmentRequest(recruitmentRequest);

        return entity;
    }

    @Override
    public Page<CandidateDto> pagingSearch(CandidateSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Candidate entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.CandidateDto(entity, false) FROM Candidate entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.displayName) LIKE LOWER(:text) OR LOWER(entity.candidateCode) LIKE LOWER(:text)) ");
        }

        if (dto.getRecruitmentRequestId() != null) {
            whereClause.append(" AND entity.recruitmentRequest.id = :recruitmentRequestId ");
        }
        if (dto.getPositionId() != null) {
            whereClause.append(" AND entity.position.id = :positionId ");
        }
        if (dto.getIntroducerId() != null) {
            whereClause.append(" AND entity.introducer.id = :introducerId ");
        }
        if (dto.getCandidateStatus() != null) {
            whereClause.append(" AND entity.candidateStatus = :candidateStatus ");
        }
        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createdAt >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createdAt <= :toDate ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), CandidateDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getRecruitmentRequestId() != null) {
            q.setParameter("recruitmentRequestId", dto.getRecruitmentRequestId());
            qCount.setParameter("recruitmentRequestId", dto.getRecruitmentRequestId());
        }
        if (dto.getPositionId() != null) {
            q.setParameter("positionId", dto.getPositionId());
            qCount.setParameter("positionId", dto.getPositionId());
        }
        if (dto.getIntroducerId() != null) {
            q.setParameter("introducerId", dto.getIntroducerId());
            qCount.setParameter("introducerId", dto.getIntroducerId());
        }
        if (dto.getCandidateStatus() != null) {
            q.setParameter("candidateStatus", dto.getCandidateStatus());
            qCount.setParameter("candidateStatus", dto.getCandidateStatus());
        }
        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }
        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);

            return new PageImpl<>(q.getResultList(), PageRequest.of(pageIndex, pageSize), (long) qCount.getSingleResult());
        }
        return new PageImpl<>(q.getResultList());
    }

    private String generateCandidateCode() {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());

        String prefix = "UV" + year + month;

        // Lấy tất cả mã ứng viên bắt đầu với prefix
        List<String> existingCodes = candidateRepository.findCandidateCodesStartingWith(prefix);

        Set<Integer> existingNumbers = existingCodes.stream()
                .map(code -> code.substring(prefix.length()))
                .filter(suffix -> suffix.matches("\\d+"))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());

        int max = existingNumbers.stream().max(Integer::compareTo).orElse(0);
        int nextNumber = max + 1;

        while (existingNumbers.contains(nextNumber)) {
            nextNumber++;
        }

        // Dùng 4 chữ số cho phần số
        String sequence = String.format("%04d", nextNumber);
        return prefix + sequence;
    }

    @Override
    @Transactional
    public Integer updateStatus(CandidateStatusDto status) {
        if (status == null || status.getCandidates() == null || status.getCandidates().isEmpty() || status.getCandidateStatus() == null) {
            return 0;
        }
        List<Candidate> candidatesEntity = new ArrayList<>();
        for (UUID candidateId : status.getCandidates()) {
            Candidate candidate = repository.findById(candidateId).orElse(null);
            if (candidate == null) continue;
            candidate.setCandidateStatus(status.getCandidateStatus());
            candidatesEntity.add(candidate);
        }
        if (status.getCandidateStatus().equals(DatnConstants.CandidateStatus.HIRED.getValue())) {
            for (Candidate candidate : candidatesEntity) {
                this.convertCandidateToStaff(candidate);
            }
        }
        candidatesEntity = repository.saveAll(candidatesEntity);
        return candidatesEntity.size();
    }

    @Override
    public Integer preScreened(CandidateStatusDto dto) {
        String targetUrl = hostAI + "/api/pre-screened";

        List<CandidateStatusItemDto> requestPayload = new ArrayList<>();

        // Chuẩn bị dữ liệu gửi sang Flask
        for (UUID candidateId : dto.getCandidates()) {
            Candidate candidate = repository.findById(candidateId).orElse(null);
            if (candidate == null) continue;
            if (candidate.getWorkExperience() == null) continue;
            if (candidate.getRecruitmentRequest() == null) continue;
            if (candidate.getRecruitmentRequest().getRequest() == null) continue;

            CandidateStatusItemDto item = new CandidateStatusItemDto();
            item.setId(candidate.getId());
            item.setWorkExperience(candidate.getWorkExperience());
            item.setRequest(candidate.getRecruitmentRequest().getRequest());
            requestPayload.add(item);
        }

        if (requestPayload.isEmpty()) {
            return 0;
        }

        // Gửi request sang Flask
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<CandidateStatusItemDto>> requestEntity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<List<CandidateStatusItemDto>> response;
        try {
            response = restTemplate.exchange(
                    targetUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (Exception e) {
            System.err.println("Lỗi gọi Flask API: " + e.getMessage());
            return 0;
        }

        List<CandidateStatusItemDto> responseBody = response.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            return 0;
        }

        // Cập nhật trạng thái ứng viên dựa trên kết quả từ Flask
        List<Candidate> candidatesToUpdate = new ArrayList<>();
        for (CandidateStatusItemDto item : responseBody) {
            Candidate candidate = repository.findById(item.getId()).orElse(null);
            if (candidate == null) continue;

            if (Boolean.TRUE.equals(item.getIsPass())) {
                candidate.setCandidateStatus(DatnConstants.CandidateStatus.PRE_SCREENED.getValue());
            } else {
                candidate.setCandidateStatus(DatnConstants.CandidateStatus.FAILED_SCREENING.getValue());
            }

            candidatesToUpdate.add(candidate);
        }

        if (!candidatesToUpdate.isEmpty()) {
            repository.saveAll(candidatesToUpdate);
        }

        return candidatesToUpdate.size();
    }

    private void convertCandidateToStaff(Candidate candidate) {

        Staff entity = new Staff();
        entity.setStaffCode(staffService.generateStaffCode());

        // ----- Thông tin kế thừa từ Person -----
        entity.setFirstName(candidate.getFirstName());
        entity.setLastName(candidate.getLastName());
        entity.setDisplayName(candidate.getDisplayName());
        entity.setBirthDate(candidate.getBirthDate());
        entity.setBirthPlace(candidate.getBirthPlace());
        entity.setGender(candidate.getGender());
        entity.setPhoneNumber(candidate.getPhoneNumber());
        entity.setIdNumber(candidate.getIdNumber());
        entity.setIdNumberIssueBy(candidate.getIdNumberIssueBy());
        entity.setIdNumberIssueDate(candidate.getIdNumberIssueDate());
        entity.setEmail(candidate.getEmail());

        entity.setMaritalStatus(candidate.getMaritalStatus());
        entity.setTaxCode(candidate.getTaxCode());

        entity.setEducationLevel(candidate.getEducationLevel());
        entity.setHeight(candidate.getHeight());
        entity.setWeight(candidate.getWeight());

        // ----- Thông tin riêng của Nhân viên -----
        entity.setRecruitmentDate(new Date());
        entity = staffRepository.saveAndFlush(entity);
        //map ứng viên với nhân viên
        candidate.setStaff(entity);
        candidate.setCandidateStatus(DatnConstants.CandidateStatus.HIRED.getValue());
        candidateRepository.save(candidate);

    }
}
