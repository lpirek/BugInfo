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
    
    private int mWMC;
    private int mDIT;
    private int mNOC;
    private int mCBO;
    private int mRFC;
    private int mLCOM;
    private int mCA;
    private int mCE;
    private int mNPM;
    private double mLCOM3;
    private int mLOC;
    private double mDAM;
    private int mMOA;
    private double mMFA;
    private double mCAM;
    private int mIC;
    private int mCBM;
    private double mAMC;
    
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
    
    public boolean isCommiter(String commiterName)
    {
    	return mCommiters.contains(commiterName);
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
    
    public int getWMC(){
    	return mWMC;
    }
    
    public int getDIT(){
    	return mDIT;
    }
    
    public int getNOC(){
    	return mNOC;
    }
    
    public int getCBO(){
    	return mCBO;
    }
    
    public int getRFC(){
    	return mRFC;
    }
    
    public int getLCOM(){
    	return mLCOM;
    }
    
    public int getCA(){
    	return mCA;
    }
    
    public int getCE(){
    	return mCE;
    }
    
    public int getNPM(){
    	return mNPM;
    }
    
    public double getLCOM3(){
    	return mLCOM3;
    }
    
    public int getLOC(){
    	return mLOC;
    }
    
    public double getDAM(){
    	return mDAM;
    }
    
    public int getMOA(){
    	return mMOA;
    }
    
    public double getMFA(){
    	return mMFA;
    }
    
    public double getCAM(){
    	return mCAM;
    }
    
    public int getIC(){
    	return mIC;
    }
    
    public int getCBM(){
    	return mCBM;
    }
    
    public double getAMC(){
    	return mAMC;
    }
    

    public void setWMC(int wmc){
    	mWMC = wmc;
    }
    
    public void setDIT(int dit){
    	mDIT = dit;
    }
    
    public void setNOC(int noc){
    	mNOC = noc;
    }
    
    public void setCBO(int cbo){
    	mCBO = cbo;
    }
    
    public void setRFC(int rfc){
    	mRFC = rfc;
    }
    
    public void setLCOM(int lcom){
    	mLCOM = lcom;
    }
    
    public void setCA(int ca){
    	mCA = ca;
    }
    
    public void setCE(int ce){
    	mCE = ce;
    }
    
    public void setNPM(int npm){
    	mNPM = npm;
    }
    
    public void setLCOM3(double lcom3){
    	mLCOM3 = lcom3;
    }
    
    public void setLOC(int loc){
    	mLOC = loc;
    }
    
    public void setDAM(double dam){
    	mDAM = dam;
    }
    
    public void setMOA(int moa){
    	mMOA = moa;
    }
    
    public void setMFA(double mfa){
    	mMFA = mfa;
    }
    
    public void setCAM(double cam){
    	mCAM = cam;
    }
    
    public void setIC(int ic){
    	mIC = ic;
    }
    
    public void setCBM(int cbm){
    	mCBM = cbm;
    }
    
    public void setAMC(double amc){
    	mAMC = amc;
    }
}
