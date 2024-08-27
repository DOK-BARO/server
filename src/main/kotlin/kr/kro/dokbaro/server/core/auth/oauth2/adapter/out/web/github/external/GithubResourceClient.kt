package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external

import kr.kro.dokbaro.server.common.exception.FeignClientErrorDecoder
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.resource.GithubAccount
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "githubResourceClient",
	url = "\${oauth2.github.provider.resource.url}",
	configuration = [FeignClientErrorDecoder::class],
)
fun interface GithubResourceClient {
	@GetMapping(
		path = ["\${oauth2.github.provider.resource.user-info-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUserProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): GithubAccount
}