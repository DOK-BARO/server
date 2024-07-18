package kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external

import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.resource.NaverAccount
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "naverResourceClient",
	url = "\${oauth2.naver.provider.resource.url}",
)
fun interface NaverResourceClient {
	@GetMapping(
		path = ["\${oauth2.naver.provider.resource.user-info-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): NaverAccount
}