package com.example.assignment3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class SSEngine {
    private final static String columns[]={"A","B","C","D"};
    private final static int NROWS =4;
    // association id->it.unitn.ronchet.Spreadsheet.Cell for of all cells in the spreadsheet
    HashMap<String,Cell> cellMap=new HashMap<>();
    // for every ID, list all cells in the formula of which the ID is present
    // its needed because when we update a cell, we must also update all the ones which depend on it
    HashMap<String, HashSet<Cell>> dependance=new HashMap<>();

    private static SSEngine engine=null;
    // singleton pattern -----------
    private SSEngine(){
    }
    public static SSEngine getSSEngine(){
        if (engine==null) {
            engine=new SSEngine();
            engine.setup();
        }
        return engine;
    }
    // end of singleton pattern -----------


    public Set<Cell> modifyCell(String id, String formula, String timestamp) {
        LinkedList<Cell> affectedCells=new LinkedList<>();
        Cell theCell=cellMap.get(id);
        Cell clone=theCell.clone();
        theCell.setFormula(formula);
        theCell.setTimestamp(Long.parseLong(timestamp));
        if (! theCell.checkCircularDependencies(id)) {
            //restore cell
            theCell.formula=clone.formula;
            theCell.id=clone.id;
            theCell.value=clone.value;
            theCell.timestamp = clone.value;
            return null;
        }
        // remove all the old dependencies, looping over operands before modification
        for (String o: clone.operands) {
            if (! Cell.isOperandNumeric(o)) {
                HashSet<Cell> cellSet = engine.dependance.get(o);
                cellSet.remove(theCell);
            }
        }
        // add all new dependencies
        for (String o: theCell.operands) {
            if (! Cell.isOperandNumeric(o))  {
                HashSet<Cell> cellSet = engine.dependance.get(o);
                cellSet.add(theCell);
            }
        }
        return theCell.recursiveEvaluateCell();
    }

    //============= INTERNAL METHODS - DO NOT CALL =================================================
    private void setup(){
        //create a set of empty cells
        for (int i = 1; i<= NROWS; i++){
            for (String a : columns) {
                String id=a+i;
                Cell c=new Cell(id,"");
                addCell(c);
            }
        }
    }

    void addCell(Cell c){
        cellMap.put(c.id,c);
        dependance.put(c.id,new HashSet<Cell>());
    }

    Set<Cell>  modifyCellAndPrint(String id, String formula, String timestamp) {
        Set<Cell> modifiedCells=modifyCell(id, formula, timestamp);
        if (modifiedCells!=null) {
            System.out.print("MODIFIED CELLS: ");
            for (Cell c : modifiedCells) {
                System.out.print(c.id + " ");
            }
            System.out.println();
        }
        System.out.println(engine.cellMap.get(id));
        return modifiedCells;
    }

    public String cellToJsonStr(){
        String toRet = "[";
        Cell[] modifiedArray =  new Cell[cellMap.size()];
        int i = 0;
        for (String key: cellMap.keySet()){
            //System.out.println(cellMap.get(key));
            modifiedArray[i] = cellMap.get(key);
            i++;
        }
        for(i = 0; i < modifiedArray.length-1; i++){
            toRet += "{\"" + "id" + "\"" + ":\"" + modifiedArray[i].id + "\",";
            toRet += "\"" + "value" + "\"" + ":\"" + modifiedArray[i].value + "\",";
            toRet += "\"" + "timestamp" + "\"" + ":\"" + modifiedArray[i].timestamp + "\",";
            toRet += "\"" + "formula" + "\"" + ":\"" + modifiedArray[i].formula + "\"},";
        }
        toRet += "{\"" + "id" + "\"" + ":\"" + modifiedArray[i].id + "\",";
        toRet += "\"" + "value" + "\"" + ":\"" + modifiedArray[i].value + "\",";
        toRet += "\"" + "timestamp" + "\"" + ":\"" + modifiedArray[i].timestamp + "\",";
        toRet += "\"" + "formula" + "\"" + ":\"" + modifiedArray[i].formula + "\"}]";

        //System.out.println(toRet);
        return toRet;
    }


    boolean isTimestampedDifferent(HashMap<String, Long> ts){
        AtomicReference<Boolean> toRet = new AtomicReference<>(false);
        ts.forEach((k,v) -> { if( this.cellMap.get(k).timestamp != v){
                toRet.set(true);
            }
        });
        return toRet.get();
    }
    // ============ TESTING METHOD =============================================
    public static void main(String arg[]){

        Gson gson = new GsonBuilder().create();

        // simple elements
        getSSEngine();
        engine.cellToJsonStr();
        /*engine.modifyCellAndPrint("A1","2"); // OK
        engine.modifyCellAndPrint("B1","=A1"); //OK

        // formulas with operators, without circular references
        engine.modifyCellAndPrint("B2","=6/2"); //OK
        engine.modifyCellAndPrint("B3","=B2*B1"); //OK
        engine.modifyCellAndPrint("B4","=A1-5"); //OK

        // modify a cell that causes a cascade of modifications
        engine.modifyCellAndPrint("A1","5"); // OK

        // formula containing a string
        engine.modifyCellAndPrint("D1","pippo"); // OK, evaluates to 0

        // formula containing one or more syntax errors (non-existing references)
        engine.modifyCell("D3","7"); // cell used in the following
        engine.modifyCellAndPrint("D1","=K1"); // non existing cell,evaluates to 0
        engine.modifyCellAndPrint("D1","=pippo"); // non existing cell,  evaluates to 0
        engine.modifyCellAndPrint("D1","=D3+K1"); // formula containing a non existing cell, evaluates to 0
        engine.modifyCellAndPrint("D1","=D3*pippo"); // formula containing a non existing cell, evaluates to 0
        engine.modifyCellAndPrint("D1","=D3*+D3"); // syntax error, evaluates to 0

        // formulas with operators, with circular references
        // simple circular reference (self)
        engine.modifyCellAndPrint("A1","=A1+B2"); //NO!
        //more complex circular reference
        engine.modifyCellAndPrint("A1","=B1*2"); //NO
        engine.modifyCellAndPrint("A1","=B3*2"); //NO*/

    }
}
