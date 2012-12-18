package net.nevercast.minecraft.bot.web;

public class MinecraftLoginException extends Exception {

	private static final long serialVersionUID = -4332193055666488867L;
	
	public MinecraftLoginException(String message) {
		super(message);
	}
	
	public MinecraftLoginException(String message, Throwable e) {
		super(message, e);
	}
	
	public MinecraftLoginException(Throwable e) {
		super(e);
	}
	
}
