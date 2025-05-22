package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.PersonAddressDto;
import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.search.StaffSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.*;
import com.buihien.datn.service.FileDescriptionService;
import com.buihien.datn.service.StaffDocumentItemService;
import com.buihien.datn.service.StaffService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl extends GenericServiceImpl<Staff, StaffDto, StaffSearchDto> implements StaffService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReligionRepository religionRepository;
    @Autowired
    private EthnicsRepository ethnicsRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private DocumentTemplateRepository documentTemplateRepository;
    @Autowired
    private StaffDocumentItemService staffDocumentItemService;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private PersonAddressRepository personAddressRepository;
    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;
    @Autowired
    private FileDescriptionService fileDescriptionService;

    @Override
    protected StaffDto convertToDto(Staff entity) {
        return new StaffDto(entity, true);
    }

    @Override
    protected Staff convertToEntity(StaffDto dto) {
        Staff entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Staff();
            entity.setStaffCode(this.generateStaffCode());
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

        entity.setEducationLevel(dto.getEducationLevel());
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());


        //thường chú
        if (dto.getPermanentResidence() != null) {
            PersonAddress permanentResidence = null;
            if (dto.getPermanentResidence().getId() != null) {
                permanentResidence = personAddressRepository.findById(dto.getPermanentResidence().getId()).orElse(null);
            }
            if (permanentResidence == null) {
                permanentResidence = new PersonAddress();
            }
            permanentResidence.setAddressDetail(dto.getPermanentResidence().getAddressDetail());

            AdministrativeUnit province = null;
            if (dto.getPermanentResidence().getProvince() != null && dto.getPermanentResidence().getProvince().getId() != null) {
                province = administrativeUnitRepository.findById(dto.getPermanentResidence().getProvince().getId()).orElse(null);
            }
            permanentResidence.setProvince(province);

            AdministrativeUnit district = null;
            if (dto.getPermanentResidence().getDistrict() != null && dto.getPermanentResidence().getDistrict().getId() != null) {
                district = administrativeUnitRepository.findById(dto.getPermanentResidence().getDistrict().getId()).orElse(null);
            }
            permanentResidence.setDistrict(district);

            AdministrativeUnit ward = null;
            if (dto.getPermanentResidence().getWard() != null && dto.getPermanentResidence().getWard().getId() != null) {
                ward = administrativeUnitRepository.findById(dto.getPermanentResidence().getWard().getId()).orElse(null);
            }
            permanentResidence.setWard(ward);
            entity.setPermanentResidence(permanentResidence);
        } else {
            entity.setPermanentResidence(null);
        }

        //Tạm chú
        if (dto.getTemporaryResidence() != null) {
            PersonAddress temporaryResidence = null;
            if (dto.getPermanentResidence().getId() != null) {
                temporaryResidence = personAddressRepository.findById(dto.getTemporaryResidence().getId()).orElse(null);
            }
            if (temporaryResidence == null) {
                temporaryResidence = new PersonAddress();
            }
            temporaryResidence.setAddressDetail(dto.getTemporaryResidence().getAddressDetail());

            AdministrativeUnit province = null;
            if (dto.getTemporaryResidence().getProvince() != null && dto.getTemporaryResidence().getProvince().getId() != null) {
                province = administrativeUnitRepository.findById(dto.getTemporaryResidence().getProvince().getId()).orElse(null);
            }
            temporaryResidence.setProvince(province);

            AdministrativeUnit district = null;
            if (dto.getTemporaryResidence().getDistrict() != null && dto.getTemporaryResidence().getDistrict().getId() != null) {
                district = administrativeUnitRepository.findById(dto.getTemporaryResidence().getDistrict().getId()).orElse(null);
            }
            temporaryResidence.setDistrict(district);

            AdministrativeUnit ward = null;
            if (dto.getTemporaryResidence().getWard() != null && dto.getTemporaryResidence().getWard().getId() != null) {
                ward = administrativeUnitRepository.findById(dto.getTemporaryResidence().getWard().getId()).orElse(null);
            }
            temporaryResidence.setWard(ward);
            entity.setTemporaryResidence(temporaryResidence);
        } else {
            entity.setTemporaryResidence(null);
        }

        FileDescription newAvatar = null;
        if (dto.getAvatar() != null && dto.getAvatar().getId() != null) {
            newAvatar = fileDescriptionService.getEntityById(dto.getAvatar().getId());
        }

        FileDescription oldAvatar = entity.getAvatar();

        if (oldAvatar != null && oldAvatar.getId() != null) {
            // Nếu file mới khác file cũ thì xóa file cũ
            if (newAvatar == null || !oldAvatar.getId().equals(newAvatar.getId())) {
                fileDescriptionService.deleteById(oldAvatar.getId());
            }
        }
        entity.setAvatar(newAvatar);

        // ----- Thông tin riêng của Nhân viên -----
        entity.setRecruitmentDate(dto.getRecruitmentDate());
        entity.setStartDate(dto.getStartDate());
        entity.setApprenticeDays(dto.getApprenticeDays());
        entity.setEmployeeStatus(dto.getEmployeeStatus());

        DocumentTemplate documentTemplate = null;
        if (dto.getDocumentTemplate() != null && dto.getDocumentTemplate().getId() != null) {
            documentTemplate = documentTemplateRepository.findById(dto.getDocumentTemplate().getId()).orElse(null);
        }
        entity.setDocumentTemplate(documentTemplate);
        entity.setStaffPhase(dto.getStaffPhase());
        entity.setRequireAttendance(dto.getRequireAttendance());
        entity.setAllowExternalIpTimekeeping(dto.getAllowExternalIpTimekeeping());
        entity.setHasSocialIns(dto.getHasSocialIns());
        return entity;
    }

    @Override
    public Page<StaffDto> pagingSearch(StaffSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Staff entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffDto(entity, false) FROM Staff entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), StaffDto.class);
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

    private String generateStaffCode() {
        LocalDate now = LocalDate.now();
        String year = String.format("%02d", now.getYear() % 100);
        String month = String.format("%02d", now.getMonthValue());

        String prefix = "NV" + year + month;

        // Lấy tất cả mã nhân viên bắt đầu với prefix
        List<String> existingCodes = staffRepository.findStaffCodesStartingWith(prefix);

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

        String sequence = String.format("%04d", nextNumber);
        return prefix + sequence;
    }

    @Override
    public StaffDto convertCandidateToStaff(Candidate candidate) {

        Staff entity = new Staff();
        entity.setStaffCode(this.generateStaffCode());

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

        Country nationality = null;
        if (candidate.getNationality() != null && candidate.getNationality().getId() != null) {
            nationality = countryRepository.findById(candidate.getNationality().getId()).orElse(null);
        }
        entity.setNationality(nationality);

        Ethnics ethnics = null;
        if (candidate.getEthnics() != null && candidate.getEthnics().getId() != null) {
            ethnics = ethnicsRepository.findById(candidate.getEthnics().getId()).orElse(null);
        }
        entity.setEthnics(ethnics);

        Religion religion = null;
        if (candidate.getReligion() != null && candidate.getReligion().getId() != null) {
            religion = religionRepository.findById(candidate.getReligion().getId()).orElse(null);
        }
        entity.setReligion(religion);
        entity.setMaritalStatus(candidate.getMaritalStatus());
        entity.setTaxCode(candidate.getTaxCode());

        User user = null;
        if (candidate.getUser() != null && candidate.getUser().getId() != null) {
            user = userRepository.findById(candidate.getUser().getId()).orElse(null);
        }
        entity.setUser(user);


        entity.setEducationLevel(candidate.getEducationLevel());
        entity.setHeight(candidate.getHeight());
        entity.setWeight(candidate.getWeight());

        // ----- Thông tin riêng của Nhân viên -----
        entity.setRecruitmentDate(new Date());
        entity = repository.saveAndFlush(entity);
        //map ứng viên với nhân viên
        candidate.setStaff(entity);
        candidate.setCandidateStatus(DatnConstants.CandidateStatus.HIRED.getValue());
        candidateRepository.save(candidate);

        //trả về thông tin nhân viêns
        return new StaffDto(entity, false);
    }
}
