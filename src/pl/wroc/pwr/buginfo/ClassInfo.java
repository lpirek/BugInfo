/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.wroc.pwr.buginfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author marian
 */
public class ClassInfo {

    private String mName;
    private long mNumOfBugs;
    private long mNumOfModifications;
    private double mModifiedLinesWeightedWithExp;
    private double mModifiedLinesWeightedWithInv;
    private long mNumOfModifiedLines;
    private long mNumOfEveningRevisions;
    private double mCommitersWeightedWithExp;
    private double mCommitersWeightedWithInv;
    private Set<String> mCommiters;

    public ClassInfo(String name){
        mName = name;
        mCommiters = new HashSet<String>();
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the mNumOfBugs
     */
    public long getNumOfBugs() {
        return mNumOfBugs;
    }

    public void addBugs(long newBugs){
        mNumOfBugs += newBugs;
    }

    /**
     * @return the mNumOfModifications
     */
    public long getNumOfModifications() {
        return mNumOfModifications;
    }

    public void addModifications( long newModifications ){
        mNumOfModifications += newModifications;
    }
    
    public double getCommitersWeightedWithExp()
    {
    	return mCommitersWeightedWithExp;
    }
    
    public double getCommitersWeightedWithInv()
    {
    	return mCommitersWeightedWithInv;
    }

    /**
     * @return the mNumOfCommiters
     */
    public long getNumOfCommiters() {
        return mCommiters.size();
    }

    public double getModifiedLinesWeightedWithExp(){
    	return mModifiedLinesWeightedWithExp;
    }
    
    public double getModifiedLinesWeightedWithInv(){
    	return mModifiedLinesWeightedWithInv;
    }
    /**
     * @return the mNumOfModifiedLines
     */
    public long getNumOfModifiedLines() {
        return mNumOfModifiedLines;
    }

    @Override
    public boolean equals(Object o){
        if( o instanceof ClassInfo ){
            return getName().equals( ((ClassInfo)o).getName() );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public void addCommiter(String commiter1) {
        mCommiters.add(commiter1);
    }
    
    
    public void addCommitersWeightedWithExp(double i)
    {
    	mCommitersWeightedWithExp += i;
    }
    
    public void addCommitersWeightedWithInv(double i)
    {
    	mCommitersWeightedWithInv += i;
    }

    public Set<String> getCommiters(){
        return mCommiters;
    }

    public void addNumOfModifiedLines(long i) {
        mNumOfModifiedLines += i;
    }
    
    
    public void addModifiedLinesWeightedWithExp(double i)
    {
    	mModifiedLinesWeightedWithExp += i;
    }
    
    public void addModifiedLinesWeightedWithInv(double i)
    {
    	mModifiedLinesWeightedWithInv += i;
    }

    public void addEveningRevisions( long i){
        mNumOfEveningRevisions += i;
    }

    public long getNumOfEveningRevisions(){
        return mNumOfEveningRevisions;
    }
}
