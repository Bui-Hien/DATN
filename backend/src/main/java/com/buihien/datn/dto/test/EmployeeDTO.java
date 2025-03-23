package com.buihien.datn.dto.test;

import com.buihien.datn.util.anotation.Excel;
import com.buihien.datn.util.anotation.ExcelColumnGetter;
import com.buihien.datn.util.anotation.ExcelColumnSetter;

import java.time.LocalDate;

@Excel(name = "Dữ liệu nhân viên")
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

    @ExcelColumnGetter(index = 0, title = "STT", numericalOrder = true)
    public Long getId() {
        return id;
    }

    @ExcelColumnSetter(index = 0)
    public void setId(Long id) {
        this.id = id;
    }

    @ExcelColumnGetter(index = 1, title = "Họ nhân viên")
    public String getFirstName() {
        return firstName;
    }

    @ExcelColumnSetter(index = 1)
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @ExcelColumnGetter(index = 2, title = "Tên nhân viên")
    public String getLastName() {
        return lastName;
    }

    @ExcelColumnSetter(index = 2)
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ExcelColumnGetter(index = 3, title = "Ngày sinh")
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @ExcelColumnSetter(index = 3)
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @ExcelColumnGetter(index = 4, title = "Giới tính")
    public String getGender() {
        return gender;
    }

    @ExcelColumnSetter(index = 4)
    public void setGender(String gender) {
        this.gender = gender;
    }

    @ExcelColumnGetter(index = 5, title = "Email")
    public String getEmail() {
        return email;
    }

    @ExcelColumnSetter(index = 5)
    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelColumnGetter(index = 6, title = "Số điện thoại")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ExcelColumnSetter(index = 6)
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @ExcelColumnGetter(index = 7, title = "Đường")
    public String getStreet() {
        return address != null ? address.getStreet() : null;
    }

    @ExcelColumnSetter(index = 7)
    public void setStreet(String street) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setStreet(street);
    }

    @ExcelColumnGetter(index = 8, title = "Thành phố")
    public String getCity() {
        return address != null ? address.getCity() : null;
    }

    @ExcelColumnSetter(index = 8)
    public void setCity(String city) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setCity(city);
    }

    @ExcelColumnGetter(index = 9, title = "Tiểu bang")
    public String getState() {
        return address != null ? address.getState() : null;
    }

    @ExcelColumnSetter(index = 9)
    public void setState(String state) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setState(state);
    }

    @ExcelColumnGetter(index = 10, title = "Mã bưu điện")
    public String getZipCode() {
        return address != null ? address.getZipCode() : null;
    }

    @ExcelColumnSetter(index = 10)
    public void setZipCode(String zipCode) {
        if (this.address == null) {
            this.address = new AddressDTO();
        }
        this.address.setZipCode(zipCode);
    }

    @ExcelColumnGetter(index = 11, title = "Quốc gia")
    public String getCountry() {
        return address != null ? address.getCountry() : null;
    }

    @ExcelColumnSetter(index = 11)
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
