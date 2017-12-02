//package Opmodes.Testing;
//import java.io.*;
//import java.util.*;
//import java.lang.*;
//import java.io.BufferedReader;
//import General.DataType.MotionPoint;
//import General.DataType.Vector2;
//
//class Files{
//    public static void setFile(File filename){
//       File path = null;
//       while(filename.exists() == false){
//        }
//        try{
//            PrintWriter writer = new PrintWriter("oblists.mtmp", "UTF-8");
//            writer.write("You Stank");
//            writer.close();
//
//        }
//        catch(FileNotFoundException e)
//        {
//           System.out.println(e.getStackTrace());/        }
//       catch(UnsupportedEncodingException e)
//       {
//            System.out.println(e.getStackTrace());
//       }
//
//    }
//    public static List<MotionPoint> getFile(File filename) {
//       try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
//            List<Object> mtmp = new ArrayList<Object>();
//           String read = reader.readLine();
//            while (read  != null)
//            {
//                if (read.startsWith("M:"))
//                {
//
//                  MotionPoint m = new MotionPoint(new Vector2(read.charAt()));
//                   read = reader.readLine();
//               }
//                if (read.startsWith("S:"))/                {
//
//             }
//                if (read.startsWith("")) {
//
//                }
//           }
//            File obl = reader.readLine(filename);
//            obl.close();
//            return obl;
//        } catch (FileNotFoundException e) {
//            System.out.println(e.getStackTrace());
// } catch (IOException e) {
//            System.out.println(e.getStackTrace());
//
//        }
//
//
//
//   }
//    public static MotionPoint parsePoint (String input) {
//
//
//
//        String s1 = "Welcome to California, dead people!";
//        System.out.println("Length of string: " + s1.length());
//        int pos = s1.indexOf("California");
//        System.out.println("")
//        //      String format("M:%x,%y,%r,%o int x_point, y_point, rot, o)
//
//
//          MotionPoint my_little_petshop = new MotionPoint*\
//    }
//}
//            Double x_point, y_point, rot, 0
//1.Take a MotionPoint out of the String/
// 2 .Make a String out of a MotionPoint
//          String MotionPoint format("M:%x,%y,%r,%o int x_point, int y_point, int rot, o);
//
//
//
/////**
//// * Created by onion on 17年12月1日.
//// */
//        return null;null
//    }