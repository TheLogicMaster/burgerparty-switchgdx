package com.agateau.burgerparty.view;

import com.agateau.burgerparty.Assets;
import com.agateau.burgerparty.model.Achievement;
import com.agateau.burgerparty.utils.Anchor;
import com.agateau.burgerparty.utils.AnchorGroup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AchievementView extends AnchorGroup {
    private static final float WIDTH = 600;
    private static final float PADDING = 10;

    public AchievementView(Assets assets, Achievement achievement) {
        TextureAtlas atlas = assets.getTextureAtlas();
        Image bg = new Image(atlas.createPatch("ui/achievement-bg"));
        bg.setFillParent(true);
        addActor(bg);

        TextureRegion iconRegion = atlas.findRegion("achievements/" + achievement.getIconName());
        if (iconRegion == null) {
            iconRegion = atlas.findRegion("achievements/generic");
        }

        Image icon = new Image(iconRegion);
        Label titleLabel = new Label(achievement.getTitle(), assets.getSkin(), "achievement-title");
        Label descriptionLabel = new Label(achievement.getDescription(), assets.getSkin(), "achievement-description");
//        descriptionLabel.setWrap(true);

        Actor statusIcon = null;
        if (achievement.isUnlocked()) {
            if (achievement.hasBeenSeen()) {
                TextureRegion statusRegion = atlas.findRegion("ui/achievement-unlocked");
                statusIcon = new Image(statusRegion);
            } else {
                statusIcon = new AchievementsButtonIndicator(assets);
            }
        } else {
            icon.setColor(1, 1, 1, 0.3f);
            titleLabel.setColor(1, 1, 1, 0.8f);
            descriptionLabel.setColor(1, 1, 1, 0.8f);
        }

        titleLabel.pack();
        descriptionLabel.pack();

        setWidth(WIDTH);
        addRule(icon, Anchor.TOP_LEFT, this, Anchor.TOP_LEFT, PADDING, -PADDING);
        addRule(titleLabel, Anchor.TOP_LEFT, icon, Anchor.TOP_RIGHT, PADDING, 0);
        addRule(descriptionLabel, Anchor.TOP_LEFT, titleLabel, Anchor.BOTTOM_LEFT, 0, 6f);
        if (statusIcon != null) {
            addRule(statusIcon, Anchor.TOP_RIGHT, this, Anchor.TOP_RIGHT, -PADDING, -PADDING);
        }

        // Call layout() now so that we can compute the height
        layout();
        float height = titleLabel.getTop() - descriptionLabel.getY() + 1;

        setHeight(height + 2 * PADDING);
    }

}
