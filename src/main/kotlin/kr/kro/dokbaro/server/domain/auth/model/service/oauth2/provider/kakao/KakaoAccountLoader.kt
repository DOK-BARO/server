package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.provider.kakao

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.LoadProviderAccountStrategy
import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort

class KakaoAccountLoader(
	private val resourceTokenPort: LoadProviderResourceTokenPort,
	private val accountPort: LoadProviderAccountPort,
) : LoadProviderAccountStrategy {
	override fun load(authorizationToken: String): ProviderAccount {
		val resourceToken: String = resourceTokenPort.getToken(authorizationToken)

		return accountPort.getAttributes(resourceToken)
	}
}