package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhitespaceRule {
	
	public void apply(TokenStream stream) throws TokenizerException {
		if(stream!=null)
		{
			String token;
			while (stream.hasNext()) 
			{
				token = stream.next();
				stream.remove();
				if (token != null)
				{	
				// write functinality here....
					String spaceRegex = "([\\w\\W]+)";
					Pattern pattern = Pattern.compile(spaceRegex);
					Matcher matcher = pattern.matcher(token);
					while(matcher.find()){
						String temp = matcher.group();
						stream.append(temp);
					}					
				}
			}
			
		}
		
	}

}
