package net.lotrek.jSQL.transact;

public class StatementContext
{
	
	
	public static enum SQLDataType
	{
		sql_sql_variant(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_xml(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_datetimeoffset(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_datetime2(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_datetime(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_smalldatetime(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_date(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_time(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_float(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_real(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_decimal(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_money(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_smallmoney(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_bigint(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_int(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_smallint(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_tinyint(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_bit(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_ntext(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_text(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_image(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_timestamp(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_uniqueidentifier(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_nvarchar(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_nchar(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_varchar(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_char(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_varbinary(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		sql_binary(new SQLTypeConverter(){
			public SQLEvaluable parseValue(String value)
			{
				return null;
			}

			public SQLEvaluable convertValue(SQLEvaluable value)
			{
				return null;
			}
		}),
		;
		
		private SQLTypeConverter conv;
		
		private SQLDataType(SQLTypeConverter typeConverter)
		{
			conv = typeConverter;
		}
		
		public SQLTypeConverter getTypeConverter()
		{
			return conv;
		}
	}
	
	public static class SQLEvaluable
	{
		
	}
	
	public static interface SQLTypeConverter
	{
		public SQLEvaluable parseValue(String value);
		public SQLEvaluable convertValue(SQLEvaluable value);
	}
	
	public static enum StatementType
	{
		CREATE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		ALTER(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DROP(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DELETE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		INSERT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		UPDATE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		READTEXT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		UDATETEXT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		WRITETEXT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		TRUNCATE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		SELECT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		UNION(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		CASE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		GOTO(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		WAITFOR(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		DECLARE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		SET(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		RETURN(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		EXECUTE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		EXEC(EXECUTE.getStatementType()),
		GRANT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		REVOKE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		PRINT(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		RAISERROR(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
				return null;
			}
		}.getClass()),
		RAISEERROR(RAISERROR.getStatementType()),
		USE(new SQLStatement(){
			public String readStatement(String statement, StatementContext context, SQLStatement lastStatement) {
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
		public String readStatement(String statement, StatementContext context, SQLStatement lastStatement);
	}
}
