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

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setBurstTime(int BurstTime) {
        this.BurstTime = BurstTime;
    }

    public void setArrivalTime(int ArrivalTime) {
        this.ArrivalTime = ArrivalTime;
    }

    public void setPriority(int Priority) {
        this.Priority = Priority;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public void setWaiting(float Waiting) {
        this.Waiting = Waiting;
    }

    public void setTurnaround(float Turnaround) {
        this.Turnaround = Turnaround;
    }

    public void setResponse(float Response) {
        this.Response = Response;
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

class PrioritySchedule {
    ArrayList<Process> Processes = new ArrayList<Process>();
    ArrayList<Process> ReadyQueue = new ArrayList<Process>();
    ArrayList<Process> FinishedProcesses = new ArrayList<Process>();
    float AverageWaiting = 0;
    float AverageTurnaround = 0;

    public PrioritySchedule(ArrayList<Process> Processes) {
        this.Processes = Processes;
        this.run();
    }

    void run() {
        int Time = 0;
        int size = Processes.size();
        while (FinishedProcesses.size() != size) {
            for (int i = 0; i < Processes.size(); i++) {
                if (Processes.get(i).getArrivalTime() <= Time && !ReadyQueue.contains(Processes.get(i)) && !FinishedProcesses.contains(Processes.get(i))) {
                    ReadyQueue.add(Processes.get(i));
                }
            }
            if (ReadyQueue.size() > 0) {
                Process currentProcess = ReadyQueue.get(0);
                for (int j = 1; j < ReadyQueue.size(); j++) {
                    if (ReadyQueue.get(j).getPriority() < currentProcess.getPriority()) {
                        currentProcess = ReadyQueue.get(j);
                    }
                }
                currentProcess.setWaiting(Time - currentProcess.getArrivalTime());
                Time += currentProcess.BurstTime;
                currentProcess.setTurnaround(Time - currentProcess.getArrivalTime());
                FinishedProcesses.add(currentProcess);
                ReadyQueue.remove(currentProcess);
            } else {
                Time++;
            }
            if (Time % 30 == 0) {
                // aging for processes in ready queue
                for (int i = 0; i < ReadyQueue.size(); i++) {
                    ReadyQueue.get(i).setPriority(ReadyQueue.get(i).getPriority() - 1);
                }
            }
        }
        for (int j = 0; j < FinishedProcesses.size(); j++) {
            AverageWaiting += FinishedProcesses.get(j).getWaiting();
            AverageTurnaround += FinishedProcesses.get(j).getTurnaround();
        }
        AverageWaiting /= FinishedProcesses.size();
        AverageTurnaround /= FinishedProcesses.size();
    }
    
    void printSchedule() {
        System.out.println("Priority Schedule:");
        System.out.println("Execution Order:");
        for (int i = 0; i < FinishedProcesses.size(); i++) {
            System.out.println("Process: " + FinishedProcesses.get(i).getName() + ", Waiting Time: " + FinishedProcesses.get(i).getWaiting() + "s, Turnaround Time: " + FinishedProcesses.get(i).getTurnaround() + "s");
        }
        System.out.println("Average Waiting Time: " + AverageWaiting);
        System.out.println("Average Turnaround Time: " + AverageTurnaround);
    }
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

        Process P1 = new Process("P1", 10, 0, 3, "red");
        Process P2 = new Process("P2", 1, 0, 1, "orange");
        Process P3 = new Process("P3", 2, 0, 4, "yellow");
        Process P4 = new Process("P4", 1, 0, 5, "green");
        Process P5 = new Process("P5", 5, 0, 2, "blue");
        ArrayList<Process> Processes = new ArrayList<Process>();
        Processes.add(P1);
        Processes.add(P2);
        Processes.add(P3);
        Processes.add(P4);
        Processes.add(P5);
        PrioritySchedule PS = new PrioritySchedule(Processes);
        PS.printSchedule();
    }
}