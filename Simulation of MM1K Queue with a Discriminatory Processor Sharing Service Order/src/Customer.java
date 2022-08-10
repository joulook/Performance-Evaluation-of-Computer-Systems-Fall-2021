public class Customer{

    int id;
    int weight;
    double arrivalTime;
    double waitingTime;
    double serviceTime;
    Event deadLineEvent;
    
    
    Customer( int id, double arrivalTime, double waitingTime, double serviceTime, int weight){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.waitingTime = waitingTime;
        this.serviceTime = serviceTime;
        this.weight = weight;
    }
}
