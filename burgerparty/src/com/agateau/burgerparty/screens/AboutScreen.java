package com.agateau.burgerparty.screens;

import com.agateau.burgerparty.BurgerPartyGame;
import com.agateau.burgerparty.utils.AnchorGroup;
import com.agateau.burgerparty.utils.FileUtils;
import com.agateau.burgerparty.utils.RefreshHelper;
import com.agateau.burgerparty.view.BurgerPartyUiBuilder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.greenyetilab.linguaj.Translator.tr;

public class AboutScreen extends BurgerPartyScreen {
    private static final String VERSION = "0.12";

    private static final float PIXEL_PER_SECOND = 48;

    private ScrollPane mScrollPane;

    public AboutScreen(BurgerPartyGame game) {
        super(game);
        Image bgImage = new Image(getTextureAtlas().findRegion("ui/menu-bg"));
        setBackgroundActor(bgImage);
        setupWidgets();
        new RefreshHelper(getStage()) {
            @Override
            protected void refresh() {
                getGame().showAboutScreen();
                dispose();
            }
        };
    }

    private void setupWidgets() {
        BurgerPartyUiBuilder builder = new BurgerPartyUiBuilder(getGame().getAssets());
        builder.build(FileUtils.assets("screens/about.gdxui"));
        AnchorGroup root = builder.getActor("root");
        getStage().addActor(root);
        root.setFillParent(true);

        builder.<ImageButton>getActor("backButton").addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent Event, Actor actor) {
                onBackPressed();
            }
        });

        builder.<Label>getActor("titleLabel").addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ++mClickCount;
                if (mClickCount >= 5) {
                    getGame().showCheatScreen();
                }
            }
            private int mClickCount = 0;
        });

        mScrollPane = builder.<ScrollPane>getActor("scrollPane");

        String aboutText =
                tr("Version %s", VERSION) + "\n\n"
                + tr("Code & Design") + "\n"
                + tr("Aurélien Gâteau") + "\n\n"
                + tr("Music") + "\n"
                + tr("Thomas Tripon") + "\n\n"
                + tr("Testers") + "\n"
                + tr("Clara Gâteau\n"
                        + "Antonin Gâteau\n"
                        + "Gwenaëlle Gâteau\n"
                        + "Mathieu Maret\n"
                        + "And many others!");

        Label label = builder.<Label>getActor("bodyLabel");
        label.setText(aboutText);
        label.pack();
    }

    @Override
    public void onBackPressed() {
        getGame().showStartScreen();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isTouched()) {
            return;
        }
        float maxY = mScrollPane.getWidget().getHeight();
        float y = mScrollPane.getScrollY();
        if (y < maxY) {
            mScrollPane.setScrollY(y + PIXEL_PER_SECOND * delta);
        }
    }
}
