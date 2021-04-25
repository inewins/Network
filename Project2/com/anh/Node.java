
package com.anh;
/*
Note: This is not the main program. Master.java will call this program to create nodes
*/
public class Node {
  //Initializing global variables
  static final int MAX_NODES = 6;
  private int nodeID;
  private int[][] dvTable;


  public void node(int nodeID, int[][] nodeData){
    /*
    //nodeID is specific to this node
    //nodeData is the input.txt file with all the data
    */
    //initializing local variables
    int i;
    int j;
    int x;
    int y;
    this.nodeID = nodeID; //will be unique when called in master code
    this.dvTable = new int[MAX_NODES][MAX_NODES]; //will be unique when called in master code
    this.dvTable = new int[MAX_NODES][MAX_NODES];  //will be unique when called in master code

    //*****************************************
    //***FILL INITIAL TABLE W DEFAULT VALUES***
    //*****************************************
    i=0;
    j=0;
    for( i = 0; i < MAX_NODES; i++ )
    {
      for( j = 0; j < MAX_NODES; j++ )
      {
        if( i == j )
        {                             //if routed to yourself
          this.dvTable[i][j] = 0;
        }
        else
        {                             //everybody else
          this.dvTable[i][j] = 16;
        }
      }
    }

    //*********************************************************
    //***FILL INITIAL TABLE W DATA PROVIDED FROM MASTER CODE***
    //*********************************************************

    //note: input.txt data structure is
    //node1 node2 cost
    i=0;
    j=0;
    for( i = 0; i < nodeData.length; i++ )
    {
      for( j = 0; j < nodeData[0].length-1; j++ ) //dont count cost as a node
      {
        if( nodeID == nodeData[i][j]) //if this node match node1 or node2 on input.txt
        {
          //x and y are array index of the nodes so they are -1
          x = nodeID - 1;
          if( j == 0 ){ //if nodeID is node1
            y = nodeData[i][1] - 1;
            this.dvTable[x][y] = nodeData[i][2];
            this.dvTable[y][x] = nodeData[i][2];
          }
          if( j == 1 ){ //id nodeID is node2
            y = nodeData[i][0] - 1;
            this.dvTable[x][y] = nodeData[i][2];
            this.dvTable[y][x] = nodeData[i][2];
          }
        }
      }
    }
  }

    //*************************
    //***PRINT ROUTING TABLE***
    //*************************

  public void printRoutingTable(int NodeID){
    int i;
    System.out.println("Routing Table:");
    System.out.println(" Destination | link");
    System.out.println("  ---------------------");

    for( i = 0; i < this.dvTable.length; i++ )
    {
      if((i+1)== NodeID) //if same node
      {
        continue;
      }
      if((this.dvTable[NodeID-1][i] != 0) && (this.dvTable[NodeID-1][i] != 16))
        {
          System.out.println( "      "+(i+1) + "      | ("+NodeID+","+(i+1)+")"); //if there exist a route
        }
        else
        {
          System.out.println( "      "+(i+1) + "      | Inf"); //if no route
        }
      
  }
}

    //***************************
    //***PRINT DIST VECT TABLE***
    //***************************
  public void printdvTable(){
    int i, j;
    System.out.println("Distance Vector Table:");
    System.out.println("    1  2  3  4  5  6");
    System.out.println("  -------------------");

    for( i = 0; i < this.dvTable.length; i++ ){
      System.out.print( i+1 + "| ");
      for( j = 0; j < this.dvTable[i].length; j++ ){
        if( this.dvTable[i][j] > 9 ){
          System.out.print(this.dvTable[i][j] + " ");
        }
        else{
          System.out.print(" " + this.dvTable[i][j] + " ");
        }

      }
      System.out.println("|");
    }
    System.out.println("  -------------------");
  }//end of printdvTable

    //*****************************
    //***SHARE TABLES W NEIGHBOR***
    //*****************************
  public int[] getNeighbor()
  {
    //intializing local variables
    int i;
    int[] neighbor = new int[MAX_NODES];

    for( i = 0; i < neighbor.length; i++ ){
      neighbor[i] = 0;
    }

    for( i = 0; i < this.dvTable[0].length; i++ ){
      if( this.dvTable[this.nodeID-1][i] < 16 ){
        neighbor[i] = this.dvTable[this.nodeID-1][i];
      }
    }
    return neighbor;
  }

    //************
    //***GETTER***
    //************
  public int[][] getdvTable(){
    return this.dvTable;
  }

    //****************************
    //***UPDATE DIST VECT TABLE***
    //****************************
  public boolean updatedvTable(int[][] neighborTable){
    //initialize local variable
    int i;
    int j;
    int counter = 0;

    for( i = 0; i < this.dvTable.length; i++ ){
      for( j = 0; j < this.dvTable[i].length; j++ ){
        if( this.dvTable[i][j] > neighborTable[i][j] && neighborTable[i][j] < 16 ){
          this.dvTable[i][j] = neighborTable[i][j];
          counter++;
        }
      }
    }
    if( counter == 0 ){
      return false;
    }
    else{
      return true;
    }
  }



}
