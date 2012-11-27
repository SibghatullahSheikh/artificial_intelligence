1. javac *.java
2. java GASolver weight1 weight2 weight3 

weight1 represent the weight for the cross section area(f1)
weight2 represent the weight for the static deflection(f2)
weight3 represent the weight for the bending stress constraint function(f3)

Note: sum of weight1, weight2 and weight3 MUST be 1.

example:
java GASolver 1 0 0
meaning minmise the cross section area(f1).