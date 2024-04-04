package io.jacobking.quickticket.core.database.repository.impl;

import io.jacobking.quickticket.core.database.repository.Repository;
import io.jacobking.quickticket.tables.pojos.ProfilePicture;
import io.jacobking.quickticket.tables.records.ProfilePictureRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.List;

import static io.jacobking.quickticket.Tables.PROFILE_PICTURE;

public class ProfilePictureRepository implements Repository<ProfilePicture> {
    @Override public ProfilePicture getById(DSLContext context, int id) {
        return context.selectFrom(PROFILE_PICTURE)
                .where(PROFILE_PICTURE.ID.eq(id))
                .fetchOneInto(ProfilePicture.class);
    }

    @Override public ProfilePicture save(DSLContext context, ProfilePicture profilePicture) {
        return context.insertInto(PROFILE_PICTURE)
                .set(new ProfilePictureRecord(profilePicture))
                .onConflictDoNothing()
                .returning()
                .fetchOneInto(ProfilePicture.class);
    }

    @Override public List<ProfilePicture> getAll(DSLContext context) {
        return context.selectFrom(PROFILE_PICTURE)
                .fetchInto(ProfilePicture.class);
    }

    @Override public boolean deleteWhere(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public List<ProfilePicture> getAll(DSLContext context, Condition condition) {
        throw new UnsupportedOperationException();
    }

    @Override public boolean delete(DSLContext context, int id) {
        return context.deleteFrom(PROFILE_PICTURE)
                .where(PROFILE_PICTURE.ID.eq(id))
                .execute() == SUCCESS;
    }

    @Override public boolean update(DSLContext context, ProfilePicture profilePicture) {
        final ProfilePictureRecord record = new ProfilePictureRecord(profilePicture);
        record.changed(PROFILE_PICTURE.ID, false);
        return context.update(PROFILE_PICTURE)
                .set(record)
                .where(PROFILE_PICTURE.ID.eq(profilePicture.getId()))
                .execute() >= SUCCESS;
    }
}
