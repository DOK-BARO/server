package kr.kro.dokbaro.server.core.auth.adapter.out.web.naver

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.resource.NaverAccount
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
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