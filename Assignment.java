import java.util.*;
class Process {
    String Name;
    int BurstTime;
    int ArrivalTime;
    int Priority;
    String Color;
    int enteredTime;
    float Waiting = 0 ;
    float Turnaround = 0 ;
    float Response = 0 ;
    int quantum = 0 ;
    int initialBurstTime = 0;
    //    int randomFactor = (int) (Math.random() * 21);
    int randomFactor = 0;

    int agFactor = updateAgFactor();
    public Process(String Name, int BurstTime , int ArrivalTime , int Priority, String Color,int AG) {
        this.Name = Name;
        this.BurstTime = BurstTime;
        this.initialBurstTime = BurstTime;
        this.ArrivalTime = ArrivalTime;
        this.Priority = Priority;
        this.Color = Color;
        this.agFactor = AG;
    }
    public int updateAgFactor() {
        if (randomFactor < 10){
            return agFactor = randomFactor +ArrivalTime +BurstTime;
        }
        else if (randomFactor > 10){
            return agFactor = 10 + ArrivalTime + BurstTime;
        }
        else {
            return agFactor = Priority + ArrivalTime + BurstTime;
        }

    }


    public int getQuantum() {
        return quantum;
    }


    public String getName() {
        return Name ;
    }

    public float getWaiting() {
        return Waiting; 
    }

    public float getTurnaround() {
        return Turnaround; 
    }
}
class SJFNonPreemptive {
    // The List of Processes not arranged Pure from user
    List<Process> Processes;
    public SJFNonPreemptive(List<Process> Processes){
        // Set the value of the list
        this.Processes = Processes;
        // Arrange the list depends on the value of the burst time for each process
        Processes.sort(Comparator.comparingInt(p -> p.BurstTime));
        /*
        // Arrange the list depends on the value of the burst time for each process
        for (int i = 0 ; i < Processes.size() ; i++){
            if (i < Processes.size()-1 && Processes.get(i).ArrivalTime == Processes.get(i + 1).ArrivalTime) {
                if (Processes.get(i).BurstTime > Processes.get(i+1).BurstTime) {
                    // swapping the processes if you need to achieve the right arrange for access the cpu
                    Collections.swap(Processes,i+1,i);
                }
            }
        }
        */
        int Counter = 0 ;
        for (Process P:Processes){
            // set the value of the waiting time for each process
            P.Waiting += Counter - P.ArrivalTime;
            // set the value of the response time for each process
            P.Response += Counter - P.ArrivalTime;
            // printing the chart of the algorithm
            System.out.print(Counter + "  " + P.Name + " = " + P.BurstTime);
            // increase the counter by the burst value of the terminated process
            Counter += P.BurstTime;
            System.out.print("  "  + Counter);
            System.out.println();
            // set the value of the turnaround time for each process
            P.Turnaround = Counter - P.ArrivalTime;
            //System.out.println(P.Waiting + " | " + P.Response + " | " + P.Turnaround);
        }
    }
    // calc the average of the turnaround time of the algorithm
    public float ATT() {
        float Sum = 0;
        for (Process P : Processes) {
            Sum += P.Turnaround;
        }
        return (Sum / Processes.size());
    }
    // calc the average of the turnaround time of the algorithm
    public float AWT() {
        float Sum = 0;
        for (Process P : Processes) {
            Sum += P.Waiting;
        }
        return (Sum / Processes.size());
    }
    // calc the average of the Response time of the algorithm
    public float ART() {
        float Sum = 0;
        for (Process P : Processes) {
            Sum += P.Response;
        }
        return (Sum / Processes.size());
    }
}

class SJFPreemptive {
}
class PriorityNonPreemptive{
}

class AG {
    private List<Process> processes;
    private Queue<Process> readyQueue;
    private List<Process> dieList;

    private int timeQuantum;
    private int contextSwitch;

    public AG(List<Process> processes, int quantum, int contextSwitch) {
        this.processes = processes;
        this.timeQuantum = quantum;
        this.contextSwitch = contextSwitch;
        this.readyQueue = new LinkedList<>();
        for(int i = 0; i < processes.size(); i++) {
            processes.get(i).quantum= quantum;
        }
        this.dieList = new ArrayList<>();
    }

    static double calculateMeanQuantum(int currentTime , Queue<Process> readyQueue) {
        if (readyQueue.isEmpty()) {
            return 0;
        }

        int sumQuantum = 0;
        int count = 0;
        for (Process process : readyQueue) {
            if(process.ArrivalTime <= currentTime)
                sumQuantum += process.quantum;
            count++;
        }

        return (double) sumQuantum / count;
    }
    public List<Process> getDieList() {
        return dieList;
    }
    public double calculateAverageWaitingTime() {
        double totalWaitingTime = 0;
        for (Process process : dieList) {
            totalWaitingTime += process.getWaiting();
        }
        return totalWaitingTime / dieList.size();
    }

