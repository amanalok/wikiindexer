/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.text.ParseException;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

/**
 * @author nikhillo
 *
 */
public class Parser {
	/* */
	private final Properties props;
	WikipediaParser wp;
	
	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;
	}
	
	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	public void parse(String filename, final Collection<WikipediaDocument> docs) {
		
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			//final ArrayList<WikipediaDocument> docs1 = new ArrayList(); 
			DefaultHandler handler = new DefaultHandler(){
				
				private int flag = 0, id_flag=0, pageID;
				private String pageTitle, pageAuthor, publishDate,wikiTxt="";
				WikipediaDocument wd;
				
				
				public void startElement(String uri, String localName, String qname, Attributes attributes){
					if(qname.equals("title")){
						flag = 1;
					}
					else if(qname=="id"){
						flag = 2;
						id_flag++;
					}
					else if (qname=="timestamp"){
						flag=3;
					}
					else if (qname=="username" || qname == "ip"){
						flag=4;
					}
					else if (qname=="text"){
						flag=5;
					}
					
				}
				
				public void endElement(String uri, String localName, String qname){
					if(qname=="title" || qname=="timestamp"||qname=="username"||qname=="ip"||qname=="id"||qname=="text"){
						flag = 0;
					}
					
					if(qname == "page"){
						try {
							wd = new WikipediaDocument(pageID,publishDate,pageAuthor,pageTitle);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						wp = new WikipediaParser(wikiTxt);
						wp.XMLMarkupRemove();
						/*System.out.println("page title : " + pageTitle);
						System.out.println("page id : "+pageID);
						System.out.println("date of publish : "+publishDate);
						System.out.println("page author : " +pageAuthor);*/
						System.out.println("text : " +wikiTxt);
						add(wd,docs);
						
						id_flag = 0;
						wikiTxt = "";
					}
					
				}
				
				public void characters(char[] ch, int start, int length){
					if(flag==1){
						pageTitle = new String(ch,start,length);
						//System.out.println("page title : " + pageTitle);
					}
					else if(id_flag==1 && flag==2){
						pageID = Integer.parseInt(new String(ch,start,length));
						//System.out.println("page id : "+pageID);
					}
					else if(flag==3){
						publishDate = new String(ch,start,length);
						//System.out.println("date of publish : "+publishDate);
					}
					else if(flag==4){
						pageAuthor = new String(ch,start,length);
						//System.out.println("page author : " +pageAuthor);
					}
					else if(flag==5){
						wikiTxt += new String(ch, start, length);
					}
					
				}
		};
		
	saxParser.parse("test.xml", handler);
	
	}catch(Exception e){
		e.printStackTrace();
	}
		
	}
	
	/**
	 * Method to add the given document to the collection.
	 * PLEASE USE THIS METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS
	 * For better performance, add the document to the collection only after
	 * you have completely populated it, i.e., parsing is complete for that document.
	 * @param doc: The WikipediaDocument to be added
	 * @param documents: The collection of WikipediaDocuments to be added to
	 */
	private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
		documents.add(doc);
	}
}
