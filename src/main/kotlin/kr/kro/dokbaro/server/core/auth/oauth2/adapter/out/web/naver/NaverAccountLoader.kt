package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.EmailNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.NickNameNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external.NaverResourceClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external.resource.NaverAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import org.springframework.stereotype.Component

@Component
class NaverAccountLoader(
	private val resourceClient: NaverResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): OAuth2ProviderAccount {
		val account: NaverAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			AuthProvider.NAVER,
			account.response.id,
			account.response.name
				?: throw NickNameNotExistException(),
			account.response.email ?: throw EmailNotExistException(),
			account.response.profileImage,
		)
	}
}