public class Customer{

    int id;
    double arrivalTime;
    double waitingTime;
    double serviceTime;
    Event deadLineEvent;
    
    
    Customer( int id, double arrivalTime, double waitingTime, double serviceTime){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.waitingTime = waitingTime;
        this.serviceTime = serviceTime;
    }
}
