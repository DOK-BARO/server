package kr.kro.dokbaro.server.domain.auth.adapter.output.google.external

import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.provideraccount.GoogleAccount
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "GoogleResourceClient",
	url = "\${oauth2.google.provider.resource-url}",
)
fun interface GoogleResourceClient {
	@GetMapping(
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): GoogleAccount
}