/**
 * Created by Evan on 5/12/2016.
 */

import java.util.*;
import java.io.*;

public class CodeSandbox {
    public static void main(String[] args) throws FileNotFoundException{

        FileInput.init();

        String testString = "doth";
        QPHash QPBac = new QPHash();
        ChainingHash CHBac = new ChainingHash();

        QPHash QPShakes = new QPHash();
        ChainingHash CHShakes = new ChainingHash();

        for (String s : FileInput.readShakespeare()) {
            QPShakes.insert(s);
            CHShakes.insert(s);
        }

        for (String s : FileInput.readBacon()) {
            QPBac.insert(s);
            CHBac.insert(s);
        }

        System.out.println("QPShakes: " + QPShakes.findCount(testString));
        System.out.println("CHShakes: " + CHShakes.findCount(testString));
        System.out.println("--------------------");
        System.out.println("QPBac: " + QPBac.findCount(testString));
        System.out.println("CHBac: " + CHBac.findCount(testString));


    }
}
