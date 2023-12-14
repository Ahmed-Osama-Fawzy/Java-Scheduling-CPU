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
    public Process(String Name, int BurstTime , int ArrivalTime , int Priority, String Color) {
        this.Name = Name;
        this.BurstTime = BurstTime;
        this.ArrivalTime = ArrivalTime;
        this.Priority = Priority;
        this.Color = Color;
    }
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
    }
}