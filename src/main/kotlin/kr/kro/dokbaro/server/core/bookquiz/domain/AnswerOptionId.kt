package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class AnswerOptionId(
	val value: Long = Constants.UNSAVED_ID,
)