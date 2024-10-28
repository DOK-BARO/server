package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class OXAnswerTest :
	StringSpec({
		"답변의 타입을 확인한다" {
			OXAnswer.from(AnswerSheet(listOf("O"))).getType() shouldBe QuizType.OX
		}

		"답변 일치 여부를 확인한다" {
			val correct = AnswerSheet(listOf("O"))
			OXAnswer.from(correct).isCorrect(correct) shouldBe true
			OXAnswer.from(correct).isCorrect(AnswerSheet(listOf("X"))) shouldBe false
		}

		"답변 포멧이 다르거나, 단일 개수가 아니면 예외를 반환한다" {
			listOf(
				listOf("X "),
				listOf("O "),
				listOf("O", "X"),
				listOf("1"),
				listOf("o"),
				listOf("x"),
			).forEach {
				shouldThrow<IllegalRegisterSheetFormatException> {
					OXAnswer.from(AnswerSheet(it))
				}

				shouldThrow<IllegalSubmitSheetFormatException> {
					OXAnswer.from(AnswerSheet(listOf("O"))).isCorrect(AnswerSheet(it))
				}
			}
		}

		"답변 목록을 가져온다" {
			val correct = AnswerSheet(listOf("O"))
			OXAnswer.from(correct).getAnswers() shouldBe listOf("O")
		}
	})