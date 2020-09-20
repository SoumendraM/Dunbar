import java.io.FileReader;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


class JSONFILE {
    public static String filename = "C:\\Users\\lenovo\\IdeaProjects\\FSLInterpreter\\src\\FSLSample.json";
}

// User defined Exception handling. Rudimentary version, should be specialized.
class FSLException extends Exception
{
    public FSLException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}

class FSL {
    private Object FSLobj;
    private JSONObject FSLjson;

    private Map<String, Integer> varIntMap = new HashMap<String, Integer> ();
    private Map<String, String> varStringMap = new HashMap<String, String> ();
    private Map<String, Float> varFloatMap = new HashMap<String, Float> ();

    /*
     * Command operation Methods for all the Setup instructions
     */
    private void update(String id, int val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: update: id is passed as null...");
        }
        varIntMap.computeIfPresent(id, (k, v) -> val);
    }

    private void update(String id, String val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: update: id is passed as null...");
        }
        varStringMap.computeIfPresent(id, (k, v) -> val);
    }

    private void update(String id, Float val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: update: id is passed as null...");
        }
        varFloatMap.computeIfPresent(id, (k, v) -> val);
    }

    private void create(String id, int val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: create: id is passed as null...");
        }
        varIntMap.put(id, val);
    }

    private void create(String id, String val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: create: id is passed as null...");
        }
        varStringMap.put(id, val);
    }

    private void create(String id, float val) throws Exception {
        if (id == null) {
            throw new FSLException("Error: create: id is passed as null...");
        }
        varFloatMap.put(id, val);
    }

    private void delete(String id) throws Exception {
        if (id == null) {
            throw new FSLException("Error: delete: id is passed as null...");
        }

        String ret = null;
        if (id.contains("int")) {
            ret = varIntMap.remove(id).toString();
        } else if (id.contains("var")) {
            ret = varStringMap.remove(id);
        } else if (id.contains("float")) {
            ret = varFloatMap.remove(id).toString();
        } else {
            throw new FSLException("Error: delete: id: " + id + " is not supported...");
        }

        if (ret == null) {
            throw new FSLException("Error: delete: Variable : " + id + " Not found...");
        }
    }

    private void add(String id, String val1, String val2) throws Exception {
        if (id == null || val1 == null || val2 == null) {
            throw new FSLException("Error: add: one of the input parameter is passed as null...");
        }

        float l = 0.0f, r = 0.0f;
        if (val1.contains("int")) {
            l = (float) varIntMap.get(val1);
        }
        else if (val1.contains("float")) {
            l = varFloatMap.get(val1);
        }

        if (val2.contains("int")) {
            r = (float) varIntMap.get(val2);
        }
        else if (val2.contains("float")) {
            r = varFloatMap.get(val2);
        }

        if (id.contains("int")) {
            int val = Math.round(l + r);
            varIntMap.computeIfPresent(id, (k, v) -> val);
        } else if (id.contains("float")) {
            float val = l + r;
            varFloatMap.computeIfPresent(id, (k, v) -> val);
        } else {
            throw new FSLException("Error: add: id not properly formatted...");
        }
    }

    private void subtract(String id, String val1, String val2) throws Exception {
        if (id == null || val1 == null || val2 == null) {
            throw new FSLException("Error: subtract: one of the input parameter is passed as null...");
        }

        float l = 0.0f, r = 0.0f;
        if (val1.contains("int")) l = (float) varIntMap.get(val1);
        else if (val1.contains("float")) l = varFloatMap.get(val1);

        if (val2.contains("int")) r = (float) varIntMap.get(val2);
        else if (val2.contains("float")) r = varFloatMap.get(val2);

        if (id.contains("int")) {
            int val = Math.round(l - r);
            varIntMap.computeIfPresent(id, (k, v) -> val);
        } else if (id.contains("float")) {
            float val = l - r;
            varFloatMap.computeIfPresent(id, (k, v) -> val);
        } else {
            throw new FSLException("Error: subtract: id not properly formatted...");
        }
    }

    private void divide(String id, String val1, String val2) throws Exception {
        if (id == null || val1 == null || val2 == null) {
            throw new FSLException("Error: subtract: one of the input parameter is passed as null...");
        }

        float l = 0.0f, r = 0.0f;
        if (val1.contains("int")) l = (float) varIntMap.get(val1);
        else if (val1.contains("float")) l = varFloatMap.get(val1);

        if (val2.contains("int")) r = (float) varIntMap.get(val2);
        else if (val2.contains("float")) r = varFloatMap.get(val2);

        if (r == 0.0) {
            throw new NumberFormatException("Error: divide by zero attempted...");
        }
        if (id.contains("int")) {
            int val = Math.round(l / r);
            varIntMap.computeIfPresent(id, (k, v) -> val);
        } else if (id.contains("float")) {
            float val = l / r;
            varFloatMap.computeIfPresent(id, (k, v) -> val);
        } else {
            throw new FSLException("Error: divide: id not properly formatted...");
        }
    }

    private void multiply(String id, String val1, String val2) throws Exception {
        if (id == null || val1 == null || val2 == null) {
            throw new FSLException("Error: subtract: one of the input parameter is passed as null...");
        }

        float l = 0.0f, r = 0.0f;
        if (val1.contains("int")) l = (float) varIntMap.get(val1);
        else if (val1.contains("float")) l = varFloatMap.get(val1);

        if (val2.contains("int")) r = (float) varIntMap.get(val2);
        else if (val2.contains("float")) r = varFloatMap.get(val2);

        if (id.contains("int")) {
            int val = Math.round(l * r);
            varIntMap.computeIfPresent(id, (k, v) -> val);
        } else if (id.contains("float")) {
            float val = l * r;
            varFloatMap.computeIfPresent(id, (k, v) -> val);
        } else {
            throw new FSLException("Error: multiply: id not properly formatted...");
        }
    }

    private void print(String id) {
        if (id.contains("int")) {
            if (varIntMap.get(id.substring(1)) != null) {
                System.out.println(varIntMap.get(id.substring(1)));
                return;
            }
        } else if (id.contains("var")) {
            if (varStringMap.get(id.substring(1)) != null) {
                System.out.println(varStringMap.get(id.substring(1)));
                return;
            }
        } else if (id.contains("float")) {
            if (varFloatMap.get(id.substring(1)) != null) {
                System.out.println(varFloatMap.get(id.substring(1)));
                return;
            }
        }
        System.out.println("undefined");
    }

    public void printDebugMap(Map<String, String> varMap) {
        Iterator<Map.Entry<String, String>> itrIntMap = varMap.entrySet().iterator();
        while (itrIntMap.hasNext()) {
            Map.Entry intPair = itrIntMap.next();
            System.out.println(intPair.getKey() + " : " + intPair.getValue());
        }
    }

    public void printDebug() throws Exception {
        Iterator<Map.Entry<String, Integer>> itrIntMap = varIntMap.entrySet().iterator();
        while (itrIntMap.hasNext()) {
            Map.Entry intPair = itrIntMap.next();
            System.out.println(intPair.getKey() + " : " + intPair.getValue());
        }

        Iterator<Map.Entry<String, String>> itrStringMap = varStringMap.entrySet().iterator();
        while (itrStringMap.hasNext()) {
            Map.Entry strPair = itrStringMap.next();
            System.out.println(strPair.getKey() + " : " + strPair.getValue());
        }

        Iterator<Map.Entry<String, Float>> itrFLoatMap = varFloatMap.entrySet().iterator();
        while (itrFLoatMap.hasNext()) {
            Map.Entry fltPair = itrFLoatMap.next();
            System.out.println(fltPair.getKey() + " : " + fltPair.getValue());
        }
    }

    protected void execUserCommands(Map<String, String> cmdMap) throws Exception {
        JSONArray cmdUser = (JSONArray) FSLjson.get(cmdMap.get("cmd").substring(1));
        Iterator itercmd = cmdUser.iterator();

        while (itercmd.hasNext()) {
            Iterator<Map.Entry> cmditer = ((Map) itercmd.next()).entrySet().iterator();

            Map<String, String> cmdUserMap = new HashMap<String, String>();
            while (cmditer.hasNext()) {
                Map.Entry pair = cmditer.next();
                cmdUserMap.put(pair.getKey().toString(), pair.getValue().toString());
            }

            // Map between calling #func(#sum) arguments and actual function(add) called
            if (cmdUserMap.get("cmd").equals("add")) {
                String id = cmdMap.get(cmdUserMap.get("id").substring(1));
                String arg1 = cmdMap.get(cmdUserMap.get("operand1").substring(1)).substring(1);
                String arg2 = cmdMap.get(cmdUserMap.get("operand2").substring(1)).substring(1);

                add(id, arg1, arg2);
            } else if (cmdUserMap.get("cmd").equals("subtract")) {
                String id = cmdMap.get(cmdUserMap.get("id").substring(1));
                String arg1 = cmdMap.get(cmdUserMap.get("operand1").substring(1)).substring(1);
                String arg2 = cmdMap.get(cmdUserMap.get("operand2").substring(1)).substring(1);

                subtract(id, arg1, arg2);
            } else if (cmdUserMap.get("cmd").equals("divide")) {
                String id = cmdMap.get(cmdUserMap.get("id").substring(1));
                String arg1 = cmdMap.get(cmdUserMap.get("operand1").substring(1)).substring(1);
                String arg2 = cmdMap.get(cmdUserMap.get("operand2").substring(1)).substring(1);

                divide(id, arg1, arg2);
            } else if (cmdUserMap.get("cmd").equals("multiply")) {
                String id = cmdMap.get(cmdUserMap.get("id").substring(1));
                String arg1 = cmdMap.get(cmdUserMap.get("operand1").substring(1)).substring(1);
                String arg2 = cmdMap.get(cmdUserMap.get("operand2").substring(1)).substring(1);

                multiply(id, arg1, arg2);
            } else if (cmdUserMap.get("cmd").equals("print")) {
                print(cmdUserMap.get("value"));
            } else {
                throw new FSLException("Error: execUserCommands: command not properly mapped...");
            }
        }
    }

    protected void execFSLCommands(String cmdStr) throws Exception {
        JSONArray cmdFSL = (JSONArray) FSLjson.get(cmdStr);
        Iterator itercmd = cmdFSL.iterator();
        // Each setup iterator has individual commands and related parameters
        while (itercmd.hasNext()) {
            //Iterator<Map.Entry> cmditer = ((Map) itercmd.next()).entrySet().iterator();
            Iterator<Map.Entry> cmditer = ((Map) itercmd.next()).entrySet().iterator();

            Map<String, String> cmdMap = new HashMap<String, String> ();
            while (cmditer.hasNext()) {
                Map.Entry pair = cmditer.next();
                cmdMap.put(pair.getKey().toString(), pair.getValue().toString());
            }

            // Execute the commands update, delete etc...
            String cmdRetrived = cmdMap.get("cmd");
            if (cmdRetrived != null) {
                // Update and Create commands
                if (cmdRetrived.equals("update") || cmdRetrived.equals("create")) { // Command : update
                    String id = null;                     // param 1
                    int valInt = 0;                       // param 2
                    String valStr = null;                 // param 2
                    float valFlt = (float) 0.0;           // param 2

                    id = (String) cmdMap.get("id");
                    if (id.contains("int")) {
                        valInt = Integer.parseInt(cmdMap.get("value"));
                    } else if (id.contains("float")) {
                        valFlt = Float.parseFloat(cmdMap.get("value"));;
                    } else if (id.contains("var")) {
                        valStr = (String) cmdMap.get("value");
                    } else {
                        throw new FSLException("Error: update id/value supplied does not conform to specification...");
                    }

                    if (cmdRetrived.equals("update")) {
                        if (id.contains("int")) {
                            update(id, valInt);
                        } else if (id.contains("var")) {
                            update(id, valStr);
                        } else if (id.contains("float")) {
                            update(id, valFlt);
                        }
                    } else if (cmdRetrived.equals("create")) {
                        if (id.contains("int")) {
                            create(id, valInt);
                        } else if (id.contains("var")) {
                            create(id, valStr);
                        } else if (id.contains("float")) {
                            create(id, valFlt);
                        }
                    }
                } else if (cmdRetrived.equals("print")) { // Command : print
                    String prntVal = cmdMap.get("value");
                    if (prntVal != null) {
                        print(prntVal);
                    } else {
                        throw new FSLException("Error: print command not properly formatted...");
                    }
                } else if (cmdRetrived.equals("delete")) {
                    String id = cmdMap.get("id");
                    if (id != null) {
                            delete(id);
                    } else {
                        throw new FSLException("Error: delete command not properly formatted...");
                    }
                } else if (cmdRetrived.charAt(0) == '#') { // Handle all the "#function" commands here #sum, #printall
                    execUserCommands(cmdMap);
                } else {

                }
            } else {
                throw new FSLException("Error: Command Format not recognized...");
            }
        } // while 1
    }

    public void processInit() throws Exception{
        JSONArray initFSL = (JSONArray) FSLjson.get("init"); //Getting JSON object locally here

        // iterating init iterator
        Iterator iterInit = initFSL.iterator();
        while (iterInit.hasNext()) {
            Iterator<Map.Entry> itr1 = ((Map) iterInit.next()).entrySet().iterator();

            // Processing always starts with 'init' tag
            // Rest of the command chain are cascades
            while (itr1.hasNext()) {
                Map.Entry cmdInitPair = itr1.next();
                // Check for the command "setup" if it is properly formatted
                try {
                    if (cmdInitPair.getKey().equals("cmd") &&
                            ((String) cmdInitPair.getValue()).charAt(0) == '#') {
                        String cmdStr = ((String) cmdInitPair.getValue()).substring(1);

                        execFSLCommands(cmdStr);
                    }
                } catch (RuntimeException e) {
                    throw e;
                }
            }
        }
    }

    FSL() throws Exception {
        FSLobj = new JSONParser().parse(new FileReader(JSONFILE.filename));

        // Typecast to JSONObject fropm valimmal Object for token extraction
        FSLjson = (JSONObject) FSLobj;

        //iteratively get all the variables with prefixes var, int and float
        String tempVar = new String ();
        int tempInt = 0;
        float tempFloat;
        double tempDouble = 0.0;

        short i = 1;
        String strName = "var";
        try {
            while (true) {
                String vsname = strName + i;
                tempVar = null;
                tempVar = (String) FSLjson.get(vsname);
                if (tempVar == null) break;
                varStringMap.put(vsname, tempVar);
                i++;
            }
        } catch (RuntimeException e) {
            // Catch all: do nothing
        }

        i = 1;
        String intName = "int";
        try {
            while (true) {
                String viname = intName + i;
                tempInt = Math.toIntExact((long) FSLjson.get(viname));
                varIntMap.put(viname, tempInt);
                i++;
             }
        } catch (RuntimeException e) {
            // Catch all: do nothing
        }

        i = 1;
        String fltName = "float";
        try {
            while (true) {
                String vfname = fltName + i;
                tempDouble = (double) FSLjson.get(vfname);
                tempFloat = (float) tempDouble;
                varFloatMap.put(vfname, tempFloat);
                i++;
            }
        } catch (RuntimeException e) {
            // Catch all: do nothing
        }
    }
}

public class FSLInterpreter {

    public static void main(String[] args) throws Exception
    {
        try {
            FSL fsl = new FSL();
            //fsl.printDebug();
            // FSL Object "init" is always the start of the FSL parsing as per the rule stated
            fsl.processInit();
            //fsl.printDebug();
        } catch (FSLException ex) {
            System.out.println("Gottcha in main()......");
            System.out.println(ex.getMessage());
        }
    }
}
