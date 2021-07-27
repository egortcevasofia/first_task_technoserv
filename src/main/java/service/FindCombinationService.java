package service;

import domain.Department;
import domain.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FindCombinationService {
    public ReadAndValidateService readAndValidateService = new ReadAndValidateService();
    private Comparator<Department> comparator = (d1, d2) -> d1.getAverageSalary().compareTo(d2.getAverageSalary());

    public void findCombination(String pathForRead, String pathForWrite) {
        Map<String, Department> validDepartments = readAndValidateService.readFromFile(pathForRead);
        System.out.println(validDepartments);
        List<Department> listOfDepartments = new ArrayList<>(validDepartments.values());
        listOfDepartments.sort(comparator);

        for (int i = 0; i < listOfDepartments.size(); i++) {
            for (int j = i + 1; j < listOfDepartments.size(); j++) {
                writeCombinationToFile(listOfDepartments.get(i), listOfDepartments.get(j), pathForWrite);
            }
        }
    }


    private void writeCombinationToFile(Department lessDepartment, Department moreDepartment, String pathForWrite) {
        try (FileWriter writer = new FileWriter(pathForWrite, true)) {
            BigDecimal bigSalary = moreDepartment.getAverageSalary();
            BigDecimal smallSalary = lessDepartment.getAverageSalary();
            List<Employee> employeeList = moreDepartment.getEmployeeList();
            writer.write(String.format("Возможен перевод из департамента " + moreDepartment.getName() + " в департамент "
                    + lessDepartment.getName() + ", где средние зарплаты: " + bigSalary.toString()
                    + " и " + smallSalary.toString() + " соответственно, следующих сотрудников: \n"));
            for (int i = 0; i < employeeList.size(); i++) {
                if (bigSalary.compareTo(employeeList.get(i).getSalary()) > 0
                        && employeeList.get(i).getSalary().compareTo(smallSalary) > 0) {

                    writer.write(lineForWrite(employeeList.get(i), moreDepartment, lessDepartment));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }


    String lineForWrite(Employee employee, Department moreDepartment, Department lessDepartment){
        BigDecimal bigSalary = moreDepartment.getSumSalary();
        BigDecimal smallSalary = lessDepartment.getSumSalary();

        BigDecimal newBigAverageSalary = (bigSalary.subtract(employee.getSalary()))
                .divide(new BigDecimal(moreDepartment.getEmployeeList().size() - 1), 2, RoundingMode.HALF_UP);

        BigDecimal newSmallAverageSalary = (smallSalary.add(employee.getSalary()))
                .divide(new BigDecimal(lessDepartment.getEmployeeList().size() + 1), 2, RoundingMode.HALF_UP);

        String lineForWrite = employee.toString() + " from " + moreDepartment.getName()
                + " to " + lessDepartment.getName()
                + " после перевода среднии зарплаты составлят соответственно,"
        + newBigAverageSalary.toString() + " и " + newSmallAverageSalary.toString() + "\n";
        return lineForWrite;
    }

}
