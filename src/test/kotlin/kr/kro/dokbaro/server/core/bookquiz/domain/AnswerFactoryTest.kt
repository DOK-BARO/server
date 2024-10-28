package kr.kro.dokbaro.server.core.bookquiz.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe

class AnswerFactoryTest :
	StringSpec({

		"유형별 문제를 생성한다" {
			GradeSheetFactory.create(QuizType.FILL_BLANK, AnswerSheet(listOf("A", "B"))) shouldNotBe null
			GradeSheetFactory.create(QuizType.MULTIPLE_CHOICE, AnswerSheet(listOf("1", "5"))) shouldNotBe null
			GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))) shouldNotBe null
			GradeSheetFactory.create(QuizType.SHORT, AnswerSheet(listOf("A", "B"))) shouldNotBe null
		}
	})