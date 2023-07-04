package com.nio.ngfs.plm.bom.configuration.remote;

import com.nio.bom.share.model.UserSsoProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${url.sso}", name = "sso")
public interface SsoClient {

    @RequestMapping(method = RequestMethod.GET, value = "oauth2/profile")
    UserSsoProfile getProfile(@RequestParam("access_token") String accessToken);
}
