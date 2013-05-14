package com.agateau.burgerparty.tools;

import com.agateau.burgerparty.view.ComposableCustomer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CustomerEditorGame extends Game {
	CustomerEditorGame(String partsXmlName) {
		mPartsXmlName = partsXmlName;
	}

	@Override
	public void create() {
		FileHandle handle = Gdx.files.absolute(mPartsXmlName);
		ComposableCustomer.initMap(handle);
		mAtlas = new TextureAtlas(Gdx.files.internal("burgerparty.atlas"));
		mSkin = new Skin(Gdx.files.internal("ui/skin.json"), mAtlas);

		showCustomerEditorScreen();
	}

	private void showCustomerEditorScreen() {
		setScreen(new CustomerEditorScreen(this, mAtlas, mSkin));
	}

	private Skin mSkin;
	private TextureAtlas mAtlas;
	private String mPartsXmlName;
}
