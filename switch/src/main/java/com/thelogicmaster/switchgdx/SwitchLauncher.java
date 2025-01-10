package com.thelogicmaster.switchgdx;

import com.agateau.burgerparty.BurgerPartyGame;
import com.badlogic.gdx.Application;

public class SwitchLauncher {

	public static void main (String[] arg) {
		new SwitchApplication(new BurgerPartyGame(), new SwitchApplication.Config(true, Application.ApplicationType.Android));
	}
}
