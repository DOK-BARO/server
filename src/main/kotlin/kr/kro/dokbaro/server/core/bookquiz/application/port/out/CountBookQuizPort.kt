package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition

fun interface CountBookQuizPort {
	fun countBookQuizBy(condition: CountBookQuizCondition): Long
}