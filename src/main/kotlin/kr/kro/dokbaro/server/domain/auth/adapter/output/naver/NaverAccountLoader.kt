package kr.kro.dokbaro.server.domain.auth.adapter.output.naver

import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.resource.NaverAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Component

@Component
class NaverAccountLoader(
	private val resourceClient: NaverResourceClient,
) : LoadProviderAccountPort {
	override fun getAttributes(accessToken: String): ProviderAccount {
		val account: NaverAccount = resourceClient.getUserProfiles(accessToken)

		return ProviderAccount(
			AuthProvider.NAVER,
			account.response.id,
			account.response.name,
			account.response.email,
			account.response.profileImage,
		)
	}
}