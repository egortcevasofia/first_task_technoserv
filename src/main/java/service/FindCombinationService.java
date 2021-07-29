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

public class FindCombinationService {
    private ReadAndValidateService readAndValidateService = new ReadAndValidateService();
    private Comparator<Department> comparator = (d1, d2) -> d1.getAverageSalary().compareTo(d2.getAverageSalary());

    public void findCombination(String pathForRead, String pathForWrite) {
        ArrayList<Department> listOfDepartments = readAndValidateService.readFromFile(pathForRead);
        readAndValidateService.printToConsole(listOfDepartments);

        for (int i = 0; i < listOfDepartments.size(); i++) {
            for (int j = i + 1; j < listOfDepartments.size(); j++) {
                writeCombinationToFile(listOfDepartments.get(i), listOfDepartments.get(j), pathForWrite);
            }
        }
    }


    private void writeCombinationToFile(Department leastDepartment, Department biggestDepartment, String pathForWrite) {
        try (FileWriter writer = new FileWriter(pathForWrite, false)) {
            BigDecimal bigSalary = biggestDepartment.getAverageSalary();
            BigDecimal smallSalary = leastDepartment.getAverageSalary();
            List<Employee> employeeList = biggestDepartment.getEmployeeList();
            writer.write("Возможен перевод из департамента " + biggestDepartment.getName() + " в департамент "
                    + leastDepartment.getName() + ", где средние зарплаты: " + bigSalary.toString()
                    + " и " + smallSalary.toString() + " соответственно, следующих сотрудников: \n");
            for (Employee employee : employeeList) {
                if (bigSalary.compareTo(employee.getSalary()) > 0
                        && employee.getSalary().compareTo(smallSalary) > 0) {

                    writer.write(lineForWrite(employee, biggestDepartment, leastDepartment));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
    }


    private String lineForWrite(Employee employee, Department biggestDepartment, Department leastDepartment) {
        BigDecimal bigSalary = biggestDepartment.getSumSalary();
        BigDecimal smallSalary = leastDepartment.getSumSalary();

        BigDecimal newBigAverageSalary = (bigSalary.subtract(employee.getSalary()))
                .divide(new BigDecimal(biggestDepartment.getEmployeeList().size() - 1), 2, RoundingMode.HALF_UP);

        BigDecimal newSmallAverageSalary = (smallSalary.add(employee.getSalary()))
                .divide(new BigDecimal(leastDepartment.getEmployeeList().size() + 1), 2, RoundingMode.HALF_UP);

        return employee.toString() + " from " + biggestDepartment.getName()
                + " to " + leastDepartment.getName()
                + " после перевода среднии зарплаты составлят соответственно,"
                + newBigAverageSalary.toString() + " и " + newSmallAverageSalary.toString() + "\n";
    }

}
