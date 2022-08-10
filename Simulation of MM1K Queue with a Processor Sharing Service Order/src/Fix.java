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
        outputWriter.write("lambda,pbSimulation,pdSimulation,pbAnalytical,pdAnalytical\n");
        for(double lambda = (double)0.1 ; lambda <= 20 ; lambda += 0.1){
            double lastArrival = 0;
            double waiting = 0;
            double service = 0;
            int pb = 0;
            int pd = 0;
            double timer = 0;
            int queueSize = 0;

            HashMap<Integer, Customer> customerSet = new HashMap<>();
            PriorityQueue <Event> arrivalEventHeap = new PriorityQueue <Event> ();
            PriorityQueue <Event> deadlineEventHeap = new PriorityQueue <Event> ();
            LinkedList<Customer> queue = new LinkedList ();

            for (int i = 0 ; i < numOfCustomers; i++){
                lastArrival = customerArrivalTime(lastArrival, lambda);
                waiting = (float) (lastArrival + 2);
                service = customerServiceTime(mu);

                Customer c = new Customer(i, lastArrival, waiting, service);
                //customerSet.put(i, c);
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
                double quantum = (double) (delta / queueSize);
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
                    double deltap = (double) (min*queueSize);
                    for(int i=0 ; i < queue.size() ; i++){
                        queue.get(i).serviceTime = (double) queue.get(i).serviceTime - min;
                        if(queue.get(i).serviceTime <= 0){
                            deadlineEventHeap.remove(queue.get(i).deadLineEvent);
                            queue.remove(queue.get(i));
                            queueSize--;
                        }
                    }
                    delta = (double) delta - deltap;
                    quantum = (double) (delta / queueSize);
                    min = quantum;
                }

            }
            outputWriter.append(lambda+",");
            outputWriter.append(pb/(double) numOfCustomers+",");
            outputWriter.append(pd/(double) numOfCustomers+",");


            double y = 0.0;
            for(int i=1 ; i<13 ; i++){
                double x = 1.0;
                for(int j=1 ; j<=i ; j++){
                    x = x * (double) (mu + (mu / ( (Math.exp( (mu*theta) / j)) -1 ) ) );
                }
                y = y + (double) (Math.pow(lambda,i) / x);
            }
            double p0 = (double) Math.pow((1+y),-1);

            double t = 1.0;
            for(int i=1 ; i<13 ; i++){
                t = t * (double) (mu + (mu / ( (Math.exp( (mu*theta) / i)) -1 ) ) );
            }
            double pk = (double) (p0*(Math.pow(lambda,12) / t));
            double pb_analytical = pk;

            double pd_analytical = (double) ((1-(mu/lambda)*(1-p0)) - pb_analytical);
            outputWriter.append(pb_analytical + ",");
            outputWriter.append(pd_analytical + "\n");

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
}
