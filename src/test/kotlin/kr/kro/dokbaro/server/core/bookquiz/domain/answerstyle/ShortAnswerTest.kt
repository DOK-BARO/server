package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class ShortAnswerTest :
	StringSpec({
		"답변의 타입을 확인한다" {
			ShortAnswer.from(AnswerSheet(listOf("답"))).getType() shouldBe QuizType.SHORT
		}

		"답변 일치 여부를 확인한다" {
			ShortAnswer.from(AnswerSheet(listOf("답1"))).isCorrect(AnswerSheet(listOf("답1"))) shouldBe true
			ShortAnswer.from(AnswerSheet(listOf("답1", "답2"))).isCorrect(AnswerSheet(listOf("답2"))) shouldBe true
			ShortAnswer.from(AnswerSheet(listOf("답1"))).isCorrect(AnswerSheet(listOf("답2"))) shouldBe false
		}

		"답변 일치 여부 확인 시 답변이 단일 개수가 아니면 예외를 반환한다" {
			shouldThrow<IllegalSubmitSheetFormatException> {
				ShortAnswer.from(AnswerSheet(listOf("답"))).isCorrect(AnswerSheet(listOf("1", "답")))
			}
		}

		"답변 목록을 가져온다" {
			val answer = listOf("답", "답2")
			ShortAnswer.from(AnswerSheet(answer)).getAnswers() shouldBe answer
		}
	})