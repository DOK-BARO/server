package kr.kro.dokbaro.server.core.account.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class SocialAccount(
	val id: Long = Constants.UNSAVED_ID,
	val socialId: String,
	val provider: AuthProvider,
	val memberId: Long,
)