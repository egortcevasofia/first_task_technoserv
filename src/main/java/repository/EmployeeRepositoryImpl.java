package repository;

import domain.Department;
import domain.Employee;
import utils.FileWriterUtility;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EmployeeRepositoryImpl {

    private static final String PATH_TO_FILE = "C://employees.txt";
    private static final String PATH_TO_COMBINATION = "C://Users//segortseva//combination.txt";
    private Path path = Path.of(PATH_TO_FILE);
    private Path pathToCombination = Path.of(PATH_TO_COMBINATION);
    private Map<String, Department> mapOfDepartment = new HashMap<>();


    public Map<String, Department> getAllDepartment() {
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Department department = parseDepartment(line);
                Employee employee = parseEmployeeFromLine(line);
                if (mapOfDepartment.containsKey(department.getName())) {
                    department = mapOfDepartment.get(department.getName());
                    List<Employee> listOfEmployee = department.getEmployeeList();
                    listOfEmployee.add(employee);
                    department.setEmployeeList(listOfEmployee);
                    mapOfDepartment.put(department.getName(), department);
                } else {
                    Department newDepartment = new Department();
                    List<Employee> newListOfEmployee = new ArrayList<>();
                    newListOfEmployee.add(employee);
                    newDepartment.setEmployeeList(newListOfEmployee);
                    newDepartment.setName(department.getName());
                    mapOfDepartment.put(department.getName(), newDepartment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapOfDepartment;
    }

    public BigDecimal getAverageSalary(String department) {
        List<BigDecimal> list = mapOfDepartment
                .get(department)
                .getEmployeeList()
                .stream()
                .map(e -> e.getSalary())
                .collect(Collectors.toList());

        BigDecimal summ = BigDecimal.ZERO;

        for (BigDecimal bigDecimal : list) {
            summ = summ.add(bigDecimal);
        }

        return summ.divide(BigDecimal.valueOf(list.size()), RoundingMode.HALF_DOWN);
    }

    public void findCombination() throws IOException {
        List<Department> listOfDepartments = new ArrayList<>(getAllDepartment().values());

        for (int i = 0; i < listOfDepartments.size(); i++) {
            for (int j = i + 1; j < listOfDepartments.size(); j++) {
                int comp = getAverageSalary(listOfDepartments.get(i).getName())
                        .compareTo(getAverageSalary(listOfDepartments.get(j).getName()));

                if (comp > 0) {
                    writeCombinationToFile(listOfDepartments.get(i), listOfDepartments.get(j));
                }
                if (comp < 0) {
                    writeCombinationToFile(listOfDepartments.get(j), listOfDepartments.get(i));
                }
            }
        }
    }

    public void writeCombinationToFile(Department moreDepartment, Department lessDepartment) throws IOException {
        BigDecimal bigSalary = getAverageSalary(moreDepartment.getName());
        BigDecimal smallSalary = getAverageSalary(lessDepartment.getName());

        List<Employee> employeeList = moreDepartment.getEmployeeList();
        for (int i = 0; i < employeeList.size(); i++) {
            if (bigSalary.compareTo(employeeList.get(i).getSalary()) > 0
                    && employeeList.get(i).getSalary().compareTo(smallSalary) > 0) {
                String lineForWrite = employeeList.get(i).toString() + " from " + moreDepartment.getName()
                        + " to " + lessDepartment.getName();
                FileWriterUtility.writeLineToFile(lineForWrite, PATH_TO_COMBINATION);
            }
        }
    }


    private Employee parseEmployeeFromLine(String line) {
        String name = null;
        BigDecimal salary = null;
        Pattern pattern = Pattern.compile("(\\w.+)[,]\\s(\\w.+)[,]\\s(\\d+\\.\\d+|\\d+)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            name = matcher.group(1);
            salary = BigDecimal.valueOf(Double.parseDouble(matcher.group(3)));
        }
        Employee employee = new Employee();
        employee.setName(name);
        employee.setSalary(salary);
        return employee;
    }


    private Department parseDepartment(String line) {
        String departmentName = null;
        Pattern pattern = Pattern.compile("(\\w.+)[,]\\s(\\w.+)[,]\\s(\\d+\\.\\d+|\\d+)");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            departmentName = matcher.group(2);
        }
        Department department = new Department();
        department.setName(departmentName);
        return department;
    }

}




