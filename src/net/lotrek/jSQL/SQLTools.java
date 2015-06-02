package net.lotrek.jSQL;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lotrek.jSQL.packet.PacketHeader;
import net.lotrek.jSQL.packet.SQLPacket;

public class SQLTools
{
	@SuppressWarnings("restriction")
	public static int getPID()
	{
		try {
			java.lang.management.RuntimeMXBean runtime = 
				    java.lang.management.ManagementFactory.getRuntimeMXBean();
				java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
				jvm.setAccessible(true);
				sun.management.VMManagement mgmt =  
				    (sun.management.VMManagement) jvm.get(runtime);
				java.lang.reflect.Method pid_method =  
				    mgmt.getClass().getDeclaredMethod("getProcessId");
				pid_method.setAccessible(true);

				return (Integer) pid_method.invoke(mgmt);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException
				| NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static void sendAggrigatePacket(DataOutputStream dos, PacketHeader head, SQLPacket pack) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream tmpDos = new DataOutputStream(baos);
		
		head.write(tmpDos);
		pack.write(tmpDos);
		
		dos.write(baos.toByteArray());
		
		baos.close();
		tmpDos.close();
	}
	
	public static void fillFieldSeriesWithValues(Object owner, String[] fields, Object[] values)
	{
		for (int i = 0; i < fields.length; i++)
			try {
				Field field = owner.getClass().getDeclaredField(fields[i]);
				field.setAccessible(true);
				field.set(owner, values[i]);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
	}
	
	public static Object[] generateArrayWithMethodCall(int iter, Object owner, Method method, Object...args)
	{
		Object[] toReturn = new Object[iter];
		
		for (int i = 0; i < toReturn.length; i++)
			try {
				toReturn[i] = method.invoke(owner, args);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		
		return toReturn;
	}
	
	public static Object[] callMethodForArray(Object[] arr, Object owner, Method method)
	{
		for (int i = 0; i < arr.length; i++)
			try {
				arr[i] = method.invoke(owner, arr[i]);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		
		return arr;
	}
	
	public static byte[] swapPasswordEncrypt(byte[] password)
	{
		byte[] toReturn = new byte[password.length];
		
		for (int i = 0; i < password.length; i++)
		{
			int ch = password[i] ^ 0xA5;
			toReturn[i] = (byte)(((ch >> 4) & 0xf) | ((ch & 0xf) << 4));
		}
		
		return toReturn;
	}
	
	public static void writeVarchar(String str, DataOutputStream dos) throws IOException
	{
		for (int i = 0; i < str.length(); i++)
		{
			short ch = (short) str.charAt(i);
			dos.writeShort(Short.reverseBytes(ch));
		}
	}
}
