package com.buihien.datn.service.impl;

import com.buihien.datn.domain.FileDescription;
import com.buihien.datn.domain.SalaryTemplate;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.User;
import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.search.StaffSearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryTemplateRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.FileDescriptionService;
import com.buihien.datn.service.StaffService;
import com.buihien.datn.util.CacheUtils;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl extends GenericServiceImpl<Staff, StaffDto, StaffSearchDto> implements StaffService {
    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private FileDescriptionService fileDescriptionService;
    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;

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

        entity.setMaritalStatus(dto.getMaritalStatus());
        entity.setTaxCode(dto.getTaxCode());

        entity.setEducationLevel(dto.getEducationLevel());
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());


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

        entity.setStaffPhase(dto.getStaffPhase());
        entity.setRequireAttendance(dto.getRequireAttendance());
        entity.setAllowExternalIpTimekeeping(dto.getAllowExternalIpTimekeeping());

        SalaryTemplate salaryTemplate = null;
        if (dto.getSalaryTemplate() != null && dto.getSalaryTemplate().getId() != null) {
            salaryTemplate = salaryTemplateRepository.findById(dto.getSalaryTemplate().getId()).orElse(null);
        }
        entity.setSalaryTemplate(salaryTemplate);
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
            whereClause.append(" AND (LOWER(entity.displayName) LIKE LOWER(:text) OR LOWER(entity.staffCode) LIKE LOWER(:text)) ");
        }
        //Khoảng thời gian tuyển dụng
        if (dto.getFromRecruitmentDate() != null) {
            whereClause.append(" AND entity.recruitmentDate >= :fromRecruitmentDate ");
        }
        if (dto.getToRecruitmentDate() != null) {
            whereClause.append(" AND entity.recruitmentDate <= :toRecruitmentDate ");
        }
        //Khoảng thời lên chính thức
        if (dto.getFromStartDate() != null) {
            whereClause.append(" AND entity.startDate >= :fromStartDate ");
        }
        if (dto.getToStartDate() != null) {
            whereClause.append(" AND entity.startDate <= :toStartDate ");
        }

        //Trạng thái nhân viên
        if (dto.getEmployeeStatus() != null) {
            whereClause.append(" AND entity.employeeStatus = :employeeStatus ");
        }
        //Loại nhân viên
        if (dto.getStaffPhase() != null) {
            whereClause.append(" AND entity.staffPhase = :staffPhase ");
        }
        //Giới tính
        if (dto.getGender() != null) {
            whereClause.append(" AND entity.gender = :gender ");
        }
        //Tình trạng hôn nhân
        if (dto.getMaritalStatus() != null) {
            whereClause.append(" AND entity.maritalStatus = :maritalStatus ");
        }
        //Trình độ học vấn
        if (dto.getEducationLevel() != null) {
            whereClause.append(" AND entity.educationLevel = :educationLevel ");
        }
        if (dto.getDepartmentId() != null) {
            whereClause.append(" AND EXISTS (SELECT 1 FROM Position p WHERE p.staff.id = entity.id AND p.isMain is true AND p.department.id = :departmentId) ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), StaffDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword().toLowerCase() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword().toLowerCase() + '%');
        }

        if (dto.getFromRecruitmentDate() != null) {
            q.setParameter("fromRecruitmentDate", dto.getFromRecruitmentDate());
            qCount.setParameter("fromRecruitmentDate", dto.getFromRecruitmentDate());
        }
        if (dto.getToRecruitmentDate() != null) {
            q.setParameter("toRecruitmentDate", dto.getToRecruitmentDate());
            qCount.setParameter("toRecruitmentDate", dto.getToRecruitmentDate());
        }

        if (dto.getFromStartDate() != null) {
            q.setParameter("fromStartDate", dto.getFromStartDate());
            qCount.setParameter("fromStartDate", dto.getFromStartDate());
        }
        if (dto.getToStartDate() != null) {
            q.setParameter("toStartDate", dto.getToStartDate());
            qCount.setParameter("toStartDate", dto.getToStartDate());
        }

        if (dto.getEmployeeStatus() != null) {
            q.setParameter("employeeStatus", dto.getEmployeeStatus());
            qCount.setParameter("employeeStatus", dto.getEmployeeStatus());
        }

        if (dto.getStaffPhase() != null) {
            q.setParameter("staffPhase", dto.getStaffPhase());
            qCount.setParameter("staffPhase", dto.getStaffPhase());
        }

        if (dto.getGender() != null) {
            q.setParameter("gender", dto.getGender());
            qCount.setParameter("gender", dto.getGender());
        }

        if (dto.getMaritalStatus() != null) {
            q.setParameter("maritalStatus", dto.getMaritalStatus());
            qCount.setParameter("maritalStatus", dto.getMaritalStatus());
        }
        if (dto.getEducationLevel() != null) {
            q.setParameter("educationLevel", dto.getEducationLevel());
            qCount.setParameter("educationLevel", dto.getEducationLevel());
        }
        if (dto.getDepartmentId() != null) {
            q.setParameter("departmentId", dto.getDepartmentId());
            qCount.setParameter("departmentId", dto.getDepartmentId());
        }

        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);

            List<StaffDto> content = q.getResultList();
            long totalElements = (long) qCount.getSingleResult();
            Page<StaffDto> page = new PageImpl<>(content, PageRequest.of(pageIndex, pageSize), totalElements);

            return page;
        }

        return new PageImpl<>(q.getResultList());
    }

    @Override
    public StaffDto saveOrUpdate(StaffDto dto) {
        if (dto == null) {
            logger.warn("Thông tin đầu vào bị null. Không thể lưu.");
            throw new ResourceNotFoundException("Thông tin đầu vào bị null. Không thể lưu.");
        }

        Staff savedEntity = repository.save(this.convertToEntity(dto));
        CacheUtils.HashMapStaff.put(savedEntity.getId(), savedEntity);

        if (dto.getId() != null) {
            logger.info("Cập nhật thông tin thành công: {}", savedEntity.getId());
        } else {
            logger.info("Lưu thông tin mới thành công: {}", savedEntity.getId());
        }

        return this.convertToDto(savedEntity);
    }

    @Override
    public Integer saveOrUpdateList(List<StaffDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            logger.warn("Danh sách dữ liệu trống. Không thể lưu.");
            return 0;
        }

        // Chuyển đổi DTO thành Entity
        List<Staff> entities = dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Lưu vào database
        List<Staff> savedEntities = repository.saveAll(entities);
        savedEntities.forEach(savedEntity -> CacheUtils.HashMapStaff.put(savedEntity.getId(), savedEntity));
        logger.info("Đã lưu thành công {} bản ghi.", savedEntities.size());
        return savedEntities.size();
    }

    @Override
    public StaffDto getById(UUID id) {
        logger.info("Đang tìm kiếm thực thể với ID: {}", id);

        long startTime = System.nanoTime(); // ⏱ Bắt đầu đo thời gian

        Staff staff = CacheUtils.HashMapStaff.get(id);

        if (staff == null) {
            staff = repository.findById(id)
                    .map(found -> {
                        CacheUtils.HashMapStaff.put(id, found); // ⚡ Cache lại nếu tìm thấy
                        return found;
                    })
                    .orElse(null);
        }

        long endTime = System.nanoTime(); // ⏱ Kết thúc đo thời gian
        long durationMs = (endTime - startTime) / 1_000_000;
        logger.info("Size cache staff: {} MB", CacheUtils.HashMapStaff.getMemoryUsage());

        if (staff != null) {
            logger.info("Lấy thực thể thành công. Thời gian truy xuất: {} ms", durationMs);
            return convertToDto(staff);
        } else {
            logger.warn("Không tìm thấy thực thể với ID: {}. Thời gian thực thi: {} ms", id, durationMs);
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu với ID: " + id);
        }
    }

    @Override
    public Boolean deleteById(UUID id) {
        logger.info("Đang xóa thực thể với ID: {}", id);
        Staff entity = repository.findById(id).orElse(null);
        if (entity != null) {
            entity.setVoided(true);
            repository.save(entity);
            CacheUtils.HashMapStaff.remove(id);
            logger.info("Đã xóa thực thể thành công với ID: {}", id);
            return true;
        } else {
            logger.warn("Không tìm thấy thực thể để xóa với ID: {}", id);
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu để xóa với ID: " + id);
        }
    }

    @Override
    public int deleteMultiple(List<UUID> ids) {
        logger.info("Đang xóa nhiều thực thể với các ID: {}", ids);
        if (ids == null || ids.isEmpty()) {
            logger.warn("Danh sách ID rỗng. Không có thực thể nào để xóa.");
            throw new ResourceNotFoundException("Danh sách ID rỗng. Không có thực thể nào để xóa.");
        }
        List<Staff> staffs = new ArrayList<>();
        for (UUID id : ids) {
            Staff entity = repository.findById(id).orElse(null);
            if (entity != null) {
                entity.setVoided(true);
                staffs.add(entity);
            }
        }
        staffs = repository.saveAll(staffs);
        CacheUtils.HashMapStaff.removes(ids);
        logger.info("Đã xóa st công {} thực thể.", staffs.size());
        return staffs.size();
    }

    @Override
    public String generateStaffCode() {
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
    public StaffDto getCurrentStaff() {
        User currentUser = this.getCurrentUser(); // Lấy user hiện tại
        if (currentUser != null && currentUser.getId() != null) {
            // Giả sử bạn có một phương thức để tìm staff dựa trên userId
            Staff staff = staffRepository.findByUserId(currentUser.getId());
            return new StaffDto(staff, true);
        }
        return null;
    }

    @Override
    public Staff getCurrentStaffEntity() {
        User currentUser = this.getCurrentUser(); // Lấy user hiện tại
        if (currentUser != null && currentUser.getId() != null) {
            // Giả sử bạn có một phương thức để tìm staff dựa trên userId
            return staffRepository.findByUserId(currentUser.getId());
        }
        return null;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new SecurityException("You are not logged in");
    }
}
