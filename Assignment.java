import java.util.*;
class Process {
    String Name;
    int BurstTime;
    int ArrivalTime;
    int Priority;
    String Color;
    float Waiting = 0 ;
    float Turnaround = 0 ;
    float Response = 0 ;
    float ExitTime = 0;
    int executionOrder = 0;
    boolean Completed = false;
    int remainingTime ;
    int swappingTime = 0;

    public Process(String Name, int BurstTime , int ArrivalTime , int Priority, String Color) {
        this.Name = Name;
        this.BurstTime = BurstTime;
        this.ArrivalTime = ArrivalTime;
        this.Priority = Priority;
        this.Color = Color;
        this.remainingTime = BurstTime;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setBurstTime(int burstTime) {
        BurstTime = burstTime;
    }

    public void setArrivalTime(int arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

    public void setColor(String color) {
        Color = color;
    }

    public void setWaiting(float waiting) {
        Waiting = waiting;
    }

    public void setTurnaround(float turnaround) {
        Turnaround = turnaround;
    }

    public void setResponse(float response) {
        Response = response;
    }

    public void setExitTime(float exitTime) {ExitTime = exitTime;}

    public void setCompleted(boolean completed) {
        Completed = completed;
    }

    public String getName() {
        return Name;
    }

    public int getBurstTime() {
        return BurstTime;
    }

    public int getArrivalTime() {
        return ArrivalTime;
    }

    public int getPriority() {
        return Priority;
    }

    public String getColor() {
        return Color;
    }

    public float getWaiting() {
        return Waiting;
    }

    public float getTurnaround() {
        return Turnaround;
    }

    public float getResponse() {
        return Response;
    }

    public float getExitTime() {
        return ExitTime;
    }

    public boolean isCompleted() {return Completed;}
    public int getExecutionOrder() {return executionOrder;}

    public void setExecutionOrder(int executionOrder) {this.executionOrder = executionOrder;}
    public void setSwappingTime(int swappingTime) {this.swappingTime = swappingTime;}

    public int getSwappingTime() {return swappingTime;}
    public void setRemainingTime(int remainingTime) {this.remainingTime = remainingTime;}
    public int getRemainingTime() {return remainingTime;}
}

class SJFNonPreemptive {
    // The List of Processes not arranged Pure from user
    List<Process> Processes;
    int SumArrival = 0;
    int SumResponse = 0;
    int SumTurnaround = 0;
    int SizeNum;
    public SJFNonPreemptive(List<Process> Processes){
        // Set the value of the list
        this.Processes = Processes;
        SizeNum = this.Processes.size();
        // Arrange the list depends on the value of the burst time for each process
        Processes.sort(Comparator.comparingInt(p -> p.ArrivalTime));
        int CurrentTime = 0;
        while (!Processes.isEmpty()) {
            // Find the process with the shortest burst time among arrived processes
            Process shortestJob = Processes.get(0);
            for (Process P : Processes) {
                if (P.ArrivalTime > CurrentTime) {break; /*Stop searching when you reach a P that hasn't arrived yet*/}
                if (P.BurstTime < shortestJob.BurstTime) {shortestJob = P;}
            }
            // set the value of the waiting time for each process
            shortestJob.Waiting += CurrentTime - shortestJob.ArrivalTime;
            SumArrival += shortestJob.Waiting;
            // set the value of the response time for each process
            shortestJob.Response += CurrentTime - shortestJob.ArrivalTime;
            SumResponse += shortestJob.Response;
            // Execute the shortest job
            System.out.println("Executing process " + shortestJob.Name +
                    " from time " + CurrentTime + " to " + (CurrentTime + shortestJob.BurstTime));
            // Update current time and remove the executed process from the list
            CurrentTime += shortestJob.BurstTime;
            shortestJob.Turnaround = CurrentTime - shortestJob.ArrivalTime;
            SumTurnaround += shortestJob.Turnaround;
            Processes.remove(shortestJob);
            System.out.println(shortestJob.Waiting + " " + shortestJob.Response + " " + shortestJob.Turnaround);
        }
        System.out.println(SumArrival + " " + SumResponse + " " + SumTurnaround);
    }
    // calc the average of the turnaround time of the algorithm
    public float ATT() {
        return (SumTurnaround / (float)SizeNum);
    }
    // calc the average of the turnaround time of the algorithm
    public float AWT() {
        return (SumArrival / (float)SizeNum);
    }
    // calc the average of the Response time of the algorithm
    public float ART() {
        return (SumResponse / (float)SizeNum);
    }
}

class SJFPreemptive {
    //solving the Starvation Problem
    //assuming that the process must enter the CPU if it's swapping time grater than 20
    int maxSwap = 20;
    List<Process> Processes;
    PriorityQueue<Process> pQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getBurstTime).thenComparingInt(Process::getArrivalTime));
    Vector executionOrder = new Vector(); // Added vector to save ExecutionOrder

