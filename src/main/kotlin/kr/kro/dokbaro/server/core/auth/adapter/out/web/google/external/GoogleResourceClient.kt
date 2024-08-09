package kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external

import kr.kro.dokbaro.server.common.exception.FeignClientErrorDecoder
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource.GoogleAccount
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "GoogleResourceClient",
	url = "\${oauth2.google.provider.resource-url}",
	configuration = [FeignClientErrorDecoder::class],
)
fun interface GoogleResourceClient {
	@GetMapping(
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): GoogleAccount
}