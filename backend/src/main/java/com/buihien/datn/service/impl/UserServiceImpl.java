package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Person;
import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.PersonRepository;
import com.buihien.datn.repository.UserRepository;
import com.buihien.datn.service.UserRoleService;
import com.buihien.datn.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDto, SearchDto> implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng " + username));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));
    }

    @Override
    public List<String> getAllRolesByUserId(UUID userId) {
        return userRepository.findAllRolesByUserId(userId);
    }

    @Override
    public List<String> getAllRolesByUserUserName(String username) {
        return userRepository.findAllRolesByUserName(username);
    }

    @Override
    public User findUserByRole(String role) {
        return userRepository.findUserByRole(role).orElse(null);
    }

    @Override
    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User result = (User) authentication.getPrincipal();
            return new UserDto(result);
        }
        throw new SecurityException("Bạn chưa đăng nhập");
    }

    @Override
    public User getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new SecurityException("Bạn chưa đăng nhập");
    }

    @Override
    protected UserDto convertToDto(User entity) {
        return new UserDto(entity);
    }

    @Override
    protected User convertToEntity(UserDto dto) {
        if (dto == null) return null;
        User entity = null;
        if (dto.getId() != null) {
            entity = userRepository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new User();
        }
        if (dto.getPassword() != null || dto.getConfirmPassword() != null) {
            if (!StringUtils.hasText(dto.getPassword())) {
                throw new InvalidDataException("Mật khẩu là bắt buộc");
            }
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new InvalidDataException("Mật khẩu xác nhận không khớp");
            }
        }
        entity.setUsername(dto.getUsername());
        entity.setEmail("user_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com");
        if (dto.getPerson() != null) {
            Person person = null;
            if (dto.getPerson().getId() != null) {
                person = personRepository.findById(dto.getPerson().getId()).orElse(null);
            }
            if (person == null) {
                person = new Person();
            }
            entity.setPerson(person);
        }
        userRoleService.handleSetRoleListInUser(dto, entity);
        return entity;
    }

    @Override
    public Page<UserDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();
        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();

        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM User entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new UserDto(entity) FROM User entity WHERE (1=1) ");
        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.username) LIKE LOWER(:text) OR LOWER(entity.email) LIKE LOWER(:text)) ");
        }

        if (dto.getRoleId() != null) {
            whereClause.append(" AND entity.roles.role.id = :roleId ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);
        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), UserDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        if (dto.getRoleId() != null) {
            q.setParameter("roleId", dto.getRoleId());
            qCount.setParameter("roleId", dto.getRoleId());
        }

        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);
            return new PageImpl<>(q.getResultList(), PageRequest.of(pageIndex, pageSize), (long) qCount.getSingleResult());
        }
        return new PageImpl<>(q.getResultList());
    }
}