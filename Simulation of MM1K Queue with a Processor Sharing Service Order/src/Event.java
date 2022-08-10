public class Event implements Comparable<Event>{

    double eventTime;
    Customer customer;
    Type type;
    
    Event(double eventTime, Customer customer, Type type){
        this.eventTime = eventTime;
        this.customer = customer;
        this.type = type;
    }
    
    @Override
    public int compareTo(Event e) {
        if (this.eventTime>e.eventTime){ return 1; }
        else if (this.eventTime<e.eventTime){ return -1; }
        else{ return 0; }
    }
    
}
