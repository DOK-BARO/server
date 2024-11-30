package kr.kro.dokbaro.server.core.auth.oauth2.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.common.type.AuthProvider

data class OAuth2Account(
	val id: Long = Constants.UNSAVED_ID,
	val socialId: String,
	val provider: AuthProvider,
	val memberId: Long,
)