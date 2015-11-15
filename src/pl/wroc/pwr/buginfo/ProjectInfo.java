/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo;

import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 *
 * @author marian
 */
public class ProjectInfo {
    private Map< String,ClassInfo > mClasses = new HashMap< String,ClassInfo >();
    private Map<String, ProgrammerInfo> mProgrammers = new HashMap<String, ProgrammerInfo>();
    private Date mFirstCommit = null;
    private Date mLastCommit = null;
    
    public Date getFirstCommit()
    {
    	return mFirstCommit;
    }
    
    public Date getLastCommit()
    {
    	return mLastCommit;
    }

    public ClassInfo getClassByName( String name ){

        return mClasses.get(name);
    }
    
    public ProgrammerInfo getProgrammerByName(String name)
    {
    	return mProgrammers.get(name);
    }
    
    public Set<String> getClassNames(){
        return mClasses.keySet();
    }
    
    public Set<String> getProgrammerNames(){
    	return mProgrammers.keySet();
    }

    public void addBugs(String className, long numOfBugs){
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addBugs(numOfBugs);
    }
    
    public void addCommit(String programmerName, Date date)
    {
    	ProgrammerInfo toUpdate = getProgrammerToUpdate(programmerName);
    	toUpdate.addCommit(date);
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
    
    private ProgrammerInfo getProgrammerToUpdate (String name)
    {
    	ProgrammerInfo toUpdate = mProgrammers.get(name);
    	if (toUpdate == null)
    	{
    		toUpdate = new ProgrammerInfo(name);
    		mProgrammers.put(name,  toUpdate);
    	}
    	return toUpdate;
    }

    public void addCommiter(String className, String commiter) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addCommiter(commiter);
    }
    
    public void addCommiterWeightedWithExp(String className, double i) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addCommitersWeightedWithExp(i);
    }
    
    public void addCommiterWeightedWithInv(String className, double i) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addCommitersWeightedWithInv(i);
    }

    public void addModifiedLines(String className, long numOfLines) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addNumOfModifiedLines(numOfLines);
    }
    
    public void addModifiedLinesWeightedWithExp(String className, double i) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addModifiedLinesWeightedWithExp(i);
    }
    
    public void addModifiedLinesWeightedWithInv(String className, double i) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addModifiedLinesWeightedWithInv(i);
    }

     public void addEveningRevisions(String className, long numOfRevisions) {
        ClassInfo toUpdate = getClassToUpdate(className);
        toUpdate.addEveningRevisions(numOfRevisions);
    }
     
     public void setFirstCommit(Date first)
     {
    	 mFirstCommit = first;
     }
     
     public void setLastCommit(Date last)
     {
    	 mLastCommit = last;
     }
     
     public void calculateProgrammerAttributes()
     {
    	 int releaseLength = Days.daysBetween(new DateTime(mFirstCommit), new DateTime(mLastCommit)).getDays();
    	 if (mFirstCommit != null && mLastCommit != null)
    	 {
    		 Iterator<String> itr = mProgrammers.keySet().iterator();

    	        while(itr.hasNext()){
    	        	
    	            ProgrammerInfo currentP = mProgrammers.get(itr.next());
    	            
    	            try{
	    				int dayOfFirstCommit = currentP.getDayOfFirstCommit(mFirstCommit);
	    				int numberOfWorkDays = currentP.getNumberOfWorkDays(mFirstCommit, mLastCommit);
	    				currentP.setExperience((double)(releaseLength - dayOfFirstCommit)/releaseLength);
	    				currentP.setInvolvement((double) numberOfWorkDays/releaseLength);
	    				
	    				System.out.println("Programmer: "+ currentP.getName()+", Experience: " + currentP.getExperience()+", involvement: "+ currentP.getInvolvement());
    	            }
    	            catch(Exception e)
    	            {
    	            	currentP.setExperience(0);
    	            	currentP.setInvolvement(0);
    	            }
    	        }
    	 }
     }
     
     
     public long getNumberOfAllCommits()
     {
    	 int i = 0;
    	 Iterator<String> itr = mProgrammers.keySet().iterator();

	        while(itr.hasNext()){ 
	        	ProgrammerInfo p = getProgrammerByName(itr.next());
	        		i += p.getNumberOfCommits();
	        }
	        
	        return i;
     }
     
     public boolean isCommiter(String className, String commiterName)
     {
    	 return getClassByName(className).isCommiter(commiterName);
     }
}
