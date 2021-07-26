package service;

import domain.Department;
import domain.Employee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FindCombinationService {
    public ReadAndValidateService readAndValidateService = new ReadAndValidateService();
    private Comparator<Department> comparator = (d1, d2) -> d1.getAverageSalary(d1).compareTo(d2.getAverageSalary(d2));

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
            BigDecimal bigSalary = moreDepartment.getAverageSalary(moreDepartment);
            BigDecimal smallSalary = lessDepartment.getAverageSalary(lessDepartment);
            List<Employee> employeeList = moreDepartment.getEmployeeList();
            writer.write(String.format("Возможен перевод из департамента " + moreDepartment.getName() + " в департамент "
                    + lessDepartment.getName() + ", где средние зарплаты: " + bigSalary.toString()
                    + " и " + smallSalary.toString() + " соответственно, следующих сотрудников: \n"));
            for (int i = 0; i < employeeList.size(); i++) {
                if (bigSalary.compareTo(employeeList.get(i).getSalary()) > 0
                        && employeeList.get(i).getSalary().compareTo(smallSalary) > 0) {

                    List<BigDecimal> avarageSalaryAfter = getAverageSalaryAfter(moreDepartment, lessDepartment, employeeList.get(i));

                    String lineForWrite = employeeList.get(i).toString() + " from " + moreDepartment.getName()
                            + " to " + lessDepartment.getName()
                            + " после перевода среднии зарплаты составлят соответственно,"
                            + avarageSalaryAfter.get(0).toString() + " и " + avarageSalaryAfter.get(1).toString() + "\n";
                    writer.write(lineForWrite);
                }
            }
        } catch (IOException e) {
            System.out.println("Некорректный путь для файла с результатами");
        }
    }


    private List<BigDecimal> getAverageSalaryAfter(Department moreDepartmen, Department lessDepartment, Employee employee) {
        List<BigDecimal> list = new ArrayList<>();
        moreDepartmen.getEmployeeList().remove(employee);
        lessDepartment.getEmployeeList().add(employee);
        list.add(moreDepartmen.getAverageSalary(moreDepartmen));
        list.add(lessDepartment.getAverageSalary(lessDepartment));
        return list;
    }

}
