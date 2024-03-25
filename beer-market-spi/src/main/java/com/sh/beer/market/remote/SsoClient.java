package com.sh.beer.market.remote;

import org.springframework.cloud.netflix.feign.FeignClient;

//@FeignClient(url = "${sugar.third-party.sso.server}", name = "sso")
public interface SsoClient {

   /* @RequestMapping(method = RequestMethod.GET, value = "oauth2/profile")
    UserSsoProfile getProfile(@RequestParam("access_token") String accessToken);*/
}
