package at.sum.android.cysmn.sensing.googleplay;

import com.google.android.gms.common.api.Api;

/**
 * Created by widnig89 on 01.05.15.
 */
public interface IGoogleApiClientService {

    public Api<Api.ApiOptions.NoOptions> getAPI();
}
