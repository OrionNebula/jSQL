package net.lotrek.jSQL.transact;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class StatementTokenizer
{
	private Queue<String> tokens = new LinkedBlockingQueue<>();
	
	public StatementTokenizer(String source)
	{
		boolean inString = false, inBrackets = false;
		int parenDepth = 0;
		String tmpToken = "";
		for(int i = 0; i < source.length(); i++)
		{
			if(source.charAt(i) == '\'' && (i+1 != source.length() && source.charAt(i+1) != '\'') && !inBrackets)
				inString = !inString;
			
			if(source.charAt(i) == '[' && !inString)
				inBrackets = true;
			
			if(source.charAt(i) == ']' && !inString)
				inBrackets = false;
			
			if(source.charAt(i) == '(')
				parenDepth++;
			if(source.charAt(i) == ')')
				parenDepth--;
			
			if((source.charAt(i) == ' ' || source.charAt(i) == ';') && !inBrackets && !inString && parenDepth == 0)
			{
				if(!tmpToken.isEmpty())
					tokens.add(tmpToken);
				tmpToken = "";
			}else
				tmpToken += source.charAt(i);
		}
	
		if(!tmpToken.isEmpty())
			tokens.add(tmpToken);
	}
	
	public Queue<String> getTokens()
	{
		return tokens;
	}
	
	private String alteredSource = "";
	public String getAlteredSource()
	{
		tokens.forEach(x -> alteredSource += x + " ");
		
		return alteredSource;
	}
}