    public double calculateAverageTurnaroundTime() {
        double totalTurnaroundTime = 0;
        for (Process process : dieList) {
            totalTurnaroundTime += process.getTurnaround();
        }
        return totalTurnaroundTime / dieList.size();
    }
    public void run(List<Process> processes){
        int currentTime = 0;
        Process runningProcess = null;

        while (true) {
            boolean end = false;
            for (Process process : processes) {
                if (process.BurstTime!=0) {
                    end = true;
                    break;
                }
            }
            if(!end)break;

            for (Process process : processes) {
                if (process.ArrivalTime <= currentTime &&
                        process.BurstTime == process.initialBurstTime &&
                        runningProcess != process) {
                    readyQueue.add(process);
                }
            }
            // If no process is currently running, or there are new processes --> select
            if (runningProcess == null) {
                if (runningProcess != null) {
                    readyQueue.add(runningProcess);
                }
                runningProcess = readyQueue.poll();
            }
            if(runningProcess!=null)
                runningProcess.enteredTime = currentTime;

            // Run the process non-preemptive for half the quantum time
            int nonPreemptiveTime = Math.min((runningProcess.quantum +1)/ 2, runningProcess.BurstTime);
            runningProcess.BurstTime -= nonPreemptiveTime;
            currentTime += nonPreemptiveTime;

            if (runningProcess.BurstTime == 0) {
                runningProcess.quantum = 0;
                runningProcess.Turnaround = currentTime - runningProcess.ArrivalTime;
                runningProcess.Waiting = runningProcess.Turnaround - runningProcess.initialBurstTime;

                dieList.add(runningProcess);
                while(!readyQueue.isEmpty() && readyQueue.peek().BurstTime==0)
                    readyQueue.poll();
                runningProcess = readyQueue.poll();
                continue;
            }
            boolean flag = false;
            // Simulate the second-by-second execution (preemptive) after half the quantum time
            for (int i = 0; i < (runningProcess.quantum/ 2); i++) {
                if(runningProcess.BurstTime==0)break;
                flag = false;
                Process minAG = runningProcess;
                Process oldProcess = runningProcess;
                for (Process process : processes) {
                    if (process.ArrivalTime <= currentTime) {
                        if (process.agFactor < minAG.agFactor && process.BurstTime!=0) {
                            minAG = process;
                        }
                        else if(process != runningProcess  &&
                                process.BurstTime==process.initialBurstTime &&
                                !readyQueue.contains(process)){
                            readyQueue.add(process);
                        }
                    }

                }
                if(minAG != oldProcess){
                    runningProcess.quantum = (runningProcess.quantum - (currentTime - runningProcess.enteredTime)) + runningProcess.quantum;
                    runningProcess = minAG;
                    readyQueue.add(oldProcess);
                    flag = true;
                }

                if(flag){
                    break;
                }

                // Reduce burst time of the running process
                runningProcess.BurstTime--;
                currentTime++;
                System.out.println("Time " + currentTime + ": " + runningProcess.Name + " Quantum Time = " + runningProcess.quantum +
                        ", Burst Time = " + runningProcess.BurstTime +", RandomFactor = " + runningProcess.randomFactor +
                        ", AG Factor = " + runningProcess.agFactor );

            }


            // Check if the process finished its execution
            if (runningProcess.BurstTime == 0) {
                runningProcess.quantum = 0;
                runningProcess.Turnaround = currentTime - runningProcess.ArrivalTime;
                runningProcess.Waiting = runningProcess.Turnaround - runningProcess.initialBurstTime;

                dieList.add(runningProcess);
                while(!readyQueue.isEmpty() && readyQueue.peek().BurstTime==0)
                    readyQueue.poll();
                runningProcess = readyQueue.poll();
            }

            else if (runningProcess.getQuantum() == (currentTime - runningProcess.enteredTime) && runningProcess.BurstTime > 0) {
                runningProcess.quantum += (int) Math.ceil(0.1 * (calculateMeanQuantum(currentTime, readyQueue)));
                Process oldProcess = runningProcess;
                while(!readyQueue.isEmpty() && readyQueue.peek().BurstTime==0)
                    readyQueue.poll();
                runningProcess = readyQueue.poll();
                readyQueue.add(oldProcess);
            }

        }
    }

}


public class Assignment{
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
//        processes.add(new Process("P1", 1,0,1,"Blue"));
//        processes.add(new Process("P2", 4,1,1,"BLack"));
//        processes.add(new Process("P3", 7,2,1,"DDDD"));
//        processes.add(new Process("P4", 5,3,1,"Red"));
/*        SJFNonPreemptive X = new SJFNonPreemptive(processes);
        System.out.println("The Average of the waiting time is: " + X.AWT());
        System.out.println("The Average of the turnaround time is: " + X.ATT());
        System.out.println("The Average of the response time is: " + X.ART());*/

        // running AG :
        processes.add(new Process("P1", 17,0,4,"Blue" , 20));
        processes.add(new Process("P2", 6,3,9,"BLack",17));
        processes.add(new Process("P3", 10,4,3,"DDDD",16));
        processes.add(new Process("P4", 4,29,8,"Red",43));
        AG x = new AG(processes,4,0);
        x.run(processes);

        System.out.println("Processes execution order:");
        for (Process process : x.getDieList()) {
            System.out.println(process.getName());
        }
        System.out.println("Waiting Time for each process:");
        for (Process process : x.getDieList()) {
            System.out.println(process.getName() + ": " + process.getWaiting());
        }
        System.out.println("Turnaround Time for each process:");
        for (Process process : x.getDieList()) {
            System.out.println(process.getName() + ": " + process.getTurnaround());
        }
        System.out.println("Average Waiting Time: " + x.calculateAverageWaitingTime());
        System.out.println("Average Turnaround Time: " + x.calculateAverageTurnaroundTime());
    }
}
