package kr.kro.dokbaro.server.core.auth.email.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class AccountPassword(
	val password: String,
	val memberId: Long,
	val id: Long = Constants.UNSAVED_ID,
)