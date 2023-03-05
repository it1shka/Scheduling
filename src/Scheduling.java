import java.util.Arrays;
import java.util.LinkedList;

public class Scheduling {

    // algorithms non-preemptive (FCFS, SJF) and preemptive (RR, pSJF)

    public static void FirstComeFirstServe(int[] processes) {
        System.out.println();
        System.out.println(AnsiColors.YELLOW + "Testing FCFS algorithm: " + AnsiColors.RESET);
        executeNonPreemptive(processes);
    }

    public static void ShortestJobFirst(int[] processes) {
        System.out.println();
        System.out.println(AnsiColors.YELLOW + "Testing SJF algorithm: " + AnsiColors.RESET);
        executeNonPreemptive(sorted(processes));
    }

    public static void RoundRobin(int[] processes) {
        System.out.println();
        System.out.println(AnsiColors.YELLOW + "Testing RR algorithm: " + AnsiColors.RESET);
        executePreemptive(processes);
    }

    public static void PreemptiveShortestJobFirst(int[] processes) {
        System.out.println();
        System.out.println(AnsiColors.YELLOW + "Testing pSJF algorithm: " + AnsiColors.RESET);
        executePreemptive(sorted(processes));
    }

    // two main approaches how to execute processes:
    // non-preemptive (sequential) execution
    // and preemptive (parallel with time split) execution

    private static void executeNonPreemptive(int[] processes) {
        var currentWaitTime = 0;
        var totalWaitTime = 0;

        for (var i = 0; i < processes.length; i++) {
            var process = processes[i];
            System.out.format("Executing process #%d (%d sec): wait %d\n", i+1, process, currentWaitTime);
            totalWaitTime += currentWaitTime;
            currentWaitTime += process;
        }

        System.out.println(AnsiColors.RED + "Total execution time: " + currentWaitTime + AnsiColors.RESET);
        var executionAverage = (double)currentWaitTime / (double)processes.length;
        System.out.println(AnsiColors.GREEN + "Avg execution time: " + executionAverage + AnsiColors.RESET);

        System.out.println(AnsiColors.RED + "Total wait time: " + totalWaitTime + AnsiColors.RESET);
        var waitAverage = (double)totalWaitTime / (double)processes.length;
        System.out.println(AnsiColors.GREEN + "Avg wait time: " + waitAverage + AnsiColors.RESET);
    }

    private static final int delta = 3;

    // helper class for preemptive execution

    private static class Process {

        private final String name;
        private final int startTime;
        private int timeLeft;

        public Process(String name, int startTime, int timeLeft) {
            this.name = name;
            this.startTime = startTime;
            this.timeLeft = timeLeft;
        }

        public void execute() {
            var prev = this.timeLeft;
            timeLeft -= delta;

            System.out.print(name + " executed. ");
            if (timeLeft <= 0) {
                System.out.println("Process finished. ");
            } else {
                System.out.println("Time reduced from " + prev + " to " + timeLeft + ". ");
            }
        }

        public int getStartTime() {
            return startTime;
        }

        public boolean finished() {
            return timeLeft <= 0;
        }

    }

    private static void executePreemptive(int[] processes) {
        var activeProcesses = new LinkedList<Process>();
        var totalWaitTime = 0;
        for (var i = 0; i < processes.length; i++) {
            var name = "Process " + (i + 1);
            var startTime = delta * i;
            var processTime = processes[i];
            var process = new Process(name, startTime, processTime);
            activeProcesses.add(process);
            totalWaitTime += startTime;
        }
        var averageWaitTime = (double)totalWaitTime / (double)processes.length;

        var time = 0;
        var totalExecutionTime = 0;
        var currentProcess = 0;
        while (activeProcesses.size() > 0) {
            var process = activeProcesses.get(currentProcess % activeProcesses.size());
            process.execute();
            time += delta;
            if (process.finished()) {
                var executionDelta = time - process.getStartTime();
                System.out.println(AnsiColors.CYAN + "+" + executionDelta + " sec of execution" + AnsiColors.RESET);
                totalExecutionTime += executionDelta;
                activeProcesses.remove(process);
            } else currentProcess++;
        }
        var averageExecutionTime = (double)totalExecutionTime / (double)processes.length;

        System.out.println(AnsiColors.RED + "Total execution time: " + totalExecutionTime + AnsiColors.RESET);
        System.out.println(AnsiColors.GREEN + "Avg execution time: " + averageExecutionTime + AnsiColors.RESET);

        System.out.println(AnsiColors.RED + "Total wait time: " + totalWaitTime + AnsiColors.RESET);
        System.out.println(AnsiColors.GREEN + "Avg wait time: " + averageWaitTime + AnsiColors.RESET);
    }

    // helping functions

    private static int[] sorted(int[] array) {
        var sortedArray = Arrays.stream(array)
                .sorted()
                .toArray();
        System.out.println("Sorted in ascending order: ");
        Main.printProcesses(sortedArray);
        return sortedArray;
    }

}
