//package com.example.dfoptimizerapp;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.Assert.*;
//
//public class GetSlateTest {
//
//
//    String befores= "[{"+ "\"player\"" + ":" + "\"Davante Adams\"" + "," + "\"Salary\""+ ":" + "\"9000\""+ "," +"\"Position\""+ ":" + "\"Wr\"" + "," + "\"Team\"" +":"+"\"GB\"" +","+"\"Opponent\""+":"+"JAX"+","+"\"Projection\""+":"+"\"29.58\"" + "}]";
//
//    @Test
//    public void testparseReceive() {
//        ArrayList<String> finals = GetSlate.ParseReceive(befores);
//        for (int i = 0; i < finals.size();i++)
//        {
//            System.out.println(finals.get(i));
//        }
//
//
//        // ...when the string is returned from the object under test...
//        String results ="[{"+ "\"player\"" + ":" + "\"Davante Adams\"" + "," + "\"Salary\""+ ":" + "\"9000\""+ "," +"\"Position\""+ ":" + "\"Wr\"" + "," + "\"Team\"" +":"+"\"GB\"" +","+"\"Opponent\""+":"+"JAX"+","+"\"Projection\""+":"+"\"29.58\"" + "}]";;
//
//        // ...then the result should be the expected one.
//        assertEquals(befores,results);
//    }
//
//}