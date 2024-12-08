package kr.kro.dokbaro.server.core.quizreview.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.UpdateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.DeleteQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.LoadQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.service.exception.NotFoundQuizReviewException
import kr.kro.dokbaro.server.dummy.EventPublisherDummy
import kr.kro.dokbaro.server.fixture.domain.quizReviewFixture
import java.util.UUID

class QuizReviewServiceTest :
	StringSpec({
		val insertQuizReviewPort = mockk<InsertQuizReviewPort>()
		val loadQuizReviewPort = mockk<LoadQuizReviewPort>()
		val updateQuizReviewPort = UpdateQuizReviewPortStub()
		val deleteQuizReviewPort = mockk<DeleteQuizReviewPort>()

		val quizReviewService =
			QuizReviewService(
				insertQuizReviewPort,
				EventPublisherDummy(),
				loadQuizReviewPort,
				updateQuizReviewPort,
				deleteQuizReviewPort,
			)

		afterEach {
			updateQuizReviewPort.clear()
		}

		"퀴즈를 생성한다" {
			every { insertQuizReviewPort.insert(any()) } returns 1

			quizReviewService.create(
				CreateQuizReviewCommand(
					1,
					3,
					"엄청 어려워요",
					UUID.randomUUID(),
					4,
				),
			) shouldNotBe null
		}

		"퀴즈 후기를 수정한다" {
			every { loadQuizReviewPort.findBy(any()) } returns quizReviewFixture()

			quizReviewService.update(
				UpdateQuizReviewCommand(
					id = 1,
					comment = "",
					starRating = 1,
					difficultyLevel = 1,
				),
			)

			updateQuizReviewPort.storage shouldNotBe null
		}

		"퀴즈 후기 수정 시 id에 해당하는 후기를 찾을 수 없으면 예외를 발생한다" {
			every { loadQuizReviewPort.findBy(any()) } returns null

			shouldThrow<NotFoundQuizReviewException> {
				quizReviewService.update(
					UpdateQuizReviewCommand(
						id = 1,
						comment = "",
						starRating = 1,
						difficultyLevel = 1,
					),
				)
			}
		}

		"퀴즈 후기를 삭제한다" {
			every { deleteQuizReviewPort.deleteBy(any()) } returns Unit

			quizReviewService.delete(4)

			verify { deleteQuizReviewPort.deleteBy(any()) }
		}
	})