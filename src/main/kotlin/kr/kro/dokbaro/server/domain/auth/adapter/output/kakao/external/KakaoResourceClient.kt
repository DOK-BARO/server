package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external

import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.global.exception.FeignClientErrorDecoder
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "kakaoResourceClient",
	url = "\${oauth2.kakao.provider.resource.url}",
	configuration = [FeignClientErrorDecoder::class],
)
fun interface KakaoResourceClient {
	@GetMapping(
		path = ["\${oauth2.kakao.provider.resource.user-info-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): KakaoAccount
}