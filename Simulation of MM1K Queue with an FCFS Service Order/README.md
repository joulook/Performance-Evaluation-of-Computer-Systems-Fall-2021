# Simulation of M/M/1/K Queue with an FCFS Service Order

My 1st project was in the "Performance Evaluation of Computer Systems" course when I was in the 3rd semester of my master's at SUT. In this project, I simulated an M/M/1/K queue(length = 12) with an FCFS service order and compared its results with analytical methods. There are two assumptions for the waiting time function (Theta), Fixed and Exponential. In sum, this simulation gives us The possibility of blocked requests(P_b) and the possibility of missed deadline requests(P_b).

## Analytical formulas

| <img src="01.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *01* |

| <img src="02.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *02* |

| <img src="03.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *03* |

| <img src="04.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *04* |

| <img src="05.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *05* |

## Simulation Results

The system was simulated under 10 Million customer requests for Lambda in [0.1,20] intervals with a jump of 0.1 unit.

###### P_b Diagram for Exponential Waiting Time

| <img src="06.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *06* |

###### P_d Diagram for Exponential Waiting Time

| <img src="07.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *07* |

###### P_b Diagram for Fixed Waiting Time

| <img src="08.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *08* |

###### P_d Diagram for Fixed Waiting Time

| <img src="09.png" alt="Pair Game" width="600"/> | 
|:--:| 
| *09* |
