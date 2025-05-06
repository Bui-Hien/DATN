package com.buihien.datn.dto;

import com.buihien.datn.domain.PersonBankAccount;

public class PersonBankAccountDto extends AuditableDto {
    private PersonDto person;
    private BankDto bank; // Ngân hàng nào
    private String bankAccountName; // Tên tài khoản ngân hàng
    private String bankAccountNumber; // Số tài khoản ngân hàng
    private String bankBranch; // Chi nhánh ngân hàng
    private Boolean isMain; // Là tài khoản ngân hàng chính


    public PersonBankAccountDto() {
    }

    public PersonBankAccountDto(PersonBankAccount entity, Boolean isGetPerson) {
        super(entity);
        if (entity != null) {
            if (isGetPerson) {
                this.person = new PersonDto(entity.getPerson(), false);
            }
            if (entity.getBank() != null) {
                this.bank = new BankDto(entity.getBank());
            }
            this.bankAccountName = entity.getBankAccountName();
            this.bankAccountNumber = entity.getBankAccountNumber();
            this.bankBranch = entity.getBankBranch();
            this.isMain = entity.getMain();
        }
    }


    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public BankDto getBank() {
        return bank;
    }

    public void setBank(BankDto bank) {
        this.bank = bank;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }
}
