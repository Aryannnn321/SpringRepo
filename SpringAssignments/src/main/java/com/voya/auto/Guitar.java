package com.voya.auto;

import org.springframework.stereotype.Component;
@Component
public class Guitar implements IInstrument {

	public void play(String song) {
		System.out.println(song+"playing song using guitar");
		
	}

}
