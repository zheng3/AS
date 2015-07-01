package com.pla.chatsys.server;

/*@Optional(Feature.CHAT_HISTORY)*/
import java.io.BufferedReader;
/*@Optional(Feature.CHAT_HISTORY)*/
import java.io.File;
/*@Optional(Feature.CHAT_HISTORY)*/
import java.io.FileReader;
/*@Optional(Feature.CHAT_HISTORY)*/
import java.io.IOException;

import com.pla.chatsys.Optional;
import com.pla.chatsys.Feature;



public class ServerImp implements IServerImp
{
	private ServerArch _arch;

    public ServerImp (){
    }

	public void setArch(ServerArch arch){
		_arch = arch;
	}
	public ServerArch getArch(){
		return _arch;
	}

	/*
  	  Myx Lifecycle Methods: these methods are called automatically by the framework
  	  as the bricks are created, attached, detached, and destroyed respectively.
	*/	
	public void init(){
	    
	}
	
	public void begin(){
		
	}
	
	public void end(){
		
	}
	
	public void destroy(){
		
	}

	/*
  	  Implementation primitives required by the architecture
	*/
  

	public void sendMessage (String sender,String message) {
		if (_arch.OUT_IChatListener != null) {
			_arch.OUT_IChatListener.messageSent(sender, message);

			/*@Optional(Feature.CHAT_HISTORY)*/
			_arch.OUT_IFileWriter.writeMsg(sender,message);
		}
	}

	@Override
    @Optional(Feature.CHAT_HISTORY)
	public String retriveChatHistory(String sender) {
    	String sCurrentLine = null;
    	String Allfile = null;
    	BufferedReader br = null;
		try {
			File file = new File("./ChatHistory.txt");
			if(!file.exists()){
				file.createNewFile();
			}

			br = new BufferedReader(new FileReader("./ChatHistory.txt"));
			while((sCurrentLine = br.readLine()) != null)
			{
				if (Allfile == null)
				{
					Allfile = sCurrentLine;
				}
				else
				{
					Allfile = Allfile +"\n" +sCurrentLine;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return Allfile;
    }

	@Override
    @Optional(Feature.FILE_SHARING)
	public void sendFile(String sender, String path, byte[] fileData) {
		if(_arch.OUT_IFileSentListener!=null){
			_arch.OUT_IFileSentListener.fileSent(sender, path, fileData);
		}
	}

	@Override
    @Optional(Feature.IMAGE_SHARING)
	public void sendImage(String sender, String name, byte[] data) {
		if(_arch.OUT_IImageSent !=null){
			_arch.OUT_IImageSent.imageSent(sender, name, data);
		}
	}

	@Override
    @Optional(Feature.GAME)
	public void startGame() {
		if (_arch.OUT_IGameListener != null){
			_arch.OUT_IGameListener.gameStarted();
		}
	}

	@Override
    @Optional(Feature.GAME)
	public void endGame() {
		if (_arch.OUT_IGameListener != null){
			_arch.OUT_IGameListener.gameEnded();
		}
	}

	@Override
    @Optional(Feature.GAME)
	public void play(String sender, int position) {
		if (_arch.OUT_IGameListener != null){
			_arch.OUT_IGameListener.played(sender, position);
		}
	}

	@Override
    @Optional(Feature.TEMPLATE)
	public void sendTemplet(String sender, String msgCode) {
		_arch.OUT_ITempletSend.sendTemplet(sender, msgCode);
	}

	@Override
	@Optional(Feature.TOOL_BAR)
	public void sendIncognitoMessage(String sender, String msg) {
		if (_arch.OUT_IChatListener != null){
			_arch.OUT_IChatListener.messageSent(sender, msg);
		}
	}

	@Override
	@Optional(Feature.TOOL_BAR)
	public void sendColor(String sender, int color) {
		if (_arch.OUT_IToolBarEvent != null){
			_arch.OUT_IToolBarEvent.onColorReceived(color);
		}
	}

	@Override
	@Optional(Feature.TOOL_BAR)
	public void sendLocation(double lat, double lon) {
		if (_arch.OUT_IToolBarEvent != null){
			_arch.OUT_IToolBarEvent.onLocationReceived(lat, lon);
		}
	}
}