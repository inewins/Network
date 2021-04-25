package com.anh;

/*
Note: This is the main program. This is used Primarily to read in files, create nodes, and handle multiple nodes.
*/

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Master {
    //Initializing global variables
    public static final int MAX_NODES = 6;


    public static void main(String[] args) {
        //Initializing local variables
        Scanner scanner = new Scanner(System.in);
        long start;
        long end;
        long diff;
        int i;
        int j;
        int[][] nodeInfo;
        Node[] nodes = new Node[MAX_NODES];
        int menu;
        String input;
        boolean stable = false;
        int cycles = 0;

        //******************
        //***READ IN FILE***
        //******************
        nodeInfo = readFile(args[0]);

        //***************
        //***Start GUI***
        //***************

        System.out.println("Please select what to do: ");
        System.out.println("1. Run step by step");
        System.out.println("2. Run without intervention");
        System.out.println("3. Exit");
        input = scanner.nextLine();

        menu = Integer.parseInt(input);

        //****************************
        //***LET THE BUILDING BEGIN***
        //****************************
        System.out.println("Building routers.");

        for( i = 0; i < nodes.length; i++ ){
            nodes[i] = new Node();      //array of 'routers'
            nodes[i].node(i+1, nodeInfo);

            //First iteration of data
            System.out.println("**************************");
            System.out.println("***ROUTER " + (i+1) + " INFORMATION***" );
            System.out.println("**************************");
            nodes[i].printdvTable();
            nodes[i].printRoutingTable(i+1);
        }

        if( menu == 1 ){ //User chose step by step
            while( !stable ){
                System.out.println("Press any key to advance to next iteration");
                input = scanner.nextLine();
                stable = shareTables(nodes);
                for( i = 0; i < nodes.length; i++ ){//keeps printing until stable
                    System.out.println("**************************");
                    System.out.println("***ROUTER " + (i+1) + " INFORMATION***" );
                    System.out.println("**************************");
                    nodes[i].printdvTable();
                    nodes[i].printRoutingTable(i+1);
                }
                cycles++;
            }
            //At this point, stability has been reached
            System.out.println("\n\nNumber of cycles: "+cycles);
            System.out.println("The network has reached stability, exitting....");
        }
        else if(menu == 2) //User chose run without intervention
        {
            start = System.nanoTime();
            while( !stable ){
                //
                stable = shareTables(nodes);
                // that was one cycle of the system sharing information
                cycles++;
            }
            System.out.println("\n\nNetwork is now stable!");
            System.out.println("Here are the results:\n");
            // take note of end time
            end = System.nanoTime();
            diff = end - start;
            System.out.println("Start time: "+(start/1000000));
            System.out.println("End time: "+(end/1000000));
            System.out.println("Total duration (ms): "+(diff/1000000));
            System.out.println("Number of cycles: "+cycles);
        }
        else if (menu == 3)
        {
            System.out.println("Exiting.....");
        }
        scanner.close();
    }


        //***************
        //***READ FILE***
        //***************

    public static int[][] readFile(String fileName){
        int i = 0;
        int j = 0;
        int[][] nodeInfo;
        nodeInfo = new int[24][3];

        try {
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
		String[] lines = line.split(" ");
                for( j = 0; j < 3; j++ ){
                    nodeInfo[i][j] = Integer.parseInt(lines[j]);
                }
                i++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodeInfo;
    }

        //*******************
        //***TABLE SHARING***
        //*******************


    public static boolean shareTables(Node[] node){
        //Initializing local variables
        int i;
        int j;
        int[] list;
        boolean bool = false;
        boolean stable = true;
        int[][] vectorTable;

        for( i = 0; i < MAX_NODES; i++ ){
            list = node[i].getNeighbor();
            for( j = 0; j < list.length; j++ ){
                if( list[j] > 0 ){
                    vectorTable = node[j].getdvTable();
                    stable = node[i].updatedvTable(vectorTable);
                    System.out.println("Router number " + (i+1) + " is getting updated with router number " + (j+1) );
                    if ( stable ){
                        bool = true;
                    }
                }
            }
        }
        if( bool == false ){
            //stable
            return true;
        }
        else{
            //not stable
            return false;
        }
    }
    
}
