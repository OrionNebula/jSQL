package net.lotrek.jSQL.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PreLogin implements SQLPacket
{
	private ArrayList<PRELOGIN_OPTION> preloginOption = new ArrayList<>();
	
	public void read(PacketHeader head, DataInputStream dis) throws IOException
	{
		int readTot = head.getTotLength() - 8;
		
		while(true)
		{
			PRELOGIN_OPTION opt = new PRELOGIN_OPTION();
			int readLen = opt.read(dis);
			readTot -= readLen;
			preloginOption.add(opt);
			if(opt.getOptionToken() == PL_OPTION_TOKEN.TERMINATOR)
				break;
		}
		
		byte[] b = new byte[readTot];
		dis.readFully(b);
		for (PRELOGIN_OPTION opt : preloginOption)
		{
			if(opt.getOptionToken() == PL_OPTION_TOKEN.TERMINATOR)
				continue;
			
			byte[] data = new byte[opt.getLength()];
			System.arraycopy(b, opt.getOffset() - (head.getTotLength() - 8 - readTot), data, 0, opt.getLength());
			opt.setData(data);
		}
	}

	public void write(DataOutputStream dos) throws IOException
	{
		int headLen = getHeadingLength();
		
		for (PRELOGIN_OPTION opt : preloginOption)
			opt.write(headLen, dos);
		
		dos.write(buildPacketData());
	}

	public PRELOGIN_OPTION getOptionByToken(PL_OPTION_TOKEN token)
	{
		for (PRELOGIN_OPTION opt : preloginOption)
			if(opt.getOptionToken() == token)
				return opt;
		
		return null;
	}
	
	private int getHeadingLength()
	{
		int len = 0;
		for (PRELOGIN_OPTION opt : preloginOption)
			len += opt.getOptionLength();
		return len;
	}
	
	private byte[] buildPacketData()
	{
		PRELOGIN_OPTION maxOpt = null;
		for (PRELOGIN_OPTION opt : preloginOption)
			if(maxOpt == null || opt.getOffset() > maxOpt.getOffset())
				maxOpt = opt;
		
		byte[] b = new byte[maxOpt.getOffset() + maxOpt.getLength()];
		for (PRELOGIN_OPTION opt : preloginOption)
			System.arraycopy(opt.data, 0, b, opt.getOffset(), opt.getLength());
		
		return b;
	}
	
	public int getPayloadLength()
	{
		return getHeadingLength() + buildPacketData().length;
	}
	
	public void addPreLoginOption(PRELOGIN_OPTION opt)
	{
		preloginOption.add(opt);
	}
	
	public ArrayList<PRELOGIN_OPTION> getPreLoginOptions()
	{
		return preloginOption;
	}
	
	public String toString() {
		return "PreLogin [preloginOption=" + preloginOption + "]";
	}
	
	public static class PRELOGIN_OPTION
	{
		public static final PRELOGIN_OPTION TERMINATOR = new PRELOGIN_OPTION().setData(new byte[0]).setLength(0).setOffset(0).setOptionToken(PL_OPTION_TOKEN.TERMINATOR);
		
		private PL_OPTION_TOKEN option;
		private int offset, length;
		private byte[] data;
		
		public int read(DataInputStream dis) throws IOException
		{
			int opt = dis.readUnsignedByte();
			if(opt == 0xff)
			{
				option = PL_OPTION_TOKEN.TERMINATOR;
				return 1;
			}

			option = PL_OPTION_TOKEN.values()[opt];
			
			offset = dis.readUnsignedShort();
			length = dis.readUnsignedShort();
			
			return 5;
		}
		
		public void write(int offsetOffset, DataOutputStream dos) throws IOException
		{
			dos.writeByte(option.getValue());
			if(option == PL_OPTION_TOKEN.TERMINATOR)
				return;
			
			dos.writeShort(offset + offsetOffset);
			dos.writeShort(length);
		}
		
		public int getOptionLength()
		{
			return getOptionToken() == PL_OPTION_TOKEN.TERMINATOR ? 1 : 5;
		}
		
		public String toString() {
			return "PRELOGIN_OPTION [option=" + option + ", offset=" + offset
					+ ", length=" + length + "]";
		}

		public PL_OPTION_TOKEN getOptionToken()
		{
			return option;
		}
		
		public int getOffset()
		{
			return offset;
		}
		
		public int getLength()
		{
			return length;
		}
		
		public PRELOGIN_OPTION setOptionToken(PL_OPTION_TOKEN token)
		{
			this.option = token;
			
			return this;
		}
		
		public PRELOGIN_OPTION setOffset(int offset)
		{
			this.offset = offset;
			
			return this;
		}
		
		public PRELOGIN_OPTION setLength(int length)
		{
			this.length = length;
			
			return this;
		}
		
		public PRELOGIN_OPTION setData(byte[] b)
		{
			this.data = b;
			
			return this;
		}
		
		public byte[] getData()
		{
			return data;
		}
	}
	
	public static enum PL_OPTION_TOKEN
	{
		VERSION(0),
		ENCRYPTION(1),
		INSTOPT(2),
		THREADID(3),
		MARS(4),
		TRACEID(5),
		FEDAUTHREQUIRED(6),
		NONCEOPT(7),
		TERMINATOR(0xff),
		;
		
		private int value;
		
		private PL_OPTION_TOKEN(int val)
		{
			value = val;
		}
		
		public int getValue()
		{
			return value;
		}
	}
}
