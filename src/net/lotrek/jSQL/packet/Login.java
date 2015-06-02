package net.lotrek.jSQL.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.lotrek.jSQL.SQLTools;

public class Login implements SQLPacket
{
	private int length, tdsVersion, packetSize, clientProgVer, clientPID, connectionID, fFloat, fUserType, clientTimeZone, clientLCID;
	private boolean fByteOrder, fChar, fDumpLoad, fUseDB, fDatabase, fSetLang, fLanguage, fODBC, fTransBoundary, fCacheConnect, fIntSecurity, fSQLType, fOLEDB, fChangePassword, fSendYukonBinaryXML, fUserInstance, fUnknownCollationHandling, fExtension, fReadOnlyIntent; 
	private OffsetLength offset;
	private byte[] extraData;
	
	public void read(PacketHeader head, DataInputStream dis) throws IOException
	{
		int lenRemain = head.getTotLength() - 102;
		
		SQLTools.fillFieldSeriesWithValues(this,
				new String[]{"length", "tdsVersion", "packetSize", "clientProgVer", "clientPID", "connectionID"},
				new Object[]{Integer.reverseBytes(dis.readInt()), dis.readInt(), Integer.reverseBytes(dis.readInt()), dis.readInt(), Integer.reverseBytes(dis.readInt()), Integer.reverseBytes(dis.readInt())});
		
		int optionFlags1 = dis.readUnsignedByte();
		fByteOrder = (optionFlags1 & 1) != 0;
		fChar = (optionFlags1 & 2) != 0;
		fFloat = (optionFlags1 >> 2) & 3;
		fDumpLoad = (optionFlags1 & 16) != 0;
		fUseDB = (optionFlags1 & 32) != 0;
		fDatabase = (optionFlags1 & 64) != 0;
		fSetLang = (optionFlags1 & 128) != 0;
		int optionFlags2 = dis.readUnsignedByte();
		fLanguage = (optionFlags2 & 1) != 0;
		fODBC = (optionFlags2 & 2) != 0;
		fTransBoundary = (optionFlags2 & 4) != 0;
		fCacheConnect = (optionFlags2 & 8) != 0;
		fUserType = (optionFlags2 >> 4) & 3;
		fIntSecurity = (optionFlags2 & 64) != 0;
		int typeFlags = dis.readUnsignedByte();
		fSQLType = (typeFlags & 1) != 0;
		fOLEDB = (typeFlags & 2) != 0;
		fReadOnlyIntent = (typeFlags & 4) != 0; 
		int optionFlags3 = dis.readUnsignedByte();
		fChangePassword = (optionFlags3 & 1) != 0;
		fSendYukonBinaryXML = (optionFlags3 & 2) != 0;
		fUserInstance = (optionFlags3 & 4) != 0;
		fUnknownCollationHandling = (optionFlags3 & 8) != 0;
		fExtension = (optionFlags3 & 16) != 0;
		
		clientTimeZone = dis.readInt();
		clientLCID = dis.readInt();
		
		offset = new OffsetLength();
		offset.read(dis);
		
		extraData = new byte[lenRemain];
		dis.readFully(extraData);
	}

	@Override
	public String toString() {
		return "Login [length=" + length + ", tdsVersion=0x" + Integer.toHexString(tdsVersion)
				+ ", packetSize=" + packetSize + ", clientProgVer="
				+ clientProgVer + ", clientPID=" + clientPID
				+ ", connectionID=" + connectionID + ", fFloat=" + fFloat
				+ ", fUserType=" + fUserType + ", clientTimeZone="
				+ clientTimeZone + ", clientLCID=" + clientLCID
				+ ", fByteOrder=" + fByteOrder + ", fChar=" + fChar
				+ ", fDumpLoad=" + fDumpLoad + ", fUseDB=" + fUseDB
				+ ", fDatabase=" + fDatabase + ", fSetLang=" + fSetLang
				+ ", fLanguage=" + fLanguage + ", fODBC=" + fODBC
				+ ", fTransBoundary=" + fTransBoundary + ", fCacheConnect="
				+ fCacheConnect + ", fIntSecurity=" + fIntSecurity
				+ ", fSQLType=" + fSQLType + ", fOLEDB=" + fOLEDB
				+ ", fChangePassword=" + fChangePassword
				+ ", fSendYukonBinaryXML=" + fSendYukonBinaryXML
				+ ", fUserInstance=" + fUserInstance
				+ ", fUnknownCollationHandling=" + fUnknownCollationHandling
				+ ", fExtension=" + fExtension + ", fReadOnlyIntent="
				+ fReadOnlyIntent + ", offset=" + offset + "]";
	}



