//package Opmodes.Testing;
//import java.io.*;
//import java.util.*;
//import java.lang.*;
//import java.io.BufferedReader;
//import General.DataType.MotionPoint;
//import General.DataType.Vector2;
//
//class Files{
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
//        try {
//            FileInputStream writer = new FileInputStream("boinkus.mtmp");
//            for (int r = 0; r < List < MotionPoint >.size;
//            r++){
//                List<MotionPoint> (r).parseDouble();
//
//            }
//            writer.write("Resolution: " + r);
//        }
//        catch (FileNotFoundException e)
//        {
//
//        }
//
//    }
//}
// so sad :'(
