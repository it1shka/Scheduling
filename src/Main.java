import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        var processes = getProcesses();
        System.out.println("Here are all the processes that wait for scheduling: ");
        printProcesses(processes);

        Scheduling.FirstComeFirstServe(processes);
        Scheduling.ShortestJobFirst(processes);
        Scheduling.RoundRobin(processes);
        Scheduling.PreemptiveShortestJobFirst(processes);
    }

    public static void printProcesses(int[] processes) {
        var processString = Arrays.stream(processes)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        System.out.println(processString);
    }

    private static int[] getProcesses() {
        var scanner = new Scanner(System.in);

        System.out.print("Select the amount of processes: ");
        var amount = scanner.nextInt();
        scanner.nextLine();
        var processes = new int[amount];

        boolean randomize;
        while (true) {
            System.out.print("Do you want to randomize processes? [Y/n] ");
            var input = scanner.nextLine().toLowerCase();
            if (input.equals("") || input.equals("y") || input.equals("yes")) {
                randomize = true;
                break;
            }
            if (input.equals("n") || input.equals("no")) {
                randomize = false;
                break;
            }
        }

        if (randomize) {
            var random = new Random();
            System.out.print("Lower bound: ");
            var lower = scanner.nextInt();
            System.out.print("Upper bound: ");
            var upper = scanner.nextInt();
            for (var i = 0; i < amount; i++) {
                processes[i] = random.nextInt(upper - lower + 1) + lower;
            }
        } else {
            for (var i = 0; i < amount; i++) {
                System.out.print("Process " + (i + 1) + ": ");
                processes[i] = scanner.nextInt();
            }
        }

        scanner.close();
        return processes;
    }

}
