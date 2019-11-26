import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordRemoverApp {
    public static void main(String[] args) {
        System.out.println("Example: java \"RecordRemoverApp\" -p D:\\FolderWithMp3'\\' -d 40");
        String path = ""; //-p
        String duration = "";//-d

        boolean containsPath;
        boolean containsDuration;
        String input = "";

        for (String tmp : args) {
            input += tmp;
        }
        //checks whether the input contains parameter switches
        containsPath = input.contains("-p");
        containsDuration = input.contains("-d");
//        System.out.println("Input was: " + input);
//        System.out.println("Input contains duration: " + containsDuration);
//        System.out.println("Input contains path: " + containsPath);


        if (!containsDuration || !containsPath) {
            System.out.println("Missing one or more input parameters. Try again.");
            return;
        }
        //recognize input parameters
        LinkedHashMap<String, String> params = assembleLaunchParams(input);

        path = params.get("path");
        duration = params.get("duration");

        boolean durationCorrectness = checkDuration(duration);
        boolean pathCorrectness = checkPath(path);

        if (!durationCorrectness || !pathCorrectness) {
            System.out.println("One or more input parameters incorrect. Try again.");
            return;
        }
//        else {
//            System.out.println("Found: ");
//            System.out.println("path: " + path);
//            System.out.println("duration: " + duration);
//        }

        System.out.println("Confirm input parameters path and duration time:\n" +
                "1) path: " + path + "" + "\n2)duration time: " + duration + " s");
        System.out.println("Do you want to proceed? ");
        String message = "Type:  \'Y\' or \'yes\' if you want to continue," +
                " \'N\' or \'no\' if you want to abort the execution.";
        int act = inputUserChoice(message);
        if (act == 1) {
            System.out.println("received positive confirmation");
            new RecordRemover(path, Integer.parseInt(duration)).removeByDefault();
        } else if (act == 2) {
            System.out.println("received negative confirmation");
            return;
        } else {
            System.out.println("this shouldn't be displayed");
        }

    }//end of psvm()

    private static LinkedHashMap<String, String> assembleLaunchParams(String input) {
        LinkedHashMap<String, String> paramHashMap = new LinkedHashMap<>();
        int indexOfPath = input.indexOf("-p");
        int indexOfDuration = input.indexOf("-d");
        String pathValue = input.substring(indexOfPath + 2, indexOfDuration);
        pathValue = pathValue.trim();
        String durationValue = input.substring(indexOfDuration + 2, input.length());
        durationValue = durationValue.trim();

        paramHashMap.put("path", pathValue);
        paramHashMap.put("duration", durationValue);

        return paramHashMap;
    }

    private static String assemblePath(String[] args) {
        String path = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                i += 1;//forward to next iteration
                while (!args[i].equals("-d")) {
                    System.out.println("args[" + i + "]:" + args[i]);
                    path += args[i];
                    i += 1;
                }
            }
        }
        System.out.println("crock: " + path);
        return path;
    }
//end of method assemblePath()

    private static boolean checkDuration(String duration) {
        //allowed from 1 to 5 charactter number
        boolean allowedQuantityOfNumbers;
        Pattern p2 = Pattern.compile("\\d{1,5}");
        Matcher m2 = p2.matcher(duration);
        allowedQuantityOfNumbers = m2.matches();
//        System.out.println("allowedQuantityOfNumbers: " + allowedQuantityOfNumbers);

        //not starts with 0 and have number characters
        boolean allowedNumberSet;
        Pattern pk = Pattern.compile("^[^0]+[0-9]+");
        Matcher mk = pk.matcher(duration);
        allowedNumberSet = mk.matches();
//        System.out.println("allowedNumberSet: " + allowedNumberSet);

        if (!allowedNumberSet || !allowedQuantityOfNumbers) {
            System.out.println("Duration time \'" + duration +
                    "\' contains restricted symbols or exceeds the requirements");
        }
        return (allowedQuantityOfNumbers && allowedNumberSet);
    }

    private static boolean checkPath(String path) {
        //contains azAZ_0-9 and does not contain these /<>|?!@~
        Pattern pp = Pattern.compile("[\\w[^/<>|?!@~]]+");
        Matcher mp = pp.matcher(path);
        boolean pathCorrectness;
        pathCorrectness = mp.matches();
//        System.out.println("pathCorrectness: " + pathCorrectness);
        if (!pathCorrectness) {
            System.out.println("Path \'" + path + "\' contains restricted symbols");
        }
        return pathCorrectness;
    }

    private static int inputUserChoice(String message) {
        String input = "";
        int counter = 0;
        int actionCode; //yes -1; no - 2; exit - 0
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        while (true) {
            if ((counter == 0) || (counter % 5 == 0)) {
                System.out.println(message);
            }
            try {
                System.out.print("Your answer: ");
                input = br.readLine();
                if (input.equalsIgnoreCase("yes") ||
                        input.equalsIgnoreCase("y")) {
                    actionCode = 1;
                    return actionCode;
                } else if (input.equalsIgnoreCase("no") ||
                        input.equalsIgnoreCase("n")) {
                    actionCode = 2;
                    return actionCode;
                }
//                else if(input.equalsIgnoreCase("exit") ||
//                        input.equalsIgnoreCase("e")){
//                    actionCode = 0;
//                    return actionCode;
//                }
                else {
                    throw new IllegalArgumentException("Incorrect input. Try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            counter++;
        }//end of while loop
    }//end of method

}//end of class