	public void write(DataOutputStream dos) throws IOException
	{
		
	}

	public int getPayloadLength()
	{
		return 0;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getTdsVersion() {
		return tdsVersion;
	}

	public void setTdsVersion(int tdsVersion) {
		this.tdsVersion = tdsVersion;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}

	public int getClientProgVer() {
		return clientProgVer;
	}

	public void setClientProgVer(int clientProgVer) {
		this.clientProgVer = clientProgVer;
	}

	public int getClientPID() {
		return clientPID;
	}

	public void setClientPID(int clientPID) {
		this.clientPID = clientPID;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public int getfFloat() {
		return fFloat;
	}

	public void setfFloat(int fFloat) {
		this.fFloat = fFloat;
	}

	public int getfUserType() {
		return fUserType;
	}

	public void setfUserType(int fUserType) {
		this.fUserType = fUserType;
	}

	public int getClientTimeZone() {
		return clientTimeZone;
	}

	public void setClientTimeZone(int clientTimeZone) {
		this.clientTimeZone = clientTimeZone;
	}

	public int getClientLCID() {
		return clientLCID;
	}

	public void setClientLCID(int clientLCID) {
		this.clientLCID = clientLCID;
	}

	public boolean isfByteOrder() {
		return fByteOrder;
	}

	public void setfByteOrder(boolean fByteOrder) {
		this.fByteOrder = fByteOrder;
	}

	public boolean isfChar() {
		return fChar;
	}

	public void setfChar(boolean fChar) {
		this.fChar = fChar;
	}

	public boolean isfDumpLoad() {
		return fDumpLoad;
	}

	public void setfDumpLoad(boolean fDumpLoad) {
		this.fDumpLoad = fDumpLoad;
	}

	public boolean isfUseDB() {
		return fUseDB;
	}

	public void setfUseDB(boolean fUseDB) {
		this.fUseDB = fUseDB;
	}

	public boolean isfDatabase() {
		return fDatabase;
	}

	public void setfDatabase(boolean fDatabase) {
		this.fDatabase = fDatabase;
	}

	public boolean isfSetLang() {
		return fSetLang;
	}

	public void setfSetLang(boolean fSetLang) {
		this.fSetLang = fSetLang;
	}

	public boolean isfLanguage() {
		return fLanguage;
	}

	public void setfLanguage(boolean fLanguage) {
		this.fLanguage = fLanguage;
	}

	public boolean isfODBC() {
		return fODBC;
	}

	public void setfODBC(boolean fODBC) {
		this.fODBC = fODBC;
	}

	public boolean isfTransBoundary() {
		return fTransBoundary;
	}

	public void setfTransBoundary(boolean fTransBoundary) {
		this.fTransBoundary = fTransBoundary;
	}

	public boolean isfCacheConnect() {
		return fCacheConnect;
	}

	public void setfCacheConnect(boolean fCacheConnect) {
		this.fCacheConnect = fCacheConnect;
	}

	public boolean isfIntSecurity() {
		return fIntSecurity;
	}

	public void setfIntSecurity(boolean fIntSecurity) {
		this.fIntSecurity = fIntSecurity;
	}

	public boolean isfSQLType() {
		return fSQLType;
	}

	public void setfSQLType(boolean fSQLType) {
		this.fSQLType = fSQLType;
	}

	public boolean isfOLEDB() {
		return fOLEDB;
	}

	public void setfOLEDB(boolean fOLEDB) {
		this.fOLEDB = fOLEDB;
	}

	public boolean isfChangePassword() {
		return fChangePassword;
	}

	public void setfChangePassword(boolean fChangePassword) {
		this.fChangePassword = fChangePassword;
	}

	public boolean isfSendYukonBinaryXML() {
		return fSendYukonBinaryXML;
	}

	public void setfSendYukonBinaryXML(boolean fSendYukonBinaryXML) {
		this.fSendYukonBinaryXML = fSendYukonBinaryXML;
	}

	public boolean isfUserInstance() {
		return fUserInstance;
	}

	public void setfUserInstance(boolean fUserInstance) {
		this.fUserInstance = fUserInstance;
	}

	public boolean isfUnknownCollationHandling() {
		return fUnknownCollationHandling;
	}

	public void setfUnknownCollationHandling(boolean fUnknownCollationHandling) {
		this.fUnknownCollationHandling = fUnknownCollationHandling;
	}

	public boolean isfExtension() {
		return fExtension;
	}

	public void setfExtension(boolean fExtension) {
		this.fExtension = fExtension;
	}

	public boolean isfReadOnlyIntent() {
		return fReadOnlyIntent;
	}

	public void setfReadOnlyIntent(boolean fReadOnlyIntent) {
		this.fReadOnlyIntent = fReadOnlyIntent;
	}

	public OffsetLength getOffset() {
		return offset;
	}

	public void setOffset(OffsetLength offset) {
		this.offset = offset;
	}

	public byte[] getExtraData() {
		return extraData;
	}

	public void setExtraData(byte[] extraData) {
		this.extraData = extraData;
	}

	public byte[] getDataKey(int offset, int len)
	{
		byte[] toReturn = new byte[len];
		System.arraycopy(getExtraData(), offset - getOffset().IbHostname, toReturn, 0, len);
		return toReturn;
	}
	
	public static class OffsetLength
	{
		private int IbHostname, cchHostName, IbUserName, cchUserName, IbPassword, cchPassword, IbAppName, cchAppName, IbServerName, cchServerName, ibUnused, cbUnused, ibExtension, cbExtension, ibCltIntName, cchCltIntName, ibLanguage, cchLanguage, ibDatabase, cchDatabase, ibSSPI, cbSSPI, ibAtchDBFile, cchAtchDBFile, ibChangePassword, cchChangePassword, cbSSPILong;
		private byte[] ClientID = new byte[6];
		
		public void read(DataInputStream dis) throws IOException
		{
			
			try {
				SQLTools.fillFieldSeriesWithValues(this,
						new String[]{"IbHostname", "cchHostName", "IbUserName", "cchUserName", "IbPassword", "cchPassword", "IbAppName", "cchAppName", "IbServerName", "cchServerName", "ibExtension", "cbExtension", "ibCltIntName", "cchCltIntName", "ibLanguage", "cchLanguage", "ibDatabase"},
						SQLTools.callMethodForArray(SQLTools.generateArrayWithMethodCall(18, dis, DataInputStream.class.getDeclaredMethod("readShort")), null, Short.class.getDeclaredMethod("reverseBytes", short.class)));
				dis.readFully(ClientID);
				SQLTools.fillFieldSeriesWithValues(this,
						new String[]{"ibSSPI", "cbSSPI", "ibAtchDBFile", "cchAtchDBFile", "ibChangePassword", "cchChangePassword"},
						SQLTools.callMethodForArray(SQLTools.generateArrayWithMethodCall(6, dis, DataInputStream.class.getDeclaredMethod("readShort")), null, Short.class.getDeclaredMethod("reverseBytes", short.class)));
			
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
			cbSSPILong = dis.readInt();
		}

		@Override
		public String toString() {
			return "OffsetLength [IbHostname=" + IbHostname + ", cchHostName="
					+ cchHostName + ", IbUserName=" + IbUserName
					+ ", cchUserName=" + cchUserName + ", IbPassword="
					+ IbPassword + ", cchPassword=" + cchPassword
					+ ", IbAppName=" + IbAppName + ", cchAppName=" + cchAppName
					+ ", IbServerName=" + IbServerName + ", cchServerName="
					+ cchServerName + ", ibUnused=" + ibUnused + ", cbUnused="
					+ cbUnused + ", ibExtension=" + ibExtension
					+ ", cbExtension=" + cbExtension + ", ibCltIntName="
					+ ibCltIntName + ", cchCltIntName=" + cchCltIntName
					+ ", ibLanguage=" + ibLanguage + ", cchLanguage="
					+ cchLanguage + ", ibDatabase=" + ibDatabase
					+ ", cchDatabase=" + cchDatabase + ", ClientID=" + Arrays.toString(ClientID)
					+ ", ibSSPI=" + ibSSPI + ", cbSSPI=" + cbSSPI
					+ ", ibAtchDBFile=" + ibAtchDBFile + ", cchAtchDBFile="
					+ cchAtchDBFile + ", ibChangePassword=" + ibChangePassword
					+ ", cchChangePassword=" + cchChangePassword
					+ ", cbSSPILong=" + cbSSPILong + "]";
		}

		public int getIbHostname() {
			return IbHostname;
		}

		public void setIbHostname(int ibHostname) {
			IbHostname = ibHostname;
		}

		public int getCchHostName() {
			return cchHostName;
		}

		public void setCchHostName(int cchHostName) {
			this.cchHostName = cchHostName;
		}

		public int getIbUserName() {
			return IbUserName;
		}

		public void setIbUserName(int ibUserName) {
			IbUserName = ibUserName;
		}

		public int getCchUserName() {
			return cchUserName;
		}

		public void setCchUserName(int cchUserName) {
			this.cchUserName = cchUserName;
		}

		public int getIbPassword() {
			return IbPassword;
		}

		public void setIbPassword(int ibPassword) {
			IbPassword = ibPassword;
		}

		public int getCchPassword() {
			return cchPassword;
		}

		public void setCchPassword(int cchPassword) {
			this.cchPassword = cchPassword;
		}

		public int getIbAppName() {
			return IbAppName;
		}

		public void setIbAppName(int ibAppName) {
			IbAppName = ibAppName;
		}

		public int getCchAppName() {
			return cchAppName;
		}

		public void setCchAppName(int cchAppName) {
			this.cchAppName = cchAppName;
		}

		public int getIbServerName() {
			return IbServerName;
		}

		public void setIbServerName(int ibServerName) {
			IbServerName = ibServerName;
		}

		public int getCchServerName() {
			return cchServerName;
		}

		public void setCchServerName(int cchServerName) {
			this.cchServerName = cchServerName;
		}

		public int getIbUnused() {
			return ibUnused;
		}

		public void setIbUnused(int ibUnused) {
			this.ibUnused = ibUnused;
		}

		public int getCbUnused() {
			return cbUnused;
		}

		public void setCbUnused(int cbUnused) {
			this.cbUnused = cbUnused;
		}

		public int getIbExtension() {
			return ibExtension;
		}

		public void setIbExtension(int ibExtension) {
			this.ibExtension = ibExtension;
		}

		public int getCbExtension() {
			return cbExtension;
		}

		public void setCbExtension(int cbExtension) {
			this.cbExtension = cbExtension;
		}

		public int getIbCltIntName() {
			return ibCltIntName;
		}

		public void setIbCltIntName(int ibCltIntName) {
			this.ibCltIntName = ibCltIntName;
		}

		public int getCchCltIntName() {
			return cchCltIntName;
		}

		public void setCchCltIntName(int cchCltIntName) {
			this.cchCltIntName = cchCltIntName;
		}

		public int getIbLanguage() {
			return ibLanguage;
		}

		public void setIbLanguage(int ibLanguage) {
			this.ibLanguage = ibLanguage;
		}

		public int getCchLanguage() {
			return cchLanguage;
		}

		public void setCchLanguage(int cchLanguage) {
			this.cchLanguage = cchLanguage;
		}

		public int getIbDatabase() {
			return ibDatabase;
		}

		public void setIbDatabase(int ibDatabase) {
			this.ibDatabase = ibDatabase;
		}

		public int getCchDatabase() {
			return cchDatabase;
		}

		public void setCchDatabase(int cchDatabase) {
			this.cchDatabase = cchDatabase;
		}

		public int getIbSSPI() {
			return ibSSPI;
		}

		public void setIbSSPI(int ibSSPI) {
			this.ibSSPI = ibSSPI;
		}

		public int getCbSSPI() {
			return cbSSPI;
		}

		public void setCbSSPI(int cbSSPI) {
			this.cbSSPI = cbSSPI;
		}

		public int getIbAtchDBFile() {
			return ibAtchDBFile;
		}

		public void setIbAtchDBFile(int ibAtchDBFile) {
			this.ibAtchDBFile = ibAtchDBFile;
		}

		public int getCchAtchDBFile() {
			return cchAtchDBFile;
		}

		public void setCchAtchDBFile(int cchAtchDBFile) {
			this.cchAtchDBFile = cchAtchDBFile;
		}

		public int getIbChangePassword() {
			return ibChangePassword;
		}

		public void setIbChangePassword(int ibChangePassword) {
			this.ibChangePassword = ibChangePassword;
		}

		public int getCchChangePassword() {
			return cchChangePassword;
		}

		public void setCchChangePassword(int cchChangePassword) {
			this.cchChangePassword = cchChangePassword;
		}

		public int getCbSSPILong() {
			return cbSSPILong;
		}

		public void setCbSSPILong(int cbSSPILong) {
			this.cbSSPILong = cbSSPILong;
		}

		public byte[] getClientID() {
			return ClientID;
		}

		public void setClientID(byte[] clientID) {
			ClientID = clientID;
		}
		
		
	}
	
}
