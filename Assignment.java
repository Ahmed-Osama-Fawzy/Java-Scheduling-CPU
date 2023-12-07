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

public class Assignment{
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 1,0,1,"Blue"));
        processes.add(new Process("P2", 4,1,1,"BLack"));
        processes.add(new Process("P3", 7,2,1,"DDDD"));
        processes.add(new Process("P4", 5,3,1,"Red"));
        SJFNonPreemptive X = new SJFNonPreemptive(processes);
        System.out.println("The Average of the waiting time is: " + X.AWT());
        System.out.println("The Average of the turnaround time is: " + X.ATT());
        System.out.println("The Average of the response time is: " + X.ART());
    }
}