package net.nevercast.minecraft.bot.test;

import net.nevercast.minecraft.bot.GamePulser;
import net.nevercast.minecraft.bot.GamePulser.IGamePulserReceptor;

public class Test implements Runnable, IGamePulserReceptor {
	public static void main(String[] args) {
		new Thread(new Test()).start();
	}

	@Override
	public void tick(long elapsedTime) throws Exception {
		System.out.println("Tick: " + elapsedTime + "ms");
	}

	@Override
	public void run() {
		new GamePulser(this, 50).start();
		while(true) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
		}
	}
}
