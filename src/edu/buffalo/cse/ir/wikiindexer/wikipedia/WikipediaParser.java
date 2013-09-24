/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nikhillo
 * This class implements Wikipedia markup processing.
 * Wikipedia markup details are presented here: http://en.wikipedia.org/wiki/Help:Wiki_markup
 * It is expected that all methods marked "todo" will be implemented by students.
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	
	String wikiText;
	public WikipediaParser(String text){
		wikiText = text;
	}
	
	/* TODO */
	/**
	 * Method to parse section titles or headings.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * @param titleStr: The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static String parseSectionTitle(String titleStr) {
		String sectionRegex = " ?={2,6} ?";
		
		if(titleStr!=null){
			return titleStr.replaceAll(sectionRegex, ""); 
		}
		
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * @param itemText: The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {
		String listRegex = "(?m)^[*#;:]* ?";
		
		if(itemText!=null){
			return itemText.replaceAll(listRegex, "");
		}
		
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTextFormatting(String text) {
		String txtFrmtRegex = "('{2,3})|('{5})";
		
		if(text!=null){
			return text.replaceAll(txtFrmtRegex, "");
		}
		
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed.
	 */
	public static String parseTagFormatting(String text) {
		String tagRegex = "(<.*?> ?)|(&lt;.*?&gt;)";
		
		if(text!=null){
			String pattern = text.replaceAll(tagRegex, "");
			return pattern.trim();
		}
		return null;
	}
	
	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	public static String parseTemplates(String text) {
		//String templateRegex = "\\{{2}.*?\\}{2}";
		String templateRegex = "\\{{2}[^{}]*\\}{2}";
		
		while(text.indexOf("{{")!=-1){
			text = text.replaceAll(templateRegex, "");
		}
		
		if(text!=null){
			return text;
		}
		return null;
	}
	
	
	/* TODO */
	/**
	 * Method to parse links and URLs.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * @param text: The text to be parsed
	 * @return An array containing two elements as follows - 
	 *  The 0th element is the parsed text as visible to the user on the page
	 *  The 1st element is the link url
	 */
	public static String[] parseLinks(String text) {
		String[] str = new String[2];
		str[0] = "";
		str[1] = "";
		
		String linkRegex = "(\\[{2})|(\\]{2})";
		//String extlinkRegex = "(\\[\\w+\\:\\/{2}\\w+\\.)([\\w\\W]+)(\\.\\w+)(\\ )([\\w\\s\\W]*)";
		text = text.replaceAll(linkRegex, "");
		
		
		String extlinkRegex = "(\\[\\w+\\:\\/{2})([\\w\\W]+)(\\/)([^\\/\\ ]*)(\\ )([\\w\\s\\W]*)(\\])";
		String simplelinkRegex = "([\\w\\s]*)(\\|{1})([\\w\\s]*)";
		String droplineRegex = "([\\w\\s]*)(\\,{0,1})(\\ )([\\w\\W\\s]*)(\\|{1})";
		String namespaceRegex1 = "([\\w\\s]*)(\\:{1})([\\w\\s\\W]*)(\\|{1})(\\w)([\\w\\s]*)";
		String namespaceRegex2 = "([\\w\\s]*)(\\:{1})([\\w\\s\\:]*)([\\w\\s\\W]*)";
		String namespaceRegex3 = "([\\w\\s]*)(\\:{1})([\\w\\s\\W]*)(\\#{1})(\\w+)([\\W\\s]*)";     //Wikipedia:Manual of Style#Links example
		String namespaceRegex4 = "(\\w{2})(\\:{1})([\\w\\s\\W]*)";                                 //es:Plancton example
		String namespaceRegex5 = "([\\w\\s]*)(\\:{1})([\\w\\s\\S]*)(\\.{1})(\\w+)([\\s\\W]*)";     //File:wiki.png example
		

		if(text!=null){                                                                            
			if(text.matches(extlinkRegex)){
				str[0] = text.replaceAll(extlinkRegex, "$5");
				str[1] = "";
			}
			else if(text.matches(simplelinkRegex)){                                                //simple links
				str[0] = text.replaceAll(simplelinkRegex, "$3");
				String simpleLink1 = text.replaceAll(simplelinkRegex, "$1");
				String simpleLink2 = simpleLink1.substring(0,1).toUpperCase() + simpleLink1.substring(1);
				str[1] = simpleLink2.replaceAll(" ", "_");
			} 
			else if(text.matches(droplineRegex)){                                                  //drop after _ and , automatically
				String drPart1 = text.replaceAll(droplineRegex, "$1");
				String drPart2 = text.replaceAll(droplineRegex, "$2");
				String drPart3 = text.replaceAll(droplineRegex, "$4");
				String drPart1C = drPart1.substring(0, 1).toUpperCase() + drPart1.substring(1);
				str[0] = drPart1;
				str[1] = drPart1C + drPart2 + "_" + drPart3;
			}
			else if(text.matches(namespaceRegex1)){                                                //namespace part1
				str[1] = "";
				str[0] = text.replaceAll(namespaceRegex1, "$5$6");
			}
			else if(text.matches(namespaceRegex2)){                                                //namespace part2
				str[1] = "";
				if(text.matches(namespaceRegex4)){
					str[0] = text;
				}
				else if(text.matches(namespaceRegex5)){
					str[0] = "";
				}
				else if(text.matches(namespaceRegex3)){
					str[0] = text.replaceAll(namespaceRegex3, "$1$2$3$4$5");
				}
				else{
					str[0] = text.replaceAll(namespaceRegex2, "$3");
				}
			}
			else{                                                                                //auto capitalization
				str[0] = text;
				String capPart = text.substring(0, 1).toUpperCase() + text.substring(1);
				str[1] = capPart.replaceAll(" ", "_");
			}
		}
		return str;
	}
	
	
    public void XMLMarkupRemove(){
	
	    wikiText = parseTemplates(wikiText);
	    wikiText = parseTextFormatting(wikiText);
	    wikiText = parseListItem(wikiText);
	    wikiText = parseTagFormatting(wikiText);
	    
	    
	    String linkRegex = "(\\[{2})(.*?)(\\]{2})";
	    
	    Pattern pattern = Pattern.compile(linkRegex);
		Matcher matcher = pattern.matcher(wikiText);
		while(matcher.find()){
			String temp = matcher.group();
		    String[] texturl = parseLinks(temp);
			wikiText = wikiText.replace(temp, texturl[0]);
		}
		
	    System.out.println("Chekcing parsed text : " + wikiText);
	}
	
}
