package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountQueryRepository
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountRepository
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.ExistOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.InsertOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.LoadOAuth2CertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount

@PersistenceAdapter
class OAuth2AccountPersistenceAdapter(
	private val repository: OAuth2AccountRepository,
	private val queryRepository: OAuth2AccountQueryRepository,
) : ExistOAuth2AccountPort,
	InsertOAuth2AccountPort,
	LoadOAuth2CertificatedAccountPort {
	override fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean =
		queryRepository.existBy(
			socialId = socialId,
			provider = provider,
		)

	override fun insert(oAuth2Account: OAuth2Account): Long = repository.insert(oAuth2Account)

	override fun findBy(
		socialId: String,
		provider: AuthProvider,
	): OAuth2CertificatedAccount? =
		queryRepository.findBy(
			socialId = socialId,
			provider = provider,
		)
}