    public SJFPreemptive(List<Process> processes) {
        Processes = processes;
    }

    public Vector getExecutionOrder() {
        return executionOrder;
    }

    public float ATT() {
        float totalTurnaround = 0;
        for (Process process : Processes) {
            totalTurnaround += process.getTurnaround();
        }
        return totalTurnaround / Processes.size();
    }

    public float AWT() {
        float totalWaiting = 0;
        for (Process process : Processes) {
            totalWaiting += process.getWaiting();
        }
        return totalWaiting / Processes.size();
    }


    public void runSJFPreemptive() {
        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null; // Process with the shortest remaining time


        while (completedProcesses < Processes.size()) {
            // Add processes to the priority queue based on remaining burst time
            for (Process p : Processes) {
                if (!p.isCompleted() && p.getArrivalTime() <= currentTime && !pQueue.contains(p)) {
                    pQueue.add(p);
                    // Check if the last process added to the vector is the same as the current process
                }
            }


            // Check if the priority queue is not empty
            if (!pQueue.isEmpty()) {

                currentProcess = pQueue.poll();
                int remainingTime = currentProcess.getRemainingTime();
                if(pQueue.peek() != currentProcess){
                    currentProcess.setSwappingTime(currentProcess.getSwappingTime()+1);
                }
                // decrease the process for one time unit
                remainingTime--;

                // Check if the process has completed
                if (remainingTime == 0) {
                    // Process has completed
                    currentProcess.setExitTime(currentTime + 1);
                    currentProcess.setTurnaround(currentProcess.getExitTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaiting(currentProcess.getTurnaround() - currentProcess.getBurstTime());
                    currentProcess.setCompleted(true);
                    completedProcesses++;
                } else {
                    // Process not completed, update its remaining time and add it back to the queue
                    currentProcess.setRemainingTime(remainingTime);
                    pQueue.add(currentProcess);
                }
                if (executionOrder.isEmpty() || !executionOrder.lastElement().equals(currentProcess.getName())) {
                    executionOrder.add(currentProcess.getName());
                }
            }
            currentTime++;

        }
    }

}



class PriorityNonPreemptive{

}

public class Assignment{
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 1,0,1,"Blue"));
        processes.add(new Process("P2", 4,0,1,"Yellow"));
        processes.add(new Process("P3", 3,0,1,"Green"));
        processes.add(new Process("P4", 5,0,1,"Red"));
        SJFNonPreemptive X = new SJFNonPreemptive(processes);
        System.out.println("The Average of the waiting time is: " + X.AWT());
        System.out.println("The Average of the turnaround time is: " + X.ATT());
        System.out.println("The Average of the response time is: " + X.ART());
//        processes.add(new Process("P1", 1,0,1,"Blue"));
//        processes.add(new Process("P2", 4,1,1,"BLack"));
//        processes.add(new Process("P3", 7,2,1,"DDDD"));
//        processes.add(new Process("P4", 5,3,1,"Red"));
//        SJFNonPreemptive X = new SJFNonPreemptive(processes);
//        System.out.println("The Average of the waiting time is: " + X.AWT());
//        System.out.println("The Average of the turnaround time is: " + X.ATT());
//        System.out.println("The Average of the response time is: " + X.ART());


        // TEST SJFPreemptive
//        processes.add(new Process("P1", 6,2,1,"Blue"));
//        processes.add(new Process("P2", 2,5,1,"BLack"));
//        processes.add(new Process("P3", 8,1,1,"DDDD"));
//        processes.add(new Process("P4", 3,0,1,"Red"));
//        processes.add(new Process("P5", 4,4,1,"pink"));
//
//        SJFPreemptive sjfPreemptive = new SJFPreemptive(processes);
//        sjfPreemptive.runSJFPreemptive();
//
//        // Output results
//        System.out.println("\nExecution Order: ");
//        //fre loop get the Vector and print it
//        for (Object executionOrder : sjfPreemptive.getExecutionOrder()) {
//            System.out.print(executionOrder + " ");
//        }
//
//        System.out.println("\nProcess\tWaiting Time\tTurnaround Time");
//        for (Process process : processes) {
//            System.out.println(process.getName() + "\t\t"  + process.getWaiting() + "\t\t\t\t" + process.getTurnaround());
//        }
//
//        System.out.println("The Average of the waiting time is: " + sjfPreemptive.AWT());
//        System.out.println("The Average of the turnaround time is: " + sjfPreemptive.ATT());
    }
}

