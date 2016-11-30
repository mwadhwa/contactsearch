Contact Search Application

This allows searching of contacts based on First Name , lastName criteria.

Internally, app is using compressed tries to store the entities.
It stores entity in 2 ways, with first Name and with last Name.

Insert:
Insert can have following 4 cases :
1) contact is not present
    Simply create a new node with compressed value as name and add to the parent
2) contact is already present
    In this case, add the entity to already existing node
3) contact is super set of text which is already present in trie
    e.q. stored contact - "Mi Yang"
    contact to store - "Milap Wadhwa"
    Create a new node, and attach this node with exiting node
4) contact is partially matching with the text stored
    e.q. stored contact - "Mike John"
    contact to store - "Milap Wadhwa"
    In this case, split the compressed node and create 2 new nodes which points to spited node

Search:
1) Contact is not present
    return empty set
2) Contact is present with exact match :
    return current tree entities
    
    
    
Setup To Run : 
 mvn package
 run startup.sh
