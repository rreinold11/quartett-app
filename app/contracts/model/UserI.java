package contracts.model;

import java.util.UUID;

/**
 * Created by adanek on 14/01/15.
 */
public interface UserI {

    UUID getID();

    String getName();

    String getEmail();

    String getPassword();
}
