package pl.wroc.pwr.buginfo;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class ProgrammerInfo {

	private String mName;
	private double mExperience;
	private double mInvolvement;
	
	DateFormat formatter;
	
	private List<Date> mCommits;
	
	
	public ProgrammerInfo(String name) {
		mName = name;
		mCommits = new ArrayList<Date>();
		
		formatter = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	public String getName() {
		return mName;
	}
	
	public double getExperience()
	{
		return mExperience;
	}
	
	public double getInvolvement()
	{
		return mInvolvement;
	}
	
	public void addCommit(Date date) {
		try{
			mCommits.add(formatter.parse(formatter.format(date)));
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public int getDayOfFirstCommit(Date firstProjectCommit) throws Exception {
		
		if (mCommits.isEmpty()) 
			throw new Exception("Programista nie dokona³ ¿adnych zmian na kodzie!");
		
		return Days.daysBetween(new DateTime(firstProjectCommit), new DateTime(mCommits.get(0))).getDays();
	}
	
	public int getNumberOfWorkDays(Date firstProjectCommit, Date lastProjectCommit) {
		
		int numberOfWorkDays = 0;
		
		Set<Date> days = new HashSet<>();
		days.addAll(mCommits);
		
		for (Date date : days) {
			if (firstProjectCommit.getTime() <= date.getTime() && date.getTime() <= lastProjectCommit.getTime()) {
				numberOfWorkDays++;
			}
		}
		
		return numberOfWorkDays;
	}
	
	
	public void setExperience(double exp)
	{
		mExperience = exp;
	}
	
	public void setInvolvement(double inv)
	{
		mInvolvement = inv;
	}
	
}
