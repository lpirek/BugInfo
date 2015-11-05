/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.wroc.pwr.buginfo.*;

/**
 *
 * @author mjureczk
 */
class CSVBugSaver extends BugSaver{

    private FileWriter outputFile;
    private String outputFileName;

    CSVBugSaver(String fileName, Properties config) throws IOException {
        outputFileName = fileName;
        outputFile = new FileWriter(fileName);
        mWriteModifications = config.readNumberOfModifications();
        mWriteCommiters = config.readNumberOfCommiters();
        mWriteModifiedLines = config.readNumberOfModifiedLines();
        mWriteBugs = config.readNumberOfBugs();
    }

    @Override
    public List<String> save(ProjectInfo bugs) {
        PrintWriter output = new PrintWriter( outputFile );
        Iterator<String> itr = bugs.getClassNames().iterator();

        printHeader(output);

        while( itr.hasNext() ){
            String key = itr.next();
            ClassInfo currentClass = bugs.getClassByName(key);
            StringBuffer line = new StringBuffer(key);
            if(mWriteBugs)
                line.append(";" + currentClass.getNumOfBugs());
            if( mWriteModifications )
                line.append( ";"+currentClass.getNumOfModifications() );
            if(mWriteCommiters)
            {
                line.append( ";"+currentClass.getNumOfCommiters() );
                line.append( ";"+currentClass.getCommitersWeightedWithExp() );
                line.append( ";"+currentClass.getCommitersWeightedWithInv() );
            }
            if(mWriteModifiedLines)
                line.append( ";"+currentClass.getNumOfModifiedLines() );
            output.println( line.toString() );
        }
        output.close();
        return new ArrayList<String>();
    }

    String getFileName(){
        return outputFileName;
    }

    private void printHeader(PrintWriter output) {
        StringBuffer header = new StringBuffer("Class Name");
        if(mWriteBugs)
            header.append(";Number of Bugs");
        if( mWriteModifications )
            header.append( ";Number of Revisions");
        if(mWriteCommiters)
        {
            header.append(";Number of Commiters");
            header.append(";Commiters weighted with experience");
            header.append(";Commiters weighted with involvement");
        }
        if(mWriteModifiedLines)
            header.append(";Number of Modified Lines");
        output.println( header.toString() );
    }

}
