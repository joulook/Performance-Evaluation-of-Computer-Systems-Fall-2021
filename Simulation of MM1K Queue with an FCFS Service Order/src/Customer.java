public class Customer{

    int id;
    float arrivalTime;
    float waitingTime;
    float serviceTime;
    Event deadLineEvent;
    
    
    Customer( int id, float arrivalTime, float waitingTime, float serviceTime){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.waitingTime = waitingTime;
        this.serviceTime = serviceTime;
    }
}
