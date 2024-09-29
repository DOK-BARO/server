package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import java.util.UUID

fun authTokenFixture(
	accessToken: String =
		"ewogICJ0eXAiOiJKV1QiLAogICJhbGciOiJIUzI1NiIKfQ." +
			"ewogICJqdGkiOiI5NjQ5MmQ1OS0wYWQ1LTRjMDAtODkyZC01." +
			"ZXdvZ0lDSjBlWEFpT2lKS1YxUWlMQW9nSUNKaGJHY2lPaU",
	refreshToken: String = "6d096f99-6e08-46ea-896f-996e08a6ea2e",
): AuthToken =
	AuthToken(
		accessToken,
		refreshToken,
	)

fun emailCertificatedAccountFixture(
	password: String = "password",
	certificationId: UUID = UUID.randomUUID(),
	role: Set<String> = setOf("GUEST"),
): EmailCertificatedAccount =
	EmailCertificatedAccount(
		password,
		certificationId,
		role,
	)

fun oAuth2ProviderAccountFixture(
	provider: AuthProvider = AuthProvider.KAKAO,
	id: String = "accountID",
	name: String = "username",
	email: String = "oauth@kakao.com",
	profileImage: String? = "profileImage.jpeg",
): OAuth2ProviderAccount =
	OAuth2ProviderAccount(
		provider,
		id,
		name,
		email,
		profileImage,
	)