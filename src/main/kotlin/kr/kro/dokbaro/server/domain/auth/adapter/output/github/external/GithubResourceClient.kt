package kr.kro.dokbaro.server.domain.auth.adapter.output.github.external

import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.provideraccount.GithubAccount
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
	name = "githubResourceClient",
	url = "\${oauth2.github.provider.resource.url}",
)
fun interface GithubResourceClient {
	@GetMapping(
		path = ["\${oauth2.github.provider.resource.user-info-path}"],
		consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE],
	)
	fun getUseProfiles(
		@RequestHeader(name = HttpHeaders.AUTHORIZATION) token: String,
	): GithubAccount
}