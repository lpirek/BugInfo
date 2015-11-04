/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo;

import java.util.*;

/**
 *
 * @author marian
 */
public class ProjectInfo {
    private Map< String,ClassInfo > mClasses = new HashMap< String,ClassInfo >();

    public ClassInfo getClassByName( String name ){

        return mClasses.get(name);
    }

    public Set<String> getClassNames(){
        return mClasses.keySet();
    }

    public void addBugs(String className, long numOfBugs){
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addBugs(numOfBugs);
    }

    public boolean isEmpty(){
        return mClasses.isEmpty();
    }

    public int size(){
        return mClasses.size();
    }

    public void addModifications(String className, long numOfModifications) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addModifications(numOfModifications);
        
    }

    public long getNumberOfBuggyClasses(){
        Iterator<String> itr = mClasses.keySet().iterator();
        long numOfBuggyClasses=0;

        while(itr.hasNext()){
            ClassInfo currentClass = mClasses.get(itr.next());
            if( currentClass.getNumOfBugs() > 0 ){
                numOfBuggyClasses++;
            }
        }

        return numOfBuggyClasses;
    }

    private ClassInfo getClassToUpdate(String className) {
        ClassInfo toUpdate = mClasses.get(className);
        if (toUpdate == null) {
            toUpdate = new ClassInfo(className);
            mClasses.put(className, toUpdate);
        }
        return toUpdate;
    }

    public void addCommiter(String className, String commiter) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addCommiter(commiter);
    }

    public void addModifiedLines(String className, long numOfLines) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addNumOfModifiedLines(numOfLines);
    }

     public void addEveningRevisions(String className, long numOfRevisions) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addEveningRevisions(numOfRevisions);
    }
}
