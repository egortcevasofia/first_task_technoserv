import service.FindCombinationService;

public class Main {
    public static void main(String[] args) {

        if (args.length != 2) try {
            throw new Exception("Неверно заданы пути для файлов чтения и записи");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        FindCombinationService findCombinationService = new FindCombinationService();
        findCombinationService.findCombination(args[0], args[1]);
    }
}
