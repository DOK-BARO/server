package kr.kro.dokbaro.server.core.bookquiz.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.EmptyAnswerSheetException

class AnswerSheetTest :
	StringSpec({

		"빈 값이 들어오면 예외를 반환한다" {
			shouldThrow<EmptyAnswerSheetException> {
				AnswerSheet(emptyList())
			}
		}
	})