package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.CalculatedWorkingDay.ShiftWorkTypeCount;
import com.buihien.datn.dto.SalaryResultItemDetailDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryResultItemDetailRepository;
import com.buihien.datn.repository.StaffLabourAgreementRepository;
import com.buihien.datn.repository.StaffWorkScheduleRepository;
import com.buihien.datn.service.SalaryResultItemDetailService;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SalaryResultItemDetailServiceImpl extends GenericServiceImpl<SalaryResultItemDetail, SalaryResultItemDetailDto, SearchDto> implements SalaryResultItemDetailService {
    @Autowired
    private SalaryResultItemDetailRepository salaryResultItemDetailRepository;
    @Autowired
    private StaffWorkScheduleRepository staffWorkScheduleRepository;
    @Autowired
    private StaffLabourAgreementRepository staffLabourAgreementRepository;

    @Override
    protected SalaryResultItemDetailDto convertToDto(SalaryResultItemDetail entity) {
        return new SalaryResultItemDetailDto(entity, true, false);
    }

    @Override
    protected SalaryResultItemDetail convertToEntity(SalaryResultItemDetailDto dto) {
        return null;
    }

    @Override
    public Page<SalaryResultItemDetailDto> pagingSearch(SearchDto dto) {
        return new PageImpl<>(null);
    }

    @Override
    public void handleSetUpSalaryResultItemDetailByStaff(SalaryResultItem salaryResultItem, SalaryTemplate salaryTemplate) {
        if (salaryResultItem == null || salaryResultItem.getSalaryResult() == null || salaryResultItem.getSalaryResult().getSalaryPeriod() == null) {
            throw new ResourceNotFoundException("Kỳ lương không tồn tại");
        }
        if (salaryTemplate == null) {
            throw new ResourceNotFoundException("Mẫu bảng lương không tồn tại");
        }
        if (salaryResultItem.getStaff() == null || salaryResultItem.getStaff().getId() == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }
        if (salaryTemplate.getTemplateItems() == null || salaryTemplate.getTemplateItems().isEmpty()) {
            return;
        }
        List<SalaryResultItemDetail> salaryResultItemDetailTypeFormula = new ArrayList<>();
        Map<String, String> formulaMap = new HashMap<>();
        Map<String, Double> variableValues = new HashMap<>();
        for (SalaryTemplateItem salaryTemplateItem : salaryTemplate.getTemplateItems()) {
            SalaryResultItemDetail salaryResultItemDetail = salaryResultItemDetailRepository.findBySalaryResultItemIdAndSalaryTemplateItemId(salaryResultItem.getId(), salaryTemplateItem.getId());
            if (salaryResultItemDetail == null) {
                salaryResultItemDetail = new SalaryResultItemDetail();
            }
            salaryResultItemDetail.setSalaryTemplateItem(salaryTemplateItem);
            salaryResultItemDetail.setSalaryResultItem(salaryResultItem);
            if (salaryTemplateItem.getSalaryItemType() != null && salaryTemplateItem.getSalaryItemType().equals(DatnConstants.SalaryItemType.VALUE.getValue())) {
                salaryResultItemDetail.setValue(salaryTemplateItem.getDefaultAmount());
                variableValues.put(salaryTemplateItem.getCode(), salaryTemplateItem.getDefaultAmount());
            } else if (salaryTemplateItem.getSalaryItemType() != null && salaryTemplateItem.getSalaryItemType().equals(DatnConstants.SalaryItemType.SYSTEM.getValue())) {
                if (salaryTemplateItem.getCode() != null && salaryTemplateItem.getCode().equals(DatnConstants.SalaryTemplateItemSystem.BASIC_SALARY.getCode())) {
                    //lấy lương cơ bản từ hợp đồng đã ký
                    //hợp đồng mới nhất và vẫn còn hiệu lực
                    UUID staffId = salaryResultItem.getStaff().getId();
                    List<StaffLabourAgreement> signedAgreements =
                            staffLabourAgreementRepository.findByStaffIdAndAgreementStatus(staffId, DatnConstants.StaffLabourAgreementStatus.SIGNED.getValue());
                    Double salary = 0.0;
                    if (signedAgreements != null && !signedAgreements.isEmpty()) {
                        salary = signedAgreements.get(0).getSalary();
                    }

                    salaryResultItemDetail.setValue(salary);
                    variableValues.put(salaryTemplateItem.getCode(), salary);

                } else if (salaryTemplateItem.getCode() != null && salaryTemplateItem.getCode().equals(DatnConstants.SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.getCode())) {
                    //lấy ngày công thực tế nhân viên chấm công được từ StaffWorkSchedule với toDate, endDate của SalaryPerious
                    Date fromDate = salaryResultItem.getSalaryResult().getSalaryPeriod().getStartDate();
                    Date toDate = salaryResultItem.getSalaryResult().getSalaryPeriod().getEndDate();
                    UUID staffId = salaryResultItem.getStaff().getId();
                    //Lấy tổng số công được tính
                    Double numberWorkingDays = this.calculateWorkingDays(staffId, fromDate, toDate);
                    salaryResultItemDetail.setValue(numberWorkingDays);
                    variableValues.put(salaryTemplateItem.getCode(), numberWorkingDays);

                } else if (salaryTemplateItem.getCode() != null && salaryTemplateItem.getCode().equals(DatnConstants.SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.getCode())) {
                    salaryResultItemDetail.setValue(salaryResultItem.getSalaryResult().getSalaryPeriod().getEstimatedWorkingDays());
                    variableValues.put(salaryTemplateItem.getCode(), salaryResultItem.getSalaryResult().getSalaryPeriod().getEstimatedWorkingDays());
                }
            } else {
                salaryResultItemDetailTypeFormula.add(salaryResultItemDetail);
                formulaMap.put(salaryTemplateItem.getCode(), salaryTemplateItem.getFormula());
            }
            if (salaryResultItem.getSalaryResultItemDetails() == null || salaryResultItem.getSalaryResultItemDetails().isEmpty()) {
                salaryResultItem.setSalaryResultItemDetails(new HashSet<>());
            }
            salaryResultItem.getSalaryResultItemDetails().add(salaryResultItemDetail);
        }
        // Kiểm tra phụ thuộc vòng lặp trong công thức
        if (!formulaMap.isEmpty() && hasCircularDependency(formulaMap)) {
            throw new InvalidDataException("Phát hiện vòng lặp trong công thức lương");
        } else {
            for (SalaryResultItemDetail itemDetail : salaryResultItemDetailTypeFormula) {
                String code = itemDetail.getSalaryTemplateItem().getCode();
                String formula = itemDetail.getSalaryTemplateItem().getFormula();
                if (code != null && formula != null) {
                    double value = this.evaluateExpression(formula, variableValues);
                    itemDetail.setValue(value);
                }
            }

        }
    }

    public double evaluateExpression(String expr, Map<String, Double> variableValues) {
        // 1. Tách tất cả các biến từ biểu thức
        Set<String> variables = extractVariables(expr);

        // 2. Kiểm tra xem các biến đã có giá trị chưa
        for (String var : variables) {
            if (!variableValues.containsKey(var)) {
                throw new InvalidDataException("Thiếu giá trị cho biến '" + var + "' trong công thức: " + expr);
            }
        }

        // 3. Xây biểu thức với exp4j
        Expression e = new ExpressionBuilder(expr)
                .variables(variables)
                .build();

        // 4. Gán giá trị cho từng biến
        for (String var : variables) {
            e.setVariable(var, variableValues.get(var));
        }

        // 5. Trả kết quả
        return e.evaluate();
    }

    private Double calculateWorkingDays(UUID staffId, Date startDate, Date endDate) {
        List<ShiftWorkTypeCount> counts = staffWorkScheduleRepository.countShiftWorkTypeByStaffInDateRange(staffId, startDate, endDate);

        double morning = 0.0, afternoon = 0.0, fullDay = 0.0;

        for (ShiftWorkTypeCount item : counts) {
            if (item.getType() != null) {
                if (item.getType().equals(DatnConstants.ShiftWorkType.MORNING.getValue())) {
                    morning = item.getCount();
                } else if (item.getType().equals(DatnConstants.ShiftWorkType.AFTERNOON.getValue())) {
                    afternoon = item.getCount();
                } else if (item.getType().equals(DatnConstants.ShiftWorkType.FULL_DAY.getValue())) {
                    fullDay = item.getCount();
                }
            }
        }
        morning = morning * DatnConstants.ShiftWorkType.MORNING.getCalculatedWorkingDay();
        afternoon = afternoon * DatnConstants.ShiftWorkType.AFTERNOON.getCalculatedWorkingDay();
        fullDay = fullDay * DatnConstants.ShiftWorkType.FULL_DAY.getCalculatedWorkingDay();
        return morning + afternoon + fullDay;
    }

    private boolean hasCircularDependency(Map<String, String> formulaMap) {
        Map<String, Set<String>> dependencyGraph = new HashMap<>();
        for (Map.Entry<String, String> entry : formulaMap.entrySet()) {
            dependencyGraph.put(entry.getKey(), extractVariables(entry.getValue()));
        }

        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();

        for (String node : dependencyGraph.keySet()) {
            if (dfsHasCycle(node, dependencyGraph, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private Set<String> extractVariables(String formula) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = Pattern.compile("\\b[A-Za-z_][A-Za-z0-9_]*\\b").matcher(formula);
        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }

    private boolean dfsHasCycle(String node, Map<String, Set<String>> graph, Set<String> visited, Set<String> stack) {
        if (stack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        stack.add(node);

        for (String neighbor : graph.getOrDefault(node, new HashSet<>())) {
            if (dfsHasCycle(neighbor, graph, visited, stack)) {
                return true;
            }
        }

        stack.remove(node);
        return false;
    }
}
