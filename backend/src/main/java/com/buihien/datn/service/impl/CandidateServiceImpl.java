package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.CandidateDto;
import com.buihien.datn.dto.search.CandidateSearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.*;
import com.buihien.datn.service.CandidateService;
import com.buihien.datn.service.FileDescriptionService;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl extends GenericServiceImpl<Candidate, CandidateDto, CandidateSearchDto> implements CandidateService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private EthnicsRepository ethnicsRepository;
    @Autowired
    private ReligionRepository religionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EducationDegreeRepository educationDegreeRepository;
    @Autowired
    private RecruitmentPlanRepository recruitmentPlanRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private FileDescriptionService fileDescriptionService;
    @Autowired
    private CandidateRepository candidateRepository;

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

        Country nationality = null;
        if (dto.getNationality() != null && dto.getNationality().getId() != null) {
            nationality = countryRepository.findById(dto.getNationality().getId()).orElse(null);
        }
        entity.setNationality(nationality);

        Ethnics ethnics = null;
        if (dto.getEthnics() != null && dto.getEthnics().getId() != null) {
            ethnics = ethnicsRepository.findById(dto.getEthnics().getId()).orElse(null);
        }
        entity.setEthnics(ethnics);

        Religion religion = null;
        if (dto.getReligion() != null && dto.getReligion().getId() != null) {
            religion = religionRepository.findById(dto.getReligion().getId()).orElse(null);
        }
        entity.setReligion(religion);
        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setTaxCode(dto.getTaxCode());

        User user = null;
        if (dto.getUser() != null && dto.getUser().getId() != null) {
            user = userRepository.findById(dto.getUser().getId()).orElse(null);
        }
        entity.setUser(user);

        EducationDegree educationDegree = null;
        if (dto.getEducationDegree() != null && dto.getEducationDegree().getId() != null) {
            educationDegree = educationDegreeRepository.findById(dto.getEducationDegree().getId()).orElse(null);
        }
        entity.setEducationDegree(educationDegree);
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());

        // ----- Thông tin riêng của Candidate -----
        RecruitmentPlan recruitmentPlan = null;
        if (dto.getRecruitmentPlan() != null && dto.getRecruitmentPlan().getId() != null) {
            recruitmentPlan = recruitmentPlanRepository.findById(dto.getRecruitmentPlan().getId()).orElse(null);
        }
        entity.setRecruitmentPlan(recruitmentPlan);

        Position position = null;
        if (dto.getPosition() != null && dto.getPosition().getId() != null) {
            position = positionRepository.findById(dto.getPosition().getId()).orElse(null);
        }
        // Kiểm tra vị trí ứng tuyển có hợp lệ không (phải nằm trong kế hoạch tuyển dụng)
        if (position != null) {
            if (recruitmentPlan == null || recruitmentPlan.getRecruitmentRequest() == null || recruitmentPlan.getRecruitmentRequest().getRecruitmentRequestItems() == null) {
                throw new ResourceNotFoundException("Vị trí ứng tuyển không hợp lệ");
            }

            boolean validPosition = false;
            for (RecruitmentRequestItem item : recruitmentPlan.getRecruitmentRequest().getRecruitmentRequestItems()) {
                if (item.getPosition() != null && item.getPosition().getId() != null && item.getPosition().getId().equals(position.getId())) {
                    validPosition = true;
                    break;
                }
            }

            if (!validPosition) {
                throw new ResourceNotFoundException("Vị trí ứng tuyển không nằm trong kế hoạch tuyển dụng");
            }

            entity.setPosition(position);

        }
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

        FileDescription newFile = null;
        if (dto.getCurriculumVitae() != null && dto.getCurriculumVitae().getId() != null) {
            newFile = fileDescriptionService.getEntityById(dto.getCurriculumVitae().getId());
        }

        FileDescription oldFile = entity.getCurriculumVitae();

        if (oldFile != null && oldFile.getId() != null) {
            // Nếu file mới khác file cũ thì xóa file cũ
            if (newFile == null || !oldFile.getId().equals(newFile.getId())) {
                fileDescriptionService.deleteById(oldFile.getId());
            }
        }

        // Gán file mới (có thể null)
        entity.setCurriculumVitae(newFile);
        return entity;
    }

    @Override
    public Boolean deleteById(UUID id) {
        logger.info("Attempting to delete entity with ID: {}", id);

        Candidate entity = repository.findById(id).orElse(null);
        if (entity == null) {
            logger.warn("Entity not found for deletion with ID: {}", id);
            throw new ResourceNotFoundException("Entity not found for deletion with ID: " + id);
        }

        // Xoá file CV nếu có
        FileDescription cv = entity.getCurriculumVitae();
        if (cv != null && cv.getId() != null) {
            fileDescriptionService.deleteById(cv.getId());
        }

        repository.deleteById(id);
        logger.info("Successfully deleted entity and related file with ID: {}", id);
        return true;
    }


    @Override
    public int deleteMultiple(List<UUID> ids) {
        logger.info("Attempting to delete multiple entities with IDs: {}", ids);

        if (ids == null || ids.isEmpty()) {
            logger.warn("Empty ID list provided. No entities to delete.");
            throw new ResourceNotFoundException("Empty ID list provided. No entities to delete.");
        }

        List<Candidate> entities = repository.findAllById(ids);
        for (Candidate entity : entities) {
            FileDescription cv = entity.getCurriculumVitae();
            if (cv != null && cv.getId() != null) {
                fileDescriptionService.deleteById(cv.getId());
            }
        }

        repository.deleteAllById(ids);
        logger.info("Successfully deleted {} entities and their CV files", ids.size());
        return ids.size();
    }


    @Override
    public Page<CandidateDto> pagingSearch(CandidateSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Candidate entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.CandidateDto(entity) FROM Candidate entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
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
}
