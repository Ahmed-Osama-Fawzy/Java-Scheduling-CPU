import java.util.*;
class Process {
    String Name;
    int BurstTime;
    int ArrivalTime;
    int Priority;
    String Color;
    public Process(String Name, int BurstTime , int ArrivalTime , int Priority, String Color) {
        this.Name = Name;
        this.BurstTime = BurstTime;
        this.ArrivalTime = ArrivalTime;
        this.Priority = Priority;
        this.Color = Color;
    }
}

class SJFNonPreemptive {
    List<Process> Processes;
    public SJFNonPreemptive(List<Process> Processes){
        this.Processes = Processes;
        Processes.sort(Comparator.comparingInt(p -> p.ArrivalTime));
        System.out.println();
        for (int i = 0 ; i < Processes.size() ; i++){
            if (i < Processes.size()-1 && Processes.get(i).ArrivalTime == Processes.get(i + 1).ArrivalTime) {
                if (Processes.get(i).BurstTime > Processes.get(i+1).BurstTime) {
                    Collections.swap(Processes,i+1,i);
                }
            }
        }
        int Counter = 0 ;
        for (Process P:Processes){
            System.out.print(Counter + "  " + P.Name + " = " + P.BurstTime);
            Counter += P.BurstTime;
            System.out.print("  "  + Counter);
            System.out.println();
        }
    }
}

class SJFPreemptive {

}
class PriorityNonPreemptive{

}

public class Assignment{
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P4", 3,0,1,"Blue"));
        processes.add(new Process("P5", 10,1,1,"BLack"));
        processes.add(new Process("P6", 50,3,1,"DDDD"));
        processes.add(new Process("P1", 6,0,1,"Red"));
        processes.add(new Process("P2", 8,3,1,"Green"));
        processes.add(new Process("P3", 7,1,1,"Yellow"));
        SJFNonPreemptive X = new SJFNonPreemptive(processes);

    }
}