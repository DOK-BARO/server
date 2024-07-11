package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "kakaoResourceClient",
	url = "\${oauth2.kakao.provider.resource.url}",
)
interface KakaoResourceClient {
	@GetMapping(
		path = ["\${oauth2.kakao.provider.resource.user-info-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = "Authorization") token: String,
	): UserInfoResponse
}