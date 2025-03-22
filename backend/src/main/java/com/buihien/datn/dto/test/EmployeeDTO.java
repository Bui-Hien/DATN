package com.buihien.datn.dto.test;

import com.buihien.datn.util.Excel;
import com.buihien.datn.util.ExcelColumn;

import java.time.LocalDate;

@Excel(name = "Dữ liệu nhân viên", startRow = 10, index = 10)
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phoneNumber;
    private AddressDTO address;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Long id, String firstName, String lastName, LocalDate dateOfBirth, String gender, String email, String phoneNumber, AddressDTO address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @ExcelColumn(index = 0, title = "STT", numericalOrder = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ExcelColumn(index = 1, title = "Họ nhân viên")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ExcelColumn(index = 2, title = "Tên nhân viên")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ExcelColumn(index = 3, title = "Ngày sinh")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @ExcelColumn(index = 4, title = "Giới tính")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @ExcelColumn(index = 5, title = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelColumn(index = 6, title = "Số điện thoại")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @ExcelColumn(index = 7, title = "Đường")
    public String getStreet() {
        return address != null ? address.getStreet() : null;
    }

    public void setStreet(String street) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setStreet(street);
    }

    @ExcelColumn(index = 8, title = "Thành phố")
    public String getCity() {
        return address != null ? address.getCity() : null;
    }

    public void setCity(String city) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setCity(city);
    }

    @ExcelColumn(index = 9, title = "Tiểu bang")
    public String getState() {
        return address != null ? address.getState() : null;
    }

    public void setState(String state) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setState(state);
    }

    @ExcelColumn(index = 10, title = "Mã bưu điện")
    public String getZipCode() {
        return address != null ? address.getZipCode() : null;
    }

    public void setZipCode(String zipCode) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setZipCode(zipCode);
    }

    @ExcelColumn(index = 11, title = "Quốc gia")
    public String getCountry() {
        return address != null ? address.getCountry() : null;
    }

    public void setCountry(String country) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setCountry(country);
    }

    @Override
    public String toString() {
        return this.email + " " + this.firstName + " " + this.lastName;
    }
}
