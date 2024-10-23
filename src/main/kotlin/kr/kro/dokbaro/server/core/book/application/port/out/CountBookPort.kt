package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition

fun interface CountBookPort {
	fun countBy(condition: ReadBookCollectionCondition): Long
}