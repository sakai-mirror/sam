package org.sakaiproject.tool.assessment.services.qti;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Arrays;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.sakaiproject.tool.assessment.facade.ItemFacade;
import org.sakaiproject.tool.assessment.qti.asi.Item;
import org.sakaiproject.tool.assessment.qti.asi.PrintUtil;
import org.sakaiproject.tool.assessment.qti.constants.QTIVersion;
import org.sakaiproject.tool.assessment.qti.helper.ExtractionHelper;
import org.sakaiproject.tool.assessment.qti.util.XmlUtil;
import org.w3c.dom.Document;

import junit.framework.TestCase;

public class QTIServiceTest extends TestCase {
	private static final Logger log = Logger.getLogger(QTIServiceTest.class);
	private static int[] ignore = {0,4};

	public void xtestImport() throws Exception {
		Document document = getDocument("exportAssessment.xml");
		ItemFacade item = extractItem(document);
		String output = PrintUtil.printItem(item);
		log.info(output);
	}

	public void xtestCreateImportedAssessment() throws Exception {
		for(int index = 1; index <= 4; index++){
			testCreateImportedAssessment(index);
		}
	}
	
	private void testCreateImportedAssessment(int index) throws Exception {
		Document document = getDocument("exportEMI-"+index+".xml");
		ItemFacade item = extractItem(document);
		String output = PrintUtil.printItem(item);
		log.info(output);
		String[] outputLines = output.split("\n");
		BufferedReader in = new BufferedReader(new InputStreamReader(QTIServiceTest.class.getClassLoader().getResourceAsStream("printEMI-"+index+".txt")));
		String line = null;
		boolean inAtt = false;
		for(int i = 0; (line = in.readLine()) != null && i < outputLines.length; i++){
			if(line.contains("AttachmentSet")){
				inAtt = true;
			}
			if(inAtt){
				if(line.contains("----- End -----")){
					inAtt = false;
				}
				continue;
			}
			if(Arrays.binarySearch(ignore, i) >= 0) continue;
			if(line.trim().startsWith("-----")) continue;
			if(line.trim().startsWith("Sequence(")) continue;
			assertEquals("Test " + index + ": Line " + (i+1), line, outputLines[i]);
		}
	}
	
	public static void printDocument(Document doc, OutputStream out) throws Exception {
	    TransformerFactory tf = TransformerFactory.newInstance();
	    Transformer transformer = tf.newTransformer();
	    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

	    transformer.transform(new DOMSource(doc), 
	         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}
	
	private Document getDocument(String fileName) throws Exception {
		URL url = QTIServiceTest.class.getClassLoader().getResource(fileName);
		if(url == null){
			throw new IllegalArgumentException("Could not find the test file, " + fileName + "! Stopping test.");
		}
		String file = url.getPath();
		log.info("File: " + file);
		return XmlUtil.readDocument(file, true);
	}

	private ItemFacade extractItem(Document document) {
		ExtractionHelper exHelper = new ExtractionHelper(QTIVersion.VERSION_1_2);
		ItemFacade item = new ItemFacade();
		Item itemXml = new Item(document, QTIVersion.VERSION_1_2);
		exHelper.updateItem(item, itemXml);
		return item;
	}

}
