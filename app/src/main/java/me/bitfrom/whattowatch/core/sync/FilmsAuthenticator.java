package me.bitfrom.whattowatch.core.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class FilmsAuthenticator extends AbstractAccountAuthenticator {

    public FilmsAuthenticator(@NonNull Context context) {
        super(context);
    }

    // No properties to edit.
    @Override
    public Bundle editProperties(@NonNull AccountAuthenticatorResponse response, @NonNull String accountType) {
        return null;
    }

    // Because we're not actually adding an account to the device, just return null.
    @Override
    public Bundle addAccount(@NonNull AccountAuthenticatorResponse response,
                             @NonNull String accountType,
                             @NonNull String authTokenType,
                             @NonNull String[] requiredFeatures,
                             @NonNull Bundle options) throws NetworkErrorException {
        return null;
    }

    // Ignore attempts to confirm credentials
    @Override
    public Bundle confirmCredentials(@NonNull AccountAuthenticatorResponse response,
                                     @NonNull Account account,
                                     @NonNull Bundle options) throws NetworkErrorException {
        return null;
    }

    // Getting an authentication token is not supported
    @Override
    public Bundle getAuthToken(@NonNull AccountAuthenticatorResponse response,
                               @NonNull Account account,
                               @NonNull String authTokenType,
                               @NonNull Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    // Getting a label for the auth token is not supported
    @Override
    public String getAuthTokenLabel(@NonNull String authTokenType) {
        throw new UnsupportedOperationException();
    }

    // Updating user credentials is not supported
    @Override
    public Bundle updateCredentials(@NonNull AccountAuthenticatorResponse response,
                                    @NonNull Account account,
                                    @NonNull String authTokenType,
                                    @NonNull Bundle options) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    // Checking features for the account is not supported
    @Override
    public Bundle hasFeatures(@NonNull AccountAuthenticatorResponse response,
                              @NonNull Account account,
                              @NonNull String[] features) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}

