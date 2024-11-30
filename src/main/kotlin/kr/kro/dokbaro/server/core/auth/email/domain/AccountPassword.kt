package kr.kro.dokbaro.server.core.auth.email.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class AccountPassword(
	val id: Long = Constants.UNSAVED_ID,
	val password: String,
	val memberId: Long,
)