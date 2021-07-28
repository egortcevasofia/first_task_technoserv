import service.DoRecursionService;
import service.FindCombinationService;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3) try {
            throw new Exception("Неверно заданы пути для файлов чтения и записи");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        FindCombinationService findCombinationService = new FindCombinationService();
        findCombinationService.findCombination(args[0], args[1]);

        DoRecursionService doRecursionService = new DoRecursionService();
        doRecursionService.findAllPossibleCombination(args[0], args[2]);

    }
}


