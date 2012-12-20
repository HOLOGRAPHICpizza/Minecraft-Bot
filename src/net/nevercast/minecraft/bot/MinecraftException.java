package net.nevercast.minecraft.bot;

public class MinecraftException extends RuntimeException {

	private static final long serialVersionUID = -6267901291640848194L;
	
	public MinecraftException(String message) {
		super(message);
	}
	
	public MinecraftException(String message, Throwable e) {
		super(message, e);
	}
	
	public MinecraftException(Throwable e) {
		super(e);
	}
}
