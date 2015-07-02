package net.lotrek.jSQL.transact;

import java.util.HashMap;
import java.util.function.Function;

public class StatementContext
{
	private HashMap<String, TypedValue> variables = new HashMap<>();
	
	public void processStatements(String toProcess)
	{
		StatementTokenizer token = new StatementTokenizer(toProcess);
		SQLStatement lastStatement;
		while(!token.getTokens().isEmpty())
			try {
				(lastStatement = StatementType.valueOf(token.getTokens().poll().toUpperCase()).getStatementType().newInstance()).readStatement(token, this, lastStatement);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
	}
	
	public TypedValue getVariable(String key, TypedValue def)
	{
		return variables.getOrDefault(key, def);
	}
	
	public void setVariable(String key, TypedValue value)
	{
		variables.put(key, value);
	}
	
	public void setVariable(String key, String type, String value)
	{
		setVariable(key, new TypedValue(type, value));
	}
	
	public String evaluateExpression(String expression)
	{
		
		
		return null;
	}
	
	public static class TypedValue
	{
		private String type;
		private String value;
		
		public TypedValue(String type, String value)
		{
			this.type = type;
			this.value = value;
		}
		
		public String getType()
		{
			return type;
		}
		
		public String getValue()
		{
			return value;
		}
	}
	
	public static enum ExpressionEvaluator
	{
		FUNCTION(".*\\([\\s\\S]*\\)", x -> null),
		CASE("case when (.*) then (.*) else (.*) end", x -> {
			
			
			return null;
		}),
		LITERAL("[\\s\\S]*", x -> new TypedValue("", x)),
		;
		
		private String regex;
		private Function<String, TypedValue> consumer;
		
		private ExpressionEvaluator(String regex, Function<String, TypedValue> consumer)
		{
			this.regex = regex;
			this.consumer = consumer;
		}
		
		public static ExpressionEvaluator getBestMatchForExpression(String expr)
		{
			for (ExpressionEvaluator item : ExpressionEvaluator.values())
				if(expr.matches(item.regex))
					return item;
			
			return null;
		}
		
		public TypedValue consumeExpression(String expr)
		{
			return consumer.apply(expr);
		}
	}
	
	public static enum StatementType
	{
		CREATE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		ALTER(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DROP(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DELETE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		INSERT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		UPDATE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		READTEXT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		UDATETEXT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		WRITETEXT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		TRUNCATE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		SELECT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				token.getTokens().clear();
				
				return null;
			}
		}.getClass()),
		UNION(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		CASE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		GOTO(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		WAITFOR(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DECLARE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement)
			{
				while(token.getTokens().peek().startsWith("@"))
				{
					String name = token.getTokens().poll(), type = token.getTokens().poll().replace(",", ""), value = null;
					if(token.getTokens().peek().equals("="))
					{
						token.getTokens().poll();
						value = token.getTokens().poll();
					}
					
					context.setVariable(name, new TypedValue(type, value));
				}
				
				
				return null;
			}
		}.getClass()),
		SET(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				
				String var = token.getTokens().poll(), assignment = token.getTokens().poll(), expr = token.getTokens().poll();
				
				context.setVariable(var, context.getVariable(var, null).getType(), expr);
				
				return null;
			}
		}.getClass()),
		RETURN(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		EXECUTE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		EXEC(EXECUTE.getStatementType()),
		GRANT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		REVOKE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		PRINT(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		RAISERROR(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		RAISEERROR(RAISERROR.getStatementType()),
		USE(new SQLStatement(){
			public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		;
		
		private Class<? extends SQLStatement> statementType;
		
		private StatementType(Class<? extends SQLStatement> statementType)
		{
			this.statementType = statementType;
		}
		
		public Class<? extends SQLStatement> getStatementType()
		{
			return statementType;
		}
	}
	
	public static interface SQLStatement
	{
		public String readStatement(StatementTokenizer token, StatementContext context, SQLStatement lastStatement);
	}
}
