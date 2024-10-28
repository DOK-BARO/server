package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

class FillBlankAnswerTest :
	StringSpec({

		"답변의 타입을 확인한다" {
			FillBlankAnswer.from(AnswerSheet(listOf("답변"))).getType() shouldBe QuizType.FILL_BLANK
		}

		"답변 일치 여부를 확인한다" {
			val correct = listOf("답변", "체크")
			val answer = FillBlankAnswer.from(AnswerSheet(correct))

			answer.isCorrect(AnswerSheet(correct)) shouldBe true
			answer.isCorrect(AnswerSheet(listOf("오답"))) shouldBe false
		}

		"답변 목록을 가져온다" {
			val correct = listOf("답변", "체크")
			val answer = FillBlankAnswer.from(AnswerSheet(correct))

			answer.getAnswers() shouldBe correct
		}
	})