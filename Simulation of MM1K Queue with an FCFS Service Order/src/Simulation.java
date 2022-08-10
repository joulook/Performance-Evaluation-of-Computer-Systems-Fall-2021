import java.util.*;
import java.io.*;

public class Simulation{

    public static void main(String[] args)throws IOException{

        File data = new File("parameters.conf");
        Scanner input = new Scanner(data);
        float theta = Float.parseFloat(input.nextLine());
        float mu = Float.parseFloat(input.nextLine());
        float lambda = 10;

        
        int numOfCustomers = 10000000;
        float lastArrival = 0;
        float waiting = 0;
        float service = 0;
        int pb = 0;
        int pd = 0;
        float timer = 0;
        int queueSize = 0;
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
        
        System.out.println("pb: " + pb / (float) numOfCustomers);
        System.out.println("pd: " + pd / (float) numOfCustomers);
        
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
