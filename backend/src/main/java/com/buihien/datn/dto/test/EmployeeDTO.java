package com.buihien.datn.dto;

public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private AddressDTO address;
    private DepartmentDTO department;
    private PositionDTO position;
    private SalaryDTO salary;
    private ContractDTO contract;
    private List<WorkHistoryDTO> workHistory;
    private EducationDTO education;
    private BankAccountDTO bankAccount;
}
