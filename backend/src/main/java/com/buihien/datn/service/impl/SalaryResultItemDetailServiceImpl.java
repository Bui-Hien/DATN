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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SalaryResultItemDetailServiceImpl extends GenericServiceImpl<SalaryResultItemDetail, SalaryResultItemDetailDto, SearchDto> implements SalaryResultItemDetailService {
    private static final Logger logger = LoggerFactory.getLogger(SalaryResultItemDetailServiceImpl.class);

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
        logger.info("Bắt đầu xử lý setup salary result item detail cho staff ID: {}",
                salaryResultItem != null && salaryResultItem.getStaff() != null ? salaryResultItem.getStaff().getId() : "null");
        // 1. Kiểm tra đầu vào
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

        // 2. Khởi tạo các chi tiết lương và các biến hỗ trợ
        Map<String, SalaryResultItemDetail> detailMap = new HashMap<>();
        Map<String, Double> variableValues = new HashMap<>();
        Map<String, String> formulaMap = new HashMap<>();

        logger.info("Số lượng template items: {}", salaryTemplate.getTemplateItems().size());

        // Sắp xếp template items theo displayOrder để đảm bảo thứ tự xử lý đúng
        List<SalaryTemplateItem> sortedItems = salaryTemplate.getTemplateItems().stream()
                .sorted(Comparator.comparingInt(SalaryTemplateItem::getDisplayOrder))
                .collect(Collectors.toList());

        for (SalaryTemplateItem item : sortedItems) {
            SalaryResultItemDetail detail = salaryResultItemDetailRepository.findBySalaryResultItemIdAndSalaryTemplateItemId(salaryResultItem.getId(), item.getId());
            if (detail == null) {
                detail = new SalaryResultItemDetail();
            }
            detail.setSalaryResultItem(salaryResultItem);
            detail.setSalaryTemplateItem(item);

            String code = item.getCode();
            Integer type = item.getSalaryItemType();

            if (DatnConstants.SalaryItemType.VALUE.getValue().equals(type)) {
                // Giá trị cố định
                Double value = (item.getDefaultAmount() != null) ? item.getDefaultAmount() : 0.0;
                detail.setValue(value);
                variableValues.put(code, value);
                logger.debug("Xử lý VALUE - Code: {}, Value: {}", code, value);
            } else if (DatnConstants.SalaryItemType.SYSTEM.getValue().equals(type)) {
                // Giá trị lấy từ hệ thống
                Double val = getSystemValue(salaryResultItem, item);
                detail.setValue(val);
                variableValues.put(code, val);
                logger.debug("Xử lý SYSTEM - Code: {}, Value: {}", code, val);
            } else if (DatnConstants.SalaryItemType.FORMULA.getValue().equals(type)) {
                // Công thức tính sau
                if (item.getFormula() != null && !item.getFormula().trim().isEmpty()) {
                    formulaMap.put(code, item.getFormula().trim());
                    logger.debug("Xử lý FORMULA - Code: {}, Formula: {}", code, item.getFormula().trim());
                }
            }

            detailMap.put(code, detail);
        }

        logger.info("Tổng số biến có sẵn: {}, Variables: {}", variableValues.size(), variableValues.keySet());
        logger.info("Tổng số công thức cần tính: {}, Formulas: {}", formulaMap.size(), formulaMap.keySet());

        // 3. Kiểm tra vòng lặp công thức
        if (hasCircularDependency(formulaMap)) {
            throw new InvalidDataException("Phát hiện vòng lặp trong công thức lương");
        }

        // 4. Tính toán công thức theo thứ tự phụ thuộc
        List<String> sortedKeys = topologicalSort(formulaMap, variableValues.keySet());
        logger.info("Thứ tự tính toán công thức: {}", sortedKeys);

        for (String code : sortedKeys) {
            String formula = formulaMap.get(code);
            if (formula == null) continue;

            logger.debug("Đang tính toán công thức cho '{}': {}", code, formula);
            logger.debug("Các biến có sẵn: {}", variableValues);

            try {
                double value = evaluateExpression(formula, variableValues);
                variableValues.put(code, value);

                SalaryResultItemDetail detail = detailMap.get(code);
                if (detail != null) {
                    detail.setValue(value);
                }
                logger.info("Tính toán thành công '{}' = {}", code, value);
            } catch (Exception e) {
                logger.error("Lỗi khi tính toán công thức cho '{}': {}", code, e.getMessage());
                throw new InvalidDataException("Lỗi khi tính toán công thức cho '" + code + "': " + e.getMessage());
            }
        }

        // 5. Gán lại danh sách chi tiết lương
        salaryResultItem.setSalaryResultItemDetails(new HashSet<>(detailMap.values()));
        logger.info("Hoàn thành xử lý setup salary result item detail cho staff ID: {}",
                salaryResultItem.getStaff().getId());
    }

    // Lấy giá trị SYSTEM từ dữ liệu liên quan (hợp đồng, kỳ lương...)
    private Double getSystemValue(SalaryResultItem salaryResultItem, SalaryTemplateItem item) {
        String code = item.getCode();
        UUID staffId = salaryResultItem.getStaff().getId();

        logger.debug("Lấy system value cho code: {}", code);

        try {
            if (DatnConstants.SalaryTemplateItemSystem.BASIC_SALARY.getCode().equals(code)) {
                List<StaffLabourAgreement> agreements =
                        staffLabourAgreementRepository.findByStaffIdAndAgreementStatus(staffId, DatnConstants.StaffLabourAgreementStatus.SIGNED.getValue());
                Double value = (agreements != null && !agreements.isEmpty()) ?
                        (agreements.get(0).getSalary() != null ? agreements.get(0).getSalary() : 0.0) : 0.0;
                logger.debug("BASIC_SALARY = {}", value);
                return value;
            } else if (DatnConstants.SalaryTemplateItemSystem.ACTUAL_NUMBER_OF_WORKING_DAYS.getCode().equals(code)) {
                Date fromDate = salaryResultItem.getSalaryResult().getSalaryPeriod().getStartDate();
                Date toDate = salaryResultItem.getSalaryResult().getSalaryPeriod().getEndDate();
                Double value = calculateWorkingDays(staffId, fromDate, toDate);
                logger.debug("ACTUAL_NUMBER_OF_WORKING_DAYS = {}", value);
                return value;
            } else if (DatnConstants.SalaryTemplateItemSystem.STANDARD_NUMBER_OF_WORKING_DAYS.getCode().equals(code)) {
                Double estimatedDays = salaryResultItem.getSalaryResult().getSalaryPeriod().getEstimatedWorkingDays();
                Double value = estimatedDays != null ? estimatedDays : 0.0;
                logger.debug("STANDARD_NUMBER_OF_WORKING_DAYS = {}", value);
                return value;
            } else {
                logger.debug("Unknown system code: {}, returning 0.0", code);
                return 0.0;
            }
        } catch (Exception e) {
            logger.error("Lỗi khi lấy system value cho code '{}': {}", code, e.getMessage());
            return 0.0;
        }
    }

    // Kiểm tra vòng lặp công thức - được cải thiện
    private boolean hasCircularDependency(Map<String, String> formulaMap) {
        Map<String, Set<String>> graph = buildDependencyGraph(formulaMap);
        Set<String> visited = new HashSet<>();
        Set<String> visiting = new HashSet<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                if (dfsDetectCycle(node, graph, visited, visiting)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfsDetectCycle(String node, Map<String, Set<String>> graph, Set<String> visited, Set<String> visiting) {
        visiting.add(node);
        Set<String> neighbors = graph.getOrDefault(node, Collections.emptySet());

        for (String neighbor : neighbors) {
            if (visiting.contains(neighbor)) {
                return true; // Phát hiện chu trình
            }
            if (!visited.contains(neighbor) && dfsDetectCycle(neighbor, graph, visited, visiting)) {
                return true;
            }
        }

        visiting.remove(node);
        visited.add(node);
        return false;
    }

    // Sắp xếp topological được cải thiện
    private List<String> topologicalSort(Map<String, String> formulaMap, Set<String> availableVariables) {
        logger.debug("Bắt đầu topological sort với {} công thức", formulaMap.size());
        logger.debug("Available variables: {}", availableVariables);

        Map<String, Set<String>> dependencyGraph = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Khởi tạo dependency graph và in-degree
        for (String formulaCode : formulaMap.keySet()) {
            String formula = formulaMap.get(formulaCode);
            Set<String> dependencies = extractVariables(formula);

            // Chỉ giữ lại các dependency thuộc formulaMap (chưa được tính toán)
            Set<String> formulaDependencies = dependencies.stream()
                    .filter(dep -> formulaMap.containsKey(dep))
                    .collect(Collectors.toSet());

            dependencyGraph.put(formulaCode, formulaDependencies);
            inDegree.put(formulaCode, formulaDependencies.size());

            logger.debug("Formula '{}' depends on: {}", formulaCode, formulaDependencies);
        }

        // Kahn's algorithm
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
                logger.debug("Added to queue (no dependencies): {}", entry.getKey());
            }
        }

        List<String> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            logger.debug("Processing: {}", current);

            // Giảm in-degree của các node phụ thuộc vào current
            for (String formulaCode : formulaMap.keySet()) {
                Set<String> deps = dependencyGraph.get(formulaCode);
                if (deps.contains(current)) {
                    int newInDegree = inDegree.get(formulaCode) - 1;
                    inDegree.put(formulaCode, newInDegree);
                    logger.debug("Reduced in-degree of '{}' to {}", formulaCode, newInDegree);

                    if (newInDegree == 0) {
                        queue.offer(formulaCode);
                        logger.debug("Added to queue: {}", formulaCode);
                    }
                }
            }
        }

        if (result.size() != formulaMap.size()) {
            logger.error("Topological sort failed. Expected: {}, Got: {}", formulaMap.size(), result.size());
            throw new InvalidDataException("Không thể sắp xếp thứ tự tính toán do có vòng lặp phụ thuộc");
        }

        logger.info("Topological sort completed. Order: {}", result);
        return result;
    }

    // Xây dựng đồ thị phụ thuộc
    private Map<String, Set<String>> buildDependencyGraph(Map<String, String> formulaMap) {
        Map<String, Set<String>> graph = new HashMap<>();

        for (Map.Entry<String, String> entry : formulaMap.entrySet()) {
            String formula = entry.getKey();
            Set<String> dependencies = extractVariables(entry.getValue());
            graph.put(formula, dependencies);
        }

        return graph;
    }

    // Trích xuất biến trong công thức - được cải thiện
    private Set<String> extractVariables(String formula) {
        Set<String> variables = new HashSet<>();
        if (formula == null || formula.trim().isEmpty()) {
            return variables;
        }

        // Pattern để match tên biến: bắt đầu bằng chữ cái hoặc _, theo sau là chữ cái, số hoặc _
        Pattern pattern = Pattern.compile("\\b[A-Za-z_][A-Za-z0-9_]*\\b");
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {
            String variable = matcher.group();
            // Loại bỏ các từ khóa toán học
            if (!isReservedWord(variable)) {
                variables.add(variable);
            }
        }

        return variables;
    }

    // Kiểm tra từ khóa toán học
    private boolean isReservedWord(String word) {
        Set<String> reservedWords = Set.of(
                "sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh",
                "log", "ln", "exp", "sqrt", "abs", "ceil", "floor", "round", "max", "min",
                "PI", "E", "true", "false"
        );
        return reservedWords.contains(word.toLowerCase());
    }

    // Đánh giá biểu thức công thức - được cải thiện
    public double evaluateExpression(String expr, Map<String, Double> variableValues) {
        if (expr == null || expr.trim().isEmpty()) {
            return 0.0;
        }

        Set<String> variables = extractVariables(expr);
        logger.debug("Evaluating expression: '{}', Variables needed: {}", expr, variables);

        // Kiểm tra tất cả biến có giá trị
        for (String var : variables) {
            if (!variableValues.containsKey(var)) {
                logger.error("Missing variable '{}' in expression '{}'. Available variables: {}",
                        var, expr, variableValues.keySet());
                throw new InvalidDataException("Thiếu giá trị cho biến '" + var + "' trong công thức: " + expr);
            }
            logger.debug("Variable '{}' = {}", var, variableValues.get(var));
        }

        try {
            Expression e = new ExpressionBuilder(expr)
                    .variables(variables)
                    .build();

            for (String var : variables) {
                Double value = variableValues.get(var);
                e.setVariable(var, value != null ? value : 0.0);
            }

            double result = e.evaluate();
            logger.debug("Expression '{}' evaluated to: {}", expr, result);
            return result;
        } catch (Exception ex) {
            logger.error("Error evaluating expression '{}': {}", expr, ex.getMessage());
            throw new InvalidDataException("Lỗi khi đánh giá công thức '" + expr + "': " + ex.getMessage());
        }
    }

    private Double calculateWorkingDays(UUID staffId, Date startDate, Date endDate) {
        try {
            logger.debug("Calculating working days for staff {} from {} to {}", staffId, startDate, endDate);

            List<ShiftWorkTypeCount> counts = staffWorkScheduleRepository.countShiftWorkTypeByStaffInDateRange(staffId, startDate, endDate);

            double morning = 0.0, afternoon = 0.0, fullDay = 0.0;

            for (ShiftWorkTypeCount item : counts) {
                if (item.getType() != null && item.getCount() != null) {
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

            double total = morning + afternoon + fullDay;
            logger.debug("Working days calculation: morning={}, afternoon={}, fullDay={}, total={}",
                    morning, afternoon, fullDay, total);

            return total;
        } catch (Exception e) {
            logger.error("Error calculating working days for staff {}: {}", staffId, e.getMessage());
            return 0.0;
        }
    }
}