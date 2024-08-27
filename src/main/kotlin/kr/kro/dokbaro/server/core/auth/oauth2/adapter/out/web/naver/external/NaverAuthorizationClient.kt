package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external

import kr.kro.dokbaro.server.common.exception.FeignClientErrorDecoder
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
	name = "naverAuthorizationClient",
	url = "\${oauth2.naver.provider.authorization.url}",
	configuration = [FeignClientErrorDecoder::class],
)
fun interface NaverAuthorizationClient {
	@PostMapping(
		path = ["\${oauth2.naver.provider.authorization.token-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getAuthorizationToken(
		@RequestParam(name = "code") code: String,
		@RequestParam(name = "grant_type") grantType: String,
		@RequestParam(name = "client_id") clientId: String,
		@RequestParam(name = "redirect_uri") redirectUri: String,
		@RequestParam(name = "client_secret") clientSecret: String,
	): NaverAuthorizationTokenResponse
}