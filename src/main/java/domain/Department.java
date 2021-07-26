package domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Department{
    private String name;
    private List<Employee> employeeList;

    public Department(String name) {
        this.name = name;
    }

    public Department(String name, List<Employee> employeeList) {
        this.name = name;
        this.employeeList = employeeList;
    }

    public Department() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", employeeList=" + employeeList +
                '}';
    }

    public BigDecimal getAverageSalary(Department department) {
        List<BigDecimal> list = department.getEmployeeList().stream()
                .map(e -> e.getSalary())
                .collect(Collectors.toList());

        BigDecimal summ = BigDecimal.ZERO;

        for (BigDecimal bigDecimal : list) {
            summ = summ.add(bigDecimal);
        }

        return summ.divide(new BigDecimal(list.size()), 2, RoundingMode.HALF_UP);
    }


}
