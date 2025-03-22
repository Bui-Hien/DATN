package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Person;
import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    public EntityManager manager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto saveOrUpdate(UserDto dto) {
        if (dto == null) return null;
        User entity = null;
        if (dto.getId() != null) {
            entity = userRepository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new User();
            if (!StringUtils.hasText(dto.getPassword())) {
                throw new InvalidDataException("Password is required");
            }
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new InvalidDataException("Passwords do not match with confirm password");
            }
        }
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
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
        User response = userRepository.save(entity);
        return new UserDto(response);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        User entity = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (entity != null) {
            userRepository.delete(entity);
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<String> getAllRolesByUserId(long userId) {
        return userRepository.findAllRolesByUserId(userId);
    }

    @Override
    public List<String> getAllRolesByUserUserName(String username) {
        return userRepository.findAllRolesByUserName(username);
    }

    @Override
    public UserDto getById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDto(user);
    }

    @Override
    public User findUserByRole(String role) {
        return userRepository.findUserByRole(role).orElse(null);
    }

    @Override
    public Page<UserDto> pagingUser(SearchDto dto) {
        if (dto == null) return null;

        int pageIndex = (dto.getPageIndex() != null || dto.getPageIndex() < 1) ? dto.getPageIndex() : 1;
        int pageSize = (dto.getPageSize() != null || dto.getPageSize() < 10) ? dto.getPageSize() : 10;

        if (pageIndex > 0) pageIndex--;
        else pageIndex = 0;

        String sqlCount = "SELECT COUNT (entity.id) from User entity WHERE (1=1) ";
        String sql = "SELECT new com.buihien.datn.dto.UserDto(entity) FROM User AS entity WHERE (1=1) ";

        String whereClause = " AND ( entity.voided = :voided) ";
        String orderBy = "";
        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause += " AND ( entity.username LIKE :text OR entity.email LIKE :text) ";
        }
        if (dto.getRoleId() != null) {
            whereClause += " AND ( entity.roles.role.id = :roleId) ";
        }
        if (dto.getOrderBy() != null && dto.getOrderBy()) {
            orderBy = "ORDER BY entity.createdAt ASC";
        } else {
            orderBy = "ORDER BY entity.createdAt DESC";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, UserDto.class);
        Query qCount = manager.createQuery(sqlCount);

        boolean voided = dto.getVoided() != null ? dto.getVoided() : false;
        q.setParameter("voided", voided);
        qCount.setParameter("voided", voided);

        if (StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getRoleId() != null) {
            q.setParameter("roleId", dto.getRoleId());
            qCount.setParameter("roleId", dto.getRoleId());
        }
        int startPosition = pageIndex * pageSize;
        q.setFirstResult(startPosition);
        q.setMaxResults(pageSize);

        List<UserDto> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        return new PageImpl<>(entities, pageable, count);
    }

}
