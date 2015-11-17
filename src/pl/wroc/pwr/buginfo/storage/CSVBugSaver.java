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
            {
                line.append( ";"+currentClass.getNumOfModifiedLines() );
            	line.append( ";"+currentClass.getModifiedLinesWeightedWithExp() );
            	line.append( ";"+currentClass.getModifiedLinesWeightedWithInv() );
            }
            
            line.append(";"+currentClass.getWMC());
            line.append(";"+currentClass.getDIT());
            line.append(";"+currentClass.getNOC());
            line.append(";"+currentClass.getCBO());
            line.append(";"+currentClass.getRFC());
            line.append(";"+currentClass.getLCOM());
            line.append(";"+currentClass.getCA());
            line.append(";"+currentClass.getCE());
            line.append(";"+currentClass.getNPM());
            line.append(";"+currentClass.getLCOM3());
            line.append(";"+currentClass.getLOC());
            line.append(";"+currentClass.getDAM());
            line.append(";"+currentClass.getMOA());
            line.append(";"+currentClass.getMFA());
            line.append(";"+currentClass.getCAM());
            line.append(";"+currentClass.getIC());
            line.append(";"+currentClass.getCBM());
            line.append(";"+currentClass.getAMC());
            	
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
        {
            header.append(";Number of Bugs");
            System.out.println("Number of bugs");
        }
        if( mWriteModifications )
            header.append( ";Number of Revisions");
        if(mWriteCommiters)
        {
            header.append(";Number of Commiters");
            header.append(";Commiters weighted with experience");
            header.append(";Commiters weighted with involvement");
        }
        if(mWriteModifiedLines)
        {
            header.append(";Number of Modified Lines");
        	header.append(";Number of Modified Lines weighted with experience");
        	header.append(";Number of Modified Lines weighted with involvement");
        }
        
        header.append(";WMC");
        header.append(";DIT");
        header.append(";NOC");
        header.append(";CBO");
        header.append(";RFC");
        header.append(";LCOM");
        header.append(";CA");
        header.append(";CE");
        header.append(";NPM");
        header.append(";LCOM3");
        header.append(";LOC");
        header.append(";DAM");
        header.append(";MOA");
        header.append(";MFA");
        header.append(";CAM");
        header.append(";IC");
        header.append(";CBM");
        header.append(";AMC");
        
        output.println( header.toString() );
    }

}
