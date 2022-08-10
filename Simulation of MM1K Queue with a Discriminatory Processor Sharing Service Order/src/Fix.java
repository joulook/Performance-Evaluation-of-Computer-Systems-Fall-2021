import java.io.*;
import java.util.*;

public class Fix {

    public static void main(String[] args)throws IOException{

        File data = new File("parameters.conf");
        Scanner input = new Scanner(data);
        double theta = Double.parseDouble(input.nextLine());
        double mu = Double.parseDouble(input.nextLine());

        int numOfCustomers = 10000000;
        File output = new File("outputFix.csv");
        if (!output.exists()) {
            output.createNewFile();
        }
        FileWriter outputWriter = new FileWriter(output);
        outputWriter.write("lambda,pbSimulation,pdSimulation,pd1,pd2\n");
        for(double lambda = (double)0.1 ; lambda <= 20.03 ; lambda += 0.1){
            double lastArrival = 0;
            double waiting = 0;
            double service = 0;
            int pb = 0;
            int pd = 0;
            int pd1 = 0;
            int pd2 = 0;
            double timer = 0;
            int queueSize = 0;


            PriorityQueue <Event> arrivalEventHeap = new PriorityQueue <Event> ();
            PriorityQueue <Event> deadlineEventHeap = new PriorityQueue <Event> ();
            LinkedList<Customer> queue = new LinkedList ();

            for (int i = 0 ; i < numOfCustomers; i++){
                lastArrival = customerArrivalTime(lastArrival, lambda);
                waiting = (float) (lastArrival + 2);
                service = customerServiceTime(mu);

                Customer c = new Customer(i, lastArrival, waiting, service, customerWeight());
                Event e = new Event(c.arrivalTime, c, Type.arrival);
                arrivalEventHeap.add(e);
            }

            while (!arrivalEventHeap.isEmpty() || !deadlineEventHeap.isEmpty()){
                Event top;
                if(arrivalEventHeap.peek() != null && deadlineEventHeap.peek() == null){
                    top = arrivalEventHeap.remove();
                }else if(arrivalEventHeap.peek() == null && deadlineEventHeap.peek() != null){
                    top = deadlineEventHeap.remove();
                }else if(arrivalEventHeap.peek().eventTime < deadlineEventHeap.peek().eventTime){
                    top = arrivalEventHeap.remove();
                }else {
                    top = deadlineEventHeap.remove();
                }
                timer = top.eventTime;
                if(top.type==Type.arrival && queueSize == 12) {
                    pb++;
                }else if(top.type==Type.arrival && queueSize != 12){
                    queue.addLast(top.customer);
                    queueSize++;
                    Event deadline = new Event(top.customer.waitingTime, top.customer, Type.deadline);
                    deadlineEventHeap.add(deadline);
                    top.customer.deadLineEvent = deadline;
                }else if(top.type == Type.deadline){
                    boolean f = queue.remove(top.customer);
                    if(f == true){
                        queueSize--;
                        pd++;
                        if(top.customer.weight == 1){
                            pd1++;
                        }else{
                            pd2++;
                        }
                    }
                }

                Event next;
                if(arrivalEventHeap.peek() == null && deadlineEventHeap.peek() == null){
                   next = top;
                   break;
                }else if(arrivalEventHeap.peek() != null && deadlineEventHeap.peek() == null){
                    next = arrivalEventHeap.peek();
                }else if(arrivalEventHeap.peek() == null && deadlineEventHeap.peek() != null){
                    next = deadlineEventHeap.peek();
                }else if(arrivalEventHeap.peek().eventTime < deadlineEventHeap.peek().eventTime){
                    next = arrivalEventHeap.peek();
                }else {
                    next = deadlineEventHeap.peek();
                }
                double delta = (double) (next.eventTime - timer);
                int sumOfWeight = 0;
                for(int i=0 ; i < queue.size() ; i++){
                    sumOfWeight = sumOfWeight + queue.get(i).weight;
                }
                double quantum = (double) (delta / sumOfWeight);
                boolean flag = true;
                double min = quantum;
                while(flag == true && delta > 0 && !queue.isEmpty()){
                    int cntr = 0;
                    for(int i=0 ; i < queue.size() ; i++){
                        if(queue.get(i).serviceTime < min){
                            min = queue.get(i).serviceTime;
                            cntr++;
                        }
                    }
                    if(cntr == 0){
                        flag = false;
                    }
                    double deltap = (double) (min*sumOfWeight);
                    for(int i=0 ; i < queue.size() ; i++){
                        queue.get(i).serviceTime = (double) queue.get(i).serviceTime - (min*queue.get(i).weight);
                        if(queue.get(i).serviceTime <= 0){
                            deadlineEventHeap.remove(queue.get(i).deadLineEvent);
                            queue.remove(queue.get(i));
                            queueSize--;
                        }
                    }
                    delta = (double) delta - deltap;
                    int sumOfWeightp = 0;
                    for(int i=0 ; i < queue.size() ; i++){
                        sumOfWeightp = sumOfWeightp + queue.get(i).weight;
                    }
                    quantum = (double) (delta / sumOfWeightp);
                    min = quantum;
                }

            }
            outputWriter.append(lambda+",");
            outputWriter.append(pb/(double) numOfCustomers+",");
            outputWriter.append(pd/(double) numOfCustomers+",");
            outputWriter.append(pd1/(double) numOfCustomers+",");
            outputWriter.append(pd2/(double) numOfCustomers+ "\n");

        }
        outputWriter.close();
    }
    
    static double customerArrivalTime(double lastArrival, double lambda){
        Random x = new Random();
        return (double) (lastArrival + (-1*(Math.log(1-x.nextDouble())/ lambda)));
        
    }
    
    static double customerServiceTime(double mu){
        Random x = new Random();
        return (double) ((-1*(Math.log(1-x.nextDouble())/ mu)));
        
    }

    static int customerWeight(){
        double r =  Math.random();
        if(r >= 0.5){
            return 1;
        }else{
            return 2;
        }
    }
}
