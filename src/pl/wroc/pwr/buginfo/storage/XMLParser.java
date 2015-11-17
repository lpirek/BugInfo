package pl.wroc.pwr.buginfo.storage;

import pl.wroc.pwr.buginfo.ClassInfo;
import pl.wroc.pwr.buginfo.ProjectInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	
	private String inputFileName;
	
	public XMLParser(String fileName){
		inputFileName = fileName;
	}
	
	public String getFileName(){
		return inputFileName;
	}
	
	public void collectMetrics(ProjectInfo project){
		
		try
		{
			DocumentBuilder dbBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dbBuilder.parse(inputFileName);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("class");
			Node current;
			Element el;
			ClassInfo classInfo;
		
			
			for (int i = 0; i < nList.getLength(); i++)
			{
				current = nList.item(i);
				
				if (current.getNodeType() == Node.ELEMENT_NODE)
				{
					el = (Element) current;
					classInfo = project.getClassByName(el.getElementsByTagName("name").item(0).getTextContent().replace(".", "/"));
					
					if (classInfo != null)
					{
						classInfo.setWMC(Integer.parseInt(el.getElementsByTagName("wmc").item(0).getTextContent()));
						classInfo.setDIT(Integer.parseInt(el.getElementsByTagName("dit").item(0).getTextContent()));
						classInfo.setNOC(Integer.parseInt(el.getElementsByTagName("noc").item(0).getTextContent()));
						classInfo.setCBO(Integer.parseInt(el.getElementsByTagName("cbo").item(0).getTextContent()));
						classInfo.setRFC(Integer.parseInt(el.getElementsByTagName("rfc").item(0).getTextContent()));
						classInfo.setLCOM(Integer.parseInt(el.getElementsByTagName("lcom").item(0).getTextContent()));
						classInfo.setCA(Integer.parseInt(el.getElementsByTagName("ca").item(0).getTextContent()));
						classInfo.setCE(Integer.parseInt(el.getElementsByTagName("ce").item(0).getTextContent()));
						classInfo.setNPM(Integer.parseInt(el.getElementsByTagName("npm").item(0).getTextContent()));
						classInfo.setLCOM3(Double.parseDouble(el.getElementsByTagName("lcom3").item(0).getTextContent()));
						classInfo.setLOC(Integer.parseInt(el.getElementsByTagName("loc").item(0).getTextContent()));
						classInfo.setDAM(Double.parseDouble(el.getElementsByTagName("dam").item(0).getTextContent()));
						classInfo.setMOA(Integer.parseInt(el.getElementsByTagName("moa").item(0).getTextContent()));
						classInfo.setMFA(Double.parseDouble(el.getElementsByTagName("mfa").item(0).getTextContent()));
						classInfo.setCAM(Double.parseDouble(el.getElementsByTagName("cam").item(0).getTextContent()));
						classInfo.setIC(Integer.parseInt(el.getElementsByTagName("ic").item(0).getTextContent()));
						classInfo.setCBM(Integer.parseInt(el.getElementsByTagName("cbm").item(0).getTextContent()));
						classInfo.setAMC(Double.parseDouble(el.getElementsByTagName("amc").item(0).getTextContent()));
					}
					
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
