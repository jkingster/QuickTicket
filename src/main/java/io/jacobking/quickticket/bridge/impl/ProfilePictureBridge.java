package io.jacobking.quickticket.bridge.impl;

import io.jacobking.quickticket.bridge.Bridge;
import io.jacobking.quickticket.core.database.repository.RepoType;
import io.jacobking.quickticket.gui.model.impl.ProfilePictureModel;
import io.jacobking.quickticket.tables.pojos.ProfilePicture;

public class ProfilePictureBridge extends Bridge<ProfilePicture, ProfilePictureModel> {
    public ProfilePictureBridge() {
        super(RepoType.PROFILE);
    }

    @Override public ProfilePictureModel convertEntity(ProfilePicture entity) {
        return new ProfilePictureModel(entity);
    }
}
