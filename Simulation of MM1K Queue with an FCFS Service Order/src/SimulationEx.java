import java.io.*;
import java.util.*;

public class SimulationEx {

    public static void main(String[] args)throws IOException{

        File data = new File("parameters.conf");
        Scanner input = new Scanner(data);
        float theta = Float.parseFloat(input.nextLine());
        float mu = Float.parseFloat(input.nextLine());
        int numOfCustomers = 10000000;
        File output = new File("output.csv");
        if (!output.exists()) {
            output.createNewFile();
        }
        FileWriter outputWriter = new FileWriter(output);
        outputWriter.write("lambda,pbSimulation,pdSimulation,pbAnalytical,pdAnalytical\n");

        for(float lambda = (float)0.1 ; lambda <= 20 ; lambda += 0.1){
            float lastArrival = 0;
            float waiting = 0;
            float service = 0;
            int pb = 0;
            int pd = 0;
            float timer = 0;
            int queueSize = 0;
            double[] fi = new double[13];
            double[] temp = new double[13];
            double sum=0;

            HashMap<Integer, Customer> customerSet = new HashMap<>();
            PriorityQueue <Event> eventHeap = new PriorityQueue <Event> ();
            LinkedList<Customer> queue = new LinkedList ();

            for (int i = 0 ; i < numOfCustomers; i++){
                lastArrival = customerArrivalTime(lastArrival, lambda);
                waiting = customerWaitingTime(lastArrival, theta);
                service = customerServiceTime(mu);

                Customer c = new Customer(i, lastArrival, waiting, service);
                customerSet.put(i, c);
                Event e = new Event(c.arrivalTime, c, Type.arrival);
                eventHeap.add(e);
            }

            while (!eventHeap.isEmpty()){
                Event top = eventHeap.remove();
                timer = top.eventTime;

                if(top.type == Type.arrival){
                    if(queueSize == 0){
                        queue.addLast(top.customer);
                        queueSize++;
                        Event exit = new Event(top.customer.serviceTime + timer, top.customer, Type.exit);
                        eventHeap.add(exit);
                    }else if(queueSize != 12){
                        queue.addLast(top.customer);
                        queueSize++;
                        Event deadline = new Event(top.customer.waitingTime, top.customer, Type.deadline);
                        top.customer.deadLineEvent = deadline;
                        eventHeap.add(deadline);
                    }else{
                        pb++;
                    }
                }else if(top.type == Type.deadline){
                    boolean f = queue.remove(top.customer);
                    if(f == true){ queueSize--; }
                    else {
                        //
                    }
                    pd++;
                }else if(top.type == Type.exit){
                    queue.removeFirst();
                    queueSize--;
                    // eventHeap.remove(top.customer.deadLineEvent);
                    if(queueSize != 0){
                        eventHeap.remove(queue.peek().deadLineEvent);
                        Event exit = new Event(queue.peek().serviceTime + timer, queue.peek(), Type.exit);
                        eventHeap.add(exit);
                    }
                }
            }
            outputWriter.append(lambda+",");
            outputWriter.append(pb/(float) numOfCustomers+",");
            outputWriter.append(pd/(float) numOfCustomers+",");

            for(int c=1 ; c<13 ; c++) {
                int factorial = 1;
                for (int i=1; i<=c; i++) {
                    factorial = factorial * i;
                }
                double pi=1;
                for (int k=0 ; k<=c ; k++) {
                    pi = pi * (mu + (k / theta));
                }

                fi[c] = (double) factorial/pi;
            }
            temp[1] = lambda / mu;
            for (int i = 2 ; i <= 12 ; i++) {
                int factorial2=1;
                for (int j = 1; j < i; j++) {
                    factorial2 = factorial2*j;
                }
                temp[i]=(double) Math.pow(lambda,i) * fi[i-1]/factorial2;
            }
            for (int i=1 ; i<=12 ; i++) {
                sum = sum+temp[i];
            }
            double p0=1/(1+sum);
            double pb_analytical = p0 * temp[12];
            double pd_analytical = 1-(mu/lambda)*(1-p0)-pb_analytical;
            outputWriter.append(pb_analytical + ",");
            outputWriter.append(pd_analytical + "\n");
        }
        outputWriter.close();
    }
    
    static float customerArrivalTime(float lastArrival, float lambda){
        Random x = new Random();
        return (float) (lastArrival + (-1*(Math.log(1-x.nextFloat())/ lambda)));
        
    }
    
    static float customerWaitingTime(float lastArrival, float theta){
        Random x = new Random();
        return (float) (lastArrival +(-1*(Math.log(1-x.nextFloat())* theta)));
        
    }
    
    static float customerServiceTime(float mu){
        Random x = new Random();
        return (float) ((-1*(Math.log(1-x.nextFloat())/ mu)));
        
    }    
}
