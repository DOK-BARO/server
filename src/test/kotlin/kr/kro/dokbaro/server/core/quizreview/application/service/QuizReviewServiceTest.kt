package kr.kro.dokbaro.server.core.quizreview.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.fixture.domain.memberResponseFixture
import java.util.UUID

class QuizReviewServiceTest :
	StringSpec({
		val insertQuizReviewPort = mockk<InsertQuizReviewPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()

		val quizReviewService = QuizReviewService(insertQuizReviewPort, findCertificatedMemberUseCase)

		"퀴즈를 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns memberResponseFixture()
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
	})